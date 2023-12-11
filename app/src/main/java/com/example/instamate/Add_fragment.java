package com.example.instamate;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.instamate.Adapters.ProfilePagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class Add_fragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    CardView cardView;
    BottomNavigationView bottomNavigationView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_fragment, container, false);

        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_s);
        bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_us);

        Intent intent = new Intent(getContext(), Add_Activity.class);
        startActivity(intent);


//        viewPager2=view.findViewById(R.id.Add_viewPager);
//
//        Add_Adapter adapter = new Add_Adapter(getActivity());
//        viewPager2.setAdapter(adapter);
//        viewPager2.setUserInputEnabled(true);
//        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
//
//        new TabLayoutMediator(tabLayout, viewPager2,
//                (tab, position) -> {
//                    switch (position) {
//                        case 0:
//                            tab.setText("Posts");
//                            break;
//                        case 1:
//                            tab.setText("Reels");
//                            break;
//                        case 2:
//                            tab.setText("Stroies");
//                            break;
//                    }
//                }
//        ).attach();
//
//
//
//        return  view;
//    }
//
//public class Add_Adapter extends FragmentStateAdapter {
//
//        public Add_Adapter(@NonNull FragmentActivity fragmentActivity) {
//            super(fragmentActivity);
//        }
//
//        @NonNull
//        @Override
//        public Fragment createFragment(int position) {
//            switch (position) {
//                case 0:
//                    return new Post_add();
//                case 1:
//                    return new Reel_add();
//                case 2:
//                    return new Strory_add();
//                default:
//                    return new Post_add();   // Default fragment
//            }
//        }

//        @Override
//        public int getItemCount() {
//            return 3; // Number of tabs
//        }
//    }
//    public void updateTabLayoutVisibility(boolean hasFocus) {
//        // If EditText has focus, hide the TabLayout; otherwise, show it
//        tabLayout.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
//        cardView.setVisibility(hasFocus ? View.GONE : View.VISIBLE);
//


//    }

return  view;
    }
}