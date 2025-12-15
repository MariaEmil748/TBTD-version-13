package com.testing.inventorytracking;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Tiasset.class}, version = 17, exportSchema = false)
public abstract class MyRoomDatabase extends RoomDatabase {
    public abstract Roominterface roominterface();
    private  static MyRoomDatabase instance ;



    public static  synchronized MyRoomDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),MyRoomDatabase.class,"Info_DataBase")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return  instance ;
    }

}
