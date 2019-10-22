package com.kinsey.passwords.provider;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.kinsey.passwords.items.Profile;

import java.util.List;


public class ProfileRepository {
    private ProfileDao profileDao;
    private LiveData<List<Profile>> allProfiles;


    public ProfileRepository(Application application) {
        ProfileDatabase database = ProfileDatabase.getInstance(application);
        profileDao = database.profileDao();
        allProfiles = profileDao.getAllProfiles();
    }

    public void insert(Profile profile, Task myInterface) {
        new InsertProfileAsyncTask(profileDao, myInterface).execute(profile);
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

    public void insertMulti(List<Profile> profiles) {
        new InsertProfilesAsyncTask(profileDao).execute(profiles);
    }

    public LiveData<List<Profile>> getAllProfiles() {
        allProfiles = profileDao.getAllProfiles();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesById() {
        allProfiles = profileDao.getAllProfilesById();
        return allProfiles;
    }

    public LiveData<List<Profile>> getAllProfilesByOpenDate() {
        allProfiles = profileDao.getAllProfilesByOpenDate();
        return allProfiles;
    }

    public LiveData<List<Profile>> searchCorpNameProfiles(String query) {
        allProfiles = profileDao.searchCorpNameProfiles(query);
        return allProfiles;
    }

    private static class InsertProfileAsyncTask extends AsyncTask<Profile, Void, Profile> {
        private ProfileDao dao;
        private Task taskId;

        private InsertProfileAsyncTask(ProfileDao profileDao, Task taskId) {
            this.dao = profileDao;
            this.taskId = taskId;
        }

        @Override
        protected Profile doInBackground(Profile... profile) {
//            Profile item = profile[0];
//            long id = profileDao.insert(item);
//            int intId = (int)id;
//            item.setId(intId);
//            item.setPassportId(intId);
//            profileDao.update(item);
//
//            return item;
            return profileDao.insert(profile);
        }

        @Override
        protected void onPostExecute(Profile profile) {
            super.onPostExecute(profile);
            taskId.processInsert(profile.id);

//            return profile;
        }


        public void insertMyProfile(Profile profile, Task myInterface) {
            new ProfileRepository.InsertProfileAsyncTask(profileDao, myInterface).execute(profile);
        }


    }

//    https://stackoverflow.com/questions/56950531/how-to-make-room-database-insert-method-return-int-through-mvvm-architecture

    private static class InsertProfilesAsyncTask extends AsyncTask<List<Profile>, Void, Void> {
        private ProfileDao profileDao;

        private InsertProfilesAsyncTask(ProfileDao profileDao) {
            this.profileDao = profileDao;
        }

        @Override
        protected Void doInBackground(List<Profile>... profiles) {

            for (Profile item : profiles[0]) {
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
