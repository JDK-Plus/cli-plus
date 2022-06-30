# 说明
一个简单的基于java版本的命令行编写工具库

详细的使用说明请参见: https://jdk.plus/pages/2ba02f/

## 如何引入

```xml
<dependency>
    <groupId>plus.jdk</groupId>
    <artifactId>cli-plus</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 定义指令并指定参数

**如何定义一个指令：**

```java
package plus.jdk.cli.command;

import plus.jdk.cli.JCommandLinePlus;
import plus.jdk.cli.annotation.CommandLinePlus;
import plus.jdk.cli.annotation.CommandParameter;
import plus.jdk.cli.annotation.SubInstruction;

@CommandLinePlus(description = "这是一个测试指令")
public class TestJCommand extends JCommandLinePlus {

    @CommandParameter(name = "u", longName = "uid", needArgs = true, description = "用户id")
    private Long uid;

    @CommandParameter(name = "p", longName = "phone", needArgs = true, description = "用户手机号")
    private String phone;

    @CommandParameter(name = "e", longName = "email", needArgs = true, description = "用户邮箱")
    private String email;

    @CommandParameter(name = "h", longName = "help", needArgs = false, description = "展示帮助信息")
    private Boolean help;

    /**
     * 可以以这种形式来指定子指令
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
        // to do something according to Input parameters
        // which has been assigned to this class member variable
    }

    public static void main(String[] args) throws Exception {
        TestJCommand testCommand = new TestJCommand();
        testCommand.run(args);
    }
}
```

**执行**

```bash
java -jar xxx.jar -u 123567 -p p2data --email xxx@jdk.plus -h
```

**配置文件**

你需要在你的工程`resources`目录下指定`cli-plus.properties`

```bash
plus.jdk.help.header.welcome=Welcome to use cli-plus
plus.jdk.help.header.description=The command options are described below:
plus.jdk.help.footer.description=usage: xxx-tool -cn xx -e "ls /root"
plus.jdk.help.footer.contact=mail: moon@jdk.plus
```

**帮助信息**

调用封装好的`showUsage`方法可以生成帮助信息并打印，示例如下：

![](https://github.com/JDK-Plus/cli-plus/blob/master/src/main/resources/demo.png)

**指令shell封装**

```shell
#!/bin/bash

# 指定运行时的JAVA_HOME
#JAVA_HOME=${HOME}/.biz-tools/java-se-8u41-ri
#PATH=${JAVA_HOME}/bin:$PATH

if ! which java > /dev/null ; then
    echo "jdk 未安装, 请安装1.8以上版本"
    exit
fi

JAVA_VERSION=$(java -version 2>&1 | sed '1!d' | sed -e 's/"//g' | awk '{print $3}')

if [[ ! "${JAVA_VERSION}" =~ ^1.8.0.* ]]; then
    echo "jdk版本必须大于等于1.8，请检查环境配置"
    exit
fi

TOOLS_JAR=$(dirname "$0")/tools.jar

# shellcheck disable=SC2046
java -jar "${TOOLS_JAR}" "$@" -c $(dirname "$0")/conf/config.properties
```
