package com.example.eventtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.textfield.TextInputLayout;

public class FILTEREVList extends AppCompatActivity {
    TextInputLayout textInputLayout;
    AutoCompleteTextView autoCompleteTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filterevlist);

        textInputLayout=findViewById(R.id.menu);
        autoCompleteTextView=findViewById(R.id.dropmenu);
        String [] items={"item1","item1","item1","item1"};
        ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(FILTEREVList.this,R.layout.items_list,items);
        autoCompleteTextView.setAdapter(itemAdapter);
    }
}