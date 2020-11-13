package net.luckyvalenok.fillwords;

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
    
    private void exit() {
        terminal.exitPrivateMode();
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
        drawMainMenu();
        
        refreshScreen();
    }
}
