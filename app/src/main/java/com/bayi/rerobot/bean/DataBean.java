package com.bayi.rerobot.bean;


import com.bayi.rerobot.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tongzn
 * on 2020/9/15
 */
public class DataBean {
    public Integer imageRes;
    public String imageUrl;
    public String title;
    public int viewType;
    public DataBean(int banner, String s, int i) {
        this.imageRes=banner;
        this.title=s;
        this.viewType=i;
    }

    public Integer getImageRes() {
        return imageRes;
    }

    public void setImageRes(Integer imageRes) {
        this.imageRes = imageRes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public static List<DataBean> getData() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean(R.mipmap.ban1, "", 1));
        list.add(new DataBean(R.mipmap.ban2, "", 3));
        list.add(new DataBean(R.mipmap.ban3, "", 3));
        list.add(new DataBean(R.mipmap.ban5, "", 3));
        return list;
    }
    public static List<DataBean> getData1() {
        List<DataBean> list = new ArrayList<>();
        list.add(new DataBean(R.mipmap.ban7, "", 1));
        list.add(new DataBean(R.mipmap.ban8, "", 3));
        list.add(new DataBean(R.mipmap.ban6, "", 3));

        return list;
    }
    public static List<Integer> getImage() {
        List<Integer> list = new ArrayList<>();
        list.add(getResId("banner_1",R.drawable.class));
        list.add(getResId("banner_2",R.drawable.class));
        list.add(getResId("banner_3",R.drawable.class));
        return list;
    }

    /**
     * 通过文件名获取资源id 例子：getResId("icon", R.drawable.class);
     *
     * @param variableName
     * @param c
     * @return
     */
    public static int getResId(String variableName, Class<?> c) {
        try {
            Field idField = c.getDeclaredField(variableName);
            return idField.getInt(idField);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
