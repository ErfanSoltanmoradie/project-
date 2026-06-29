package service.trade;

import model.building.Building;
import model.building.Customhouse;
import model.player.Player;
import model.resources.ResourcesType;
import model.time.TradeTask;
import model.trade.TradeOffer;
import model.trade.TradeStatus;
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
    public TradeService(UUID id , ResourcesManagement resourcesManagement) {
        this.id = id;
        this.resourcesManagement = resourcesManagement;
    }

    public void sendRequest(Player sender, Player receiver, Map<ResourcesType,Integer> offered , Map<ResourcesType,Integer> requested) {
        Village senderVillage = sender.getVillage();
        Village receiverVillage = receiver.getVillage();
        boolean senderHasCustoms= false;
        for(Building building : senderVillage.getBuildings().values()){
            if(building instanceof Customhouse){
                senderHasCustoms=true;
                break;
            }
        }if (!senderHasCustoms){return;}
        boolean receiverHasCustoms= false;
        for(Building building : receiverVillage.getBuildings().values()){
            if(building instanceof Customhouse){
                receiverHasCustoms=true;
                break;
            }
        }if (!receiverHasCustoms){return;}

        for(Map.Entry<ResourcesType,Integer> entry : offered.entrySet()){
            ResourcesType type = entry.getKey();
            Integer amount = entry.getValue();
            if(senderVillage.getResources().getAmount(type) < amount){return;}
        }
        for(Map.Entry<ResourcesType,Integer> entry : requested.entrySet()){
            ResourcesType type = entry.getKey();
            Integer amount = entry.getValue();
            if(receiverVillage.getResourcesManagement().getMaxCapacity(type) < amount){return;}
        }

        TradeOffer tradeOffer = new TradeOffer(senderVillage,receiverVillage,sender,receiver,offered,requested);// for making & showing an offer in receiverVillage
        senderVillage.getTradeOffers().put(tradeOffer.getUuid(), tradeOffer);
        receiverVillage.getTradeOffers().put(tradeOffer.getUuid(), tradeOffer);
    }

    public void acceptOffer(TradeOffer offer) {
        if (offer == null || offer.getTradeStatus() != TradeStatus.PENDING) { return; }
        offer.setTradeStatus(TradeStatus.ACCEPTED);

        Customhouse senderCustomhouse = null;
        Customhouse receiverCustomhouse = null;
        for(Building building1 : offer.getSender().getBuildings().values()){
            if(building1 instanceof Customhouse customhouse){senderCustomhouse = customhouse ;break;}
        }
        for(Building building2 : offer.getReceiver().getBuildings().values()){
            if(building2 instanceof Customhouse customhouse){receiverCustomhouse = customhouse ;break;}
        }
        boolean areAllied = offer.getAlliancesender().getAlliance() != null &&
                (offer.getAlliancesender().getAlliance().getSender().equals(offer.getAlliancesreceiver()) ||
                        offer.getAlliancesender().getAlliance().getReceiver().equals(offer.getAlliancesreceiver()));
        double senderTaxRate = areAllied ? 0 : (senderCustomhouse != null ? senderCustomhouse.getCommission() : 0);
        double receiverTaxRate = areAllied ? 0 : (receiverCustomhouse != null ? receiverCustomhouse.getCommission() : 0);

        for(Map.Entry<ResourcesType,Integer> entry : offer.getOfferedResources().entrySet()){
            ResourcesType type = entry.getKey();
            Integer originalAmount = entry.getValue();
            int amountWithTax = originalAmount + (int) (originalAmount * senderTaxRate);
            if(offer.getReceiver().getResourcesManagement().getMaxCapacity(type) < originalAmount){return;}
            int currentAmount = offer.getReceiver().getResources().getAmount(type);
            int maxCapacity = offer.getReceiver().getResourcesManagement().getMaxCapacity(type);
            if (currentAmount + originalAmount > maxCapacity) {
                return;
            }
        }
        for(Map.Entry<ResourcesType, Integer> entry : offer.getOfferedResources().entrySet()){
            ResourcesType type = entry.getKey();
            int originalAmount = entry.getValue();
            int amountWithTax = originalAmount + (int)(originalAmount * senderTaxRate);
            offer.getSender().getResourcesManagement().withdrawResource(amountWithTax, type);
        }
        for(Map.Entry<ResourcesType,Integer> entry : offer.getRequestedResources().entrySet()){
            ResourcesType type = entry.getKey();
            Integer originalAmount = entry.getValue();
            int amountWithTax = originalAmount + (int) (originalAmount * receiverTaxRate);
            if(offer.getReceiver().getResources().getAmount(type) < amountWithTax){return;}
            int currentAmount = offer.getSender().getResources().getAmount(type);
            int maxCapacity = offer.getSender().getResourcesManagement().getMaxCapacity(type);
            if (currentAmount + originalAmount > maxCapacity) {
                return;
            }
        }

        for(Map.Entry<ResourcesType,Integer> entry : offer.getRequestedResources().entrySet()) {
            ResourcesType type = entry.getKey();
            int originalAmount = entry.getValue();
            int amountWithTax = originalAmount + (int)(originalAmount * receiverTaxRate);
            offer.getReceiver().getResourcesManagement().withdrawResource(amountWithTax, type);
        }

        Duration transferTime = Duration.ofHours(offer.getTradeTime());
        TradeTask tradeTask = new TradeTask(Instant.now(), Instant.now().plus(transferTime), offer);

        offer.getSender().getTimedOperation().put(tradeTask.getId(), tradeTask);
    }

    public void rejectOffer(TradeOffer offer) {
        offer.setTradeStatus(TradeStatus.REJECTED);
    }


}

