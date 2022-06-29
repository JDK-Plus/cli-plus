package plus.jdk.cli.model;

import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

@Data
public class ReflectFieldModel<AnnotationClazz extends Annotation>{

    private AnnotationClazz annotation;

    private Field field;
}
