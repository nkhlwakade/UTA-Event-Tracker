package com.example.eventtracker;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class OrganizerLogin extends AppCompatActivity {

    Button organizer_login;
    EditText organizer_email, organizer_password;
    private FirebaseAuth orgAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_org);
        organizer_login = (Button) findViewById(R.id.organizer_login_button);
        organizer_email = (EditText) findViewById(R.id.organizer_email);
        organizer_password = (EditText) findViewById(R.id.organizer_password);
        orgAuth = FirebaseAuth.getInstance();

        organizer_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orgAuth.signInWithEmailAndPassword(organizer_email.getText().toString().trim(), organizer_password.getText().toString().trim()).addOnCompleteListener(OrganizerLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Organizer Login", "signInWithEmail:success");
                            FirebaseUser user = orgAuth.getCurrentUser();
                            orgLoginSuccess(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Organizer Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(OrganizerLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void orgLoginSuccess(FirebaseUser user) {
        Toast.makeText(OrganizerLogin.this, user.toString(), Toast.LENGTH_LONG).show();
        Log.d("Organizer Details", ""+user.getEmail());

        Intent i = new Intent(OrganizerLogin.this, OrganizerDashboard.class);
        i.putExtra("orgUser",user);
        startActivity(i);
    }
}
