package com.example.jasontrowbridgec196v2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MentorEditorActivity extends AppCompatActivity {


    public static final String EXTRA_ID =
            "com.example.jasontrowbridgec196v2.EXTRA_ID";
    public static final String EXTRA_NAME =
            "com.example.jasontrowbridgec196v2.EXTRA_NAME";
    public static final String EXTRA_PHONE =
            "com.example.jasontrowbridgec196v2.EXTRA_PHONE";
    public static final String EXTRA_EMAIL =
            "com.example.jasontrowbridgec196v2.EXTRA_EMAIL";
    public static final String EXTRA_COURSEID =
            "com.example.jasontrowbridgec196v2.EXTRA_COURSEID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_editor);
    }
}
