package com.bayi.rerobot.tobot;

/**
 * Created by tongzn
 * on 2020/8/28
 */
public class Status {
    private int locationQuality,battery;
    private String status;
    private boolean chargeStatus;
    public int getLocationQuality() {
        return locationQuality;
    }

    public void setLocationQuality(int locationQuality) {
        this.locationQuality = locationQuality;
    }

    public boolean isChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(boolean chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public int getBattery() {
        return battery;
    }

    public void setBattery(int battery) {
        this.battery = battery;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
