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
    
    private void clearScreen() {
        screen.clear();
    }
    
    private GameButton handleMainMenu() {
        int selected = GameButton.START_GAME.getY();
        Key k;
        while (isLive) {
            k = readKeyInput();
            if (k != null) {
                switch (k.getKind()) {
                    case ArrowDown:
                        if (selected < GameButton.EXTT.getY())
                            selected++;
                        break;
                    case ArrowUp:
                        if (selected > GameButton.START_GAME.getY())
                            selected--;
                        break;
                    case Enter:
                        return GameButton.getButton(selected);
                    default:
                        break;
                }
                drawMainSelOption(GameButton.getButton(selected));
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        return GameButton.START_GAME;
    }
    
    private void drawMainSelOption(GameButton selected) {
        drawString(BUTTON_OFFSET_X, GameButton.START_GAME.getY(), "Новая игра", selected == GameButton.START_GAME ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, GameButton.PROCEED.getY(), "Продолжить", selected == GameButton.PROCEED ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, GameButton.RATING.getY(), "Рейтинг", selected == GameButton.RATING ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, GameButton.EXTT.getY(), "Выход", selected == GameButton.EXTT ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        
        refreshScreen();
    }
    
    private void drawMainMenu() {
        int x = 20;
        int y = 2;
        
        drawString(x, y, "╔══╗╔══╗╔╗  ╔╗  ╔╗╔╗╔╗╔══╗╔═══╗╔══╗ ╔══╗", Terminal.Color.GREEN);
        drawString(x, ++y, "║╔═╝╚╗╔╝║║  ║║  ║║║║║║║╔╗║║╔═╗║║╔╗╚╗║╔═╝", Terminal.Color.GREEN);
        drawString(x, ++y, "║╚═╗ ║║ ║║  ║║  ║║║║║║║║║║║╚═╝║║║╚╗║║╚═╗", Terminal.Color.GREEN);
        drawString(x, ++y, "║╔═╝ ║║ ║║  ║║  ║║║║║║║║║║║╔╗╔╝║║ ║║╚═╗║", Terminal.Color.GREEN);
        drawString(x, ++y, "║║  ╔╝╚╗║╚═╗║╚═╗║╚╝╚╝║║╚╝║║║║║ ║╚═╝║╔═╝║", Terminal.Color.GREEN);
        drawString(x, ++y, "╚╝  ╚══╝╚══╝╚══╝╚═╝╚═╝╚══╝╚╝╚╝ ╚═══╝╚══╝", Terminal.Color.GREEN);
        
        drawString(BUTTON_OFFSET_X, GameButton.START_GAME.getY(), "Новая игра", Terminal.Color.WHITE);
        drawString(BUTTON_OFFSET_X, GameButton.PROCEED.getY(), "Продолжить", Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, GameButton.RATING.getY(), "Рейтинг", Terminal.Color.BLUE);
        drawString(BUTTON_OFFSET_X, GameButton.EXTT.getY(), "Выход", Terminal.Color.BLUE);
    }
    
    protected void openMainMenu() {
        isLive = true;
        drawMainMenu();
        
        refreshScreen();
        
        GameButton selectButton = handleMainMenu();
        if (selectButton == GameButton.EXTT) {
            exit();
        } else {
            clearScreen();
            drawString(25, 10, "Тут однажды будет " + selectButton.getName(), Terminal.Color.WHITE);
            refreshScreen();
        }
    }
}