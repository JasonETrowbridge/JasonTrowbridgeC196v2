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
import com.example.jasontrowbridgec196v2.Adapter.NoteAdapter;
import com.example.jasontrowbridgec196v2.Database.AssessmentEntity;
import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.Database.NoteEntity;
import com.example.jasontrowbridgec196v2.ViewModel.AssessmentEditorViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.AssessmentViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.CourseEditorViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.CourseViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.NoteViewModel;

import java.util.Calendar;
import java.util.List;

public class AssessmentEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, AdapterView.OnItemSelectedListener {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    public static final String EXTRA_ID =
            "com.example.jasontrowbridgec196v2.EXTRA_ID";
    public static final String EXTRA_NAME =
            "com.example.jasontrowbridgec196v2.EXTRA_NAME";
    public static final String EXTRA_DUE_DATE =
            "com.example.jasontrowbridgec196v2.EXTRA_DUE_DATE";
    public static final String EXTRA_TYPE =
            "com.example.jasontrowbridgec196v2.EXTRA_TYPE";
    public static final String EXTRA_COURSEID =
            "com.example.jasontrowbridgec196v2.EXTRA_COURSEID";
    public static final String EXTRA_COURSE_TITLE =
            "com.example.jasontrowbridgec196v2.EXTRA_COURSE_TITLE";

    private AssessmentEditorViewModel assessmentEditorViewModel;
    private NoteViewModel noteViewModel;
    private CourseViewModel courseViewModel;
    private AssessmentViewModel assessmentViewModel;


    private CourseEditorViewModel courseEditorViewModel;

    public static int numAssessments;
    private int position;
    private int currentAssessmentID;
    private boolean newAssessment;
    private boolean editAssessment;
    private EditText assessmentNameEditText;
    private EditText assessmentDateEditText;
    private TextView assessmentTypeTextView;
    private TextView assessmentCourseTitleTextView;
    Button assessmentDatePickerButton;
    private TextView datePickerView;
    private Spinner assessmentTypeSpinner;
    private Spinner assessmentCourseIDSpinner;

