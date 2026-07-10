package service.trade;

import model.building.Building;
import model.building.BuildingType;
import model.building.Customhouse;
import model.player.Player;
import model.resources.ResourcesType;
import model.time.TaskProcessor;
import model.village.Village;
import model.world.Coordinate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Test {

    public static void main(String[] args) throws InterruptedException {

        /*System.out.println("START");

        Village villageSender = new Village(new Coordinate(0, 0), 100);
        Village villageReceiver = new Village(new Coordinate(30, 40), 100);

        Player senderPlayer = new Player("Player_Sender", villageSender);
        Player receiverPlayer = new Player("Player_Receiver", villageReceiver);

        Customhouse customsSender = new Customhouse(BuildingType.CUSTOMHOUSE, new Coordinate(1, 5));
        Customhouse customsReceiver = new Customhouse(BuildingType.CUSTOMHOUSE, new Coordinate(7, 10));
        villageSender.getBuildings().put(customsSender.getId(), customsSender);
        villageReceiver.getBuildings().put(customsReceiver.getId(), customsReceiver);

        villageSender.getResourcesManagement().addResource(1000, ResourcesType.WOOD);
        villageReceiver.getResourcesManagement().addResource(1000, ResourcesType.IRON);

        System.out.println("Wood= " + villageSender.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("Iron= " + villageReceiver.getResources().getAmount(ResourcesType.IRON));

        Map<ResourcesType, Integer> offered = new HashMap<>();
        offered.put(ResourcesType.WOOD, 400);

        Map<ResourcesType, Integer> requested = new HashMap<>();
        requested.put(ResourcesType.IRON, 200);

        TradeService tradeService = new TradeService(UUID.randomUUID(), villageSender.getResourcesManagement());
        TaskProcessor processorSender = new TaskProcessor(villageSender);
        TaskProcessor processorReceiver = new TaskProcessor(villageReceiver);

        System.out.println("SEND REQUST . . .");
        tradeService.sendRequest(senderPlayer, receiverPlayer, offered, requested);

        TradeOffer offer = new TradeOffer(villageSender, villageReceiver, senderPlayer, receiverPlayer, offered, requested);

        System.out.println("offer status: " + offer.getTradeStatus());
        System.out.println("transfer time: " + offer.getTradeTime());
        System.out.println("sender wood  " + villageSender.getResources().getAmount(ResourcesType.WOOD));


        System.out.println("reject offer ");
        tradeService.rejectOffer(offer);
        System.out.println("offer status " + offer.getTradeStatus());
        System.out.println("sender wood after reject: " + villageSender.getResources().getAmount(ResourcesType.WOOD));

        offer.setTradeStatus(TradeStatus.PENDING);
        villageSender.getResourcesManagement().withdrawResource(400, ResourcesType.WOOD);

        System.out.println("accept . . . ");

        tradeService.acceptOffer(offer);

        System.out.println("reciever iron: " + villageReceiver.getResources().getAmount(ResourcesType.IRON));
        System.out.println("" + villageSender.getTimedOperation().size());



        Thread.sleep(10500);
        processorSender.process();
        processorReceiver.process();


        System.out.println("end ");
        System.out.println("offer statuc after task: " + offer.getTradeStatus());

        System.out.println("final sender iron: " + villageSender.getResources().getAmount(ResourcesType.IRON));
        System.out.println("final sender wood  " + villageSender.getResources().getAmount(ResourcesType.WOOD));


        System.out.println("final reciver wooc  " + villageReceiver.getResources().getAmount(ResourcesType.WOOD));
        System.out.println("fianl reciver iron " + villageReceiver.getResources().getAmount(ResourcesType.IRON));*/

    }
}