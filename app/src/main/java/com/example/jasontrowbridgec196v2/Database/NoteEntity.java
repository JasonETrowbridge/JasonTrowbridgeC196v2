package com.example.jasontrowbridgec196v2.Database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "notes",
        foreignKeys = @ForeignKey(entity = AssessmentEntity.class,
                parentColumns = "assessment_id",
                childColumns = "assessment_id", onDelete = ForeignKey.CASCADE))
public class NoteEntity {

    @PrimaryKey(autoGenerate = true)
    private int note_id;

    private String note_title;

    private String note_text;

    private int assessment_id;

    public NoteEntity(String note_title, String note_text, int assessment_id) {
        this.note_title = note_title;
        this.note_text = note_text;
        this.assessment_id = assessment_id;
    }

    //Setter for note_id since its auto-generated and not in constructor
    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public void setNote_title(String note_title) {
        this.note_title = note_title;
    }

    public void setNote_text(String note_text) {
        this.note_text = note_text;
    }

    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    //Getters
    public int getNote_id() {
        return note_id;
    }

    public String getNote_title() {
        return note_title;
    }

    public String getNote_text() {
        return note_text;
    }

    public int getAssessment_id() {
        return assessment_id;
    }
}
