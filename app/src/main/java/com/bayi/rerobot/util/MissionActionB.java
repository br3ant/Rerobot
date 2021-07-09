package com.bayi.rerobot.util;

import com.tobot.slam.data.LocationBean;

/**
 * Created by tongzn
 * on 2020/12/24
 */
public class MissionActionB {
    private int type;
    private String mapName;
    private String sourceName;

    public LocationBean getBean() {
        return bean;
    }

    public void setBean(LocationBean bean) {
        this.bean = bean;
    }

    private LocationBean bean;
    public MissionActionB() {
    }

    public MissionActionB(int type, String mapName, String sourceName, LocationBean bean) {
        this.type = type;
        this.mapName = mapName;
        this.sourceName = sourceName;
        this.bean=bean;
    }

    public int getType() {
        return this.type;
    }

    public String getStringToSave() {
        return this.type == 0 ? "[NavigationTask#" + this.mapName + "#" + this.sourceName + "]," : (this.type == 1 ? "[PlayPathTask#" + this.mapName + "#" + this.sourceName + "]," : "[PlayGraphPathTask#" + this.mapName + "#" + this.sourceName + "#" + this.sourceName + "],");
    }

    public String getStringToShow(int language) {
        if (language == 0) {
            return this.type == 0 ? "前往目标点" + this.sourceName : (this.type == 2 ? "沿着手绘路径" + this.sourceName + "走" : "沿着录制路径" + this.sourceName + "走");
        } else {
            return this.type == 0 ? "GoTo " + this.sourceName : (this.type == 2 ? "FollowGraphPath " + this.sourceName : "FollowRecordPath " + this.sourceName);
        }
    }
}
