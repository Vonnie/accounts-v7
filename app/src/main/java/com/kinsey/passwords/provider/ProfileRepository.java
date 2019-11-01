package com.kinsey.passwords.provider;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.kinsey.passwords.MainActivity;
import com.kinsey.passwords.items.Profile;

import java.util.List;


public class ProfileRepository {
    private ProfileDao profileDao;
    private LiveData<List<Profile>> allProfiles;
    private MutableLiveData<Long> dbInsertId = new MediatorLiveData<>();
    public String dbMsg;


    public ProfileRepository(Application application) {
        ProfileDatabase database = ProfileDatabase.getInstance(application);
        profileDao = database.profileDao();
        allProfiles = profileDao.getAllProfiles();
        dbMsg = database.getOpenHelper().getDatabaseName();
    }

    public void insertProfile(Profile profile, Task myInterface) {
        new InsertProfileAsyncTask(profileDao, myInterface).execute(profile);
    }

    public void insertAll(List<Profile> profiles) {
        new InsertProfilesAsyncTask(profileDao, profiles).execute();
    }

    public void update(Profile profile) {
        new UpdateProfileAsyncTask(profileDao).execute(profile);
    }

    public void delete(Profile profile) {
        new DeleteProfileAsyncTask(profileDao).execute(profile);
    }

    public void deleteAllProfiles() {
        new DeleteAllProfilesAsyncTask(profileDao).execute();
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
        return profileDao.getProfileById(id);
    }

    public LiveData<List<Profile>> getAllProfilesByCorpName() {
        allProfiles = profileDao.getAllProfilesByCorpName();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesByOpenDate() {
        allProfiles = profileDao.getAllProfilesByOpenDate();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesByPassportId() {
        allProfiles = profileDao.getAllProfilesByPassportId();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesCustomSort() {
        allProfiles = profileDao.getAllProfilesCustomSort();
        return allProfiles;
    }

    public LiveData<Profile> getMaxSequence() {
        return profileDao.getMaxSequence();
    }

    public LiveData<List<Profile>> searchCorpNameProfiles(String query) {
        allProfiles = profileDao.searchCorpNameProfiles(query);
        return allProfiles;
    }

    private static class InsertProfileAsyncTask extends AsyncTask<Profile, Void, Profile> {
        private ProfileDao mDao;
        private Task mTask;

        private InsertProfileAsyncTask(ProfileDao profileDao, Task task) {
            this.mDao = profileDao;
            this.mTask = task;
        }

        @Override
        protected Profile doInBackground(Profile... profile) {
            Profile item = profile[0];
            long id = this.mDao.insertProfile(item);
            int intId = (int)id;
            item.setId(intId);
            item.setPassportId(intId);
            return item;
//            profileDao.update(item);
//
//            return item;
//            return profileDao.insert(profile);
//            return mDao.insertProfile(profile[0]);
        }

        @Override
        protected void onPostExecute(Profile profile) {
            super.onPostExecute(profile);
            mTask.processInsert(profile);

//            return profile;
        }


    }

//    https://stackoverflow.com/questions/56950531/how-to-make-room-database-insert-method-return-int-through-mvvm-architecture

    private static class InsertProfilesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProfileDao profileDao;
        private List<Profile> profiles;

        private InsertProfilesAsyncTask(ProfileDao profileDao, List<Profile> profiles) {

            this.profileDao = profileDao;
            this.profiles = profiles;
        }

        @Override
        protected Void doInBackground(Void... aVoid) {

            for (Profile item : this.profiles) {
                profileDao.insert(item);
            }
            return null;
        }
    }

    private static class UpdateProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private ProfileDao profileDao;

        private UpdateProfileAsyncTask(ProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(Profile... profiles) {
            profileDao.update(profiles[0]);
            return null;
        }
    }

    private static class DeleteProfileAsyncTask extends AsyncTask<Profile, Void, Void> {
        private ProfileDao profileDao;

        private DeleteProfileAsyncTask(ProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(Profile... profiles) {
            profileDao.delete(profiles[0]);
            return null;
        }
    }

    private static class DeleteAllProfilesAsyncTask extends AsyncTask<Void, Void, Void> {
        private ProfileDao profileDao;

        private DeleteAllProfilesAsyncTask(ProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            profileDao.deleteAllProfiles();
            return null;
        }
    }

}
