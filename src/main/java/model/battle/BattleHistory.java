package model.battle;

import model.army.ArmyType;
import model.resources.ResourcesType;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BattleHistory implements Serializable {

    private final UUID battleId;
    private final UUID attackerVillageId;
    private final UUID defenderVillageId;
    private final BattleWinner winner;
    private final Instant finishedTime;
    private final Map<ArmyType, Integer> attackerLosses;
    private final Map<ArmyType, Integer> defenderLosses;
    private final Map<ResourcesType, Integer> attackerLoot;

    public BattleHistory(
            Battle battle,
            Map<ArmyType, Integer> attackerLosses,
            Map<ArmyType, Integer> defenderLosses,
            Map<ResourcesType, Integer> attackerLoot) {

        this.battleId = battle.getBattleId();
        this.attackerVillageId = battle.getAttackerVillage().getVillageId();
        this.defenderVillageId = battle.getDefenderVillage().getVillageId();
        this.winner = battle.getWinner();
        this.finishedTime = battle.getFinishedTime();
        this.attackerLosses = new HashMap<>(attackerLosses);
        this.defenderLosses = new HashMap<>(defenderLosses);
        this.attackerLoot = new HashMap<>(attackerLoot);
    }

    public UUID getBattleId() {
        return battleId;
    }

    public UUID getAttackerVillageId() {
        return attackerVillageId;
    }

    public UUID getDefenderVillageId() {
        return defenderVillageId;
    }

    public BattleWinner getWinner() {
        return winner;
    }

    public Instant getFinishedTime() {
        return finishedTime;
    }

    public Map<ArmyType, Integer> getAttackerLosses() {
        return attackerLosses;
    }

    public Map<ArmyType, Integer> getDefenderLosses() {
        return defenderLosses;
    }

    public Map<ResourcesType, Integer> getAttackerLoot() {
        return attackerLoot;
    }
}