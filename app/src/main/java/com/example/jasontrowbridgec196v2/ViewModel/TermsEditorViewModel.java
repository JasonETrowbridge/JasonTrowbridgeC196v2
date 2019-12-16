package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.Database.TermRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class TermsEditorViewModel extends AndroidViewModel {
    public MutableLiveData<TermEntity> mLiveTerm =
            new MutableLiveData<>();
    private TermRepository termRepository;
    private LiveData<List<TermEntity>> allTerms;
    private Executor executor = Executors.newSingleThreadExecutor();



    public TermsEditorViewModel(@NonNull Application application) {
            super(application);
            termRepository = new TermRepository(application);
            allTerms = termRepository.getAllTerms();
    }

    public void loadData(final int termID){
        executor.execute(new Runnable(){
            @Override
            public void run(){
                TermEntity term = termRepository.getTermByID(termID);
                mLiveTerm.postValue(term);
            }
        });
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
