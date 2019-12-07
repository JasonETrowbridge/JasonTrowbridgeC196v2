package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.Database.TermRepository;

import java.util.List;


public class TermsEditorViewModel extends AndroidViewModel {
    private TermRepository termRepository;
    private LiveData<List<TermEntity>> allTerms;



    public TermsEditorViewModel(@NonNull Application application) {
            super(application);
            termRepository = new TermRepository(application);
            allTerms = termRepository.getAllTerms();
    }

    public LiveData<List<TermEntity>> getAllTerms(){
        return allTerms;
    }

    public void insertTerm(TermEntity termEntity){
        termRepository.insertTerm(termEntity);
    }

    public void deleteTerm(TermEntity termEntity){
        termRepository.deleteTerm(termEntity);
    }

    public int lastID(){
        return allTerms.getValue().size();
    }
}
