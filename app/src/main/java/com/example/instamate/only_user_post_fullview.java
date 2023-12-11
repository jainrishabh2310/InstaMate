package com.example.instamate;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instamate.Models.Post_Model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link only_user_post_fullview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class only_user_post_fullview extends Fragment {
    private RecyclerView postsRecyclerView;
    private DatabaseReference databaseReference;
    private onlypost_2nd_Adapter secondAdapter;

    public static only_user_post_fullview newInstance(String userId, String postId, int position) {
        only_user_post_fullview fragment = new only_user_post_fullview();

        // Pass the userId and postId as arguments
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("postId", postId);
        args.putString("position", String.valueOf(position));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_only_user_post_fullview, container, false);
        Log.d("234567890","second frament oloaded");
        postsRecyclerView = view.findViewById(R.id.only_post_recview);

        String userId = getArguments().getString("userId");
        String postId = getArguments().getString("postId");
        int pos= Integer.parseInt(getArguments().getString("position"));
        databaseReference= FirebaseDatabase.getInstance().getReference();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        postsRecyclerView.setLayoutManager(layoutManager);
        // Set up the SecondAdapter with FirebaseRecyclerOptions for posts associated with the userId
        secondAdapter = new onlypost_2nd_Adapter(new FirebaseRecyclerOptions.Builder<Post_Model>()
                .setQuery(databaseReference.child("Users").child(userId).child("Posts"), Post_Model.class)
                .build());

        postsRecyclerView.setAdapter(secondAdapter);
        secondAdapter.startListening();
        Log.d(TAG, "onCreateView: "+pos);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                postsRecyclerView.scrollToPosition(pos);
            }
        }, 20); // Delay in milliseconds


//        if (postId != null) {
//            Log.d("avcdfghguyuyutyutytuu", "onCreateView: "+postId);
//            databaseReference.child("Users").child(userId).child("Posts").orderByKey().equalTo(postId).addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        Log.d("avcdfghguyuyutyutytuu", "ondataexits: " + postId);
//
//                        String postIdFromFirebase = snapshot.getChildren().iterator().next().getKey();
//
//                        // Check if the obtained postId matches the requested postId
//                        if (postId.equals(postIdFromFirebase)) {
//                            // Find the position in the Snapshots list
//                            int position = secondAdapter.getSnapshots().indexOf(snapshot.getChildren().iterator().next().getValue(Post_Model.class));
//                            Log.d(TAG, "onDataChange:" + position);
//
//                            if (position != RecyclerView.NO_POSITION) {
//                                Log.d("avcdfghguyuyutyutytuu", "onpositionexits: " + postId);
//
//                                postsRecyclerView.scrollToPosition(position);
//                                Log.d(TAG, "onDataChange:" + position);
//                            }
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    Log.d("avcdfghguyuyutyutytuu", "onerror: " + postId);
//                }
//            });
//            secondAdapter.startListening();
//
//
//        }
        OnBackPressedCallback onBackPressedCallback= new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,new Profile_fragment())
                        .commit();

            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(onBackPressedCallback);

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        secondAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        secondAdapter.stopListening();
    }
}