package com.example.jasontrowbridgec196v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jasontrowbridgec196v2.Adapter.CourseAdapter;
import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.ViewModel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;



public class CourseListActivity extends AppCompatActivity {
    public static final int ADD_COURSE_REQUEST = 1;
    public static final int EDIT_COURSE_REQUEST = 2;
    private CourseViewModel courseViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);


        FloatingActionButton buttonAddCourse = findViewById(R.id.fab_add_course);
        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseListActivity.this, CourseEditorActivity.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CourseAdapter courseAdapter = new CourseAdapter();
        recyclerView.setAdapter(courseAdapter);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                courseAdapter.setCourses(courseEntities);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseListActivity.this);
                builder.setMessage("Are you sure you want to delete this course?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                courseViewModel.deleteCourse(courseAdapter.getCourseAtPosition(viewHolder.getAdapterPosition()));
                                Toast.makeText(CourseListActivity.this, "Course was deleted!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CourseListActivity.this, CourseListActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CourseListActivity.this, CourseListActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }).attachToRecyclerView(recyclerView);

        courseAdapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            //Selects item clicked to be edited in CourseEditorActivity and populates fields with selected term data
            @Override
            public void onItemClick(CourseEntity course) {
                Intent intent = new Intent(CourseListActivity.this, CourseEditorActivity.class);
                intent.putExtra(CourseEditorActivity.EXTRA_ID, course.getCourse_id());
                intent.putExtra(CourseEditorActivity.EXTRA_TITLE, course.getCourse_title());
                intent.putExtra(CourseEditorActivity.EXTRA_START_DATE, course.getCourse_start_date());
                intent.putExtra(CourseEditorActivity.EXTRA_END_DATE, course.getCourse_end_date());
                intent.putExtra(CourseEditorActivity.EXTRA_TERMID, course.getTerm_id());
                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }
        });
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_COURSE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(CourseEditorActivity.EXTRA_TITLE);
            String startDate = data.getStringExtra(CourseEditorActivity.EXTRA_START_DATE);
            String endDate = data.getStringExtra(CourseEditorActivity.EXTRA_END_DATE);
            String status = data.getStringExtra(CourseEditorActivity.EXTRA_STATUS);
            //Not sure this is how I should get termID?
            int termID = data.getIntExtra("TermID",Integer.parseInt(CourseEditorActivity.EXTRA_TERMID));
            Toast.makeText(this, "Adding course with TermID = " + termID, Toast.LENGTH_SHORT).show();
            CourseEntity course = new CourseEntity(title, startDate, endDate, status, termID);
            courseViewModel.insertCourse(course);

            Toast.makeText(this, "Course saved!", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_COURSE_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(CourseEditorActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Course can't be updated!", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(CourseEditorActivity.EXTRA_TITLE);
            String startDate = data.getStringExtra(CourseEditorActivity.EXTRA_START_DATE);
            String endDate = data.getStringExtra(CourseEditorActivity.EXTRA_END_DATE);
            String status = data.getStringExtra(CourseEditorActivity.EXTRA_STATUS);
            //Not sure this his how I should get termID?
            int termID = data.getIntExtra("TermID",Integer.parseInt(CourseEditorActivity.EXTRA_TERMID));

            CourseEntity course = new CourseEntity(title, startDate, endDate, status, termID);
            course.setCourse_id(id);
            courseViewModel.insertCourse(course);
            Toast.makeText(this, "Course has been UPDATED!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Course NOT Saved!", Toast.LENGTH_SHORT).show();
        }
    }
*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CourseListActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_delete_all_courses:
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseListActivity.this);
                builder.setMessage("Delete all courses?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                courseViewModel.deleteAllCourses();
                                Toast.makeText(CourseListActivity.this, "All courses were deleted!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(CourseListActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CourseListActivity.this, CourseListActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_menu, menu);
        return true;
    }
}