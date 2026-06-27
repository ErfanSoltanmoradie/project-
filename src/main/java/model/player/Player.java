package model.player;

import model.village.Village;
import model.world.Coordinate;
import service.alliance.Alliance;

import java.io.Serializable;
import java.util.UUID;

public class Player  implements Serializable {

    private final String username;
    private final UUID playerId;
    private Village village;
    private UUID userId;
    private Alliance alliance;
    private int allianceCounts;

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

    public Alliance getAlliance() {
        return alliance;
    }

    public void setAlliance(Alliance alliance) {
        this.alliance = alliance;
    }

    public int getAllianceCounts() {
        return allianceCounts;
    }

    public void setAllianceCounts(int allianceCounts) {
        this.allianceCounts = allianceCounts;
    }
}

