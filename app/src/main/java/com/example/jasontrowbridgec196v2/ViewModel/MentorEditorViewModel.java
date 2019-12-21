package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jasontrowbridgec196v2.Database.MentorEntity;
import com.example.jasontrowbridgec196v2.Database.MentorRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MentorEditorViewModel extends AndroidViewModel {
    private LiveData<List<MentorEntity>> allMentors;
    public MutableLiveData<MentorEntity> mLiveMentor = new MutableLiveData<>();
    private MentorRepository mentorRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public MentorEditorViewModel(@NonNull Application application) {
        super(application);
        mentorRepository = new MentorRepository(application);
        allMentors = mentorRepository.getAllMentors();
    }

    public void loadData(final int mentorID){
        executor.execute(new Runnable(){
            @Override
            public void run(){
                MentorEntity mentorEntity = mentorRepository.getMentorByID(mentorID);
                mLiveMentor.postValue(mentorEntity);
            }
        });
    }

    public void saveData(String mentorName, String mentorPhone, String mentorEmail, int courseID){
        MentorEntity mentor = mLiveMentor.getValue();

        if (mentor == null){
            if (TextUtils.isEmpty(mentorName.trim())){
                return;
            }
            mentor = new MentorEntity(mentorName.trim(), mentorPhone.trim(), mentorEmail.trim(), courseID);
        } else {
            mentor.setMentor_name(mentorName.trim());
            mentor.setMentor_phone(mentorPhone.trim());
            mentor.setMentor_email(mentorEmail.trim());
            mentor.setCourse_id(courseID);
        }
        mentorRepository.insertMentor(mentor);
    }

    public void insertMentor(MentorEntity mentorEntity){
        mentorRepository.insertMentor(mentorEntity);
    }

    public void deleteMentor(MentorEntity mentorEntity){
        mentorRepository.deleteMentor(mLiveMentor.getValue());
    }

    public int lastID(){
        return allMentors.getValue().size();
    }
}
