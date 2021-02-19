package net.luckyvalenok.fillwords.objects;

import net.luckyvalenok.fillwords.utils.GeneticsHelper;
import net.luckyvalenok.fillwords.utils.RandomUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class GameMap {
    
    private final List<String> allWords;
    private final int columns;
    private final int rows;
    private final List<String> words;
    private final char[][] board;
    private final Map<String, Map<Integer, Position>> wordPoints;
    
    public GameMap(List<String> allWords, int columns, int rows, List<String> words, char[][] board, Map<String, Map<Integer, Position>> wordPoints) {
        this.allWords = allWords;
        this.columns = columns;
        this.rows = rows;
        this.wordPoints = wordPoints;
        this.words = words;
        this.board = board;
    }
    
    public GameMap(List<String> allWords, int maxLengthWord, int columns, int rows) {
        this.allWords = allWords;
        this.columns = columns;
        this.rows = rows;
        wordPoints = new HashMap<>();
        words = new ArrayList<>();
        
        board = new char[rows][columns];
        generateBoard(maxLengthWord);
    }
    
    public void generateBoard(int maxLengthWord) {
        GeneticsHelper geneticsHelper = new GeneticsHelper(columns, rows);
        int[][] matrix = geneticsHelper.evolve(RandomUtils.intRange(3, maxLengthWord / 2));
        Map<Integer, List<Position>> dic = geneticsHelper.getDictionary();
        Map<Integer, String> wordsMap = new HashMap<>();
        for (Map.Entry<Integer, List<Position>> entry : dic.entrySet()) {
            String word = getWord(entry.getValue().size());
            wordsMap.put(entry.getKey(), word);
            words.add(word);
            wordPoints.put(word, new TreeMap<>());
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int index = matrix[i][j];
                int o = 0;
                while (!dic.get(index).get(o).equals(new Position(i, j))) {
                    o++;
                }
                wordPoints.get(wordsMap.get(index)).put(o, new Position(i, j));
                board[i][j] = wordsMap.get(index).charAt(o);
            }
        }
    }
    
    public List<String> getWords() {
        return words;
    }
    
    public char[][] getBoard() {
        return board;
    }
    
    public Map<String, Map<Integer, Position>> getWordPositions() {
        return wordPoints;
    }
    
    public int getColumns() {
        return columns;
    }
    
    public int getRows() {
        return rows;
    }
    
    private String getWord(int length) {
        String word = RandomUtils.of(allWords);
        while (word.length() != length || words.contains(word)) {
            word = RandomUtils.of(allWords);
        }
        return word;
    }
}
