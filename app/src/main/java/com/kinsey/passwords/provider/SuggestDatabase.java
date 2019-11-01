package com.kinsey.passwords.provider;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kinsey.passwords.items.Suggest;

@Database(entities = {Suggest.class}, version = 2, exportSchema = false)
public abstract class SuggestDatabase extends RoomDatabase {

    private static SuggestDatabase instance;

    public abstract SuggestDao suggestDao();

    public static synchronized SuggestDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SuggestDatabase.class, "suggest_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }


    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private SuggestDao suggestDao;

        private PopulateDbAsyncTask(SuggestDatabase db) {
            suggestDao = db.suggestDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            suggestDao.insert(new Suggest("aaaaaa", 2, 0l));
            suggestDao.insert(new Suggest("bbbbbb", 5, 0l));
            suggestDao.insert(new Suggest("cccccc", 9, 0l));
            return null;
        }
    }
}
