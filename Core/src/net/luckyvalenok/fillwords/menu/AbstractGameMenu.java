package net.luckyvalenok.fillwords.menu;

import java.io.IOException;

public abstract class AbstractGameMenu {
    
    abstract void onKeyInput() throws IOException;
    
    abstract void draw() throws IOException;
    
    abstract void refreshScreen() throws IOException;
    
    abstract void clearScreen() throws IOException;
    
    public void open() throws IOException {
        clearScreen();
        draw();
        refreshScreen();
        onKeyInput();
    }
}
