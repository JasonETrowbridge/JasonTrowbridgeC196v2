package com.example.jasontrowbridgec196v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
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

import com.example.jasontrowbridgec196v2.Adapter.TermAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TermEditorActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_ID =
            "com.example.jasontrowbridgec196v2.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.jasontrowbridgec196v2.EXTRA_TITLE";
    public static final String EXTRA_START_DATE =
            "com.example.jasontrowbridgec196v2.START_DATE";
    public static final String EXTRA_END_DATE =
            "com.example.jasontrowbridgec196v2.END_DATE";

    private EditText editTextTitle;
    private EditText termStartDate;
    private EditText termEndDate;
    Button startDatePickerButton;
    Button endDatePickerButton;

    private DateFormat dateFormat;
    private TextView datePickerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dateFormat = new SimpleDateFormat("MM/dd/YYYY", Locale.US);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_term_editor);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        editTextTitle = findViewById(R.id.edit_text_title);
        termStartDate = findViewById(R.id.term_start_date_text);
        termEndDate = findViewById(R.id.term_end_date_text);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Term");
            editTextTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            termStartDate.setText(intent.getStringExtra(EXTRA_START_DATE));
            termEndDate.setText(intent.getStringExtra(EXTRA_END_DATE));
        } else {
            setTitle("Add Term");
        }

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
                saveTerm();
                Toast.makeText(this, "Save Term selected", Toast.LENGTH_SHORT).show();
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
