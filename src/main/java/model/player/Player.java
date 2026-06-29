package model.player;

import model.village.Village;
import model.world.Coordinate;
import service.alliance.Alliance;
import service.alliance.AllianceRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Player  implements Serializable {

    private final String username;
    private final UUID playerId;
    private Village village;
    private UUID userId;
    private final List<AllianceRequest> pendingRequests = new ArrayList<>();
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

    public void addPendingRequest(AllianceRequest allianceRequest) {
        this.pendingRequests.add(allianceRequest);
    }
    public List<AllianceRequest> getPendingRequests() {
        return pendingRequests;
    }

    public void removePendingRequest(AllianceRequest allianceRequest) {
        this.pendingRequests.remove(allianceRequest);
    }

    public int getAllianceCounts() {
        return allianceCounts;
    }

    public void setAllianceCounts(int allianceCounts) {
        this.allianceCounts = allianceCounts;
    }

}