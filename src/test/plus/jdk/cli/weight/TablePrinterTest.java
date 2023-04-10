package plus.jdk.cli.weight;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import plus.jdk.cli.annotation.TableColumnName;

import java.util.ArrayList;
import java.util.List;

class TablePrinterTest {

    @Data
    @AllArgsConstructor
    private static class TableRow {

        @TableColumnName("id")
        private Integer id;

        @TableColumnName("姓名")
        private String name;

        @TableColumnName("年龄")
        private Integer age;

        @TableColumnName("性别")
        private String sex;
    }

    @Test
    void printTable() {
        TablePrinter tablePrinter = new TablePrinter();
        List<TableRow> tableRows = new ArrayList<>();
        tableRows.add(new TableRow(1, "张三💅", 30, "男"));
        tableRows.add(new TableRow(2, "李四", 89, "男"));
        tableRows.add(new TableRow(3, "王老五", 30, "男👵👲"));
        tableRows.add(new TableRow(4, "chang kai shen", 30, "女"));
        tableRows.add(new TableRow(4, "p-moon ☺️☺️", 30, "不明"));
        tablePrinter.printTable(tableRows, TableRow.class);
    }
}