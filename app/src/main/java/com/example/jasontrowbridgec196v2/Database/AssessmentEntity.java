package com.example.jasontrowbridgec196v2.Database;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "assessments",
        foreignKeys = @ForeignKey(entity = CourseEntity.class,
                parentColumns = "course_id",
                childColumns = "course_id", onDelete = ForeignKey.CASCADE))
public class AssessmentEntity {

    @PrimaryKey(autoGenerate = true)
    private int assessment_id;

    private String assessment_name;

    private String assessment_date;

    private String assessment_type;

    private int course_id;

    public AssessmentEntity(String assessment_name, String assessment_date, String assessment_type, int course_id) {
        this.assessment_name = assessment_name;
        this.assessment_date = assessment_date;
        this.assessment_type = assessment_type;
        this.course_id = course_id;
    }

    //Setter for assessment_id since it is auto-generated and not in constructor
    public void setAssessment_id(int assessment_id) {
        this.assessment_id = assessment_id;
    }

    public void setAssessment_name(String assessment_name) {
        this.assessment_name = assessment_name;
    }

    public void setAssessment_date(String assessment_date) {
        this.assessment_date = assessment_date;
    }

    public void setAssessment_type(String assessment_type) {
        this.assessment_type = assessment_type;
    }

    public void setCourse_id(int course_id) {
        this.course_id = course_id;
    }

    //Getters

    public int getAssessment_id() {
        return assessment_id;
    }

    public String getAssessment_name() {
        return assessment_name;
    }

    public String getAssessment_date() {
        return assessment_date;
    }

    public String getAssessment_type() {
        return assessment_type;
    }

    public int getCourse_id() {
        return course_id;
    }
}
