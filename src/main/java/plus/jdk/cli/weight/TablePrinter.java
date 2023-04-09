package plus.jdk.cli.weight;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TablePrinter {

    public static void printTable(List<List<String>> data) {
        // 计算每列最大宽度
        int numColumns = data.get(0).size();
        int[] maxColumnWidths = new int[numColumns];
        for (List<String> row : data) {
            for (int i = 0; i < numColumns; i++) {
                String cell = row.get(i);
                int cellWidth = cell.codePoints().map(c -> isChineseCharacter(c) ? 2 : 1).sum();
                if (cellWidth > maxColumnWidths[i]) {
                    maxColumnWidths[i] = cellWidth;
                }
            }
        }

        // 输出表格
        printLine(maxColumnWidths);
        for (List<String> row : data) {
            System.out.print("| ");
            for (int i = 0; i < numColumns; i++) {
                String cell = row.get(i);
                int cellWidth = cell.codePoints().map(c -> isChineseCharacter(c) ? 2 : 1).sum();
                String paddedCell = padRight(cell, maxColumnWidths[i] - cellWidth + cell.length());
                System.out.print(paddedCell + " | ");
            }
            System.out.println();
        }
        printLine(maxColumnWidths);
    }

    private static boolean isChineseCharacter(int codePoint) {
        return Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN;
    }

    private static String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private static void printLine(int[] maxColumnWidths) {
        System.out.print("+");
        for (int i = 0; i < maxColumnWidths.length; i++) {
            System.out.print(repeat("-", maxColumnWidths[i] + 2));
            System.out.print("+");
        }
        System.out.println();
    }

    private static String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        List<List<String>> data = new ArrayList<>();
        data.add(Arrays.asList("id", "name", "age", "gender"));
        data.add(Arrays.asList("1", "张三", "20", "男"));
        data.add(Arrays.asList("2", "李四", "22", "女"));
        data.add(Arrays.asList("3", "王五", "18", "男"));
        printTable(data);
    }
}
