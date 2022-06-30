package plus.jdk.cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ArgHelpInfo {

    /**
     * 参数格式
     */
    private String argsFormat;

    /**
     * 参数帮助信息
     */
    private String argHelpInfo;
}
