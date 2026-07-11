package model.battle;

import model.village.Village;
import service.battle.BattleTravelTime;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

public class Battle implements Serializable {

    private final UUID battleId;
    private final Village attackerVillage;
    private final Village defenderVillage;
    private final BattleArmy attackerArmy;
    //private final Duration travelTime;
    private final long travelTime;
    private BattleArmy defenderArmy;
    private BattleStatus status;
    private BattleWinner winner;
    private Instant finishedTime;

    private String attackerUsername;
    private String defenderUsername;

    public Battle(Village attackerVillage, Village defenderVillage, BattleArmy attackerArmy) {

        this.battleId = UUID.randomUUID();
        this.attackerVillage = attackerVillage;
        this.defenderVillage = defenderVillage;
        this.attackerArmy = attackerArmy;
        this.status = BattleStatus.GOING;
        this.travelTime = BattleTravelTime.calculateTravelTime(attackerVillage, defenderVillage);

        this.attackerUsername = attackerVillage.getUserName();
        this.defenderUsername = defenderVillage.getUserName();
    }


    public UUID getBattleId() {
        return battleId;
    }

    public Village getAttackerVillage() {
        return attackerVillage;
    }

    public Village getDefenderVillage() {
        return defenderVillage;
    }

    public BattleArmy getAttackerArmy() {
        return attackerArmy;
    }

    public BattleArmy getDefenderArmy() {
        return defenderArmy;
    }

    public BattleStatus getStatus() {
        return status;
    }

    public BattleWinner getWinner() {
        return winner;
    }

    public Instant getFinishedTime() {
        return finishedTime;
    }

    public long getTravelTime() {
        return travelTime;
    }


    public void setDefenderArmy(BattleArmy defenderArmy) {
        this.defenderArmy = defenderArmy;
    }

    public void setStatus(BattleStatus status) {
        this.status = status;
    }

    public void setWinner(BattleWinner winner) {
        this.winner = winner;
    }

    public void setFinishedTime(Instant finishedTime) {
        this.finishedTime = finishedTime;
    }

    public String getAttackerUsername() {
        return attackerUsername;
    }

    public void setAttackerUsername(String attackerUsername) {
        this.attackerUsername = attackerUsername;
    }

    public String getDefenderUsername() {
        return defenderUsername;
    }

    public void setDefenderUsername(String defenderUsername) {
        this.defenderUsername = defenderUsername;
    }
}