package com.example.eventtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class EventDetails extends AppCompatActivity {
    FirebaseUser stdUser;
    String eventName, eventAdmin, eventDeptName;
    private DatabaseReference mDatabase;
    JSONObject eventObj;
    TextView eventTitle, eventDesc, eventDept, eventDate, eventTime, eventPhn, eventEmail;
    ImageView eventImg;
    StorageReference storageReference;
    Button orgInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        eventTitle = (TextView) findViewById(R.id.event_details_title);
        eventDesc = (TextView) findViewById(R.id.txt_event_desc_value);
        eventDept = (TextView) findViewById(R.id.event_details_dept);
        eventDate = (TextView) findViewById(R.id.txt_date_value);
        eventTime = (TextView) findViewById(R.id.txt_time_value);
        eventEmail = (TextView) findViewById(R.id.txt_email_value);
        eventPhn = (TextView) findViewById(R.id.txt_phn_value);
        eventImg = (ImageView) findViewById(R.id.event_details_img);
        orgInfo = (Button) findViewById(R.id.btn_org_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stdUser = (FirebaseUser) extras.get("stdUser");
            eventName = (String) extras.get("eventName");
            eventAdmin = (String) extras.get("eventAdmin");
            Log.d("Student Dashboard Extras", ""+stdUser.getEmail()+"---"+eventName+"---"+eventAdmin);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference("Events").child(eventAdmin).child(eventName);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> eventData = (Map<String, String>) dataSnapshot.getValue();
                Log.d("Update event", "Event to be updated is: " + eventData);

                if (eventData == null) {
                    finish();
                } else {
                    eventObj = new JSONObject(eventData);
                    eventTitle.setText(eventName);
                    try {
                        eventDesc.setText(eventObj.get("desc").toString());
                        eventEmail.setText(eventObj.get("email").toString());
                        eventPhn.setText(eventObj.get("phn").toString());
                        eventTime.setText(eventObj.get("time").toString());
                        eventDate.setText(eventObj.get("date").toString());
                        eventDept.setText(eventObj.get("dept").toString());
                        eventDeptName = eventObj.get("dept").toString();

                        storageReference = FirebaseStorage.getInstance().getReference("event_images/" + eventObj.get("image_name").toString());
                        Glide.with(EventDetails.this)
                                .load(storageReference)
                                .into(eventImg);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Update event", "Failed to read value.", error.toException());
            }
        });

        orgInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orgInfo = new Intent(EventDetails.this, AboutOrgOptions.class);
                orgInfo.putExtra("deptName", eventDeptName);
                orgInfo.putExtra("eventAdmin",eventAdmin);
                orgInfo.putExtra("stdUser",stdUser);
                startActivity(orgInfo);
            }
        });
    }
}
