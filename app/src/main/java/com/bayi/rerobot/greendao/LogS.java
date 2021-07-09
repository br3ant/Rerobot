package com.bayi.rerobot.greendao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by tongzn
 * on 2020/5/4
 */
@Entity
public class LogS {
    @Id(autoincrement = true)
    private Long id;
    private String ymd,hm,msg;
    @Generated(hash = 2070040211)
    public LogS(Long id, String ymd, String hm, String msg) {
        this.id = id;
        this.ymd = ymd;
        this.hm = hm;
        this.msg = msg;
    }
    @Generated(hash = 1578331109)
    public LogS() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getYmd() {
        return this.ymd;
    }
    public void setYmd(String ymd) {
        this.ymd = ymd;
    }
    public String getHm() {
        return this.hm;
    }
    public void setHm(String hm) {
        this.hm = hm;
    }
    public String getMsg() {
        return this.msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}