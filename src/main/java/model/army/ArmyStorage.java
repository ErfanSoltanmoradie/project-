package model.army;

public class ArmyStorage {
    private int ragnarCount;
    private int russoCount;
    private int lagertaCount;

    public ArmyStorage() {
        this.ragnarCount = 0;
        this.russoCount = 0;
        this.lagertaCount = 0;
    }

    public int getRagnarCount() {
        return this.ragnarCount;
    }

    public int getRussoCount() {
        return this.russoCount;
    }

    public int getLagertaCount() {return this.lagertaCount;}

    public int getTotalArmyCount() {
        return this.ragnarCount + this.russoCount + this.lagertaCount;
    }

    public void increaseArmy(ArmyType type, int count){

        if(count < 0){return;}

        switch (type){
            case RAGNAR  -> this.ragnarCount += count;
            case ROSOO   -> this.russoCount += count;
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
                if(this.russoCount < count)
                    return false;
                this.russoCount -= count;
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
