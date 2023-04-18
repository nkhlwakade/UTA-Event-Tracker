package com.example.eventtracker;

import android.os.Bundle;
import android.util.Log;
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

public class OrgPage extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private StorageReference storageReference;
    FirebaseUser stdUser;
    String deptName, eventAdmin;
    TextView orgDept, orgName, about_1, about_2, about_3, address, contact, website;
    JSONObject orgInfoObj;
    ImageView orgPageImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.org_page);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stdUser = (FirebaseUser) extras.get("stdUser");
            deptName = (String) extras.get("deptName");
            eventAdmin = (String) extras.get("eventAdmin");
            Log.d("Org Page Extras", ""+stdUser.getEmail()+"---"+deptName+"---"+eventAdmin);
        }

        orgDept = (TextView) findViewById(R.id.org_page_title);
        orgName = (TextView) findViewById(R.id.about_org_name_value);
        about_1 = (TextView) findViewById(R.id.org_page_about_value_1);
        about_2 = (TextView) findViewById(R.id.org_page_about_value_2);
        about_3 = (TextView) findViewById(R.id.org_page_about_value_3);
        address = (TextView) findViewById(R.id.about_org_address_value);
        contact = (TextView) findViewById(R.id.about_org_phn_value);
        website = (TextView) findViewById(R.id.about_org_website_value);
        orgPageImg = (ImageView) findViewById(R.id.org_page_img);

        mDatabase = FirebaseDatabase.getInstance().getReference("Organizers").child(deptName);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, String> orgInfo = (Map<String, String>) dataSnapshot.getValue();
                Log.d("Org Page", "Org Info : " + orgInfo);

                if (orgInfo == null) {
                    finish();
                } else {
                    orgInfoObj = new JSONObject(orgInfo);
                    orgName.setText(eventAdmin+"@uta.edu");
                    try {
                        orgDept.setText("UTA: "+deptName);
                        about_1.setText(orgInfoObj.get("about_1").toString());
                        about_2.setText(orgInfoObj.get("about_2").toString());
                        about_3.setText(orgInfoObj.get("about_3").toString());
                        address.setText(orgInfoObj.get("address").toString());
                        contact.setText(orgInfoObj.get("phone").toString());
                        website.setText(orgInfoObj.get("url").toString());

                        storageReference = FirebaseStorage.getInstance().getReference("event_images/" + orgInfoObj.get("image_name").toString());
                        Glide.with(OrgPage.this)
                                .load(storageReference)
                                .into(orgPageImg);
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

    }
}
