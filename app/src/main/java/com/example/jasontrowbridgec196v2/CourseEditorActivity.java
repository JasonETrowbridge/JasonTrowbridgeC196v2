package com.example.jasontrowbridgec196v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

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

import java.util.Calendar;

public class CourseEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
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

    private EditText editTextTitle;
    private EditText courseStartDate;
    private EditText courseEndDate;
    private EditText courseStatus;
    private EditText courseTermID;
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

        editTextTitle = findViewById(R.id.edit_text_title);
        courseStartDate = findViewById(R.id.course_start_date_text);
        courseEndDate = findViewById(R.id.course_end_date_text);
        courseStatus = findViewById(R.id.course_status_text);
        courseTermID = findViewById(R.id.course_termid_text);

        Intent intent = getIntent();

        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Course");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            courseStartDate.setText(intent.getStringExtra(EXTRA_START_DATE));
            courseEndDate.setText(intent.getStringExtra(EXTRA_END_DATE));
            courseStatus.setText(intent.getStringExtra(EXTRA_STATUS));
            courseTermID.setText(intent.getStringExtra(EXTRA_TERMID));
        } else {
            setTitle("Add Course");
        }

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

    private void saveCourse() {
        String title = editTextTitle.getText().toString();
        String startDate = courseStartDate.getText().toString();
        String endDate = courseEndDate.getText().toString();
        String status = courseStatus.getText().toString();
        String termID = courseTermID.getText().toString();

        if (title.trim().isEmpty() || startDate.trim().isEmpty() || endDate.trim().isEmpty() || status.trim().isEmpty() || termID.trim().isEmpty()) {
            Toast.makeText(this, "Please insert a title, start date, and end date, status, and termID.", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_START_DATE, startDate);
        data.putExtra(EXTRA_END_DATE, endDate);
        data.putExtra(EXTRA_STATUS, status);
        data.putExtra(EXTRA_TERMID, termID);

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
        calendar.set(Calendar.MONTH, month = month +1);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = month + "/" + dayOfMonth + "/" + year;
        datePickerView.setText(currentDateString);
    }
}

