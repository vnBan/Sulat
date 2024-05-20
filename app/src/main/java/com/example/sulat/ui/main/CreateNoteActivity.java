package com.example.sulat.ui.main;

import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.sulat.R;
import com.example.sulat.database.Notes;
import com.example.sulat.database.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class CreateNoteActivity extends AppCompatActivity {
    EditText txtTitle, txtSubtitle, txtNoteContent;
    TextView txtTimeAndDate;
    ImageView imgSaveNote, imgBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_note);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        txtTitle = findViewById(R.id.inputNoteTitle);
        txtSubtitle = findViewById(R.id.inputNoteSubtitle);
        txtNoteContent = findViewById(R.id.inputNote);
        txtTimeAndDate = findViewById(R.id.textDateTime);
        imgBack = findViewById(R.id.imgBack);
        imgSaveNote = findViewById(R.id.imageSave);

        txtTimeAndDate.setText(String.valueOf(Timestamp.now()));

        imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        imgSaveNote.setOnClickListener(v -> saveNote());

    }

    private void saveNote(){
        String noteTitle = txtTitle.getText().toString();
        String noteSubtitle = txtSubtitle.getText().toString();
        String noteContent = txtNoteContent.getText().toString();

        if(noteTitle.isEmpty()){
            txtTitle.setError("Title is required.");
            return;
        }
        Notes note = new Notes();
        note.setTitle(noteTitle);
        note.setSubtitle(noteSubtitle);
        note.setContent(noteContent);
        note.setTimeDate(String.valueOf(Timestamp.now()));

        saveToFirebase(note);
    }

    private void saveToFirebase(Notes note){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceNotes().document();
        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // note is added to database
                    Toast.makeText(CreateNoteActivity.this, "Note is added.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(CreateNoteActivity.this, "Saving Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}