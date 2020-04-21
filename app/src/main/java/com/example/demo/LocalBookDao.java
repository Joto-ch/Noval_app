package com.example.demo;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface LocalBookDao {

    @Query("select * from LocalBook")
    List<LocalBook> getAllLocalBook();

    @Query("select * from LocalBook where name = :name")
    LocalBook getLocalBook(String name);

    @Insert
    void insertLocalBook(LocalBook... localBooks);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updataLocalBook(LocalBook... localBooks);

    @Delete
    void deleteLocalBook(LocalBook... localBooks);
}
