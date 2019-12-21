package com.example.jasontrowbridgec196v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.jasontrowbridgec196v2.Adapter.AssessmentAdapter;
import com.example.jasontrowbridgec196v2.Database.AssessmentEntity;
import com.example.jasontrowbridgec196v2.ViewModel.AssessmentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class AssessmentListActivity extends AppCompatActivity {
    public static final int ADD_ASSESSMENT_REQUEST = 1;
    public static final int EDIT_ASSESSMENT_REQUEST = 2;
    private AssessmentViewModel assessmentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment_list);

        FloatingActionButton buttonAddAssessment = findViewById(R.id.fab_add_assessment);
        buttonAddAssessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentEditorActivity.class);
                startActivityForResult(intent, ADD_ASSESSMENT_REQUEST);
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final AssessmentAdapter assessmentAdapter = new AssessmentAdapter();
        recyclerView.setAdapter(assessmentAdapter);

        assessmentViewModel = ViewModelProviders.of(this).get(AssessmentViewModel.class);
        assessmentViewModel.getAllAssessments().observe(this, new Observer<List<AssessmentEntity>>() {
            @Override
            public void onChanged(List<AssessmentEntity> assessmentEntities) {
                assessmentAdapter.setAssessments(assessmentEntities);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentListActivity.this);
                builder.setMessage("Are you sure you want to delete this assessment?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                assessmentViewModel.deleteAssessment(assessmentAdapter.getAssessmentAtPosition(viewHolder.getAdapterPosition()));
                                Toast.makeText(AssessmentListActivity.this, "Assessment was deleted!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AssessmentListActivity.this, AssessmentListActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(AssessmentListActivity.this, AssessmentListActivity.class);
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }).attachToRecyclerView(recyclerView);

        assessmentAdapter.setOnItemClickListener(new AssessmentAdapter.OnItemClickListener() {
            //Selects item clicked to be edited in CourseEditorActivity and populates fields with selected term data
            @Override
            public void onItemClick(AssessmentEntity assessment) {
                Intent intent = new Intent(AssessmentListActivity.this, AssessmentEditorActivity.class);
                intent.putExtra(AssessmentEditorActivity.EXTRA_ID, assessment.getAssessment_id());
                intent.putExtra(AssessmentEditorActivity.EXTRA_NAME, assessment.getAssessment_name());
                intent.putExtra(AssessmentEditorActivity.EXTRA_DUE_DATE, assessment.getAssessment_date());
                intent.putExtra(AssessmentEditorActivity.EXTRA_TYPE, assessment.getAssessment_type());
                intent.putExtra(AssessmentEditorActivity.EXTRA_COURSEID, assessment.getCourse_id());
                startActivityForResult(intent, EDIT_ASSESSMENT_REQUEST);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AssessmentListActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_delete_all_assessments:
                AlertDialog.Builder builder = new AlertDialog.Builder(AssessmentListActivity.this);
                builder.setMessage("Delete all assessments?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                assessmentViewModel.deleteAllAssessments();
                                Toast.makeText(AssessmentListActivity.this, "All assessments were deleted!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(AssessmentListActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AssessmentListActivity.this, AssessmentListActivity.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.assessment_menu, menu);
        return true;
    }
}
