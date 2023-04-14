package com.example.eventtracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    CardView stu_login, org_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        stu_login = (CardView) findViewById(R.id.student_login_card);
        org_login = (CardView) findViewById(R.id.organizer_login_card);

        stu_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stu_intent = new Intent(getApplicationContext(), StudentLogin.class);
                startActivity(stu_intent);
            }
        });

        org_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent org_intent = new Intent(getApplicationContext(), OrganizerLogin.class);
                startActivity(org_intent);
            }
        });
    }
}