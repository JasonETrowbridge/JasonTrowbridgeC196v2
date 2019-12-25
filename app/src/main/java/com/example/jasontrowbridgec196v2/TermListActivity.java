package com.example.jasontrowbridgec196v2;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasontrowbridgec196v2.Adapter.CourseAdapter;
import com.example.jasontrowbridgec196v2.Adapter.TermAdapter;
import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.ViewModel.CourseViewModel;
import com.example.jasontrowbridgec196v2.ViewModel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.ArrayList;
import java.util.List;

public class TermListActivity extends AppCompatActivity {
    public static final int ADD_TERM_REQUEST = 1;
    public static final int EDIT_TERM_REQUEST = 2;
    private TermViewModel termViewModel;
    private CourseViewModel courseViewModel;
    private CourseAdapter courseAdapter = new CourseAdapter();
    private int numCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white);

        FloatingActionButton buttonAddTerm = findViewById(R.id.fab_add_term);
        buttonAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermListActivity.this, TermEditorActivity.class);
                startActivityForResult(intent, ADD_TERM_REQUEST);
            }
        });

        initCourseRecyclerView();

        initTermRecyclerView();
    }

    private void initTermRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TermAdapter adapter = new TermAdapter();
        recyclerView.setAdapter(adapter);

        termViewModel = ViewModelProviders.of(this).get(TermViewModel.class);
        termViewModel.getAllTerms().observe(this, new Observer<List<TermEntity>>() {
            @Override
            public void onChanged(List<TermEntity> terms) {
                adapter.setTerms(terms);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(TermListActivity.this);
                builder.setMessage("Are you sure you want to delete this term?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                boolean courseFound = false;
                                for (int i = 0; i < numCourses; i++) {
                                    int courseTermID = courseAdapter.getCourseAtPosition(i).getTerm_id();
                                    int termTermID = adapter.getTermAtPosition(viewHolder.getAdapterPosition()).getTerm_id();
                                    if (courseTermID == termTermID) {
                                        courseFound = true;
                                    }
                                }
                                if (!courseFound) {
                                    termViewModel.deleteTerm(adapter.getTermAtPosition(viewHolder.getAdapterPosition()));
                                    Toast.makeText(TermListActivity.this, "Term was deleted!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(TermListActivity.this, TermListActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(TermListActivity.this, "Cannot delete Term with Courses associated with it!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(TermListActivity.this, TermListActivity.class);
                                    startActivity(intent);
                                }
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TermListActivity.this, TermListActivity.class);
                        startActivity(intent);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new TermAdapter.OnItemClickListener() {
            //Selects item clicked to be edited in TermEditorActivity and populates fields with selected term data
            @Override
            public void onItemClick(TermEntity term) {
                Intent intent = new Intent(TermListActivity.this, TermEditorActivity.class);
                intent.putExtra(TermEditorActivity.EXTRA_TERMID, term.getTerm_id());
                intent.putExtra(TermEditorActivity.EXTRA_TITLE, term.getTerm_title());
                intent.putExtra(TermEditorActivity.EXTRA_START_DATE, term.getTerm_start_date());
                intent.putExtra(TermEditorActivity.EXTRA_END_DATE, term.getTerm_end_date());
                startActivityForResult(intent, EDIT_TERM_REQUEST);
            }
        });
    }

    private void initCourseRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.term_course_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final CourseAdapter adapter = new CourseAdapter();
        recyclerView.setAdapter(adapter);
        courseViewModel = ViewModelProviders.of(this).get(CourseViewModel.class);
        courseViewModel.getAllCourses().observe(this, new Observer<List<CourseEntity>>() {
            @Override
            public void onChanged(@Nullable final List<CourseEntity> courseEntities) {
                adapter.setCourses(courseEntities);
                courseAdapter.setCourses(courseEntities);
                numCourses = courseEntities.size();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.term_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent = new Intent(TermListActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_delete_all_terms:
                AlertDialog.Builder builder = new AlertDialog.Builder(TermListActivity.this);
                builder.setMessage("Delete all terms?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                termViewModel.deleteAllTerms();
                                Toast.makeText(TermListActivity.this, "All terms were deleted!", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(TermListActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(TermListActivity.this, TermListActivity.class);
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
}
