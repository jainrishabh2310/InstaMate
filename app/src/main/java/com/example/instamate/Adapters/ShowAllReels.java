package com.example.instamate.Adapters;

import static android.view.View.resolveSizeAndState;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instamate.Models.Comment_Model;
import com.example.instamate.Models.Post_Model;
import com.example.instamate.Models.Reel_Model;
import com.example.instamate.Models.Reels_Comment_Model;
import com.example.instamate.Profile_DataHolder;
import com.example.instamate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllReels extends RecyclerView.Adapter<ShowAllReels.myreelholder> {
     List<String> reelsid;
    RecyclerView recyclerView_comment;
    Context context;
    GestureDetector gestureDetector;

    public ShowAllReels(Context context, List<String> reelsid){
        this.context=context;
        this.reelsid=reelsid;
    }
    public void updateData(List<String> newReelsId) {
        // Calculate the diff between old and new lists
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ReelsDiffCallback(reelsid, newReelsId));

        // Update the data
        reelsid.clear();
        reelsid.addAll(newReelsId);

        // Apply the diff result to the adapter
        diffResult.dispatchUpdatesTo(this);
    }


    @NonNull
    @Override
    public myreelholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.reel_layout,parent,false);
        return new myreelholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myreelholder holder, @SuppressLint("RecyclerView") int position) {
        String uniquereelid = reelsid.get(position).toString();
        String CurrentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();



        DatabaseReference urlsload=FirebaseDatabase.getInstance().getReference("ReelsUrl").child(uniquereelid).child("reel_url");
//        urlsload.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                String url=snapshot.getValue(String.class);
//                Log.d("URL_DEBUG", "URL: " + url); // Add this line to check the retrieved URL
//
//                // Load video only if it hasn't been loaded before
//                holder.reels.setVideoURI(Uri.parse(url));
//                holder.reels.start();
//
////                holder.like_count.setText(reelModel.getLikes()+"");
//                holder.reels.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        mp.setLooping(true);
//                    }
//                });
//
//                holder.reels.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                    @Override
//                    public void onCompletion(MediaPlayer mp) {
//                        // Restart the video when it completes
//                        holder.reels.seekTo(0); // Seek to the beginning
//                        holder.reels.start();
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });



        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reels").child(uniquereelid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
             Reel_Model reelModel = snapshot.getValue(Reel_Model.class);
                holder.reels.setVideoURI(Uri.parse(reelModel.getReel_url()));
                holder.reels.start();


//                holder.like_count.setText(reelModel.getLikes()+"");
                holder.reels.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.setLooping(true);
                        mp.start();
//                        mp.stop();
                    }
                });

                holder.reels.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // Restart the video when it completes
                        holder.reels.seekTo(0); // Seek to the beginning
