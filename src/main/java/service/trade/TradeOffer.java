package service.trade;

import model.player.Player;
import model.resources.ResourcesType;
import model.village.Village;
import model.world.Coordinate;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

public class TradeOffer implements Serializable {
    private final UUID uuid;
    private Map<ResourcesType, Integer> netOfferedResources;   // چیزی که واقعاً به گیرنده می‌رسد
    private Map<ResourcesType, Integer> netRequestedResources;  // چیزی که واقعاً به فرستنده می‌رسد
    private final Player senderPlayer;
    private final Player receiverPlayer;
    private final  Map<ResourcesType , Integer> offeredResources;
    private final  Map<ResourcesType , Integer> requestedResources;
    private TradeStatus tradeStatus;
    private int tradeTime ;
    private final Coordinate coordinateOfSender;
    private final Coordinate coordinateOfReceiver;

    public TradeOffer(/*Village sender, Village receiver,*/ Player alliancesender, Player alliancesreceiver,
                      Map<ResourcesType , Integer> offeredResources, Map<ResourcesType , Integer> requestedResources) {
        senderPlayer = alliancesender;
        receiverPlayer = alliancesreceiver;
        this.uuid = UUID.randomUUID();
        //this.sender = sender;
        //this.receiver = receiver;
        this.offeredResources = offeredResources;
        this.requestedResources = requestedResources;
        this.coordinateOfSender =alliancesender.getVillage().getCoordinate(); //sender.getCoordinate();
        this.coordinateOfReceiver =alliancesreceiver.getVillage().getCoordinate(); //receiver.getCoordinate();
        this.tradeStatus = TradeStatus.PENDING;
        double distance = Math.sqrt(Math.pow(coordinateOfSender.getX() - coordinateOfReceiver.getX(), 2) + Math.pow(coordinateOfSender.getY() - coordinateOfReceiver.getY(), 2));
        this.tradeTime = (int) Math.ceil(distance / 10);
    }

    public void setNetOfferedResources(Map<ResourcesType, Integer> netOfferedResources) {
        this.netOfferedResources = netOfferedResources;
    }
    public Map<ResourcesType, Integer> getNetOfferedResources() {
        return netOfferedResources;
    }
    public void setNetRequestedResources(Map<ResourcesType, Integer> netRequestedResources) {
        this.netRequestedResources = netRequestedResources;
    }
    public Map<ResourcesType, Integer> getNetRequestedResources() {
        return netRequestedResources;
    }

    public UUID getUuid() {
        return uuid;
    }

    /*public Village getSenderVillage() {
        return sender;
    }

    public Village getReceiverVillage() {
        return receiver;
    }*/

    public Map<ResourcesType, Integer> getOfferedResources() {
        return offeredResources;
    }

    public Map<ResourcesType, Integer> getRequestedResources() {
        return requestedResources;
    }

    public TradeStatus getTradeStatus() {
        return tradeStatus;
    }
    public void setTradeStatus(TradeStatus tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public int getTradeTime() {
        return tradeTime;
    }

    public Coordinate getCoordinateOfSender() {
        return coordinateOfSender;
    }

    public Coordinate getCoordinateOfReceiver() {
        return coordinateOfReceiver;
    }

    public Player getAlliancesender() {
        return senderPlayer;
    }

    public Player getAlliancesreceiver() {
        return receiverPlayer;
    }

}