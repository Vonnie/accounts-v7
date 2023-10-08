package com.kinsey.passwords.provider;


import static com.kinsey.passwords.provider.ProfileDatabase.TAG;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.kinsey.passwords.items.Profile;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ProfileDao {

//    @Query("SELECT * FROM passport_detail")
//    LiveData<List<Profile>> getAllProfiles();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Profile profile);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insertProfile(Profile profile);
    @Update
    void updateItem(Profile profile);
    @Transaction
    public default void addProfile(Profile profile) {
        long accountId = insertProfile(profile);
        Log.d(TAG, "inserted Profile Id " + accountId);

        profile.setPassportId((int)accountId);
        profile.setId((int)accountId);
        updateItem(profile);
        Log.d(TAG, "updated Profile " + profile.getCorpName() + ":" + profile.getPassportId() + ":" + profile.getId());
    }


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    Long[] insertProfiles(List<Profile> profiles);

    @Transaction
    public default Long[] addProfiles(List<Profile> profiles) {
        Long[] acctids = new Long[profiles.size()];
        for(int i=0;i<profiles.size(); i++){
            Profile profileAcct = profiles.get(i);
            long accountId = insertProfile(profileAcct);
            Log.d(TAG, "inserted Profile Id " + accountId);

            profileAcct.setPassportId((int)accountId);
            profileAcct.setId((int)accountId);
            updateItem(profileAcct);
            acctids[i] = accountId;
            Log.d(TAG, "updated Profile " + profileAcct.getCorpName() + ":" + profileAcct.getPassportId() + ":" + profileAcct.getId());
        }
        return acctids;
    }


    @Update
    void update(Profile profile);

    @Delete
    void delete(Profile profile);

    @Query("DELETE FROM passport_detail")
    void deleteAllProfiles();

    @Query("DELETE FROM passport_detail where _id = :id")
    void deleteProfileId(int id);



    @Query("SELECT * FROM passport_detail ORDER BY corporation_name COLLATE NOCASE ASC")
    LiveData<List<Profile>> getAllProfilesByCorpName();

    @Query("SELECT * FROM passport_detail ORDER BY passport_id ASC")
    LiveData<List<Profile>> getAllProfilesByPassportId();

    @Query("SELECT * FROM passport_detail ORDER BY open_date DESC, corporation_name ASC")
    LiveData<List<Profile>> getAllProfilesByOpenDate();

    @Query("SELECT * FROM passport_detail ORDER BY sequence ASC, corporation_name COLLATE NOCASE ASC")
    LiveData<List<Profile>> getAllProfilesCustomSort();

    @Query("SELECT * FROM passport_detail where corporation_name LIKE :name or LOWER(corporation_name) like LOWER(:name) order by corporation_name COLLATE NOCASE ASC")
    LiveData<List<Profile>> searchCorpNameProfiles(String name);

    @Query("SELECT * FROM passport_detail where _id = :id")
    public LiveData<Profile> getProfileById(int id);

    @Query("SELECT * FROM passport_detail ORDER BY sequence DESC LIMIT 1")
    LiveData<Profile> getMaxSequence();

    @Query("SELECT count(*) FROM passport_detail")
    LiveData<Integer> getCount();



}
