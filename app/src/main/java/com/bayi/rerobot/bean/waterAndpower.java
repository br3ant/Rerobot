package com.bayi.rerobot.bean;

/**
 * Created by tongzn
 * on 2020/5/29
 */
public class waterAndpower {
    int water,power;
     private boolean charge;

    public boolean isCharge() {
        return charge;
    }

    public waterAndpower setCharge(boolean charge) {
        this.charge = charge;
        return this;
    }

    public int getWater() {
        return water;
    }

    public waterAndpower setWater(int water) {
        this.water = water;
        return this;
    }

    public int getPower() {
        return power;
    }

    public waterAndpower setPower(int power) {
        this.power = power;
        return  this;
    }
}
