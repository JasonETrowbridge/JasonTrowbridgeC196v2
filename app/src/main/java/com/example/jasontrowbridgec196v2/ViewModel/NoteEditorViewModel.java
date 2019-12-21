package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jasontrowbridgec196v2.Database.NoteEntity;
import com.example.jasontrowbridgec196v2.Database.NoteRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteEditorViewModel extends AndroidViewModel {
    private LiveData<List<NoteEntity>> allNotes;
    public MutableLiveData<NoteEntity> mLiveNote = new MutableLiveData<>();
    private NoteRepository noteRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public NoteEditorViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotes = noteRepository.getAllNotes();
    }

    public void loadData(final int noteID){
        executor.execute(new Runnable(){
            @Override
            public void run(){
                NoteEntity noteEntity = noteRepository.getNoteByID(noteID);
                mLiveNote.postValue(noteEntity);
            }
        });
    }

    public void saveData(String noteTitle, String noteText, int assessmentID){
        NoteEntity note = mLiveNote.getValue();

        if (note == null){
            if (TextUtils.isEmpty(noteTitle.trim())){
                return;
            }
            note = new NoteEntity(noteTitle.trim(), noteText.trim(), assessmentID);
        } else {
            note.setNote_title(noteTitle.trim());
            note.setNote_text(noteText.trim());
            note.setAssessment_id(assessmentID);
        }
        noteRepository.insertNote(note);
    }

    public void insertNote(NoteEntity noteEntity){
        noteRepository.insertNote(noteEntity);
    }

    public void deleteNote(NoteEntity noteEntity){
        noteRepository.deleteNote(mLiveNote.getValue());
    }

    public int lastID(){
        return allNotes.getValue().size();
    }
}
