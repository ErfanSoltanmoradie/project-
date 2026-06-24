package model.gameengine;

import model.player.PlayerFactory;
import model.repository.PlayerRepository;
import model.repository.UserRepository;
import model.user.AuthService;
import model.world.WorldMap;
import service.filehandeling.GameState;
import service.filehandeling.LoadService;
import service.filehandeling.SaveService;

import java.io.File;
import java.util.Scanner;

public class GameFlow {

    private final WorldMap worldMap = new WorldMap();
    private  GameState gameState;
    private  UserRepository userRepository ;
    private  PlayerRepository playerRepository ;
    private final PlayerFactory playerFactory;
    private final AuthService authService ;

    private LoadService usersLoadService;
    private LoadService PlayersLoadService;
    private SaveService usersSaveService;
    private SaveService playersSaveService;

    private  File usersFile;
    private File playersFile;

    public GameFlow() {
         this.usersFile = new File("users.dat");
         this.playersFile = new File("players.dat");

        this.gameState = LoadService.load(this.usersFile);
        this.gameState.setUser(this.gameState.getUsers());
        this.userRepository = new UserRepository(this.gameState.getUsers());

        this.gameState = LoadService.load(this.playersFile);
        this.gameState.setPlayer(this.gameState.getPlayers());
        this.playerRepository = new PlayerRepository(this.gameState.getPlayers());

        this.playerFactory = new PlayerFactory(this.worldMap);
        this.authService = new AuthService(this.userRepository, this.playerRepository, this.playerFactory);
    }

    public void register(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username");
        String username = scanner.next();
        System.out.println("Enter your password");
        String password = scanner.next();

        authService.register(username, password);

        gameState.setPlayer(playerRepository.getAllPlayers());
        gameState.setUser(userRepository.getAllUsers());

        //for exiting the game
        SaveService.save(gameState, playersFile);
        SaveService.save(gameState, usersFile);
    }

    public void login(String username, String password){
        authService.login(username, password);

        
        //for exiting the game
        SaveService.save(gameState, playersFile);
        SaveService.save(gameState, usersFile);

    }
}
