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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateEvent extends AppCompatActivity implements View.OnClickListener {

    String eventName, eventAdmin;
    Button btnDatePicker, btnTimePicker, updateEvent, imageUpload, deleteEvent;
    EditText eventTitle, eventDesc, eventEmail, eventPhn, txtDate, txtTime;
    Spinner eventDept;
    ImageView eventImg;
    TextView uploadImgText;
    ProgressDialog progressDialog;
    ArrayAdapter<CharSequence> adapter;
    Uri imageUri;
    StorageReference storageReference;
    JSONObject eventObj;
    String image_name = "";
    private DatabaseReference mDatabase;
    private String event_dept, event_dept_selection;
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_event);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventName = (String) extras.get("eventName");
            eventAdmin = (String) extras.get("eventAdmin");
            Log.d("Update event", eventName + "---" + eventAdmin);
        }

        eventTitle = (EditText) findViewById(R.id.update_event_title);
        eventDesc = (EditText) findViewById(R.id.update_event_description);
        eventEmail = (EditText) findViewById(R.id.update_event_email);
        eventPhn = (EditText) findViewById(R.id.update_event_contact);
        btnDatePicker = (Button) findViewById(R.id.update_btn_date);
        btnTimePicker = (Button) findViewById(R.id.update_btn_time);
        updateEvent = (Button) findViewById(R.id.update_btn_event);
        deleteEvent = (Button) findViewById(R.id.delete_btn_event);
        imageUpload = (Button) findViewById(R.id.update_event_img_upload);
        uploadImgText = (TextView) findViewById(R.id.update_upload_img_text);
        txtDate = (EditText) findViewById(R.id.update_in_date);
        txtTime = (EditText) findViewById(R.id.update_in_time);
        eventImg = (ImageView) findViewById(R.id.update_event_img);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);

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
                        txtTime.setText(eventObj.get("time").toString());
                        txtDate.setText(eventObj.get("date").toString());
                        uploadImgText.setText(eventObj.get("image_name").toString());

                        int spinnerPosition = adapter.getPosition(eventObj.get("dept").toString());
                        eventDept.setSelection(spinnerPosition);

                        storageReference = FirebaseStorage.getInstance().getReference("event_images/" + eventObj.get("image_name").toString());
                        Glide.with(UpdateEvent.this)
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

        eventDept = (Spinner) findViewById(R.id.update_event_department);
        adapter = ArrayAdapter.createFromResource(this,
                R.array.dept_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventDept.setAdapter(adapter);
        eventDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(UpdateEvent.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                event_dept = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(UpdateEvent.this, "Nothing selected", Toast.LENGTH_SHORT).show();
            }
        });

        updateEvent.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!eventTitle.getText().toString().isEmpty() && !eventDesc.getText().toString().isEmpty() &&
                        !txtDate.getText().toString().isEmpty() && !txtTime.getText().toString().isEmpty() &&
                        !event_dept.isEmpty() && !eventPhn.getText().toString().isEmpty() &&
                        !eventEmail.getText().toString().isEmpty()) {

                    progressDialog = new ProgressDialog(UpdateEvent.this);
                    progressDialog.setTitle("Updating Event...");
                    progressDialog.show();

                    if (image_name.isEmpty()) {
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
                            eventAdmin);

                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    storageReference = FirebaseStorage.getInstance().getReference("event_images/" + image_name);

                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("Events/" + eventAdmin + "/" + eventName, newEventObj);
                    mDatabase.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(UpdateEvent.this, "Event " + event_dept + " has been successfully updated.", Toast.LENGTH_LONG).show();

                            storageReference.putFile(imageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(UpdateEvent.this, "Image Successfully Uploaded", Toast.LENGTH_SHORT).show();
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            Toast.makeText(UpdateEvent.this, "Failed to Upload Image", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    });
                } else {
                    Toast.makeText(UpdateEvent.this, "Please provide valid input in all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase = FirebaseDatabase.getInstance().getReference("Events/" + eventAdmin + "/" + eventName);
                finish();
                mDatabase.removeValue();
            }
        });

        imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imgUpload = new Intent();
                imgUpload.setType("image/*");
                imgUpload.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(imgUpload, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null) {
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
