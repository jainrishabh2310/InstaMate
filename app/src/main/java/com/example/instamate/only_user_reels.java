package com.example.instamate;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instamate.Adapters.only_users_reels_grid;
import com.example.instamate.Models.Reel_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


public class only_user_reels extends Fragment {

RecyclerView reels_recview;

ArrayList<Reel_Model> reel_list;

only_users_reels_grid onlyUsersReelsGrid;
DatabaseReference databaseReference;

    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_only_user_reels, container, false);
        reels_recview=view.findViewById(R.id.only_user_reel_recview);

        reel_list= new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false);
        reels_recview.setLayoutManager(layoutManager);
        String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        onlyUsersReelsGrid= new only_users_reels_grid(getContext(),reel_list,

                new only_users_reels_grid.OnItemClickListener(){
                    @Override
                    public void onItemClick(String userId,String reelid, int position){
                        openFragment(userId,reelid,position);


                    }

                });

        reels_recview.setAdapter(onlyUsersReelsGrid);
        databaseReference.child("Users").child(userid).child("Reels").orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    reel_list.clear();
                    for (DataSnapshot values: snapshot.getChildren()) {
                        Reel_Model model=values.getValue(Reel_Model.class);
                        reel_list.add(model);


                    }
                    Collections.reverse(reel_list);
                    onlyUsersReelsGrid.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }

    public void openFragment(String userId,String reelid,int position) {
        only_user_reels_fullview secondFragment = only_user_reels_fullview.newInstance(userId,reelid,position);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, secondFragment)
                .addToBackStack(null)
                .commit();


    }


}