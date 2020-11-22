package net.luckyvalenok.fillwords;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {
    
    public final static List<String> allWords = new ArrayList<>();
    private final static int LARGE_WIDTH = 80;
    private final static int LARGE_HEIGHT = 23;
    
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("words.txt"));
        String line;
        int maxLengthWord = 0;
        while ((line = reader.readLine()) != null) {
            allWords.add(line);
            if (line.length() > maxLengthWord) {
                maxLengthWord = line.length();
            }
        }
        MainMenu mainMenu = new MainMenu(LARGE_WIDTH, LARGE_HEIGHT);
        mainMenu.openMainMenu();
    }
}
