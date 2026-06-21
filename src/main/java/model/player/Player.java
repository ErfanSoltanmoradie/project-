package model.player;

import model.village.Village;
import model.world.Coordinate;

import java.util.UUID;

public class Player {

    private final String username;
    private final String passwordHash;
    private final UUID playerId;
    private final Village village;

    public Player(String username, String passwordHash, Village village) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.village = village;
        this.playerId = UUID.randomUUID();
    }

    public Village getVillage() {
        return village;
    }

    public UUID getPlayerId() {
        return playerId;
    }
}
