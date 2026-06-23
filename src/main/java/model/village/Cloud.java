package model.village;

import java.io.Serializable;

public class Cloud implements Serializable {
    private int radiation;

    public Cloud() {
        this.radiation = 2000;
    }

    public int getRadiation() {return radiation;}

    public void setRadiation(int radiation) {this.radiation = radiation;}
}


