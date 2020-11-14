package net.luckyvalenok.fillwords;

import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.gui.Window;
import com.googlecode.lanterna.gui.component.Button;
import com.googlecode.lanterna.gui.component.TextBox;
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
    private static GUIScreen guiScreen;
    
    private boolean isLive;
    
    Menu(int width, int height) {
        terminal = new SwingTerminal(width, height);
        terminal.setTerminalPalette(TerminalPalette.MAC_OS_X_TERMINAL_APP);
        
        screen = new Screen(terminal);
        screen.setCursorPosition(null);
        screen.startScreen();
        terminal.getJFrame().setTitle("FillWords");
        terminal.getJFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        guiScreen = new GUIScreen(screen);
    }
    
    private void drawString(int x, int y, String string, Terminal.Color color) {
        screen.putString(x, y, string, color, null);
    }
    
    private void drawButton(GameButton button, Terminal.Color color) {
        drawString(31, button.getY(), button.getName(), color);
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
        drawButton(GameButton.START_GAME, selected == GameButton.START_GAME ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        drawButton(GameButton.PROCEED, selected == GameButton.PROCEED ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        drawButton(GameButton.RATING, selected == GameButton.RATING ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        drawButton(GameButton.EXTT, selected == GameButton.EXTT ? Terminal.Color.WHITE : Terminal.Color.BLUE);
        
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
        
        drawButton(GameButton.START_GAME, Terminal.Color.WHITE);
        drawButton(GameButton.PROCEED, Terminal.Color.BLUE);
        drawButton(GameButton.RATING, Terminal.Color.BLUE);
        drawButton(GameButton.EXTT, Terminal.Color.BLUE);
    }
    
    private void getPlayerName() {
        clearScreen();
        
        Window playerNameWindow = new Window("Введите свое имя");
        TextBox textBox = new TextBox(null, 16);
        playerNameWindow.addComponent(textBox);
        Button button = new Button("Готово", () -> {
            System.out.println(textBox.getText());
            openMainMenu();
        });
        playerNameWindow.addComponent(button);
        guiScreen.showWindow(playerNameWindow, GUIScreen.Position.CENTER);
    }
    
    protected void openMainMenu() {
        isLive = true;
        clearScreen();
        drawMainMenu();
        
        refreshScreen();
        
        GameButton selectButton = handleMainMenu();
        if (selectButton == GameButton.EXTT) {
            exit();
        } else if (selectButton == GameButton.START_GAME) {
            getPlayerName();
        } else {
            clearScreen();
            drawString(25, 10, "Тут однажды будет " + selectButton.getName(), Terminal.Color.WHITE);
            refreshScreen();
        }
    }
}