package net.luckyvalenok.fillwords;

import com.googlecode.lanterna.TextColor;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Settings {
    public static int sizeMap = 5;
    public static int sizeCell = 1;
    public static TextColor.ANSI selectCellColor = TextColor.ANSI.BLUE;
    public static TextColor.ANSI selectWordColor = TextColor.ANSI.RED;
    public static TextColor.ANSI solvedWordColor = TextColor.ANSI.GREEN;
    public static TextColor.ANSI colorMap = TextColor.ANSI.BLACK;
    public static boolean randomColor = false;
    
    public static Object get(String field) {
        try {
            return Settings.class.getDeclaredField(field).get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
    
    /**
     * Устанавливает новое значение статическому полю класса
     *
     * @param field имя поля
     * @param value новое значение
     */
    public static void set(String field, Object value) {
        try {
            Field field1 = Settings.class.getDeclaredField(field);
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
            field1.set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void set(String field, int offset, Object[] availableValues) {
        try {
            Field field1 = Settings.class.getDeclaredField(field);
            Object value = field1.get(null);
            switch (field1.getType().getName()) {
                case "int": {
                    int min = (int) availableValues[0];
                    int max = (int) availableValues[1];
                    int temp = (int) value + offset;
                    value = temp > max ? min : temp < min ? max : temp;
                    break;
                }
                case "com.googlecode.lanterna.TextColor$ANSI": {
                    TextColor.ANSI temp = (TextColor.ANSI) value;
                    List<Object> objectList = Arrays.stream(availableValues).collect(Collectors.toList());
                    int index = objectList.indexOf(temp);
                    value = index + offset >= objectList.size() || index + offset < 0 ? temp : objectList.get(index + offset);
                    break;
                }
                case "boolean":
                    value = !((boolean) value);
                    break;
            }
            field1.set(null, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
