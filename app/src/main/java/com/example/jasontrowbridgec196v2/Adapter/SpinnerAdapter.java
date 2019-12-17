package com.example.jasontrowbridgec196v2.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.R;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private final LayoutInflater mInflater;
    private final Context mContext;
    private final List<TermEntity> terms;
    private final int mResource;

    public SpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);

        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        terms = objects;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return createItemView(position, convertView, parent);
    }
    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView termID = (TextView) view.findViewById(R.id.spinner_termid);
        TextView termTitle = (TextView) view.findViewById(R.id.spinner_term_title);
        TextView termDates = (TextView) view.findViewById(R.id.spinner_term_dates);

        TermEntity termData = terms.get(position);

        termID.setText(termData.getTerm_id());
        termTitle.setText(termData.getTerm_title());
        termDates.setText(termData.toString());

        return view;
    }
}
