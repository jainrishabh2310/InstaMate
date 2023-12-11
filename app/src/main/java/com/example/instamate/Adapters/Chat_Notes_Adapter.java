package com.example.instamate.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instamate.Models.Notes_Model;
import com.example.instamate.Profile_DataHolder;
import com.example.instamate.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class Chat_Notes_Adapter extends RecyclerView.Adapter<Chat_Notes_Adapter.myviewholder>{
ArrayList<Notes_Model> list;
Context context;

    public Chat_Notes_Adapter(ArrayList<Notes_Model> noteholder, Context applicationContext) {
        this.list=noteholder;
        this.context=applicationContext;
    }


    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notes_single_layout,parent,false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        Log.d("Noteskadata", "onBindViewHolder: "+"data aarha h kbjkdf");
        Notes_Model model=list.get(position);
        Glide.with(context).load(model.getPic()).centerCrop()
                .into(holder.user_pic);
        holder.username.setText(model.getUsername().toString());
        holder.notes.setText(model.getText().toString());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class myviewholder extends RecyclerView.ViewHolder{


        ImageView user_pic;
        TextView username;
        TextView notes;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            user_pic=itemView.findViewById(R.id.chat_user_note_pic);
            username=itemView.findViewById(R.id.chat_user_note_username);
            notes=itemView.findViewById(R.id.chat_single_user_note);






        }
    }
}



