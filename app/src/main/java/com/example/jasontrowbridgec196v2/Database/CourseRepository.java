package com.example.jasontrowbridgec196v2.Database;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class CourseRepository {
    private CourseDAO courseDAO;

    private static CourseRepository ourInstance;

    public LiveData<List<CourseEntity>> mCourses;

    public LiveData<List<CourseEntity>> tCourses;
    public Integer courseTerm;

    private Database database;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static CourseRepository getInstance(Context context){
        if(ourInstance == null) {
            ourInstance = new CourseRepository(context);
        }
        return ourInstance;
    }

    private CourseRepository(Context context){
        database = Database.getDbInstance(context);
        mCourses = getAllCourses();
    }

    //Insert methods
    public void insertCourse(final CourseEntity course){
        executor.execute(new Runnable() {
            @Override
            public void run(){
                database.courseDAO().insertCourse(course);
            }
        });
    }

    private LiveData<List<CourseEntity>> getAllCourses() {
        return database.courseDAO().getAll();
    }
    //Delete methods
    public void deleteCourse(final CourseEntity course){
        executor.execute(new Runnable() {
            @Override
            public void run(){
                int numChildren = database.assessmentDAO().getAssessmentCountByCourse(course.getCourse_id());
                numChildren += database.mentorDAO().getMentorCountByCourse(course.getCourse_id());
                if(numChildren == 0){
                    database.courseDAO().deleteCourse(course);
                }
            }
        });

    }

    public void deleteAllCourses(){
        executor.execute(new Runnable() {
            @Override
            public void run(){
                int numChildren = database.assessmentDAO().getAssessmentCountByAnyCourse();
                numChildren += database.mentorDAO().getMentorCountByAnyCourse();
                if (numChildren == 0){
                    database.courseDAO().deleteAllCourses();
                }
            }
        });

    }

    //Get methods
    public LiveData<List<CourseEntity>> getmCourses(){
        return mCourses;
    }

    public CourseEntity getCourseByID(int courseID){
        return database.courseDAO().getCourseByID(courseID);
    }

    public LiveData<List<CourseEntity>> getCoursesByTerm(int courseTerm){
        this.courseTerm = courseTerm;
        tCourses = database.courseDAO().getCoursesByTerm(courseTerm);
        return tCourses;
    }

}