package com.example.jasontrowbridgec196v2.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasontrowbridgec196v2.Database.TermEntity;
import com.example.jasontrowbridgec196v2.R;

import java.util.ArrayList;
import java.util.List;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {
    private List<TermEntity> terms = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.terms_list, parent, false);

        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermViewHolder holder, int position) {
        TermEntity currentTerm = terms.get(position);
        holder.textViewTermTitle.setText(currentTerm.getTerm_title());
        holder.textViewTermDates.setText(currentTerm.getTerm_start_date() + " - " + currentTerm.getTerm_end_date());
    }

    @Override
    public int getItemCount() {
        return terms.size();
    }

    public void setTerms(List<TermEntity> terms) {
        this.terms = terms;
        notifyDataSetChanged();//*** NOT THE BEST OPTION ***
    }

    public TermEntity getTermAtPosition(int position) {
        return terms.get(position);
    }

    class TermViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTermTitle;
        private TextView textViewTermDates;

        public TermViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTermTitle = itemView.findViewById(R.id.text_view_term_title);
            textViewTermDates = itemView.findViewById(R.id.text_view_term_dates);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(terms.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TermEntity term);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
