package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.Database.TermRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class TermEditorViewModel extends AndroidViewModel {
    public MutableLiveData<TermEntity> mLiveTerm =
            new MutableLiveData<>();
    private TermRepository termRepository;
    private LiveData<List<TermEntity>> allTerms;
    private Executor executor = Executors.newSingleThreadExecutor();



    public TermEditorViewModel(@NonNull Application application) {
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
    public void saveData(String termTitle, String startDate, String endDate){
        TermEntity term = mLiveTerm.getValue();

        if (term == null){
            if (TextUtils.isEmpty(termTitle.trim())){
                return;
            }
            term = new TermEntity(termTitle.trim(), startDate.trim(), endDate.trim());
        } else {
            term.setTerm_title(termTitle.trim());
            term.setTerm_start_date(startDate.trim());
            term.setTerm_end_date(endDate.trim());

        }
        termRepository.insertTerm(term);
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
