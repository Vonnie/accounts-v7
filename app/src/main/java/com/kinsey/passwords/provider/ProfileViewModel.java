package com.kinsey.passwords.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.kinsey.passwords.items.Profile;

import java.util.List;

public class ProfileViewModel extends AndroidViewModel {
    private ProfileRepository repository;
    private LiveData<List<Profile>> allProfiles;

    public ProfileViewModel(@NonNull Application application) {
        super(application);
        repository = new ProfileRepository(application);
        allProfiles = repository.getAllProfiles();
    }

    public void insert(Profile profile) {
        repository.insert(profile);
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


    public LiveData<List<Profile>> getAllProfiles() {
        allProfiles = repository.getAllProfiles();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesById() {
        allProfiles = repository.getAllProfilesById();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesByOpenDate() {
        allProfiles = repository.getAllProfilesByOpenDate();
        return allProfiles;
    }

    public LiveData<List<Profile>> searchCorpNameProfiles(String query) {
        allProfiles = repository.searchCorpNameProfiles(query);
        return allProfiles;
    }

}
