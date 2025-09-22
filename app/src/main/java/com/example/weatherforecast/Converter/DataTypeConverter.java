package com.example.weatherforecast.Converter;

import androidx.room.TypeConverter;
import com.example.weatherforecast.Bean.Data;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class DataTypeConverter {
    private static Gson gson = new Gson();

    @TypeConverter
    public static String fromData(Data data) {
        if (data == null) {
            return null;
        }
        return gson.toJson(data);
    }

    @TypeConverter
    public static Data toData(String dataString) {
        if (dataString == null) {
            return null;
        }
        Type type = new TypeToken<Data>() {}.getType();
        return gson.fromJson(dataString, type);
    }
}
