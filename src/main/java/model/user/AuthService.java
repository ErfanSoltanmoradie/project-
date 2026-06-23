package model.user;

import model.repository.PlayerRepository;
import model.repository.UserRepository;
import service.filehandeling.GameState;
import service.filehandeling.LoadService;

import java.io.File;
import java.util.Objects;

public class AuthService {

    private UserManager userManager;

    private static final String USERNAME_PATTERN = "[A-Za-z0-9_]{3,20}";
    private static final String PASSWORD_PATTERN = "[A-Za-z0-9_]{6,20}";

    public AuthService(UserManager userManager) {
        this.userManager = userManager;
    }

    /*File file = new File("users.dat");
    private GameState state = LoadService.load(file);

    UserRepository userRepo = new UserRepository(state.getUser());
    PlayerRepository playerRepo = new PlayerRepository(state.getPlayer());*/


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

        if (userManager.existsUser(username))
            return new AuthResult(AuthStatus.USERNAME_ALREADY_EXIST, null);


        String hash = PasswordHasher.hash(password);
        User user = new User(username, hash);
        userManager.addUser(user);

        return new AuthResult(AuthStatus.SUCCESS, user);
    }

    public AuthResult login(String username, String password) {

        if (username != null) username = username.trim();
        if (password != null) password = password.trim();

        if (!isValidUsername(username))
            return new AuthResult(AuthStatus.INVALID_USERNAME, null);

        if (!isValidPassword(password))
            return new AuthResult(AuthStatus.INVALID_PASSWORD, null);

        if (!userManager.existsUser(username))
            return new AuthResult(AuthStatus.USERNAME_NOT_FOUND, null);

        User user = userManager.findUser(username);
        String enteredHash = PasswordHasher.hash(password);

        if (!Objects.equals(enteredHash, user.getPasswordHash()))
            return new AuthResult(AuthStatus.WRONG_PASSWORD, null);

        return new AuthResult(AuthStatus.SUCCESS, user);
    }
}
