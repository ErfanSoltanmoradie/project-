package model.village;


import model.army.Army;
import model.building.Building;
import model.building.BuildingType;
import model.building.StorageBuilding;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.time.RandomEventTask;
import model.time.TaskProcessor;
import model.time.TimedOperation;
import model.time.TimedOperationType;
import model.world.Coordinate;
import service.buildings.BuildingFactory;
import service.buildings.BuildingsManagement;
import service.resource.ResourcesManagement;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Village implements Serializable {

    private final UUID villageId;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Resources resources;
    private Coordinate coordinate;
    private Map<UUID, Building> buildings;
    private Map<UUID, TimedOperation> timedOperation;
    private transient  ResourcesManagement resourcesManagement; // transient ---> do not save it in the file
    private Cloud cloud;
    private Army army;
    private int health;


    public Village(Coordinate coordinate, int health) {
        this.villageId = UUID.randomUUID();
        this.resources = new Resources();
        this.buildings = new HashMap<>();
        this.timedOperation = new HashMap<>();
        this.cloud = new Cloud();
        this.coordinate = coordinate;
        this.health = health;
        this.resourcesManagement = new ResourcesManagement(this);
        RandomEventTask randomEventTask = new RandomEventTask(Instant.now(), Duration.ofMinutes(1), TimedOperationType.RANDOM_EVENT_TASK);
    }

    public void runTimeServices(){  // we want the logic after loading the game
        this.resourcesManagement = new ResourcesManagement(this);
        //TaskProcessor taskProcessor = new TaskProcessor(this);
    }

    public UUID getVillageId() {
        return villageId;
    }

    public Resources getResources() {
        return resources;
    }

    public void setResources(Resources resources) {
        this.resources = resources;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Map<UUID, Building> getBuildings() {
        return buildings;
    }

    public void setBuildings(Map<UUID, Building> buildings) {
        this.buildings = buildings;
    }

    public Map<UUID, TimedOperation> getTimedOperation() {
        return timedOperation;
    }

    public void setTimedOperation(Map<UUID, TimedOperation> timedOperation) {
        this.timedOperation = timedOperation;
    }

    public ResourcesManagement getResourcesManagement() {
        return resourcesManagement;
    }

    public Cloud getCloud() {
        return cloud;
    }

    public void setCloud(Cloud cloud) {
        this.cloud = cloud;
    }

    public Army getArmy() {
        return army;
    }

    public void setArmy(Army army) {
        this.army = army;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public ReentrantReadWriteLock getLock() {
        return lock;
    }
}

