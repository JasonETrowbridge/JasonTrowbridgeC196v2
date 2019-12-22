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
import com.example.jasontrowbridgec196v2.Adapter.CourseAdapter;
import com.example.jasontrowbridgec196v2.Adapter.MentorAdapter;
import com.example.jasontrowbridgec196v2.Database.AssessmentEntity;
import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.Database.MentorEntity;
import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.ViewModel.AssessmentViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.CourseEditorViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.CourseViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.MentorViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermEditorViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

public class CourseEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    public static final int ADD_MENTOR_REQUEST = 1;
    public static final int EDIT_MENTOR_REQUEST = 2;
    public static final int ADD_ASSESSMENT_REQUEST = 1;
    public static final int EDIT_ASSESSMENT_REQUEST = 2;

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

    public static final String EXTRA_TERM_TITLE =
            "com.example.jasontrowbridgec196v2.EXTRA_TERM_TITLE";

    private CourseEditorViewModel courseEditorViewModel;
    private TermViewModel termViewModel;
    private TermViewModel termViewModel2;
    private MentorViewModel mentorViewModel;
    private AssessmentViewModel assessmentViewModel;
    private MentorAdapter mentorAdapter;

    //experiment
    private TermEditorViewModel termEditorViewModel;

    private EditText courseTitleEditText;
    private EditText courseStartDateEditText;
    private EditText courseEndDateEditText;
    private TextView courseStatusTextView;
    private TextView courseTermTitleTextView;
    private Spinner courseStatusSpinner;
    private Spinner courseTermIDSpinner;

    private boolean newCourse;
    private boolean initialSpinner;
    private int currentTermID;
    private String currentTermTitle;
    private String currentTermTitleID;
    private String currentCourseStatus;
    private int courseTermID;
    private int currentCourseID;

    Button startDatePickerButton;
    Button endDatePickerButton;
    Button addMentorButton;
    Button addAssessmentButton;
    private TextView datePickerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        setupDatePickers();

        initViewModel();

        //Opens MentorEditorActivity when add_mentor_button is selected
        Button buttonAddMentor = findViewById(R.id.add_mentor_button);
        buttonAddMentor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CourseEditorActivity.this, MentorEditorActivity.class);
                startActivityForResult(intent, ADD_MENTOR_REQUEST);
            }
        });

        //Opens AssessmentEditorActivity when add_assessment_button is selected
        Button buttonAddAssessment = findViewById(R.id.add_assessment_button);
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

        initViewModel2();

        courseTitleEditText = findViewById(R.id.edit_text_title);
        courseStartDateEditText = findViewById(R.id.course_start_date_text);
        courseEndDateEditText = findViewById(R.id.course_end_date_text);
        courseStatusTextView = findViewById(R.id.course_status_text);
        courseTermTitleTextView = findViewById(R.id.course_term_title);

        initMentorRecyclerView();
        initAssessmentRecyclerView();



    }

    private void initMentorRecyclerView(){
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
                intent.putExtra(MentorEditorActivity.EXTRA_ID, mentor.getMentor_id());
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
                intent.putExtra(AssessmentEditorActivity.EXTRA_ID, assessment.getAssessment_id());
                intent.putExtra(AssessmentEditorActivity.EXTRA_NAME, assessment.getAssessment_name());
                intent.putExtra(AssessmentEditorActivity.EXTRA_DUE_DATE, assessment.getAssessment_date());
                intent.putExtra(AssessmentEditorActivity.EXTRA_TYPE,assessment.getAssessment_type());
                intent.putExtra(AssessmentEditorActivity.EXTRA_COURSEID, assessment.getCourse_id());
                startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST);
            }
        });
    }

    private int getSpinnerIndex(Spinner spinner, int myInt) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myInt)) {
                index = i;
            }
        }
        return index;
    }

    private int getSpinnerIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString.trim())) {
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
                if (termEntity != null && intent.hasExtra(EXTRA_ID)) {
                    courseTermTitleTextView.setText(String.valueOf(termEntity.getTerm_title()));
                    courseTermIDSpinner.getCount();
                    currentTermTitle = courseTermTitleTextView.getText().toString();
                    //currentTermTitleID = String.valueOf(termEntity.getTerm_id());
                    //*** need to be able to take the getTerm_id value and convert that to
                    //the corresponding TermEntity getTerm_title
                    if (courseTermTitleTextView != null) {
                        courseTermIDSpinner.setSelection(getSpinnerIndex(courseTermIDSpinner, currentTermTitle));


                    }

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
        initialSpinner = false;
        courseEditorViewModel = ViewModelProviders.of(this)
                .get(CourseEditorViewModel.class);

        courseEditorViewModel.mLiveCourse.observe(this, new Observer<CourseEntity>() {

            @Override
            public void onChanged(@Nullable CourseEntity courseEntity) {
                Intent intent = getIntent();
                if (courseEntity != null && intent.hasExtra(EXTRA_ID)) {
                    courseTitleEditText.setText(courseEntity.getCourse_title());
                    courseStartDateEditText.setText(courseEntity.getCourse_start_date());
                    courseEndDateEditText.setText(courseEntity.getCourse_end_date());
                    courseStatusTextView.setText(courseEntity.getCourse_status());
                    courseTermID = courseEntity.getTerm_id();

                    //*** need to be able to take the getTerm_id value and convert that to
                    //the corresponding TermEntity getTerm_title
                    // courseTermTitleTextView.setText(currentTermTitle);

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
        //When you select a course from the courseTermIDSpinner this part
        //captures the TermEntity by casting a single TermEntity corresponding to the selected Term
        //Then you assign the term_id value of the termSelected to currentTermID

        TermEntity termSelected = (TermEntity) courseTermIDSpinner.getSelectedItem();
        currentTermTitleID = String.valueOf(termSelected.getTerm_id());
        currentTermTitle = String.valueOf(termSelected.getTerm_title());

        //used to set term_id when saving course
        currentTermID = termSelected.getTerm_id();

        Toast.makeText(this, "currentTermTitleID = " + currentTermTitleID, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "currentTermTitle = " + currentTermTitle, Toast.LENGTH_SHORT).show();
        //courseTermTitleTextView.setText(termSelected.getTerm_title());
        courseStatusTextView.setText(courseStatusSpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void saveCourse() {

        String title = courseTitleEditText.getText().toString();
        String startDate = courseStartDateEditText.getText().toString();
        String endDate = courseEndDateEditText.getText().toString();
        String status = courseStatusSpinner.getSelectedItem().toString();
        String term = courseTermIDSpinner.getSelectedItem().toString();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() || status.trim().isEmpty() || term.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, start date, and end date, status, and a term.", Toast.LENGTH_SHORT).show();
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

