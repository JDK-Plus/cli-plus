package plus.jdk.cli.command;

import plus.jdk.cli.JCommandLinePlus;
import plus.jdk.cli.annotation.CommandLinePlus;
import plus.jdk.cli.annotation.CommandParameter;


@CommandLinePlus(description = "这是一个测试子指令集")
public class TestSubInstruction extends JCommandLinePlus {

    @CommandParameter(name = "p1", longName = "params1", needArgs = true, description = "参数1")
    private String params1;

    @CommandParameter(name = "p2", longName = "params2", needArgs = true, description = "参数2")
    private String params2;

    @CommandParameter(name = "p3", longName = "params3", needArgs = true, description = "参数3")
    private Integer params3;


    @Override
    protected void doInCommand() {

    }
}
