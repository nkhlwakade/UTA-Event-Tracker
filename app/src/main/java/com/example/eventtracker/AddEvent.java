package com.example.eventtracker;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.Objects;

public class AddEvent extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    Button btnDatePicker, btnTimePicker, createEvent, imageUpload;
    EditText eventTitle, eventDesc, eventEmail, eventPhn, txtDate, txtTime;
    Spinner eventDept;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String event_dept, image_name;
    FirebaseDatabase database;
    DatabaseReference myRef;
    ArrayAdapter<CharSequence> adapter;
    FirebaseUser orgUser;

    Uri imageUri;
    TextView uploadImgText;

    StorageReference storageReference;
    ProgressDialog progressDialog;

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
        imageUpload = (Button) findViewById(R.id.event_img_upload);
        uploadImgText = (TextView) findViewById(R.id.upload_img_text);
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

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgUpload = new Intent();
                imgUpload.setType("image/*");
                imgUpload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imgUpload,100);
            }
        });

        createEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (!eventTitle.getText().toString().isEmpty() && !eventDesc.getText().toString().isEmpty() &&
                        !txtDate.getText().toString().isEmpty() && !txtTime.getText().toString().isEmpty() &&
                        !event_dept.isEmpty() && !eventPhn.getText().toString().isEmpty() &&
                        !eventEmail.getText().toString().isEmpty()) {

                    progressDialog = new ProgressDialog(AddEvent.this);
                    progressDialog.setTitle("Adding new Event...");
                    progressDialog.show();

                    if(image_name.isEmpty()){
                        image_name = "default.jpg";
                    }

                    Event newEventObj = new Event(
                            eventTitle.getText().toString(),
                            eventDesc.getText().toString(),
                            txtDate.getText().toString(),
                            txtTime.getText().toString(),
                            event_dept,
                            eventPhn.getText().toString(),
                            eventEmail.getText().toString(),
                            image_name,
                            orgUser.getEmail().split("@")[0].toString());

                    myRef = database.getReference("Events");
                    storageReference = FirebaseStorage.getInstance().getReference("event_images/"+image_name);

                    myRef.child(Objects.requireNonNull(orgUser.getEmail().split("@")[0]))
                            .child(eventTitle.getText().toString()).setValue(newEventObj)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddEvent.this, "New event for " + event_dept + " has been successfully created.", Toast.LENGTH_LONG).show();
                            clearFormFields();

                            storageReference.putFile(imageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(AddEvent.this,"Image Successfully Uploaded",Toast.LENGTH_SHORT).show();
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            Toast.makeText(AddEvent.this,"Failed to Upload Image",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                } else {
                    Toast.makeText(AddEvent.this, "Please provide valid input in all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            Log.d("Image Name", getFileName(imageUri));
            image_name = getFileName(imageUri);
            uploadImgText.setText(image_name);
        }
    }

    @SuppressLint("Range")
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
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
