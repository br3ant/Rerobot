package com.bayi.rerobot.greendao;

import com.tobot.slam.data.LocationBean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class Task {
    @Id(autoincrement = true)
    private Long id;
    private int state;
    private Long time;
    private int floor;
    @Property
    @Convert(columnType = String.class, converter = targetConverter.class)
    private LocationBean targetname;
    @Generated(hash = 1756381981)
    public Task(Long id, int state, Long time, int floor, LocationBean targetname) {
        this.id = id;
        this.state = state;
        this.time = time;
        this.floor = floor;
        this.targetname = targetname;
    }
    @Generated(hash = 733837707)
    public Task() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getState() {
        return this.state;
    }
    public void setState(int state) {
        this.state = state;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public int getFloor() {
        return this.floor;
    }
    public void setFloor(int floor) {
        this.floor = floor;
    }
    public LocationBean getTargetname() {
        return this.targetname;
    }
    public void setTargetname(LocationBean targetname) {
        this.targetname = targetname;
    }
}