    private boolean initialSpinner;
    private int currentCourseID;
    private String currentCourseTitle;
    private String currentCourseTitleID;
    private int assessmentCourseID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);





        //Opens NoteEditorActivity when add_note_button is selected
        Button buttonAddNote = findViewById(R.id.add_note_button);
        buttonAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentEditorActivity.this, NoteEditorActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST);
            }
        });

        assessmentNameEditText = findViewById(R.id.edit_text_name);
        assessmentDateEditText = findViewById(R.id.assessment_due_date_text);
        assessmentTypeTextView = findViewById(R.id.assessment_type_text);
        assessmentCourseTitleTextView = findViewById(R.id.course_title_text);

        setupDatePickers();

        //initViewModel MUST before course list recycler view or currentTermID will be zero
        initViewModel();

        initNoteRecyclerView();

        //Type Spinner setup
        assessmentTypeSpinner = findViewById(R.id.assessment_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentTypeSpinner.setAdapter(adapter);
        assessmentTypeSpinner.setOnItemSelectedListener(this);

        //CourseID Spinner setup
        assessmentCourseIDSpinner = findViewById(R.id.assessment_course_spinner);
        final ArrayAdapter<CourseEntity> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        assessmentCourseIDSpinner.setAdapter(adapter2);
        assessmentCourseIDSpinner.setOnItemSelectedListener(this);

        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(List<CourseEntity> courseEntities) {
                adapter2.addAll(courseEntities);
            }
        });

        initViewModel2();

    }

    private void initNoteRecyclerView(){

        //Setup RecyclerView for Note List
        RecyclerView recyclerView = findViewById(R.id.note_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
        Toast.makeText(this, "currentAssessmentID = " + currentAssessmentID, Toast.LENGTH_SHORT).show();
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getNotesByAssessment(currentAssessmentID).observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                noteAdapter.setNotes(noteEntities);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentEditorActivity.this);
                builder.setMessage("Are you sure you want to delete this Note?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                noteViewModel.deleteNote(noteAdapter.getNoteAtPosition(viewHolder.getAdapterPosition()));
                                Toast.makeText(AssessmentEditorActivity.this, "Note was deleted!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AssessmentEditorActivity.this, AssessmentListActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AssessmentEditorActivity.this, AssessmentListActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }).attachToRecyclerView(recyclerView);

        //Selects Note and switches to NoteEditorActivity
        noteAdapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            //Selects item clicked to be edited in NoteEditorActivity and populates fields with selected term data
            @Override
            public void onItemClick(NoteEntity note) {
                Intent intent = new Intent(AssessmentEditorActivity.this, NoteEditorActivity.class);
                intent.putExtra(NoteEditorActivity.EXTRA_ID, note.getNote_id());
                intent.putExtra(NoteEditorActivity.EXTRA_TITLE, note.getNote_title());
                intent.putExtra(NoteEditorActivity.EXTRA_TEXT, note.getNote_text());
                intent.putExtra(NoteEditorActivity.EXTRA_ASSESSMENTID, note.getAssessment_id());
                startActivityForResult(intent, EDIT_NOTE_REQUEST);
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
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    //This initViewModel2 allows me to gather the Course Title needed for display
    private void initViewModel2() {
        courseEditorViewModel = ViewModelProviders.of(this)
                .get(CourseEditorViewModel.class);

            courseEditorViewModel.mLiveCourse.observe(this, new Observer<CourseEntity>() {

                @Override
                public void onChanged(@Nullable CourseEntity courseEntity) {
                    Intent intent = getIntent();
                    if (courseEntity != null && intent.hasExtra(EXTRA_ID)) {
                        assessmentCourseTitleTextView.setText(String.valueOf(courseEntity.getCourse_title()));
                        //currentCourseTitle = assessmentCourseTitleTextView.getText().toString();
                        //currentCourseTitleID = String.valueOf(courseEntity.getCourse_id());
                        //*** need to be able to take the getTerm_id value and convert that to
                        //the corresponding TermEntity getTerm_title
                        if (assessmentCourseTitleTextView != null) {
                            assessmentCourseIDSpinner.setSelection(getSpinnerIndex(assessmentCourseIDSpinner, currentCourseID));
                            //courseTermIDSpinner.setSelection(getSpinnerIndex(courseTermIDSpinner, courseTermTitleTextView.getText().toString()));
                        }
                    }
                }
            });

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int courseID = extras.getInt(EXTRA_COURSEID);
                this.currentCourseID = courseID;
                courseEditorViewModel.loadData(courseID);
            }
        }

    private void initViewModel() {
        assessmentEditorViewModel = ViewModelProviders.of(this)
                .get(AssessmentEditorViewModel.class);

        assessmentEditorViewModel.mLiveAssessment.observe(this, new Observer<AssessmentEntity>() {
            @Override
            public void onChanged(@Nullable AssessmentEntity assessmentEntity) {
                Intent intent = getIntent();
                if (assessmentEntity != null && intent.hasExtra(EXTRA_ID)) {
                    assessmentNameEditText.setText(assessmentEntity.getAssessment_name());
                    assessmentDateEditText.setText(assessmentEntity.getAssessment_date());
                    assessmentTypeTextView.setText(assessmentEntity.getAssessment_type());
                    assessmentCourseID = assessmentEntity.getCourse_id();

                    if(assessmentTypeTextView != null){
                        assessmentTypeSpinner.setSelection(getSpinnerIndex(assessmentTypeSpinner, assessmentTypeTextView.getText().toString()));
                    }
                }
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("Add Assessment");
            newAssessment = true;
        } else {
            setTitle("Edit Assessment");
            int assessmentID = extras.getInt(EXTRA_ID);
            this.currentAssessmentID = assessmentID;
            assessmentEditorViewModel.loadData(assessmentID);
        }
    }


    private void setupDatePickers() {
        assessmentDatePickerButton = findViewById(R.id.due_date_picker);

        assessmentDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerView = findViewById(R.id.assessment_due_date_text);
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

    }

    private void saveAssessment() {
        String name = assessmentNameEditText.getText().toString();
        String dueDate = assessmentDateEditText.getText().toString();
        String type = assessmentTypeSpinner.getSelectedItem().toString();

        if (name.trim().isEmpty() || dueDate.trim().isEmpty() || type.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a name, due date, and type.", Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, name);
        data.putExtra(EXTRA_DUE_DATE, dueDate);
        data.putExtra(EXTRA_TYPE,type);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
         */
        assessmentEditorViewModel.saveData(name, dueDate, type, currentCourseID);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assessment_editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_assessment:
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentEditorActivity.this);
                builder.setMessage("Save?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveAssessment();
                                Toast.makeText(AssessmentEditorActivity.this, "Assessment was saved.", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AssessmentEditorActivity.this, CourseListActivity.class);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CourseEntity courseSelected = (CourseEntity) assessmentCourseIDSpinner.getSelectedItem();
        currentCourseTitleID = String.valueOf(courseSelected.getCourse_id());
        currentCourseTitle = String.valueOf(courseSelected.getCourse_title());

        //used to set course_id when saving assessment
        currentCourseID = courseSelected.getCourse_id();

        //assessmentCourseTitleTextView.setText(courseSelected.getCourse_title());
        assessmentTypeTextView.setText(assessmentTypeSpinner.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
