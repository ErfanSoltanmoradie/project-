package service.trade;

import model.building.Building;
import model.building.Customhouse;
import model.player.Player;
import model.resources.ResourcesType;

import model.village.Village;
import service.alliance.AllianceService;
import service.resource.ResourcesManagement;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class TradeService {
    private final UUID id;
    private Village village;
    private Customhouse customhouse;
    private ResourcesManagement resourcesManagement;

    /*public TradeService(UUID id , ResourcesManagement resourcesManagement) {
        this.id = id;
        this.resourcesManagement = resourcesManagement;
    }*/

    public TradeService() {
        this.id = UUID.randomUUID();
    }


    public void sendRequest(Player sender, Player receiver, Map<ResourcesType, Integer> offered, Map<ResourcesType, Integer> requested) {

        Village senderVillage = sender.getVillage();
        Village receiverVillage = receiver.getVillage();

        AllianceService.lockPlayers(sender, receiver);
        try {
            AllianceService.lockVillages(sender, receiver);
            try {
                boolean senderHasCustoms = false;
                for (Building building : senderVillage.getBuildings().values()) {
                    if (building instanceof Customhouse) {
                        senderHasCustoms = true;
                        break;
                    }
                }
                if (!senderHasCustoms) {
                    return;
                }

                boolean receiverHasCustoms = false;
                for (Building building : receiverVillage.getBuildings().values()) {
                    if (building instanceof Customhouse) {
                        receiverHasCustoms = true;
                        break;
                    }
                }
                if (!receiverHasCustoms) {
                    return;
                }

                TradeOffer tradeOffer = new TradeOffer(senderVillage, receiverVillage, sender, receiver, offered, requested);// for making & showing an offer in receiverVillage
                sender.getVillage().getSentTradeRequests().add(tradeOffer);
                receiver.getVillage().getReceivedTradeRequests().add(tradeOffer);

                for (Map.Entry<ResourcesType, Integer> entry : offered.entrySet()) {
                    ResourcesType type = entry.getKey();
                    Integer amount = entry.getValue();
                    if (senderVillage.getResources().getAmount(type) < amount) {
                        return;
                    }
                }

                for (Map.Entry<ResourcesType, Integer> entry : tradeOffer.getOfferedResources().entrySet()) {
                    ResourcesType type = entry.getKey();
                    int originalAmount = entry.getValue();

                    tradeOffer.getSenderVillage().getResourcesManagement().withdrawResource(originalAmount, type);
                }

                for (Map.Entry<ResourcesType, Integer> entry : requested.entrySet()) {
                    ResourcesType type = entry.getKey();
                    Integer amount = entry.getValue();
                    if (receiverVillage.getResourcesManagement().getMaxCapacity(type) < amount) {
                        return;
                    }
                }

                tradeOffer.setTradeStatus(TradeStatus.PENDING);

            } finally {
                AllianceService.unlockVillages(sender, receiver);
            }
        } finally {
            AllianceService.unlockPlayers(sender, receiver);
        }

        /*senderVillage.getLock().writeLock().lock();
        try {
            boolean senderHasCustoms= false;
            for(Building building : senderVillage.getBuildings().values()){
                if(building instanceof Customhouse){
                    senderHasCustoms=true;
                    break;
                }
            }
            if (!senderHasCustoms){return;}
        }finally {
            senderVillage.getLock().writeLock().unlock();
        }

        receiverVillage.getLock().writeLock().lock();
        try {
            boolean receiverHasCustoms= false;
            for(Building building : receiverVillage.getBuildings().values()){
                if(building instanceof Customhouse){
                    receiverHasCustoms=true;
                    break;
                }
            }if (!receiverHasCustoms){return;}
        }finally {
            receiverVillage.getLock().writeLock().unlock();
        }

        TradeOffer tradeOffer = new TradeOffer(senderVillage,receiverVillage,sender,receiver,offered,requested);// for making & showing an offer in receiverVillage

        senderVillage.getLock().writeLock().lock();
        try {
            for(Map.Entry<ResourcesType,Integer> entry : offered.entrySet()){
                ResourcesType type = entry.getKey();
                Integer amount = entry.getValue();
                if(senderVillage.getResources().getAmount(type) < amount){return;}
            }
        }finally {
            senderVillage.getTradeOffers().put(tradeOffer.getUuid(), tradeOffer);
            senderVillage.getLock().writeLock().unlock();
        }


        senderVillage.getLock().writeLock().lock();
        try {
            for(Map.Entry<ResourcesType, Integer> entry : tradeOffer.getOfferedResources().entrySet()){
                ResourcesType type = entry.getKey();
                int originalAmount = entry.getValue();

                tradeOffer.getSenderVillage().getResourcesManagement().withdrawResource(originalAmount, type);
            }
        }finally {
            senderVillage.getLock().writeLock().unlock();
        }

        receiverVillage.getLock().writeLock().lock();
        try {
            for(Map.Entry<ResourcesType,Integer> entry : requested.entrySet()){
                ResourcesType type = entry.getKey();
                Integer amount = entry.getValue();
                if(receiverVillage.getResourcesManagement().getMaxCapacity(type) < amount){return;}
            }
        }finally {
            receiverVillage.getTradeOffers().put(tradeOffer.getUuid(), tradeOffer);
            receiverVillage.getLock().writeLock().unlock();
        }*/
    }


    public void acceptOffer(TradeOffer offer) {
        if (offer == null || offer.getTradeStatus() != TradeStatus.PENDING) {
            return;
        }

        offer.setTradeStatus(TradeStatus.ACCEPTED);

        Village senderVillage = offer.getSenderVillage();
        Village receiverVillage = offer.getReceiverVillage();

        Customhouse senderCustomhouse = null;
        Customhouse receiverCustomhouse = null;


        AllianceService.lockVillages(offer.getAlliancesender(), offer.getAlliancesreceiver());
        try {
            for (Building building1 : offer.getSenderVillage().getBuildings().values()) {
                if (building1 instanceof Customhouse customhouse) {
                    senderCustomhouse = customhouse;
                    break;
                }
            }

            for (Building building2 : offer.getReceiverVillage().getBuildings().values()) {
                if (building2 instanceof Customhouse customhouse) {
                    receiverCustomhouse = customhouse;
                    break;
                }
            }

            boolean areAllied = offer.getAlliancesender().getAlliance() != null &&
                    (offer.getAlliancesender().getAlliance().getSender().equals(offer.getAlliancesreceiver()) ||
                            offer.getAlliancesender().getAlliance().getReceiver().equals(offer.getAlliancesreceiver()));

            double senderTaxRate = 0;

            senderTaxRate = areAllied ? 0 : (senderCustomhouse != null ? senderCustomhouse.getCommission() : 0);

            double receiverTaxRate = 0;

            receiverTaxRate = areAllied ? 0 : (receiverCustomhouse != null ? receiverCustomhouse.getCommission() : 0);
            for (Map.Entry<ResourcesType, Integer> entry : offer.getOfferedResources().entrySet()) {
                ResourcesType type = entry.getKey();
                int originalAmount = entry.getValue();
                int amountWithTax = (int) (originalAmount * senderTaxRate);
                offer.getSenderVillage().getResourcesManagement().withdrawResource(amountWithTax, type);
            }

            for (Map.Entry<ResourcesType, Integer> entry : offer.getRequestedResources().entrySet()) {
                ResourcesType type = entry.getKey();
                int originalAmount = entry.getValue();
                int amountWithTax = originalAmount + (int) (originalAmount * receiverTaxRate);
                offer.getReceiverVillage().getResourcesManagement().withdrawResource(amountWithTax, type);
            }

            TradeTask tradeTask = new TradeTask(Instant.now(), Duration.ofSeconds(4), offer);

            offer.getSenderVillage().getTimedOperation().put(tradeTask.getId(), tradeTask);
            offer.setTradeStatus(TradeStatus.ACCEPTED);

            offer.getReceiverVillage().getReceivedTradeRequests().remove(offer);
            offer.getSenderVillage().getSentTradeRequests().remove(offer);

        } finally {
            AllianceService.unlockVillages(offer.getAlliancesender(), offer.getAlliancesreceiver());
        }
    }


        /*senderVillage.getLock().writeLock().lock();
        try {
            for(Building building1 : offer.getSenderVillage().getBuildings().values()){
                if(building1 instanceof Customhouse customhouse){senderCustomhouse = customhouse ;break;}
            }
        }finally {
            senderVillage.getLock().writeLock().unlock();
        }

        receiverVillage.getLock().writeLock().lock();
        try {

            for(Building building2 : offer.getReceiverVillage().getBuildings().values()){
                if(building2 instanceof Customhouse customhouse){receiverCustomhouse = customhouse ;break;}
            }
        }finally {
            receiverVillage.getLock().writeLock().unlock();
        }

        boolean areAllied = offer.getAlliancesender().getAlliance() != null &&
                (offer.getAlliancesender().getAlliance().getSender().equals(offer.getAlliancesreceiver()) ||
                        offer.getAlliancesender().getAlliance().getReceiver().equals(offer.getAlliancesreceiver()));

        double senderTaxRate = 0;
        senderVillage.getLock().writeLock().lock();
        try {
            senderTaxRate = areAllied ? 0 : (senderCustomhouse != null ? senderCustomhouse.getCommission() : 0);
        }finally {
            senderVillage.getLock().writeLock().unlock();
        }

        double receiverTaxRate = 0;
        receiverVillage.getLock().writeLock().lock();
        try {
            receiverTaxRate = areAllied ? 0 : (receiverCustomhouse != null ? receiverCustomhouse.getCommission() : 0);
        }finally {
            receiverVillage.getLock().writeLock().unlock();
        }


        senderVillage.getLock().writeLock().lock();
        try {
            for(Map.Entry<ResourcesType, Integer> entry : offer.getOfferedResources().entrySet()){
                ResourcesType type = entry.getKey();
                int originalAmount = entry.getValue();
                int amountWithTax =  (int)(originalAmount * senderTaxRate);
                offer.getSenderVillage().getResourcesManagement().withdrawResource(amountWithTax, type);
            }
        }finally {
            senderVillage.getLock().writeLock().unlock();
        }

        receiverVillage.getLock().writeLock().lock();
        try {
            for(Map.Entry<ResourcesType,Integer> entry : offer.getRequestedResources().entrySet()) {
                ResourcesType type = entry.getKey();
                int originalAmount = entry.getValue();
                int amountWithTax = originalAmount + (int)(originalAmount * receiverTaxRate);
                offer.getReceiverVillage().getResourcesManagement().withdrawResource(amountWithTax, type);
            }
        }finally {
            receiverVillage.getLock().writeLock().unlock();
        }

        //Duration transferTime = Duration.ofHours(offer.getTradeTime());

        TradeTask tradeTask = new TradeTask(Instant.now(), Duration.ofSeconds(4), offer);
        offer.setTradeStatus(TradeStatus.ACCEPTED);


        senderVillage.getLock().writeLock().lock();
        try {
            offer.getSenderVillage().getTimedOperation().put(tradeTask.getId(), tradeTask);
        }finally {
            senderVillage.getLock().writeLock().unlock();
        }*/

        /*for(Map.Entry<ResourcesType,Integer> entry : offer.getRequestedResources().entrySet()){
            ResourcesType type = entry.getKey();
            Integer originalAmount = entry.getValue();
            int amountWithTax = originalAmount + (int) (originalAmount * receiverTaxRate);
            if(offer.getReceiver().getResources().getAmount(type) < amountWithTax){return;}
            int currentAmount = offer.getSender().getResources().getAmount(type);
            int maxCapacity = offer.getSender().getResourcesManagement().getMaxCapacity(type);
            if (currentAmount + originalAmount > maxCapacity) {
                return;
            }
        }*/

        /*for(Map.Entry<ResourcesType,Integer> entry : offer.getOfferedResources().entrySet()){

            ResourcesType type = entry.getKey();
            Integer originalAmount = entry.getValue();

            int amountWithTax = originalAmount + (int) (originalAmount * senderTaxRate);

            if(offer.getReceiver().getResourcesManagement().getMaxCapacity(type) < originalAmount){return;}
            int currentAmount = offer.getReceiver().getResources().getAmount(type);
            int maxCapacity = offer.getReceiver().getResourcesManagement().getMaxCapacity(type);
            if (currentAmount + originalAmount > maxCapacity) {
                return;
            }
        }*/


    public void rejectOffer(TradeOffer offer) {
        offer.setTradeStatus(TradeStatus.REJECTED);

        offer.getSenderVillage().getLock().writeLock().lock();
        try {
            for(Map.Entry<ResourcesType, Integer> entry : offer.getOfferedResources().entrySet()){
                ResourcesType type = entry.getKey();
                int originalAmount = entry.getValue();

                offer.getSenderVillage().getResourcesManagement().addResource(originalAmount, type);
                offer.getReceiverVillage().getReceivedTradeRequests().remove(offer);
                offer.getSenderVillage().getSentTradeRequests().remove(offer);
            }
        }finally {
            offer.getSenderVillage().getLock().writeLock().unlock();
        }
    }

}
