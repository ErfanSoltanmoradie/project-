package service.alliance;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AllianceRequestRepository {

    private Map<UUID, AllianceRequest> allianceRequestRepository;

    public AllianceRequestRepository() {
        this.allianceRequestRepository = new HashMap<>();
    }

    public void saveAllianceRequest(AllianceRequest allianceRequest){
        allianceRequestRepository.put(allianceRequest.getId(), allianceRequest);
    }

    public void removeAllianceRequest(AllianceRequest allianceRequest){
        allianceRequestRepository.remove(allianceRequest.getId());
    }

    public Map<UUID, AllianceRequest> getAllAllianceRequests(){
        return allianceRequestRepository;
    }
}
