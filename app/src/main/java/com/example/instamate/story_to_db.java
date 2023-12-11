package com.example.instamate;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.instamate.Models.Story_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Date;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;
import omari.hamza.storyview.utils.DynamicImageView;

public class story_to_db extends AppCompatActivity {

    DynamicImageView imageView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_to_db);
//
//imageView=findViewById(R.id.dynamic);
//imageView.


//        Intent intent= getIntent();
//        String username=intent.getStringExtra("username");
//        String userpic=intent.getStringExtra("userpic");
//        Log.d(TAG, "onCreate: "+userpic);
//        Log.d(TAG, "onCreate: "+username);
//
//        ArrayList<String> reelid=intent.getStringArrayListExtra("reelid");
//
//        ArrayList<String> reels_url=intent.getStringArrayListExtra("reelurl");
//
//        ArrayList<MyStory> reel_url=new ArrayList<>();
//        Log.d(TAG, "onCreate: "+reelid.get(0));
//        Log.d(TAG, "onCreate: "+reels_url.get(0));
//
//        for(int i=0;i<reels_url.size();i++) {
//            reel_url.add(new MyStory(reels_url.get(i)));
//            Log.d(TAG, "onCreate: "+reels_url.get(i));
//
//
//        }
//
//
//
//
//
//
//
//
//
//
//        if (reel_url.size() == reelid.size()) {
//            Log.d(TAG, "onCreate: "+"jllkjohj");
//            StoryView.Builder builder = new StoryView.Builder(getSupportFragmentManager())
//                    .setStoriesList(reel_url)
//                    .setStoryDuration(15000)
//                    .setTitleText(username)
//                    .setTitleLogoUrl(userpic)
//                    .setStoryClickListeners(new StoryClickListeners() {
//                        @Override
//                        public void onDescriptionClickListener(int position) {
//                            // your action
//                        }
//
//                        @Override
//                        public void onTitleIconClickListener(int position) {
//                            // your action
//                        }
//                    })
//                    .build();
//
//
//            // Must be called before calling show method
//            builder.show();
//        }
//    }
//
//
//


    }
}



