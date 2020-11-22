package net.luckyvalenok.fillwords;

public class Position {
    public int x;
    public int y;
    
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public boolean equals(Position b) {
        return x == b.x && y == b.y;
    }
}