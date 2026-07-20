package model.user;

import model.player.Player;
import model.player.PlayerFactory;
import model.repository.PlayerRepository;
import model.repository.UserRepository;
import model.world.WorldMap;
import service.filehandeling.GameInitializer;
import service.filehandeling.GameState;
import service.filehandeling.LoadService;
import service.filehandeling.SaveService;

import service.gameManager.GameManager;

import java.io.File;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private ScheduledExecutorService phaseOneScheduler;

    public Start() {

        this.worldMap = new WorldMap();
        this.users = new HashMap<>();
        this.players = new HashMap<>();
        this.playerFactory = new PlayerFactory(worldMap);
        this.gameState = new GameState();
        this.playerRepository = new PlayerRepository(players);
        this.userRepository = new UserRepository(users);
        GameState loaded = LoadService.load(FILE, gameState);
        if (loaded == null) {
            loaded = gameState;
        }
        this.gameState.setUser(loaded.getUsers());
        this.gameState.setPlayer(loaded.getPlayers());
        this.gameState.setGameStartTime(loaded.getGameStartTime());
        this.gameState.setPhaseOneEnforced(loaded.isPhaseOneEnforced());
        this.gameState.setPhaseTwoStartTime(loaded.getPhaseTwoStartTime());
        this.gameState.setPhaseTwoEnforced(loaded.isPhaseTwoEnforced());
        this.gameState.setGameWinner(loaded.getGameWinner());
        this.gameState.setEliminatedUsernames(loaded.getEliminatedUsernames());
        if (this.gameState.getGameStartTime() == null) {

            this.gameState.setGameStartTime(Instant.now());
        }

        this.saveToRepositories();
        GameInitializer.init(this.gameState);
        //this.addProducedResources();

        this.authService = new AuthService(userRepository, playerRepository, playerFactory, gameState);
        this.startPhaseOneWatcher();
    }

    private void startPhaseOneWatcher() {

        this.phaseOneScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "phase-one-watcher");
            t.setDaemon(true);
            return t;
        });

        this.phaseOneScheduler.scheduleAtFixedRate(() -> {
            try {

                List<String> eliminated = GameManager.checkAndEnforcePhaseOneEnd(
                        this.gameState,
                        this.playerRepository,
                        this.userRepository,
                        this.worldMap);

                if (!eliminated.isEmpty()) {
                    System.out.println("Phase 1 ended. Eliminated players: " + eliminated);
                    this.saveAllData();
                }

                List<String> healthEliminated = GameManager.checkAndEnforceHealthElimination(
                        this.gameState, this.playerRepository, this.userRepository, this.worldMap);

                if (!healthEliminated.isEmpty()) {
                    System.out.println("Eliminated due to zero health: " + healthEliminated);
                    this.saveAllData();
                }

                if (GameManager.isPhaseTwoStarted(this.gameState)) {

                    boolean phaseTwoWasEnforced = this.gameState.isPhaseTwoEnforced();

                    String winner = GameManager.checkAndEnforcePhaseTwoEnd(
                            this.gameState,
                            this.playerRepository,
                            this.userRepository,
                            this.worldMap);

                    if (!phaseTwoWasEnforced && this.gameState.isPhaseTwoEnforced()) {
                        System.out.println("Phase 2 ended. Winner: "
                                + (winner != null ? winner : "\"All lands have been destroyed. Humanity has gone extinct.\""));
                        this.saveAllData();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    public void stopPhaseOneWatcher() {
        if (this.phaseOneScheduler != null) {
            this.phaseOneScheduler.shutdownNow();
        }
    }

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

    public GameState getGameState() {
        return gameState;
    }

}