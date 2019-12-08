package com.example.jasontrowbridgec196v2.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;


public class TermRepository {

    private TermDAO termDAO;
    private LiveData<List<TermEntity>> allTerms;


    public TermRepository(Application application){
        Database database = Database.getDbInstance(application);
        termDAO = database.termDAO();
        allTerms = termDAO.getAllTerms();

    }

    //Insert methods
    public void insertTerm(TermEntity term){
        new InsertTermAsyncTask(termDAO).execute(term);

    }

    //Delete methods
    public void deleteTerm(TermEntity term){
        new DeleteTermAsyncTask(termDAO).execute(term);

    }

    public void deleteAllTerms(){
        new DeleteAllTermsAsyncTask(termDAO).execute();

    }


    //Get methods
    public LiveData<List<TermEntity>> getAllTerms(){
        return allTerms;
    }

    public TermEntity getTermByID(int termID){
        return termDAO.getTermByID(termID);
    }

    private static class InsertTermAsyncTask extends AsyncTask<TermEntity, Void, Void>{
        private TermDAO termDAO;

        private InsertTermAsyncTask(TermDAO termDAO){
            this.termDAO = termDAO;
        }

        @Override
        protected Void doInBackground(TermEntity... termEntities) {
            termDAO.insertTerm(termEntities[0]);
            return null;
        }
    }

    private static class DeleteTermAsyncTask extends AsyncTask<TermEntity, Void, Void>{
        private TermDAO termDAO;

        private DeleteTermAsyncTask(TermDAO termDAO){
            this.termDAO = termDAO;
        }

        @Override
        protected Void doInBackground(TermEntity... termEntities) {
            termDAO.deleteTerm(termEntities[0]);
            return null;
        }
    }

    private static class DeleteAllTermsAsyncTask extends AsyncTask<Void, Void, Void>{
        private TermDAO termDAO;

        private DeleteAllTermsAsyncTask(TermDAO termDAO){
            this.termDAO = termDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDAO.deleteAllTerms();
            return null;
        }
    }
}
