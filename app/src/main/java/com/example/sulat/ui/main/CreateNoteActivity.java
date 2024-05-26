package com.example.sulat.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.example.sulat.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import jp.wasabeef.richeditor.RichEditor;

public class CreateNoteActivity extends AppCompatActivity {
    EditText txtTitle, txtSubtitle;
    RichEditor txtNoteContent;
    TextView txtTimeAndDate;
    ImageView imgSaveNote, imgBack;
    Spinner spinnerFontSizes;
    Button btnItalic, btnBold, btnUnderline, btnIndent, btnOutdent, btnAlignLeft, btnAlignRight, btnAlignCenter;
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
        spinnerFontSizes = findViewById(R.id.spinner_font_size);
        btnBold = findViewById(R.id.btnBold);
        btnItalic = findViewById(R.id.btnItalic);
        btnUnderline = findViewById(R.id.btnUnderline);
        btnAlignCenter = findViewById(R.id.btnCenter);
        btnAlignLeft = findViewById(R.id.btnLeft);
        btnAlignRight = findViewById(R.id.btnRight);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.font_sizes, R.layout.spinner_item);
        spinnerFontSizes.setAdapter(adapter);
        txtTimeAndDate.setText(Utility.timeStampToString(Timestamp.now()));

        imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());


        txtNoteContent.setEditorFontColor(Color.WHITE);
        txtNoteContent.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        //EDITOR TOGGLE FUNCTIONS
        btnItalic.setOnClickListener(v -> {
            txtNoteContent.setItalic();
        });
        btnBold.setOnClickListener(v -> {
            txtNoteContent.setBold();
        });
        btnUnderline.setOnClickListener(v->{
            txtNoteContent.setUnderline();
        });
        btnAlignRight.setOnClickListener(v -> {
            txtNoteContent.setAlignRight();
        });
        btnAlignCenter.setOnClickListener(v -> {
            txtNoteContent.setAlignCenter();
        });
        btnAlignCenter.setOnClickListener(v -> {
            txtNoteContent.setAlignCenter();
        });

        imgSaveNote.setOnClickListener(v -> saveNote());
    }

    private void saveNote(){
        String noteTitle = txtTitle.getText().toString();
        String noteSubtitle = txtSubtitle.getText().toString();
        String noteContent = txtNoteContent.getHtml();

        if(noteTitle.isEmpty()){
            txtTitle.setError("Title is required.");
            return;
        }
        Notes note = new Notes();
        note.setTitle(noteTitle);
        note.setSubtitle(noteSubtitle);
        note.setContent(noteContent);
        note.setTimeDate(Utility.timeStampToString(Timestamp.now()));

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