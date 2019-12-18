package com.example.jasontrowbridgec196v2.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "terms")
public class TermEntity {

    @PrimaryKey(autoGenerate = true)
    private int term_id;
    private String term_title;
    private String term_start_date;
    private String term_end_date;


    //Constructor
    public TermEntity(String term_title, String term_start_date, String term_end_date) {
        this.term_title = term_title;
        this.term_start_date = term_start_date;
        this.term_end_date = term_end_date;
    }

    //Setter for term_id needed because not in constructor due to it being auto-generated
    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    //Getters

    public int getTerm_id() {
        return term_id;
    }

    public String getTerm_title() {
        return term_title;
    }

    public String getTerm_start_date() {
        return term_start_date;
    }

    public String getTerm_end_date() {
        return term_end_date;
    }

    @Override
    public String toString() {
        return getTerm_title();
    }
}
