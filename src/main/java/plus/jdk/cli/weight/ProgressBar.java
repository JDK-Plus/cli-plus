package plus.jdk.cli.weight;

import lombok.Getter;

import java.util.Collections;

@Getter
public class ProgressBar {

    /**
     * 任务的总数，即需要完成的任务数
     */
    private final Integer total;

    /**
     * width表示进度条的宽度，即进度条在命令行中占用的字符数
     */
    private final Integer width;

    /**
     * 进度条左侧的描述信息
     */
    private String lMessage = "Processing";

    /**
     * 进度条右侧的描述信息
     */
    private String rMessage = "";

    public ProgressBar(int total, int width, String message) {
        this.total = total;
        this.width = width;
        this.lMessage = message;
    }

    public void update(int progress, String lMessage, String rMessage) {
        this.rMessage = rMessage;
        this.lMessage = lMessage;
        int completed = (int) Math.round(((double) progress / total) * width);
        String bar = String.join("", Collections.nCopies(completed, "=")) + ">"
                + String.join("", Collections.nCopies(width - completed, " "));
        String percent = String.format("%.0f", ((double) progress / total) * 100);
        StringBuilder builder = new StringBuilder("\r");
        if(lMessage != null) {
            builder.append(lMessage);
        }
        builder.append(" [").append(bar).append("] ").append(percent).append("% ");
        if(rMessage != null) {
            builder.append(rMessage);
        }
        System.out.print(builder);
        if (progress == total) {
            System.out.println();
        }
    }

    public void update(int progress) {
        update(progress, lMessage, rMessage);
    }

    public static void main(String[] args) {
        int total = 512;
        ProgressBar progressBar = new ProgressBar(total, 40, "Processing");
        for (int i = 0; i <= total; i++) {
            progressBar.update(i, "Processing", String.format("%s/%s", i, total));
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
