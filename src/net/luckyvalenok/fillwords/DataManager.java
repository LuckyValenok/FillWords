package net.luckyvalenok.fillwords;

import com.googlecode.lanterna.TextColor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
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
            set(Settings.class, parts[0], parts[1]);
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
    
    /**
     * Устанавливает новое значение статическому полю класса
     *
     * @param clazz класс
     * @param field имя поля
     * @param value новое значение
     */
    public static void set(Class clazz, String field, Object value) {
        try {
            Field field1 = clazz.getDeclaredField(field);
            System.out.println(field1.getType().getName());
            switch (field1.getType().getName()) {
                case "int":
                    value = Integer.parseInt((String) value);
                    break;
                case "com.googlecode.lanterna.TextColor$ANSI":
                    value = TextColor.ANSI.valueOf((String) value);
                    break;
                case "boolean":
                    value = Boolean.valueOf((String) value);
                    break;
            }
            clazz.getDeclaredField(field).set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
