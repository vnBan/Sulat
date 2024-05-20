package com.example.sulat.database;

import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class Utility {
    public static CollectionReference getCollectionReferenceNotes(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return FirebaseFirestore.getInstance().collection("notes").document(currentUser.getUid()).collection("my_notes");
    }
}
