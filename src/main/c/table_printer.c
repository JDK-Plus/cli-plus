#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <locale.h>
#include <wchar.h>

#define MAX_ROWS 50
#define MAX_COLS 10
#define MAX_COL_WIDTH 20

void print_table(char data[MAX_ROWS][MAX_COLS][MAX_COL_WIDTH], int num_rows, int num_cols) {
    int col_widths[MAX_COLS] = {0};
    int i, j, k;

    // Find the maximum width of each column
    for (i = 0; i < num_cols; i++) {
        for (j = 0; j < num_rows; j++) {
            int width = 0;
            for (k = 0; k < strlen(data[j][i]); k++) {
                wchar_t c;
                mbtowc(&c, &data[j][i][k], 1);
                if (iswcntrl(c) || iswspace(c) || iswpunct(c)) {
                    width++;
                } else {
                    width += 2;
                }
            }
            if (width > col_widths[i]) {
                col_widths[i] = width;
            }
        }
    }

    // Print the table headers
    for (i = 0; i < num_cols; i++) {
        printf("+");
        for (j = 0; j < col_widths[i] + 2; j++) {
            printf("-");
        }
    }
    printf("+\n");

    for (i = 0; i < num_cols; i++) {
        printf("| %-*s ", col_widths[i], data[0][i]);
    }
    printf("|\n");

    // Print the table rows
    for (i = 1; i < num_rows; i++) {
        for (j = 0; j < num_cols; j++) {
            printf("| ");
            for (k = 0; k < strlen(data[i][j]); k++) {
                wchar_t c;
                mbtowc(&c, &data[i][j][k], 1);
                if (iswcntrl(c) || iswspace(c) || iswpunct(c)) {
                    printf("%c", data[i][j][k]);
                } else {
                    wprintf(L"%lc", c);
                }
            }
            printf("%*s ", col_widths[j] - wcslen(data[i][j]), "");
        }
        printf("|\n");
    }

    // Print the table footer
    for (i = 0; i < num_cols; i++) {
        printf("+");
        for (j = 0; j < col_widths[i] + 2; j++) {
            printf("-");
        }
    }
    printf("+\n");
}


int main() {
    char data[MAX_ROWS][MAX_COLS][MAX_COL_WIDTH] = {
        {"ID", "Name", "Age", "City", "Country"},
        {"1", "John Smith", "32", "New York", "USA"},
        {"2", "刘伟", "27", "北京", "中国"},
        {"3", "Mario Rossi", "45", "Rome", "Italy"},
        {"4", "中本聪", "40", "东京", "日本"},
        {"5", "Marie Dupont", "29", "Paris", "France"},
        {"6", "Gustav Schmidt", "36", "Berlin", "Germany"},
        {"7", "Juan Pérez", "31", "Mexico City", "Mexico"},
        {"8", "Анастасия Иванова", "24", "Москва", "Россия"},
        {"9", "Fernando García", "39", "Madrid", "Spain"},
        {"10", "Mehmet Yılmaz", "30", "Istanbul", "Turkey"}
    };
    int num_rows = 11;  // including header
    int num_cols = 5;

    print_table(data, num_rows, num_cols);
    return 0;
}
