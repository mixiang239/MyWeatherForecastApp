package com.example.weatherforecast.DataBase;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Bean.LocatedCity;
import com.example.weatherforecast.Converter.DataTypeConverter;
import com.example.weatherforecast.Dao.CityDao;
import com.example.weatherforecast.Dao.LocatedCityDao;

@Database(entities = {LocatedCity.class}, version = 1)
@TypeConverters({DataTypeConverter.class})
public abstract class LocatedCityDatabase extends RoomDatabase {

    public abstract LocatedCityDao locatedCityDao();

    // 数据库迁移（版本1到2）
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
        }
    };
}
