package com.example.demo;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LocalBook {

    @NonNull
    @PrimaryKey
    private String name;

    @ColumnInfo(name = "author")
    private String author;
    @ColumnInfo(name = "cover_url")
    private String cover_url;
    @ColumnInfo(name = "read_url")
    private String read_url;

    public LocalBook(@NonNull String name, String author, String cover_url, String read_url) {
        this.name = name;
        this.author = author;
        this.cover_url = cover_url;
        this.read_url = read_url;
    }

    @NonNull
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getRead_url() {
        return read_url;
    }

    public void setRead_url(String read_url) {
        this.read_url = read_url;
    }
}
