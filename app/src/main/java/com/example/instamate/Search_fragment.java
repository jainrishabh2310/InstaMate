package com.example.instamate;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.instamate.Adapters.Search_Image;
import com.example.instamate.Adapters.Search_Profile;
import com.example.instamate.Models.Post_Model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

import omari.hamza.storyview.utils.DynamicImageView;


public class Search_fragment extends Fragment {


    BottomNavigationView bottomNavigationView;
    EditText searchtext;
    RecyclerView search_image_rv;
    RecyclerView search_pr0file_rv;

    Search_Image searchImage;
    Search_Profile searchProfile;




    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_s);
        bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_us);
        View view= inflater.inflate(R.layout.fragment_search_fragment, container, false);

        searchtext=view.findViewById(R.id.search_text);
        search_image_rv=view.findViewById(R.id.search_image_rv);
        search_pr0file_rv=view.findViewById(R.id.search_profile_rv);



        String text=searchtext.getText().toString();

        FirebaseRecyclerOptions<Post_Model> Imagesoptions
                = new FirebaseRecyclerOptions.Builder<Post_Model>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Posts"), Post_Model.class)
                .build();

        FirebaseRecyclerOptions<Profile_DataHolder> ProfileImages
                = new FirebaseRecyclerOptions.Builder<Profile_DataHolder>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users")
                        .orderByChild("username").startAt(text).endAt(text+"\uf8ff"), Profile_DataHolder.class)
                .build();


        searchImage= new Search_Image(Imagesoptions);
        searchProfile= new Search_Profile(ProfileImages, new Search_Profile.OnItemClickListener() {
            @Override
            public void onItemClick(String userId) {
                openSecondFragment(userId);

            }
        });


        search_image_rv.setLayoutManager(new GridLayoutManager(requireContext(), 3));
        search_image_rv.setAdapter(searchImage);

        search_pr0file_rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        search_pr0file_rv.setAdapter(searchProfile);


        searchtext.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text1 = s.toString().trim();

                if(!text1.isEmpty()){
                    search_image_rv.setVisibility(View.GONE);
                    search_pr0file_rv.setVisibility(View.VISIBLE);
                    searchImage.stopListening();
                    FirebaseRecyclerOptions<Profile_DataHolder> updatedProfileImages
                            = new FirebaseRecyclerOptions.Builder<Profile_DataHolder>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("Users")
                                    .orderByChild("username").startAt(text1).endAt(text1 + "\uf8ff"), Profile_DataHolder.class)
                            .build();
                    searchProfile.updateOptions(updatedProfileImages);
                    search_pr0file_rv.setAdapter(searchProfile);
                    searchProfile.startListening();

                }


                else{


                    search_image_rv.setVisibility(View.VISIBLE);
                    search_pr0file_rv.setVisibility(View.GONE);
                    searchImage.startListening();
                    searchProfile.stopListening();

                }





            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });





















        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        searchImage.startListening();
        searchProfile.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        searchImage.stopListening();
        searchProfile.stopListening();
    }
    private void openSecondFragment(String userId) {
        User_Profiles secondFragment = User_Profiles.newInstance(userId);

        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, secondFragment)
                .addToBackStack(null)
                .commit();
        Log.d("zxcvbnmasdfghjkl","ha bhyi call hora h ye");
    }

}