package net.luckyvalenok.fillwords.menu;

import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import net.luckyvalenok.fillwords.Game;

import java.io.IOException;

public class SettingsMenu extends GameMenu {
    public SettingsMenu(GameMenu menu) {
        super(menu);
    }
    
    @Override
    void onKeyInput() throws IOException {
        int selected = SettingsButton.SIZE_MAP.getY();
        KeyStroke key = getTerminal().readInput();
        while (key.getKeyType() != KeyType.EOF) {
            switch (key.getKeyType()) {
                case ArrowDown:
                    if (selected < SettingsButton.BACK.getY())
                        selected++;
                    break;
                case ArrowUp:
                    if (selected > SettingsButton.SIZE_MAP.getY())
                        selected--;
                    break;
                case ArrowRight:
                    SettingsButton.getButton(selected).switchButton(1);
                    break;
                case ArrowLeft:
                    SettingsButton.getButton(selected).switchButton(-1);
                    break;
                case Enter:
                    SettingsButton button = SettingsButton.getButton(selected);
                    if (button == SettingsButton.BACK) {
                        Game.dataManager.saveSettings();
                        button.pressButton(this);
                        return;
                    }
            }
            drawSelOption(SettingsButton.getButton(selected));
            key = getTerminal().readInput();
        }
        getTerminal().close();
    }
    
    @Override
    void draw() throws IOException {
        drawSelOption(SettingsButton.SIZE_MAP);
    }
    
    public void drawButton(SettingsButton button, SettingsButton selected) {
        getGraphics().setForegroundColor(TextColor.ANSI.WHITE);
        if (selected == button) {
            getGraphics().setForegroundColor(TextColor.ANSI.GREEN);
        }
        drawString(15, button.getY(), button.getName(), SGR.BOLD);
        if (button != SettingsButton.BACK) {
            drawString(55, button.getY(), String.valueOf(button.getValue()), SGR.BOLD);
        }
    }
    
    public void drawSelOption(SettingsButton selected) throws IOException {
        clearScreen();
        drawButton(SettingsButton.SIZE_MAP, selected);
        drawButton(SettingsButton.SIZE_CELL, selected);
        drawButton(SettingsButton.COLOR_MAP, selected);
        drawButton(SettingsButton.SELECT_CELL_COLOR, selected);
        drawButton(SettingsButton.SELECT_WORD_COLOR, selected);
        drawButton(SettingsButton.SOLVED_WORD_COLOR, selected);
        drawButton(SettingsButton.RANDOM_COLOR, selected);
        drawButton(SettingsButton.BACK, selected);
    
        refreshScreen();
    }
}
