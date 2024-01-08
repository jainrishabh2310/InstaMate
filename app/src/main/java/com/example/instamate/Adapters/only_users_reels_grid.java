package com.example.instamate.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.example.instamate.Models.Reel_Model;
import com.example.instamate.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;



public class only_users_reels_grid extends RecyclerView.Adapter<only_users_reels_grid.myviewholder> {
    Context context;
    ArrayList<Reel_Model> list;
    OnItemClickListener onItemClickListener;

    public only_users_reels_grid(Context context, ArrayList<Reel_Model> reelUrl, OnItemClickListener onItemClickListener) {

        this.onItemClickListener = onItemClickListener;
        this.list = reelUrl;
        this.context = context;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.only_user_single_reels,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position) {

        String currentid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Reel_Model model=list.get(position);



        holder.videoView.setVideoPath(model.getReel_url());
        holder.videoView.seekTo(1);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(currentid,model.getReel_url(),list.size()-1);

            }
        });




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{
        VideoView videoView;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            videoView=itemView.findViewById(R.id.only_user_single_reel);
        }


    }
    public interface OnItemClickListener {
        void onItemClick(String userId,String reelid ,int position);
    }

}
