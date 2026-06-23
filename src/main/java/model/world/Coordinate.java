package model.world;

import java.io.Serializable;

public class Coordinate implements Serializable {
    private int x;
    private int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinate={" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Coordinate)) return false;
        Coordinate other = (Coordinate) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    public int getX() {return x;}

    public int getY() {return y;}
}
