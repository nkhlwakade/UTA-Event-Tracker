package com.example.eventtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class OrgEventList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<EventList> eList;
    private DatabaseReference mDatabase;

    FirebaseUser orgUser;
    String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("orgUser")){
                user = "organizer";
                orgUser = (FirebaseUser) extras.get("orgUser");
                Log.d("Organizer Event Opt Extras", ""+orgUser.getEmail());
            }

            if (extras.containsKey("stdUser")){
                user = "student";
                orgUser = (FirebaseUser) extras.get("stdUser");
                Log.d("Organizer Event Opt Extras", ""+orgUser.getEmail());
            }

        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Events");
        ListView listView = findViewById(R.id.custom_list_view);

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> value = (Map<String, String>) dataSnapshot.getValue();
                Log.d("OrgEventList", "Value is: " + value);

                eList = new ArrayList<>();

                JSONObject response = new JSONObject(value);
                Iterator<String> adminList = response.keys();

                while(adminList.hasNext()){
                    String adminName = adminList.next();
                    Log.d("OrgEventList", "event admins: " + adminName);
                    if(user.equals("organizer") && adminName.equals(orgUser.getEmail().split("@")[0])){
                        try {
                            Iterator<String> eventNames = ((JSONObject)response.get(adminName)).keys();
                            while(eventNames.hasNext()){
                                String eventName = eventNames.next();
                                String eventDesc = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventName)).get("desc");
                                eList.add(new EventList(eventName, eventDesc, adminName));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else if(user.equals("student")) {
                        try {
                            Iterator<String> eventNames = ((JSONObject)response.get(adminName)).keys();
                            while(eventNames.hasNext()){
                                String eventName = eventNames.next();
                                String eventDesc = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventName)).get("desc");
                                eList.add(new EventList(eventName, eventDesc, adminName));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                CustomAdapter customAdapter = new CustomAdapter(OrgEventList.this, eList);
                listView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("OrgEventList", "Failed to read value.", error.toException());
            }
        });
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        EventList event = eList.get(position);
        if (user.equals("organizer")){
            Intent updateEvent = new Intent(OrgEventList.this, UpdateEvent.class);
            updateEvent.putExtra("eventName",event.getEventTitle());
            updateEvent.putExtra("eventAdmin",event.getAdminName());
            startActivity(updateEvent);
        }

        if(user.equals("student")){
            Intent eventDetails = new Intent(OrgEventList.this, EventDetails.class);
            eventDetails.putExtra("eventName",event.getEventTitle());
            eventDetails.putExtra("eventAdmin",event.getAdminName());
            eventDetails.putExtra("stdUser",orgUser);
            startActivity(eventDetails);
        }
    }
}
