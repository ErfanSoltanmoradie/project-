package model.building;

import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public abstract class DefensiveBuilding extends Building  implements Serializable {

    public DefensiveBuilding(BuildingType type, Coordinate position) {
        super(type, position);
    }

    public final void upgrade(){
        this.setLevel(this.getLevel() + 1 );
        this.setBuildingStatus(BuildingStatus.ACTIVE);
    }

    public abstract int getDefenceStrength();

}
