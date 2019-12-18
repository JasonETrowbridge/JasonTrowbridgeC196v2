package com.example.jasontrowbridgec196v2.Database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(tableName = "courses",
        foreignKeys = @ForeignKey(entity = TermEntity.class,
                parentColumns = "term_id",
                childColumns = "term_id", onDelete = ForeignKey.CASCADE))
public class CourseEntity {

    @PrimaryKey(autoGenerate = true)
    private int course_id;
    private String course_title;
    private String course_start_date;
    private String course_end_date;
    private String course_status;
    private int term_id;

    //Constructor
    public CourseEntity(String course_title, String course_start_date, String course_end_date, String course_status, int term_id) {
        this.course_title = course_title;
        this.course_start_date = course_start_date;
        this.course_end_date = course_end_date;
        this.course_status = course_status;
        this.term_id = term_id;
    }

    public void setCourse_title(String course_title) {
        this.course_title = course_title;
    }

    public void setCourse_start_date(String course_start_date) {
        this.course_start_date = course_start_date;
    }

    public void setCourse_end_date(String course_end_date) {
        this.course_end_date = course_end_date;
    }

    public void setCourse_status(String course_status) {
        this.course_status = course_status;
    }

    public void setTerm_id(int term_id) {
        this.term_id = term_id;
    }

    //Setter for course_id since it is auto-generated and not in the constructor
    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }



    //Getters

    public int getCourse_id() {
        return course_id;
    }

    public String getCourse_title() {
        return course_title;
    }

    public String getCourse_start_date() {
        return course_start_date;
    }

    public String getCourse_end_date() {
        return course_end_date;
    }

    public String getCourse_status() {
        return course_status;
    }

    public int getTerm_id() {
        return term_id;
    }


    @Override
    public String toString() {
        return course_title;
    }
}
