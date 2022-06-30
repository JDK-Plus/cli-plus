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
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class PropertiesUtil {

    @Setter
    private static Gson gson = new Gson();

    /**
     * 读取.properties文件
     * @param clazz 配置类示例
     * @param path 配置路径
     * @param resource 是否在resource目录下，否则从当前系统目录读取
     */
    public static <T> T initializationConfig(Class<T> clazz, String path, Boolean resource) throws IOException, IllegalAccessException, InstantiationException {
        Properties properties = new Properties();
        InputStream inputStream;
        if(resource) {
            inputStream = clazz.getClassLoader().getResourceAsStream(path);
        }else {
            inputStream = new FileInputStream(path);
        }
        properties.load(inputStream);
        List<ReflectFieldModel<PropertiesValue>> reflectFieldModels =
                ReflectUtil.getFieldsModelByAnnotation(clazz.newInstance(), PropertiesValue.class);
        HashMap<String, String> dataMap = new HashMap<>();
        for (ReflectFieldModel<PropertiesValue> reflectFieldModel : reflectFieldModels) {
            Field field = reflectFieldModel.getField();
            PropertiesValue propertiesValue = reflectFieldModel.getAnnotation();
            String data = properties.getProperty(propertiesValue.value());
            if(propertiesValue.resource()) {
                data = ResourceUtil.getResourceContent(propertiesValue.path());
            }
            String fieldName = field.getName();
            dataMap.put(fieldName, data);
        }
        return gson.fromJson(gson.toJson(dataMap), clazz);
    }
}
