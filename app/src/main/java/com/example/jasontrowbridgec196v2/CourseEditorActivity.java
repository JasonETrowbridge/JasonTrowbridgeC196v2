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
import android.widget.Space;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.ViewModel.CourseEditorViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermViewModel;

import java.util.Calendar;
import java.util.List;

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


    private CourseEditorViewModel courseEditorViewModel;
    private TermViewModel termViewModel;
    private EditText editTextTitle;
    private EditText courseStartDate;
    private EditText courseEndDate;
    private Spinner courseStatusSpinner;
    private Spinner courseTermIDSpinner;

    private boolean newCourse;
    private int currentTermID;
    private int currentCourseID;

    Button startDatePickerButton;
    Button endDatePickerButton;
    private TextView datePickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //Status Spinner setup
        courseStatusSpinner = findViewById(R.id.course_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatusSpinner.setAdapter(adapter);
        courseStatusSpinner.setOnItemSelectedListener(this);

        //Term Spinner setup
        courseTermIDSpinner = findViewById(R.id.term_spinner);
        final ArrayAdapter<TermEntity> termAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        termAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseTermIDSpinner.setAdapter(termAdapter);
        courseTermIDSpinner.setOnItemSelectedListener(this);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
               termAdapter.addAll(termEntities);
            }
        });

        editTextTitle = findViewById(R.id.edit_text_title);
        courseStartDate = findViewById(R.id.course_start_date_text);
        courseEndDate = findViewById(R.id.course_end_date_text);
        courseStatusSpinner.setAdapter(adapter);
        courseStatusSpinner.getSelectedItemPosition();
        courseTermIDSpinner.getSelectedItemPosition();


        setupDatePickers();
        initViewModel();
    }

    private void initViewModel() {
        courseEditorViewModel = ViewModelProviders.of(this)
                .get(CourseEditorViewModel.class);

        courseEditorViewModel.mLiveCourse.observe(this, new Observer<CourseEntity>() {

            @Override
            public void onChanged(@Nullable CourseEntity courseEntity) {
                Intent intent = getIntent();
                if (courseEntity != null && intent.hasExtra(EXTRA_ID)) {
                    editTextTitle.setText(courseEntity.getCourse_title());
                    courseStartDate.setText(courseEntity.getCourse_start_date());
                    courseEndDate.setText(courseEntity.getCourse_end_date());
                    courseStatusSpinner.getSelectedItemPosition();
                    courseTermIDSpinner.getSelectedItemPosition();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            setTitle("Add Course");
            newCourse = true;
        } else {
            setTitle("Edit Course");
            int courseID = extras.getInt(EXTRA_ID);
            this.currentCourseID = courseID;
            courseEditorViewModel.loadData(courseID);
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
        //String termID = courseTermIDSpinner.getItemAtPosition(position).toString();
        //String spinner_item2 = courseStatusSpinner.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void saveCourse() {

        String title = editTextTitle.getText().toString();
        String startDate = courseStartDate.getText().toString();
        String endDate = courseEndDate.getText().toString();
        String status = courseStatusSpinner.getSelectedItem().toString();
        currentTermID = courseTermIDSpinner.getSelectedItemPosition();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() || status.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, start date, and end date, status, and termID.", Toast.LENGTH_SHORT).show();
            return;
        }
        courseEditorViewModel.saveData(title, startDate, endDate, status, 9);
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

