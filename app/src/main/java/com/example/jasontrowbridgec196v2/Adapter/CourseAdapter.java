package com.example.jasontrowbridgec196v2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasontrowbridgec196v2.Database.CourseEntity;
import com.example.jasontrowbridgec196v2.R;

import java.util.ArrayList;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {
    private List<CourseEntity> courses = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courses_list, parent, false);

        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseViewHolder holder, int position) {
        CourseEntity currentCourse = courses.get(position);
        holder.textViewCourseTitle.setText(currentCourse.getCourse_title());
        holder.textViewCourseDates.setText(currentCourse.toString());
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public void setCourses(List<CourseEntity> courses) {
        this.courses = courses;
        notifyDataSetChanged();//*** NOT THE BEST OPTION ***
    }

    public CourseEntity getCourseAtPosition(int position) {
        return courses.get(position);
    }

    class CourseViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewCourseTitle;
        private TextView textViewCourseDates;


        public CourseViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCourseTitle = itemView.findViewById(R.id.text_view_course_title);
            textViewCourseDates = itemView.findViewById(R.id.text_view_course_dates);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(courses.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(CourseEntity course);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
