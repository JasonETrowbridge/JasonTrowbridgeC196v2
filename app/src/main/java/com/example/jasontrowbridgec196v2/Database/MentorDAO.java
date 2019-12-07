package com.example.jasontrowbridgec196v2.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MentorDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMentor(MentorEntity mentorEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllMentors(List<MentorEntity> mentors);

    @Delete
    void deleteMentor(MentorEntity mentorEntity);

    @Query("SELECT * FROM mentors WHERE mentor_id= :mentorID")
    MentorEntity getMentorByID(int mentorID);

    @Query("SELECT * FROM mentors WHERE course_id = :courseID")
    LiveData<List<MentorEntity>> getMentorByCourse(int courseID);

    @Query("SELECT * FROM mentors ORDER BY mentor_name DESC")
    LiveData<List<MentorEntity>> getAllMentors();

    @Query("SELECT COUNT(*) FROM mentors")
    int getMentorCount();

    @Query("DELETE FROM mentors")
    int deleteAllMentors();

    @Query("SELECT COUNT(*) FROM mentors WHERE course_id = :courseID")
    int getMentorCountByCourse(int courseID);

    @Query("SELECT COUNT(*) FROM mentors WHERE course_id IS NOT NULL")
    int getMentorCountByAnyCourse();
}
