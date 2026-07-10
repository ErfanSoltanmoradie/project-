package service.alliance;

import model.building.*;
import model.player.Player;


import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class AllianceService {

    public void sendRequest(Player sender, Player receiver){

        AllianceService.lockPlayers(sender, receiver);
        try {
            AllianceService.lockVillages(sender, receiver);
            try {

                if(!this.checkMajorBuildingLevel(sender, receiver)){
                    return;
                }

                if(this.hasAlliance(sender, receiver) || sender.getPlayerId().equals(receiver.getPlayerId()) || isRequestAlreadyExist(sender, receiver))
                    return;

                if(!this.checkCloudForAlliance(sender, receiver)) return;

                if(!this.checkAllianceCost(sender, receiver))
                    return;

                if (sender.getPendingRequests() != null) {
                    for (AllianceRequest req : sender.getPendingRequests()) {
                        if (req.getReceiver().equals(receiver) && req.getAllianceStatus() == AllianceStatus.PENDING) {
                            return;
                        }
                    }
                }

                if (receiver.getPendingRequests() != null) {
                    for (AllianceRequest req : receiver.getPendingRequests()) {
                        if (req.getSender().equals(receiver) && req.getReceiver().equals(sender) && req.getAllianceStatus() == AllianceStatus.PENDING) {
                            return;
                        }
                    }
                }


                AllianceRequest allianceRequest = new AllianceRequest(sender, receiver);
                this.addPendingRequest(allianceRequest, sender, receiver);
            }finally {
                AllianceService.unlockVillages(sender, receiver);
            }
        }finally {
            AllianceService.unlockPlayers(sender, receiver);
        }
    }

    private void addPendingRequest(AllianceRequest allianceRequest, Player sender, Player receiver){
        sender.addPendingRequest(allianceRequest);
        receiver.addPendingRequest(allianceRequest);
    }

    private boolean checkCloudForAlliance(Player sender, Player receiver){

        if(sender.getVillage().getCloud().getNeutralized()<200) return false;
        if(receiver.getVillage().getCloud().getNeutralized()<200) return false;

        return true;
    }



    public void acceptRequest(AllianceRequest allianceRequest){
        if(allianceRequest==null) return;



        AllianceService.lockPlayers(allianceRequest.getSender(), allianceRequest.getReceiver());
        try {
            AllianceService.lockVillages(allianceRequest.getSender(), allianceRequest.getReceiver());
            try {
                if(!this.checkCloudForAlliance(allianceRequest)) return;

                if(!this.checkMajorBuildingLevel(allianceRequest.getSender(), allianceRequest.getReceiver()))
                    return;

                if(this.checkAllianceCost(allianceRequest.getSender(), allianceRequest.getReceiver())) {
                    this.withdrawAllianceCost(allianceRequest.getSender(), allianceRequest.getReceiver());
                    Duration transferTime = Duration.ofHours(1);
                    AllianceTask allianceTask = new AllianceTask(Instant.now(), Duration.ofSeconds(5), allianceRequest);

                    this.removePendingRequests(allianceRequest);

                    allianceRequest.getSender().getVillage().getTimedOperation().put(allianceTask.getId(), allianceTask);
                    this.cleanUpAllPendingRequests(allianceRequest.getSender(), allianceRequest.getReceiver(), allianceRequest);
                    return;
                }

            }finally {
                AllianceService.unlockVillages(allianceRequest.getSender(), allianceRequest.getReceiver());
            }
        }finally {
            AllianceService.unlockPlayers(allianceRequest.getSender(), allianceRequest.getReceiver());
        }

        rejectRequest(allianceRequest);

    }

    private void cleanUpAllPendingRequests(Player sender, Player receiver, AllianceRequest acceptedRequest) {

        acceptedRequest.setAllianceStatus(AllianceStatus.ACCEPTED);
        cancelAllPlayerPendingRequests(sender);
        cancelAllPlayerPendingRequests(receiver);

    }

    private void cancelAllPlayerPendingRequests(Player player) {

        if (player == null || player.getPendingRequests() == null) return;

        List<AllianceRequest> snapshot = new ArrayList<>(player.getPendingRequests());

        for (AllianceRequest req : snapshot) {
            req.setAllianceStatus(AllianceStatus.REJECTED);

            if (req.getSender() != null) {
                req.getSender().removePendingRequest(req);
            }
            if (req.getReceiver() != null) {
                req.getReceiver().removePendingRequest(req);
            }
        }
    }

    private void removePendingRequests(AllianceRequest allianceRequest){
        allianceRequest.getSender().removePendingRequest(allianceRequest);
        allianceRequest.getReceiver().removePendingRequest(allianceRequest);
    }

    private void withdrawAllianceCost(Player senderPlayer, Player receiverPlayer){
        senderPlayer.getVillage().getResourcesManagement().withdrawResourcesCost(Cost.allianceCost());
        receiverPlayer.getVillage().getResourcesManagement().withdrawResourcesCost(Cost.allianceCost());
    }

    public boolean checkAllianceCost(Player senderPlayer, Player receiverPlayer){

        if(!receiverPlayer.getVillage().getResourcesManagement().checkResourcesCost(Cost.allianceCost()))
            return false;

        if(!senderPlayer.getVillage().getResourcesManagement().checkResourcesCost(Cost.allianceCost())){
           return false;
        }
        return true;
    }


    public static void lockPlayers(Player p1, Player p2) {

        if (p1.getPlayerId().compareTo(p2.getPlayerId()) < 0) {
            p1.getLock().writeLock().lock();
            p2.getLock().writeLock().lock();
        } else {
            p2.getLock().writeLock().lock();
            p1.getLock().writeLock().lock();
        }
    }

    public static void unlockPlayers(Player p1, Player p2) {
        p1.getLock().writeLock().unlock();
        p2.getLock().writeLock().unlock();
    }

    public static void lockVillages(Player p1, Player p2) {
        if (p1.getPlayerId().compareTo(p2.getPlayerId()) < 0) {
            p1.getVillage().getLock().writeLock().lock();
            p2.getVillage().getLock().writeLock().lock();
        } else {
            p2.getVillage().getLock().writeLock().lock();
            p1.getVillage().getLock().writeLock().lock();
        }
    }

    public static void unlockVillages(Player p1, Player p2) {
        p1.getVillage().getLock().writeLock().unlock();
        p2.getVillage().getLock().writeLock().unlock();
    }

    public void rejectRequest(AllianceRequest allianceRequest){
        if (allianceRequest != null) {
            allianceRequest.setAllianceStatus(AllianceStatus.REJECTED);

            if(allianceRequest.getSender() != null && allianceRequest.getReceiver() != null)
                this.removePendingRequests(allianceRequest);

        }

    }

    private boolean checkCloudForAlliance(AllianceRequest allianceRequest){

        allianceRequest.getSender().getVillage().getLock().readLock().lock();

        if(allianceRequest.getSender().getVillage().getCloud().getNeutralized()<200) return false;


        allianceRequest.getReceiver().getVillage().getLock().readLock().lock();

        if(allianceRequest.getReceiver().getVillage().getCloud().getNeutralized()<200) return false;

        return true;
    }


    public boolean isRequestAlreadyExist(Player sender, Player receiver) {

        for (AllianceRequest request : sender.getPendingRequests()) {
            if (request.getReceiver().getPlayerId().equals(receiver.getPlayerId())) {
                return true;
            }
        }
        
        for (AllianceRequest request : receiver.getPendingRequests()) {
            if (request.getSender().getPlayerId().equals(sender.getPlayerId())) {
                return true;
            }
        }

        return false;
    }

    public boolean hasAlliance(Player senderPlayer, Player receiverPlayer){

        if (senderPlayer.getAlliance() != null || receiverPlayer.getAlliance() != null) {
            return true;
        }
        return false;
    }

    public boolean checkMajorBuildingLevel(Player senderPlayer, Player receiverPlayer){
        Building receiverPlayerMajorBuilding= null;
        Building senderPlayerMajorBuilding = null;

        for(Building building : receiverPlayer.getVillage().getBuildings().values()){
            if(building.getType() == BuildingType.MAJOR_BUILDING){
                receiverPlayerMajorBuilding = building;
                break;
            }
        }

        for(Building building : senderPlayer.getVillage().getBuildings().values()){
            if(building.getType() == BuildingType.MAJOR_BUILDING){
                senderPlayerMajorBuilding = building;
                break;
            }
        }

        if (receiverPlayerMajorBuilding == null || senderPlayerMajorBuilding == null)
            return false;

        if(senderPlayerMajorBuilding.getLevel() >= 2 && receiverPlayerMajorBuilding.getLevel() >= 2)
            return true;

        return false;
    }
}