package com.kinsey.passwords.provider;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kinsey.passwords.items.Profile;

import java.util.List;

@Dao
public interface ProfileDao {

    @Insert
    void insert(Profile profile);

    @Update
    void update(Profile profile);

    @Delete
    void delete(Profile profile);

    @Query("DELETE FROM Passport")
    void deleteAllProfiles();

    @Query("SELECT * FROM Passport ORDER BY corporation_name ASC")
    LiveData<List<Profile>> getAllProfiles();

    @Query("SELECT * FROM Passport ORDER BY passport_id ASC")
    LiveData<List<Profile>> getAllProfilesById();

    @Query("SELECT * FROM Passport ORDER BY open_date DESC")
    LiveData<List<Profile>> getAllProfilesByOpenDate();
}
