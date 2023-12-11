package com.example.instamate.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

import org.ocpsoft.prettytime.PrettyTime;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Reel_Comment_Adapter extends FirebaseRecyclerAdapter<Reels_Comment_Model,Reel_Comment_Adapter.myreelcommentviewholder> {


    public Reel_Comment_Adapter(@NonNull FirebaseRecyclerOptions<Reels_Comment_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myreelcommentviewholder holder, int position, @NonNull Reels_Comment_Model model) {

        String currentuserid=model.getUserId();
        holder.rv_user_comment_content.setText(model.getCaption().toString());
        Log.d("asdfghjkl;",model.getCaption().toString());
        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Users").child(currentuserid)
                ;

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile_DataHolder dataHolder= snapshot.getValue(Profile_DataHolder.class);
                Glide.with(holder.rv_comment_user_pic.getContext()).load(dataHolder.getProfile_pic_url())
                        .fitCenter()
                        .into(holder.rv_comment_user_pic);
                holder.rv_username.setText(dataHolder.getUsername().toString());
                Log.d("qweryui",dataHolder.getUsername().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Date timestampDate = model.getTimestamp();

        PrettyTime prettyTime = new PrettyTime();
        String relativeTime = prettyTime.format(timestampDate);
        Log.d("wertyuioplkjhgfdsazxcvbn",relativeTime);
        holder.rv_user_timestamp.setText(relativeTime);

        holder.post_comment_like_count.setText(String.valueOf(model.getLikes()));



        final Map<String, Boolean>[] map = new Map[]{new HashMap<String, Boolean>()};
        DatabaseReference liked=FirebaseDatabase.getInstance().getReference("Reels").child(model.getReelid());
        DatabaseReference likeref=liked.child("CommentList").child(getRef(position).getKey());
        DatabaseReference likedcheck=likeref.child("likedby");


        String currentloginuser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final boolean[] currentuserlike = new boolean[1];
        likedcheck.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                map[0] = (Map<String, Boolean>) snapshot.getValue();
                if(map[0]==null){
                    likedcheck.setValue(new HashMap<>());
                    holder.post_comment_like.setImageResource(R.drawable.like_us);
                    currentuserlike[0]=false;

                }
                else {
                    currentuserlike[0] = map[0].containsKey(currentloginuser);
                    holder.post_comment_like.setImageResource(currentuserlike[0] ? R.drawable.reel_like_s : R.drawable.like_us);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        HashMap<String,Object> map1= new HashMap<>();
        HashMap<String,Object> map2= new HashMap<>();
        HashMap<String,Object> map3= new HashMap<>();




        holder.post_comment_like.setOnClickListener(v -> {
            if (currentuserlike[0]) {
                holder.post_comment_like.setImageResource(R.drawable.like_us);

                // User already liked, so unlike
                holder.post_comment_like_count.setText(String.valueOf(model.getLikes() - 1));
                map1.put("likes",model.getLikes()-1);
                likeref.updateChildren(map1);
                likeref.child("likedby").child(currentloginuser).removeValue();
            } else {
                holder.post_comment_like.setImageResource(R.drawable.like_s);
                map2.put("likes",model.getLikes()+1);
                likeref.updateChildren(map2);

                // User hasn't liked, so like
                holder.post_comment_like_count.setText(String.valueOf(model.getLikes() + 1));
//                        model.getLikedBy().put(likeuserid, true);
                map3.put(currentloginuser,true);
                likeref.child("likedby").updateChildren(map3);


            }




        });




    }

    @NonNull
    @Override
    public myreelcommentviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_comment,parent,false);
        return new myreelcommentviewholder(view);
    }

    class myreelcommentviewholder extends RecyclerView.ViewHolder{
        CircleImageView rv_comment_user_pic;
        TextView rv_user_comment_content;
        TextView rv_username;
        TextView rv_user_timestamp;

        ImageView post_comment_like;
        TextView post_comment_like_count;



        public myreelcommentviewholder(@NonNull View itemView) {
            super(itemView);

            rv_comment_user_pic =itemView.findViewById(R.id.rv_user_pic);
            rv_username=itemView.findViewById(R.id.rv_user_username);
            rv_user_comment_content=itemView.findViewById(R.id.rv_user_content);
            rv_user_timestamp=itemView.findViewById(R.id.rv_user_timestamp);
            post_comment_like=itemView.findViewById(R.id.post_comment_like_btn);
            post_comment_like_count=itemView.findViewById(R.id.post_comment_like_count);

        }
    }

}
