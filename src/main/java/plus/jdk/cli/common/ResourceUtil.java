package plus.jdk.cli.common;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

public class ResourceUtil {

    public static String getResourceContent(String path) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource(path);
        byte[] bytes= FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
        return new String(bytes);
    }
}
