package com.example.weatherforecast.Converter;

import androidx.room.TypeConverter;
import com.example.weatherforecast.Bean.Data;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.concurrent.locks.ReentrantLock;

//public class DataTypeConverter {
//    private static Gson gson = new Gson();
//
//    @TypeConverter
//    public static String fromData(Data data) {
//        if (data == null) {
//            return null;
//        }
//        synchronized (data) {
//            return gson.toJson(data);
//        }
//        //return gson.toJson(data);
//    }
//
//    @TypeConverter
//    public static Data toData(String dataString) {
//        if (dataString == null) {
//            return null;
//        }
//        Type type = new TypeToken<Data>() {}.getType();
//        return gson.fromJson(dataString, type);
//    }
//}


public class DataTypeConverter {
    private static final Gson gson = new Gson();
    private static final Type dataType = new TypeToken<Data>() {}.getType();

    // 使用可重入锁确保线程安全
    private static final ReentrantLock lock = new ReentrantLock();

    @TypeConverter
    public static String fromData(Data data) {
        if (data == null) {
            return null;
        }

        lock.lock(); // 获取锁
        try {
            return gson.toJson(data);
        } finally {
            lock.unlock(); // 确保锁被释放
        }
    }

    @TypeConverter
    public static Data toData(String dataString) {
        if (dataString == null) {
            return null;
        }

        lock.lock(); // 获取锁
        try {
            return gson.fromJson(dataString, dataType);
        } finally {
            lock.unlock(); // 确保锁被释放
        }
    }
}
