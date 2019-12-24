package com.example.jasontrowbridgec196v2;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.core.app.NotificationCompat;

import static com.example.jasontrowbridgec196v2.AssessmentEditorActivity.EXTRA_ASSESSMENTID;
import static com.example.jasontrowbridgec196v2.CourseEditorActivity.EXTRA_COURSEID;
import static com.example.jasontrowbridgec196v2.TermEditorActivity.EXTRA_TERMID;

public class NotificationReceiver extends BroadcastReceiver {
    public static final String courseAlarmFile = "courseAlarms";
    public static final String termAlarmFile = "termAlarms";
    public static final String assessmentAlarmFile = "assessmentAlarms";
    public static final String alarmFile = "alarmFile";
    public static final String nextAlarmField = "nextAlarmID";
    private static final String CHANNEL_ID = "TermTracker";

    @Override
    public void onReceive(Context context, Intent intent){

        //Inputs
        String destination = intent.getStringExtra("destination");
        if (destination == null || destination.isEmpty()) {
            destination = "";
        }
        int id = intent.getIntExtra("id", 0);
        String alarmTitle = intent.getStringExtra("title");
        String alarmText = intent.getStringExtra("text");
        int nextAlarmId = intent.getIntExtra("nextAlarmId", getAndIncrementNextAlarmId(context));

        // Notification Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_lightbulb_outline_black_24dp)
                .setContentTitle(alarmTitle)
                .setContentText(alarmText);

        // Target intent to launch
        Intent resultIntent;
        Uri uri;
        SharedPreferences sharedPreferences;
        switch (destination) {

            case "course":
                resultIntent = new Intent(context, CourseEditorActivity.class);
                resultIntent.putExtra(EXTRA_COURSEID, id);
                break;

            case "term":
                resultIntent = new Intent(context, TermEditorActivity.class);
                resultIntent.putExtra(EXTRA_TERMID, id);
                break;

            case "assessment":
                resultIntent = new Intent(context, AssessmentEditorActivity.class);
                resultIntent.putExtra(EXTRA_ASSESSMENTID, id);
                break;

            default:
                resultIntent = new Intent(context, MainActivity.class);
                break;
        }

        // task navigation for target intent.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent).setAutoCancel(true);

        // Do the notification
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(nextAlarmId, builder.build());
    }


    public static boolean _scheduleAlarm(Context context, int id, long time, String title, String text, String destinationString, String alarmFile) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        int nextAlarmId = getNextAlarmId(context);
        Intent intentAlarm = new Intent(context, NotificationReceiver.class);
        intentAlarm.putExtra("id", id);
        intentAlarm.putExtra("title", title);
        intentAlarm.putExtra("text", text);
        intentAlarm.putExtra("destination", destinationString);
        intentAlarm.putExtra("nextAlarmId", nextAlarmId);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, PendingIntent.getBroadcast(context, nextAlarmId, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT)); //FLAG_UPDATE_CURRENT));

        SharedPreferences sp = context.getSharedPreferences(alarmFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(Integer.toString(id), nextAlarmId);
        editor.commit();

        incrementNextAlarmId(context);
        return true;
    }

    public static boolean scheduleCourseAlarm(Context context, int id, long time, String title, String text) {
        return _scheduleAlarm(context, id, time, title, text, "course", courseAlarmFile);
    }

    public static boolean scheduleAssessmentAlarm(Context context, int id, long time, String title, String text) {
        return _scheduleAlarm(context, id, time, title, text, "assessment", assessmentAlarmFile);
    }

    public static boolean scheduleTermAlarm(Context context, int id, long time, String title, String text) {
        return _scheduleAlarm(context, id, time, title, text, "term", termAlarmFile);
    }

    private static int getNextAlarmId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alarmFile, Context.MODE_PRIVATE);
        int nextAlarmId = alarmPrefs.getInt(nextAlarmField, 1);
        return nextAlarmId;
    }

    private static void incrementNextAlarmId(Context context) {
        SharedPreferences alarmPrefs;
        alarmPrefs = context.getSharedPreferences(alarmFile, Context.MODE_PRIVATE);
        int nextAlarmId = alarmPrefs.getInt(nextAlarmField, 1);
        SharedPreferences.Editor alarmEditor = alarmPrefs.edit();
        alarmEditor.putInt(nextAlarmField, nextAlarmId + 1);
        alarmEditor.commit();
    }

    private static int getAndIncrementNextAlarmId(Context context) {
        int nextAlarmId = getNextAlarmId(context);
        incrementNextAlarmId(context);
        return nextAlarmId;
    }
}
