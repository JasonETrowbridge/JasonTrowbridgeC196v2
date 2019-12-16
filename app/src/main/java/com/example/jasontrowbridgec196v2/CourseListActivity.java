package com.example.jasontrowbridgec196v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.jasontrowbridgec196v2.Adapter.CourseAdapter;
import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.ViewModel.CourseViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CourseListActivity extends AppCompatActivity {
    public static final int ADD_COURSE_REQUEST = 1;
    public static final int EDIT_COURSE_REQUEST = 2;

    private FloatingActionButton courseAddFab;

    public static final String EXTRA_TERMID =
            "com.example.jasontrowbridgec196v2.EXTRA_TERMID";

    private CourseViewModel courseViewModel;
    private List<CourseEntity> courseData = new ArrayList<>();
    private CourseAdapter courseAdapter;
    private Integer courseParentTermField;

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @OnClick(R.id.fab_add_course)
    void fabClickHandler(){
        Intent intent = new Intent(this, CourseEditorActivity.class);
        intent.putExtra(EXTRA_TERMID, courseParentTermField);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        initRecyclerView();
        initViewModel();
        courseAddFab = findViewById(R.id.fab_add_course);
        if (courseParentTermField == null){
            courseAddFab.hide();
        } else {
            courseAddFab.show();
        }
/*
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
                                courseViewModel.deleteCourse(adapter.getCourseAtPosition(viewHolder.getAdapterPosition()));
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

        adapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            //Selects item clicked to be edited in TermEditorActivity and populates fields with selected term data
            @Override
            public void onItemClick(CourseEntity course) {
                Intent intent = new Intent(CourseListActivity.this, CourseEditorActivity.class);
                intent.putExtra(CourseEditorActivity.EXTRA_ID, course.getCourse_id());
                intent.putExtra(CourseEditorActivity.EXTRA_TITLE, course.getCourse_title());
                intent.putExtra(CourseEditorActivity.EXTRA_START_DATE, course.getCourse_start_date());
                intent.putExtra(CourseEditorActivity.EXTRA_END_DATE, course.getCourse_end_date());
                intent.putExtra(String.valueOf(CourseEditorActivity.EXTRA_TERMID), course.getTerm_id());
                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }
        }); */
    }

    private void initViewModel(){

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            courseViewModel = ViewModelProviders.of(this)
                    .get(CourseViewModel.class);
        } else {
            courseParentTermField = extras.getInt(EXTRA_TERMID);
            courseViewModel = ViewModelProviders.of(this)
                    .get(CourseViewModel.class);
            courseViewModel.setCurrentTerm(courseParentTermField);
        }

        final Observer<List<CourseEntity>> coursesObserver =
                new Observer<List<CourseEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<CourseEntity> courseEntities) {
                        courseData.clear();
                        courseData.addAll(courseEntities);

                        if (courseAdapter == null) {
                            courseAdapter = new CourseAdapter(courseData,CourseListActivity.this);
                            recyclerView.setAdapter(courseAdapter);
                        } else {
                            courseAdapter.notifyDataSetChanged();
                        }
                    }
                };
        courseViewModel = ViewModelProviders.of(this)
                .get(CourseViewModel.class);
        courseViewModel.allCourses.observe(this, coursesObserver);
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration divider = new DividerItemDecoration(
                recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

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
            int termID = data.getIntExtra("TermID",Integer.parseInt(CourseEditorActivity.EXTRA_TERMID));

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