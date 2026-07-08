package model.army;

import java.time.Instant;

public record QueuedArmy(
        ArmyType armyType,
        Instant finishTime
){}
