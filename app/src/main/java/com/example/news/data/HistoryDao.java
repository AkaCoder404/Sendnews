package com.example.news.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface HistoryDao {

    @Insert
    void insert(History history);

    @Update
    void update(History history);

    @Delete
    void delete(History history);

    @Query("DELETE FROM news_history_table")
    void deleteAllNotes();

    @Query("SELECT * FROM news_history_table ORDER BY time DESC")
    LiveData<List<History>> getAllHistory();


}
