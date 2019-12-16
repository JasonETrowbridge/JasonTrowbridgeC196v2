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

import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.ViewModel.TermViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermsEditorViewModel;

import java.util.Calendar;


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

    private TermsEditorViewModel termsEditorViewModel;
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
