package model.time;

import model.army.ArmyType;

import model.battle.BattleStatus;
import model.battle.BattleWinner;

import model.building.Building;
import model.building.Plant;
import model.event.EventType;
import model.resources.ResourcesType;
import service.alliance.AllianceRequest;
import service.trade.TradeOffer;
import service.trade.TradeTask;

import java.time.Instant;
import java.util.*;

public class TaskResult {

    private final Map<UUID, Building> buildingsToAdd = new HashMap<>();
    private final Map<UUID, Plant> plantsToAdd = new HashMap<>();
    private final List<UUID> buildingsToUpgrade = new ArrayList<>();
    //private final List<UUID> buildingsToRemove = new ArrayList<>();
    private final Map<UUID, TimedOperation> tasksToAdd = new HashMap<>();
    private final Map<ResourcesType, Integer> resourcesToAdd = new HashMap<>();
    private final Map<ResourcesType, Integer> resourcesToWithdraw = new HashMap<>();
    private final List<UUID> tasksToRemove = new ArrayList<>();
    private final List<UUID> productionBuildingsToReschedule = new ArrayList<>();
    private final List<EventType> eventType = new ArrayList<>();
    private final Map<ArmyType, Integer> trainedArmiesToAdd = new HashMap<>();

    private final Map<UUID , BattleStatus> battleStatusChange = new HashMap<>();
    private final Map<ArmyType, Integer> attackerArmyLosses= new HashMap<>();
    private final Map<ArmyType, Integer> attackerReturningArmies= new HashMap<>();
    private final Map<ArmyType, Integer> defenderArmyLosses= new HashMap<>();
    private final Map<ArmyType, Integer> defenderReturningArmies= new HashMap<>();
    private final Map<ResourcesType, Integer> attackerLoot= new HashMap<>();
    private final Map<ResourcesType, Integer> defenderResourceLosses= new HashMap<>();
    private final Map<UUID, Integer> villageHealthChange = new HashMap<>();
    private final Map<UUID, BattleWinner>  battleWinners = new HashMap<>();

    public Map<UUID, BattleStatus> getBattleStatusChange() {
        return battleStatusChange;
    }

    public Map<ArmyType, Integer> getAttackerArmyLosses() {
        return attackerArmyLosses;
    }

    public Map<ArmyType, Integer> getAttackerReturningArmies() {
        return attackerReturningArmies;
    }

    public Map<ArmyType, Integer> getDefenderArmyLosses() {
        return defenderArmyLosses;
    }

    public Map<ArmyType, Integer> getDefenderReturningArmies() {
        return defenderReturningArmies;
    }

    public Map<ResourcesType, Integer> getAttackerLoot() {
        return attackerLoot;
    }

    public Map<ResourcesType, Integer> getDefenderResourceLoss() {
        return defenderResourceLosses;
    }

    public Map<UUID , Integer> getVillageHealthChange() {
        return villageHealthChange;
    }

    public Map<UUID, BattleWinner> getBattleWinners() {
        return battleWinners;
    }

    //-----------------------------------------------------------------------------------

    public Map<ArmyType, Integer> getTrainedArmiesToAdd() {
        return trainedArmiesToAdd;
    }

    private final List<TradeOffer> tradeOffers = new ArrayList<>();

    public List<TradeOffer> getTradeOffers() {
        return tradeOffers;
    }

    private final List<AllianceRequest> allianceRequestsToApply = new ArrayList<>();

    public List<AllianceRequest> getAllianceRequestsToApply() {
        return allianceRequestsToApply;
    }

    public Map<UUID, Plant> getPlantsToAdd() {
        return plantsToAdd;
    }


    public List<EventType> getEventType () {
        return eventType;
    }

    public List<UUID> getProductionBuildingsToReschedule () {
        return productionBuildingsToReschedule;
    }

    public Map<ResourcesType, Integer> getResourcesToWithdraw () {
        return resourcesToWithdraw;
    }

    public Map<ResourcesType, Integer> getResourcesToAdd () {
        return resourcesToAdd;
    }

    public List<UUID> getBuildingsToUpgrade () {
        return buildingsToUpgrade;
    }

    public Map<UUID, Building> getBuildingsToAdd () {
        return buildingsToAdd;
    }

    public Map<UUID, TimedOperation> getTasksToAdd () {
        return tasksToAdd;
    }

    public List<UUID> getTasksToRemove () {
        return tasksToRemove;
    }
}