package com.example.instamate.Adapters;

import android.content.Context;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instamate.Profile_DataHolder;
import com.example.instamate.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Story_Views_Adapter extends RecyclerView.Adapter<Story_Views_Adapter.myholder> {
Context context;
ArrayList<String> urls;

    public Story_Views_Adapter(Context context, ArrayList<String> urls) {
        this.context=context;
        this.urls=urls;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.story_singleuser_view,parent,false);
        return new myholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {
        String url = urls.get(position);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(url);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Profile_DataHolder profileDataHolder= snapshot.getValue(Profile_DataHolder.class);

                holder.textView.setText(profileDataHolder.getUsername());
                Glide.with(context).load(profileDataHolder.getProfile_pic_url())
                        .centerCrop()
                        .into(holder.imageView);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    class myholder extends RecyclerView.ViewHolder{
ImageView imageView;
TextView textView;


        public myholder(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.story_single_user_pic);
            textView=itemView.findViewById(R.id.story_single_username);
        }
    }
}
