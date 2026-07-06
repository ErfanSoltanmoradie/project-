package model.user;

import model.player.Player;
import model.player.PlayerFactory;
import model.repository.PlayerRepository;
import model.repository.UserRepository;
import model.village.Village;
import model.world.WorldMap;

import java.util.Objects;

public class AuthService {

    private static final String USERNAME_PATTERN = "[A-Za-z0-9_]{3,20}";
    private static final String PASSWORD_PATTERN = "[A-Za-z0-9_]{6,20}";

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final PlayerFactory playerFactory;

    public AuthService(UserRepository userRepository,
                       PlayerRepository playerRepository, PlayerFactory playerFactory) {
        this.userRepository = userRepository;
        this.playerRepository = playerRepository;
        this.playerFactory = playerFactory;
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public PlayerRepository getPlayerRepository() {
        return playerRepository;
    }

    public PlayerFactory getPlayerFactory() {
        return playerFactory;
    }

    private boolean isValidUsername(String username) {
        if (username == null) return false;
        if (!username.matches(USERNAME_PATTERN)) return false;
        return true;
    }

    private boolean isValidPassword(String password) {
        if (password == null) return false;
        if (!password.matches(PASSWORD_PATTERN)) return false;
        return true;
    }

    public AuthResult register(String username, String password) {

        if (username != null) username = username.trim();
        if (password != null) password = password.trim();

        if (!isValidUsername(username))
            return new AuthResult(AuthStatus.INVALID_USERNAME, null);

        if (!isValidPassword(password))
            return new AuthResult(AuthStatus.INVALID_PASSWORD, null);

        if(userRepository.exists(username))
            return new AuthResult(AuthStatus.USERNAME_ALREADY_EXIST, null);


        String hash = PasswordHasher.hash(password);

        Player player = playerFactory.createPlayer(username);
        User user = new User(username, hash, player.getPlayerId());
        playerRepository.savePlayer(player);
        userRepository.save(user);

        return new AuthResult(AuthStatus.SUCCESS, player);
    }

    public AuthResult login(String username, String password) {

        if (username != null) username = username.trim();
        if (password != null) password = password.trim();

        if (!isValidUsername(username))
            return new AuthResult(AuthStatus.INVALID_USERNAME, null);

        if (!isValidPassword(password))
            return new AuthResult(AuthStatus.INVALID_PASSWORD, null);

        if(!userRepository.exists(username))
            return new AuthResult(AuthStatus.USERNAME_NOT_FOUND, null);

        User user = userRepository.findUserByUsername(username);
        String enteredHash = PasswordHasher.hash(password);

        if (!Objects.equals(enteredHash, user.getPasswordHash()))
            return new AuthResult(AuthStatus.WRONG_PASSWORD, null);

        Player player = playerRepository.findPlayerById(user.getId());
        return new AuthResult(AuthStatus.SUCCESS, player);
    }
}
