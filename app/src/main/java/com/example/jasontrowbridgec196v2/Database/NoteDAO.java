package com.example.jasontrowbridgec196v2.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(NoteEntity noteEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllNotes(List<NoteEntity> notes);

    @Delete
    void deleteNote(NoteEntity noteEntity);

    @Query("SELECT * FROM notes WHERE note_id = :noteID")
    NoteEntity getNoteByID(int noteID);

    @Query("SELECT * FROM notes ORDER BY note_title DESC")
    LiveData<List<NoteEntity>> getAllNotes();

    @Query("SELECT * FROM notes WHERE assessment_id = :assessmentID")
    LiveData<List<NoteEntity>> getNoteByAssessment(int assessmentID);

    @Query("DELETE FROM notes")
    int deleteAllNotes();

    @Query("SELECT COUNT(*) FROM notes WHERE assessment_id = :assessmentID")
    int getNoteCountByAssessment(int assessmentID);

    @Query("SELECT COUNT(*) FROM notes")
    int getNoteCount();

    @Query("SELECT COUNT(*) FROM notes WHERE assessment_id IS NOT NULL")
    int getNoteCountByAnyAssessment();

}
