package plus.jdk.cli.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BackgroundColor {

    /**
     * 黑色
     */
    BLACK(40),

    /**
     * 红色
     */
    RED(41),

    /**
     * 绿色
     */
    GREEN(42),

    /**
     * 黄色
     */
    YELLOW(43),

    /**
     * 蓝色
     */
    BLUE(44),

    /**
     * 紫色
     */
    MAGENTA(45),

    /**
     * 天蓝色（青色
     */
    CYAN(46),


    /**
     * 白色
     */
    WHITE(47),
    ;
    private final Integer escapeCode;
}
