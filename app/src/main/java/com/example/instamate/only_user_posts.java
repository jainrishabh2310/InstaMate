package com.example.instamate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instamate.Models.Post_Model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link only_user_posts#newInstance} factory method to
 * create an instance of this fragment.
 */
public class only_user_posts extends Fragment {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    onlypost_1st_Adapter onlypost1stAdapter;

    ArrayList<String> imageurls;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public only_user_posts() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment only_user_posts.
     */
    // TODO: Rename and change types and number of parameters
    public static only_user_posts newInstance(String param1, String param2) {
        only_user_posts fragment = new only_user_posts();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_only_user_posts, container, false);
        recyclerView = view.findViewById(R.id.only_user_postrecyclerView);
        imageurls= new ArrayList<>();

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference();

        onlypost1stAdapter = new onlypost_1st_Adapter(getContext(),imageurls
                , new onlypost_1st_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(String userId, String postId, int position) {
                openSecondFragment(userId, postId,position);

            }
        });
        recyclerView.setAdapter(onlypost1stAdapter);




        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Users").child(userid).child("Posts");

        postsRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot uniqueIdSnapshot : dataSnapshot.getChildren()) {
                    // Iterate through each unique ID under "Reels"
                    String uniqueId = uniqueIdSnapshot.getKey();

                    // Assuming you have a "reelsurl" property under each unique ID
                    String url = uniqueIdSnapshot.child("post_url").getValue(String.class);
                    Log.d("wertyuioplkjhgdszxcvbnm,", url);


                    // Add the URL to the list
                    imageurls.add(url);
                }
                Collections.reverse(imageurls);
                onlypost1stAdapter.notifyDataSetChanged();

            }
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if any
                Log.d("xcvbnmkjh","error h bhai ");

            }
        });








        return view;
    }
    private void openSecondFragment(String userId, String postId, int position) {
        only_user_post_fullview secondFragment = only_user_post_fullview.newInstance(userId, postId,position);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, secondFragment)
                .addToBackStack(null)
                .commit();
        Log.d("zxcvbnmasdfghjkl","ha bhyi call hora h ye");
    }
}