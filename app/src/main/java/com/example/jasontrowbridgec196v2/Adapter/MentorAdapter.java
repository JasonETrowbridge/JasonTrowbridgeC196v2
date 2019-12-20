package com.example.jasontrowbridgec196v2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasontrowbridgec196v2.Database.MentorEntity;
import com.example.jasontrowbridgec196v2.R;

import java.util.ArrayList;
import java.util.List;

public class MentorAdapter extends RecyclerView.Adapter<MentorAdapter.MentorViewHolder>{
    private List<MentorEntity> mentors = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public MentorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mentors_list, parent, false);
        return new MentorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MentorViewHolder holder, int position) {
        final MentorEntity mentor = mentors.get(position);
        holder.textViewMentorName.setText(mentor.getMentor_name());
    }

    @Override
    public int getItemCount() {
        return mentors.size();
    }

    public MentorEntity getMentorAtPosition(int position){
        return mentors.get(position);
    }

    class MentorViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewMentorName;

        public MentorViewHolder(@NonNull View itemView){
            super(itemView);
            textViewMentorName = itemView.findViewById(R.id.text_view_mentor_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(mentors.get(position));
                    }
                }
            });
        }
    }

    public void setMentors(List<MentorEntity> mentors){
        this.mentors = mentors;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener{
        void onItemClick(MentorEntity mentor);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
}
