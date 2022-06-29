package plus.jdk.cli.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PropertiesValue {

    /**
     * 配置在properties中的路径
     */
    String value();

    /**
     * 标注是否从资源文件中初始化该字段
     * 若是，则从resource中的path指定的文件中读取内容
     */
    boolean resource() default false;

    /**
     * 指定资源路径
     */
    String path() default "";
}
