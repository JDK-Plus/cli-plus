package plus.jdk.cli.command;

import plus.jdk.cli.JCommandLinePlus;
import plus.jdk.cli.annotation.CommandLinePlus;
import plus.jdk.cli.annotation.CommandParameter;
import plus.jdk.cli.annotation.SubInstruction;

import java.util.List;
import java.util.Set;

@CommandLinePlus(description = "这是一个测试指令")
public class TestJCommand extends JCommandLinePlus {

    /**
     * 你可以使用required参数指定该参数必须输入
     */
    @CommandParameter(name = "u", longName = "uid", needArgs = true, description = "用户id", required = true)
    private Long uid;

    @CommandParameter(name = "p", longName = "phone", needArgs = true, description = "用户手机号")
    private String phone;

    /**
     * 对于简单类型，你可以使用, -<name> 或 --<longName> 指定参数，例如：-e xxx@jdk.plus, 或 --email xxx@jdk.plus
     */
    @CommandParameter(name = "e", longName = "email", needArgs = true, description = "用户邮箱")
    private String email;

    /**
     * 你可以使用多个选项来指定列表,例如 : -d d1 -d d2 --data d3
     */
    @CommandParameter(name = "d", longName = "data", needArgs = true, description = "参数列表")
    private List<String> dataList;

    /**
     * 你可以使用多个选项来指定列表,例如 : -d d1 -d d2 --data d3
     */
    @CommandParameter(name = "s", longName = "set", needArgs = true, description = "参数集合")
    private Set<String> dataSet;

    /**
     * 针对不需要参数的选项，你可以使用Boolean类型来接收，例如指定 -h 或 --help, 该值会被赋值为true,否则为false
     */
    @CommandParameter(name = "h", longName = "help", needArgs = false, description = "展示帮助信息")
    private Boolean help;

    /**
     * 可以使用 @SubInstruction 注解来定义子指令, 但凡指令中指定了要求执行子指令，那么将直接执行子指令中的逻辑，不再执行当前指令中的任务
     */
    @SubInstruction
    @CommandParameter(name = "sub", longName = "subInstruction", needArgs = false, description = "子指令")
    private TestSubInstruction subInstruction;

    @Override
    public void doInCommand() throws Exception {
        if(help) { // 若指定 -h 或 --help 选项，则展示帮助信息
            showUsage();
            return;
        }

        // 校验参数合法性
        validate();

        // to do something according to Input parameters
        // which has been assigned to a member variable
    }

    public static void main(String[] args) throws Exception {
        TestJCommand testCommand = new TestJCommand();
        testCommand.run(args);
    }
}
