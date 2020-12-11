package net.luckyvalenok.fillwords;

import com.googlecode.lanterna.TextColor;
import net.luckyvalenok.fillwords.menu.GameMenu;
import net.luckyvalenok.fillwords.menu.InGameMenu;
import net.luckyvalenok.fillwords.menu.SettingsButton;
import net.luckyvalenok.fillwords.objects.GameMap;
import net.luckyvalenok.fillwords.objects.Position;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DataManager {
    
    private final List<String> allWords = new ArrayList<>();
    private final Map<String, Integer> score = new HashMap<>();
    private int maxLengthWord = 0;
    
    public DataManager() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("words.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            allWords.add(line);
            if (line.length() > maxLengthWord) {
                maxLengthWord = line.length();
            }
        }
        reader.close();
        
        try {
            reader = new BufferedReader(new FileReader("score.txt"));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                score.put(parts[0], Integer.parseInt(parts[1]));
            }
            reader.close();
        } catch (FileNotFoundException ignored) {
        }
        
        try {
            reader = new BufferedReader(new FileReader("settings.txt"));
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                Settings.set(parts[0], parts[1]);
            }
            reader.close();
        } catch (FileNotFoundException ignored) {
        }
    }
    
    public List<String> getAllWords() {
        return allWords;
    }
    
    public int getMaxLengthWord() {
        return maxLengthWord;
    }
    
    public Map<String, Integer> getScore() {
        return score;
    }
    
    public void sortScore() throws IOException {
        List<Map.Entry<String, Integer>> collect = score.entrySet().stream()
            .sorted((e1, e2) -> -e1.getValue().compareTo(e2.getValue()))
            .collect(Collectors.toList());
        FileWriter fileWriter = new FileWriter("score.txt");
        for (Map.Entry<String, Integer> entry : collect) {
            fileWriter.write(entry.getKey() + ";" + entry.getValue() + "\n");
        }
        fileWriter.close();
    }
    
    public void saveSettings() throws IOException {
        FileWriter fileWriter = new FileWriter("settings.txt");
        for (SettingsButton button : SettingsButton.values()) {
            if (button == SettingsButton.BACK) {
                continue;
            }
            fileWriter.write(button.getField() + ";" + button.getValue() + "\n");
        }
        fileWriter.close();
    }
    
    public void saveGame(GameMap map, String name, Map<Position, TextColor.ANSI> solved) throws IOException {
        FileWriter fileWriter = new FileWriter("game.txt");
        fileWriter.write("name='" + name + "'\n");
        fileWriter.write("rows=" + map.getRows() + "\n");
        fileWriter.write("columns=" + map.getColumns() + "\n");
        fileWriter.write("words=" + String.join(" ", map.getWords()) + "\nboard=");
        for (char[] chars : map.getBoard()) {
            for (char c : chars) {
                fileWriter.write(c);
            }
        }
        fileWriter.write("\nwordpos=");
        for (Map.Entry<String, Map<Integer, Position>> entry : map.getWordPositions().entrySet()) {
            fileWriter.write(entry.getKey());
            for (Map.Entry<Integer, Position> entry1 : entry.getValue().entrySet()) {
                fileWriter.write("#" + entry1.getKey() + "&" + entry1.getValue().x + "|" + entry1.getValue().y);
            }
            fileWriter.write(" ");
        }
        if (!solved.isEmpty()) {
            fileWriter.write("\nsolved=");
            for (Map.Entry<Position, TextColor.ANSI> entry : solved.entrySet()) {
                fileWriter.write(entry.getKey().x + "|" + entry.getKey().y + "@" + entry.getValue() + " ");
            }
        }
        fileWriter.close();
    }
    
    public InGameMenu loadGame(GameMenu menu) throws IOException {
        try {
            String name = null;
            int rows = 0, columns = 0;
            List<String> words = null;
            char[][] board = new char[0][];
            Map<String, Map<Integer, Position>> wordPos = new HashMap<>();
            Map<Position, TextColor.ANSI> solved = new HashMap<>();
            BufferedReader reader = new BufferedReader(new FileReader("game.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] strings = line.split("=");
                switch (strings[0]) {
                    case "name":
                        name = strings[1].replaceAll("'", "");
                        break;
                    case "rows":
                        rows = Integer.parseInt(strings[1]);
                        break;
                    case "columns":
                        columns = Integer.parseInt(strings[1]);
                        break;
                    case "words":
                        words = Arrays.asList(strings[1].split(" "));
                        break;
                    case "board":
                        board = new char[rows][columns];
                        for (int i = 0; i < rows; i++) {
                            for (int j = 0; j < columns; j++) {
                                board[i][j] = strings[1].charAt(i * columns + j);
                            }
                        }
                        break;
                    case "wordpos": {
                        String[] strings1 = strings[1].split(" ");
                        for (String s : strings1) {
                            String[] strings2 = s.split("#");
                            Map<Integer, Position> positionMap = new HashMap<>();
                            for (int i = 1; i < strings2.length; i++) {
                                String[] split = strings2[i].split("&");
                                String[] splitPos = split[1].split("\\|");
                                positionMap.put(Integer.parseInt(split[0]), new Position(Integer.parseInt(splitPos[0]), Integer.parseInt(splitPos[1])));
                            }
                            wordPos.put(strings2[0], positionMap);
                        }
                        break;
                    }
                    case "solved": {
                        String[] strings1 = strings[1].split(" ");
                        for (String s : strings1) {
                            String[] strings2 = s.split("@");
                            String[] splitPos = strings2[0].split("\\|");
                            solved.put(new Position(Integer.parseInt(splitPos[0]), Integer.parseInt(splitPos[1])), TextColor.ANSI.valueOf(strings2[1]));
                        }
                        break;
                    }
                }
            }
            reader.close();
            GameMap map = new GameMap(rows, columns, words, board, wordPos);
            return new InGameMenu(menu, map, name, solved);
        } catch (FileNotFoundException e) {
            return null;
        }
    }
}
