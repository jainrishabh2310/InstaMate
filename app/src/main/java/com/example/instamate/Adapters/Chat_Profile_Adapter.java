package com.example.instamate.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instamate.Chatting_Users;
import com.example.instamate.Models.Chat_Message_Model;
import com.example.instamate.Profile_DataHolder;
import com.example.instamate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.ocpsoft.prettytime.PrettyTime;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

public class Chat_Profile_Adapter extends RecyclerView.Adapter<Chat_Profile_Adapter.myviewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */


    ArrayList<String> list;
    Context context;
    public Chat_Profile_Adapter( ArrayList<String> list,Context context) {
        this.list=list;
        this.context=context;

    }



    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_chat,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {
      String currentuserid=FirebaseAuth.getInstance().getCurrentUser().getUid();
       String userids=list.get(position);
        FirebaseDatabase.getInstance()
                        .getReference("Users")
                                .child(userids)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                Profile_DataHolder model=snapshot.getValue(Profile_DataHolder.class);
                                                holder.textView.setText(model.getUsername().toString());
                                                Glide.with(holder.imageView.getContext())
                                                        .load(model.getProfile_pic_url())
                                                        .centerCrop()
                                                        .into(holder.imageView);



                                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        Intent intent=new Intent(holder.imageView.getContext(), Chatting_Users.class);
                                                        intent.putExtra("Uid", list.get(position));
                                                        Log.d("Userid", "onClick: "+list.get(position));
                                                        holder.itemView.getContext().startActivity(intent);



                                                    }
                                                });



                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });


        FirebaseDatabase.getInstance().getReference("Users").child(userids).child("Status")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()) {
                            String status = snapshot.getValue(String.class);
                            if (status.equals("online")) {
                                holder.status.setVisibility(View.VISIBLE);
                            } else {
                                holder.status.setVisibility(View.GONE);
                            }
                        }
                        else{
                            holder.status.setVisibility(View.GONE);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Lastseen").child(currentuserid).child(userids);
               ref.child("lastid").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String lastids= (String) snapshot.getValue();
                            Log.d("Lastid", "onDataChange: "+lastids);
                            FirebaseDatabase.getInstance().getReference("Chats").child(currentuserid).child(userids).child(lastids).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()) {
                                        Log.d("snapshotvalue", "onDataChange: " + snapshot);
                                        Chat_Message_Model model = snapshot.getValue(Chat_Message_Model.class);
                                        if (model.getSenderuid().equals(currentuserid)) {
                                            Date timestampDate = model.getTimestmap();

                                            PrettyTime prettyTime = new PrettyTime();
                                            String relativeTime = prettyTime.format(timestampDate);
                                            holder.chat_profile_latest_news.setText("Sent " + relativeTime);

                                        } else {
                                            holder.chat_profile_latest_news.setText(model.getMessage());
                                        }


                                    }
                                    else{
                                        holder.chat_profile_latest_news.setText("No messages");
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                        }
                        else{
                            holder.chat_profile_latest_news.setText("No messages");
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });







    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView textView;

        MaterialCardView status;
        TextView chat_profile_latest_news;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.chat_profile_pic);
            textView=itemView.findViewById(R.id.chat_profile_name);
            status=itemView.findViewById(R.id.status);
            chat_profile_latest_news=itemView.findViewById(R.id.chat_profile_latest_news);
        }
    }

}
