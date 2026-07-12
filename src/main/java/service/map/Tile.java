package service.map;

import model.building.Building;
import model.building.Plant;

import java.io.Serializable;
import java.lang.reflect.Type;

public class Tile implements Serializable {

    public enum Type{PLAYABLE, BORDER, BORDER_WITH_TREE};

    public enum DecorateType{
        NONE,
        BROWN_TREE,
        WHITE_TREE,
        ROCK
    }

    private final int column;
    private final int row;
    private Building building;
    private Plant plant;
    private Object object;
    private Type type = Type.PLAYABLE;
    private DecorateType decorateType =DecorateType.NONE;

    public Tile(int column, int row) {
        this.column = column;
        this.row = row;
        this.building = null;  // ----> it means that area is free
        this.plant = null;
    }

    public DecorateType getDecorateType() {
        return decorateType;
    }

    public void setDecorateType(DecorateType decorateType) {
        this.decorateType = decorateType;
    }

    public boolean isBuildable(){
        if(this.type == Type.PLAYABLE)
            return true;

        return false;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
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

    public Plant getPlant() {
        return plant;
    }

    public void setPlant(Plant plant){
        this.plant = plant;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
