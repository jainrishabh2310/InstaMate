package com.example.instamate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instamate.Models.Post_Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class onlypost_1st_Adapter extends RecyclerView.Adapter<onlypost_1st_Adapter.mysingleimagepost> {

//    private AdapterView.OnItemClickListener onItemClickListener;
    private OnItemClickListener onItemClickListener;
    ArrayList images;

    public onlypost_1st_Adapter(Context context,ArrayList images,OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        this.images = images;
        this.context = context;
    }

    Context context;




    @NonNull
    @Override
    public mysingleimagepost onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.only_user_post_images,parent,false);
        return new mysingleimagepost(view);
    }

    @Override
    public void onBindViewHolder(@NonNull mysingleimagepost holder, @SuppressLint("RecyclerView") int position) {
        String userid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        Glide.with(context).load(images.get(position)).centerCrop().
                into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(userid, (String) images.get(position),images.size()-1-position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return images.size();
    }


    class  mysingleimagepost extends RecyclerView.ViewHolder{

        ImageView imageView;

        public mysingleimagepost(@NonNull View itemView) {
            super(itemView);

            imageView=itemView.findViewById(R.id.only_user_post_single_image);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(String userId, String postId, int position);
    }


}
