package net.luckyvalenok.fillwords;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Settings {
    public static int sizeMap = 5;
    public static int sizeCell = 1;
    public static Color selectCellColor = Color.BLUE;
    public static Color selectWordColor = Color.RED;
    public static Color solvedWordColor = Color.GREEN;
    public static Color colorMap = Color.BLACK;
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
                case "java.awt.Color":
                    value = Color.getColor((String) value);
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
                case "java.awt.Color": {
                    Color color = (Color) value;
                    List<Object> objectList = Arrays.stream(availableValues).collect(Collectors.toList());
                    int index = objectList.indexOf(color);
                    value = index + offset >= objectList.size() || index + offset < 0 ? color : objectList.get(index + offset);
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
