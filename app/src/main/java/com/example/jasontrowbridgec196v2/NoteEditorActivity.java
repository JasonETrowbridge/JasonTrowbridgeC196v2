package com.example.jasontrowbridgec196v2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NoteEditorActivity extends AppCompatActivity {
    public static final String EXTRA_ID =
            "com.example.jasontrowbridgec196v2.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.example.jasontrowbridgec196v2.EXTRA_TITLE";
    public static final String EXTRA_TEXT =
            "com.example.jasontrowbridgec196v2.EXTRA_TEXT";
    public static final String EXTRA_ASSESSMENTID =
            "com.example.jasontrowbridgec196v2.EXTRA_ASSESSMENTID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
    }
}
