package com.example.jasontrowbridgec196v2.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MentorRepository {
    private MentorDAO mentorDAO;

    private LiveData<List<MentorEntity>> allMentors;

    public MentorRepository(Application application){
        Database database = Database.getDbInstance(application);
        mentorDAO = database.mentorDAO();
        allMentors = mentorDAO.getAllMentors();

    }

    //Insert methods
    public void insertMentor(MentorEntity mentor){
        new InsertMentorAsyncTask(mentorDAO).execute(mentor);

    }

    //Delete methods
    public void deleteMentor(MentorEntity mentor){
        new DeleteMentorAsyncTask(mentorDAO).execute(mentor);

    }

    public void deleteAllMentors(){
        new DeleteAllMentorsAsyncTask(mentorDAO).execute();

    }

    //Get methods
    public LiveData<List<MentorEntity>> getAllMentors(){
        return allMentors;
    }

    public MentorEntity getMentorByID(int mentorID){
        return mentorDAO.getMentorByID(mentorID);
    }

    public LiveData<List<MentorEntity>> getMentorByCourse(int mentorCourse){
        return mentorDAO.getMentorByCourse(mentorCourse);
    }

    private static class InsertMentorAsyncTask extends AsyncTask<MentorEntity, Void, Void> {
        private MentorDAO mentorDAO;

        private InsertMentorAsyncTask(MentorDAO mentorDAO){
            this.mentorDAO = mentorDAO;
        }

        @Override
        protected Void doInBackground(MentorEntity... mentorEntities) {
            mentorDAO.insertMentor(mentorEntities[0]);
            return null;
        }
    }

    private static class DeleteMentorAsyncTask extends AsyncTask<MentorEntity, Void, Void>{
        private MentorDAO mentorDAO;

        private DeleteMentorAsyncTask(MentorDAO mentorDAO){
            this.mentorDAO = mentorDAO;
        }

        @Override
        protected Void doInBackground(MentorEntity... mentorEntities) {
            mentorDAO.deleteMentor(mentorEntities[0]);
            return null;
        }
    }

    private static class DeleteAllMentorsAsyncTask extends AsyncTask<Void, Void, Void>{
        private MentorDAO mentorDAO;

        private DeleteAllMentorsAsyncTask(MentorDAO mentorDAO){
            this.mentorDAO = mentorDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mentorDAO.deleteAllMentors();
            return null;
        }
    }
}
