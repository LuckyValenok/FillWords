package net.luckyvalenok.fillwords;

import net.luckyvalenok.fillwords.menu.MainMenu;

import java.io.IOException;

public class Game {
    
    private final static int LARGE_WIDTH = 80;
    private final static int LARGE_HEIGHT = 23;
    public static DataManager dataManager;
    
    static {
        try {
            dataManager = new DataManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        new MainMenu(LARGE_WIDTH, LARGE_HEIGHT).open();
    }
}
