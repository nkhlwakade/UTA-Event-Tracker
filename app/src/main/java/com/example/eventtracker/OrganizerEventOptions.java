package com.example.eventtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseUser;

public class OrganizerEventOptions extends AppCompatActivity {

    CardView addEvent, updateEvent;
    FirebaseUser orgUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_event_options);

        addEvent = (CardView) findViewById(R.id.add_event_card);
        updateEvent = (CardView) findViewById(R.id.update_event_option);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orgUser = (FirebaseUser) extras.get("orgUser");
            Log.d("Organizer Event Opt Extras", ""+orgUser.getEmail());
        }

        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addEvent = new Intent(getApplicationContext(), AddEvent.class);
                addEvent.putExtra("orgUser",orgUser);
                startActivity(addEvent);
            }
        });

        updateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent updateEvent = new Intent(getApplicationContext(), OrgEventList.class);
                updateEvent.putExtra("orgUser",orgUser);
                startActivity(updateEvent);
            }
        });
    }
}
