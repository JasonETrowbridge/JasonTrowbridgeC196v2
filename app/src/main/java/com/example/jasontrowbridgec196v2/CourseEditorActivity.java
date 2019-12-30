package com.example.jasontrowbridgec196v2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import com.example.jasontrowbridgec196v2.Adapter.AssessmentAdapter;
import com.example.jasontrowbridgec196v2.Adapter.MentorAdapter;
import com.example.jasontrowbridgec196v2.Database.AssessmentEntity;
import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.Database.DateConverter;
import com.example.jasontrowbridgec196v2.Database.MentorEntity;
import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.ViewModel.AssessmentViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.CourseEditorViewModel;

import com.example.jasontrowbridgec196v2.ViewModel.MentorViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermEditorViewModel;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CourseEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    public static final int ADD_MENTOR_REQUEST = 1;
    public static final int EDIT_MENTOR_REQUEST = 2;
    public static final int ADD_ASSESSMENT_REQUEST = 1;
    public static final int EDIT_ASSESSMENT_REQUEST = 2;

    public static final String EXTRA_COURSEID =
            "com.example.jasontrowbridgec196v2.EXTRA_COURSEID";
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
    private TermViewModel termViewModel2;
    private MentorViewModel mentorViewModel;
    private AssessmentViewModel assessmentViewModel;
    private MentorAdapter mentorAdapter;
    private TermEditorViewModel termEditorViewModel;

    private EditText courseTitleEditText;
    private EditText courseStartDateEditText;
    private EditText courseEndDateEditText;
    private TextView courseStatusTextView;
    private TextView courseTermTitleTextView;
    private Spinner courseStatusSpinner;
    private Spinner courseTermIDSpinner;

    private boolean newCourse;

    private int spinnerCount = 0;
    private int currentTermID;
    private String originalTermTitle;
    private String currentTermTitle;
    private String currentTermTitleID;
    private String currentCourseStatus;
    private int courseTermID;
    private int currentCourseID;

    Button startDatePickerButton;
    Button endDatePickerButton;
    Button buttonAddMentor;
    Button buttonAddAssessment;
    private TextView datePickerView;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        setupDatePickers();

        //Opens MentorEditorActivity when add_mentor_button is selected
        buttonAddMentor = findViewById(R.id.add_mentor_button);
        buttonAddMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseEditorActivity.this, MentorEditorActivity.class);
                startActivityForResult(intent, ADD_MENTOR_REQUEST);
            }
        });

        //Opens AssessmentEditorActivity when add_assessment_button is selected
        buttonAddAssessment = findViewById(R.id.add_assessment_button);
        buttonAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseEditorActivity.this, AssessmentEditorActivity.class);
                startActivityForResult(intent, ADD_ASSESSMENT_REQUEST);
            }
        });

        //Status Spinner setup
        courseStatusSpinner = findViewById(R.id.course_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.status, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseStatusSpinner.setAdapter(adapter);
        courseStatusSpinner.setOnItemSelectedListener(this);

        //TermID Spinner setup
        courseTermIDSpinner = findViewById(R.id.term_spinner);
        final ArrayAdapter<TermEntity> adapter2 = new ArrayAdapter<TermEntity>(this,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        courseTermIDSpinner.setAdapter(adapter2);
        courseTermIDSpinner.setOnItemSelectedListener(this);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> termEntities) {
                adapter2.addAll(termEntities);
            }
        });

        initViewModel();
        initViewModel2();

        //Populate RecyclerViews
        initMentorRecyclerView();
        initAssessmentRecyclerView();

        courseTitleEditText = findViewById(R.id.course_title_edit_text);
        courseStartDateEditText = findViewById(R.id.course_start_date_edit_text);
        courseEndDateEditText = findViewById(R.id.course_end_date_edit_text);
        courseStatusTextView = findViewById(R.id.course_status_text);
        courseTermTitleTextView = findViewById(R.id.course_term_title_text_view);

        //Only enables AddMentor button and AddAssessment button if all fields are filled and !newCourse
        courseTitleEditText.addTextChangedListener(courseTextWatcher);
        courseStartDateEditText.addTextChangedListener(courseTextWatcher);
        courseEndDateEditText.addTextChangedListener(courseTextWatcher);
        courseStatusTextView.addTextChangedListener(courseTextWatcher);
        courseTermTitleTextView.addTextChangedListener(courseTextWatcher);

    }

    private void _scheduleAlert(int id, String time, String title, String text) {
        long now = DateConverter.nowDate();
        long alertTime = DateConverter.toTimestamp(time);
        if (now <= DateConverter.toTimestamp(time)) {
            NotificationReceiver.scheduleCourseAlarm(getApplicationContext(), id, alertTime,
                    text, title + ", occurring at: " + time);
        }
    }

    public void scheduleAlert(MenuItem menuItem) {
        String dateText = courseStartDateEditText.getText().toString();
        String text = "Course starts today!";
        String title = courseTitleEditText.getText().toString();
        _scheduleAlert(currentCourseID, dateText, title, text);

        dateText = courseEndDateEditText.getText().toString();
        text = "Course ends today!";
        _scheduleAlert(currentCourseID, dateText, title, text);
    }

    private TextWatcher courseTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String courseTitle = courseTitleEditText.getText().toString().trim();
            String startDate = courseStartDateEditText.getText().toString().trim();
            String endDate = courseEndDateEditText.getText().toString().trim();
            String status = courseStatusTextView.getText().toString().trim();
            String term = courseTermTitleTextView.getText().toString().trim();

            buttonAddMentor.setEnabled(!newCourse && !courseTitle.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty() && !status.isEmpty() && !term.isEmpty());
            buttonAddAssessment.setEnabled(!newCourse && !courseTitle.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty() && !status.isEmpty() && !term.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initMentorRecyclerView() {
        //Setup RecyclerView for Mentor List
        RecyclerView recyclerView = findViewById(R.id.mentor_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final MentorAdapter mentorAdapter = new MentorAdapter();
        recyclerView.setAdapter(mentorAdapter);
        mentorViewModel = ViewModelProviders.of(this).get(MentorViewModel.class);
        mentorViewModel.getMentorByCourse(currentCourseID).observe(this, new Observer<List<MentorEntity>>() {
            @Override
            public void onChanged(List<MentorEntity> mentorEntities) {
                mentorAdapter.setMentors(mentorEntities);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseEditorActivity.this);
                builder.setMessage("Are you sure you want to delete this mentor?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mentorViewModel.deleteMentor(mentorAdapter.getMentorAtPosition(viewHolder.getAdapterPosition()));
                                Toast.makeText(CourseEditorActivity.this, "Mentor was deleted!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CourseEditorActivity.this, CourseListActivity.class);
                                startActivity(intent);
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
            }
        }).attachToRecyclerView(recyclerView);

        //Selects Mentor and switches to MentorEditorActivity
        mentorAdapter.setOnItemClickListener(new MentorAdapter.OnItemClickListener() {
            //Selects item clicked to be edited in MentorEditorActivity and populates fields with selected term data
            @Override
            public void onItemClick(MentorEntity mentor) {
                Intent intent = new Intent(CourseEditorActivity.this, MentorEditorActivity.class);
                intent.putExtra(MentorEditorActivity.EXTRA_MENTORID, mentor.getMentor_id());
                intent.putExtra(MentorEditorActivity.EXTRA_NAME, mentor.getMentor_name());
                intent.putExtra(MentorEditorActivity.EXTRA_PHONE, mentor.getMentor_phone());
                intent.putExtra(MentorEditorActivity.EXTRA_EMAIL, mentor.getMentor_email());
                intent.putExtra(MentorEditorActivity.EXTRA_COURSEID, mentor.getCourse_id());
                startActivityForResult(intent, EDIT_MENTOR_REQUEST);
            }
        });
    }

    private void initAssessmentRecyclerView() {
        //Setup RecyclerView for Assessment List
        RecyclerView recyclerView2 = findViewById(R.id.assessment_list_recycler_view);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        recyclerView2.setHasFixedSize(true);

        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter();
        recyclerView2.setAdapter(assessmentAdapter);
        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAssessmentByCourse(currentCourseID).observe(this, new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessmentEntities) {
                assessmentAdapter.setAssessments(assessmentEntities);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView2, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseEditorActivity.this);
                builder.setMessage("Are you sure you want to delete this assessment?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                assessmentViewModel.deleteAssessment(assessmentAdapter.getAssessmentAtPosition(viewHolder.getAdapterPosition()));
                                Toast.makeText(CourseEditorActivity.this, "Assessment was deleted!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CourseEditorActivity.this, CourseListActivity.class);
                                startActivity(intent);
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
            }
        }).attachToRecyclerView(recyclerView2);

        //Selects Assessment and switches to AssessmentEditorActivity
        assessmentAdapter.setOnItemClickListener(new AssessmentAdapter.OnItemClickListener() {
            //Selects item clicked to be edited in MentorEditorActivity and populates fields with selected term data
            @Override
            public void onItemClick(AssessmentEntity assessment) {
                Intent intent = new Intent(CourseEditorActivity.this, AssessmentEditorActivity.class);
                intent.putExtra(AssessmentEditorActivity.EXTRA_ASSESSMENTID, assessment.getAssessment_id());
                intent.putExtra(AssessmentEditorActivity.EXTRA_NAME, assessment.getAssessment_name());
                intent.putExtra(AssessmentEditorActivity.EXTRA_DUE_DATE, assessment.getAssessment_date());
                intent.putExtra(AssessmentEditorActivity.EXTRA_TYPE, assessment.getAssessment_type());
                intent.putExtra(AssessmentEditorActivity.EXTRA_COURSEID, assessment.getCourse_id());
                startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST);
            }
        });
    }

    private int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().trim().equals(myString.trim())) {
                index = i;
            }
        }
        return index;
    }

    //This initViewModel2 allows me to gather the Term Title needed for display
    private void initViewModel2() {
        termEditorViewModel = ViewModelProviders.of(this)
                .get(TermEditorViewModel.class);

        termEditorViewModel.mLiveTerm.observe(this, new Observer<TermEntity>() {

            @Override
            public void onChanged(@Nullable TermEntity termEntity) {
                Intent intent = getIntent();
                if (termEntity != null && intent.hasExtra(EXTRA_COURSEID)) {
                    courseTermTitleTextView.setText(String.valueOf(termEntity.getTerm_title()));
                    originalTermTitle = courseTermTitleTextView.getText().toString().trim();
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int termID = extras.getInt(EXTRA_TERMID);
            this.currentTermID = termID;
            termEditorViewModel.loadData(termID);
        }
    }

    private void initViewModel() {

        courseEditorViewModel = ViewModelProviders.of(this)
                .get(CourseEditorViewModel.class);

        courseEditorViewModel.mLiveCourse.observe(this, new Observer<CourseEntity>() {

            @Override
            public void onChanged(@Nullable CourseEntity courseEntity) {
                Intent intent = getIntent();
                if (courseEntity != null && intent.hasExtra(EXTRA_COURSEID)) {
                    courseTitleEditText.setText(courseEntity.getCourse_title());
                    courseStartDateEditText.setText(courseEntity.getCourse_start_date());
                    courseEndDateEditText.setText(courseEntity.getCourse_end_date());
                    courseStatusTextView.setText(courseEntity.getCourse_status());
                    courseTermID = courseEntity.getTerm_id();

                    //Sets initial selection on courseStatusSpinner when editing existing course
                    if (courseStatusTextView != null) {
                        courseStatusSpinner.setSelection(getSpinnerIndex(courseStatusSpinner, courseStatusTextView.getText().toString()));
                    }
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("Add Course");
            newCourse = true;
        } else {
            setTitle("Edit Course");
            int courseID = extras.getInt(EXTRA_COURSEID);
            newCourse = false;
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
                datePickerView = findViewById(R.id.course_start_date_edit_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        endDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerView = findViewById(R.id.course_end_date_edit_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //When you select a course from the courseTermIDSpinner this part
        //captures the TermEntity by casting a single TermEntity corresponding to the selected Term

        //spinnerCount is used to prevent courseTermTitleTextView from changing before user actually makes a selection
        //also is used to (setSelection) on courseTermIDSpinner only once
        TermEntity termSelected = (TermEntity) courseTermIDSpinner.getSelectedItem();
        if (termSelected != null) {
            if (spinnerCount <= 1 && !newCourse) {
                //Toast.makeText(this, "Spinner index = " + getSpinnerIndex(courseTermIDSpinner, originalTermTitle), Toast.LENGTH_SHORT).show();
                courseTermIDSpinner.setSelection(getSpinnerIndex(courseTermIDSpinner, originalTermTitle));
                spinnerCount++;
            }
            currentTermTitleID = String.valueOf(termSelected.getTerm_id());
            currentTermTitle = String.valueOf(termSelected.getTerm_title());
            if (spinnerCount > 1) {
                courseTermTitleTextView.setText(currentTermTitle);
            }
            //used to set term_id when saving course

            currentTermID = termSelected.getTerm_id();
        }

        courseStatusTextView.setText(courseStatusSpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private boolean saveCourse() {

        if (courseTermIDSpinner.getCount() == 0) {
            Toast.makeText(this, "Please create a Term before adding a Course!", Toast.LENGTH_LONG).show();
            finish();
            return false;
        }
        String title = courseTitleEditText.getText().toString();
        String startDate = courseStartDateEditText.getText().toString();
        String endDate = courseEndDateEditText.getText().toString();
        String status = courseStatusSpinner.getSelectedItem().toString();
        String term = courseTermIDSpinner.getSelectedItem().toString();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() || status.trim().isEmpty() || term.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, start date, and end date, status, and a term.", Toast.LENGTH_SHORT).show();
            return false;
        }
        courseEditorViewModel.saveData(title, startDate, endDate, status, currentTermID);
        finish();
        return true;
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

                                if (saveCourse()) {
                                    Toast.makeText(CourseEditorActivity.this, "Course was saved.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(CourseEditorActivity.this, "Course was NOT saved!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CourseEditorActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }
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
            case R.id.alert_course:
                scheduleAlert(item);
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
        // String currentDateString = month + "/" + dayOfMonth + "/" + year;
        String currentDateString = year + "-" + month + "-" + dayOfMonth;
        datePickerView.setText(currentDateString);
    }

}

