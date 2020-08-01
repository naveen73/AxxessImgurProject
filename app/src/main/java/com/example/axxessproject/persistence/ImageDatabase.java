package com.example.axxessproject.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.axxessproject.models.ImageItem;

@Database(entities = {ImageItem.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class ImageDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "recipes_db";

    private static ImageDatabase instance;

    public static ImageDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    ImageDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract ImageDao getImageDao();

}






