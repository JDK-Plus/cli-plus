package plus.jdk.cli.annotation;

import java.lang.annotation.*;

@Inherited
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableColumnName {

    /**
     * @return 列名
     */
    String value();
}
