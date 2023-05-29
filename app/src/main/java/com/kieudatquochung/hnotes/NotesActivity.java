package com.kieudatquochung.hnotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class NotesActivity extends AppCompatActivity {

    ImageButton mSaveNotesBtn;
    FloatingActionButton mCreateNoteFab;
    SearchView mSearchView;
    RecyclerView mRecyclerView;
    NoteAdapter noteAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        mCreateNoteFab = findViewById(R.id.createNoteFab);
        mSaveNotesBtn = findViewById(R.id.saveNotesBtn);
        mSearchView = findViewById(R.id.searchView);
        mRecyclerView = findViewById(R.id.recyclerView);

        mCreateNoteFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotesActivity.this, CreateNotesActivity.class));
            }
        });

        mSaveNotesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenu();
            }
        });

        setupRecyclerView();
    }
    private void showMenu()
    {
        //TODO display Menu
        PopupMenu popupMenu = new PopupMenu(NotesActivity.this, mSaveNotesBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getTitle() == "Logout")
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(NotesActivity.this, MainActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }
    void setupRecyclerView()
    {
        Query query = Utility.getCollectionReferenceForNotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class).build();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options, this);
        mRecyclerView.setAdapter(noteAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        noteAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        noteAdapter.notifyDataSetChanged();
    }
}