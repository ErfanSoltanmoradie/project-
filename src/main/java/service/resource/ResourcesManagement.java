package service.resource;

import model.army.ArmyCost;
import model.building.*;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.village.Village;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ResourcesManagement{

    private  Village village;

    public ResourcesManagement(Village village) {
        this.village = village;
    }

    public boolean checkResourcesCost(Cost cost) {

        Resources resources = village.getResources();

        return resources.getAmount(ResourcesType.WOOD) >= cost.getWood()
                && resources.getAmount(ResourcesType.IRON) >= cost.getIron()
                && resources.getAmount(ResourcesType.STONE) >= cost.getStone()
                && resources.getAmount(ResourcesType.COIN) >= cost.getCoin()
                && resources.getAmount(ResourcesType.CLEAN_SOIL) >= cost.getCleanSoil()
                && resources.getAmount(ResourcesType.CLEAN_WATER) >= cost.getCleanWater()
                && resources.getAmount(ResourcesType.GUN_POWDER) >= cost.getGunPowder();
    }

    public void withdrawResourcesCost(Cost cost) {
        if (!checkResourcesCost(cost)) return;

        Resources resources = village.getResources();

        resources.withdraw(ResourcesType.WOOD, cost.getWood());
        resources.withdraw(ResourcesType.IRON, cost.getIron());
        resources.withdraw(ResourcesType.STONE, cost.getStone());
        resources.withdraw(ResourcesType.COIN, cost.getCoin());
        resources.withdraw(ResourcesType.CLEAN_SOIL, cost.getCleanSoil());
        resources.withdraw(ResourcesType.CLEAN_WATER, cost.getCleanWater());
        resources.withdraw(ResourcesType.GUN_POWDER, cost.getGunPowder());
    }

    //For Army
    public boolean checkResourcesArmyCost(ArmyCost cost, int count) {

        Resources resources = village.getResources();

        return resources.getAmount(ResourcesType.WOOD) >= cost.wood() * count
                && resources.getAmount(ResourcesType.STONE) >= cost.stone() * count
                && resources.getAmount(ResourcesType.IRON) >= cost.iron() * count
                && resources.getAmount(ResourcesType.GUN_POWDER) >= cost.gunPowder() * count;
    }

    public void withdrawResourcesArmyCost(ArmyCost cost, int count) {

        if (!checkResourcesArmyCost(cost, count))
            return;

        Resources resources = village.getResources();

        resources.withdraw(ResourcesType.WOOD, cost.wood() * count);
        resources.withdraw(ResourcesType.STONE, cost.stone() * count);
        resources.withdraw(ResourcesType.IRON, cost.iron() * count);
        resources.withdraw(ResourcesType.GUN_POWDER, cost.gunPowder() * count);
    }

    public void addResource(int amount, ResourcesType type) {
        int capacity = this.getMaxCapacity(type);
        village.getResources().addResource(type, amount, capacity);
    }

    public int getMaxCapacity(ResourcesType type){

        return switch (type){

            case WOOD -> calAllCapacity(BuildingType.WOOD_STORAGE);

            case IRON -> calAllCapacity(BuildingType.IRON_STORAGE);

            case STONE -> calAllCapacity(BuildingType.STONE_STORAGE);

            case GUN_POWDER -> calAllCapacity(BuildingType.GUNPOWDER_STORAGE);

            case CLEAN_WATER, DIRTY_WATER -> calAllCapacity(BuildingType.WATER_STORAGE);

            case CLEAN_SOIL, DIRTY_SOIL -> calAllCapacity(BuildingType.SOIL_STORAGE);

            case COIN -> Integer.MAX_VALUE;
        };
    }

    public int calAllCapacity(BuildingType buildingType){

        int capacity = 0;
        for (Building building : this.village.getBuildings().values()){
            if(building.getType() == buildingType  &&  building instanceof StorageBuilding){
                capacity += ((StorageBuilding) building).getCapacity();
            }
        }
        return capacity;
    }

    public void withdrawResource(int amount, ResourcesType type) {
        village.getResources().withdraw(type, amount);
    }
}