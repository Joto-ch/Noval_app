package com.example.demo;

import androidx.room.Database;
import androidx.room.RoomDatabase;
@Database(entities = {LocalBook.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract LocalBookDao getLocalBookDao();
}
