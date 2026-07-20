package model.resources;

import java.io.Serializable;

public class Resources implements Serializable {

    protected ResourcesType type;
    private int wood;
    private int iron;
    private int gunPowder;
    private int stone;
    private int dirtyWater;
    private int dirtySoil;
    private int cleanWater;
    private int cleanSoil;
    private int coin;

    public Resources() {
        this.wood = 80000;
        this.iron = 80000;
        this.gunPowder = 80000;
        this.stone = 80000;
        this.dirtyWater = 0;
        this.dirtySoil = 0;
        this.cleanWater = 1000;
        this.cleanSoil = 1000;
        this.coin = 2000;
    }


    public void addResource(ResourcesType type, int amount, int capacity) {
        setAmount(type,  /*amount + getAmount(type)*/Math.min(getAmount(type) + amount, capacity));
    }

    public boolean withdraw(ResourcesType type, int amount) {
        if (getAmount(type) < amount) return false;
        setAmount(type, getAmount(type) - amount);
        return true;
    }

    public int getAmount(ResourcesType type) {
        return switch (type) {
            case WOOD -> this.wood;
            case IRON -> this.iron;
            case STONE -> this.stone;
            case GUN_POWDER -> this.gunPowder;
            case DIRTY_WATER -> this.dirtyWater;
            case CLEAN_WATER -> this.cleanWater;
            case DIRTY_SOIL -> this.dirtySoil;
            case CLEAN_SOIL -> this.cleanSoil;
            case COIN -> this.coin;
        };
    }

    private void setAmount(ResourcesType type, int value) {
        if (value < 0) return;

        switch (type) {
            case WOOD -> this.wood = value;
            case IRON -> this.iron = value;
            case STONE -> this.stone = value;
            case GUN_POWDER -> this.gunPowder = value;
            case DIRTY_WATER -> this.dirtyWater = value;
            case CLEAN_WATER -> this.cleanWater = value;
            case DIRTY_SOIL -> this.dirtySoil = value;
            case CLEAN_SOIL -> this.cleanSoil = value;
            case COIN -> this.coin = value;
        }
    }

    public void setType(ResourcesType type) {
        this.type = type;
    }
}
