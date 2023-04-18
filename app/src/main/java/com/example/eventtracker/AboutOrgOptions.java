package com.example.eventtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseUser;

public class AboutOrgOptions extends AppCompatActivity {

    FirebaseUser stdUser;
    String deptName, eventAdmin;

    CardView orgPage, orgEventList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_info);

        orgPage = (CardView) findViewById(R.id.about_org_card);
        orgEventList = (CardView) findViewById(R.id.org_listing_option);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            stdUser = (FirebaseUser) extras.get("stdUser");
            deptName = (String) extras.get("deptName");
            eventAdmin = (String) extras.get("eventAdmin");
            Log.d("About Org Extras", ""+stdUser.getEmail()+"---"+deptName+"---"+eventAdmin);
        }

        orgPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orgPage = new Intent(AboutOrgOptions.this, OrgPage.class);
                orgPage.putExtra("deptName",deptName);
                orgPage.putExtra("eventAdmin",eventAdmin);
                orgPage.putExtra("stdUser",stdUser);
                startActivity(orgPage);
            }
        });

        orgEventList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orgEventPage = new Intent(AboutOrgOptions.this, OrgEventList.class);
                orgEventPage.putExtra("orgInfoPage","orgInfoPage");
                orgEventPage.putExtra("eventAdmin",eventAdmin);
                orgEventPage.putExtra("deptName",deptName);
                startActivity(orgEventPage);
            }
        });
    }
}
