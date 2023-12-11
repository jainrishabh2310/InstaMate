package com.example.instamate.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instamate.Models.Chat_Message_Model;
import com.example.instamate.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Chatting_Adapter extends RecyclerView.Adapter {
    ArrayList<Chat_Message_Model> list;
    Context context;
    int ITEM_SEND=1;
    int ITEM_RECIEVE=2;

    public Chatting_Adapter(ArrayList<Chat_Message_Model> list, Context applicationContext) {

        this.list=list;
        this.context=applicationContext;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {



        if(viewType==ITEM_SEND){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_chat,parent,false);
            return new Senderviewholder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_chat,parent,false);
            return new Receiverviewholder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Chat_Message_Model messages = list.get(position);
        Log.d("meesage", "onBindViewHolder: "+list.get(position));
        if(holder.getClass()==Senderviewholder.class){
            Senderviewholder viewHolder = (Senderviewholder) holder;
            viewHolder.sendmessage.setText(messages.getMessage());
        }
        else {
            Receiverviewholder viewHolder = (Receiverviewholder) holder;
            viewHolder.receivermessage.setText(messages.getMessage());
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {
        Chat_Message_Model messages = list.get(position);
        if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderuid())){
            return ITEM_SEND;
        }
        else return ITEM_RECIEVE;
    }
    class Senderviewholder extends RecyclerView.ViewHolder{
        TextView sendmessage;

        public Senderviewholder(@NonNull View itemView) {
            super(itemView);
            sendmessage=itemView.findViewById(R.id.sender_message);
        }
    }

    class Receiverviewholder extends RecyclerView.ViewHolder{
        TextView receivermessage;
        public Receiverviewholder(@NonNull View itemView) {
            super(itemView);
            receivermessage=itemView.findViewById(R.id.receiver_message);

        }
    }
}
