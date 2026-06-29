package model.village;


import model.army.Army;
import model.building.*;
import model.resources.Resources;
import model.time.TimedOperation;
import model.trade.TradeOffer;
import model.world.Coordinate;
import service.alliance.AllianceRequest;
import service.resource.ResourcesManagement;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Village {

    private final UUID villageId;
    private Resources resources;
    private Coordinate coordinate;
    private Map<UUID, Building> buildings;
    private Map<UUID, TimedOperation> timedOperation;
    private final Map<UUID, Plant> plants = new HashMap<>();

    private final ResourcesManagement resourcesManagement;

    private Cloud cloud;
    private Army army;
    private int health;
    private final Map<UUID, TradeOffer>  tradeOffers = new HashMap<>();
    private AllianceRequest allianceRequest;

    /*private int scienceLevel;
    private int majorBuildingLevel;*/

    public Village(Coordinate coordinate, int health) {
        this.villageId = UUID.randomUUID();
        this.resources = new Resources();

        this.resourcesManagement = new ResourcesManagement(this);

        this.buildings = new HashMap<>();
        this.timedOperation = new HashMap<>();
        this.coordinate = coordinate;

        this.cloud = new Cloud();
        this.health = health;
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

    public Map<UUID, Plant> getPlant() {
        return plants;
    }

    public Map<UUID, TradeOffer> getTradeOffers() {
        return tradeOffers;
    }

}

