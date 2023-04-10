package plus.jdk.cli.weight;

import org.springframework.util.CollectionUtils;
import plus.jdk.cli.annotation.TableColumnName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class TablePrinter {

    public <T> void printTable(List<T> data, Class<T> clazz) {
        if (CollectionUtils.isEmpty(data)) {
            return;
        }
        // 获取表头和数据
        List<List<String>> rows = new ArrayList<>();
        List<String> headerRow = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String headerName = field.getName();
            TableColumnName tableColumnName = field.getDeclaredAnnotation(TableColumnName.class);
            if (tableColumnName != null) {
                headerName = tableColumnName.value();
            }
            headerRow.add(headerName);
        }
        rows.add(headerRow);
        for (T item : data) {
            List<String> dataRow = new ArrayList<>();
            for (Field field : fields) {
                try {
                    field.setAccessible(true);
                    Object value = field.get(item);
                    dataRow.add(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            rows.add(dataRow);
        }

        // 计算每列最大宽度
        int numColumns = rows.get(0).size();
        int[] maxColumnWidths = new int[numColumns];
        for (List<String> row : rows) {
            for (int i = 0; i < numColumns; i++) {
                String cell = row.get(i);
                int cellWidth = getCellWidth(cell);
                if (cellWidth > maxColumnWidths[i]) {
                    maxColumnWidths[i] = cellWidth;
                }
            }
        }

        // 输出表格
        printLine(maxColumnWidths);
        for (List<String> row : rows) {
            System.out.print("| ");
            for (int i = 0; i < numColumns; i++) {
                String cell = row.get(i);
                int cellWidth = getCellWidth(cell);
                String paddedCell = padRight(cell, maxColumnWidths[i] - cellWidth + cell.length());
                System.out.print(paddedCell + " | ");
            }
            System.out.println();
            if (row == headerRow) {
                printLine(maxColumnWidths);
            }
        }
        printLine(maxColumnWidths);
    }

    private int getCellWidth(String cell) {
        int cellWidth = 0;
        for (int i = 0; i < cell.length(); i++) {
            int codePoint = cell.codePointAt(i);
             if (Character.charCount(codePoint) == 2) {
                 // 处理UTF-16编码中的代理对
                 i++;
             }
            if (Character.isSupplementaryCodePoint(codePoint)) {
                // 处理 Emoji 等特殊字符
                cellWidth += 2;
            } else {
                int charWidth = isWideCharacter(codePoint) ? 2 : 1;
                cellWidth += charWidth;
            }
        }
        return cellWidth;
    }

    private static boolean isWideCharacter(int codePoint) {
        return (codePoint >= 0x1100 && codePoint <= 0x115f) || // Hangul Jamo
                (codePoint >= 0x2329 && codePoint <= 0x232a) || // CJK Misc
                (codePoint >= 0x2e80 && codePoint <= 0x9fff) || // CJK
                (codePoint >= 0xa960 && codePoint <= 0xa97f) || // Hangul Jamo Extended-A
                (codePoint >= 0xac00 && codePoint <= 0xd7af) || // Hangul Syllables
                (codePoint >= 0xf900 && codePoint <= 0xfaff) || // CJK Compatibility Ideographs
                (codePoint >= 0xff00 && codePoint <= 0xffef);   // Halfwidth and Fullwidth Forms
    }

    private boolean isChineseCharacter(int codePoint) {
        return Character.UnicodeScript.of(codePoint) == Character.UnicodeScript.HAN;
    }

    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }

    private void printLine(int[] maxColumnWidths) {
        System.out.print("+");
        for (int maxColumnWidth : maxColumnWidths) {
            System.out.print(repeat("-", maxColumnWidth + 2));
            System.out.print("+");
        }
        System.out.println();
    }

    private String repeat(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(s);
        }
        return sb.toString();
    }
}