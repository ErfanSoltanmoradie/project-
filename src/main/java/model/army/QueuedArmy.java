package model.army;

import java.io.Serializable;
import java.time.Instant;

public record QueuedArmy(
        ArmyType armyType,
        Instant finishTime

) implements Serializable {
}

