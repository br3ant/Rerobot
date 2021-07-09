package com.bayi.rerobot.greendao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class AddTimer {
    @Id(autoincrement = true)
    private Long id;
    private String week; //消毒 once everyday 1234567周期
    private int modetype;//类型
    private int roundnum;//消毒次数
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> areaNameList;//消毒区域
    private boolean state=true;//是否开启
    private String time;//时间
    @Generated(hash = 1832696609)
    public AddTimer(Long id, String week, int modetype, int roundnum,
            List<String> areaNameList, boolean state, String time) {
        this.id = id;
        this.week = week;
        this.modetype = modetype;
        this.roundnum = roundnum;
        this.areaNameList = areaNameList;
        this.state = state;
        this.time = time;
    }
    @Generated(hash = 433332369)
    public AddTimer() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getWeek() {
        return this.week;
    }
    public void setWeek(String week) {
        this.week = week;
    }
    public int getModetype() {
        return this.modetype;
    }
    public void setModetype(int modetype) {
        this.modetype = modetype;
    }
    public int getRoundnum() {
        return this.roundnum;
    }
    public void setRoundnum(int roundnum) {
        this.roundnum = roundnum;
    }
    public List<String> getAreaNameList() {
        return this.areaNameList;
    }
    public void setAreaNameList(List<String> areaNameList) {
        this.areaNameList = areaNameList;
    }
    public boolean getState() {
        return this.state;
    }
    public void setState(boolean state) {
        this.state = state;
    }
    public String getTime() {
        return this.time;
    }
    public void setTime(String time) {
        this.time = time;
    }
}
