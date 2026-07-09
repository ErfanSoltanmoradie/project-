package model.battle;

import model.army.ArmyType;

import java.io.Serializable;

public class BattleArmy implements Serializable {

    private int ragnarCount;
    private int rosooCount;
    private int lagertaCount;

    public BattleArmy() {
    }

    public BattleArmy(int ragnarCount, int rosooCount, int lagertaCount) {
        this.ragnarCount = ragnarCount;
        this.rosooCount = rosooCount;
        this.lagertaCount = lagertaCount;
    }

    public int getRagnarCount() {
        return ragnarCount;
    }

    public int getRosooCount() {
        return rosooCount;
    }

    public int getLagertaCount() {
        return lagertaCount;
    }

    public int getTotalArmyCount() {
        return ragnarCount + rosooCount + lagertaCount;
    }

    public void increaseArmy(ArmyType type, int count){

        if(count < 0){return;}

        switch (type){
            case RAGNAR  -> this.ragnarCount  += count;
            case ROSOO   -> this.rosooCount   += count;
            case LAGERTA -> this.lagertaCount += count;
        }
    }

    public boolean decreaseArmy(ArmyType type, int count){
        switch (type){
            case RAGNAR-> {
                if(this.ragnarCount < count)
                    return false;
                this.ragnarCount -= count;
            }
            case ROSOO -> {
                if(this.rosooCount < count)
                    return false;
                this.rosooCount -= count;
            }
            case LAGERTA -> {
                if(this.lagertaCount < count)
                    return false;
                this.lagertaCount -= count;
            }
        }
        return true;
    }

    public int getArmyCount(ArmyType type) {
        return switch (type) {
            case RAGNAR  -> ragnarCount;
            case ROSOO   -> rosooCount;
            case LAGERTA -> lagertaCount;
        };
    }
}
