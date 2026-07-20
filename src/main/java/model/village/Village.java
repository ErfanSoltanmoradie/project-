package model.village;


import model.army.Armies;
import model.army.Army;
import model.army.ArmyType;
import model.army.LinkedList;
import model.battle.Battle;
import model.battle.BattleHistory;
import model.building.Building;
import model.building.Plant;
import model.building.*;
import model.finalPart.GlobalTower;
import model.resources.Resources;
import model.time.*;
import model.world.Coordinate;
import service.alliance.AllianceRequest;
import service.buildings.BuildingFactory;
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
    private GameMap gameMap  = new GameMap(150, 150);


    private final Map<UUID, Battle> activeBattles;
    private final LinkedList<BattleHistory> battleHistory;
    //private final Map<UUID, BattleHistory> battleHistory;

    private final List<TradeOffer> receivedTradeRequests = new ArrayList<>();
    private final List<TradeOffer> sentTradeRequests = new ArrayList<>();


    private final Map<UUID, TradeOffer>  tradeOffers = new HashMap<>();
    private AllianceRequest allianceRequest;
    private GlobalTower  globalTower=null;

    private boolean pendingTowerElimination = false;

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
        this.timedOperation.put(randomEventTask.getId(), randomEventTask);

        MajorBuilding majorBuilding = new MajorBuilding(BuildingType.MAJOR_BUILDING, new Coordinate(68, 71));
        this.getBuildings().put(majorBuilding.getId(), majorBuilding);

        ResearchCenter researchCenter = new ResearchCenter(BuildingType.RESEARCH_CENTER, new Coordinate(27, 37));
        this.getBuildings().put(researchCenter.getId(), researchCenter);

        Laboratory laboratory = new Laboratory(BuildingType.LABORATORY, new Coordinate(17, 28));
        this.getBuildings().put(laboratory.getId(), laboratory);


        StorageBuilding stoneStorage = new StorageBuilding(BuildingType.STONE_STORAGE, new Coordinate(36, 104), 2000);
        StorageBuilding woodStorage = new StorageBuilding(BuildingType.WOOD_STORAGE, new Coordinate(27, 103), 2000);
        StorageBuilding ironStorage = new StorageBuilding(BuildingType.IRON_STORAGE, new Coordinate(27, 112), 2000);
        StorageBuilding gunPowderStorage = new StorageBuilding(BuildingType.GUNPOWDER_STORAGE, new Coordinate(36, 113), 2000);
        StorageBuilding waterStorage = new StorageBuilding(BuildingType.WATER_STORAGE, new Coordinate(27, 93), 2000);
        StorageBuilding soilStorage = new StorageBuilding(BuildingType.SOIL_STORAGE, new Coordinate(37, 95), 2000);

        this.getBuildings().put(waterStorage.getId(), waterStorage);
        this.getBuildings().put(soilStorage.getId(), soilStorage);
        this.getBuildings().put(stoneStorage.getId(), stoneStorage);
        this.getBuildings().put(woodStorage.getId(), woodStorage);
        this.getBuildings().put(ironStorage.getId(), ironStorage);
        this.getBuildings().put(gunPowderStorage.getId(), gunPowderStorage);


        waterStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        soilStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        stoneStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        woodStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        gunPowderStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        ironStorage.setBuildingStatus(BuildingStatus.ACTIVE);
        majorBuilding.setBuildingStatus(BuildingStatus.ACTIVE);
        researchCenter.setBuildingStatus(BuildingStatus.ACTIVE);
        laboratory.setBuildingStatus(BuildingStatus.ACTIVE);

        this.gameMap.placeBuilding(waterStorage, 27, 93);
        this.gameMap.placeBuilding(soilStorage, 37, 95);
        this.gameMap.placeBuilding(majorBuilding, 68, 71);
        this.gameMap.placeBuilding(researchCenter, 27, 37);
        this.gameMap.placeBuilding(laboratory, 27, 28);
        this.gameMap.placeBuilding(stoneStorage, 36, 104);
        this.gameMap.placeBuilding(woodStorage, 27, 103);
        this.gameMap.placeBuilding(gunPowderStorage, 36, 113);
        this.gameMap.placeBuilding(ironStorage, 27, 113);

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

    public Map<UUID, TradeOffer> getTradeOffers() {
        return tradeOffers;
    }

    public GlobalTower getGlobalTower() {
        return globalTower;
    }

    public void setGlobalTower(GlobalTower globalTower) {
        this.globalTower = globalTower;
    }

    public boolean isPendingTowerElimination() {
        return pendingTowerElimination;
    }

    public void setPendingTowerElimination(boolean pendingTowerElimination) {
        this.pendingTowerElimination = pendingTowerElimination;
    }

    /*public Map<UUID, BattleHistory> getBattleHistory() {
        return battleHistory;
    }*/
}