package net.luckyvalenok.fillwords;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu {
    
    private final Terminal terminal;
    private final TextGraphics graphics;
    private final MultiWindowTextGUI gui;
    private Timer timer;
    
    MainMenu(int width, int height) throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(width, height));
        defaultTerminalFactory.setTerminalEmulatorTitle("FillWords");
        terminal = defaultTerminalFactory.createTerminal();
        
        graphics = terminal.newTextGraphics();
        
        Screen screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
        screen.startScreen();
        
        gui = new MultiWindowTextGUI(screen,  TextColor.ANSI.BLACK);
    }
    
    public void drawString(int x, int y, String string, SGR srg) {
        graphics.putString(x, y, string, srg);
    }
    
    public void drawButton(GameButton button, GameButton selected) {
        graphics.setForegroundColor(TextColor.ANSI.WHITE);
        if (selected == button) {
            graphics.setForegroundColor(TextColor.ANSI.GREEN);
        }
        drawString(30, button.getY(), button.getName(), SGR.BOLD);
    }
    
    public void refreshScreen() throws IOException {
        terminal.flush();
    }
    
    public void clearScreen() throws IOException {
        terminal.clearScreen();
    }
    
    private void drawMainSelOption(GameButton selected) throws IOException {
        drawButton(GameButton.START_GAME, selected);
        drawButton(GameButton.PROCEED, selected);
        drawButton(GameButton.RATING, selected);
        drawButton(GameButton.EXTT, selected);
        
        refreshScreen();
    }
    
    private void drawMainMenu() throws IOException {
        drawMainSelOption(GameButton.START_GAME);
    
        timer = new Timer();
        timer.schedule(new InfoUpdater(), 0, 200);
        terminal.flush();
    }
    
    protected void openMainMenu() throws IOException {
        clearScreen();
        drawMainMenu();
        
        refreshScreen();
        
        int selected = GameButton.START_GAME.getY();
        KeyStroke key = terminal.readInput();
        while (key.getKeyType() != KeyType.EOF && key.getKeyType() != KeyType.Escape) {
            switch (key.getKeyType()) {
                case ArrowDown:
                    if (selected < GameButton.EXTT.getY())
                        selected++;
                    break;
                case ArrowUp:
                    if (selected > GameButton.START_GAME.getY())
                        selected--;
                    break;
                case Enter:
                    timer.cancel();
                    clearScreen();
                    GameButton.getButton(selected).getTerminalConsumer().accept(this);
                    return;
            }
            drawMainSelOption(GameButton.getButton(selected));
            key = terminal.readInput();
        }
        terminal.close();
    }
    
    public Terminal getTerminal() {
        return terminal;
    }
    
    public MultiWindowTextGUI getGui() {
        return gui;
    }
    
    private class InfoUpdater extends TimerTask {
        TextColor[] colors = new TextColor[]{new TextColor.RGB(255, 0, 24), new TextColor.RGB(255, 165, 44), new TextColor.RGB(255, 255, 65), new TextColor.RGB(0, 128, 24), new TextColor.RGB(0, 0, 249), new TextColor.RGB(134, 0, 125)};
        private int offset = 0;
        
        @Override
        public void run() {
            try {
                int y = 2;
                
                drawInfo(y, "╔══╗╔══╗╔╗  ╔╗  ╔╗╔╗╔╗╔══╗╔═══╗╔══╗ ╔══╗", colors[(offset) % 6]);
                drawInfo(++y, "║╔═╝╚╗╔╝║║  ║║  ║║║║║║║╔╗║║╔═╗║║╔╗╚╗║╔═╝", colors[(1 + offset) % 6]);
                drawInfo(++y, "║╚═╗ ║║ ║║  ║║  ║║║║║║║║║║║╚═╝║║║╚╗║║╚═╗", colors[(2 + offset) % 6]);
                drawInfo(++y, "║╔═╝ ║║ ║║  ║║  ║║║║║║║║║║║╔╗╔╝║║ ║║╚═╗║", colors[(3 + offset) % 6]);
                drawInfo(++y, "║║  ╔╝╚╗║╚═╗║╚═╗║╚╝╚╝║║╚╝║║║║║ ║╚═╝║╔═╝║", colors[(4 + offset) % 6]);
                drawInfo(++y, "╚╝  ╚══╝╚══╝╚══╝╚═╝╚═╝╚══╝╚╝╚╝ ╚═══╝╚══╝", colors[(5 + offset) % 6]);
                graphics.setForegroundColor(TextColor.ANSI.WHITE);
                terminal.flush();
                offset++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void drawInfo(int y, String string, TextColor color) {
            graphics.setForegroundColor(color);
            drawString(20, y, string, SGR.BOLD);
        }
    }
}