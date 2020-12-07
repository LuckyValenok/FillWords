package net.luckyvalenok.fillwords;

import net.luckyvalenok.fillwords.menu.SettingsButton;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
        
        reader = new BufferedReader(new FileReader("score.txt"));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            score.put(parts[0], Integer.parseInt(parts[1]));
        }
        reader.close();
        
        reader = new BufferedReader(new FileReader("settings.txt"));
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(";");
            Settings.set(parts[0], parts[1]);
        }
        reader.close();
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
}
