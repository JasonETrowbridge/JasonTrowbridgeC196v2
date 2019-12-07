package com.example.jasontrowbridgec196v2.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDAO noteDAO;

    private LiveData<List<NoteEntity>> allNotes;

    public NoteRepository(Application application){
        Database database = Database.getDbInstance(application);
        noteDAO = database.noteDAO();
        allNotes = noteDAO.getAllNotes();

    }

    //Insert methods
    public void insertNote(NoteEntity note){
        new InsertNoteAsyncTask(noteDAO).execute(note);
    }

    //Delete methods
    public void deleteNote(NoteEntity note){
        new DeleteNoteAsyncTask(noteDAO).execute(note);
    }

    public void deleteAllNotes(){
        new DeleteAllNotesAsyncTask(noteDAO).execute();
    }

    //Get methods
    public LiveData<List<NoteEntity>> getAllNotes(){
        return allNotes;
    }

    public NoteEntity getNoteByID(int noteID){
        return noteDAO.getNoteByID(noteID);
    }

    public LiveData<List<NoteEntity>> getNotesByAssessment(int noteAssessment){
        return noteDAO.getNoteByAssessment(noteAssessment);
    }

    private static class InsertNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void> {
        private NoteDAO noteDAO;

        private InsertNoteAsyncTask(NoteDAO noteDAO){
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDAO.insertNote(noteEntities[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<NoteEntity, Void, Void>{
        private NoteDAO noteDAO;

        private DeleteNoteAsyncTask(NoteDAO noteDAO){
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(NoteEntity... noteEntities) {
            noteDAO.deleteNote(noteEntities[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void>{
        private NoteDAO noteDAO;

        private DeleteAllNotesAsyncTask(NoteDAO noteDAO){
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.deleteAllNotes();
            return null;
        }
    }
}
