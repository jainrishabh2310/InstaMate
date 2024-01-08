package com.example.instamate;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link only_user_reels_fullview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class only_user_reels_fullview extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public only_user_reels_fullview() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static only_user_reels_fullview newInstance(String userId, String reelid,int position) {
        only_user_reels_fullview fragment = new only_user_reels_fullview();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        args.putString("reelid", reelid);
        args.putString("position", String.valueOf(position));
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
        View view= inflater.inflate(R.layout.fragment_only_user_reels_fullview, container, false);


    return view;}
}