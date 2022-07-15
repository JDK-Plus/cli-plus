package plus.jdk.cli.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Option {

    /**
     * 短选项
     */
    private String opt;

    /**
     * 长选项
     */
    private String longOpt;

    /**
     * 是否需要参数
     */
    private Boolean hasArg;

    /**
     * 参数描述
     */
    private String description;

    /**
     * 参数类型
     */
    private Field field;
}
