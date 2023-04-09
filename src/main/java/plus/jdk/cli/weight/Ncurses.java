package plus.jdk.cli.weight;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

public interface Ncurses extends Library {
    Ncurses INSTANCE = Native.load("ncurses", Ncurses.class);

    void initscr();

    void endwin();

    void cbreak();

    void noecho();

    int getch();

    Pointer newwin(int nlines, int ncols, int begin_y, int begin_x);

    void delwin(Pointer win);

    void box(Pointer win, int verch, int horch);

    int mvwprintw(Pointer win, int y, int x, String format, Object... args);

    int mvwaddch(Pointer win, int y, int x, char ch);

    void wrefresh(Pointer win);
}
