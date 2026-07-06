package model.user;


import model.building.Building;
import model.building.BuildingType;
import model.building.MinerBuilding;
import model.player.Player;
import model.player.PlayerFactory;
import model.repository.PlayerRepository;
import model.repository.UserRepository;
import model.resources.ResourcesType;
import model.world.WorldMap;
import service.filehandeling.GameInitializer;
import service.filehandeling.GameState;
import service.filehandeling.LoadService;
import service.filehandeling.SaveService;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Start {

    private WorldMap worldMap;
    private PlayerFactory playerFactory;
    private AuthService authService;
    private Map<String, User> users;
    private Map<UUID, Player> players;
    private PlayerRepository playerRepository;
    private UserRepository userRepository;
    GameState gameState;

    private final File FILE = new File("game.dat");

    public Start() {

        this.worldMap = new WorldMap();
        this.users = new HashMap<>();
        this.players = new HashMap<>();
        this.playerFactory = new PlayerFactory(worldMap);
        this.gameState = new GameState();
        this.playerRepository = new PlayerRepository(players);
        this.userRepository = new UserRepository(users);

        this.gameState.setUser(LoadService.load(FILE, gameState).getUsers());
        this.gameState.setPlayer(LoadService.load(FILE, gameState).getPlayers());
        this.saveToRepositories();
        GameInitializer.init(this.gameState);
        //this.addProducedResources();

        this.authService = new AuthService(userRepository, playerRepository, playerFactory);


    }

    /*private void addProducedResources(){
        for (Player player : this.gameState.getPlayers().values()){

            player.getVillage().getLock().writeLock().lock();
            try {
                long passedTime = System.currentTimeMillis() - player.getLastSeen();
                passedTime /= 1000;

                int producedIron = this.callProducedIron(player, passedTime);
                int producedWood = this.callProducedWood(player, passedTime);
                int producedDirtyWater = this.callProducedDirtyWater(player, passedTime);
                int producedCleanWater = this.callProducedCleanWater(player, passedTime);
                int producedStone = this.callProducedStone(player, passedTime);
                int producedGunPowder = this.callProducedGunPowder(player, passedTime);
                int producedCleanSoil = this.callProducedCleanSoil(player, passedTime);
                int producedDirtySoil = this.callProducedDirtySoil(player, passedTime);

                player.getVillage().getResourcesManagement().addResource(producedIron, ResourcesType.IRON);
                player.getVillage().getResourcesManagement().addResource(producedCleanSoil, ResourcesType.CLEAN_SOIL);
                player.getVillage().getResourcesManagement().addResource(producedDirtySoil, ResourcesType.DIRTY_SOIL);
                player.getVillage().getResourcesManagement().addResource(producedCleanWater, ResourcesType.CLEAN_WATER);
                player.getVillage().getResourcesManagement().addResource(producedStone, ResourcesType.STONE);
                player.getVillage().getResourcesManagement().addResource(producedGunPowder, ResourcesType.GUN_POWDER);
                player.getVillage().getResourcesManagement().addResource(producedWood, ResourcesType.WOOD);
                player.getVillage().getResourcesManagement().addResource(producedDirtyWater, ResourcesType.DIRTY_WATER);

            }finally {
                player.getVillage().getLock().writeLock().unlock();
            }

        }
    }

    private int callProducedWood(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){
            if(building instanceof MinerBuilding){
                if(building.getType() == BuildingType.WOOD_MINE){
                    amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
                }
            }
        }
        return amount;
    }

    private int callProducedIron(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){
            if(building.getType() == BuildingType.IRON_MINE){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedDirtyWater(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.DIRTY_WATER_MINE){
                    amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedCleanWater(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.WATER_PURIFIER){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedCleanSoil(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.SOIL_PURIFIER){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedDirtySoil(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.DIRTY_SOIL_MINE){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedGunPowder(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.GUNPOWDER_MINE){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }

    private int callProducedStone(Player player, long passedTime){
        int amount = 0;
        for (Building building : player.getVillage().getBuildings().values()){

            if(building.getType() == BuildingType.STONE_MINE){
                amount += (int) (passedTime * ((MinerBuilding) building).getProduction());
            }
        }
        return amount;
    }*/



    public void saveToRepositories(){
        for (Player player : this.gameState.getPlayers().values()){
            this.playerRepository.savePlayer(player);
        }

        for (User user : this.gameState.getUsers().values()){
            this.userRepository.save(user);
        }
    }

    public void saveAllData(){

        this.players = playerRepository.getAllPlayers();
        this.users = userRepository.getAllUsers();

        gameState.setPlayer(players);
        gameState.setUser(users);

        for (Player player : playerRepository.getAllPlayers().values()){
            player.setLastSeen(System.currentTimeMillis());
        }

        SaveService.save(gameState, FILE);
    }

    public WorldMap getWorldMap() {
        return worldMap;
    }

    public void setWorldMap(WorldMap worldMap) {
        this.worldMap = worldMap;
    }

    public PlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    public void setPlayerFactory(PlayerFactory playerFactory) {
        this.playerFactory = playerFactory;
    }

    public AuthService getAuthService() {
        return authService;
    }

    public void setAuthService(AuthService authService) {
        this.authService = authService;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public void setUsers(Map<String, User> users) {
        this.users = users;
    }

    public Map<UUID, Player> getPlayers() {
        return players;
    }

    public void setPlayers(Map<UUID, Player> players) {
        this.players = players;
    }

    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    public void setPlayerRepository(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
