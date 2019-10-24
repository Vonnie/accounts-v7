package com.kinsey.passwords.provider;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.kinsey.passwords.items.Suggest;

import java.util.List;

public class SuggestRepository {
    private SuggestDao suggestDao;
    private LiveData<List<Suggest>> allSuggests;

    public SuggestRepository(Application application) {
        SuggestDatabase database = SuggestDatabase.getInstance(application);
        suggestDao = database.suggestDao();
        allSuggests = suggestDao.getAllSuggest();
    }

    public void insert(Suggest suggest) {
        new InsertSuggestAsyncTask(suggestDao).execute(suggest);
    }

    public void update(Suggest suggest) {
        new UpdateSuggestAsyncTask(suggestDao).execute(suggest);
    }

    public void delete(Suggest suggest) {
        new DeleteSuggestAsyncTask(suggestDao).execute(suggest);
    }

    public void deleteAllSuggests() {
        new DeleteAllSuggestsAsyncTask(suggestDao).execute();
    }

    public LiveData<List<Suggest>> getAllSuggests() {
        return allSuggests;
    }

    public LiveData<Suggest> getMaxSequence() {
        return suggestDao.getMaxSequence();
    }

    private static class InsertSuggestAsyncTask extends AsyncTask<Suggest, Void, Void> {
        private SuggestDao suggestDao;

        private InsertSuggestAsyncTask(SuggestDao suggestDao) {
            this.suggestDao = suggestDao;
        }

        @Override
        protected Void doInBackground(Suggest... suggests) {
            suggestDao.insert(suggests[0]);
            return null;
        }
    }

    private static class UpdateSuggestAsyncTask extends AsyncTask<Suggest, Void, Void> {
        private SuggestDao suggestDao;

        private UpdateSuggestAsyncTask(SuggestDao suggestDao) {
            this.suggestDao = suggestDao;
        }

        @Override
        protected Void doInBackground(Suggest... suggests) {
            suggestDao.update(suggests[0]);
            return null;
        }
    }

    private static class DeleteSuggestAsyncTask extends AsyncTask<Suggest, Void, Void> {
        private SuggestDao suggestDao;

        private DeleteSuggestAsyncTask(SuggestDao suggestDao) {
            this.suggestDao = suggestDao;
        }

        @Override
        protected Void doInBackground(Suggest... suggests) {
            suggestDao.delete(suggests[0]);
            return null;
        }
    }

    private static class DeleteAllSuggestsAsyncTask extends AsyncTask<Void, Void, Void> {
        private SuggestDao suggestDao;

        private DeleteAllSuggestsAsyncTask(SuggestDao suggestDao) {
            this.suggestDao = suggestDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            suggestDao.deleteAllSuggests();
            return null;
        }
    }

}
