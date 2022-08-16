package plus.jdk.cli.common;

public class StringUtils {

    public static boolean isEmpty(String data) {
        return data == null || data.length() == 0;
    }
}
