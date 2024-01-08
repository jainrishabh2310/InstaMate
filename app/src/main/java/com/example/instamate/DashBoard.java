package com.example.instamate;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class DashBoard extends AppCompatActivity {
FrameLayout frame_container;
BottomNavigationView bottomNavigationView;
FrameLayout relativeLayout;
private GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFcmToken();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_dash_board);
         bottomNavigationView = findViewById(R.id.bottom_navigation);
        frame_container = findViewById(R.id.fragment_container);
        int color = ContextCompat.getColor(this, R.color.white); // Replace with your desired color
        bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(color));
        setBottomNavigationIconSize(bottomNavigationView, R.dimen.your_custom_icon_size);
        // Replace with your desired size

        loadFragment(new Home_fragment());

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id==R.id.navigation_home){
                    bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_hh);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_hh);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_hh);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_hh);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_hh);
                    bottomNavigationView.setBackgroundResource(R.color.black);
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.white); // Replace with your desired color
                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(color));

                    loadFragment(new Home_fragment());
                    return true;
                }
                else if(id==R.id.navigation_search){
                    bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_s);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_us);
                    bottomNavigationView.setBackgroundResource(R.color.white);
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.black); // Replace with your desired color
                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(color));


                    loadFragment(new Search_fragment());
                    return true;
                }
                else if(id==R.id.navigation_add){
//                    bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_us);
//                    bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_us);
//                    bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_s);
//                    bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_us);
//                    bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_us);
//
//                    loadFragment(new Add_fragment());
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.black); // Replace with your desired color
                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(color));

                    Intent intent = new Intent(DashBoard.this, Add_Activity.class);
                    startActivity(intent);
                    return true;
                }
                else if (id==R.id.navigation_reel) {
                    bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_s);
                    bottomNavigationView.setBackgroundResource(R.color.white);

                    int color = ContextCompat.getColor(getApplicationContext(), R.color.black); // Replace with your desired color
                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(color));

                    loadFragment(new Reels_fragment());
                    return true;

                }
                else if(id==R.id.navigation_profile){
                    bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_s);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_us);
                    bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_us);
                    bottomNavigationView.setBackgroundResource(R.color.white);
                    int color = ContextCompat.getColor(getApplicationContext(), R.color.black); // Replace with your desired color
                    bottomNavigationView.setItemIconTintList(ColorStateList.valueOf(color));



                    loadFragment(new Profile_fragment());
                    return true;
                }

                else
                    return false;



            }
        });




    }

    private void getFcmToken() {

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                String token=task.getResult();
                Log.d("onComplete: ",token);
                HashMap<String,Object> tokenmap= new HashMap<>();
                tokenmap.put("FCM_token",token);
                String currentid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseDatabase.getInstance().getReference("Users")
                        .child(currentid).updateChildren(tokenmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d(TAG, "onComplete: "+"sucess");
                            }
                        });



            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        if(!fragment.isStateSaved())
             transaction.addToBackStack(null);
        transaction.commit();
    }
    private void setBottomNavigationIconSize(BottomNavigationView bottomNavigationView, int iconSizeRes) {
        // Get the desired icon size from resources
        int iconSize = getResources().getDimensionPixelSize(iconSizeRes);

        // Iterate through each menu item and set the icon size
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            View icon = bottomNavigationView.findViewById(menuItem.getItemId());
            icon.getLayoutParams().height = iconSize;
            icon.getLayoutParams().width = iconSize;
        }
    }



}