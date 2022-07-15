package plus.jdk.cli.type.adapter;

public interface ITypeAdapter<T> {

    /**
     * 反序列化
     */
    T deserialize(String dataStr);

    /**
     * 序列化
     */
    String serialize(T data);
}
