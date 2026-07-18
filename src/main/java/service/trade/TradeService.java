package service.trade;

import model.building.Building;
import model.building.Customhouse;
import model.player.Player;
import model.resources.ResourcesType;

import model.village.Village;
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
    public TradeService(UUID id ) {
        this.id = id;
    }

    public void sendRequest(Player sender, Player receiver, Map<ResourcesType,Integer> offered , Map<ResourcesType,Integer> requested) {

        Village senderVillage = sender.getVillage();
        Village receiverVillage = receiver.getVillage();

        senderVillage.getLock().writeLock().lock();
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

        TradeOffer tradeOffer = new TradeOffer(sender,receiver,offered,requested);// for making & showing an offer in receiverVillage

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

                tradeOffer.getSenderPlayer().getVillage().getResourcesManagement().withdrawResource(originalAmount, type);
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
        }
    }



    public void acceptOffer(TradeOffer offer) {
        if (offer == null || offer.getTradeStatus() != TradeStatus.PENDING) { return; }

        offer.setTradeStatus(TradeStatus.ACCEPTED);

        Village senderVillage = offer.getSenderPlayer().getVillage();
        Village receiverVillage = offer.getReceiverPlayer().getVillage();

        Customhouse senderCustomhouse = null;
        Customhouse receiverCustomhouse = null;

        senderVillage.getLock().writeLock().lock();
        try {
            for(Building building1 : offer.getSenderPlayer().getVillage().getBuildings().values()){
                if(building1 instanceof Customhouse customhouse){senderCustomhouse = customhouse ;break;}
            }
        }finally {
            senderVillage.getLock().writeLock().unlock();
        }

        receiverVillage.getLock().writeLock().lock();
        try {

            for(Building building2 : offer.getReceiverPlayer().getVillage().getBuildings().values()){
                if(building2 instanceof Customhouse customhouse){receiverCustomhouse = customhouse ;break;}
            }
        }finally {
            receiverVillage.getLock().writeLock().unlock();
        }

        boolean areAllied = offer.getSenderPlayer().getAlliance() != null &&
                (offer.getSenderPlayer().getAlliance().getSender().equals(offer.getReceiverPlayer()) ||
                        offer.getSenderPlayer().getAlliance().getReceiver().equals(offer.getReceiverPlayer()));

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
                offer.getSenderPlayer().getVillage().getResourcesManagement().withdrawResource(amountWithTax, type);
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
                offer.getReceiverPlayer().getVillage().getResourcesManagement().withdrawResource(amountWithTax, type);
            }
        }finally {
            receiverVillage.getLock().writeLock().unlock();
        }

        //Duration transferTime = Duration.ofHours(offer.getTradeTime());
        TradeTask tradeTask = new TradeTask(Instant.now(), Duration.ofSeconds(4), offer);

        senderVillage.getLock().writeLock().lock();
        try {
            offer.getSenderPlayer().getVillage().getTimedOperation().put(tradeTask.getId(), tradeTask);
        }finally {
            senderVillage.getLock().writeLock().unlock();
        }

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
    }

    public void rejectOffer(TradeOffer offer) {
        offer.setTradeStatus(TradeStatus.REJECTED);

        offer.getSenderPlayer().getVillage().getLock().writeLock().lock();
        try {
            for(Map.Entry<ResourcesType, Integer> entry : offer.getOfferedResources().entrySet()){
                ResourcesType type = entry.getKey();
                int originalAmount = entry.getValue();

                offer.getSenderPlayer().getVillage().getResourcesManagement().addResource(originalAmount, type);
            }
        }finally {
            offer.getSenderPlayer().getVillage().getLock().writeLock().unlock();
        }
    }


}