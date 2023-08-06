package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText editTextNote;
    private LinearLayout layoutNotes;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("MyNotes", MODE_PRIVATE);

        editTextNote = findViewById(R.id.editTextNote);
        layoutNotes = findViewById(R.id.layoutNotes);

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveNote();
            }
        });

        Button buttonClearAll = findViewById(R.id.buttonClearAll);
        buttonClearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllNotes();
            }
        });

        loadNotesFromSharedPreferences();
    }

    private void saveNote() {
        String noteText = editTextNote.getText().toString().trim();
        if (!noteText.isEmpty()) {
            addNewNoteView(noteText);
            editTextNote.setText("");
            saveNoteToSharedPreferences(noteText);
        }
    }

    private void saveNoteToSharedPreferences(String noteText) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = "Note_" + System.currentTimeMillis();
        editor.putString(key, noteText);
        editor.apply();
    }

    private void loadNotesFromSharedPreferences() {
        Map<String, ?> allEntries = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            if (entry.getKey().startsWith("Note_")) {
                String noteText = entry.getValue().toString();
                addNewNoteView(noteText);
            }
        }
    }

    private void addNewNoteView(String noteText) {
        TextView textViewNote = new TextView(this);
        textViewNote.setText(noteText);
        textViewNote.setTextSize(18);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        int margin = getResources().getDimensionPixelSize(R.dimen.note_margin);
        layoutParams.setMargins(0, margin, 0, 0);

        textViewNote.setLayoutParams(layoutParams);

        layoutNotes.addView(textViewNote);
    }

    private void clearAllNotes() {
        layoutNotes.removeAllViews();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}