package service.filehandeling;

import model.player.Player;
import model.repository.PlayerRepository;
import model.repository.UserRepository;
import model.user.User;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameState implements Serializable{

    private Map<String, User> user;
    private Map<UUID, Player> player;

    public GameState() {
        this.user = new HashMap<>();
        this.player = new HashMap<>();
    }

    public Map<String, User> getUsers() {
        return user;
    }

    public void setUser(Map<String, User> user) {
        this.user = user;
    }

    public Map<UUID, Player> getPlayers() {
        return player;
    }

    public void setPlayer(Map<UUID, Player> player) {
        this.player = player;
    }
}
