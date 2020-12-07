package net.luckyvalenok.fillwords;

import net.luckyvalenok.fillwords.menu.MainMenu;

import java.io.IOException;

public class Game {
    
    public static DataManager dataManager;
    
    static {
        try {
            dataManager = new DataManager();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) throws IOException {
        new MainMenu(80, 23).open();
    }
}
