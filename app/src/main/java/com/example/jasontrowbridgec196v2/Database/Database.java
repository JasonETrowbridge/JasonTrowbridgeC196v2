package com.example.jasontrowbridgec196v2.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


@androidx.room.Database(entities = {NoteEntity.class, TermEntity.class, CourseEntity.class, AssessmentEntity.class,
        MentorEntity.class}, version = 2)
public abstract class Database extends RoomDatabase {

    private static final String DATABASE_NAME = "WGUC196.db";
    private static volatile Database dbInstance;
    private static final Object LOCK = new Object();

    public abstract TermDAO termDAO();
    public abstract CourseDAO courseDAO();
    public abstract AssessmentDAO assessmentDAO();
    public abstract MentorDAO mentorDAO();
    public abstract NoteDAO noteDAO();

    public static Database getDbInstance(Context context) {

        if (dbInstance == null) {
            synchronized (LOCK) {
                if (dbInstance == null) {

                    dbInstance = Room.databaseBuilder(context.getApplicationContext(), Database.class, DATABASE_NAME)
                            .fallbackToDestructiveMigration()
                            .addCallback(roomCallBack)
                            .build();
                    Toast.makeText(context, "Database being created", Toast.LENGTH_SHORT).show();
                }
            }
        }
        return dbInstance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(dbInstance).execute();

        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private TermDAO termDAO;
        private CourseDAO courseDAO;
        private AssessmentDAO assessmentDAO;
        private MentorDAO mentorDAO;
        private NoteDAO noteDAO;

        private PopulateDbAsyncTask(Database db) {

            termDAO = db.termDAO();
            courseDAO = db.courseDAO();
            assessmentDAO = db.assessmentDAO();
            mentorDAO = db.mentorDAO();
            noteDAO = db.noteDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            termDAO.insertTerm(new TermEntity("Term 1", "12/01/2019", "12/29/2019"));
            termDAO.insertTerm(new TermEntity("Term 2", "12/02/2019", "12/30/2019"));
            termDAO.insertTerm(new TermEntity("Term 3", "12/03/2019", "12/31/2019"));
            return null;
        }
    }
}
