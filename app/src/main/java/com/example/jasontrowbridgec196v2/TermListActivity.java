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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasontrowbridgec196v2.Adapter.TermAdapter;
import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.ViewModel.TermViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;

public class TermListActivity extends AppCompatActivity {
    public static final int ADD_TERM_REQUEST = 1;
    public static final int EDIT_TERM_REQUEST = 2;
    private TermViewModel termViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_list);

        FloatingActionButton buttonAddTerm = findViewById(R.id.fab_add_term);
        buttonAddTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TermListActivity.this, TermEditorActivity.class);
                startActivityForResult(intent, ADD_TERM_REQUEST);
            }
        });
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                                termViewModel.deleteTerm(adapter.getTermAtPosition(viewHolder.getAdapterPosition()));
                                Toast.makeText(TermListActivity.this, "Term was deleted!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TermListActivity.this, TermListActivity.class);
                                startActivity(intent);
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
                intent.putExtra(TermEditorActivity.EXTRA_ID, term.getTerm_id());
                intent.putExtra(TermEditorActivity.EXTRA_TITLE, term.getTerm_title());
                intent.putExtra(TermEditorActivity.EXTRA_START_DATE, term.getTerm_start_date());
                intent.putExtra(TermEditorActivity.EXTRA_END_DATE, term.getTerm_end_date());
                startActivityForResult(intent, EDIT_TERM_REQUEST);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TERM_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(TermEditorActivity.EXTRA_TITLE);
            String startDate = data.getStringExtra(TermEditorActivity.EXTRA_START_DATE);
            String endDate = data.getStringExtra(TermEditorActivity.EXTRA_END_DATE);

            TermEntity term = new TermEntity(title, startDate, endDate);
            termViewModel.insertTerm(term);

            Toast.makeText(this, "Term saved!", Toast.LENGTH_SHORT).show();

        } else if (requestCode == EDIT_TERM_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(TermEditorActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Term can't be updated!", Toast.LENGTH_SHORT).show();
                return;
            }
            String title = data.getStringExtra(TermEditorActivity.EXTRA_TITLE);
            String startDate = data.getStringExtra(TermEditorActivity.EXTRA_START_DATE);
            String endDate = data.getStringExtra(TermEditorActivity.EXTRA_END_DATE);

            TermEntity term = new TermEntity(title, startDate, endDate);
            term.setTerm_id(id);
            termViewModel.insertTerm(term);
            Toast.makeText(this, "Term has been UPDATED!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Term NOT Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Toast.makeText(this, "Home selected", Toast.LENGTH_SHORT).show();
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

                //Toast.makeText(this, "All Terms Deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.term_menu, menu);
        return true;
    }

}
