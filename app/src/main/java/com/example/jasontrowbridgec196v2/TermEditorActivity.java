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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jasontrowbridgec196v2.Adapter.CourseAdapter;
import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.ViewModel.CourseViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermEditorViewModel;

import java.util.Calendar;
import java.util.List;


public class TermEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    public static final int ADD_COURSE_REQUEST = 1;
    public static final int EDIT_COURSE_REQUEST = 2;

    public static final String EXTRA_TERMID =
            "com.example.jasontrowbridgec196v2.EXTRA_TERMID";
    public static final String EXTRA_TITLE =
            "com.example.jasontrowbridgec196v2.EXTRA_TITLE";
    public static final String EXTRA_START_DATE =
            "com.example.jasontrowbridgec196v2.START_DATE";
    public static final String EXTRA_END_DATE =
            "com.example.jasontrowbridgec196v2.END_DATE";
    public static final String EXTRA_STATUS =
            "com.example.jasontrowbridgec196v2.STATUS";


    private TermEditorViewModel termEditorViewModel;
    private CourseViewModel courseViewModel;
    public static int numTerms;
    private int position;
    private int currentTermID;
    private boolean newTerm;
    private boolean editTerm;
    private EditText termTitleEditText;
    private EditText termStartDateEditText;
    private EditText termEndDateEditText;
    Button startDatePickerButton;
    Button endDatePickerButton;
    Button buttonAddCourse;
    private TextView datePickerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        //Opens CourseEditorActivity when add_course_button is selected
        buttonAddCourse = findViewById(R.id.add_course_button);
        buttonAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermEditorActivity.this, CourseEditorActivity.class);
                startActivityForResult(intent, ADD_COURSE_REQUEST);
            }
        });

        setupDatePickers();
        //initViewModel MUST before course list recycler view or currentTermID will be zero
        initViewModel();

        initCourseRecyclerView();

        termTitleEditText = findViewById(R.id.term_title_edit_text);
        termStartDateEditText = findViewById(R.id.term_start_date_edit_text);
        termEndDateEditText = findViewById(R.id.term_end_date_edit_text);

        //Only enable AddCourse button if all fields are filled and !newTerm
        termTitleEditText.addTextChangedListener(termTextWatcher);
        termStartDateEditText.addTextChangedListener(termTextWatcher);
        termEndDateEditText.addTextChangedListener(termTextWatcher);

    }

    private TextWatcher termTextWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String termTitle = termTitleEditText.getText().toString().trim();
            String startDate = termStartDateEditText.getText().toString().trim();
            String endDate = termEndDateEditText.getText().toString().trim();

            buttonAddCourse.setEnabled(!newTerm && !termTitle.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void initCourseRecyclerView(){
        //Setup RecyclerView for Course List
        RecyclerView recyclerView = findViewById(R.id.course_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CourseAdapter courseAdapter = new CourseAdapter();
        recyclerView.setAdapter(courseAdapter);
        Toast.makeText(this, "currentTermID = " + currentTermID, Toast.LENGTH_SHORT).show();
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getCoursesByTerm(currentTermID).observe(this, new Observer<List<CourseEntity>>() {
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
                intent.putExtra(CourseEditorActivity.EXTRA_COURSEID, course.getCourse_id());
                intent.putExtra(CourseEditorActivity.EXTRA_TITLE, course.getCourse_title());
                intent.putExtra(CourseEditorActivity.EXTRA_START_DATE, course.getCourse_start_date());
                intent.putExtra(CourseEditorActivity.EXTRA_END_DATE, course.getCourse_end_date());
                intent.putExtra(CourseEditorActivity.EXTRA_STATUS, course.getCourse_status());
                intent.putExtra(CourseEditorActivity.EXTRA_TERMID, course.getTerm_id());
                startActivityForResult(intent, EDIT_COURSE_REQUEST);
            }
        });
    }

    private void initViewModel(){
        termEditorViewModel = ViewModelProviders.of(this)
                .get(TermEditorViewModel.class);

        termEditorViewModel.mLiveTerm.observe(this, new Observer<TermEntity>(){
            @Override
            public void onChanged(@Nullable TermEntity termEntity){
                Intent intent = getIntent();
                if(termEntity != null && intent.hasExtra(EXTRA_TERMID)){
                    termTitleEditText.setText(intent.getStringExtra(EXTRA_TITLE));
                    termStartDateEditText.setText(intent.getStringExtra(EXTRA_START_DATE));
                    termEndDateEditText.setText(intent.getStringExtra(EXTRA_END_DATE));
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null){
            setTitle("Add Term");
            newTerm = true;
        } else {
            setTitle("Edit Term");
            newTerm = false;
            int termID = extras.getInt(EXTRA_TERMID);
            this.currentTermID = termID;
            termEditorViewModel.loadData(termID);
        }
    }

    private void setupDatePickers(){
        startDatePickerButton = findViewById(R.id.start_date_picker);
        endDatePickerButton = findViewById(R.id.end_date_picker);

        startDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerView = findViewById(R.id.term_start_date_edit_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        endDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerView = findViewById(R.id.term_end_date_edit_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }
    private void saveTerm() {
        String title = termTitleEditText.getText().toString();
        String startDate = termStartDateEditText.getText().toString();
        String endDate = termEndDateEditText.getText().toString();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, start date, and end date.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_START_DATE, startDate);
        data.putExtra(EXTRA_END_DATE, endDate);

        int id = getIntent().getIntExtra(EXTRA_TERMID, -1);
        if(id != -1){
            data.putExtra(EXTRA_TERMID, id);
        }
        setResult(RESULT_OK, data);
        termEditorViewModel.saveData(title, startDate, endDate);
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
