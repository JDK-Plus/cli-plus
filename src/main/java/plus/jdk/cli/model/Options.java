package plus.jdk.cli.model;

import plus.jdk.cli.common.CommandException;

import java.util.*;


public class Options {

    /**
     * 按照opt作为key存储option列表
     */
    private final HashMap<String, Option> optOptionsMap = new HashMap<>();

    /**
     * 按照longOpt作为key存储option列表
     */
    private final HashMap<String, Option> longOptOptionsMap = new HashMap<>();

    /**
     * 记录该option是否出现过
     */
    private final HashMap<String, Boolean> hasOptionMap = new HashMap<>();

    /**
     * 输入的参数值列表, 存储以opt为主
     */
    private final HashMap<String, List<Object>> optValues = new HashMap<>();

    /**
     * 判定是否输入该key
     */
    public boolean hasOption(String opt) {
        return hasOptionMap.get(opt) != null;
    }

    public Object getOptionValue(String opt, Class<?> type) {
        Option option = optOptionsMap.get(opt);
        if (option == null) {
            option = longOptOptionsMap.get(opt);
        }
        if (option == null) {
            return null;
        }
        List<Object> values = optValues.get(opt);
        if (List.class.isAssignableFrom(type)) {
            return values;
        }
        if (Set.class.isAssignableFrom(type)) {
            return new LinkedHashSet<>(values);
        }
        if(values.size() == 0) {
            return null;
        }
        return values.get(0);
    }

    public void addOption(String opt, String longOpt, boolean hasArg, String description, Class<?> type) throws CommandException {
        Option option = new Option(opt, longOpt, hasArg, description, type);
        if (longOptOptionsMap.get(longOpt) != null || optOptionsMap.get(opt) != null) {
            throw new CommandException(String.format("-%s or --%s has exist!", opt, longOpt));
        }
        optOptionsMap.put(opt, option);
        longOptOptionsMap.put(longOpt, option);
    }

    public void parse(String[] args, boolean stopAtNonOption) throws CommandException {
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            Option option = getOption(arg);
            if (option == null) {
                continue;
            }
            // 记录该选项有输入
            hasOptionMap.put(option.getOpt(), true);
            hasOptionMap.put(option.getLongOpt(), true);

            optValues.putIfAbsent(option.getOpt(), new ArrayList<>());
            int valueIndex = Math.min(i + 1, args.length - 1);
            String value = args[valueIndex];
            if (!option.getHasArg()) { // 不需要参数，继续下一轮判断
                continue;
            }
            if (value.startsWith("-")) {
                throw new CommandException(String.format("-%s or --%s must specify parameters", option.getOpt(), option.getLongOpt()));
            }
            optValues.get(option.getOpt()).add(parseStringByType(value, option.getType()));
        }
    }

    private Option getOption(String arg) {
        Option option = null;
        if (arg.startsWith("--")) { // 长选项
            option = longOptOptionsMap.get(arg.substring(2));
        }
        if (option == null && arg.startsWith("-")) { //短选项
            option = optOptionsMap.get(arg.substring(1));
        }
        return option;
    }

    private <T> Object parseStringByType(String value, Class<T> clazz) {
        if (String.class.equals(clazz)) {
            return String.valueOf(value);
        }
        if (Integer.class.equals(clazz)) {
            return Integer.parseInt(value);
        }
        if (Boolean.class.equals(clazz)) {
            return Boolean.parseBoolean(value);
        }
        if (Long.class.equals(clazz)) {
            return Long.parseLong(value);
        }
        return value;
    }
}
