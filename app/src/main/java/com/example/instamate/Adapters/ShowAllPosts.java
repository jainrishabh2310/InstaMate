package com.example.instamate.Adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instamate.ChatActivity;
import com.example.instamate.Models.Comment_Model;
import com.example.instamate.Models.Post_Model;
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

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ShowAllPosts extends FirebaseRecyclerAdapter<Post_Model,ShowAllPosts.mypostviewholder> {


    RecyclerView recyclerView_comment;
     CommentAdapter commentAdapter;
     private GestureDetector gestureDetector;

     Context context;
    public ShowAllPosts(@NonNull FirebaseRecyclerOptions<Post_Model> options,Context context) {
        super(options);
        this.context=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull mypostviewholder holder, @SuppressLint("RecyclerView") final int position, @NonNull Post_Model model) {
        Log.d("Rishabh ", "onBindViewHolder: "+position);
        String userid=model.getUserId();

        String likeuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users").child(userid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile_DataHolder dataHolder= snapshot.getValue(Profile_DataHolder.class);
                holder.user_name.setText(dataHolder.getUsername().toString());
                Glide.with(holder.user_name.getContext()).load(dataHolder.getProfile_pic_url())
                        .centerCrop()
                        .into(holder.user_pic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Glide.with(holder.post_pic.getContext()).load(model.getPost_url())
                .into(holder.post_pic);
        holder.post_caption.setText(model.getCaption().toString());
        holder.total_like_count.setText(model.getLikes()+" likes");
        final Map<String, Boolean>[] map = new Map[]{new HashMap<String, Boolean>()};
        DatabaseReference liked=FirebaseDatabase.getInstance().getReference("Posts").child(getRef(position).getKey());
        DatabaseReference likeref=liked.child("likedby");
        final boolean[] currentuserlike = new boolean[1];
        likeref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                map[0] = (Map<String, Boolean>) snapshot.getValue();
                if(map[0]==null){
                    likeref.setValue(new HashMap<>());
                    holder.like.setImageResource(R.drawable.like_us);
                    currentuserlike[0]=false;

                }
                else {
                    currentuserlike[0] = map[0].containsKey(likeuserid);

                    if(currentuserlike[0]){

//
//                        Bitmap original= BitmapFactory.decodeResource(holder.post_pic.getContext().getResources(),R.drawable.like_s);
//
//                        Bitmap gradinet=Bitmap.createBitmap(original.getWidth(),original.getHeight(),Bitmap.Config.ARGB_8888);
//                        Canvas canvas=new Canvas(gradinet);
//
//                        Shader shader= new LinearGradient(
//                                45,0,holder.like.getWidth(),0,
//                                new int[]{
//                                        Color.parseColor("#02bfcc"),
//                                        Color.parseColor("#7d2ae6")
//
//
//                                }, null,Shader.TileMode.CLAMP
//                        );
//                        Paint paint= new Paint();
//                        paint.setShader(shader);
//                        canvas.drawBitmap(original,0,0,paint);
//                        holder.like.setImageBitmap(gradinet);





                        holder.like.setImageResource(R.drawable.like_s);
//                        holder.like.setBackgroundDrawable(R.drawable.gradient_back);
//                        holder.like.setColorFilter(Color.parseColor("fff"),PorterDuff.Mode.SRC_IN);

                    }
                    else{
                        holder.like.setImageResource(R.drawable.like_us);
                        holder.like.setColorFilter(Color.parseColor("#000000"));


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        HashMap<String,Object> map1= new HashMap<>();
        HashMap<String,Object> map2= new HashMap<>();
        HashMap<String,Object> map3= new HashMap<>();




        holder.like.setOnClickListener(v -> {
                    if (currentuserlike[0]) {
                        holder.like.setImageResource(R.drawable.like_us);

                        // User already liked, so unlike
                        holder.total_like_count.setText(String.valueOf(model.getLikes() - 1));
                        map1.put("likes",model.getLikes()-1);
                        liked.updateChildren(map1);
                        liked.child("likedby").child(likeuserid).removeValue();
                    } else {
                        holder.like.setImageResource(R.drawable.like_s);
                        map2.put("likes",model.getLikes()+1);
                        liked.updateChildren(map2);

                        // User hasn't liked, so like
                        holder.total_like_count.setText(String.valueOf(model.getLikes() + 1));
//                        model.getLikedBy().put(likeuserid, true);
                        map3.put(likeuserid,true);
                        liked.child("likedby").updateChildren(map3);


                    }




                });

        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_section(holder,position,model);

            }
        });
        holder.total_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_section(holder,position,model);


            }
        });

        Date timestampDate = model.getTimestamp();

        PrettyTime prettyTime = new PrettyTime();
        String relativeTime = prettyTime.format(timestampDate);
        holder.home_post_time.setText(relativeTime);

        DatabaseReference countcomment=FirebaseDatabase.getInstance().getReference("Posts")
                .child(getRef(position).getKey()).child("CommentList");



        countcomment.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int count_comm= (int) snapshot.getChildrenCount();
                holder.total_comment.setText("View all "+count_comm+" Comments");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        gestureDetector= new GestureDetector(holder.post_pic.getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(@NonNull MotionEvent e) {

                  // Adjust the duration as needed
                if(!currentuserlike[0])
                    showpopupimage(holder,currentuserlike[0],model,liked);

                return super.onDoubleTap(e);
            }
        });


        holder.post_pic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });

        holder.post_pic.setOnTouchListener(new OnSwipeTouchListener(context){
            public void onSwipeLeft() {
                Intent intent= new Intent(context, ChatActivity.class);
                holder.itemView.getContext().startActivity(intent);
            }

        });










    }



    @NonNull
    @Override
    public mypostviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_layout, parent, false);

        return new mypostviewholder(view);
    }

    class mypostviewholder extends RecyclerView.ViewHolder{
        ImageView user_pic;
        TextView user_name;
        ImageView post_pic,settings,like,comment;
        TextView total_like_count;
        TextView post_caption;
        TextView total_comment;

        TextView home_post_time;

        ImageView home_post_popup_like;





        public mypostviewholder(@NonNull View itemView) {
            super(itemView);
            user_pic=itemView.findViewById(R.id.post_user_pic);
            user_name=itemView.findViewById(R.id.post_user_username);
            post_pic=itemView.findViewById(R.id.home_user_post);
            settings=itemView.findViewById(R.id.post_settings);
            like=itemView.findViewById(R.id.post_like_btn);
            comment=itemView.findViewById(R.id.post_comment_btn);
            total_like_count=itemView.findViewById(R.id.home_post_show_like_count);
            post_caption=itemView.findViewById(R.id.home_post_caption);
            total_comment=itemView.findViewById(R.id.home_post_view_all_comments);
            home_post_time=itemView.findViewById(R.id.home_post_time);
            home_post_popup_like=itemView.findViewById(R.id.home_user_popup_like);









        }
    }
    private void comment_section(mypostviewholder holder, int position, Post_Model model) {
        int screenHeight = getScreenHeight(holder.post_pic.getContext());
        int dialogHeight = 3*screenHeight / 4;

        final DialogPlus dialogPlus = DialogPlus.newDialog(holder.post_pic.getContext())
                .setContentHolder(new ViewHolder(LayoutInflater.from(holder.post_pic.getContext()).inflate(R.layout.commenr_section,null)))
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
                Glide.with(holder.user_name.getContext()).load(dataHolder.getProfile_pic_url())
                        .fitCenter()
                        .into(user_pic);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseRecyclerOptions<Comment_Model> options1
                = new FirebaseRecyclerOptions.Builder<Comment_Model>()
                .setQuery(FirebaseDatabase.getInstance().getReference("Posts").child(getRef(position).getKey()).child("CommentList"), Comment_Model.class)
                .build();
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.user_name.getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView_comment.setLayoutManager(layoutManager);
        commentAdapter= new CommentAdapter(options1);
        recyclerView_comment.setAdapter(commentAdapter);
        startAdapterListening();

        DatabaseReference commentref=FirebaseDatabase.getInstance().getReference("Posts").child(getRef(position).getKey()).child("CommentList");
        post_comment.setOnClickListener(new View.OnClickListener() {


        DatabaseReference reff=FirebaseDatabase.getInstance().getReference("Posts");
        String postid= String.valueOf((getRef(position).getKey()));
            @Override
            public void onClick(View v) {
                String commentContent=comment_content.getText().toString();

                Comment_Model commentModel= new Comment_Model(currentuserid,commentContent,new Date(),postid,0,new HashMap<>());
                String commentid=commentref.push().getKey();
                if(!commentContent.isEmpty()) {
                    commentref.child(commentid).setValue(commentModel);


                    comment_content.setText("");
                }
                else{
                    Toast.makeText(holder.user_name.getContext(),"Please Enter Comment",Toast.LENGTH_LONG).show();
                }



            }
        });





    }

    private void startAdapterListening() {
        if (commentAdapter != null) {
            commentAdapter.startListening();
        }
    }

    // Function to stop listening when needed
    private void stopAdapterListening() {
        if (commentAdapter != null) {
            commentAdapter.stopListening();
        }
    }
    private int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }


    private void showpopupimage(mypostviewholder holder, boolean currentlike, Post_Model model,final DatabaseReference liked) {
        Log.d("duniya", "showpopupimage: ");
        HashMap<String,Object> map2= new HashMap<>();
        HashMap<String,Object> map3=new HashMap<>();
        String likeuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();
//
        if (!currentlike) {
            holder.home_post_popup_like.setVisibility(View.VISIBLE);

            Animation popupAnimation = AnimationUtils.loadAnimation(holder.post_pic.getContext(), R.anim.popup_animation);
            holder.home_post_popup_like.startAnimation(popupAnimation);

            // After 3 seconds, revert back to the original image
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                holder.home_post_popup_like.setVisibility(View.GONE);
//                        notifyItemChanged(position);
            }, 800);


            // Change the image to the new one
//        imageView.setImageResource(R.drawable.your_new_image);

            holder.like.setImageResource(R.drawable.like_s);
            map2.put("likes",model.getLikes()+1);
            liked.updateChildren(map2);

            // User hasn't liked, so like
            holder.total_like_count.setText(String.valueOf(model.getLikes() + 1));
            map3.put(likeuserid,true);
            liked.child("likedby").updateChildren(map3);
        }

    }

    public static class OnSwipeTouchListener implements View.OnTouchListener {

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

