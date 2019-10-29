package com.kinsey.passwords.provider;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kinsey.passwords.items.Suggest;

import java.util.List;

@Dao
public interface SuggestDao {

    @Insert
    void insert(Suggest suggest);

    @Update
    void update(Suggest suggest);

    @Delete
    void delete(Suggest suggest);

    @Query("DELETE FROM password_item")
    void deleteAllSuggests();

    @Query("SELECT * FROM password_item ORDER BY sequence ASC")
    LiveData<List<Suggest>> getAllSuggest();

    @Query("SELECT * FROM password_item ORDER BY sequence DESC LIMIT 1")
    LiveData<Suggest> getMaxSequence();

//    @Query("SELECT MAX(sequence) FROM password_item_v2 ORDER BY sequence DESC")
//    LiveData<Suggest> getMaxSuggestSeq();
}
