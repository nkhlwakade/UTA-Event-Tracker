package com.example.eventtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseUser;

public class OrganizerDashboard extends AppCompatActivity {

    CardView createEvent, updateEvent;
    FirebaseUser orgUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_dashboard);

        createEvent = (CardView) findViewById(R.id.create_event_card);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orgUser = (FirebaseUser) extras.get("orgUser");
            Log.d("Organizer Dashboard Extras", ""+orgUser.getEmail());
        }

        createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), OrganizerEventOptions.class);
                i.putExtra("orgUser",orgUser);
                startActivity(i);
            }
        });
    }
}
