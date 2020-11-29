package net.luckyvalenok.fillwords.menu;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import net.luckyvalenok.fillwords.Game;
import net.luckyvalenok.fillwords.objects.GameMap;
import net.luckyvalenok.fillwords.objects.Position;
import net.luckyvalenok.fillwords.enums.State;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InGameMenu extends GameMenu {
    
    private static final TerminalPosition START_POSITION = new TerminalPosition(6, 4);
    private final GameMap map;
    private final List<Position> solved = new ArrayList<>();
    private final List<Position> selected = new ArrayList<>();
    private final int widthCell = 3;
    int score = 0;
    private State state = State.SEARCH;
    private Position currentPosition = new Position(0, 0);
    private String selectedWord = "";
    
    public InGameMenu(GameMap map) throws IOException {
        super(100, 50);
        
        this.map = map;
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
                    if (state == State.SEARCH && !solved.contains(currentPosition)) {
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
                        getTerminal().close();
                    }
                    break;
                default:
                    break;
            }
        } while (keyStroke.getKeyType() != KeyType.EOF && !map.getWords().isEmpty());
    }
    
    private TerminalPosition getRelative(int i, int j) {
        return START_POSITION.withRelative(i, j);
    }
    
    @Override
    void draw() throws IOException {
        clearScreen();
        getGraphics().putString(0, 0, "Очков: " + score);
        if (!selectedWord.isEmpty()) {
            getGraphics().putString(0, 1, "Выделенное слово: " + selectedWord);
        }
        char[][] board = map.getBoard();
        getGraphics().putString(getRelative(0, -1), getLine('┌', '┐', '┬'));
        int centerCell = widthCell / 2;
        for (int i = 0; i < map.getRows() * (widthCell + 1); i++) {
            for (int j = 0; j < map.getColumns(); j++) {
                Position position = new Position(i / (widthCell + 1), j);
                boolean isCurrentPosition = currentPosition.equals(position);
                boolean isSelected = selected.contains(position);
                boolean isSolved = solved.contains(position);
                for (int k = 0; k < widthCell; k++) {
                    getGraphics().putString(getRelative(j * widthCell + j, i + k), "│");
                    if (isSelected) {
                        getGraphics().setBackgroundColor(TextColor.ANSI.RED);
                    }
                    if (isCurrentPosition) {
                        getGraphics().setBackgroundColor(TextColor.ANSI.BLUE);
                    }
                    if (isSolved) {
                        getGraphics().setBackgroundColor(TextColor.ANSI.GREEN);
                    }
                    if (centerCell == k) {
                        getGraphics().putString(getRelative(j * widthCell + j + 1, i + k), StringUtils.center(board[i / (widthCell + 1)][j] + "", widthCell));
                    } else {
                        getGraphics().putString(getRelative(j * widthCell + j + 1, i + k), StringUtils.center(" ", widthCell));
                    }
                    getGraphics().setBackgroundColor(TextColor.ANSI.BLACK);
                    getGraphics().putString(getRelative(j * widthCell + j + widthCell + 1, i + k), "│");
                }
            }
            i += widthCell;
            getGraphics().putString(getRelative(0, i), getLine('├', '┤', '┼'));
        }
        getGraphics().putString(getRelative(0, map.getRows() * (widthCell + 1) - 1), getLine('└', '┘', '┴'));
        getTerminal().flush();
    }
    
    private boolean checkWord() {
        return map.getWords().contains(selectedWord);
    }
    
    private boolean checkPosition() {
        Position[] positions = map.getWordPoints().get(selectedWord).values().toArray(new Position[0]);
        for (int i = 0; i < selectedWord.length(); i++) {
            if (!positions[i].equals(selected.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private void processReply(String selectedWord) throws IOException {
        if (!Game.allWords.contains(selectedWord)) {
            getGraphics().putString(getRelative(0, map.getRows() * (widthCell + 1) + 1), "Данного слова нет в словаре");
        } else if (!checkWord()) {
            getGraphics().putString(getRelative(0, map.getRows() * (widthCell + 1) + 1), "Данное слово не загадано на этом уровне");
        } else if (!checkPosition()) {
            getGraphics().putString(getRelative(0, map.getRows() * (widthCell + 1) + 1), "Попробуйте выделить это слово по-другому");
        } else {
            state = State.SEARCH;
            solved.addAll(selected);
            selected.clear();
            score += selectedWord.length();
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
            if (!selected.contains(currentPosition) && !solved.contains(currentPosition)) {
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
