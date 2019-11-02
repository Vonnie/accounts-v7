package com.kinsey.passwords.provider;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.kinsey.passwords.MainActivity;
import com.kinsey.passwords.items.Profile;
import com.kinsey.passwords.items.Suggest;
import com.kinsey.passwords.tools.Converters;

import java.util.Calendar;
import java.util.Date;

@Database(entities = {Profile.class}, version = 2, exportSchema = false)
public abstract class ProfileDatabase extends RoomDatabase {
    public static final String TAG = "ProfileDatabase";

    private static ProfileDatabase instance;

    public abstract ProfileDao profileDao();

//    public abstract SuggestDao suggestDao();

    boolean converted = false;

    public static synchronized ProfileDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProfileDatabase.class, "Passport")
                    .addMigrations(MIGRATION_1_2)
//                    .addMigrations(MIGRATION_2_1)
//                    .addMigrations(MIGRATION_2_3)
//                    .fallbackToDestructiveMigration()
//                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

//    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            // Since we didn't alter the table, there's nothing else to do here.
//        }
//    };

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.d(TAG, "about to migrate 1 TO 2");
            if (MainActivity.migration2Complete) {
                return;
            }
            MainActivity.migration2Complete = true;
//            database.execSQL("create TABLE `password_item_test` "
//                    + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + "password_text text NOT NULL, "
//                    + "sequence INTEGER)");
//            database.execSQL("ALTER TABLE `password_item` "
//                    + "DROP COLUMN ACTVY_DATE ");
//            database.execSQL("ALTER TABLE `password_item` "
//                    + "(DROP COLUMN ACTVY_DATE) ");
//            database.execSQL("DROP TABLE `password_item` ;");
//            database.execSQL("create TABLE `password_item_test` "
//                    + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + "password_text text NOT NULL, "
//                    + "sequence INTEGER)");

//            database.execSQL("ALTER TABLE password_item "
//                    + " ADD COLUMN actvy_long LONG");
//            Log.d(TAG, "about to migrate 1 TO 2 Update");
//            long ms = new Date().getTime();
//            database.execSQL("UPDATE password_item "
//                    + " SET actvy_long = " + ms);

        }
    };

    static final Migration MIGRATION_2_1 = new Migration(2, 1) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.d(TAG, "about to downgrade migrate");
//            database.execSQL("CREATE TABLE `password_item` (`id` INTEGER, "
//                    + "`name` TEXT, PRIMARY KEY(`id`))");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.d(TAG, "about to downgrade migrate 2 to 3");
//            database.execSQL("ALTER TABLE password_item_test "
//                    + " ADD COLUMN actvy_date DATETIME");
        }
    };

    static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.d(TAG, "about to downgrade migrate 3 to 4");
//            database.execSQL("ALTER TABLE password_item_test "
//                    + " ADD COLUMN actvy_date DATETIME");
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
//        private SuggestDao suggestDao;

        private PopulateDbAsyncTask(ProfileDatabase db) {

            profileDao = db.profileDao();
//            suggestDao = db.suggestDao();
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


//            suggestDao.insert(new Suggest("aaaaaa", 2));
//            suggestDao.insert(new Suggest("bbbbbb", 5));
//            suggestDao.insert(new Suggest("cccccc", 9));

            return null;
        }


    }

}
