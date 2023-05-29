package com.kieudatquochung.hnotes;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;
    FirestoreRecyclerAdapter<Note, NoteViewHolder> noteAdapter;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.mTitleTextView.setText(note.title);
        holder.mContentTextView.setText(note.content);
        holder.mTimestampTextView.setText(Utility.timestampToString(note.timestamp));
        String docId = noteAdapter.getSnapshots().getSnapshot(position).getId();

        ImageView popupButton = holder.itemView.findViewById(R.id.menuPopupButton);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NoteDetailsActivity.class);
                intent.putExtra("title", note.getTitle());
                intent.putExtra("content", note.getContent());
                intent.putExtra("noteId", docId);

                v.getContext().startActivity(intent);
                //Toast.makeText(context.getApplicationContext(), "This is Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        popupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setGravity(Gravity.END);
                popupMenu.getMenu().add("Edit").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        Intent intent = new Intent(v.getContext(), editNoteActivity.class);
                        intent.putExtra("title", note.getTitle());
                        intent.putExtra("content", note.getContent());
                        intent.putExtra("noteId", docId);
                        v.getContext().startActivity(intent);
                        return false;
                    }
                });
                popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        DocumentReference documentReference = firebaseFirestore.collection("notes")
                                .document(firebaseUser.getUid()).collection("myNotes").document(docId);
                        documentReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(v.getContext(), "This is note deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(v.getContext(), "Failed To Delete", Toast.LENGTH_SHORT).show();
                            }
                        });
                        //Toast.makeText(v.getContext(), "This is note deleted", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                popupMenu.show();
            }
        });

    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_item, parent, false);
        return new NoteViewHolder(view);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder
    {
        TextView mTitleTextView, mContentTextView, mTimestampTextView;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.note_Title_Text_View);
            mContentTextView = itemView.findViewById(R.id.note_Content_Text_View);
            mTimestampTextView = itemView.findViewById(R.id.note_Timestamp_Text_View);
        }
    }
}