//                        holder.reels.start();

                    }
                });




                holder.caption.setText(reelModel.getCaption());


                DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users").child(reelModel.getUserId());
                userref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profile_DataHolder dataHolder = snapshot.getValue(Profile_DataHolder.class);

                        holder.Username.setText(dataHolder.getUsername().toString());
                        Glide.with(holder.reels.getContext()).load(dataHolder.getProfile_pic_url()).into(holder.user_image);


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
//        Log.d(TAG, "onBindViewHolder: " + reelModel[0].getCaption());












       final Map<String, Boolean>[] map = new Map[]{new HashMap<String, Boolean>()};

        DatabaseReference liked = FirebaseDatabase.getInstance().getReference("Reels").child(uniquereelid);
        DatabaseReference likeref = liked.child("likedby");
        final boolean[] currentuserlike = new boolean[1];
        likeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Reel_Model model = snapshot.getValue(Reel_Model.class);

                map[0] = (Map<String, Boolean>) snapshot.getValue();
                if (map[0] == null) {
                    likeref.setValue(new HashMap<>());
                    holder.like_btn.setImageResource(R.drawable.reel_like_us);
                    currentuserlike[0] = false;

                } else {
                    currentuserlike[0] = map[0].containsKey(CurrentUser);
                    holder.like_btn.setImageResource(currentuserlike[0] ? R.drawable.like_s : R.drawable.reel_like_us);

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });



        HashMap<String, Object> map1 = new HashMap<>();
        HashMap<String, Object> map2 = new HashMap<>();
        HashMap<String, Object> map3 = new HashMap<>();

        holder.Comment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_section(holder,position,uniquereelid);
            }
        });


        DatabaseReference likeupdate=FirebaseDatabase.getInstance().getReference("Reels").child(uniquereelid).child("likedby");

        likeupdate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count= (int) snapshot.getChildrenCount();
                holder.like_count.setText(count+"");


                holder.like_btn.setOnClickListener(v -> {
                    if (currentuserlike[0]) {
                        holder.like_btn.setImageResource(R.drawable.reel_like_us);

                        // User already liked, so unlike
//                        likeupdate.setValue(currentlike-1);

                        likeref.child(CurrentUser).removeValue();
                    } else {
                        holder.reel_like_popup.setVisibility(View.VISIBLE);

                        Animation popupAnimation = AnimationUtils.loadAnimation(holder.reels.getContext(), R.anim.popup_animation);
                        holder.reel_like_popup.startAnimation(popupAnimation);

                        // After 3 seconds, revert back to the original image
                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            holder.reel_like_popup.setVisibility(View.GONE);
//                        notifyItemChanged(position);
                        }, 800);
                        holder.like_btn.setHapticFeedbackEnabled(true);
                        holder.like_btn.setImageResource(R.drawable.like_s);
//                        likeupdate.setValue(currentlike + 1);

                        // User hasn't liked, so like
                        map3.put(CurrentUser, true);
                        likeref.updateChildren(map3);

                    }

                    List<String> newReelsId = new ArrayList<>();
                    newReelsId.addAll(reelsid);
                    ShowAllReels showAllReels= new ShowAllReels(context,newReelsId);
                    showAllReels.updateData(newReelsId);



                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference countcomment = FirebaseDatabase.getInstance().getReference("Reels")
                .child(uniquereelid).child("CommentList");

        countcomment.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count_comm = (int) snapshot.getChildrenCount();
                holder.commen_count.setText(count_comm + "");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        gestureDetector= new GestureDetector(holder.reels.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(@NonNull MotionEvent e) {

                // Adjust the duration as needed
                if(!currentuserlike[0]){
                    holder.reel_like_popup.setVisibility(View.VISIBLE);

                    Animation popupAnimation = AnimationUtils.loadAnimation(holder.reels.getContext(), R.anim.popup_animation);
                    holder.reel_like_popup.startAnimation(popupAnimation);

                    // After 3 seconds, revert back to the original image
                    new Handler(Looper.getMainLooper()).postDelayed(() -> {
                        holder.reel_like_popup.setVisibility(View.GONE);
//                        notifyItemChanged(position);
                    }, 800);

                    holder.like_btn.setImageResource(R.drawable.like_s);

                    // User hasn't liked, so like
                     map3.put(CurrentUser,true);
                    liked.child("likedby").updateChildren(map3);





                }

                return super.onDoubleTap(e);
            }
        });

        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });














    }







    @Override
    public int getItemCount() {
        return reelsid.size();
    }


    class myreelholder extends RecyclerView.ViewHolder {
        CircleImageView user_image;
        TextView Username;
        TextView caption;

        ImageView like_btn,Comment_btn;
        TextView like_count,commen_count;

        VideoView reels;
        ImageView reel_like_popup;

        public myreelholder(@NonNull View itemView) {
            super(itemView);
            reels = itemView.findViewById(R.id.reel_video_pic);
            like_btn = itemView.findViewById(R.id.reel_like_btn);
            like_count = itemView.findViewById(R.id.reel_like_count);
            commen_count = itemView.findViewById(R.id.reel_comment_count);
            Comment_btn = itemView.findViewById(R.id.reel_comment_btn);
            user_image = itemView.findViewById(R.id.reel_user_pic);
            Username = itemView.findViewById(R.id.reel_username);
            caption = itemView.findViewById(R.id.reel_caption);
            reel_like_popup=itemView.findViewById(R.id.reel_like_popup);


        }
    }
    private void comment_section(myreelholder holder, int position,String reelid) {
        int screenHeight = getScreenHeight(holder.user_image.getContext());
        int dialogHeight = 3*screenHeight / 4;

        final DialogPlus dialogPlus = DialogPlus.newDialog(holder.user_image.getContext())
                .setContentHolder(new ViewHolder(LayoutInflater.from(holder.user_image.getContext()).inflate(R.layout.commenr_section,null)))
                .setExpanded(false)
                .setContentHeight(dialogHeight)  // Set a maximum height

                .create();

        dialogPlus.show();
//        startAdapterListening();

        View comment_view =dialogPlus.getHolderView();
        recyclerView_comment=comment_view.findViewById(R.id.rv_comments);
        CircleImageView user_pic=comment_view.findViewById(R.id.comment_section_user_pic);
        EditText comment_content=comment_view.findViewById(R.id.comment_content);
        ImageView post_comment=comment_view.findViewById(R.id.post_comment);



        String currentuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(currentuserid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile_DataHolder dataHolder= snapshot.getValue(Profile_DataHolder.class);
                Glide.with(holder.user_image.getContext()).load(dataHolder.getProfile_pic_url())
                        .fitCenter()
                        .into(user_pic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<Reels_Comment_Model> options1
                = new FirebaseRecyclerOptions.Builder<Reels_Comment_Model>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Reels").child(reelid).child("CommentList"), Reels_Comment_Model.class)
                .build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.user_image.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView_comment.setLayoutManager(layoutManager);
        Reel_Comment_Adapter reelCommentAdapter= new Reel_Comment_Adapter(options1);
        recyclerView_comment.setAdapter(reelCommentAdapter);
        reelCommentAdapter.startListening();

        DatabaseReference commentref=FirebaseDatabase.getInstance().getReference("Reels").child(reelid).child("CommentList");
        post_comment.setOnClickListener(new View.OnClickListener() {


            DatabaseReference reff=FirebaseDatabase.getInstance().getReference("Reels");
            @Override
            public void onClick(View v) {
                String commentContent=comment_content.getText().toString();

                Reels_Comment_Model commentModel= new Reels_Comment_Model(currentuserid,commentContent,new Date(),reelid,0,new HashMap<>());
                String commentid=commentref.push().getKey();
                if(!commentContent.isEmpty()) {
                    commentref.child(commentid).setValue(commentModel);


                    comment_content.setText("");
                }
                else{
                    Toast.makeText(holder.user_image.getContext(),"Please Enter Comment",Toast.LENGTH_LONG).show();
                }



            }
        });





    }


    private int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    public class ReelsDiffCallback extends DiffUtil.Callback {
        private final List<String> oldList;
        private final List<String> newList;

        public ReelsDiffCallback(List<String> oldList, List<String> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // Add logic to check if items are the same, for example, using unique IDs
            return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            // Add logic to check if the content of items is the same
            // This method is called if areItemsTheSame returns true
            // You can compare fields of the items here
            return true; // Modify this according to your requirements
        }
    }


}
