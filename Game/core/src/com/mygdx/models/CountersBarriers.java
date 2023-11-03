package com.mygdx.models;

public class CountersBarriers {
    private int woodCounter = 10;
    private int cementCounter = 10;
    private int steelCounter = 10;
    private int eagleCounter = 1;

    public int getWoodCounter() {
        return woodCounter;
    }

    public int getCementCounter() {
        return cementCounter;
    }

    public int getSteelCounter() {
        return steelCounter;
    }

    public int getEagleCounter() {
        return eagleCounter;
    }

    public void setWoodCounter(int value) {
        woodCounter = value;
    }

    public void setCementCounter(int value) {
        cementCounter = value;
    }

    public void setSteelCounter(int value){
        steelCounter = value;
    }
    public void setEagleCounter(int value){
        eagleCounter = value;
    }
}