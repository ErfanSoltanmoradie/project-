package model.repository;

import model.player.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerRepository {

    private Map<UUID, Player> playerRepository;

    public PlayerRepository(Map<UUID, Player> playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void savePlayer(Player player){
        this.playerRepository.put(player.getPlayerId(), player);
    }

    public boolean isPlayerExists(UUID uuid){
        return this.playerRepository.containsKey(uuid);
    }

    public Player findPlayerById(UUID uuid){
        if(this.isPlayerExists(uuid))
            return this.playerRepository.get(uuid);
        return null;
    }

    public Map<UUID, Player> getAllPlayers(){
        return this.playerRepository;
    }
}
