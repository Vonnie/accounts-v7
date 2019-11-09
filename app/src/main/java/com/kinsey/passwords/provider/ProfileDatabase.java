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
import com.kinsey.passwords.tools.Converters;

import java.util.Calendar;
import java.util.Date;

@Database(entities = {Profile.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class ProfileDatabase extends RoomDatabase {
    public static final String TAG = "ProfileDatabase";

    private static ProfileDatabase instance;

    public abstract ProfileDao profileDao();

//    public abstract SuggestDao suggestDao();


    boolean converted = false;
    static int countMig = 0;

    public static synchronized ProfileDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ProfileDatabase.class, "Passport")
                    .addMigrations(MIGRATION_1_2)
                    .addMigrations(MIGRATION_2_1)
                    .addMigrations(MIGRATION_2_3)
                    .addMigrations(MIGRATION_3_2)
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
            Log.d(TAG, "about to migrate 1 TO 2 a");
            Log.d(TAG, "about to migrate 1 TO 2 b");
            if (countMig > 0) {
                Log.d(TAG, "only one migration");
                return;
            }
            MainActivity.migrationStarted = true;
            MainActivity.profileMigrateLevel = 2;
            migrateWork();

            Log.d(TAG, "create passport_detail_mig");
//            database.execSQL("DROP TABLE IF EXISTS passport_detail_mig");

            database.execSQL("create TABLE passport_detail_mig "
                    + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT 0, "
                    + "passport_id INTEGER NOT NULL DEFAULT 0, "
                    + "sequence INTEGER NOT NULL DEFAULT 0, "
                    + "open_date INTEGER NOT NULL DEFAULT 0, "
                    + "actvy_date INTEGER NOT NULL DEFAULT 0, "
                    + "corporation_name TEXT DEFAULT '', "
                    + "user_name TEXT DEFAULT '', "
                    + "user_email TEXT DEFAULT '', "
                    + "corporation_website TEXT DEFAULT '', "
                    + "passport_note TEXT DEFAULT '', "
                    + "ref_from INTEGER NOT NULL DEFAULT 0, "
                    + "ref_to INTEGER NOT NULL DEFAULT 0)");

//            + "user_name TEXT NOT NULL DEFAULT '', "
//                    + "user_email TEXT NOT NULL DEFAULT '', "
//            + "passport_note TEXT NOT NULL DEFAULT '', "
//            + "corporation_name TEXT NOT NULL DEFAULT '', "


            database.execSQL(
                    "UPDATE passport_detail " +
                            "SET corporation_website = '' " +
                            "WHERE corporation_website IS NULL");

            database.execSQL(
                    "UPDATE passport_detail " +
                            "SET passport_note = '' " +
                            "WHERE passport_note IS NULL");

            long currDate = new Date().getTime();
            database.execSQL(
                    "UPDATE passport_detail " +
                            "SET open_date = " + currDate +
                            " WHERE open_date IS NULL");
            database.execSQL(
                    "UPDATE passport_detail " +
                            "SET actvy_date = " + currDate +
                            " WHERE actvy_date IS NULL");
            database.execSQL(
                    "UPDATE passport_detail " +
                            "SET sequence = 0 " +
                            " WHERE sequence IS NULL");
            database.execSQL(
                    "UPDATE passport_detail " +
                            "SET ref_from = 0 " +
                            " WHERE ref_from IS NULL");
            database.execSQL(
                    "UPDATE passport_detail " +
                            "SET ref_to = 0 " +
                            " WHERE ref_to IS NULL");

            Log.d(TAG, "insert into passport_detail_mig");
            database.execSQL(
                    "INSERT INTO passport_detail_mig"
                    + "(passport_id, sequence, open_date, "
                    + "actvy_date, corporation_name, user_name, "
                    + "user_email, corporation_website, "
                    + "passport_note, ref_from, ref_to) "
                    + "SELECT passport_id, sequence, open_date, actvy_date, "
                    + "corporation_name, user_name, "
                    + "user_email, corporation_website, "
                    + "passport_note, ref_from, ref_to "
                    + "FROM passport_detail ");

//            database.execSQL(
//                    "INSERT INTO passport_detail_mig"
//                    + "(passport_id, sequence, open_date, "
//                    + "actvy_date, corporation_name, user_name, "
//                    + "user_email, corporation_website, "
//                    + "passport_note, ref_from, ref_to) "
//                    + "SELECT passport_id, sequence, open_date, actvy_date, "
//                    + "corporation_name, user_name, "
//                    + "user_email, corporation_website, "
//                    + "passport_note, 0, 0 "
//                    + "FROM passport_detail ");

            //            database.execSQL(
//                    "INSERT INTO passport_detail_mig"
//                            + "(passport_id, sequence, open_date, "
//                            + "actvy_date, corporation_name, user_name, "
//                            + "user_email, corporation_website, "
//                            + "passport_note, ref_from, ref_to) "
//                            + "SELECT passport_id, sequence, open_date, "
//                            + "actvy_date, corporation_name, user_name, "
//                            + "user_email, corporation_website, "
//                            + "passport_note, ref_from, "
//                            + "ref_to FROM passport_detail ");

            database.execSQL("DROP TABLE passport_detail");
            Log.d(TAG, "drop table passport_detail_mig");

            Log.d(TAG, "rename passport_detail_mig");
            database.execSQL(
                    "ALTER TABLE passport_detail_mig RENAME to passport_detail");

            countMig += 1;
            Log.d(TAG, "migration 2 complete " + countMig);
//            if (MainActivity.migration2Complete) {
//                return;
//            }
//            MainActivity.migration2Complete = true;
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
            Log.d(TAG, "about to upgrade migrate 2 to 3");
            Log.d(TAG, "about to upgrade migrate 2 to 3 a");
            Log.d(TAG, "about to upgrade migrate 2 to 3 b");
            MainActivity.profileMigrateLevel = 3;

            if (countMig > 0) {
                Log.d(TAG, "only one migration");
                return;
            }

//            migrateWork();
//            database.execSQL("ALTER TABLE password_item_test "
//                    + " ADD COLUMN actvy_date DATETIME");


            countMig += 1;
            Log.d(TAG, "migration 3 complete " + countMig);
        }
    };

    static final Migration MIGRATION_3_2 = new Migration(3, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            Log.d(TAG, "about to downgrade migrate 3 to 2");
//            database.execSQL("CREATE TABLE `password_item` (`id` INTEGER, "
//                    + "`name` TEXT, PRIMARY KEY(`id`))");
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

    private static void migrateWork() {

        Log.d(TAG, "about to do migrate work");
        // Queries the user dictionary and returns results
//        String[] projection = null;
//        String selectionClause = null;
//        String[] selectionArgs = {""};
//        String sortOrder = String.format("%s COLLATE NOCASE ASC, %s COLLATE NOCASE ASC",
//                AccountsContract.Columns.CORP_NAME_COL, AccountsContract.Columns.SEQUENCE_COL);
//        Loader<Cursor> cursor =
//                MainActivity.getContentResolver().query(
//                AccountsContract.CONTENT_URI,   // The content URI of the words table
//                projection,                        // The columns to return for each row
//                selectionClause,                   // Selection criteria
//                selectionArgs,                     // Selection criteria
//                sortOrder);                        // The sort order for the returned rows
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

//            Profile profile = new Profile(0, "Corp 1 Sample", "user A", "aaa@xxx.com", "");
//            profile.setPassportId(1);
//            profile.setNote("Sample data only to show data");
//            profile.setOpenLong(c1.getTimeInMillis());
//            profile.setActvyLong(c1.getTimeInMillis());
//            profileDao.insertProfile(profile);
//            profile = new Profile(9, "Corp 2 Sample", "user B", "bbb@xxx.com", "");
//            profile.setPassportId(2);
//            profile.setNote("Sample data only to show data");
//            profile.setOpenLong(c1.getTimeInMillis());
//            profile.setActvyLong(c1.getTimeInMillis());
//            profileDao.insertProfile(profile);
//            profile = new Profile(0, "Corp 3 Sample", "user C", "ccc@xxx.com", "");
//            profile.setPassportId(3);
//            profile.setNote("Sample data only to show data");
//            profile.setOpenLong(c1.getTimeInMillis());
//            profile.setActvyLong(c1.getTimeInMillis());
//            profileDao.insertProfile(profile);


//            suggestDao.insert(new Suggest("aaaaaa", 2));
//            suggestDao.insert(new Suggest("bbbbbb", 5));
//            suggestDao.insert(new Suggest("cccccc", 9));

            return null;
        }


    }

}
