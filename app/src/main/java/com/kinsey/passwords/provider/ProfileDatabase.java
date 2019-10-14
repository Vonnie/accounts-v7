package com.kinsey.passwords.provider;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kinsey.passwords.items.Profile;

@Database(entities = Profile.class, version = 1, exportSchema = false)
public abstract class ProfileDatabase extends RoomDatabase {

    private static ProfileDatabase instance;

    public abstract ProfileDao profileDao();

    public static synchronized ProfileDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProfileDatabase.class, "profile_database")
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
        private ProfileDao profileDao;

        private PopulateDbAsyncTask(ProfileDatabase db) {
            profileDao = db.profileDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            profileDao.insert(new Profile(1, "vonnie1", "vonnie1", "vonniekinsey@gmail.com", ""));
            profileDao.insert(new Profile(2, "vonnie2", "vonnie2", "vonniekinsey@gmail.com", ""));
            profileDao.insert(new Profile(3, "vonnie3", "vonnie3", "vonniekinsey@gmail.com", ""));
            return null;
        }
    }

}
