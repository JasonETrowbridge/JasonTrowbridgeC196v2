package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jasontrowbridgec196v2.Database.AssessmentEntity;
import com.example.jasontrowbridgec196v2.Database.AssessmentRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AssessmentEditorViewModel extends AndroidViewModel {
    private LiveData<List<AssessmentEntity>> allAssessments;
    public MutableLiveData<AssessmentEntity> mLiveAssessment = new MutableLiveData<>();
    private AssessmentRepository assessmentRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public AssessmentEditorViewModel(@NonNull Application application) {
        super(application);
        assessmentRepository = new AssessmentRepository(application);
        allAssessments = assessmentRepository.getAllAssessments();
    }

    public void loadData(final int assessmentID){
        executor.execute(new Runnable(){
            @Override
            public void run(){
                AssessmentEntity assessmentEntity = assessmentRepository.getAssessmentByID(assessmentID);
                mLiveAssessment.postValue(assessmentEntity);
            }
        });
    }

    public void saveData(String assessmentName, String assessmentDate, String assessmentType, int courseID){
        AssessmentEntity assessment = mLiveAssessment.getValue();

        if (assessment == null){
            if (TextUtils.isEmpty(assessmentName.trim())){
                return;
            }
            assessment = new AssessmentEntity(assessmentName.trim(), assessmentDate.trim(), assessmentType.trim(), courseID);
        } else {
            assessment.setAssessment_name(assessmentName.trim());
            assessment.setAssessment_date(assessmentDate.trim());
            assessment.setAssessment_type(assessmentType.trim());
            assessment.setCourse_id(courseID);
        }
        assessmentRepository.insertAssessment(assessment);
    }

    public void insertAssessment(AssessmentEntity assessmentEntity){
        assessmentRepository.insertAssessment(assessmentEntity);
    }

    public void deleteAssessment(AssessmentEntity assessmentEntity){
        assessmentRepository.deleteAssessment(mLiveAssessment.getValue());
    }

    public int lastID(){
        return allAssessments.getValue().size();
    }
}

