package com.kinsey.passwords.provider;

import static com.kinsey.passwords.provider.ProfileDatabase.TAG;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MediatorLiveData;
//import androidx.lifecycle.MutableLiveData;

import com.kinsey.passwords.items.Profile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class ProfileRepository {
    private ProfileDao mProfileDao;
    private LiveData<List<Profile>> allProfiles;
//    private MutableLiveData<Long> dbInsertId = new MediatorLiveData<>();
//    public String dbMsg;


    public ProfileRepository(Application application) {
//        ProfileDatabase database = ProfileDatabase.getInstance(application);
        ProfileDatabase database = ProfileDatabase.getDatabase(application);
        mProfileDao = database.profileDao();
        allProfiles = mProfileDao.getAllProfilesByCorpName();
//        dbMsg = database.getOpenHelper().getDatabaseName();
    }

//    public void insertProfile(Profile profile, Task myInterface) {
    public void insertProfile(Profile profile) {
//        new InsertProfileAsyncTask(profileDao, myInterface).execute(profile);
//        Long profileId = 0L;
        ProfileDatabase.databaseWriteExecutor.execute(() -> {
//            profileId = mProfileDao.insert(profile);
//            return profileId;
            mProfileDao.addProfile(profile);
//            Log.d(TAG, "insertProfile " + profile.getCorpName() + ":" + profile.getPassportId() + ":" + profile.getId());
        });
//        return profileId;
    }

    public void insertAll(List<Profile> profiles) {
//        new InsertProfilesAsyncTask(profileDao, profiles).execute();
        ProfileDatabase.databaseWriteExecutor.execute(() -> {
            mProfileDao.insertAll(profiles);
        });
    }

    public void update(Profile profile) {
//        new UpdateProfileAsyncTask(profileDao).execute(profile);
        Log.d(TAG, "update Profile " + profile);
        ProfileDatabase.databaseWriteExecutor.execute(() -> {
            mProfileDao.update(profile);
        });
    }

    public void delete(Profile profile) {
//        new DeleteProfileAsyncTask(profileDao).execute(profile);
        ProfileDatabase.databaseWriteExecutor.execute(() -> {
            mProfileDao.delete(profile);
        });
    }


    public void deleteAllProfiles() {
//        new DeleteAllProfilesAsyncTask(profileDao).execute();
        ProfileDatabase.databaseWriteExecutor.execute(() -> {
            mProfileDao.deleteAllProfiles();
        });
    }

//    public LiveData<List<Profile>> getAllProfiles(int listsortOrder) {
//        switch (listsortOrder) {
//            case MainActivity.LISTSORT_CORP_NAME:
//                allProfiles = profileDao.getAllProfilesByCorpName();
//                break;
//            case MainActivity.LISTSORT_PASSPORT_ID:
//                allProfiles = profileDao.getAllProfilesByPassportId();
//                break;
//            case MainActivity.LISTSORT_OPEN_DATE:
//                allProfiles = profileDao.getAllProfilesByOpenDate();
//                break;
//            case MainActivity.LISTSORT_CUSTOM_SORT:
//                allProfiles = profileDao.getAllProfilesCustomSort();
//                break;
//            default:
//                allProfiles = profileDao.getAllProfilesByCorpName();
//                break;
//        }
//
//        return allProfiles;
//    }

    public LiveData<Profile> getProfileById(int id) {
        return mProfileDao.getProfileById(id);
    }

    public LiveData<List<Profile>> getAllProfilesByCorpName() {
//        allProfiles = mProfileDao.getAllProfilesByCorpName();
        return allProfiles;

    }

    public LiveData<List<Profile>> getAllProfilesByOpenDate() {
        allProfiles = mProfileDao.getAllProfilesByOpenDate();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesByPassportId() {
        allProfiles = mProfileDao.getAllProfilesByPassportId();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesCustomSort() {
        allProfiles = mProfileDao.getAllProfilesCustomSort();
        return allProfiles;
    }

    public void deleteProfileById(int id) {
        mProfileDao.deleteProfileId(id);
    }

    public LiveData<Profile> getMaxSequence() {
        return mProfileDao.getMaxSequence();
    }

    public LiveData<List<Profile>> searchCorpNameProfiles(String query) {
        allProfiles = mProfileDao.searchCorpNameProfiles(query);
        return allProfiles;
    }

    public LiveData<Integer> getCount() {
        return mProfileDao.getCount();
    }

//    private void sortByCorpName() {
//
//
//        Collections.sort(allProfiles, new Comparator() {
//            @Override
//            public int compare(Object o1, Object o2) {
//                Profile p1 = (Profile) o1;
//                Profile p2 = (Profile) o2;
//                return p1.getCorpName().compareToIgnoreCase(p2.getCorpName());
//            }
//        });
//    }

//    private static class InsertProfileAsyncTask extends AsyncTask<Profile, Void, Profile> {
//        private static final String TAG = "InsertProfileAsyncTask";
//        private ProfileDao mDao;
//        private Task mTask;
//
//        private InsertProfileAsyncTask(ProfileDao profileDao, Task task) {
//            this.mDao = profileDao;
//            this.mTask = task;
//        }
//
//        @Override
//        protected Profile doInBackground(Profile... profile) {
//            Profile item = profile[0];
//            long id = this.mDao.insertProfile(item);
//            int intId = (int)id;
//            item.setId(intId);
//            item.setPassportId(intId);
//            Log.d(TAG, "doInBackground: newId updated " + intId);
////            dbInsertId.postValue();
//            return item;
////            profileDao.update(item);
////
////            return item;
////            return profileDao.insert(profile);
////            return mDao.insertProfile(profile[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Profile profile) {
//            super.onPostExecute(profile);
//            mTask.processInsert(profile);
//
////            return profile;
//        }
//
//
//    }
//
////    https://stackoverflow.com/questions/56950531/how-to-make-room-database-insert-method-return-int-through-mvvm-architecture
//
//    private static class InsertProfilesAsyncTask extends AsyncTask<Void, Void, Void> {
//        private ProfileDao profileDao;
//        private List<Profile> profiles;
//
//        private InsertProfilesAsyncTask(ProfileDao profileDao, List<Profile> profiles) {
//
//            this.profileDao = profileDao;
//            this.profiles = profiles;
//        }
//
//        @Override
//        protected Void doInBackground(Void... aVoid) {
//
//            for (Profile item : this.profiles) {
//                profileDao.insert(item);
//            }
//            return null;
//        }
//    }
//
//    private static class UpdateProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
//        private ProfileDao profileDao;
//
//        private UpdateProfileAsyncTask(ProfileDao profileDao) {
//            this.profileDao = profileDao;
//        }
//
//        @Override
//        protected Void doInBackground(Profile... profiles) {
//            profileDao.update(profiles[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
//        private ProfileDao profileDao;
//
//        private DeleteProfileAsyncTask(ProfileDao profileDao) {
//            this.profileDao = profileDao;
//        }
//
//        @Override
//        protected Void doInBackground(Profile... profiles) {
//            profileDao.delete(profiles[0]);
//            return null;
//        }
//    }
//
//    private static class DeleteAllProfilesAsyncTask extends AsyncTask<Void, Void, Void> {
//        private ProfileDao profileDao;
//
//        private DeleteAllProfilesAsyncTask(ProfileDao profileDao) {
//            this.profileDao = profileDao;
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            profileDao.deleteAllProfiles();
//            return null;
//        }
//    }

}
