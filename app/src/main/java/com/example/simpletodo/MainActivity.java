package com.example.simpletodo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TEXT = "item";
    public static final String KEY_POS = "item position";
    public static final int EDIT_CODE = 28;
    List<String> itemsList;
    Button button;
    EditText edit;
    RecyclerView rvItems;
    Adapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.addButton);
        edit = findViewById(R.id.editItem);
        rvItems = findViewById(R.id.rvItems);

       loadData();

        Adapter.OnLongClick onLongClick = new Adapter.OnLongClick() {
            @Override
            public void RemoveClicked(int position) {
                //remove item
                itemsList.remove(position);
                //notify adapter that item has been removed
                itemsAdapter.notifyItemRemoved(position);
                Toast.makeText(getApplicationContext(), "Item removed!", Toast.LENGTH_SHORT).show();
                saveData();
            }
        };

        Adapter.OnClick onClick = new Adapter.OnClick() {
            @Override
            public void EditClicked(int position) {
                //create new intent to open activity
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                //pass data being edited
                intent.putExtra(KEY_TEXT, itemsList.get(position));
                intent.putExtra(KEY_POS, position);
                //display activity
                startActivityForResult(intent, EDIT_CODE);
            }
        };

        itemsAdapter = new Adapter(itemsList, onLongClick, onClick);
        rvItems.setAdapter(itemsAdapter);
        rvItems.setLayoutManager(new LinearLayoutManager(this));

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newItem = edit.getText().toString();
                //adding new item to list
                itemsList.add(newItem);
                //notify adapter that item has been added
                itemsAdapter.notifyItemInserted(itemsList.size() - 1);
                //clearing EditText view after adding new item
                edit.setText("");
                Toast.makeText(getApplicationContext(), "Item added!", Toast.LENGTH_SHORT).show();
                saveData();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_CODE && resultCode == RESULT_OK){
            //retrieve updated text
            String editedText = data.getStringExtra(KEY_TEXT);
            //get position of edited item
            int editedPos = data.getExtras().getInt(KEY_POS);
            //update the text at received position
            itemsList.set(editedPos, editedText);
            //notify adapter of item change
            itemsAdapter.notifyItemChanged(editedPos);
            //persistence
            saveData();
            Toast.makeText(getApplicationContext(), "Item updated!", Toast.LENGTH_SHORT).show();

        }else{
            Log.w("MainActivity", "Warning!");
        }
    }

    private File getDataFile(){
        return new File(getFilesDir(), "items.txt");
    }

    //this function will read from file and load data
    private void loadData(){
        try {
            itemsList = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity", "Error reading data", e);
            itemsList = new ArrayList<>();
        }
    }

    //this function will write data to file
    private void saveData(){
        try {
            FileUtils.writeLines(getDataFile(), itemsList);
        } catch (IOException e) {
            Log.e("MainActivity", "Error writing data", e);
        }
    }
}