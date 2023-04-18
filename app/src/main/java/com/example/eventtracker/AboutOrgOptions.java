package com.example.eventtracker;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;

public class AboutOrgOptions extends AppCompatActivity {

    FirebaseUser stdUser;
    String eventName, eventAdmin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_info);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stdUser = (FirebaseUser) extras.get("stdUser");
            eventName = (String) extras.get("eventName");
            eventAdmin = (String) extras.get("eventAdmin");
            Log.d("About Org Extras", ""+stdUser.getEmail()+"---"+eventName+"---"+eventAdmin);
        }
    }
}
