package service.filehandeling;

import model.army.LinkedList;
import model.player.Player;
import model.repository.PlayerRepository;
import model.repository.UserRepository;
import model.user.User;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameState implements Serializable{

    private Map<String, User> user;
    private Map<UUID, Player> player;

    private Instant gameStartTime;
    private boolean phaseOneEnforced = false;

    private Instant phaseTwoStartTime;
    private boolean phaseTwoEnforced = false;
    private String gameWinner;

    private LinkedList<String> eliminatedUsernames = new LinkedList<>();

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

    public Instant getGameStartTime() {
        return gameStartTime;
    }

    public void setGameStartTime(Instant gameStartTime) {this.gameStartTime = gameStartTime;}

    public boolean isPhaseOneEnforced() {
        return phaseOneEnforced;
    }

    public void setPhaseOneEnforced(boolean phaseOneEnforced) {
        this.phaseOneEnforced = phaseOneEnforced;
    }

    public Instant getPhaseTwoStartTime() {return phaseTwoStartTime;}

    public void setPhaseTwoStartTime(Instant phaseTwoStartTime) {this.phaseTwoStartTime = phaseTwoStartTime;}

    public boolean isPhaseTwoEnforced() {return phaseTwoEnforced;}

    public void setPhaseTwoEnforced(boolean phaseTwoEnforced) {this.phaseTwoEnforced = phaseTwoEnforced;}


    public String getGameWinner() {return gameWinner;}

    public void setGameWinner(String gameWinner) {this.gameWinner = gameWinner;}

    public LinkedList<String> getEliminatedUsernames() { return eliminatedUsernames; }
    public void setEliminatedUsernames(LinkedList<String> eliminatedUsernames) {
        this.eliminatedUsernames = eliminatedUsernames;
    }
}