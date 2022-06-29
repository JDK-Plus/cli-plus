package plus.jdk.cli.common;

import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import plus.jdk.cli.annotation.CommandLinePlus;
import plus.jdk.cli.model.ReflectFieldModel;

public class ReflectUtil {

    public static Set<Class<?>> scanClassesByPackageName(String packageName, Class<? extends Annotation> annotationClazz) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getTypesAnnotatedWith(annotationClazz);
    }

    public static <AnnotationClazz extends Annotation>
    List<ReflectFieldModel<AnnotationClazz>> getFieldsModelByAnnotation(Object object, Class<AnnotationClazz> annotationClazz) {
        List<ReflectFieldModel<AnnotationClazz>> fieldModelList = new ArrayList<>();
        Field[] fields = object.getClass().getDeclaredFields();
        for(Field field: fields) {
            field.setAccessible(true);
            AnnotationClazz annotation = field.getDeclaredAnnotation(annotationClazz);
            if(annotation == null) {
                continue;
            }
            ReflectFieldModel<AnnotationClazz> reflectFieldModel = new ReflectFieldModel<>();
            reflectFieldModel.setAnnotation(annotation);
            reflectFieldModel.setField(field);
            fieldModelList.add(reflectFieldModel);
        }
        return fieldModelList;
    }

    public static void main(String[] args) {
        scanClassesByPackageName("com.weibo", CommandLinePlus.class);
    }
}
