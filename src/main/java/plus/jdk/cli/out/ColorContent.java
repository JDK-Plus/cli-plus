package plus.jdk.cli.out;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * "\033[字体效果;字体颜色;背景颜色m你的字符（输出的字符）\033[0m"
 */

@Setter
@NoArgsConstructor
public class ColorContent {

    private FontStyle fontStyle = FontStyle.DEFAULT;

    private FontColor fontColor = FontColor.DEFAULT;

    private BackgroundColor backgroundColor = BackgroundColor.BLACK;

    private String content;

    public ColorContent(String content) {
        this.content = content;
    }

    public String toString() {
        return String.format("\033[%d;%d;%dm%s\033[0m",
                fontStyle.getStyle(), fontColor.getEscapeCode(), backgroundColor.getEscapeCode(), content);
    }

    public static void main(String[] args) throws Exception {
        ColorContent colorContent = new ColorContent("哈哈哈哈哈哈😄😄");
        for (FontStyle fontStyle : FontStyle.values()) {
            for (FontColor fontColor : FontColor.values()) {
                for (BackgroundColor backgroundColor : BackgroundColor.values()) {
                    colorContent.setFontStyle(fontStyle);
                    colorContent.setFontColor(fontColor);
                    colorContent.setBackgroundColor(backgroundColor);
                    System.out.println(colorContent);
                }
            }
        }
    }
}
