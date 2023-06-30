package com.kieudatquochung.hnotes;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class Utility {
    static CollectionReference getCollectionReferenceForNotes()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes")
                .document(currentUser.getUid()).collection("my_note");
    }
    static String timestampToString(Timestamp timestamp)
    {
        return new SimpleDateFormat("EEE, d MMM YYYY HH:mm a").format(timestamp.toDate());
    }
}