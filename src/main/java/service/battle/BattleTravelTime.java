package service.battle;

import model.village.Village;
import model.world.Coordinate;

import java.time.Duration;

public class BattleTravelTime {

    private static final double DISTANCE_UNIT = 10.0;
    private static final int ONE_HOUR_MINUTES = 60;

    private BattleTravelTime() {}

    public static long calculateTravelTime(Village attackerVillage, Village defenderVillage) {

        Coordinate attackerCoordinate = attackerVillage.getCoordinate();
        Coordinate defenderCoordinate = defenderVillage.getCoordinate();

        double distance = Math.sqrt(
                Math.pow(attackerCoordinate.getX() - defenderCoordinate.getX(), 2) +
                Math.pow(attackerCoordinate.getY() - defenderCoordinate.getY(), 2)
        );

        double travelHours = Math.ceil(distance / DISTANCE_UNIT) / 2.0;

        long travelMinutes = Math.round(travelHours * ONE_HOUR_MINUTES);

        long travelSeconds = Math.round(travelMinutes * 60);

        return 10;
    }
}