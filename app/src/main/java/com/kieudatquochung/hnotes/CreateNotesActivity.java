package com.kieudatquochung.hnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

public class CreateNotesActivity extends AppCompatActivity {
    EditText mNotesTitleText, mNotesContentText;
    ImageButton mImageBackBtn, mImageDoneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        mNotesContentText = findViewById(R.id.notesContentText);
        mNotesTitleText = findViewById(R.id.notesTitleText);
        mImageBackBtn = findViewById(R.id.imageBackBtn);
        mImageDoneBtn = findViewById(R.id.imageDoneBtn);

        mImageDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNotes();
            }
        });
        mImageBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    void saveNotes()
    {
        String noteTitle = mNotesTitleText.getText().toString().trim();
        String noteContent = mNotesContentText.getText().toString().trim();
        if (noteTitle == null || noteTitle.isEmpty())
        {
            mNotesTitleText.setError("Title is Required");
            return;
        }
    }
}