package model.building;

import model.world.Coordinate;

import java.io.Serializable;

public class MajorBuilding extends Building  implements Serializable {

    public MajorBuilding(BuildingType type, Coordinate position) {
        super(type, position, 1, 1);
    }

    @Override
    public void upgrade() {
        this.setLevel(this.getLevel() + 1);
    }
}
