package com.kinsey.passwords.provider;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.tools.Converters;

import java.util.Calendar;
import java.util.Date;

@Database(entities = {Profile.class, Suggest.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class ProfileDatabase extends RoomDatabase {

    private static ProfileDatabase instance;

    public abstract ProfileDao profileDao();

    public abstract SuggestDao suggestDao();

    public static synchronized ProfileDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProfileDatabase.class, "Passport")
//                    .addMigrations(MIGRATION_1_2)
//                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProfileDao profileDao;
        private SuggestDao suggestDao;

        private PopulateDbAsyncTask(ProfileDatabase db) {

            profileDao = db.profileDao();
            suggestDao = db.suggestDao();
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


            suggestDao.insert(new Suggest("aaaaaa", 2, new Date()));
            suggestDao.insert(new Suggest("bbbbbb", 5, new Date()));
            suggestDao.insert(new Suggest("cccccc", 9, new Date()));

            return null;
        }


    }

}
