package plus.jdk.cli.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 字体颜色
 */
@Getter
@AllArgsConstructor
public enum FontColor {

    /**
     * 默认颜色
     */
    DEFAULT(0),

    /**
     * 黑色=30
     */
    BLACK(30),
    /**
     * 红色=31
     */
    RED(31),
    /**
     * 绿色=32
     */
    GREEN(32),
    /**
     * 黄色=33
     */
    YELLOW(33),
    /**
     * 蓝色=34
     */
    BLUE(34),
    /**
     * 紫色=35
     */
    MAGENTA(35),
    /**
     * 天蓝色（青色）=36
     */
    CYAN(36),
    /**
     * 白色=37
     */
    WHITE(37),
    ;

    private final Integer escapeCode;
}
