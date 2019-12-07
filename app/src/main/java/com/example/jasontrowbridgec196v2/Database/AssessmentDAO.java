package com.example.jasontrowbridgec196v2.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface AssessmentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssessment(AssessmentEntity assessmentEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllAssessments(List<AssessmentEntity> assessments);

    @Delete
    void deleteAssessment(AssessmentEntity assessmentEntity);

    @Query("SELECT * FROM assessments WHERE assessment_id = :assessmentID")
    AssessmentEntity getAssessmentByID(int assessmentID);

    @Query("SELECT * FROM assessments WHERE course_id = :courseID")
    LiveData<List<AssessmentEntity>> getAssessmentByCourse(int courseID);

    @Query("SELECT * FROM assessments ORDER BY assessment_date DESC")
    LiveData<List<AssessmentEntity>> getAllAssessments();

    @Query("DELETE FROM assessments")
    int deleteAllAssessments();

    @Query("SELECT COUNT(*) FROM assessments")
    int getAssessmentCount();

    @Query("SELECT COUNT(*) FROM assessments WHERE course_id = :courseID")
    int getAssessmentCountByCourse(int courseID);

    @Query("SELECT COUNT(*) FROM assessments WHERE course_id IS NOT NULL")
    int getAssessmentCountByAnyCourse();

}
