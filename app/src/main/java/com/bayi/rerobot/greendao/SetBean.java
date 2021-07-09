package com.bayi.rerobot.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tongzn
 * on 2020/5/6
 */
@Entity
public class SetBean {
    @Id(autoincrement = true)
    private Long id;
    private float tem,hum,vvoc,co2,pm25,pm10;
    private String ymd,hms;
    @Generated(hash = 1702948745)
    public SetBean(Long id, float tem, float hum, float vvoc, float co2, float pm25,
            float pm10, String ymd, String hms) {
        this.id = id;
        this.tem = tem;
        this.hum = hum;
        this.vvoc = vvoc;
        this.co2 = co2;
        this.pm25 = pm25;
        this.pm10 = pm10;
        this.ymd = ymd;
        this.hms = hms;
    }
    @Generated(hash = 1000615903)
    public SetBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public float getTem() {
        return this.tem;
    }
    public void setTem(float tem) {
        this.tem = tem;
    }
    public float getHum() {
        return this.hum;
    }
    public void setHum(float hum) {
        this.hum = hum;
    }
    public float getVvoc() {
        return this.vvoc;
    }
    public void setVvoc(float vvoc) {
        this.vvoc = vvoc;
    }
    public float getCo2() {
        return this.co2;
    }
    public void setCo2(float co2) {
        this.co2 = co2;
    }
    public float getPm25() {
        return this.pm25;
    }
    public void setPm25(float pm25) {
        this.pm25 = pm25;
    }
    public float getPm10() {
        return this.pm10;
    }
    public void setPm10(float pm10) {
        this.pm10 = pm10;
    }
    public String getYmd() {
        return this.ymd;
    }
    public void setYmd(String ymd) {
        this.ymd = ymd;
    }
    public String getHms() {
        return this.hms;
    }
    public void setHms(String hms) {
        this.hms = hms;
    }
}
