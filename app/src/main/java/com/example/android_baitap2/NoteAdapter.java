package com.example.android_baitap2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.MyViewHolder> {

    private ArrayList<Note> noteList;
    private INoteClickListener mListener;

    public NoteAdapter(ArrayList<Note> noteList, INoteClickListener mListener) {
        this.noteList = noteList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Note note=noteList.get(position);
        if(note!=null)
        {
            holder.title.setText(note.getTitle());
            holder.description.setText(note.getDescription());
            String timeFormat= DateFormat.getDateTimeInstance().format(note.getTimeCreated());
            holder.timeCreated.setText(timeFormat);
            holder.mLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onClickHandle(note,holder.getAdapterPosition());
                }
            });

            holder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    mListener.OnLongClickHandle(holder.getAdapterPosition());
                    return true;
                }
            });

        }
    }

    @Override
    public int getItemCount() {

        return noteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView title,description,timeCreated;
        private View mLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.title);
            description=itemView.findViewById(R.id.description);
            timeCreated=itemView.findViewById(R.id.timeCreated);
            mLayout=itemView.findViewById(R.id.noteLayout);

        }


    }
}
