package com.bayi.rerobot.greendao;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tobot.slam.data.LocationBean;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tongzn
 * on 2020/12/24
 */
public class LocationBeanConverter implements PropertyConverter<List<LocationBean>,String> {
    private final Gson mGson;

    public LocationBeanConverter() {
        mGson = new Gson();

    }

    @Override
    public List<LocationBean> convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<ArrayList<LocationBean>>() {
        }.getType();
        ArrayList<LocationBean> itemList= mGson.fromJson(databaseValue, type);
        return itemList;
    }

    @Override
    public String convertToDatabaseValue(List<LocationBean> entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }
}
