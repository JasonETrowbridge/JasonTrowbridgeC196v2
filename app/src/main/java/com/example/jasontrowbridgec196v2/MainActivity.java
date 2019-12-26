package com.example.jasontrowbridgec196v2;

// *** This code was written in API 29 ***

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        createNotificationChannel();

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_terms:
                //Toast.makeText(this, "Terms selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, TermListActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_courses:
                //Toast.makeText(this, "Courses selected", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(MainActivity.this, CourseListActivity.class);
                startActivity(intent2);
                return true;
            case R.id.nav_assessments:
                Toast.makeText(this, "Assessments selected", Toast.LENGTH_SHORT).show();
                Intent intent3 = new Intent(MainActivity.this, AssessmentListActivity.class);
                startActivity(intent3);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            String channel_id = getString(R.string.channel_id);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channel_id, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showAssessmentList(View view) {
        Intent intent = new Intent(this, AssessmentListActivity.class);
        startActivity(intent);
    }

    public void showCourseList(View view) {
        Intent intent = new Intent(MainActivity.this, CourseListActivity.class);
        startActivity(intent);
    }

    public void showTermList(View view) {
        Intent intent = new Intent(MainActivity.this, TermListActivity.class);
        startActivity(intent);
    }

}
