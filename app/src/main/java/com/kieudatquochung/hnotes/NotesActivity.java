package com.kieudatquochung.hnotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class NotesActivity extends AppCompatActivity {

    ImageButton mSaveNotesBtn;
    FloatingActionButton mCreateNoteFab;
    SearchView mSearchView;
    RecyclerView mRecyclerView;


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
}