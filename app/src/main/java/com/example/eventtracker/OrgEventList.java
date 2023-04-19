package com.example.eventtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
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

    private ArrayList<Event> eList;
    private DatabaseReference mDatabase;

    FirebaseUser orgUser;
    String user;

    TextView eventListingHeading;

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

            if (extras.containsKey("orgInfoPage")){
                user = "orgInfoPage";
                Log.d("Organizer Event Opt Extras", ""+user);
            }

        }

        eventListingHeading = (TextView) findViewById(R.id.event_listing_heading);
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
                    if((user.equals("organizer") && adminName.equals(orgUser.getEmail().split("@")[0])) ||
                        (user.equals("orgInfoPage") && adminName.equals(extras.get("eventAdmin")))){
                        try {
                            Iterator<String> eventNames = ((JSONObject)response.get(adminName)).keys();
                            while(eventNames.hasNext()){
                                String eventTitle = eventNames.next();
                                String eventDesc = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("desc");
                                String eventDate = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("date");
                                String eventTime = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("time");
                                String eventDept = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("dept");
                                String eventPhn = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("phn");
                                String eventEmail = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("email");
                                String eventImg = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("image_name");
                                eList.add(new Event(eventTitle, eventDesc, eventDate, eventTime, eventDept, eventPhn, eventEmail, eventImg, adminName));
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    } else if(user.equals("student")) {
                        try {
                            Iterator<String> eventNames = ((JSONObject)response.get(adminName)).keys();
                            while(eventNames.hasNext()){
                                String eventTitle = eventNames.next();
                                String eventDesc = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("desc");
                                String eventDate = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("date");
                                String eventTime = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("time");
                                String eventDept = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("dept");
                                String eventPhn = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("phn");
                                String eventEmail = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("email");
                                String eventImg = (String) ((JSONObject)((JSONObject)response.get(adminName)).get(eventTitle)).get("image_name");
                                eList.add(new Event(eventTitle, eventDesc, eventDate, eventTime, eventDept, eventPhn, eventEmail, eventImg, adminName));
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

        if (user.equals("orgInfoPage")){
            eventListingHeading.setText(extras.get("deptName").toString()+": Event List");
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Event event = eList.get(position);
        if (user.equals("organizer")){
            Intent updateEvent = new Intent(OrgEventList.this, UpdateEvent.class);
            updateEvent.putExtra("eventName",event.getTitle());
            updateEvent.putExtra("eventAdmin",event.getAdminName());
            startActivity(updateEvent);
        }

        if(user.equals("student")){
            Intent eventDetails = new Intent(OrgEventList.this, EventDetails.class);
            eventDetails.putExtra("eventName",event.getTitle());
            eventDetails.putExtra("eventAdmin",event.getAdminName());
            eventDetails.putExtra("stdUser",orgUser);
            startActivity(eventDetails);
        }
    }
}
