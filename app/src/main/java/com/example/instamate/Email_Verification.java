package com.example.instamate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Email_Verification extends AppCompatActivity {
    TextInputEditText email,pass;
    private FirebaseAuth mAuth;

    AppCompatButton verify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_email_verification);
        email = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        verify = findViewById(R.id.verify);
        mAuth = FirebaseAuth.getInstance();

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = email.getText().toString();
                String password = pass.getText().toString();

                if (mAuth != null) {
                    mAuth.createUserWithEmailAndPassword(username, password)
                            .addOnCompleteListener(Email_Verification.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Registration success
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        // Send verification email
                                        sendEmailVerification(user);
                                    } else {
                                        // If registration fails, try logging in
                                        mAuth.signInWithEmailAndPassword(username, password)
                                                .addOnCompleteListener(Email_Verification.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            // Sign in success
                                                            FirebaseUser user = mAuth.getCurrentUser();

                                                            // Check if the email is verified
                                                            if (user != null && user.isEmailVerified()) {
                                                                // Email is verified, move to the dashboard
                                                                moveToDashboard();
                                                            } else {
                                                                // Email is not verified, show a message or handle accordingly
                                                                Toast.makeText(Email_Verification.this, "Please verify your email.", Toast.LENGTH_LONG).show();
                                                            }
                                                        } else {
                                                            // If sign in fails, display a message to the user.
                                                            Toast.makeText(Email_Verification.this, "Login failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });
                } else {
                    // Log an error or handle it appropriately
                    Toast.makeText(Email_Verification.this, "Authentication not initialized.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void moveToDashboard() {
        // Implement the logic to move to the dashboard activity
        // For example, you can start a new activity or open a new fragment
        Intent intent = new Intent(Email_Verification.this, Registration_users.class);
        startActivity(intent);
        finish(); // Optional: Close the current activity
    }
    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Email_Verification.this,
                                    "Verification email sent to " + user.getEmail()+user.getUid(),
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Email_Verification.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    protected void onStart() {
        super.onStart();
        FirebaseAuth mauth= FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();

            try {
                if (mauth.getUid() != null) {
                    FirebaseDatabase.getInstance().getReference("Users").child(mauth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                startActivity(new Intent(Email_Verification.this, DashBoard.class));
                                finish();
                            }
                            else{
                                startActivity(new Intent(Email_Verification.this, Registration_users.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            } catch (Exception e) {

            }

//        Log.d("ABCD",mauth.getUid());

    }

}