package com.example.jasontrowbridgec196v2;

import android.app.DatePickerDialog;
import android.icu.util.Calendar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class DatePicker {
    public static View.OnClickListener getDatePicker(final TextView textView) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LISTENER", "JUST INSIDE");
                DatePickerDialog picker;
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(v.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Log.d("LISTENER", "OK MADE IT HERE" + dayOfMonth);
                                textView.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        };

    }

}
