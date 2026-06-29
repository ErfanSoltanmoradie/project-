package model.time;

import model.building.Building;
import model.building.Customhouse;
import model.resources.ResourcesType;
import model.trade.TradeOffer;
import model.trade.TradeStatus;
import model.village.Village;

import java.time.Instant;
import java.util.List;
import java.util.Map;

public class TradeTask extends TimedOperation{
    private final TradeOffer offer;
    public TradeTask(Instant start, Instant end, TradeOffer offer) {
        super(start,end,TimedOperationType.TRADE_TASK);
        this.offer=offer;

    }
    @Override
    public void execute(Village village, List<TimedOperation> toAdd) {
        Customhouse senderCustomhouse=null;
        for(Building b : this.offer.getSender().getBuildings().values()){
            if(b instanceof Customhouse c){senderCustomhouse=c; break;}
        }Customhouse receiverCustomhouse=null;
        for(Building b : this.offer.getReceiver().getBuildings().values()){
            if(b instanceof Customhouse c){receiverCustomhouse=c; break;}
        }
        for(Map.Entry<ResourcesType,Integer> entry : this.offer.getRequestedResources().entrySet()){
            ResourcesType resource = entry.getKey();
            int amount = entry.getValue();
            if(offer.getSender().getResourcesManagement().getMaxCapacity(resource) < amount){return;}
            this.offer.getSender().getResourcesManagement().addResource(amount, resource);
        }

        for(Map.Entry<ResourcesType,Integer> entry : this.offer.getOfferedResources().entrySet()){
            ResourcesType resource = entry.getKey();
            int amount = entry.getValue();
            if(offer.getReceiver().getResourcesManagement().getMaxCapacity(resource) < amount){return;}
            this.offer.getReceiver().getResourcesManagement().addResource(amount, resource);
        }
        this.offer.setTradeStatus(TradeStatus.ACCEPTED);
    }
}
