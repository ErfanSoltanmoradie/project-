package model.village;


import model.army.Armies;
import model.army.Army;

import model.army.LinkedList;
import model.battle.Battle;
import model.battle.BattleHistory;
import model.building.Building;
import model.building.Plant;

import model.building.*;

import model.resources.Resources;
import model.time.RandomEventTask;
import model.time.TimedOperation;
import model.time.TimedOperationType;
import model.world.Coordinate;

import service.alliance.AllianceRequest;
import service.buildings.BuildingFactory;
import service.buildings.BuildingsManagement;

import service.filehandeling.LoadService;
import service.map.GameMap;
import service.resource.ResourcesManagement;
import service.trade.TradeOffer;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Village implements Serializable {

    private  String userName;
    private final UUID villageId;
    private Resources resources;
    private Coordinate coordinate;
    private Map<UUID, Building> buildings;
    private Map<UUID, TimedOperation> timedOperation;
    private transient  ResourcesManagement resourcesManagement; // transient ---> do not save it in the file
    private transient LoadService loadService;
    private Cloud cloud;
    private Army army;
    private Armies armies;
    private int health;

    private final Map<UUID, Plant> plants = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private GameMap gameMap = new GameMap(70, 70, 10);


    private final Map<UUID, Battle> activeBattles;
    private final LinkedList<BattleHistory> battleHistory;


    private final List<TradeOffer> receivedTradeRequests = new ArrayList<>();
    private final List<TradeOffer> sentTradeRequests = new ArrayList<>();


    private final Map<UUID, TradeOffer>  tradeOffers = new HashMap<>();
    private AllianceRequest allianceRequest;

    public GameMap getGameMap() {
        return gameMap;
    }

    public Village(Coordinate coordinate, int health) {
        this.villageId = UUID.randomUUID();
        this.resources = new Resources();
        this.buildings = new HashMap<>();
        this.timedOperation = new HashMap<>();
        this.coordinate = coordinate;
        this.cloud = new Cloud();
        this.health = health;
        this.resourcesManagement = new ResourcesManagement(this);
        this.armies = new Armies();

        this.activeBattles = new HashMap<>();
        this.battleHistory = new LinkedList<>();

        RandomEventTask randomEventTask = new RandomEventTask(Instant.now(), Duration.ofMinutes(1), TimedOperationType.RANDOM_EVENT_TASK);

        Customhouse customhouse = new Customhouse(BuildingType.CUSTOMHOUSE, new Coordinate(20,  10));
        MajorBuilding majorBuilding = new MajorBuilding(BuildingType.MAJOR_BUILDING, new Coordinate(12, 15));
        ResearchCenter researchCenter = new ResearchCenter(BuildingType.RESEARCH_CENTER, new Coordinate(5, 9));
        majorBuilding.setLevel(5);
        researchCenter.setLevel(5);
        this.getBuildings().put(majorBuilding.getId(), majorBuilding);
        this.getBuildings().put(researchCenter.getId(), researchCenter);
        this.getBuildings().put(customhouse.getId(), customhouse);
    }

    public void runTimeServices(){  // we want the logic after loading the game
        this.resourcesManagement = new ResourcesManagement(this);
        this.loadService = new LoadService();
    }

    public List<TradeOffer> getReceivedTradeRequests() {
        return receivedTradeRequests;
    }

    public List<TradeOffer> getSentTradeRequests() {
        return sentTradeRequests;
    }

    public Map<UUID, TradeOffer> getTradeOffers() {
        return tradeOffers;
    }

    public AllianceRequest getAllianceRequest() {
        return allianceRequest;
    }

    public void setAllianceRequest(AllianceRequest allianceRequest) {
        this.allianceRequest = allianceRequest;
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

    public Armies getArmies(){return armies;}

    public void setArmy(Army army) {
        this.army = army;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }


    public void decreaseHealth(int amount){
        this.health = Math.max(0, this.health - amount);
    }

    public Map<UUID, Battle> getActiveBattles() {
        return activeBattles;
    }

    public LinkedList<BattleHistory> getBattleHistory() {
        return battleHistory;
    }


    public ReentrantReadWriteLock getLock() {
        return lock;
    }

    public Map<UUID, Plant> getPlants() {
        return plants;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /*public Map<UUID, TradeOffer> getTradeOffers() {
        return tradeOffers;
    }*/
}

