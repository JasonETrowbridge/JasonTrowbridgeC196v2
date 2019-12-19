package com.example.jasontrowbridgec196v2.ViewModel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.Database.CourseRepository;
import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.Database.TermRepository;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CourseEditorViewModel extends AndroidViewModel {
    private LiveData<List<CourseEntity>> allCourses;
    public MutableLiveData<CourseEntity> mLiveCourse = new MutableLiveData<>();
    private CourseRepository courseRepository;
    private Executor executor = Executors.newSingleThreadExecutor();


    public CourseEditorViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        allCourses = courseRepository.getAllCourses();
    }



    public void loadData(final int courseID){
        executor.execute(new Runnable(){
            @Override
            public void run(){
                CourseEntity courseEntity = courseRepository.getCourseByID(courseID);
                mLiveCourse.postValue(courseEntity);
            }
        });
    }

    public void saveData(String courseTitle, String startDate, String endDate, String status, int termID){
        CourseEntity course = mLiveCourse.getValue();

        if (course == null){
            if (TextUtils.isEmpty(courseTitle.trim())){
                return;
            }
            course = new CourseEntity(courseTitle.trim(), startDate.trim(), endDate.trim(), status.trim(), termID);
        } else {
            course.setCourse_title(courseTitle.trim());
            course.setCourse_start_date(startDate.trim());
            course.setCourse_end_date(endDate.trim());
            course.setCourse_status(status.trim());
            course.setTerm_id(termID);
        }
        courseRepository.insertCourse(course);
    }

    public void insertCourse(CourseEntity courseEntity){
        courseRepository.insertCourse(courseEntity);
    }

    public void deleteCourse(CourseEntity courseEntity){
        courseRepository.deleteCourse(mLiveCourse.getValue());
    }

    public int lastID(){
        return allCourses.getValue().size();
    }
}
