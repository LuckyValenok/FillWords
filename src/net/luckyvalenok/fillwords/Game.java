package net.luckyvalenok.fillwords;

public class Game {
    
    private final static int LARGE_WIDTH  = 80;
    private final static int LARGE_HEIGHT = 23;
    
    public static void main(String[] args) {
        Menu menu = new Menu(LARGE_WIDTH, LARGE_HEIGHT);
        menu.openMainMenu();
    }
}
