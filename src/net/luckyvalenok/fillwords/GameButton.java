package net.luckyvalenok.fillwords;

import java.util.HashMap;
import java.util.Map;

public enum GameButton {
    
    START_GAME(11, "Новая игра"),
    PROCEED(12, "Продолжить"),
    RATING(13, "Рейтинг"),
    EXTT(14, "Выход");
    
    public static Map<Integer, GameButton> buttons = new HashMap<>();
    
    static {
        for (GameButton button : values()) {
            buttons.put(button.getY(), button);
        }
    }
    
    private final int y;
    private final String name;
    
    GameButton(int y, String name) {
        this.y = y;
        this.name = name;
    }
    
    public static GameButton getButton(int y) {
        return buttons.get(y);
    }
    
    public String getName() {
        return name;
    }
    
    public int getY() {
        return y;
    }
}
