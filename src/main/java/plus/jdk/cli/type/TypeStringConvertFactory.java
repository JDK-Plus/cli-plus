package plus.jdk.cli.type;

import plus.jdk.cli.type.adapter.*;

import java.lang.reflect.Type;
import java.util.HashMap;

public class TypeStringConvertFactory {

    private final HashMap<Type, ITypeAdapter> adapterMap = new HashMap<>();

    public TypeStringConvertFactory() {
        registerTypeAdapter(Boolean.class, new BooleanTypeAdapter());
        registerTypeAdapter(String.class, new StringTypeAdapter());
        registerTypeAdapter(Integer.class, new IntegerTypeAdapter());
        registerTypeAdapter(Long.class, new LongTypeAdapter());
    }

    public <Adapter extends ITypeAdapter<?>>
    void registerTypeAdapter(Type clazz, Adapter adapter) {
        adapterMap.put(clazz, adapter);
    }

    public <T> T deserialize(String dataStr, Class<T> clazz) {
        ITypeAdapter<T> adapter = adapterMap.get(clazz);
        if(adapter == null) {
            return null;
        }
        return adapter.deserialize(dataStr);
    }

    public <T> String serialize(T data) {
        ITypeAdapter<T> adapter = adapterMap.get(data.getClass());
        if(adapter == null) {
            return null;
        }
        return adapter.serialize(data);
    }
}
