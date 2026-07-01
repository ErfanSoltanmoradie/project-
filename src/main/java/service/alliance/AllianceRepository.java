package service.alliance;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllianceRepository {

    private final Map<UUID, Alliance> allianceRepository;

    public AllianceRepository() {
        this.allianceRepository = new HashMap<>();
    }

    public void saveAlliance(Alliance alliance){
        allianceRepository.put(alliance.getAllianceId(), alliance);
    }

    public void removeAlliance(Alliance alliance){
        allianceRepository.remove(alliance.getAllianceId());
    }

    public Map<UUID, Alliance> getAllAlliances(){
        return allianceRepository;
    }
}
