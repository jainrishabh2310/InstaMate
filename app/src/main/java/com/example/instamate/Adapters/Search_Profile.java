package com.example.instamate.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instamate.Profile_DataHolder;
import com.example.instamate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class Search_Profile extends FirebaseRecyclerAdapter<Profile_DataHolder,Search_Profile.myholder> {

    OnItemClickListener onItemClickListener;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public Search_Profile(@NonNull FirebaseRecyclerOptions<Profile_DataHolder> options,OnItemClickListener onItemClickListener) {
        super(options);
        this.onItemClickListener=onItemClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull myholder holder, @SuppressLint("RecyclerView") int position, @NonNull Profile_DataHolder model) {

        Glide.with(holder.imageView.getContext()).load(model.getProfile_pic_url())
                .centerCrop()
                .into(holder.imageView);
        holder.username.setText(model.getUsername());
        holder.bio.setText(model.getBio());
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        onItemClickListener.onItemClick(getRef(position).getKey());

    }
});




    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.search_user,parent,false);
        return new myholder(view);
    }

    class myholder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView username,bio;


        public myholder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.search_profile_pic);
            username=itemView.findViewById(R.id.search_username);
            bio=itemView.findViewById(R.id.search_bio);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(String userId);

    }
}
