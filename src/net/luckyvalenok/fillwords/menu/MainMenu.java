package net.luckyvalenok.fillwords.menu;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainMenu extends GameMenu {
    
    private Timer timer;
    
    public MainMenu(int width, int height) throws IOException {
        super(width, height);
    }
    
    public MainMenu(GameMenu menu) {
        super(menu);
    }
    
    private void drawMainSelOption(MainButton selected) throws IOException {
        drawButton(MainButton.START_GAME, selected);
        drawButton(MainButton.PROCEED, selected);
        drawButton(MainButton.RATING, selected);
        drawButton(MainButton.SETTINGS, selected);
        drawButton(MainButton.EXTT, selected);
        
        refreshScreen();
    }
    
    protected void draw() throws IOException {
        drawMainSelOption(MainButton.START_GAME);
        getTerminal().flush();
        
        timer = new Timer();
        timer.schedule(new InfoUpdater(), 0, 200);
    }
    
    protected void onKeyInput() throws IOException {
        int selected = MainButton.START_GAME.getY();
        KeyStroke key = getTerminal().readInput();
        while (key.getKeyType() != KeyType.EOF && key.getKeyType() != KeyType.Escape) {
            switch (key.getKeyType()) {
                case ArrowDown:
                    if (selected < MainButton.EXTT.getY())
                        selected++;
                    break;
                case ArrowUp:
                    if (selected > MainButton.START_GAME.getY())
                        selected--;
                    break;
                case Enter:
                    timer.cancel();
                    clearScreen();
                    MainButton.getButton(selected).getTerminalConsumer().accept(this);
                    return;
            }
            drawMainSelOption(MainButton.getButton(selected));
            key = getTerminal().readInput();
        }
        getTerminal().close();
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
                getGraphics().setForegroundColor(TextColor.ANSI.WHITE);
                getTerminal().flush();
                offset++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        private void drawInfo(int y, String string, TextColor color) {
            getGraphics().setForegroundColor(color);
            drawString(20, y, string, SGR.BOLD);
        }
    }
}