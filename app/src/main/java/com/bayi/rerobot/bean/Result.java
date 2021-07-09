package com.bayi.rerobot.bean;

/**
 * Created by tongzn
 * on 2021/3/5
 */
public class Result {
    private String archiveno,type_name,remark,title;
    private boolean show;

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public String getArchiveno() {
        return archiveno;
    }

    public void setArchiveno(String archiveno) {
        this.archiveno = archiveno;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
