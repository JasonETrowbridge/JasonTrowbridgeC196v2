package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jasontrowbridgec196v2.Database.MentorEntity;
import com.example.jasontrowbridgec196v2.Database.MentorRepository;

import java.util.List;

public class MentorViewModel extends AndroidViewModel {
    private MentorRepository repository;
    public LiveData<List<MentorEntity>> allMentors;

    public MentorViewModel(@NonNull Application application) {
        super(application);
        repository = new MentorRepository(application);
        allMentors = repository.getAllMentors();
    }

    public void insertMentor(MentorEntity mentor){
        repository.insertMentor(mentor);
    }

    public void deleteMentor(MentorEntity mentor){
        repository.deleteMentor(mentor);
    }

    public void deleteAllMentors(){
        repository.deleteAllMentors();
    }

    public LiveData<List<MentorEntity>> getAllMentors() {
        return allMentors;
    }

    public MentorEntity getMentorByID(int mentorID){
        return repository.getMentorByID(mentorID);
    }
    public LiveData<List<MentorEntity>> getMentorByCourse(int currentCourseID){
        return repository.getMentorByCourse(currentCourseID);
    }
    public void setCurrentCourse(int currentCourse){
        allMentors = repository.getMentorByCourse(currentCourse);
    }
}
