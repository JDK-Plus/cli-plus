package plus.jdk.cli.type.adapter;

import com.google.gson.TypeAdapter;

public class LongTypeAdapter implements ITypeAdapter<Long> {
    @Override
    public Long deserialize(String dataStr) {
        if(dataStr == null) {
            return null;
        }
        return Long.parseLong(dataStr);
    }

    @Override
    public String serialize(Long data) {
        if(data == null) {
            return null;
        }
        return String.valueOf(data);
    }
}
