package plus.jdk.cli.model;

import lombok.Data;
import plus.jdk.cli.annotation.PropertiesValue;

@Data
public class CliHelpModel {

    /**
     * header欢迎使用的标题
     */
    @PropertiesValue("plus.jdk.help.header.welcome")
    private String headerWelcome;

    /**
     * 想要展示的banner信息, 你可以使用 resource 和 path字段指定要输出的banner位置
     */
    @PropertiesValue(value = "plus.jdk.help.header.banner", resource = true, path = "banner/banner.txt")
    private String banner;

    /**
     * header描述信息
     */
    @PropertiesValue("plus.jdk.help.header.description")
    private String headerDesc;

    /**
     * 底部描述信息
     */
    @PropertiesValue("plus.jdk.help.footer.description")
    private String footerDesc;

    /**
     * 联系信息
     */
    @PropertiesValue("plus.jdk.help.footer.contact")
    private String footerContact;
}
