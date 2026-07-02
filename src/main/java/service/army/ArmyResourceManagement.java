package service.army;

import model.army.ArmyCost;
import model.resources.Resources;
import model.resources.ResourcesType;
import model.village.Village;

public class ArmyResourceManagement {

    private final Village village;

    public ArmyResourceManagement(Village village) {
        this.village = village;
    }

    public boolean checkResources(ArmyCost cost, int count) {

        Resources resources = village.getResources();

        return resources.getAmount(ResourcesType.WOOD) >= cost.wood() * count
                && resources.getAmount(ResourcesType.STONE) >= cost.stone() * count
                && resources.getAmount(ResourcesType.IRON) >= cost.iron() * count
                && resources.getAmount(ResourcesType.GUN_POWDER) >= cost.gunPowder() * count;
    }

    public void withdrawResources(ArmyCost cost, int count) {

        if (!checkResources(cost, count))
            return;

        Resources resources = village.getResources();

        resources.withdraw(ResourcesType.WOOD, cost.wood() * count);
        resources.withdraw(ResourcesType.STONE, cost.stone() * count);
        resources.withdraw(ResourcesType.IRON, cost.iron() * count);
        resources.withdraw(ResourcesType.GUN_POWDER, cost.gunPowder() * count);
    }
}