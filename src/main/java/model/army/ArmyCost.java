package model.army;

import java.time.Duration;

public record ArmyCost (
        int wood,
        int stone,
        int iron,
        int gunPowder,
        Duration neededTime
){}
