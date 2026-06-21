package model.army;

import model.military.Millitary;

public class Army extends Millitary {

    private ArmyType type;

    public Army(int attackStr, int defenceStr, ArmyType type) {
        super(attackStr, defenceStr);
        this.type = type;
    }
}
