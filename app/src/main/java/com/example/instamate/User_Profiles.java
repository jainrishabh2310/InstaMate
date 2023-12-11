package com.example.instamate;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link User_Profiles#newInstance} factory method to
 * create an instance of this fragment.
 */
public class User_Profiles extends Fragment {

CircleImageView user_profile_userpic;
TextView user_profile_username;
TextView user_profile_userbio;
TextView post_count;
TextView reel_count;
AppCompatButton message_btn;

    public User_Profiles() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static User_Profiles newInstance(String userid) {
        User_Profiles fragment = new User_Profiles();
        Bundle args = new Bundle();

        args.putString("userId", userid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_user__profiles, container, false);
        String userid = getArguments().getString("userId");
        user_profile_userpic = view.findViewById(R.id.users_profile_userpic);
        user_profile_username=view.findViewById(R.id.users_profile_username);
        user_profile_userbio = view.findViewById(R.id.users_profile_userbio);
        post_count=view.findViewById(R.id.users_profile_all_post_count);
        reel_count=view.findViewById(R.id.users_profile_all_reels_count);
        message_btn=view.findViewById(R.id.user_profile_message_btn);




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile_DataHolder model = snapshot.getValue(Profile_DataHolder.class);
                Glide.with(getContext()).load(model.getProfile_pic_url()).centerCrop().into(user_profile_userpic);
                user_profile_username.setText(model.getUsername().toString());
                user_profile_userbio.setText(model.getBio().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    post_count.setText((int) snapshot.getChildrenCount()+"");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        reference.child("Reels").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    reel_count.setText((int) snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        message_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(),Chatting_Users.class);
                intent.putExtra("Uid",userid);
                startActivity(intent);
            }
        });







        return view;
    }
}