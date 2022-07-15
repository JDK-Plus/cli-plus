package plus.jdk.cli.type.adapter;

public class BooleanTypeAdapter implements ITypeAdapter<Boolean> {
    @Override
    public Boolean deserialize(String dataStr) {
        if(dataStr == null) {
            return false;
        }
        return Boolean.parseBoolean(dataStr);
    }

    @Override
    public String serialize(Boolean data) {
        if(data == null) {
            return "false";
        }
        return data.toString();
    }
}
