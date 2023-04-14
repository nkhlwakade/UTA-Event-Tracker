package com.example.eventtracker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Objects;

public class AddEvent extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    Button btnDatePicker, btnTimePicker, createEvent;
    EditText eventTitle, eventDesc, eventEmail, eventPhn, txtDate, txtTime;
    Spinner eventDept;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String event_title, event_desc, event_date, event_time, event_dept;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayAdapter<CharSequence> adapter;
    FirebaseUser orgUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orgUser = (FirebaseUser) extras.get("orgUser");
            Log.d("Add Event Extras", ""+orgUser.getEmail());
        }

        eventTitle = (EditText) findViewById(R.id.event_title);
        eventDesc = (EditText) findViewById(R.id.event_description);
        eventEmail = (EditText) findViewById(R.id.event_email);
        eventPhn = (EditText) findViewById(R.id.event_contact);
        btnDatePicker = (Button) findViewById(R.id.btn_date);
        btnTimePicker = (Button) findViewById(R.id.btn_time);
        createEvent = (Button) findViewById(R.id.btn_create_event);
        txtDate = (EditText) findViewById(R.id.in_date);
        txtTime = (EditText) findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

        eventDept = (Spinner) findViewById(R.id.event_department);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.dept_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventDept.setAdapter(adapter);
        eventDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddEvent.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                event_dept = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(AddEvent.this, "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        database = FirebaseDatabase.getInstance();

        // Read from the database
//        myRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "Value is: " + value);
//                Toast.makeText(AddEvent.this, "Firebase Content - " + value, Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!eventTitle.getText().toString().isEmpty() && !eventDesc.getText().toString().isEmpty() &&
                        !txtDate.getText().toString().isEmpty() && !txtTime.getText().toString().isEmpty() &&
                        !event_dept.isEmpty() && !eventPhn.getText().toString().isEmpty() && !eventEmail.getText().toString().isEmpty()) {
                    Event newEventObj = new Event(
                            eventTitle.getText().toString(),
                            eventDesc.getText().toString(),
                            txtDate.getText().toString(),
                            txtTime.getText().toString(),
                            event_dept,
                            eventPhn.getText().toString(),
                            eventEmail.getText().toString());

                    myRef = database.getReference("Events");
                    myRef.child(Objects.requireNonNull(orgUser.getEmail().split("@")[0]))
                            .child(eventTitle.getText().toString()).setValue(newEventObj)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddEvent.this, "New event for " + event_dept + " has been successfully created.", Toast.LENGTH_LONG).show();
                            clearFormFields();
                        }
                    });
                } else {
                    Toast.makeText(AddEvent.this, "Please provide valid input in all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void clearFormFields() {
        eventTitle.getText().clear();
        eventDesc.getText().clear();
        eventDept.setAdapter(adapter);
        txtDate.getText().clear();
        txtTime.getText().clear();
        eventEmail.getText().clear();
        eventPhn.getText().clear();
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {

                            txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            txtTime.setText(hourOfDay + ":" + minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
}
