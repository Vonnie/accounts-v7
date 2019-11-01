package com.kinsey.passwords.provider;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kinsey.passwords.items.Profile;

import java.util.Calendar;
import java.util.Date;

@Database(entities = Profile.class, version = 1, exportSchema = false)
public abstract class ProfileDatabase extends RoomDatabase {

    private static ProfileDatabase instance;

    public abstract ProfileDao profileDao();

    public static synchronized ProfileDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProfileDatabase.class, "Passport")
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
            Date dte = new Date();
            Calendar c1 = Calendar.getInstance();
            c1.setTime(dte);

            Profile profile = new Profile(0, "Corp 1 Sample", "user A", "aaa@xxx.com", "");
            profile.setPassportId(1);
            profile.setNote("Sample data only to show data");
            profile.setOpenLong(c1.getTimeInMillis());
            profile.setActvyLong(c1.getTimeInMillis());
            profileDao.insertProfile(profile);
            profile = new Profile(9, "Corp 2 Sample", "user B", "bbb@xxx.com", "");
            profile.setPassportId(2);
            profile.setNote("Sample data only to show data");
            profile.setOpenLong(c1.getTimeInMillis());
            profile.setActvyLong(c1.getTimeInMillis());
            profileDao.insertProfile(profile);
            profile = new Profile(0, "Corp 3 Sample", "user C", "ccc@xxx.com", "");
            profile.setPassportId(3);
            profile.setNote("Sample data only to show data");
            profile.setOpenLong(c1.getTimeInMillis());
            profile.setActvyLong(c1.getTimeInMillis());
            profileDao.insertProfile(profile);
            return null;
        }


    }

}
