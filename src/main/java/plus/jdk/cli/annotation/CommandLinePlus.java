package plus.jdk.cli.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandLinePlus {


    /**
     * 指令说明
     */
    String description();

    /**
     * 使用示例
     */
    String usage() default "";

    /**
     * 帮助信息
     */
    String help() default "";
}
