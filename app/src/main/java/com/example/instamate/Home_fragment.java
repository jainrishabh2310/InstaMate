package com.example.instamate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instamate.Adapters.ShowAllPosts;
import com.example.instamate.Adapters.StoryAdapter;
import com.example.instamate.Adapters.Story_Views_Adapter;
import com.example.instamate.Models.Post_Model;
import com.example.instamate.Models.Story_Model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Date;

import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.OnStoryChangedCallback;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;


public class Home_fragment extends Fragment {
    RecyclerView show_all_posts;
    ShowAllPosts allPostsAdapter;
    BottomNavigationView bottomNavigationView;

    RecyclerView show_all_story;
    StoryAdapter storyAdapter;
    ImageView story_add_user;
    TextView story_add_username;
    ArrayList<String> reelid;
    ArrayList<MyStory> reels_url;

ArrayList<String> userreelid;
LinearLayout cv_color_change;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         reelid=new ArrayList<>();
         reels_url= new ArrayList<>();
         userreelid= new ArrayList<>();
        // Inflate the layout for this fragment
        String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        View view = inflater.inflate(R.layout.fragment_home_fragment, container, false);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_s);
        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_us);


        show_all_posts=view.findViewById(R.id.vp_view_all_posts);
        show_all_story=view.findViewById(R.id.story_recview);

         story_add_user=view.findViewById(R.id.story_current_user);
         story_add_username=view.findViewById(R.id.story_current_username);
        cv_color_change = view.findViewById(R.id.cv_change_color);




TextView logo=view.findViewById(R.id.logo);
        Shader shader= new LinearGradient(
                45,0,0,logo.getLineHeight(),
                new int[]{
                        Color.parseColor("#02bfcc"),
                        Color.parseColor("#7d2ae6")


                }, null,Shader.TileMode.REPEAT
        );
        logo.getPaint().setShader(shader);


        FirebaseRecyclerOptions<Post_Model> options
                = new FirebaseRecyclerOptions.Builder<Post_Model>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Posts"), Post_Model.class)
                .build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        show_all_posts.setLayoutManager(layoutManager);
        allPostsAdapter= new ShowAllPosts(options,getContext());
        show_all_posts.setAdapter(allPostsAdapter);


        storyAdapter= new StoryAdapter(userreelid,getActivity().getSupportFragmentManager(),getContext());
        show_all_story.setAdapter(storyAdapter);


    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stories");
    reference.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            userreelid.clear();

            for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                // Iterate through each unique ID under "Reels"
                String uniqueId = uniqueIdSnapshot.getKey();
                if(!userid.equals(uniqueId)) {
                    Log.d("reeels ki ids", "onDataChange: " + uniqueId);
                    userreelid.add(uniqueId);
                }
            }
            storyAdapter.notifyDataSetChanged();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    });




//
//
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
        layoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        layoutManager1.setReverseLayout(true);
        layoutManager1.setStackFromEnd(true);
        show_all_story.setLayoutManager(layoutManager1);




        DatabaseReference reference12=FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference12.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cv_color_change.setBackgroundResource(R.drawable.gradient_back);
                Profile_DataHolder profileDataHolder=snapshot.getValue(Profile_DataHolder.class);
                Glide.with(getContext()).load(profileDataHolder.getProfile_pic_url())
                        .centerCrop()
                        .into(story_add_user);
                story_add_username.setText(profileDataHolder.getUsername());
                story_add_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabaseReference reference1=reference12.child("Story");
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                reelid.clear();
                                reels_url.clear();

                                for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                                    // Iterate through each unique ID under "Reels"
                                    String uniqueId = uniqueIdSnapshot.getKey();

                                    Log.d("reeels ki ids", "onDataChange: " + uniqueId);
                                    reelid.add(uniqueId);
                                }
                                if(reelid.size()>0){
                                    Log.d("jhkjhsjhdfjshjh", "onDataChange: "+reelid.size());
                                    cv_color_change.setBackgroundResource(R.drawable.gradient_back);
                                }


                                for (int i = 0; i < reelid.size(); i++) {
                                    DatabaseReference reelurl = reference1.child(reelid.get(i));
                                    reelurl.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            Story_Model model = snapshot.getValue(Story_Model.class);
                                            Log.d("storyurld", "onDataChange:" + model.getStory_url());
                                            Date currenttime=model.getTimestamp();

                                            final int[] views = new int[1];
                                            reelurl.child("seen").addValueEventListener(new ValueEventListener() {
                                                                                            @Override
                                                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                                                                                    Log.d("wertyuiojhgv", "onDataChange:  1 hogya bhyi");
                                                                                                    views[0] = views[0] + 1;
                                                                                                    Log.d("wertyuiojhgv", "onDataChange:  1 hogya bhyi" + views[0]);





                                                                                                }
                                                                                                reels_url.add(new MyStory(model.getStory_url(),currenttime,views[0]+" Views"));
                                                                                                notifyAdapterIfNeeded(profileDataHolder.getUsername(),profileDataHolder.getProfile_pic_url(),userid);


                                                                                            }

                                                                                            @Override
                                                                                            public void onCancelled(@NonNull DatabaseError error) {

                                                                                            }
                                                                                        });




                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });









                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                int position = viewHolder.getAdapterPosition();
