package com.example.instamate.Adapters;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instamate.Models.Story_Model;
import com.example.instamate.Profile_DataHolder;
import com.example.instamate.R;
import com.example.instamate.story_to_db;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import omari.hamza.storyview.StoryView;
import omari.hamza.storyview.callback.OnStoryChangedCallback;
import omari.hamza.storyview.callback.StoryClickListeners;
import omari.hamza.storyview.model.MyStory;

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.mystoryholder> {
    ArrayList<String> reelid;
    FragmentManager fragmentManager;
    ArrayList<MyStory> reel_url;
    Context context;

     ArrayList<String> userreelid;


    public StoryAdapter(@NonNull ArrayList<String> userreelid, FragmentManager fragmentManager, Context context) {
       this.fragmentManager = fragmentManager;
        this.reelid = new ArrayList<>();
        this.reel_url = new ArrayList<>();
        this.context = context;
        this.userreelid=userreelid;
    }

    @Override
    public void onBindViewHolder(@NonNull mystoryholder holder, @SuppressLint("RecyclerView") int position) {
        DatabaseReference deletestory=FirebaseDatabase.getInstance().getReference("Stories")
                        .child(userreelid.get(position));
        deletestory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reelid.clear();
                reel_url.clear();
                for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                    // Iterate through each unique ID under "Reels"
                    String uniqueId = uniqueIdSnapshot.getKey();

                    reelid.add(uniqueId);
                }
                for (int i = 0; i < reelid.size(); i++) {

                    DatabaseReference reelurl = deletestory.child(reelid.get(i));
                    int finalI = i;
                    reelurl.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {

                                Story_Model model = snapshot.getValue(Story_Model.class);
                                if (model != null && model.getTimestamp() != null) {

                                    Date storydate = model.getTimestamp();
                                    Date currentDate = new Date();
                                    long timediff = currentDate.getTime() - model.getTimestamp().getTime();
                                    if (timediff > 24 * 60 * 60) {


//                               String deletekey= String.valueOf(deletestory.child(reelid.get(finalI)).child(model.getStoryid()));
                                        Log.d("ye h delete key", "onDataChange: " + model.getStoryid());
//                               deletestory.child(reelid.get(finalI)).child(deletekey).removeValue();

                                    }
                                }


//                            Log.d("storyurld", "onDataChange:" + model.getStory_url());

                            }
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




        Log.d("Position", "onBindViewHolder: "+position);

        String reeluploaderuserid=userreelid.get(position);


        String current = FirebaseAuth.getInstance().getCurrentUser().getUid();



        final String[] username = {""};
        final String[] Userpic = {""};





        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(reeluploaderuserid);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reel_url.clear();
                reelid.clear();


                Profile_DataHolder profileDataHolder = snapshot.getValue(Profile_DataHolder.class);

                username[0] =profileDataHolder.getUsername();
                Userpic[0] =profileDataHolder.getProfile_pic_url();


                Glide.with(holder.itemView.getContext()).load(profileDataHolder.getProfile_pic_url())
                        .centerCrop()
                        .into(holder.story_user_pic);

                holder.textView.setText(profileDataHolder.getUsername());

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });

        DatabaseReference check=FirebaseDatabase.getInstance().getReference("Stories").child(reeluploaderuserid);
        check.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reelid.clear();
                reel_url.clear();

                for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                    // Iterate through each unique ID under "Reels"
                    String uniqueId = uniqueIdSnapshot.getKey();

                    Log.d("reeels ki ids", "onDataChange: " + uniqueId);
                    reelid.add(uniqueId);
                }

                DatabaseReference check=FirebaseDatabase.getInstance().getReference("Stories")
                        .child(reeluploaderuserid).child(reelid.get(reelid.size()-1)).child("seen");
                check.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            HashMap<String,Object> map= (HashMap<String,Object>) snapshot.getValue();
                            if(map.containsKey(current)){
                                Log.d("Ider aaaa", "onDataChange: "+"ha ider chla gya" );
                                Log.d("Ider aaaa", "onDataChange: "+"ha ider chla gya" +position+" "+0);

                                notifyItemMoved(position,0);
                                holder.linearLayout.setBackgroundColor(R.color.black);


//                                notifyDataSetChanged();
                            }


                        }


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









                holder.story_user_pic.setOnClickListener(new View.OnClickListener() {


                    @Override
                    public void onClick(View v) {


                        DatabaseReference rr = FirebaseDatabase.getInstance().getReference("Stories").child(reeluploaderuserid);
                        rr.addValueEventListener(new ValueEventListener() {

                            @SuppressLint("ResourceAsColor")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {


                                reelid.clear();
                                reel_url.clear();

                                for (DataSnapshot uniqueIdSnapshot : snapshot.getChildren()) {
                                    // Iterate through each unique ID under "Reels"
                                    String uniqueId = uniqueIdSnapshot.getKey();

                                    Log.d("reeels ki ids", "onDataChange: " + uniqueId);
                                    reelid.add(uniqueId);
                                }






                                for (int i = 0; i < reelid.size(); i++) {

                                    DatabaseReference reelurl = rr.child(reelid.get(i));
                                    reelurl.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                                            Story_Model model = snapshot.getValue(Story_Model.class);
                                            Date currentdate=model.getTimestamp();
                                            PrettyTime prettyTime = new PrettyTime();
                                            String relativeTime = prettyTime.format(currentdate);
                                            Date currentDate= new Date();
                                            long timediff=currentDate.getTime()-model.getTimestamp().getTime();

                                            reel_url.add(new MyStory(model.getStory_url(),currentdate));
                                            notifyAdapterIfNeeded(username[0],Userpic[0],position,current,holder);



                                            Log.d("storyurld", "onDataChange:" + model.getStory_url());

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });


                                }


                                notifyAdapterIfNeeded(username[0], Userpic[0],position,current,holder);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


//


                    }
                });

            }














    @NonNull
    @Override
    public mystoryholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_layout, parent, false);
