package com.kieudatquochung.hnotes;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import java.io.InputStream;

public class CreateNotesActivity extends AppCompatActivity {
    EditText mNotesTitleText, mNotesContentText;
    ImageButton mImageBackBtn, mImageDoneBtn;
    ProgressBar mProgressBarOfCreateNoteActivity;
    String title, content, docId, image;
    boolean isEditMode = false;
    View mViewSubtitleIndicator;
    ImageView mImageNote;
    TextView mTextWebURL;
    LinearLayout mLayoutWebURL;
//    TextView mDeleteNoteTextViewBtn;

    String selectedNoteColor;
    private String selectedImagePath;

    private static final int REQUEST_CODE_STORAGE_PERMISSION = 1;
    private static final int REQUEST_CODE_SELECT_IMAGE = 2;

    private AlertDialog mDialogAddURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notes);

        mNotesContentText = findViewById(R.id.notesContentText);
        mNotesTitleText = findViewById(R.id.notesTitleText);
        mImageBackBtn = findViewById(R.id.imageBackBtn);
        mImageDoneBtn = findViewById(R.id.imageDoneBtn);
        mProgressBarOfCreateNoteActivity = findViewById(R.id.progressBarOfCreateNoteActivity);
        mViewSubtitleIndicator = findViewById(R.id.viewSubtitleIndicator);
        mImageNote = findViewById(R.id.imageNote);
        mTextWebURL = findViewById(R.id.textWebURL);
        mLayoutWebURL = findViewById(R.id.layoutWebURL);
//        mDeleteNoteTextViewBtn = findViewById(R.id.deleteNoteTextViewBtn);

        //Receive data
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        image = getIntent().getStringExtra("image");


        if (docId != null && !docId.isEmpty())
        {
            isEditMode = true;
        }

        mNotesTitleText.setText(title);
        mNotesContentText.setText(content);

        selectedNoteColor = "#333333";
        selectedImagePath = "";

        initMiscellaneous();
        setSubtitleIndicatorColor();
//        if (isEditMode)
//        {
//            mDeleteNoteTextViewBtn.setVisibility(View.VISIBLE);
//        }
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
//        mDeleteNoteTextViewBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                deleteNoteFromFirebase();
//            }
//        });
    }
    void saveNotes() {
        String noteTitle = mNotesTitleText.getText().toString().trim();
        String noteContent = mNotesContentText.getText().toString().trim();
        if (noteTitle == null || noteTitle.isEmpty()) {
            mNotesTitleText.setError("Title is Required");
            return;
        }
        if (noteContent.isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Please add some note", Toast.LENGTH_SHORT).show();
            return;
        }
        final Note note = new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());
        note.setColor(selectedNoteColor);
        note.setImagePath(selectedImagePath);
        saveNoteToFirebase(note);

        if (mLayoutWebURL.getVisibility() == View.VISIBLE){
            note.setWebLink(mTextWebURL.getText().toString());
        }
    }
    void saveNoteToFirebase(Note note)
    {
        DocumentReference documentReference;
        if (isEditMode)
        {
            //Update the note
            documentReference = Utility.getCollectionReferenceForNotes().document(docId);
        }
        else
        {
            //Create new note
            documentReference = Utility.getCollectionReferenceForNotes().document();
        }
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
    private void initMiscellaneous()
    {
        LinearLayout layoutMiscellaneous = findViewById(R.id.layoutMiscellaneous);
        BottomSheetBehavior<LinearLayout> bottomSheetBehavior = BottomSheetBehavior.from(layoutMiscellaneous);
        layoutMiscellaneous.findViewById(R.id.textMiscellaneous).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED)
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else
                {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        final ImageView imageColor1 = layoutMiscellaneous.findViewById(R.id.imageColor1);
        final ImageView imageColor2 = layoutMiscellaneous.findViewById(R.id.imageColor2);
        final ImageView imageColor3 = layoutMiscellaneous.findViewById(R.id.imageColor3);
        final ImageView imageColor4 = layoutMiscellaneous.findViewById(R.id.imageColor4);
        final ImageView imageColor5 = layoutMiscellaneous.findViewById(R.id.imageColor5);
        final ImageView imageColor6 = layoutMiscellaneous.findViewById(R.id.imageColor6);

        layoutMiscellaneous.findViewById(R.id.viewColor1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#333333";
                imageColor1.setImageResource(R.drawable.baseline_done_24);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });

        layoutMiscellaneous.findViewById(R.id.viewColor2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FDB33B";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(R.drawable.baseline_done_24);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FF4842";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(R.drawable.baseline_done_24);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#3A52Fc";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(R.drawable.baseline_done_24);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#000000";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(R.drawable.baseline_done_24);
                imageColor6.setImageResource(0);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.viewColor6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedNoteColor = "#FFFFFFFF";
                imageColor1.setImageResource(0);
                imageColor2.setImageResource(0);
                imageColor3.setImageResource(0);
                imageColor4.setImageResource(0);
                imageColor5.setImageResource(0);
                imageColor6.setImageResource(R.drawable.baseline_done_24);
                setSubtitleIndicatorColor();
            }
        });
        layoutMiscellaneous.findViewById(R.id.layoutAddImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (ContextCompat.checkSelfPermission(
                        getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            CreateNotesActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE_PERMISSION
                    );
                } else {
                    selectImage();
                }
            }
        });
        layoutMiscellaneous.findViewById(R.id.layoutAddUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                showAddURLDialog();
            }
        });
    }
    private void setSubtitleIndicatorColor()
    {
        GradientDrawable gradientDrawable = (GradientDrawable) mViewSubtitleIndicator.getBackground();
        gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
    }
    private void selectImage()
    {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.length > 0){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == RESULT_OK){
            if (data != null){
                Uri selectedImageUri = data.getData();
                if (selectedImageUri != null){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        mImageNote.setImageBitmap(bitmap);
                        mImageNote.setVisibility(View.VISIBLE);
                        selectedImagePath = getPathFromUri(selectedImageUri);
                    }catch (Exception exception){
                        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
    private String getPathFromUri(Uri contentUri) {
        String filePath;
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null)
        {
            filePath = contentUri.getPath();
        }else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }
        return filePath;
    }
    private void showAddURLDialog(){
        if (mDialogAddURL == null){
            AlertDialog.Builder builder = new AlertDialog.Builder(CreateNotesActivity.this);
            View view = LayoutInflater.from(this).inflate(R.layout.layout_add_uri,(ViewGroup) findViewById(R.id.layoutAddUrlContainer));
            builder.setView(view);
            mDialogAddURL = builder.create();
            if (mDialogAddURL.getWindow() != null){
                mDialogAddURL.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }

            final EditText mInputURL = view.findViewById(R.id.inputURL);
            mInputURL.requestFocus();

            view.findViewById(R.id.textAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mInputURL.getText().toString().trim().isEmpty()){
                        Toast.makeText(CreateNotesActivity.this, "Enter URL", Toast.LENGTH_SHORT).show();
                    } else if (!Patterns.WEB_URL.matcher(mInputURL.getText().toString()).matches()) {
                        Toast.makeText(CreateNotesActivity.this, "Enter valid URL", Toast.LENGTH_SHORT).show();
                    } else {
                        mTextWebURL.setText(mInputURL.getText().toString());
                        mLayoutWebURL.setVisibility(View.VISIBLE);
                        mDialogAddURL.dismiss();
                    }
                }
            });
            view.findViewById(R.id.textCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialogAddURL.dismiss();
                }
            });
        }
        mDialogAddURL.show();
    }
}