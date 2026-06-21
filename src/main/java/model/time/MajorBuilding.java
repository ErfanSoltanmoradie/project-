package model.time;

import model.building.Building;
import model.building.BuildingType;
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
