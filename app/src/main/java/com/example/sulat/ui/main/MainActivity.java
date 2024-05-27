package com.example.sulat.ui.main;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import  android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.sulat.R;
import com.example.sulat.Utility;
import com.example.sulat.database.Notes;
import com.example.sulat.ui.auth.LoginActivity;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {
    ImageView imageAddNoteMain, imageLogout;
    RecyclerView notesDisplay;
    NotesAdapter notesAdapter;
    EditText search;
    FirebaseAuth fAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //SplashScreen.installSplashScreen(this);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imageAddNoteMain = findViewById(R.id.imageAddNoteMain);
        notesDisplay = findViewById(R.id.notesRecyleView);
        search = findViewById(R.id.inputSearch);
        imageAddNoteMain.setOnClickListener(v -> {
            Intent makeNote = new Intent(getApplicationContext(), CreateNoteActivity.class);
            startActivity(makeNote);
        });

        imageLogout = findViewById(R.id.imageLogout);
        imageLogout.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            finish();
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchNotes(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Do nothing
            }
        });

        setupRecyclerView();
    }

    private void setupRecyclerView(){
        Query query = Utility.getCollectionReferenceNotes().orderBy("timeDate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Notes> options = new FirestoreRecyclerOptions.Builder<Notes>().setQuery(query,Notes.class).build();
        notesDisplay.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        notesAdapter = new NotesAdapter(options, this);
        notesDisplay.setAdapter(notesAdapter);
    }

    private void searchNotes(String searchText) {
        Query query;
        if (searchText.isEmpty()) {
            query = Utility.getCollectionReferenceNotes().orderBy("timeDate", Query.Direction.DESCENDING);
        } else {
            query = Utility.getCollectionReferenceNotes()
                    .orderBy("title")
                    .startAt(searchText)
                    .endAt(searchText + "\uf8ff");
        }
        FirestoreRecyclerOptions<Notes> options = new FirestoreRecyclerOptions.Builder<Notes>()
                .setQuery(query, Notes.class)
                .build();
        notesAdapter.updateOptions(options);
    }

    @Override
    protected void onStart() {
        super.onStart();
        notesAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        notesAdapter.stopListening();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onResume() {
        super.onResume();
        notesAdapter.notifyDataSetChanged();
    }
}