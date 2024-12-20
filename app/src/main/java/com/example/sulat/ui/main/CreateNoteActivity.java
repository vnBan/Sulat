package com.example.sulat.ui.main;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

import com.example.sulat.R;
import com.example.sulat.database.Notes;
import com.example.sulat.Utility;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

import jp.wasabeef.richeditor.RichEditor;

public class CreateNoteActivity extends AppCompatActivity {
    EditText txtTitle, txtSubtitle;
    RichEditor txtNoteContent;
    TextView txtTimeAndDate;
    ImageView imgSaveNote, imgBack;
    Spinner spinnerFontSizes;
    String title, subtitle, content, docId;
    BottomAppBar bottomAppBar;
    Button btnItalic, btnBold, btnUnderline, btnIndent, btnOutdent, btnAlignLeft, btnAlignRight, btnAlignCenter, btnBullets, btnDelete, btnNumberBullets;
    boolean isEditing = false;
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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

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
        btnOutdent = findViewById(R.id.btnOutdent);
        btnIndent = findViewById(R.id.btnIndent);
        btnBullets = findViewById(R.id.btnBullets);
        btnNumberBullets = findViewById(R.id.btnNumberBullets);
        btnDelete = findViewById(R.id.btnDelete);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.font_sizes, R.layout.spinner_item);
        spinnerFontSizes.setAdapter(adapter);
        bottomAppBar = findViewById(R.id.bottomAppBar);

        txtTimeAndDate.setText(Utility.timeStampToString(Timestamp.now()));

        imgBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());


        txtNoteContent.setEditorFontColor(Color.WHITE);
        txtNoteContent.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        txtNoteContent.setPlaceholder("Type your note here...");
        //EDITOR TOGGLE FUNCTIONS

        btnBold.setSelected(false);
        btnItalic.setSelected(false);
        btnUnderline.setSelected(false);

        btnItalic.setOnClickListener(v -> {
            txtNoteContent.setItalic();
            btnItalic.setSelected(!btnItalic.isSelected());
        });
        btnBold.setOnClickListener(v -> {
            txtNoteContent.setBold();
            btnBold.setSelected(!btnBold.isSelected());
        });
        btnUnderline.setOnClickListener(v->{
            txtNoteContent.setUnderline();
            btnUnderline.setSelected(!btnUnderline.isSelected());
        });
        btnAlignLeft.setOnClickListener(v -> {
            txtNoteContent.setAlignLeft();
        });
        btnAlignRight.setOnClickListener(v -> {
            txtNoteContent.setAlignRight();
        });
        btnAlignCenter.setOnClickListener(v -> {
            txtNoteContent.setAlignCenter();
        });
        btnOutdent.setOnClickListener(v -> {
            txtNoteContent.setOutdent();
        });
        btnIndent.setOnClickListener(v -> {
            txtNoteContent.setIndent();
        });
        btnBullets.setOnClickListener(v -> {
            txtNoteContent.setBullets();
        });
        btnNumberBullets.setOnClickListener(v -> {
            txtNoteContent.setNumbers();
        });
        spinnerFontSizes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedSize = parent.getItemAtPosition(position).toString();
                txtNoteContent.setFontSize(parseFontSize(selectedSize));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing
            }
        });

        //UPDATE NOTES
        title = getIntent().getStringExtra("title");
        subtitle = getIntent().getStringExtra("subtitle");
        content = getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");
        Log.d(TAG, "Received docId: " + docId);
        if(docId!=null){
            isEditing = true;
        }
        if(isEditing){
            txtTitle.setText(title);
            txtSubtitle.setText(subtitle);
            txtNoteContent.setHtml(content);
        }


        //SAVE & DELETE
        imgSaveNote.setOnClickListener(v -> saveNote());
        btnDelete.setOnClickListener(v-> deleteNote());
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
        if(isEditing){
            documentReference = Utility.getCollectionReferenceNotes().document(docId);
        }else {
            documentReference = Utility.getCollectionReferenceNotes().document();
        }
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
    private void deleteNote(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceNotes().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // note is DELETED
                    Toast.makeText(CreateNoteActivity.this, "Note deleted.", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    Toast.makeText(CreateNoteActivity.this, "Deleting Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private int parseFontSize(String selectedSize) {
        switch (selectedSize){
            case "Small":
                return 12;
            case "Medium":
                return 16;
            case "Large":
                return 20;
            case "Extra Large":
                return 24;
        }
        return 0;
    }

}