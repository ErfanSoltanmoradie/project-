package model.village;

import java.io.Serializable;

public class Cloud implements Serializable {
    private int radiation;
    private int neutralized = 300;

    public Cloud() {
        this.radiation = 2000;
    }

    public int getRadiation() {return radiation;}

    public void setRadiation(int radiation) {this.radiation = radiation;}

    public int getNeutralized() {
        return neutralized;
    }

    public void setNeutralized(int neutralized) {
        this.neutralized = neutralized;
    }

    public boolean isFullyNeutralized() {
        return this.radiation <= 0;
    }
}