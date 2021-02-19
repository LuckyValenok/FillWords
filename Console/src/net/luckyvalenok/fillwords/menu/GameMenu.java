package net.luckyvalenok.fillwords.menu;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public abstract class GameMenu extends AbstractGameMenu {
    
    private final Terminal terminal;
    private final TextGraphics graphics;
    private final MultiWindowTextGUI gui;
    private Screen screen;
    
    public GameMenu(int width, int height) throws IOException {
        DefaultTerminalFactory defaultTerminalFactory = new DefaultTerminalFactory();
        defaultTerminalFactory.setInitialTerminalSize(new TerminalSize(width, height));
        defaultTerminalFactory.setTerminalEmulatorTitle("FillWords");
        terminal = defaultTerminalFactory.createTerminal();
        
        graphics = terminal.newTextGraphics();
        
        screen = new TerminalScreen(terminal);
        screen.setCursorPosition(null);
        screen.startScreen();
        
        gui = new MultiWindowTextGUI(screen, TextColor.ANSI.BLACK);
    }
    
    public GameMenu(Terminal terminal, TextGraphics graphics, MultiWindowTextGUI gui) {
        this.terminal = terminal;
        this.graphics = graphics;
        this.gui = gui;
    }
    
    public GameMenu(GameMenu menu) {
        this.terminal = menu.getTerminal();
        this.graphics = menu.getGraphics();
        this.gui = menu.getGui();
        this.screen = menu.getScreen();
    }
    
    abstract void onKeyInput() throws IOException;
    
    abstract void draw() throws IOException;
    
    public void open() throws IOException {
        clearScreen();
        draw();
        refreshScreen();
        onKeyInput();
    }
    
    public MultiWindowTextGUI getGui() {
        return gui;
    }
    
    public TextGraphics getGraphics() {
        return graphics;
    }
    
    public Terminal getTerminal() {
        return terminal;
    }
    
    public Screen getScreen() {
        return screen;
    }
    
    public void refreshScreen() throws IOException {
        getTerminal().flush();
    }
    
    public void clearScreen() throws IOException {
        getTerminal().clearScreen();
    }
    
    public void drawString(int x, int y, String string, SGR srg) {
        getGraphics().putString(x, y, string, srg);
    }
}
