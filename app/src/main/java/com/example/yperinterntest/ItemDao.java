package com.example.yperinterntest;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ItemDao {
    @Insert
    void insert(RecyclerItem item);

    @Delete
    void delete(RecyclerItem item);

    @Query("SELECT * FROM items")
    List<RecyclerItem> getAllItem();

    @Query("SELECT * FROM items WHERE address=:address and time=:time")
    RecyclerItem getItem(String address, String time);

    @Query("select `key` from items  WHERE address=:address and time=:time")
    int getItemKey(String address, String time);

}
