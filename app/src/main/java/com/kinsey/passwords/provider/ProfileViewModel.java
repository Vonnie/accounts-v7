package com.kinsey.passwords.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.items.Suggest;

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
        return allProfiles;
    }

}
