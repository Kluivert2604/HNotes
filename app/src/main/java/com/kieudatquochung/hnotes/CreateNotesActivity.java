package com.kieudatquochung.hnotes;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class CreateNotesActivity extends AppCompatActivity {
    EditText mNotesTitleText, mNotesContentText;
    ImageButton mImageBackBtn, mImageDoneBtn;
    ProgressBar mProgressBarOfCreateNoteActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        mNotesContentText = findViewById(R.id.notesContentText);
        mNotesTitleText = findViewById(R.id.notesTitleText);
        mImageBackBtn = findViewById(R.id.imageBackBtn);
        mImageDoneBtn = findViewById(R.id.imageDoneBtn);
        mProgressBarOfCreateNoteActivity = findViewById(R.id.progressBarOfCreateNoteActivity);

        mImageDoneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgressBarOfCreateNoteActivity.setVisibility(View.VISIBLE);
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
    void saveNotes() {
        String noteTitle = mNotesTitleText.getText().toString().trim();
        String noteContent = mNotesContentText.getText().toString().trim();
        if (noteTitle == null || noteTitle.isEmpty()) {
            mNotesTitleText.setError("Title is Required");
            return;
        }
        Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        saveNoteToFirebase(note);
    }
    void saveNoteToFirebase(Note note)
    {
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForNotes().document();

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful())
                {
                    //Note is added
                    Toast.makeText(getApplicationContext(), "Note added successful", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Failed while adding note", Toast.LENGTH_SHORT).show();
                    mProgressBarOfCreateNoteActivity.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

}