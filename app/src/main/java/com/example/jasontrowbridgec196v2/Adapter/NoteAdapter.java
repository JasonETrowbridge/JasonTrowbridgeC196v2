package com.example.jasontrowbridgec196v2.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jasontrowbridgec196v2.Database.NoteEntity;
import com.example.jasontrowbridgec196v2.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{
    private List<NoteEntity> notes = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_list, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        final NoteEntity note = notes.get(position);
        holder.textViewNoteTitle.setText(note.getNote_title());

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public NoteEntity getNoteAtPosition(int position) {
        return notes.get(position);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewNoteTitle;


        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNoteTitle = itemView.findViewById(R.id.text_view_note_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(notes.get(position));
                    }
                }
            });
        }
    }

    public void setCourses(List<NoteEntity> notes) {
        this.notes = notes;
        notifyDataSetChanged();//*** NOT THE BEST OPTION ***
    }
    public interface OnItemClickListener {
        void onItemClick(NoteEntity note);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