//                Intent intent= new Intent(getContext(),ChatActivity.class);startActivity(intent);
//            }
//        };
//
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
//        itemTouchHelper.attachToRecyclerView(show_all_posts);




        show_all_posts.setOnTouchListener(new OnSwipeTouchListener(getContext()) {

            public void onSwipeLeft() {
                Intent intent= new Intent(getContext(), ChatActivity.class);
                getContext().startActivity(intent);
            }


        });


        return view;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        allPostsAdapter.startListening();
//        storyAdapter.startListening();
    }


//    @Override
//    public void onStop()
//    {
//        super.onStop();
//        allPostsAdapter.stopListening();
////        storyAdapter.stopListening();
//    }


    private void notifyAdapterIfNeeded(String username, String userpic, String userid) {


        if (reels_url.size() == reelid.size()) {
            // Create a new RelativeLayout to host the StoryView and "Seen" button


            // Create a new Builder instance for StoryView
           StoryView.Builder builder= new StoryView.Builder(getActivity().getSupportFragmentManager())
                    .setStoriesList(reels_url)
                    .setStoryDuration(15000)
                    .setTitleText(username)
                    .setTitleLogoUrl(userpic);

                    builder.setOnStoryChangedCallback(new OnStoryChangedCallback() {
                        @Override
                        public void storyChanged(int position) {







                        }
                    })
                            .setStoryClickListeners(new StoryClickListeners() {

                                @Override
                                public void onDescriptionClickListener(int position) {

                                    ArrayList<String> urls=new ArrayList<>();
                                    urls.clear();



                                    int screenHeight = getScreenHeight(getContext());
                                    int dialogHeight = 3*screenHeight / 4;

                                    final DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                                            .setContentHolder(new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.story_views_layout,null)))
                                            .setExpanded(false)
                                            .setContentHeight(dialogHeight)  // Set a maximum height
                                            .create();

                                    dialogPlus.show();

                                    View story_view =dialogPlus.getHolderView();

                                    RecyclerView recyclerView=story_view.findViewById(R.id.story_view_rv);

                                    LinearLayoutManager layoutManager1 = new LinearLayoutManager(getContext());
                                    layoutManager1.setOrientation(RecyclerView.VERTICAL);
                                    layoutManager1.setReverseLayout(true);
                                    layoutManager1.setStackFromEnd(true);
                                    recyclerView.setLayoutManager(layoutManager1);

                                    Story_Views_Adapter storyViewsAdapter= new Story_Views_Adapter(getContext(),urls);
                                    recyclerView.setAdapter(storyViewsAdapter);

                                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users").child(userid).child("Story").
                                    child(reelid.get(position)).
                                            child("seen");

                                    reference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                                                    String singleid=snapshot1.getKey();
                                                    urls.add(singleid);


                                                }


                                                storyViewsAdapter.notifyDataSetChanged();

                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });



                                }

                                @Override
                                public void onTitleIconClickListener(int position) {




                                }
                            })
                    .build();
            builder.show();
            // Save the reference to the StoryView

            // Create a new FrameLayout to host the StoryView and "Seen" button




        }



        // Set the OnTouchListener for the RecyclerView


    }


    private int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public class OnSwipeTouchListener implements View.OnTouchListener {

        private final GestureDetector gestureDetector;

        public OnSwipeTouchListener (Context ctx){
            gestureDetector = new GestureDetector((Context) ctx,new GestureListener());
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }

        private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                try {
                    float diffY = e2.getY() - e1.getY();
                    float diffX = e2.getX() - e1.getX();
                    if (Math.abs(diffX) > Math.abs(diffY)) {
                        if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                            if (diffX > 0) {
//                                onSwipeRight();
                            } else {
                                onSwipeLeft();
                            }
                            result = true;
                        }
                    }
                    else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
//                            onSwipeBottom();
                        } else {
//                            onSwipeTop();
                        }
                        result = true;
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                return result;
            }
        }



        public void onSwipeLeft() {
            Log.d("left", "onSwipeLeft: ");
        }


    }


}