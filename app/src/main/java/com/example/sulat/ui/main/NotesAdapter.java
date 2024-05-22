package com.example.sulat.ui.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sulat.R;
import com.example.sulat.database.Notes;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotesAdapter extends FirestoreRecyclerAdapter<Notes, NotesAdapter.NoteViewHolder> {
    Context context;
    public NotesAdapter(@NonNull FirestoreRecyclerOptions<Notes> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int i, @NonNull Notes notes) {
        noteViewHolder.title.setText(notes.getTitle());
        noteViewHolder.subtitle.setText(notes.getSubtitle());
        noteViewHolder.time.setText(notes.getTimeDate());
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_note,parent, false);
        return new NoteViewHolder(view);
    }


    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView title, subtitle, time;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            title =  itemView.findViewById(R.id.textTitle);
            subtitle =  itemView.findViewById(R.id.textSubtitle);
            time =  itemView.findViewById(R.id.textDateTime);
        }
    }
}
