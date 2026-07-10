package model.army;

import java.io.Serializable;

public class ArmyStorage implements Serializable {
    private int ragnarCount;
    private int rosooCount;
    private int lagertaCount;

    public ArmyStorage() {
        this.ragnarCount = 0;
        this.rosooCount = 0;
        this.lagertaCount = 0;
    }

    public int getRagnarCount() {
        return this.ragnarCount;
    }

    public int getRosooCount() {
        return this.rosooCount;
    }

    public int getLagertaCount() {return this.lagertaCount;}

    public int getTotalArmyCount() {
        return this.ragnarCount + this.rosooCount + this.lagertaCount;
    }

    public int getArmyCount(ArmyType type) {
        return switch (type) {
            case RAGNAR  -> ragnarCount;
            case ROSOO   -> rosooCount;
            case LAGERTA -> lagertaCount;
        };
    }

    public void increaseArmy(ArmyType type, int count){

        if(count < 0) {return;}

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
}
