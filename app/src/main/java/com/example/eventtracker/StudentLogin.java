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

public class StudentLogin extends AppCompatActivity {

    Button student_login;
    EditText student_email, student_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_stu);
        student_login = (Button) findViewById(R.id.student_login_button);
        student_email = (EditText) findViewById(R.id.student_email);
        student_password = (EditText) findViewById(R.id.student_password);
        mAuth = FirebaseAuth.getInstance();

        student_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(student_email.getText().toString().trim(), student_password.getText().toString().trim())
                        .addOnCompleteListener(StudentLogin.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Student Login", "signInWithEmail:success");
                            FirebaseUser stdUser = mAuth.getCurrentUser();
                            loginSuccess(stdUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Student Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(StudentLogin.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void loginSuccess(FirebaseUser stdUser) {
        Toast.makeText(StudentLogin.this, stdUser.toString(), Toast.LENGTH_LONG).show();
        Log.d("Student Details", ""+stdUser.getEmail());

        Intent i = new Intent(StudentLogin.this, StudentDashboard.class);
        i.putExtra("stdUser",stdUser);
        startActivity(i);
    }
}
