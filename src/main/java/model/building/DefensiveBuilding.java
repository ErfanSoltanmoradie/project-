package model.building;

import model.world.Coordinate;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class DefensiveBuilding extends Building {

    public DefensiveBuilding(BuildingType type, Coordinate position) {
        super(type, position);
    }

    public final void upgrade(){
        this.setLevel(this.getLevel() + 1 );
    }

    public abstract int getDefenceStrength();

}
