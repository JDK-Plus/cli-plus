package plus.jdk.cli.common;

import com.google.gson.Gson;
import plus.jdk.cli.annotation.PropertiesValue;
import plus.jdk.cli.model.HelpInfoModel;
import plus.jdk.cli.model.ReflectFieldModel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class PropertiesUtil {

    private static final Gson gson = new Gson();

    public static <T> T initializationConfig(Class<T> clazz, String path) throws IOException, IllegalAccessException, InstantiationException {
        Properties properties = new Properties();
        InputStream resourceStream = clazz.getClassLoader().getResourceAsStream(path);
        properties.load(resourceStream);
        List<ReflectFieldModel<PropertiesValue>> reflectFieldModels =
                ReflectUtil.getFieldsModelByAnnotation(clazz.newInstance(), PropertiesValue.class);
        HashMap<String, String> dataMap = new HashMap<>();
        for (ReflectFieldModel<PropertiesValue> reflectFieldModel : reflectFieldModels) {
            Field field = reflectFieldModel.getField();
            PropertiesValue propertiesValue = reflectFieldModel.getAnnotation();
            String data = properties.getProperty(propertiesValue.value());
            String fieldName = field.getName();
            dataMap.put(fieldName, data);
        }
        return gson.fromJson(gson.toJson(dataMap), clazz);
    }

    public static void main(String[] args) throws IOException, IllegalAccessException, InstantiationException {
        HelpInfoModel helpInfoModel = initializationConfig(HelpInfoModel.class, "help.properties");
        System.out.println("xxxxxx");
    }

}
