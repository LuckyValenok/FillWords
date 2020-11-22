package net.luckyvalenok.fillwords;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMap {
    
    private final String[] allWords;
    private final int columns;
    private final int rows;
    private final List<String> words = new ArrayList<>();
    private final char[][] board;
    private final Map<String, List<Position>> wordPoints = new HashMap<>();
    
    public GameMap(String[] allWords, int maxLengthWord, int columns, int rows) {
        this.allWords = allWords;
        this.columns = columns;
        this.rows = rows;
        
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
            wordPoints.put(word, entry.getValue());
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                int index = matrix[i][j];
                int o = 0;
                while (!dic.get(index).get(o).equals(new Position(i, j))) {
                    o++;
                }
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
    
    public Map<String, List<Position>> getWordPoints() {
        return wordPoints;
    }
    
    private String getWord(int length) {
        String word = RandomUtils.of(allWords);
        while (word.length() != length || words.contains(word)) {
            word = RandomUtils.of(allWords);
        }
        return word;
    }
}
