package com.example.simpletodo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    Button saveButton;
    EditText edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        saveButton = findViewById(R.id.saveButton);
        edit = findViewById(R.id.editText);

        getSupportActionBar().setTitle("Edit item");
        edit.setText(getIntent().getStringExtra(MainActivity.KEY_TEXT));

        //after clicking, return back
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an intent to handle results
                Intent result = new Intent();
                //pass edited data
                result.putExtra(MainActivity.KEY_TEXT, edit.getText().toString());
                result.putExtra(MainActivity.KEY_POS, getIntent().getExtras().getInt(MainActivity.KEY_POS));
                //set result of intent
                setResult(RESULT_OK, result);
                //finish activity and go back to original screen
                finish();
            }
        });
    }
}