package plus.jdk.cli.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandParameter {

    /**
     * 参数名称
     */
    String name();

    /**
     * 参数长名称
     */
    String longName();

    /**
     * 使用示例
     */
    boolean needArgs();

    /**
     * 参数描述
     */
    String description();

    /**
     * 是否必须输入
     */
    boolean required() default false;
}
