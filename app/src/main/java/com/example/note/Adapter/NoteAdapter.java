package com.example.note.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.Models.Notes;
import com.example.note.NoteClickListener;
import com.example.note.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder>{

    Context context;
    List<Notes> notes;
    NoteClickListener noteClickListener;

    public NoteAdapter(Context context, List<Notes> notes, NoteClickListener noteClickListener) {
        this.context = context;
        this.notes = notes;
        this.noteClickListener = noteClickListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(context).inflate(R.layout.note_design, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.textView_title.setText(notes.get(position).getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_note.setText(notes.get(position).getNotes());

        holder.textView_date.setText(notes.get(position).getDate());
        holder.textView_date.setSelected(true);

        if(notes.get(position).isPinned()){
            holder.imageView.setImageResource(R.drawable.ic_baseline_push_pin_24);
        }else {
            holder.imageView.setImageResource(0);
        }

        int setColor= getRandomColor();
        holder.cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(setColor, null));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteClickListener.onClick(notes.get(holder.getAdapterPosition()));
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                noteClickListener.onLongClick(notes.get(holder.getAdapterPosition()), holder.cardView);
                return true;
            }
        });
    }

    private int getRandomColor(){
        List<Integer> colorNum= new ArrayList<>();
        colorNum.add(R.color.color1);
        colorNum.add(R.color.color2);
        colorNum.add(R.color.color3);
        colorNum.add(R.color.color4);
        colorNum.add(R.color.color5);
        colorNum.add(R.color.color6);
        colorNum.add(R.color.color7);
        colorNum.add(R.color.color8);
        colorNum.add(R.color.color9);
        colorNum.add(R.color.color10);

        Random random= new Random();
        int colorRandom= random.nextInt(colorNum.size());
        return colorNum.get(colorRandom);

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public void searchList(List<Notes> search){
        notes= search;
        notifyDataSetChanged();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView textView_title, textView_note, textView_date;
        CardView cardView;
        ImageView imageView;
        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView= itemView.findViewById(R.id.note_design);
            textView_title= itemView.findViewById(R.id.tv_title_design);
            textView_note= itemView.findViewById(R.id.tv_note_design);
            textView_date= itemView.findViewById(R.id.tv_date_design);
            imageView= itemView.findViewById(R.id.iv_note_design);

        }
    }
}
