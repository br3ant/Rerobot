package com.bayi.rerobot.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.bayi.rerobot.App;


public class GetYearHelp {
    private static final String NAME = "config";
    private static final String INIT_YEAR = "init_year";
    private static final String INIT_HOUR_YEAR = "init_hour_year";

    private static SharedPreferences getSharedPreferences() {
        return App.context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static void saveInitYear(int year) {
        getSharedPreferences().edit().putInt(INIT_YEAR, year).apply();
    }

    public static void saveInitHourYear(int year) {

        getSharedPreferences().edit().putInt(INIT_HOUR_YEAR, year).apply();
    }

    public static int getInitYear() {
        return getSharedPreferences().getInt(INIT_YEAR, 2017);
    }

    public static int getInitHourYear() {
        return getSharedPreferences().getInt(INIT_HOUR_YEAR, 2017);
    }

}
