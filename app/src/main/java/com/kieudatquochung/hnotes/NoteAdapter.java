package com.kieudatquochung.hnotes;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.makeramen.roundedimageview.RoundedImageView;

public class NoteAdapter extends FirestoreRecyclerAdapter<Note, NoteAdapter.NoteViewHolder> {
    Context context;
    public NoteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }
    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.mTitleTextView.setText(note.title);
        holder.mContentTextView.setText(note.content);
        holder.mTimestampTextView.setText(Utility.timestampToString(note.timestamp));
        String docId = this.getSnapshots().getSnapshot(position).getId();
        holder.setNote(note);
        ImageView mMenuPopButton = holder.itemView.findViewById(R.id.menuPopButton);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CreateNotesActivity.class);
                intent.putExtra("title", note.getTitle());
                intent.putExtra("content", note.getContent());
                intent.putExtra("docId", docId);
                intent.putExtra("image", note.getImagePath());
                context.startActivity(intent);
            }
        });


        mMenuPopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
                popupMenu.setGravity(Gravity.END);
                popupMenu.getMenu().add("Pin/Unpin").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        Toast.makeText(v.getContext(), "This Note Clicked", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                });
                popupMenu.getMenu().add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setTitle("Confirm");
                        alertDialog.setIcon(R.drawable.ic_icon_delete);
                        alertDialog.setMessage("Are you sure you want to DELETE the note?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(docId);
                                documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(context, "Note Deleted Successful", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed While Deleting Note", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        return false;
                    }
                });
                popupMenu.getMenu().add("Complete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem item) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                        alertDialog.setTitle("You've done the work");
                        alertDialog.setIcon(R.drawable.ic_icon_done);
                        alertDialog.setMessage("Do you want to mark work as complete?");
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Note completedNote = getItem(holder.getAdapterPosition());
                                completedNote.setCompleted(true);
                                DocumentReference documentReference = Utility.getCollectionReferenceForNotes().document(docId);
                                documentReference.set(completedNote).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        MyDialog myDialog = new MyDialog(context);
                                        myDialog.show();
//                                        Toast.makeText(context, "Note marked as complete", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to mark note as complete", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                getSnapshots().getSnapshot(holder.getAdapterPosition()).getReference().delete();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
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
        LinearLayout mLayoutNote;
        TextView mTitleTextView, mContentTextView, mTimestampTextView;
        RoundedImageView mImageNote;
        ImageView mImageViewPin;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.note_Title_Text_View);
            mContentTextView = itemView.findViewById(R.id.note_Content_Text_View);
            mTimestampTextView = itemView.findViewById(R.id.note_Timestamp_Text_View);
            mLayoutNote = itemView.findViewById(R.id.layoutNote);
            mImageNote = itemView.findViewById(R.id.imageNote);
            mImageViewPin = itemView.findViewById(R.id.imageView_pin);

        }

        public void setNote(Note note) {
            GradientDrawable gradientDrawable = (GradientDrawable) mLayoutNote.getBackground();
            if (note.getColor() != null)
            {
                gradientDrawable.setColor(Color.parseColor(note.getColor()));
            } else
            {
                gradientDrawable.setColor(Color.parseColor("#333333"));
            }
            if (note.getImagePath() != null)
            {
                mImageNote.setImageBitmap(BitmapFactory.decodeFile(note.getImagePath()));
                mImageNote.setVisibility(View.VISIBLE);
            }
            else
            {
                mImageNote.setVisibility(View.GONE);
            }
        }
    }
}
