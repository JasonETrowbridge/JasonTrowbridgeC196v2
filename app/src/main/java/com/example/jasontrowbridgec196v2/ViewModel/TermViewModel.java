package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.Database.TermRepository;

import java.util.List;

public class TermViewModel extends AndroidViewModel {
    private TermRepository repository;
    private LiveData<List<TermEntity>> allTerms;

    public TermViewModel(@NonNull Application application) {
        super(application);
        repository = new TermRepository(application);
        allTerms = repository.getAllTerms();
    }

    public void insertTerm(TermEntity term){
        repository.insertTerm(term);
    }

    public void deleteTerm(TermEntity term){
        repository.deleteTerm(term);
    }

    public void deleteAllTerms(){
        repository.deleteAllTerms();
    }

    public LiveData<List<TermEntity>> getAllTerms() {
        return allTerms;
    }

    public TermEntity getTermByID(int termID){
        return repository.getTermByID(termID);
    }

}
