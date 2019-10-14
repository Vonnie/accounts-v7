package com.kinsey.passwords.provider;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.provider.SuggestRepository;

import java.util.List;

public class SuggestViewModel extends AndroidViewModel {
    private SuggestRepository repository;
    private LiveData<List<Suggest>> allSuggests;

    public SuggestViewModel(@NonNull Application application) {
        super(application);
        repository = new SuggestRepository(application);
        allSuggests = repository.getAllSuggests();
    }

    public void insert(Suggest suggest) {
        repository.insert(suggest);
    }

    public void update(Suggest suggest) {
        repository.update(suggest);
    }

    public void delete(Suggest suggest) {
        repository.delete(suggest);
    }

    public void deleteAllSuggests() {
        repository.deleteAllSuggests();
    }

    public LiveData<List<Suggest>> getAllSuggests() {
        return allSuggests;
    }

}
