package model.time;

import model.finalPart.GlobalTower;
import model.finalPart.GlobalTowerAnnouncer;
import model.village.Village;
import model.world.Coordinate;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;

public class BuildGlobalTowerTask extends TimedOperation implements Serializable{
    private final Village village;
    private final Coordinate coordinate;

    public BuildGlobalTowerTask(Instant startTime, Duration neededTime, Village village, Coordinate coordinate) {
        super(startTime, neededTime, TimedOperationType.BUILD_TASK);
        this.village = village;
        this.coordinate = coordinate;
    }

    @Override
    public TaskResult execute() {
        TaskResult taskResult = new TaskResult();
        GlobalTower tower = new GlobalTower();
        tower.active();
        tower.setBuilderVillageUsername(village.getUserName());

        village.getGameMap().placeGlobalTower(tower, coordinate.getX(), coordinate.getY());
        village.setGlobalTower(tower);

        GlobalTowerAnnouncer.announceTowerBuilt(village.getUserName());

        return taskResult;
    }
}