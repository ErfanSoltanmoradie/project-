package service.map;

import model.building.Building;

import java.io.Serializable;

public class Tile implements Serializable {

    private final int column;
    private final int row;
    private Building building;

    public Tile(int column, int row) {
        this.column = column;
        this.row = row;
        this.building = null;  // ----> it means that area is free
    }

    public int getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
