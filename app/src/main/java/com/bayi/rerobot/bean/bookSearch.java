package com.bayi.rerobot.bean;

import org.json.JSONArray;

/**
 * Created by tongzn
 * on 2021/4/19
 */
public class bookSearch {
    private String intent;
    private JSONArray searchTxt;

    public JSONArray getSearchTxt() {
        return searchTxt;
    }

    public void setSearchTxt(JSONArray searchTxt) {
        this.searchTxt = searchTxt;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }
}
