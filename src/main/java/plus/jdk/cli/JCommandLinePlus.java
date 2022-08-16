package plus.jdk.cli;

import com.google.gson.Gson;
import lombok.Data;
import lombok.Setter;
import plus.jdk.cli.annotation.CommandLinePlus;
import plus.jdk.cli.annotation.CommandParameter;
import plus.jdk.cli.annotation.SubInstruction;
import plus.jdk.cli.common.CommandException;
import plus.jdk.cli.common.ReflectUtil;
import plus.jdk.cli.common.StringUtils;
import plus.jdk.cli.model.ArgHelpInfo;
import plus.jdk.cli.model.CliHelpModel;
import plus.jdk.cli.model.Options;
import plus.jdk.cli.model.ReflectFieldModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static plus.jdk.cli.common.PropertiesUtil.initializationConfig;


@Data
@CommandLinePlus(description = "这是一个测试指令")
public abstract class JCommandLinePlus {

    protected static CliHelpModel cliHelpModel = new CliHelpModel();

    static {
        try {
            cliHelpModel = initializationConfig(cliHelpModel, "cli-plus.properties", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Setter
    private Gson gson = new Gson();

    private Options options;

    private List<ReflectFieldModel<CommandParameter>> parameterModels;

    public JCommandLinePlus() {

    }

    public final void run(String[] args) throws Exception {
        try{
            parameterModels = ReflectUtil.getFieldsModelByAnnotation(this, CommandParameter.class);
            options = buildOptions();
            options.parse(args, true);
            buildParameters();
            if (!doFirstSubInstruction(args)) {
                doInCommand();
            }
        }catch (CommandException e) {
            println("\t", e.getMessage());
        }
    }

    private Options buildOptions() throws IllegalAccessException, CommandException {
        Options options = new Options();
        for (ReflectFieldModel<CommandParameter> fieldModel : parameterModels) {
            Field field = fieldModel.getField();
            CommandParameter commandParameter = fieldModel.getAnnotation();
            boolean needArgs = fieldNeedArgs(fieldModel);
            options.addOption(commandParameter.name(), commandParameter.longName(), needArgs, commandParameter.description(), field);
        }
        return options;
    }

    private boolean fieldNeedArgs(ReflectFieldModel<CommandParameter> reflectFieldModel) throws IllegalAccessException {
        CommandParameter commandParameter = reflectFieldModel.getAnnotation();
        boolean needArgs = commandParameter.needArgs();
        if (isSubInstruction(reflectFieldModel.getField())) {
            needArgs = false;
        }
        return needArgs;
    }

    /**
     * 获取第一个子指令并获取其下标
     */
    private boolean doFirstSubInstruction(String[] args) throws Exception {
        for (ReflectFieldModel<CommandParameter> fieldModel : parameterModels) {
            Field field = fieldModel.getField();
            CommandParameter commandParameter = fieldModel.getAnnotation();
            if (isSubInstruction(field)) {
                if (!(options.hasOption(commandParameter.name()) || options.hasOption(commandParameter.longName()))) {
                    continue;
                }
                field.setAccessible(true);
                JCommandLinePlus jCommandLinePlus = (JCommandLinePlus) (field.getType().getConstructor().newInstance());
                jCommandLinePlus.run(args);
                return true;
            }
        }
        return false;
    }

    /**
     * 交由子类来完成调用，校验参数是否合法，支持重写
     */
    protected void validate() throws IllegalAccessException, CommandException {
        for (ReflectFieldModel<CommandParameter> fieldModel : parameterModels) {
            CommandParameter commandParameter = fieldModel.getAnnotation();
            Field field = fieldModel.getField();
            field.setAccessible(true);
            boolean needArgs = fieldNeedArgs(fieldModel);
            Object value = options.getOptionValue(commandParameter.name(), field.getType());
            String optName = commandParameter.name(), optLongName = commandParameter.longName();
            if(needArgs && commandParameter.required() && value == null) {
                throw new CommandException(String.format("arg(-%s or --%s) must be input", optName, optLongName));
            }
        }
    }

    /**
     * 判断某个字段是否为子指令
     */
    private boolean isSubInstruction(Field field) throws IllegalAccessException {
        SubInstruction subInstruction = field.getDeclaredAnnotation(SubInstruction.class);
        field.setAccessible(true);
        return subInstruction != null && JCommandLinePlus.class.isAssignableFrom(field.getType());
    }

    private void buildParameters() throws IllegalAccessException, CommandException {
        for (ReflectFieldModel<CommandParameter> fieldModel : parameterModels) {
            CommandParameter commandParameter = fieldModel.getAnnotation();
            Field field = fieldModel.getField();
            field.setAccessible(true);
            boolean needArgs = fieldNeedArgs(fieldModel);
            boolean hasOption = options.hasOption(commandParameter.name());
            Object value = options.getOptionValue(commandParameter.name(), field.getType());
//            String optName = commandParameter.name(), optLongName = commandParameter.longName();
            if(hasOption && needArgs && value == null) {
                continue;
//                throw new CommandException(String.format("invalid arg(-%s or --%s) value", optName, optLongName));
            }
            if (hasOption && needArgs) {
                field.set(this, value);
            }
            if (!needArgs) { // 不需要参数的选项
                if (field.getType() == Boolean.class) {
                    field.set(this, hasOption);
                    continue;
                }
                if ((field.getType() == Integer.class)) {
                    field.set(this, hasOption? 1 : 0);
                }
            }
        }
    }

    protected void showUsage() throws IllegalAccessException {
        println("\t", cliHelpModel.getHeaderWelcome());
        println("\t", cliHelpModel.getHeaderDesc());
        println(cliHelpModel.getBanner());
        CommandLinePlus commandLinePlus = this.getClass().getDeclaredAnnotation(CommandLinePlus.class);
        if(commandLinePlus != null) {
            println("\t", commandLinePlus.description());
        }
        println("\t", cliHelpModel.getHeaderDesc());
        List<ReflectFieldModel<CommandParameter>> parameterModels = ReflectUtil.getFieldsModelByAnnotation(this, CommandParameter.class);
        int maxArgsInfoLen = 0;
        List<ArgHelpInfo> argHelpInfos = new ArrayList<>();
        for (ReflectFieldModel<CommandParameter> reflectFieldModel : parameterModels) {
            CommandParameter commandParameter = reflectFieldModel.getAnnotation();
            List<String> commandDescList = new ArrayList<>();
            commandDescList.add(String.format("-%s", commandParameter.name()));
            commandDescList.add(",");
            commandDescList.add(String.format("--%s", commandParameter.longName()));
            commandDescList.add(" ");
            boolean needArgs = fieldNeedArgs(reflectFieldModel);
            commandDescList.add(needArgs ? "<arg>" : " ");
            String argsInfo = String.join("", commandDescList);
            maxArgsInfoLen = Math.max(maxArgsInfoLen, argsInfo.length());
            argHelpInfos.add(new ArgHelpInfo(argsInfo, commandParameter.description()));
        }
        for (ArgHelpInfo argHelpInfo : argHelpInfos) {
            StringBuilder builder = new StringBuilder(argHelpInfo.getArgsFormat());
            while (builder.length() < maxArgsInfoLen) {
                builder.append(" ");
            }
            println("\t   ", builder.toString(), "  ", argHelpInfo.getArgHelpInfo());
        }
        if(commandLinePlus != null && StringUtils.isEmpty(commandLinePlus.usage())) {
            println("\t", commandLinePlus.usage());
        }
        if(StringUtils.isEmpty(cliHelpModel.getFooterDesc())) {
            println("\t", cliHelpModel.getFooterDesc());
        }
        if(StringUtils.isEmpty(cliHelpModel.getFooterContact())) {
            println("\t", cliHelpModel.getFooterContact());
        }
    }

    protected void println(String... contents) {
        System.out.println(String.join("", contents));
    }

    /**
     * 你可以根据你输入的参数来执行你的任务
     */
    protected abstract void doInCommand() throws Exception;
}
