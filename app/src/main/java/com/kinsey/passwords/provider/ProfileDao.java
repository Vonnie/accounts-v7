package com.kinsey.passwords.provider;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.items.Suggest;

import java.util.List;

import javax.sql.DataSource;

@Dao
public interface ProfileDao {

    @Insert
    Long insert(Profile profile);

//    @Insert
//    Long[] insertAll(Profile[] profiles);

    @Insert
    Long insertProfile(Profile profile);

    @Update
    void update(Profile profile);

    @Delete
    void delete(Profile profile);

    @Query("DELETE FROM profile_detail")
    void deleteAllProfiles();

    @Query("SELECT * FROM profile_detail ORDER BY corporation_name COLLATE NOCASE ASC")
    LiveData<List<Profile>> getAllProfiles();

    @Query("SELECT * FROM profile_detail ORDER BY corporation_name COLLATE NOCASE ASC")
    LiveData<List<Profile>> getAllProfilesByCorpName();

    @Query("SELECT * FROM profile_detail ORDER BY passport_id ASC")
    LiveData<List<Profile>> getAllProfilesByPassportId();

    @Query("SELECT * FROM profile_detail ORDER BY open_date DESC, corporation_name ASC")
    LiveData<List<Profile>> getAllProfilesByOpenDate();

    @Query("SELECT * FROM profile_detail ORDER BY sequence ASC, corporation_name COLLATE NOCASE ASC")
    LiveData<List<Profile>> getAllProfilesCustomSort();

    @Query("SELECT * FROM profile_detail where corporation_name LIKE :name or LOWER(corporation_name) like LOWER(:name) order by corporation_name COLLATE NOCASE ASC")
    LiveData<List<Profile>> searchCorpNameProfiles(String name);

    @Query("SELECT * FROM profile_detail where _id = :id")
    LiveData<Profile> getProfileById(int id);

    @Query("SELECT * FROM profile_detail ORDER BY sequence DESC LIMIT 1")
    LiveData<Profile> getMaxSequence();

}
