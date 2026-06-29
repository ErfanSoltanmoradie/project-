package model.village;

public class Cloud {
    private int radiation;
    private int neutralized;

    public Cloud() {
        this.radiation = 2000;
    }

    public int getRadiation() {return radiation;}

    public void setRadiation(int radiation) {this.radiation = radiation;}

    public void setNeutralized(int neutralized) {
        this.neutralized =neutralized;
    }
    public int getNeutralized() {return neutralized;}
}


