package com.bayi.rerobot.bean;

public class MainMsg {
    private int state;
    private String msg;
    private boolean  ask;

    public boolean isAsk() {
        return ask;
    }

    public MainMsg setAsk(boolean ask) {
        this.ask = ask;
        return this;
    }

    public int getState() {
        return state;
    }

    public MainMsg setState(int state) {
        this.state = state;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public MainMsg setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
