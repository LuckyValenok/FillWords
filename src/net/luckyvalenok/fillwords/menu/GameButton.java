package net.luckyvalenok.fillwords.menu;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import net.luckyvalenok.fillwords.Game;
import net.luckyvalenok.fillwords.objects.GameMap;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public enum GameButton {
    
    START_GAME(11, "Новая игра", terminal -> {
        Window playerNameWindow = new BasicWindow("Введите свое имя");
        playerNameWindow.setHints(Collections.singletonList(Window.Hint.CENTERED));
        TextBox textBox = new TextBox(new TerminalSize(16, 1));
        Panel contentPanel = new Panel(new GridLayout(2));
        contentPanel.addComponent(textBox);
        Button button = new Button("Готово", () -> {
            System.out.println(textBox.getText());
            playerNameWindow.close();
            try {
                GameMap gameMap = new GameMap(Game.allWords, Game.maxLengthWord, 5, 5);
                terminal.getTerminal().close();
                new InGameMenu(gameMap).open();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        contentPanel.addComponent(button);
        playerNameWindow.setComponent(contentPanel);
        terminal.getGui().addWindowAndWait(playerNameWindow);
    }),
    PROCEED(12, "Продолжить", terminal -> {
        try {
            terminal.drawString(25, 10, "Тут однажды будет Продолжить", SGR.BOLD);
            terminal.refreshScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }),
    RATING(13, "Рейтинг", terminal -> {
        try {
            terminal.drawString(25, 10, "Тут однажды будет Рейтинг", SGR.BOLD);
            terminal.refreshScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }),
    EXTT(14, "Выход", mainMenu -> {
        try {
            mainMenu.getTerminal().close();
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
