package model.army;

import model.building.ArmyProducer;

import java.time.Duration;

public enum ArmyType {

    RAGNAR(15, 10, 1,
            new ArmyCost(100,50,25,20, Duration.ofSeconds(5))),

    ROSOO(25, 15, 2,
            new ArmyCost(150, 75, 50, 25,Duration.ofSeconds(10))),

    LAGERTA(40, 35, 3,
            new ArmyCost(200, 100, 75,40,Duration.ofSeconds(10)));

    private final int attack;
    private final int defense;
    private final int requiredLevel;
    private final ArmyCost trainCost;

    ArmyType(int attack, int defense, int requiredBarrackLevel,ArmyCost trainCost) {

        this.attack = attack;
        this.defense = defense;
        this.requiredLevel = requiredBarrackLevel;
        this.trainCost = trainCost;
    }
    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public ArmyCost getTrainCost() {
        return trainCost;
    }

    public boolean isUnlocked(int barrackLevel) {
        return barrackLevel >= requiredLevel;
    }

    public int getFinalAttack(ArmyProducer producer) {
        return (int) Math.round(getAttack() * producer.getPowerMultiplier());
    }

    public int getFinalDefense(ArmyProducer producer) {
        return (int) Math.round(getDefense() * producer.getPowerMultiplier());
    }
}
