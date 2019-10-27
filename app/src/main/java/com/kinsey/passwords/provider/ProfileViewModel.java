package com.kinsey.passwords.provider;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;

import com.kinsey.passwords.items.Profile;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.schedulers.Schedulers;

public class ProfileViewModel extends AndroidViewModel
        implements Task {
    public static final String TAG = "ProfileViewModel";
    private ProfileRepository repository;
    private LiveData<List<Profile>> allProfiles;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new ProfileRepository(application);
        allProfiles = repository.getAllProfiles();
    }

    public void insertProfile(Profile profile) {
        repository.insertProfile(profile, this);
    }

    public void update(Profile profile) {
        repository.update(profile);
    }

    public void delete(Profile profile) {
        repository.delete(profile);
    }

    public void deleteAllProfiles() {
        repository.deleteAllProfiles();
    }

    public void insertAll(List<Profile> profiles) {

        repository.insertAll(profiles);
    }


    public LiveData<List<Profile>> getAllProfiles() {
        allProfiles = repository.getAllProfiles();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesByPassportId() {
        allProfiles = repository.getAllProfilesByPassportId();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesByOpenDate() {
        allProfiles = repository.getAllProfilesByOpenDate();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesCustomSort() {
        allProfiles = repository.getAllProfilesCustomSort();
        return allProfiles;
    }

    public LiveData<List<Profile>> searchCorpNameProfiles(String query) {
        allProfiles = repository.searchCorpNameProfiles(query);
        return allProfiles;
    }

    @Override
    public void processInsert(Profile profile) {

        profile.setPassportId(profile.getId());
        repository.update(profile);

//        int intId = (int) id;
//        Log.d(TAG, "new id: " + id + " int " + intId);
//        LiveData<Profile> profile = repository.getProfileById(intId);
//
//
//        Log.d(TAG, "profile " + profile);
//        Log.d(TAG, "getValue " + profile.getValue().getPassportId());
//
//
//        profile.getValue().setPassportId(intId);
//        repository.update(profile.getValue());

//        LiveData<Profile> profileLiveData = repository.getProfileById(intId);
//        LiveData<String> profileName = Transformations.map(profileLiveData, profileld -> {
//            return profileld.getId() + " " + profileld.getPassportId();
//        });
//
//        Log.d(TAG, "profileName " + profileName);
//
//        Log.d(TAG, "get " + profileName.observe());




//        Profile profileItem = profile.getValue();
//        profileItem.setPassportId(intId);
//        repository.update(profileItem);

//        List<Profile> profileListFull = new ArrayList<Profile>(profiles);
//        for (Profile item : profileListFull) {
//            if (intId == item.getId()) {
//                item.setPassportId(intId);
//                update(item);
//                break;
//            }
//        }

    }
}
