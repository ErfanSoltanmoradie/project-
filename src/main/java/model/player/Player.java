package model.player;

import model.village.Village;
import model.world.Coordinate;
import service.alliance.Alliance;
import service.alliance.AllianceRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Player  implements Serializable {

    private final String username;
    private final UUID playerId;
    private Village village;
    private UUID userId;
    private long lastSeen;
    private boolean onlineStatus = false;

    private final List<AllianceRequest> pendingRequests = new ArrayList<>();
    private Alliance alliance;
    private int allianceCounts;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public Player(String username , Village village) {
        this.username = username;
        this.village = village;
        this.playerId = UUID.randomUUID();
        this.userId = this.playerId;
        this.getVillage().setUserName(this.username);
    }

    public ReentrantReadWriteLock getLock() {
        return lock;
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

    public void addPendingRequest(AllianceRequest allianceRequest) {
        this.pendingRequests.add(allianceRequest);
    }

    public void removePendingRequest(AllianceRequest allianceRequest) {
        this.pendingRequests.remove(allianceRequest);
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public List<AllianceRequest> getPendingRequests() {
        return pendingRequests;
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

