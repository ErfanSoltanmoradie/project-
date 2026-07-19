package service.battle;

import model.army.Armies;
import model.army.ArmyStorage;
import model.army.ArmyType;
import model.battle.Battle;
import model.battle.BattleArmy;
import model.battle.TravelToBattleTask;
import model.building.ArmyProducer;
import model.building.Barrack;
import model.building.Building;
import model.building.BuildingType;
import model.village.Village;

import java.time.Duration;
import java.time.Instant;

public class BattleManagement {

    private final Village attackerVillage;
    private final Village defenderVillage;
    private final Armies armies;

    public BattleManagement(Village attackerVillage, Village defenderVillage) {
        this.attackerVillage = attackerVillage;
        this.defenderVillage = defenderVillage;
        this.armies =attackerVillage.getArmies();
    }

    private boolean canStartBattle(BattleArmy attackerArmy) {

        //check destination village
        if (defenderVillage == null) {return false;}

        //check dont battle to itself
        if(attackerVillage == defenderVillage) {return false;}

        if (!attackerVillage.getActiveBattles().isEmpty() || !defenderVillage.getActiveBattles().isEmpty()) {return false;}

        //army count must be > 0
        if(attackerArmy.getTotalArmyCount()==0) {return false;}

        ArmyProducer producer = getArmyProducer();
        if (producer == null) {return false;}

        Barrack barrack = getBarrack();
        if (barrack == null) {return false;}


        for(ArmyType type : ArmyType.values()){
            if(attackerArmy.getArmyCount(type) > armies.getArmyStorage().getArmyCount(type))
                return false;
        }


        return true;
    }

    //finding ArmyProducer
    private ArmyProducer getArmyProducer() {

        for (Building building : attackerVillage.getBuildings().values()) {

            if (building.getType() == BuildingType.ARMY_PRODUCER)
                return (ArmyProducer) building;
        }

        return null;
    }

    //finding Barack
    private Barrack getBarrack() {

        for (Building building : attackerVillage.getBuildings().values()) {

            if (building.getType() == BuildingType.BARRACKS)
                return (Barrack) building;
        }

        return null;
    }


    private void withdrawArmy(BattleArmy attackerArmy) {

        ArmyStorage armyStorage = armies.getArmyStorage();

        for (ArmyType type : ArmyType.values()) {

            int count = attackerArmy.getArmyCount(type);

            if (count > 0) {
                armyStorage.decreaseArmy(type, count);
            }
        }
    }


    public boolean startBattle(BattleArmy attackerArmy) {

        attackerVillage.getLock().writeLock().lock();

        try {

            if (!canStartBattle(attackerArmy))
                return false;

            withdrawArmy(attackerArmy);

            Battle battle = new Battle(
                    attackerVillage,
                    defenderVillage,
                    attackerArmy
            );

            attackerVillage.getActiveBattles().put(
                    battle.getBattleId(),
                    battle
            );

            defenderVillage.getActiveBattles().put(
                    battle.getBattleId(),
                    battle
            );

            TravelToBattleTask travelTask =
                    new TravelToBattleTask(
                            Instant.now(),
                            Duration.ofSeconds(battle.getTravelTime()),
                            battle
                    );

            attackerVillage.getTimedOperation().put(
                    travelTask.getId(),
                    travelTask
            );

            return true;

        } finally {
            attackerVillage.getLock().writeLock().unlock();
        }
    }
}
