package com.example.jasontrowbridgec196v2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasontrowbridgec196v2.Database.AssessmentEntity;
import com.example.jasontrowbridgec196v2.R;

import java.util.ArrayList;
import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {
    private List<AssessmentEntity> assessments = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.assessments_list, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentViewHolder holder, int position) {
        final AssessmentEntity assessment = assessments.get(position);
        holder.textViewAssessmentName.setText(assessment.getAssessment_name());

    }

    @Override
    public int getItemCount() {
        return assessments.size();
    }

    public AssessmentEntity getAssessmentAtPosition(int position) {
        return assessments.get(position);
    }

    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewAssessmentName;


        public AssessmentViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAssessmentName = itemView.findViewById(R.id.text_view_assessment_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(assessments.get(position));
                    }
                }
            });
        }
    }

    public void setAssessments(List<AssessmentEntity> assessments) {
        this.assessments = assessments;
        notifyDataSetChanged();//*** NOT THE BEST OPTION ***
    }
    public interface OnItemClickListener {
        void onItemClick(AssessmentEntity assessment);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
