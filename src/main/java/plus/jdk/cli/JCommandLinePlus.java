package plus.jdk.cli;

import com.google.gson.Gson;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.*;
import plus.jdk.cli.annotation.CommandLinePlus;
import plus.jdk.cli.annotation.CommandParameter;
import plus.jdk.cli.annotation.SubInstruction;
import plus.jdk.cli.common.ReflectUtil;
import plus.jdk.cli.model.ReflectFieldModel;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


@Slf4j
@Data
@CommandLinePlus(description = "这是一个测试指令")
public abstract class JCommandLinePlus {


    public JCommandLinePlus() {
    }

    public final void run(String[] args) throws ParseException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        List<ReflectFieldModel<CommandParameter>> parameterModels = ReflectUtil.getFieldsModelByAnnotation(this, CommandParameter.class);
        Options options = buildOptions(parameterModels);
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = parser.parse(options, args, true);
        buildParameters(commandLine, parameterModels);
        if(!doFirstSubInstruction(parameterModels, commandLine, args)) {
            doInCommand();
        }
    }

    private Options buildOptions(List<ReflectFieldModel<CommandParameter>> parameterModelList) throws IllegalAccessException {
        Options options = new Options();
        for (ReflectFieldModel<CommandParameter> fieldModel : parameterModelList) {
            CommandParameter commandParameter = fieldModel.getAnnotation();
            boolean needArgs = commandParameter.needArgs();
            if(isSubInstruction(fieldModel.getField())) {
                needArgs = false;
            }
            options.addOption(commandParameter.name(), commandParameter.longName(), needArgs, commandParameter.description());
        }
        return options;
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

    private Gson gson = new Gson();

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

    /**
     * 你可以根据你输入的参数来执行你的任务
     */
    protected abstract void doInCommand();
}
