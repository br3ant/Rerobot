package com.bayi.rerobot.bean;

/**
 * Created by tongzn
 * on 2021/3/2
 */
public class CewenBean {
    private String base64;
    private int level=0;//温度级别
    private String name;
    private String time;
    private String tem;

    public String getBase64() {
        return base64;
    }

    public void setBase64(String base64) {
        this.base64 = base64;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }
}
