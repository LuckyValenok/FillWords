package net.luckyvalenok.fillwords.menu;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import net.luckyvalenok.fillwords.Game;
import net.luckyvalenok.fillwords.Settings;
import net.luckyvalenok.fillwords.enums.State;
import net.luckyvalenok.fillwords.objects.GameMap;
import net.luckyvalenok.fillwords.objects.Position;
import net.luckyvalenok.fillwords.utils.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InGameMenu extends GameMenu {
    
    private static final TerminalPosition START_POSITION = new TerminalPosition(6, 4);
    private final GameMap map;
    private final String name;
    private final Map<Position, TextColor.ANSI> solved;
    private final List<Position> selected = new ArrayList<>();
    private final int widthCell = Settings.sizeCell;
    private State state = State.SEARCH;
    private Position currentPosition = new Position(0, 0);
    private String selectedWord = "";
    
    public InGameMenu(GameMenu menu, GameMap map, String name, Map<Position, TextColor.ANSI> solved) {
        super(menu);
        
        this.map = map;
        this.name = name;
        this.solved = solved;
    }
    
    public InGameMenu(GameMenu menu, GameMap map, String name) throws IOException {
        super(menu);
        
        this.map = map;
        this.name = name;
        solved = new HashMap<>();
        getTerminal().setCursorVisible(false);
    }
    
    @Override
    void onKeyInput() throws IOException {
        KeyStroke keyStroke;
        do {
            keyStroke = getTerminal().readInput();
            switch (keyStroke.getKeyType()) {
                case ArrowUp:
                    moveCursor(-1, 0);
                    break;
                case ArrowDown:
                    moveCursor(1, 0);
                    break;
                case ArrowLeft:
                    moveCursor(0, -1);
                    break;
                case ArrowRight:
                    moveCursor(0, 1);
                    break;
                case Enter:
                    if (state == State.SEARCH && !solved.containsKey(currentPosition)) {
                        state = State.SELECT;
                        selectedWord += map.getBoard()[currentPosition.x][currentPosition.y];
                        selected.add(currentPosition);
                    } else {
                        processReply(selectedWord);
                    }
                    break;
                case Escape:
                    if (state == State.SELECT) {
                        clearScreen();
                        state = State.SEARCH;
                        selectedWord = "";
                        selected.clear();
                        draw();
                    } else {
                        clearScreen();
                        drawString(15, 7, "Вы уверены, что хотите выйти?", SGR.BOLD);
                        drawString(15, 9, "Нажмите ESC, чтобы вернуться", SGR.BOLD);
                        drawString(15, 10, "Нажмите Enter, чтобы выйти и сохранить игру", SGR.BOLD);
                        getTerminal().flush();
                        KeyType keyType;
                        while ((keyType = getTerminal().readInput().getKeyType()) != KeyType.Escape && keyType != KeyType.Enter);
                        if (keyType == KeyType.Escape) {
                            draw();
                        } else {
                            Game.dataManager.saveGame(map, name, solved);
                            new MainMenu(this).open();
                            return;
                        }
                    }
                    break;
                default:
                    break;
            }
        } while (solved.size() != map.getColumns() * map.getRows());
        int allScore = Game.dataManager.getScore().getOrDefault(name, 0);
        Game.dataManager.getScore().put(name, allScore + solved.size());
        Game.dataManager.sortScore();
        clearScreen();
        drawString(15, 6, "Поздравляем, вы успешно набрали " + solved.size() + " очков", SGR.BOLD);
        drawString(15, 7, "Всего очков: " + (allScore + solved.size()), SGR.BOLD);
        drawString(15, 9, "Нажмите ESC, чтобы вернуться в главное меню", SGR.BOLD);
        getTerminal().flush();
        while (getTerminal().readInput().getKeyType() != KeyType.Escape);
        clearScreen();
        new MainMenu(this).open();
    }
    
    private TerminalPosition getRelative(int i, int j) {
        return START_POSITION.withRelative(i, j);
    }
    
    @Override
    void draw() throws IOException {
        clearScreen();
        getGraphics().setBackgroundColor(TextColor.ANSI.BLACK);
        getGraphics().putString(0, 0, "Очков: " + solved.size());
        if (!selectedWord.isEmpty()) {
            getGraphics().putString(0, 1, "Выделенное слово: " + selectedWord);
        }
        char[][] board = map.getBoard();
        getGraphics().setBackgroundColor(Settings.colorMap);
        getGraphics().putString(getRelative(0, -1), getLine('┌', '┐', '┬'));
        int centerCell = widthCell / 2;
        for (int i = 0; i < map.getRows() * (widthCell + 1); i++) {
            for (int j = 0; j < map.getColumns(); j++) {
                Position position = new Position(i / (widthCell + 1), j);
                boolean isCurrentPosition = currentPosition.equals(position);
                boolean isSelected = selected.contains(position);
                boolean isSolved = solved.containsKey(position);
                for (int k = 0; k < widthCell; k++) {
                    getGraphics().putString(getRelative(j * widthCell + j, i + k), "│");
                    if (isSelected) {
                        getGraphics().setBackgroundColor(Settings.selectWordColor);
                    }
                    if (isCurrentPosition) {
                        getGraphics().setBackgroundColor(Settings.selectCellColor);
                    }
                    if (isSolved) {
                        getGraphics().setBackgroundColor(solved.get(position));
                    }
                    if (centerCell == k) {
                        getGraphics().putString(getRelative(j * widthCell + j + 1, i + k), StringUtils.center(board[i / (widthCell + 1)][j] + "", widthCell));
                    } else {
                        getGraphics().putString(getRelative(j * widthCell + j + 1, i + k), StringUtils.center(" ", widthCell));
                    }
                    getGraphics().setBackgroundColor(Settings.colorMap);
                    getGraphics().putString(getRelative(j * widthCell + j + widthCell + 1, i + k), "│");
                }
            }
            i += widthCell;
            getGraphics().putString(getRelative(0, i), getLine('├', '┤', '┼'));
        }
        getGraphics().putString(getRelative(0, map.getRows() * (widthCell + 1) - 1), getLine('└', '┘', '┴'));
        getGraphics().setBackgroundColor(TextColor.ANSI.BLACK);
        getTerminal().flush();
    }
    
    private boolean checkWord() {
        return map.getWords().contains(selectedWord);
    }
    
    private boolean checkPosition() {
        Position[] positions = map.getWordPositions().get(selectedWord).values().toArray(new Position[0]);
        for (int i = 0; i < selectedWord.length(); i++) {
            if (!positions[i].equals(selected.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private void processReply(String selectedWord) throws IOException {
        if (!Game.dataManager.getAllWords().contains(selectedWord)) {
            getGraphics().putString(getRelative(0, map.getRows() * (widthCell + 1) + 1), "Данного слова нет в словаре");
        } else if (!checkWord()) {
            getGraphics().putString(getRelative(0, map.getRows() * (widthCell + 1) + 1), "Данное слово не загадано на этом уровне");
        } else if (!checkPosition()) {
            getGraphics().putString(getRelative(0, map.getRows() * (widthCell + 1) + 1), "Попробуйте выделить это слово по-другому");
        } else {
            state = State.SEARCH;
            TextColor.ANSI color = Settings.randomColor ? RandomUtils.ofSafe(new TextColor.ANSI[] {Settings.colorMap, TextColor.ANSI.DEFAULT, TextColor.ANSI.WHITE}, TextColor.ANSI.values()) : Settings.solvedWordColor;
            for (Position position : selected) {
                solved.put(position, color);
            }
            selected.clear();
            this.selectedWord = "";
            draw();
        }
        getTerminal().flush();
    }
    
    private void moveCursor(int dx, int dy) throws IOException {
        int x = currentPosition.x;
        int y = currentPosition.y;
        if (x + dx > map.getRows() - 1 || x + dx < 0 || y + dy > map.getColumns() - 1 || y + dy < 0)
            return;
        currentPosition = new Position(x + dx, y + dy);
        if (state == State.SELECT) {
            if (!selected.contains(currentPosition) && !solved.containsKey(currentPosition)) {
                selectedWord += map.getBoard()[currentPosition.x][currentPosition.y];
                selected.add(currentPosition);
            }
        }
        draw();
    }
    
    private String getLine(char start, char stop, char center) {
        String cells = "";
        for (int i = 0; i < map.getColumns(); i++) {
            cells += StringUtils.repeat('─', widthCell) + center;
        }
        cells = cells.substring(0, cells.length() - 1);
        return start + cells + stop;
    }
}
