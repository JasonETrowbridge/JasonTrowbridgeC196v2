package com.example.jasontrowbridgec196v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.ViewModel.CourseEditorViewModel;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CourseEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    public static final String EXTRA_ID =
            "com.example.jasontrowbridgec196v2.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.jasontrowbridgec196v2.EXTRA_TITLE";
    public static final String EXTRA_START_DATE =
            "com.example.jasontrowbridgec196v2.EXTRA_START_DATE";
    public static final String EXTRA_END_DATE =
            "com.example.jasontrowbridgec196v2.EXTRA_END_DATE";
    public static final String EXTRA_STATUS =
            "com.example.jasontrowbridgec196v2.EXTRA_STATUS";
    public static final String EXTRA_TERMID =
            "com.example.jasontrowbridgec196v2.EXTRA_TERMID";
    public static final String EXTRA_EDITING =
            "com.example.jasontrowbridgec196v2.EXTRA_EDITING";

    private CourseEditorViewModel courseEditorViewModel;
    private boolean newCourse;
    private boolean editCourse;
    private Integer currentCourseID;
    Integer currentTermID;

    @BindView(R.id.edit_text_title)
    EditText editTextTitle;
    @BindView(R.id.course_start_date_text)
    EditText courseStartDate;
    @BindView(R.id.course_end_date_text)
    EditText courseEndDate;
    @BindView(R.id.course_spinner)
    Spinner courseStatus;
    @BindView(R.id.course_termid_text)
    TextView courseTermID;

    Button startDatePickerButton;
    Button endDatePickerButton;
    private TextView datePickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        ButterKnife.bind(this);

        if(savedInstanceState != null){
            editCourse = savedInstanceState.getBoolean(EXTRA_EDITING);
        }

        Spinner spinner = findViewById(R.id.course_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        setupDatePickers();
        initViewModel();
    }

    private void initViewModel() {
        courseEditorViewModel = ViewModelProviders.of(this)
                .get(CourseEditorViewModel.class);

        courseEditorViewModel.mLiveCourse.observe(this, new Observer<CourseEntity>() {

            @Override
            public void onChanged(@Nullable CourseEntity courseEntity) {

                if (courseEntity != null && !editCourse) {
                    editTextTitle.setText(courseEntity.getCourse_title());
                    courseStartDate.setText(courseEntity.getCourse_start_date());
                    courseEndDate.setText(courseEntity.getCourse_end_date());
                    courseTermID.setText(courseEntity.getTerm_id());
                    courseStatus.getSelectedItemPosition();
                    currentTermID = courseEntity.getTerm_id();
                    currentCourseID = courseEntity.getCourse_id();
                }
            }
        });

        Bundle extras = getIntent().getExtras();

        int course_id = 0;
        try {
            course_id = extras.getInt(EXTRA_ID);
        }catch (NullPointerException e){
            Toast.makeText(this, "Null Pointer Exception", Toast.LENGTH_SHORT).show();
        }
        if (course_id == 0) {
            setTitle("Add Course");
            newCourse = true;
            try {
                this.currentTermID = extras.getInt(EXTRA_TERMID);
            } catch (NullPointerException e){
                Toast.makeText(this, "Null Pointer Exception", Toast.LENGTH_SHORT).show();
            }
        } else {
            setTitle("Edit Course");
            this.currentCourseID = course_id;
            courseEditorViewModel.loadData(currentCourseID);
        }
    }

    private void setupDatePickers() {
        startDatePickerButton = findViewById(R.id.start_date_picker);
        endDatePickerButton = findViewById(R.id.end_date_picker);

        startDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerView = findViewById(R.id.course_start_date_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        endDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerView = findViewById(R.id.course_end_date_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //String status = parent.getItemAtPosition(position).toString();
        courseStatus.getItemAtPosition(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void saveCourse() {
        String title = editTextTitle.getText().toString();
        String startDate = courseStartDate.getText().toString();
        String endDate = courseEndDate.getText().toString();
        String status = courseStatus.getSelectedItem().toString();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() || status.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, start date, and end date, status, and termID.", Toast.LENGTH_SHORT).show();
            return;
        }
        courseEditorViewModel.saveData(title, startDate, endDate, status, currentTermID);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.course_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_course:
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseEditorActivity.this);
                builder.setMessage("Save?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveCourse();
                                Toast.makeText(CourseEditorActivity.this, "Course was saved.", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(CourseEditorActivity.this, CourseListActivity.class);
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
        calendar.set(Calendar.MONTH, month = month + 1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = month + "/" + dayOfMonth + "/" + year;
        datePickerView.setText(currentDateString);
    }


}

