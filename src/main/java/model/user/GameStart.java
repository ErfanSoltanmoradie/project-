package model.user;

import model.player.PlayerFactory;
import model.repository.PlayerRepository;
import model.repository.UserRepository;
import model.world.WorldMap;
import service.filehandeling.GameState;

import java.util.Scanner;

public class GameStart {

    private final WorldMap worldMap = new WorldMap();
    private final GameState gameState = new GameState();
    private final UserRepository userRepository = new UserRepository(gameState.getUsers());
    private final PlayerRepository playerRepository = new PlayerRepository(gameState.getPlayers());
    private final PlayerFactory playerFactory = new PlayerFactory(worldMap);
    private final AuthService authService = new AuthService(userRepository, playerRepository, playerFactory);

    public void register(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your username");
        String username = scanner.next();
        System.out.println("Enter your password");
        String password = scanner.next();

        authService.register(username, password);
    }

    public void login(String username, String password){
        authService.login(username, password);
    }
}
