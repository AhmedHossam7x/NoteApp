package com.example.note;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.note.Adapter.NoteAdapter;
import com.example.note.Database.RoomDB;
import com.example.note.Models.Notes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{

    RecyclerView recyclerView;
    NoteAdapter noteAdapter;
    List<Notes> notes= new ArrayList<>();
    RoomDB database;
    FloatingActionButton floatingActionButton;
    SearchView searchView;
    Notes selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        intViews();

        database= RoomDB.getInstance(this);
        notes= database.mainDAO().getAll();

        updateRecycler(notes);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainActivity.this, NotesActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchNote(s);
                return false;
            }
        });
    }

    private void searchNote(String s) {
        List<Notes> searchNote= new ArrayList<>();
        for (Notes single : notes){
            if(single.getTitle().toLowerCase().contains(s.toLowerCase())
               || single.getNotes().toLowerCase().contains(s.toLowerCase())){
                searchNote.add(single);
            }
        }
        noteAdapter.searchList(searchNote);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == Activity.RESULT_OK){
                Notes notes1= (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(notes1);//add note data in database
                notes.clear();
                notes.addAll(database.mainDAO().getAll());//add all note in notes object
                noteAdapter.notifyDataSetChanged();
            }
        }else if(requestCode == 2){
            if(resultCode == Activity.RESULT_OK){
                Notes neW= (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(neW.getID(), neW.getTitle(), neW.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                noteAdapter.notifyDataSetChanged();
            }
        }

    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayout.VERTICAL));
        noteAdapter= new NoteAdapter(MainActivity.this, notes, noteClickListener);
        recyclerView.setAdapter(noteAdapter);
    }

    private final NoteClickListener noteClickListener= new NoteClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent= new Intent(MainActivity.this, NotesActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 2);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selected= new Notes();
            selected= notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu= new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    private void intViews() {
        recyclerView= findViewById(R.id.recycleView);
        floatingActionButton= findViewById(R.id.fab_btn);
        searchView= findViewById(R.id.sv_main);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.pin_popupmenu:
                if(selected.isPinned()){
                    database.mainDAO().pin(selected.getID(), false);
                    Toast.makeText(MainActivity.this, "Unpinned", Toast.LENGTH_SHORT).show();
                }else {
                    database.mainDAO().pin(selected.getID(), true);
                    Toast.makeText(MainActivity.this, "Pinned", Toast.LENGTH_SHORT).show();
                }
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                noteAdapter.notifyDataSetChanged();
                return true;
            case R.id.delete_popupmenu:
                database.mainDAO().delete(selected);
                notes.remove(selected);
                noteAdapter.notifyDataSetChanged();
                Toast.makeText(MainActivity.this, "DELETED", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }
    }
}