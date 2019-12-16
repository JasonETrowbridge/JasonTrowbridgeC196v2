package com.example.jasontrowbridgec196v2.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CourseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertCourse(CourseEntity courseEntity);

    //Used for SampleData
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllCourses(List<CourseEntity> courses);

    @Delete
    void deleteCourse(CourseEntity courseEntity);

    @Query("SELECT * FROM courses WHERE course_id = :courseID")
    CourseEntity getCourseByID(int courseID);

    @Query("SELECT * FROM courses WHERE term_id = :termID")
    LiveData<List<CourseEntity>> getCoursesByTerm(int termID);

    @Query("SELECT * FROM courses ORDER BY course_start_date DESC")
    LiveData<List<CourseEntity>> getAll();

    @Query("DELETE FROM courses")
    int deleteAllCourses();

    @Query("SELECT COUNT(*) FROM courses")
    int getCourseCount();

    //Are these even necessary?
    @Query("SELECT COUNT(*) FROM courses WHERE term_id = :termID")
    int getCountByTerm(int termID);

    @Query("SELECT COUNT(*) FROM courses WHERE term_id IS NOT NULL")
    int getCountByAnyTerm();

}
