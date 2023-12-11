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

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Splash_Screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        TextView textView= findViewById(R.id.insta_logo);
        Shader shader= new LinearGradient(
                0,0,0,textView.getLineHeight(),
                new int[]{
                        Color.parseColor("#02bfcc"),
                         Color.parseColor("#7d2ae6")


                }, null,Shader.TileMode.REPEAT
                );
        textView.getPaint().setShader(shader);
//        TextView textView1= findViewById(R.id.insta_logo1);
//
//        textView1.getPaint().setShader(shader);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Splash_Screen.this,Email_Verification.class));
                finish();

            }
        },3000);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }
}