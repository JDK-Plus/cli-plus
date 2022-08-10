package plus.jdk.cli.out;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FontStyle {

    /**
     * 默认
     */
    DEFAULT(0),

    /**
     * 粗体
     */
    BOLD(1),


    /**
     * 斜体
     */
    ITALIC(3),

    /**
     * 下划线
     */
    UNDER_SCORE(4),

    /**
     * 反显
     */
    REVERSE(7),

    /**
     * 反显
     */
    REVERSE2(8),

    /**
     * 删除线
     */
    STRIKETHROUGH(9),
    ;
    private final int style;
}
