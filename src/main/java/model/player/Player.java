package model.player;

import model.village.Village;
import model.world.Coordinate;

import java.io.Serializable;
import java.util.UUID;

public class Player  implements Serializable {

    private final String username;
    private final UUID playerId;
    private Village village;
    private UUID userId;

    public Player(String username , Village village) {
        this.username = username;
        this.village = village;
        this.playerId = UUID.randomUUID();
        this.userId = this.playerId;
    }

    public Village getVillage() {
        return village;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setVillage(Village village) {
        this.village = village;

    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}

