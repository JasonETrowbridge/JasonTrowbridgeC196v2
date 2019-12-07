package com.example.jasontrowbridgec196v2.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AssessmentRepository {
    private AssessmentDAO assessmentDAO;

    private LiveData<List<AssessmentEntity>> allAssessments;

    public AssessmentRepository(Application application){
        Database database = Database.getDbInstance(application);
        assessmentDAO = database.assessmentDAO();
        allAssessments = assessmentDAO.getAllAssessments();

    }

    //Insert methods
    public void insertAssessment(AssessmentEntity assessment){
        new InsertAssessmentAsyncTask(assessmentDAO).execute(assessment);

    }

    //Delete methods
    public void deleteAssessment(AssessmentEntity assessment){
        new DeleteAssessmentAsyncTask(assessmentDAO).execute(assessment);

    }

    public void deleteAllAssessments(){
        new DeleteAllAssessmentsAsyncTask(assessmentDAO).execute();

    }

    //Get methods
    public LiveData<List<AssessmentEntity>> getAllAssessments(){
        return allAssessments;
    }

    public AssessmentEntity getAssessmentByID(int assessmentID){
        return assessmentDAO.getAssessmentByID(assessmentID);
    }

    public LiveData<List<AssessmentEntity>> getAssessmentByCourse(int assessmentCourse){
        return assessmentDAO.getAssessmentByCourse(assessmentCourse);
    }

    private static class InsertAssessmentAsyncTask extends AsyncTask<AssessmentEntity, Void, Void> {
        private AssessmentDAO assessmentDAO;

        private InsertAssessmentAsyncTask(AssessmentDAO assessmentDAO){
            this.assessmentDAO = assessmentDAO;
        }

        @Override
        protected Void doInBackground(AssessmentEntity... assessmentEntities) {
            assessmentDAO.insertAssessment(assessmentEntities[0]);
            return null;
        }
    }

    private static class DeleteAssessmentAsyncTask extends AsyncTask<AssessmentEntity, Void, Void>{
        private AssessmentDAO assessmentDAO;

        private DeleteAssessmentAsyncTask(AssessmentDAO assessmentDAO){
            this.assessmentDAO = assessmentDAO;
        }

        @Override
        protected Void doInBackground(AssessmentEntity... assessmentEntities) {
            assessmentDAO.deleteAssessment(assessmentEntities[0]);
            return null;
        }
    }

    private static class DeleteAllAssessmentsAsyncTask extends AsyncTask<Void, Void, Void>{
        private AssessmentDAO assessmentDAO;

        private DeleteAllAssessmentsAsyncTask(AssessmentDAO assessmentDAO){
            this.assessmentDAO = assessmentDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            assessmentDAO.deleteAllAssessments();
            return null;
        }
    }
}
