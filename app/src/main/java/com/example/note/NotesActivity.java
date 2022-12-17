package com.example.note;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.note.Models.Notes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesActivity extends AppCompatActivity {

    EditText editText_title, editText_notes;
    ImageView imageView;
    Notes notes;
    boolean old= false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        intViews();

        notes= new Notes();

        try {
            notes= (Notes) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notes.getTitle());
            editText_notes.setText(notes.getNotes());
            old= true;
        }catch (Exception e){
            e.printStackTrace();
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title= editText_title.getText().toString();
                String description= editText_notes.getText().toString();

                if(description.isEmpty()){
                    Toast.makeText(NotesActivity.this, "Add your notes PLZ", Toast.LENGTH_SHORT).show();
                    return;
                }
                SimpleDateFormat dateFormat= new SimpleDateFormat("EEE d MMM YYY HH:mm a");
                Date date= new Date();

                if(!old){   notes= new Notes(); }

                notes.setTitle(title);
                notes.setNotes(description);
                notes.setDate(dateFormat.format(date));

                Intent intent= new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

    }

    private void intViews() {
        editText_title= findViewById(R.id.et_title_notes_Activity);
        editText_notes= findViewById(R.id.et_note_notes_Activity);
        imageView= findViewById(R.id.image_save);
    }
}