package com.example.weatherforecast.DataBase;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Converter.DataTypeConverter;
import com.example.weatherforecast.Dao.CityDao;

@Database(entities = {City.class}, version = 3)
@TypeConverters({DataTypeConverter.class})
public abstract class CityDataBase extends RoomDatabase {
    public abstract CityDao cityDao();

    // 数据库迁移
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 添加新列
            database.execSQL("ALTER TABLE city_room ADD COLUMN city_data TEXT");
        }
    };

    // 数据库迁移 - 从版本2到3（添加locationId列）
    public static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // 添加新列
            database.execSQL("ALTER TABLE city_room ADD COLUMN city_location_id TEXT");
        }
    };
}
