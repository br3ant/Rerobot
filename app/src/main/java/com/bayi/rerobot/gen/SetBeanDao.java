package com.bayi.rerobot.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.bayi.rerobot.greendao.SetBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SET_BEAN".
*/
public class SetBeanDao extends AbstractDao<SetBean, Long> {

    public static final String TABLENAME = "SET_BEAN";

    /**
     * Properties of entity SetBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Tem = new Property(1, float.class, "tem", false, "TEM");
        public final static Property Hum = new Property(2, float.class, "hum", false, "HUM");
        public final static Property Vvoc = new Property(3, float.class, "vvoc", false, "VVOC");
        public final static Property Co2 = new Property(4, float.class, "co2", false, "CO2");
        public final static Property Pm25 = new Property(5, float.class, "pm25", false, "PM25");
        public final static Property Pm10 = new Property(6, float.class, "pm10", false, "PM10");
        public final static Property Ymd = new Property(7, String.class, "ymd", false, "YMD");
        public final static Property Hms = new Property(8, String.class, "hms", false, "HMS");
    }


    public SetBeanDao(DaoConfig config) {
        super(config);
    }
    
    public SetBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SET_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TEM\" REAL NOT NULL ," + // 1: tem
                "\"HUM\" REAL NOT NULL ," + // 2: hum
                "\"VVOC\" REAL NOT NULL ," + // 3: vvoc
                "\"CO2\" REAL NOT NULL ," + // 4: co2
                "\"PM25\" REAL NOT NULL ," + // 5: pm25
                "\"PM10\" REAL NOT NULL ," + // 6: pm10
                "\"YMD\" TEXT," + // 7: ymd
                "\"HMS\" TEXT);"); // 8: hms
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SET_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, SetBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindDouble(2, entity.getTem());
        stmt.bindDouble(3, entity.getHum());
        stmt.bindDouble(4, entity.getVvoc());
        stmt.bindDouble(5, entity.getCo2());
        stmt.bindDouble(6, entity.getPm25());
        stmt.bindDouble(7, entity.getPm10());
 
        String ymd = entity.getYmd();
        if (ymd != null) {
            stmt.bindString(8, ymd);
        }
 
        String hms = entity.getHms();
        if (hms != null) {
            stmt.bindString(9, hms);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, SetBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindDouble(2, entity.getTem());
        stmt.bindDouble(3, entity.getHum());
        stmt.bindDouble(4, entity.getVvoc());
        stmt.bindDouble(5, entity.getCo2());
        stmt.bindDouble(6, entity.getPm25());
        stmt.bindDouble(7, entity.getPm10());
 
        String ymd = entity.getYmd();
        if (ymd != null) {
            stmt.bindString(8, ymd);
        }
 
        String hms = entity.getHms();
        if (hms != null) {
            stmt.bindString(9, hms);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public SetBean readEntity(Cursor cursor, int offset) {
        SetBean entity = new SetBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getFloat(offset + 1), // tem
            cursor.getFloat(offset + 2), // hum
            cursor.getFloat(offset + 3), // vvoc
            cursor.getFloat(offset + 4), // co2
            cursor.getFloat(offset + 5), // pm25
            cursor.getFloat(offset + 6), // pm10
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // ymd
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // hms
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, SetBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTem(cursor.getFloat(offset + 1));
        entity.setHum(cursor.getFloat(offset + 2));
        entity.setVvoc(cursor.getFloat(offset + 3));
        entity.setCo2(cursor.getFloat(offset + 4));
        entity.setPm25(cursor.getFloat(offset + 5));
        entity.setPm10(cursor.getFloat(offset + 6));
        entity.setYmd(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setHms(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(SetBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(SetBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(SetBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
