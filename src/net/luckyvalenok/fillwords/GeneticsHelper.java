package net.luckyvalenok.fillwords;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneticsHelper {
    
    private final int[][] matrix;
    private final int columns;
    private final int rows;
    private final Map<Integer, List<Position>> wordsPlaceHolders;
    private int length;
    
    public GeneticsHelper(int rows, int columns) {
        wordsPlaceHolders = new HashMap<>();
        matrix = new int[this.rows = rows][this.columns = columns];
    }
    
    public Map<Integer, List<Position>> getDictionary() {
        return wordsPlaceHolders;
    }
    
    public int[][] evolve(int length) {
        this.length = length;
        init();
        for (int i = 0; i < RandomUtils.intRange(2, 20); i++) {
            for (int anInt : wordsPlaceHolders.keySet().toArray(new Integer[0])) {
                tryGrow(anInt);
            }
        }
        return matrix;
    }
    
    private void tryGrow(int index) {
        if (!wordsPlaceHolders.containsKey(index)) {
            return;
        }
        List<Position> worm = wordsPlaceHolders.get(index);
        if (worm.size() > length) {
            return;
        }
        Position head = worm.get(0);
        Position tail = worm.get(worm.size() - 1);
        
        List<Directions> possibleDirection = getDirections(tail, index);
        if (possibleDirection.size() != 0) {
            grow(index, possibleDirection, tail);
            return;
            
        }
        possibleDirection = getDirections(head, index);
        if (possibleDirection.size() != 0) {
            List<Position> points = wordsPlaceHolders.get(index);
            Collections.reverse(points);
            wordsPlaceHolders.put(index, points);
            grow(index, possibleDirection, head);
        }
    }
    
    private void grow(int index, List<Directions> possibleDirection, Position tail) {
        Directions direct = possibleDirection.get(RandomUtils.intRange(0, possibleDirection.size() - 1));
        Position otherPoint;
        switch (direct) {
            case LEFT:
                otherPoint = new Position(tail.x, tail.y - 1);
                break;
            case RIGHT:
                otherPoint = new Position(tail.x, tail.y + 1);
                break;
            case TOP:
                otherPoint = new Position(tail.x - 1, tail.y);
                break;
            case BOTTOM:
                otherPoint = new Position(tail.x + 1, tail.y);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + direct);
        }
        int key = matrix[otherPoint.x][otherPoint.y];
        List<Position> keyList = wordsPlaceHolders.get(key);
        List<Position> indexList = wordsPlaceHolders.get(index);
        if ((keyList.size() != 1 && indexList.size() != 1) &&
            (keyList.size() > length || indexList.size() > length ||
                indexList.size() + keyList.size() > length)) {
            return;
        }
        if (!otherPoint.equals(keyList.get(0))) {
            Collections.reverse(keyList);
            wordsPlaceHolders.put(key, keyList);
        }
        
        for (Position point : keyList) {
            matrix[point.x][point.y] = index;
        }
    
        indexList.addAll(keyList);
        wordsPlaceHolders.remove(key);
    }
    
    private List<Directions> getDirections(Position point, int i) {
        List<Directions> possibleDirection = new ArrayList<>();
        if (point.y > 0 && matrix[point.x][point.y - 1] != i && isHead(new Position(point.x, point.y - 1))) {
            possibleDirection.add(Directions.LEFT);
        }
        if (point.y < rows - 1 && matrix[point.x][point.y + 1] != i && isHead(new Position(point.x, point.y + 1))) {
            possibleDirection.add(Directions.RIGHT);
        }
        if (point.x > 0 && matrix[point.x - 1][point.y] != i && isHead(new Position(point.x - 1, point.y))) {
            possibleDirection.add(Directions.TOP);
        }
        if (point.x < columns - 1 && matrix[point.x + 1][point.y] != i && isHead(new Position(point.x + 1, point.y))) {
            possibleDirection.add(Directions.BOTTOM);
        }
        
        return possibleDirection;
    }
    
    private boolean isHead(Position point) {
        if (point.x < 0 || point.y < 0) {
            return false;
        }
        int key = matrix[point.x][point.y];
        List<Position> positions = wordsPlaceHolders.get(key);
        return (positions.get(0).equals(point) ||
            positions.get(positions.size() - 1).equals(point));
    }
    
    private void init() {
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = index;
                List<Position> oneElemList = new ArrayList<>();
                oneElemList.add(new Position(i, j));
                wordsPlaceHolders.put(index, oneElemList);
                index++;
            }
        }
    }
}
