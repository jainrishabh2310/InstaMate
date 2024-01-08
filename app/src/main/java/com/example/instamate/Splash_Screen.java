package com.example.instamate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.google.android.gms.common.util.AndroidUtilsLight;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);



        TextView textView = findViewById(R.id.insta_logo);
        Shader shader = new LinearGradient(
                0, 0, 0, textView.getLineHeight(),
                new int[]{
                        Color.parseColor("#02bfcc"),
                        Color.parseColor("#7d2ae6")


                }, null, Shader.TileMode.REPEAT
        );
        textView.getPaint().setShader(shader);
//        TextView textView1= findViewById(R.id.insta_logo1);
//
//        textView1.getPaint().setShader(shader);


        if (getIntent().getExtras() != null) {

            String userid=getIntent().getExtras().getString("Uid");

            FirebaseDatabase.getInstance().getReference("Users")
                    .child(userid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                Profile_DataHolder dataHolder= snapshot.getValue(Profile_DataHolder.class);
                                Intent intent= new Intent(getApplicationContext(), Chatting_Users.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(Splash_Screen.this, Email_Verification.class));
                    finish();

                }
            }, 3000);

        }

    }
    @Override
    protected void onStart() {
        super.onStart();
//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}