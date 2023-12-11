package com.example.instamate.Adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instamate.Models.Post_Model;
import com.example.instamate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Search_Image extends FirebaseRecyclerAdapter<Post_Model,Search_Image.myholder> {


    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Search_Image(@NonNull FirebaseRecyclerOptions<Post_Model> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myholder holder, int position, @NonNull Post_Model model) {
        Glide.with(holder.imageView.getContext()).load(model.getPost_url())
                .centerCrop()
                .into(holder.imageView);



    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.only_user_post_images,parent,false);
        return new myholder(view);
    }

    class myholder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public myholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.only_user_post_single_image);
        }
    }
}
