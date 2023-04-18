package com.example.eventtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseUser;

public class StudentDashboard extends AppCompatActivity {

    CardView viewAllEvents, updateEvent;
    FirebaseUser stdUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_dashboard);

        viewAllEvents = (CardView) findViewById(R.id.view_all_events_option);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stdUser = (FirebaseUser) extras.get("stdUser");
            Log.d("Student Dashboard Extras", ""+stdUser.getEmail());
        }

        viewAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OrgEventList.class);
                i.putExtra("stdUser",stdUser);
                startActivity(i);
            }
        });
    }
}
