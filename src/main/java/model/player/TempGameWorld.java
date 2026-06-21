package model.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TempGameWorld {

    private Map<UUID, Player> players;

    public TempGameWorld() {
        this.players = new HashMap<>();
    }

    public void addPlayer(Player player){
        players.put(player.getPlayerId(), player);
    }

    public Player getPlayer(UUID playerId){
        return players.get(playerId);
    }

    public void removePlayer(UUID playerId){
        players.remove(playerId);
    }

    public Map<UUID, Player> getPlayers() {
        return players;
    }
}

