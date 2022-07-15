package plus.jdk.cli.type.adapter;

public class StringTypeAdapter implements ITypeAdapter<String>{
    @Override
    public String deserialize(String dataStr) {
        return dataStr;
    }

    @Override
    public String serialize(String data) {
        return data;
    }
}
