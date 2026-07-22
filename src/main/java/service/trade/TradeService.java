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

                for (Map.Entry<ResourcesType, Integer> entry : offered.entrySet()) {
                    ResourcesType type = entry.getKey();
                    Integer amount = entry.getValue();
                    if (senderVillage.getResources().getAmount(type) < amount) {
                        return;
                    }
                }

                TradeOffer tradeOffer = new TradeOffer(/*senderVillage, receiverVillage,*/ sender, receiver, offered, requested);// for making & showing an offer in receiverVillage

                sender.getVillage().getSentTradeRequests().add(tradeOffer);
                receiver.getVillage().getReceivedTradeRequests().add(tradeOffer);

                for (Map.Entry<ResourcesType, Integer> entry : tradeOffer.getOfferedResources().entrySet()) {
                    ResourcesType type = entry.getKey();
                    int originalAmount = entry.getValue();
                    tradeOffer.getAlliancesender().getVillage().getResourcesManagement().withdrawResource(originalAmount, type);
                }

                tradeOffer.setTradeStatus(TradeStatus.PENDING);

            } finally {
                AllianceService.unlockVillages(sender, receiver);
            }
        } finally {
            AllianceService.unlockPlayers(sender, receiver);
        }
    }


    public void acceptOffer(TradeOffer offer) {
        if (offer == null || offer.getTradeStatus() != TradeStatus.PENDING) {
            return;
        }

        offer.setTradeStatus(TradeStatus.ACCEPTED);

        Customhouse senderCustomhouse = null;
        Customhouse receiverCustomhouse = null;

        AllianceService.lockVillages(offer.getAlliancesender(), offer.getAlliancesreceiver());
        try {
            for (Building building1 : offer.getAlliancesender().getVillage().getBuildings().values()) {
                if (building1 instanceof Customhouse customhouse) { senderCustomhouse = customhouse; break; }
            }
            for (Building building2 : offer.getAlliancesreceiver().getVillage().getBuildings().values()) {
                if (building2 instanceof Customhouse customhouse) { receiverCustomhouse = customhouse; break; }
            }

            // گیرنده باید به اندازه‌ی کافی از منابع درخواستی را داشته باشد
            for (Map.Entry<ResourcesType, Integer> entry : offer.getRequestedResources().entrySet()) {
                if (offer.getAlliancesreceiver().getVillage().getResources().getAmount(entry.getKey()) < entry.getValue()) {
                    return;
                }
            }

            boolean areAllied = offer.getAlliancesender().getAlliance() != null &&
                    (offer.getAlliancesender().getAlliance().getSender().equals(offer.getAlliancesreceiver()) ||
                            offer.getAlliancesender().getAlliance().getReceiver().equals(offer.getAlliancesreceiver()));

            double senderTaxRate = areAllied ? 0 : (senderCustomhouse != null ? senderCustomhouse.getCommission() : 0);
            double receiverTaxRate = areAllied ? 0 : (receiverCustomhouse != null ? receiverCustomhouse.getCommission() : 0);

            for (Map.Entry<ResourcesType, Integer> entry : offer.getRequestedResources().entrySet()) {
                offer.getAlliancesreceiver().getVillage().getResourcesManagement()
                        .withdrawResource(entry.getValue(), entry.getKey());
            }

            Map<ResourcesType, Integer> netOffered = new java.util.HashMap<>();
            for (Map.Entry<ResourcesType, Integer> entry : offer.getOfferedResources().entrySet()) {
                int net = entry.getValue() - (int) (entry.getValue() * senderTaxRate);
                netOffered.put(entry.getKey(), net);
            }
            offer.setNetOfferedResources(netOffered);

            Map<ResourcesType, Integer> netRequested = new java.util.HashMap<>();
            for (Map.Entry<ResourcesType, Integer> entry : offer.getRequestedResources().entrySet()) {
                int net = entry.getValue() - (int) (entry.getValue() * receiverTaxRate);
                netRequested.put(entry.getKey(), net);
            }
            offer.setNetRequestedResources(netRequested);

            TradeTask tradeTask = new TradeTask(Instant.now(), Duration.ofSeconds(offer.getTradeTime()), offer);

            offer.getAlliancesender().getVillage().getTimedOperation().put(tradeTask.getId(), tradeTask);
            offer.setTradeStatus(TradeStatus.ACCEPTED);

            offer.getAlliancesreceiver().getVillage().getReceivedTradeRequests().remove(offer);
            offer.getAlliancesender().getVillage().getSentTradeRequests().remove(offer);

        } finally {
            AllianceService.unlockVillages(offer.getAlliancesender(), offer.getAlliancesreceiver());
        }
    }


    public void rejectOffer(TradeOffer offer) {
        offer.setTradeStatus(TradeStatus.REJECTED);

        offer.getAlliancesender().getVillage().getLock().writeLock().lock();
        try {
            for(Map.Entry<ResourcesType, Integer> entry : offer.getOfferedResources().entrySet()){
                ResourcesType type = entry.getKey();
                int originalAmount = entry.getValue();

                offer.getAlliancesender().getVillage().getResourcesManagement().addResource(originalAmount, type);
                offer.getAlliancesender().getVillage().getReceivedTradeRequests().remove(offer);
                offer.getAlliancesender().getVillage().getSentTradeRequests().remove(offer);
            }
        }finally {
            offer.getAlliancesender().getVillage().getLock().writeLock().unlock();
        }
    }
}
