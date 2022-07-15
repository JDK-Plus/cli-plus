package plus.jdk.cli.type.adapter;


public class IntegerTypeAdapter implements ITypeAdapter<Integer> {
    @Override
    public Integer deserialize(String dataStr) {
        if (dataStr == null) {
            return null;
        }
        return Integer.parseInt(dataStr);
    }

    @Override
    public String serialize(Integer data) {
        if (data == null) {
            return null;
        }
        return String.valueOf(data);
    }
}
