package com.bayi.rerobot.bean;

/**
 * Created by tongzn
 * on 2021/1/5
 */
public class ChatMsg {
    private String msg;
    private boolean left;

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
