package net.luckyvalenok.fillwords.menu;

import com.googlecode.lanterna.TextColor;
import net.luckyvalenok.fillwords.Settings;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public enum SettingsButton {
    
    SIZE_MAP(7, "Размер карты", "sizeMap", null, new Object[]{2, 30}),
    SIZE_CELL(8, "Размер ячейки", "sizeCell", null, new Object[]{1, 4}),
    COLOR_MAP(9, "Цвет карты", "colorMap", null, new TextColor.ANSI[]{TextColor.ANSI.BLACK, TextColor.ANSI.RED, TextColor.ANSI.GREEN, TextColor.ANSI.YELLOW, TextColor.ANSI.BLUE, TextColor.ANSI.MAGENTA, TextColor.ANSI.CYAN}),
    SELECT_CELL_COLOR(10, "Цвет выделенной ячейки", "selectCellColor", null, new TextColor.ANSI[]{TextColor.ANSI.BLACK, TextColor.ANSI.RED, TextColor.ANSI.GREEN, TextColor.ANSI.YELLOW, TextColor.ANSI.BLUE, TextColor.ANSI.MAGENTA, TextColor.ANSI.CYAN}),
    SELECT_WORD_COLOR(11, "Цвет выделенного слова", "selectWordColor", null, new TextColor.ANSI[]{TextColor.ANSI.BLACK, TextColor.ANSI.RED, TextColor.ANSI.GREEN, TextColor.ANSI.YELLOW, TextColor.ANSI.BLUE, TextColor.ANSI.MAGENTA, TextColor.ANSI.CYAN}),
    SOLVED_WORD_COLOR(12, "Цвет разгаданного слова", "solvedWordColor", null, new TextColor.ANSI[]{TextColor.ANSI.BLACK, TextColor.ANSI.RED, TextColor.ANSI.GREEN, TextColor.ANSI.YELLOW, TextColor.ANSI.BLUE, TextColor.ANSI.MAGENTA, TextColor.ANSI.CYAN}),
    RANDOM_COLOR(13, "Рандомный цвет разгаданного слова", "randomColor", null, null),
    BACK(14, "Вернуться и сохранить", null, menu -> {
        try {
            new MainMenu(80, 23).open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }, null);
    
    public static Map<Integer, SettingsButton> buttons = new HashMap<>();
    
    static {
        for (SettingsButton button : values()) {
            buttons.put(button.getY(), button);
        }
    }
    
    private final int y;
    private final String name;
    private final String field;
    private final Consumer<GameMenu> menuConsumer;
    private final Object[] availableValues;
    
    SettingsButton(int y, String name, String field, Consumer<GameMenu> menuConsumer, Object[] availableValues) {
        this.y = y;
        this.name = name;
        this.field = field;
        this.menuConsumer = menuConsumer;
        this.availableValues = availableValues;
    }
    
    public static SettingsButton getButton(int y) {
        return buttons.get(y);
    }
    
    public String getName() {
        return name;
    }
    
    public int getY() {
        return y;
    }
    
    public void switchButton(int offset) {
        Settings.set(field, offset, availableValues);
    }
    
    public void pressButton(GameMenu menu) {
        menuConsumer.accept(menu);
    }
    
    public String getField() {
        return field;
    }
    
    public Object getValue() {
        return Settings.get(field);
    }
}