//        reel_url.clear();
        
        return new mystoryholder(view);
    }


    @Override
    public int getItemCount() {
        return userreelid.size();
    }

    class mystoryholder extends RecyclerView.ViewHolder {

        ImageView story_user_pic;
        TextView textView;

        LinearLayout linearLayout;

        public mystoryholder(@NonNull View itemView) {
            super(itemView);
            story_user_pic = itemView.findViewById(R.id.story_pic_user);
            textView = itemView.findViewById(R.id.story_username);
            linearLayout=itemView.findViewById(R.id.cv_change_color);
        }
    }

    private void notifyAdapterIfNeeded(String username, String userpic,int position,String currentuserid,mystoryholder holder) {
        if (reel_url.size() == reelid.size()) {


            StoryView.Builder builder = new StoryView.Builder(fragmentManager)
                    .setStoriesList(reel_url)
                    .setStoryDuration(15000)
                    .setTitleText(username)
                    .setTitleLogoUrl(userpic)

                    .setSubtitleText("Views")

                    .setOnStoryChangedCallback(new OnStoryChangedCallback() {
                        @SuppressLint("ResourceAsColor")
                        @Override
                        public void storyChanged(int index) {
                            Log.d("Storyesisis", "onstory"+index+" "+reelid.size());

                            if(!reelid.isEmpty() && index==reelid.size()-1) {
//
                                final HashMap<String, Object>[] allstorysee = new HashMap[]{new HashMap<>()};
                                HashMap<String, Object> map2 = new HashMap<>();
//
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stories").child(userreelid.get(position))
                                        .child(reelid.get(index)).child("seen");
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        allstorysee[0] = (HashMap<String, Object>) snapshot.getValue();
                                        Log.d("StoryRelated", "onDataChange: "+allstorysee[0]);

                                        if (allstorysee[0]==null) {
                                            Log.d(TAG, "onSuccess: "+"ismeaarhi h");

                                            reference.setValue(new HashMap<>()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    map2.put(currentuserid, true);
                                                    reference.updateChildren(map2);
                                                    notifyDataSetChanged();


                                                    Log.d(TAG, "onSuccess: "+"done");

                                                }
                                            })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.d(TAG, "onSuccess: "+e);

                                                        }
                                                    });


                                        }

                                        else if(!allstorysee[0].containsKey(currentuserid)) {
                                                map2.put(currentuserid, true);
                                                reference.updateChildren(map2);
                                                notifyDataSetChanged();
                                        }




                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
//


                            }










                        }

                    })

                    .setStoryClickListeners(new StoryClickListeners() {
                        @Override
                        public void onDescriptionClickListener(int position) {
                            Log.d("45678ihbnm", "onDescriptionClickListener:  dessss"+position);

                            // your action
                        }

                        @Override
                        public void onTitleIconClickListener(int position) {

                            Intent intent=new Intent(holder.story_user_pic.getContext(), story_to_db.class);
                            holder.linearLayout.getContext().startActivity(intent);


                            // your action
                            Log.d("45678ihbnm", "onDescriptionClickListener: titlee"+position);

                        }



                    })
                    .build();

            // Must be called before calling show method
            builder.show();
        }
    }
}

