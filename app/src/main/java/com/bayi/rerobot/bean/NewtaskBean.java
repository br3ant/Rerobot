package com.bayi.rerobot.bean;

public class NewtaskBean {
    private int type;
    private String mapName;
    private String sourceName;

    public void setType(int type) {
        this.type = type;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public NewtaskBean() {
    }

    public NewtaskBean(int type, String mapName, String sourceName) {
        this.type = type;
        this.mapName = mapName;
        this.sourceName = sourceName;
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
