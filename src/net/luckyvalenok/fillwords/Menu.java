package net.luckyvalenok.fillwords;

import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.terminal.Terminal;
import com.googlecode.lanterna.terminal.swing.SwingTerminal;
import com.googlecode.lanterna.terminal.swing.TerminalPalette;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Menu {
    
    private static SwingTerminal terminal;
    private static Screen screen;
    
    private final int BUTTON_OFFSET_X = 30;
    private final int START_GAME = 11;
    private final int PROCEED = 12;
    private final int RATING = 13;
    private final int EXTT = 14;
    
    private boolean isLive;
    
    Menu(int width, int height) {
        terminal = new SwingTerminal(width, height);
        terminal.setTerminalPalette(TerminalPalette.MAC_OS_X_TERMINAL_APP);
        
        screen = new Screen(terminal);
        screen.setCursorPosition(null);
        screen.startScreen();
        terminal.getJFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
    }
    
    private void drawString(int x, int y, String string, Terminal.Color fg_color) {
        screen.putString(x, y, string, fg_color, null);
    }
    
    private void refreshScreen() {
        screen.refresh();
    }
    
    private Key readKeyInput() {
        return terminal.readInput();
    }
    
    private void exit() {
        isLive = false;
        terminal.exitPrivateMode();
    }
    
    private int handleMainMenu() {
        int selected = START_GAME;
        Key k;
        while (isLive) {
            k = readKeyInput();
            if (k != null) {
                switch (k.getKind()) {
                    case ArrowDown:
                        if (selected < EXTT)
                            selected++;
                        break;
                    case ArrowUp:
                        if (selected > START_GAME)
                            selected--;
                        break;
                    case Enter:
                        return selected;
                    default:
                        break;
                }
                highlighMainMenuSelectedOption(selected);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        return 0;
    }
    
    private void highlighMainMenuSelectedOption(int selected) {
        drawString(BUTTON_OFFSET_X, START_GAME, "Новая игра", selected == START_GAME ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, PROCEED, "Продолжить", selected == PROCEED ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, RATING, "Рейтинг", selected == RATING ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, EXTT, "Выход", selected == EXTT ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        
        refreshScreen();
    }
    
    private void drawMainMenu() {
        int x = 20;
        int y = 2;
        
        drawString(x, y,   "╔══╗╔══╗╔╗  ╔╗  ╔╗╔╗╔╗╔══╗╔═══╗╔══╗ ╔══╗", Terminal.Color.GREEN);
        drawString(x, ++y, "║╔═╝╚╗╔╝║║  ║║  ║║║║║║║╔╗║║╔═╗║║╔╗╚╗║╔═╝", Terminal.Color.GREEN);
        drawString(x, ++y, "║╚═╗ ║║ ║║  ║║  ║║║║║║║║║║║╚═╝║║║╚╗║║╚═╗", Terminal.Color.GREEN);
        drawString(x, ++y, "║╔═╝ ║║ ║║  ║║  ║║║║║║║║║║║╔╗╔╝║║ ║║╚═╗║", Terminal.Color.GREEN);
        drawString(x, ++y, "║║  ╔╝╚╗║╚═╗║╚═╗║╚╝╚╝║║╚╝║║║║║ ║╚═╝║╔═╝║", Terminal.Color.GREEN);
        drawString(x, ++y, "╚╝  ╚══╝╚══╝╚══╝╚═╝╚═╝╚══╝╚╝╚╝ ╚═══╝╚══╝", Terminal.Color.GREEN);
        
        drawString(BUTTON_OFFSET_X, START_GAME, "Новая игра", Terminal.Color.WHITE);
        drawString(BUTTON_OFFSET_X, PROCEED, "Продолжить", Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, RATING, "Рейтинг", Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, EXTT, "Выход", Terminal.Color.BLUE);
    }
    
    protected void openMainMenu() {
        isLive = true;
        drawMainMenu();
        
        refreshScreen();
        
        handleMainMenu();
    }
}