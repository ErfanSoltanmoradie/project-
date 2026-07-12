package model.military;

public abstract class Millitary {
    private int attackStr;
    private int defenceStr;

    public Millitary(int attackStr, int defenceStr) {
        this.attackStr = attackStr;
        this.defenceStr = defenceStr;
    }
}
