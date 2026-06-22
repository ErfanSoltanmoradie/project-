package model.building;

import model.world.Coordinate;

public class MajorBuilding extends Building {

    public MajorBuilding(BuildingType type, Coordinate position) {
        super(type, position);
    }

    @Override
    public void upgrade() {
        this.setLevel(this.getLevel() + 1);
    }
}
