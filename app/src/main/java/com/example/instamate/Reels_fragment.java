package com.example.instamate;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instamate.Adapters.ShowAllReels;
import com.example.instamate.Models.Post_Model;
import com.example.instamate.Models.Reel_Model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Reels_fragment extends Fragment {
ViewPager2 reel_holder;
ShowAllReels showAllReels;

List<String> videourls = new ArrayList<>();
    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_reels_fragment, container, false);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_s);
        reel_holder=view.findViewById(R.id.reelviewpager);
        showAllReels= new ShowAllReels(getContext(),videourls);
        reel_holder.setAdapter(showAllReels);
        DatabaseReference reelsRef = FirebaseDatabase.getInstance().getReference("Reels");

        reelsRef.orderByKey().addValueEventListener(new ValueEventListener() {
                                           @Override
                                           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                               List<String> newReelsId = new ArrayList<>(); // Create a new list to store updated data

                                               for (DataSnapshot uniqueIdSnapshot : dataSnapshot.getChildren()) {
                                                   // Iterate through each unique ID under "Reels"
                                                   String uniqueId = uniqueIdSnapshot.getKey();

                                                   // Assuming you have a "reelsurl" property under each unique ID
//                                                   String url = uniqueIdSnapshot.child("reel_url").getValue(String.class);
//                                                   Log.d("wertyuioplkjhgdszxcvbnm,", url);


                                                   // Add the URL to the list
                                                   newReelsId.add(uniqueId);
                                               }
                                               Collections.reverse(newReelsId);
                                               updateAdapterData(newReelsId);

                                           }
                                               public void onCancelled(@NonNull  DatabaseError databaseError) {
                                                   // Handle errors if any
                                                   Log.d("xcvbnmkjh","error h bhai ");

                                               }


                                           });

//        FirebaseRecyclerOptions<Reel_Model> options
//                = new FirebaseRecyclerOptions.Builder<Reel_Model>()
//                .setQuery(FirebaseDatabase.getInstance().getReference("Reels"), Reel_Model.class)
//                .build();

//        reel_holder




        return view;
    }
    private void updateAdapterData(List<String> newReelsId) {
        if (showAllReels != null) {
            showAllReels.updateData(newReelsId);
        }
    }
//    public void onStart()
//    {
//        super.onStart();
//        showAllReels.startListening();
//    }
//
//
//    @Override
//    public void onStop()
//    {
//        super.onStop();
//        showAllReels.stopListening();
//    }



}