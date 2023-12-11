package com.example.instamate.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.instamate.Home_fragment;
import com.example.instamate.Profile_fragment;
import com.example.instamate.Reels_fragment;
import com.example.instamate.Search_fragment;
import com.example.instamate.only_user_posts;
import com.example.instamate.only_user_reels;

public class ProfilePagerAdapter extends FragmentStateAdapter {

    public ProfilePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new only_user_posts();
            case 1:
                return new only_user_reels();
            default:
                return new only_user_posts(); // Default fragment
        }
    }

    @Override
    public int getItemCount() {
        return 2; // Number of tabs
    }
}
