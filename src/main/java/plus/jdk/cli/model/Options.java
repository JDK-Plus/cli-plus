package plus.jdk.cli.model;

import plus.jdk.cli.common.CommandException;
import plus.jdk.cli.type.TypeStringConvertFactory;
import plus.jdk.cli.type.adapter.ITypeAdapter;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;


public class Options {

    private final static TypeStringConvertFactory typeConvertFactory = new TypeStringConvertFactory();

    public static <Adapter extends ITypeAdapter<?>>
    void registerTypeAdapter(Type clazz, Adapter adapter) {
        typeConvertFactory.registerTypeAdapter(clazz, adapter);
    }

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
        if(values == null || values.size() == 0) {
            return null;
        }
        if (List.class.isAssignableFrom(type)) {
            return values;
        }
        if (Set.class.isAssignableFrom(type)) {
            return new LinkedHashSet<>(values);
        }
        return values.get(0);
    }

    public void addOption(String opt, String longOpt, boolean hasArg, String description, Field field) throws CommandException {
        Option option = new Option(opt, longOpt, hasArg, description, field);
        if (longOptOptionsMap.get(longOpt) != null || optOptionsMap.get(opt) != null) {
            throw new CommandException(String.format("-%s or --%s has exist!", opt, longOpt));
        }
        optOptionsMap.put(opt, option);
        longOptOptionsMap.put(longOpt, option);
    }

    public void parse(String[] args, boolean stopAtNonOption) throws CommandException, ClassNotFoundException {
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
            Type type = option.getField().getType();
            if(Set.class.isAssignableFrom(option.getField().getType()) || List.class.isAssignableFrom(option.getField().getType())) {
                type = ((ParameterizedTypeImpl) option.getField().getGenericType()).getActualTypeArguments()[0];
            }
            optValues.get(option.getOpt()).add(parseStringByType(value, type));
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

    private <T> Object parseStringByType(String value, Type type) throws ClassNotFoundException {
        Object data = typeConvertFactory.deserialize(value, Class.forName(type.getTypeName()));
        if(data == null) {
            data = value;
        }
        return data;
    }
}
