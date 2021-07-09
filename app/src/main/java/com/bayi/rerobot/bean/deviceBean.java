package com.bayi.rerobot.bean;

/**
 * Created by tongzn
 * on 2020/5/8
 */
public class deviceBean {
    //1成功2失败
    private int state=0;
    private int pic;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPic() {
        return pic;
    }

    public void setPic(int pic) {
        this.pic = pic;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    private String text;
}
