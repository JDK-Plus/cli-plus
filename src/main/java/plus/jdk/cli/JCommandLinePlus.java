package plus.jdk.cli;

import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import plus.jdk.cli.annotation.CommandLinePlus;
import plus.jdk.cli.annotation.CommandParameter;
import plus.jdk.cli.annotation.SubInstruction;
import plus.jdk.cli.common.ReflectUtil;
import plus.jdk.cli.model.CliHelpModel;
import plus.jdk.cli.model.ReflectFieldModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static plus.jdk.cli.common.PropertiesUtil.initializationConfig;


@Data
@CommandLinePlus(description = "这是一个测试指令")
public abstract class JCommandLinePlus {

    protected static CliHelpModel cliHelpModel;

    static {
        try{
            cliHelpModel = initializationConfig(CliHelpModel.class, "help.properties");
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Setter
    private Gson gson = new Gson();

    public JCommandLinePlus() {

    }

    public final void run(String[] args) throws ParseException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        List<ReflectFieldModel<CommandParameter>> parameterModels = ReflectUtil.getFieldsModelByAnnotation(this, CommandParameter.class);
        Options options = buildOptions(parameterModels);
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, true);
        buildParameters(commandLine, parameterModels);
        if(!doFirstSubInstruction(parameterModels, commandLine, args)) {
            if(isShowUsage()) { // 若输入不合法，则展示帮助信息
                showUsage();
                return;
            }
            doInCommand();
        }
    }

    private Options buildOptions(List<ReflectFieldModel<CommandParameter>> parameterModelList) throws IllegalAccessException {
        Options options = new Options();
        for (ReflectFieldModel<CommandParameter> fieldModel : parameterModelList) {
            CommandParameter commandParameter = fieldModel.getAnnotation();
            boolean needArgs = fieldNeedArgs(fieldModel);
            options.addOption(commandParameter.name(), commandParameter.longName(), needArgs, commandParameter.description());
        }
        return options;
    }

    private boolean fieldNeedArgs(ReflectFieldModel<CommandParameter> reflectFieldModel) throws IllegalAccessException {
        CommandParameter commandParameter = reflectFieldModel.getAnnotation();
        boolean needArgs = commandParameter.needArgs();
        if(isSubInstruction(reflectFieldModel.getField())) {
            needArgs = false;
        }
        return needArgs;
    }

    /**
     * 获取第一个子指令并获取其下标
     */
    private boolean doFirstSubInstruction(List<ReflectFieldModel<CommandParameter>> fieldModels, CommandLine commandLine, String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException, ParseException {
        for (ReflectFieldModel<CommandParameter> fieldModel : fieldModels) {
            Field field = fieldModel.getField();
            CommandParameter commandParameter = fieldModel.getAnnotation();
            if(isSubInstruction(field)) {
                if (!(commandLine.hasOption(commandParameter.name()) || commandLine.hasOption(commandParameter.longName()))) {
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
     * 判断某个字段是否为子指令
     */
    private boolean isSubInstruction(Field field) throws IllegalAccessException {
        SubInstruction subInstruction = field.getDeclaredAnnotation(SubInstruction.class);
        field.setAccessible(true);
        return subInstruction != null && JCommandLinePlus.class.isAssignableFrom(field.getType());
    }

    private void buildParameters(CommandLine commandLine, List<ReflectFieldModel<CommandParameter>> parameterModelList) throws IllegalAccessException {
        for (ReflectFieldModel<CommandParameter> fieldModel : parameterModelList) {
            CommandParameter commandParameter = fieldModel.getAnnotation();
            Field field = fieldModel.getField();
            field.setAccessible(true);
            if (commandLine.hasOption(commandParameter.name()) || commandLine.hasOption(commandParameter.longName())) {
                String value = commandLine.getOptionValue(commandParameter.name());
                field.set(this, gson.fromJson(value, field.getType()));
            }
        }
    }

    protected void showUsage() throws IllegalAccessException {
        println("\t", cliHelpModel.getHeaderWelcome());
        println("\t", cliHelpModel.getHeaderDesc());
        println(cliHelpModel.getBanner());
        println("\t", cliHelpModel.getHeaderDesc());
        List<ReflectFieldModel<CommandParameter>> parameterModels = ReflectUtil.getFieldsModelByAnnotation(this, CommandParameter.class);
        for(ReflectFieldModel<CommandParameter> reflectFieldModel: parameterModels) {
            CommandParameter commandParameter = reflectFieldModel.getAnnotation();
            List<String> commandDescList = new ArrayList<>();
            commandDescList.add(String.format("-%s", commandParameter.name()));
            commandDescList.add(",");
            commandDescList.add(String.format("--%s", commandParameter.longName()));
            commandDescList.add(" ");
            boolean needArgs = fieldNeedArgs(reflectFieldModel);
            commandDescList.add(needArgs ? "<arg>" : " ");
            commandDescList.add("\t");
            commandDescList.add(commandParameter.description());
            println("\t", "\t",  String.join("", commandDescList));
        }
        println("\t", cliHelpModel.getFooterDesc());
        println("\t", cliHelpModel.getFooterContact());
    }

    protected void println(String... contents) {
        System.out.println(String.join("", contents));
    }

    /**
     * 你可以根据你输入的参数来执行你的任务
     */
    protected abstract void doInCommand();

    /**
     * 是否展示帮助信息，子类实现。
     * 若参数输入有误，则返回true展示帮助信息即可
     */
    protected boolean isShowUsage() {
        return false;
    };
}
