package com.example.jasontrowbridgec196v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasontrowbridgec196v2.Adapter.CourseAdapter;
import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.ViewModel.CourseViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermsEditorViewModel;

import java.util.Calendar;
import java.util.List;


public class TermEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final int ADD_COURSE_REQUEST = 1;
    public static final int EDIT_COURSE_REQUEST = 2;

    public static final String EXTRA_ID =
            "com.example.jasontrowbridgec196v2.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.jasontrowbridgec196v2.EXTRA_TITLE";
    public static final String EXTRA_START_DATE =
            "com.example.jasontrowbridgec196v2.START_DATE";
    public static final String EXTRA_END_DATE =
            "com.example.jasontrowbridgec196v2.END_DATE";
    public static final String EXTRA_STATUS =
            "com.example.jasontrowbridgec196v2.STATUS";
    public static final String EXTRA_TERMID =
            "com.example.jasontrowbridgec196v2.TERMID";

    private TermsEditorViewModel termsEditorViewModel;
    private CourseViewModel courseViewModel;
    public static int numTerms;
    private int position;
    private int currentTermID;
    private boolean newTerm;
    private boolean editTerm;
    private EditText editTextTitle;
    private EditText termStartDate;
    private EditText termEndDate;
    Button startDatePickerButton;
    Button endDatePickerButton;
    private TextView datePickerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //Opens CourseEditorActivity when add_course_button is selected
        Button buttonAddCourse = findViewById(R.id.add_course_button);
        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermEditorActivity.this, CourseEditorActivity.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST);
            }
        });

        editTextTitle = findViewById(R.id.edit_text_title);
        termStartDate = findViewById(R.id.term_start_date_text);
        termEndDate = findViewById(R.id.term_end_date_text);

        //Setup RecyclerView for Course List
        RecyclerView recyclerView = findViewById(R.id.course_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CourseAdapter courseAdapter = new CourseAdapter();
        recyclerView.setAdapter(courseAdapter);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        // *********** getCoursesByTerm argument should not be hard coded ************
        courseViewModel.getCoursesByTerm(9).observe(this, new Observer<List<CourseEntity>>() {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(TermEditorActivity.this);
                builder.setMessage("Are you sure you want to delete this course?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                courseViewModel.deleteCourse(courseAdapter.getCourseAtPosition(viewHolder.getAdapterPosition()));
                                Toast.makeText(TermEditorActivity.this, "Course was deleted!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TermEditorActivity.this, TermListActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TermEditorActivity.this, TermListActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }).attachToRecyclerView(recyclerView);

        //Selects Course and switches to CourseEditorActivity
        courseAdapter.setOnItemClickListener(new CourseAdapter.OnItemClickListener() {
            //Selects item clicked to be edited in CourseEditorActivity and populates fields with selected term data
            @Override
            public void onItemClick(CourseEntity course) {
                Intent intent = new Intent(TermEditorActivity.this, CourseEditorActivity.class);
                intent.putExtra(CourseEditorActivity.EXTRA_ID, course.getCourse_id());
                intent.putExtra(CourseEditorActivity.EXTRA_TITLE, course.getCourse_title());
                intent.putExtra(CourseEditorActivity.EXTRA_START_DATE, course.getCourse_start_date());
                intent.putExtra(CourseEditorActivity.EXTRA_END_DATE, course.getCourse_end_date());
                intent.putExtra(CourseEditorActivity.EXTRA_STATUS, course.getCourse_status());
                intent.putExtra(CourseEditorActivity.EXTRA_TERMID, course.getTerm_id());
                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }
        });
        setupDatePickers();
        initViewModel();
    }

    private void initViewModel(){
        termsEditorViewModel = ViewModelProviders.of(this)
                .get(TermsEditorViewModel.class);

        termsEditorViewModel.mLiveTerm.observe(this, new Observer<TermEntity>(){
            @Override
            public void onChanged(@Nullable TermEntity termEntity){
                Intent intent = getIntent();
                if(termEntity != null && intent.hasExtra(EXTRA_ID)){
                    editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
                    termStartDate.setText(intent.getStringExtra(EXTRA_START_DATE));
                    termEndDate.setText(intent.getStringExtra(EXTRA_END_DATE));
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            setTitle("Add Term");
            newTerm = true;
        } else {
            setTitle("Edit Term");
            int termID = extras.getInt(EXTRA_ID);
            this.currentTermID = termID;
            termsEditorViewModel.loadData(termID);
        }
    }

    private void setupDatePickers(){
        startDatePickerButton = findViewById(R.id.start_date_picker);
        endDatePickerButton = findViewById(R.id.end_date_picker);

        startDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerView = findViewById(R.id.term_start_date_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        endDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerView = findViewById(R.id.term_end_date_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }
    private void saveTerm() {
        String title = editTextTitle.getText().toString();
        String startDate = termStartDate.getText().toString();
        String endDate = termEndDate.getText().toString();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, start date, and end date.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_START_DATE, startDate);
        data.putExtra(EXTRA_END_DATE, endDate);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.term_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_term:
                AlertDialog.Builder builder = new AlertDialog.Builder(TermEditorActivity.this);
                builder.setMessage("Save?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveTerm();
                                Toast.makeText(TermEditorActivity.this, "Term was saved.", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TermEditorActivity.this, TermListActivity.class);
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month = month +1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = month + "/" + dayOfMonth + "/" + year;
        datePickerView.setText(currentDateString);
    }
}
