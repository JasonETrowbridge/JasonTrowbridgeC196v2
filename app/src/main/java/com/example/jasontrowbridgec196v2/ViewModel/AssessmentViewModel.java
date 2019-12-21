package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jasontrowbridgec196v2.Database.AssessmentEntity;
import com.example.jasontrowbridgec196v2.Database.AssessmentRepository;

import java.util.List;

public class AssessmentViewModel extends AndroidViewModel {
    private AssessmentRepository repository;
    public LiveData<List<AssessmentEntity>> allAssessments;

    public AssessmentViewModel(@NonNull Application application) {
        super(application);
        repository = new AssessmentRepository(application);
        allAssessments = repository.getAllAssessments();
    }

    public void insertAssessment(AssessmentEntity assessment){
        repository.insertAssessment(assessment);
    }

    public void deleteAssessment(AssessmentEntity assessment){
        repository.deleteAssessment(assessment);
    }

    public void deleteAllAssessments(){
        repository.deleteAllAssessments();
    }

    public LiveData<List<AssessmentEntity>> getAllAssessments() {
        return allAssessments;
    }

    public AssessmentEntity getAssessmentByID(int assessmentID){
        return repository.getAssessmentByID(assessmentID);
    }
    public LiveData<List<AssessmentEntity>> getAssessmentByCourse(int currentCourseID){
        return repository.getAssessmentByCourse(currentCourseID);
    }
    public void setCurrentCourse(int currentCourse){
        allAssessments = repository.getAssessmentByCourse(currentCourse);
    }
}
