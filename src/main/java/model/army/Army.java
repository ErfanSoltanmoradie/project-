package model.army;

import model.military.Millitary;

import java.io.Serializable;

public class Army extends Millitary implements Serializable {

    private ArmyType type;

    public Army(int attackStr, int defenceStr, ArmyType type) {
        super(attackStr, defenceStr);
        this.type = type;
    }
}
