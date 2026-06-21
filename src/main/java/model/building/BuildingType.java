package model.building;

import java.time.Duration;

public enum BuildingType {

    WOOD_MINE(new Cost(10, 0, 0, 0,0,0,0, Duration.ofSeconds(1))),
    IRON_MINE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    STONE_MINE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    DIRTY_WATER_MINE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofSeconds(1))),
    DIRTY_SOIL_MINE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofSeconds(1))),

    WATER_PURIFIER(new Cost(40, 30, 20, 0,0,0,0, Duration.ofSeconds(1))),
    SOIL_PURIFIER(new Cost(40, 30, 20, 0,0,0,0, Duration.ofSeconds(1))),

    GUNPOWDER_MINE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    REFINERY(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),

    WOOD_STORAGE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(1))),
    IRON_STORAGE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    STONE_STORAGE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    WATER_STORAGE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofSeconds(1))),
    SOIL_STORAGE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofSeconds(1))),
    GUNPOWDER_STORAGE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),

    BALLISTA_DEFENSIVE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    CATAPULT_DEFENSIVE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    SENTINEL_DEFENSIVE(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    BARRACKS(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),

    LABORATORY(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    RESEARCH_CENTER(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    ARMY_PRODUCER(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    MAJOR_BUILDING(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    PURIFICATION(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10))),
    CUSTOMS(new Cost(40, 30, 20, 0,0,0,0, Duration.ofMinutes(10)));

    BuildingType(Cost baseBuildCost) {
        this.baseBuildCost = baseBuildCost;
    }

    public Cost getBaseBuildCost() {
        return baseBuildCost;
    }

    private final Cost baseBuildCost;
}
