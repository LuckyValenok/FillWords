package net.luckyvalenok.fillwords.menu;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import net.luckyvalenok.fillwords.Game;
import net.luckyvalenok.fillwords.Settings;
import net.luckyvalenok.fillwords.objects.GameMap;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public enum GameButton {
    
    START_GAME(11, "Новая игра", menu -> {
        Window playerNameWindow = new BasicWindow("Введите свое имя");
        playerNameWindow.setHints(Collections.singletonList(Window.Hint.CENTERED));
        TextBox textBox = new TextBox(new TerminalSize(16, 1));
        Panel contentPanel = new Panel(new GridLayout(2));
        contentPanel.addComponent(textBox);
        Button button = new Button("Готово", () -> {
            String name = textBox.getText();
            playerNameWindow.close();
            try {
                GameMap gameMap = new GameMap(Game.dataManager.getAllWords(), Game.dataManager.getMaxLengthWord(), Settings.sizeMap, Settings.sizeMap);
                menu.getTerminal().close();
                new InGameMenu(gameMap, name).open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        contentPanel.addComponent(button);
        playerNameWindow.setComponent(contentPanel);
        menu.getGui().addWindowAndWait(playerNameWindow);
    }),
    PROCEED(12, "Продолжить", menu -> {
        try {
            menu.drawString(25, 10, "Тут однажды будет Продолжить", SGR.BOLD);
            menu.refreshScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }),
    RATING(13, "Рейтинг", menu -> {
        try {
            menu.getGraphics().setBackgroundColor(TextColor.ANSI.GREEN);
            menu.drawString(25, 5, "Рейтинг", SGR.BOLD);
            menu.getGraphics().setBackgroundColor(TextColor.ANSI.BLACK);
    
            List<Map.Entry<String, Integer>> collect = Game.dataManager.getScore().entrySet().stream()
                .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toList());
            int i = 1;
            while (i <= 10 && i <= collect.size()) {
                Map.Entry<String, Integer> entry = collect.get(i - 1);
                menu.drawString(20, 6 + i, i + ". " + entry.getKey() + " " + entry.getValue() + " очков", SGR.BOLD);
                i++;
            }
            menu.refreshScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }),
    SETTINGS(14, "Настройки", menu -> {
        try {
            menu.drawString(25, 10, "Тут однажды будет Настройки", SGR.BOLD);
            menu.refreshScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }),
    EXTT(15, "Выход", menu -> {
        try {
            menu.getTerminal().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    });
    
    public static Map<Integer, GameButton> buttons = new HashMap<>();
    
    static {
        for (GameButton button : values()) {
            buttons.put(button.getY(), button);
        }
    }
    
    private final int y;
    private final String name;
    private final Consumer<MainMenu> terminalConsumer;
    
    GameButton(int y, String name, Consumer<MainMenu> terminalConsumer) {
        this.y = y;
        this.name = name;
        this.terminalConsumer = terminalConsumer;
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
    
    public Consumer<MainMenu> getTerminalConsumer() {
        return terminalConsumer;
    }
}
