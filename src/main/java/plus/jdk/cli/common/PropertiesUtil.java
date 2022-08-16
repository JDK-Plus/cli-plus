package plus.jdk.cli.common;

import com.google.gson.Gson;
import lombok.Setter;
import plus.jdk.cli.annotation.PropertiesValue;
import plus.jdk.cli.model.CliHelpModel;
import plus.jdk.cli.model.ReflectFieldModel;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class PropertiesUtil {

    @Setter
    private static Gson gson = new Gson();

    /**
     * 读取.properties文件
     * @param path 配置路径
     * @param resource 是否在resource目录下，否则从当前系统目录读取
     */
    public static <T> T initializationConfig(T obj, String path, Boolean resource) throws IOException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Properties properties = new Properties();
        InputStream inputStream;
        if(resource) {
            inputStream = obj.getClass().getClassLoader().getResourceAsStream(path);
        }else {
            inputStream = new FileInputStream(path);
        }
        properties.load(inputStream);
        List<ReflectFieldModel<PropertiesValue>> reflectFieldModels =
                ReflectUtil.getFieldsModelByAnnotation(obj, PropertiesValue.class);
        for (ReflectFieldModel<PropertiesValue> reflectFieldModel : reflectFieldModels) {
            Field field = reflectFieldModel.getField();
            PropertiesValue propertiesValue = reflectFieldModel.getAnnotation();
            String data = properties.getProperty(propertiesValue.value());
            if(propertiesValue.resource()) {
                data = ResourceUtil.getResourceContent(propertiesValue.path());
            }
            String fieldName = field.getName();
            field.set(obj, data);
        }
        return obj;
    }
}
