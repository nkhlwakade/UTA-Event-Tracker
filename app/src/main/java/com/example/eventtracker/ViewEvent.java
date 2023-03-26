package com.example.eventtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ViewEvent extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        button = (Button) findViewById(R.id.viewevent);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewEvent();
            }
        });
    }
    public void openViewEvent(){
        Intent intent = new Intent(this,FILTEREVList.class);
        startActivity(intent);
    }
}