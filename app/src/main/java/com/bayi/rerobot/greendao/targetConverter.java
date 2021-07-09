package com.bayi.rerobot.greendao;

import com.google.gson.Gson;
import com.tobot.slam.data.LocationBean;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * Created by tongzn
 * on 2020/12/24
 */
public  class targetConverter implements PropertyConverter<LocationBean, String> {
    @Override
    public LocationBean convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        return new Gson().fromJson(databaseValue, LocationBean.class);
    }

    @Override
    public String convertToDatabaseValue(LocationBean entityProperty) {
        if (entityProperty == null) {
            return null;
        }
        return new Gson().toJson(entityProperty);
    }
}
