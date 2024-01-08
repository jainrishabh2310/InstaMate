package com.example.instamate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instamate.Adapters.Chatting_Adapter;
import com.example.instamate.Models.Chat_Message_Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Chatting_Users extends AppCompatActivity {



    ImageView receiver,Sender;
    TextView rec_text,Message;

    RecyclerView recyclerView;

    ArrayList<Chat_Message_Model> list;
    Chatting_Adapter chattingAdapter;

    ImageView sendmessage;
    String currentuid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_users);
        final String[] SenderName = {""};
        final String[] ReceiverName = {""};

        Intent intent=getIntent();
       String receiveruid= intent.getStringExtra("Uid");
        currentuid= FirebaseAuth.getInstance().getCurrentUser().getUid();

       receiver=findViewById(R.id.chat_receiver_profile_pic);
       Sender=findViewById(R.id.chat_sender_profile_pic);

       rec_text=findViewById(R.id.chat_receiver_username);
       Message=findViewById(R.id.chat_message);
       recyclerView=findViewById(R.id.chat_two_users_recview);
       sendmessage=findViewById(R.id.send_meesage);
       list= new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        chattingAdapter= new Chatting_Adapter(list,getApplicationContext());
        recyclerView.setAdapter(chattingAdapter);


        final String[] send = {""};
        final String[] receiver1 = {""};






        DatabaseReference reference_receiver= FirebaseDatabase.getInstance().getReference("Users")
                .child(receiveruid);
        reference_receiver.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile_DataHolder dataHolder=snapshot.getValue(Profile_DataHolder.class);
                rec_text.setText(dataHolder.getUsername().toString());
                Glide.with(getApplicationContext())
                        .load(dataHolder.getProfile_pic_url().toString())
                        .centerCrop()
                        .into(receiver);
                ReceiverName[0] =dataHolder.getUsername().toString().trim();
                receiver1[0] =ReceiverName[0]+receiveruid;



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference sender=FirebaseDatabase.getInstance().getReference("Users").child(currentuid);
        sender.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Profile_DataHolder dataHolder=snapshot.getValue(Profile_DataHolder.class);
                Glide.with(getApplicationContext())
                        .load(dataHolder.getProfile_pic_url().toString())
                        .centerCrop()
                        .into(Sender);
                SenderName[0] =dataHolder.getUsername().toString().trim();
                send[0] =SenderName[0]+currentuid;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=Message.getText().toString();
                if(!message.isEmpty()){
                    DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");

                    Chat_Message_Model model= new Chat_Message_Model(currentuid,message,new Date());

                    String uniqueid=reference.push().getKey();
                    reference.child(currentuid).child(receiveruid).child(uniqueid).setValue(model);
                    reference.child(receiveruid).child(currentuid).child(uniqueid).setValue(model);


                    sendNotification(message,currentuid,receiveruid);




                    HashMap<String ,Object> map = new HashMap<>();
                    map.put("lastid",uniqueid);

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Lastseen");
                    ref.child(receiveruid).child(currentuid).updateChildren(map);
                    ref.child(currentuid).child(receiveruid).updateChildren(map);

                    Message.setText("");














                }
                else{
                    Toast.makeText(Chatting_Users.this,"Please Enter Message",Toast.LENGTH_LONG).show();
                }



            }
        });

        Log.d("123456789", "onDataChange: "+send[0]);
        Log.d("123456789", "onDataChange: "+receiver1[0]);



        DatabaseReference chatting=FirebaseDatabase.getInstance().getReference("Chats")
                .child(currentuid).child(receiveruid);

        chatting.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                Log.d("123456789", "onDataChange: "+snapshot.getChildrenCount());
                for(DataSnapshot value:snapshot.getChildren()){
                  Chat_Message_Model model=value.getValue(Chat_Message_Model.class);
                    Log.d("78787878", "onDataChange: ");
                  list.add(model);
                  recyclerView.scrollToPosition(chattingAdapter.getItemCount()-1);

                }
                chattingAdapter.notifyDataSetChanged();




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });












    }

    private void sendNotification(String message, String currentuser, String receiveruid) {

        FirebaseDatabase.getInstance().getReference("Users").child(currentuser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            Profile_DataHolder dataHolder=snapshot.getValue(Profile_DataHolder.class);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(receiveruid).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){

                                                String token=snapshot.child("FCM_token").getValue(String.class);







                                                try {


                                                    JSONObject jsonObject= new JSONObject();

                                                    JSONObject notiificationobj= new JSONObject();
                                                    notiificationobj.put("title",dataHolder.getUsername());
                                                    notiificationobj.put("body",message);
                                                    notiificationobj.put("sound", com.google.firebase.messaging.R.raw.firebase_common_keep); // Set the sound
                                                    notiificationobj.put("icon",R.drawable.instagram);


                                                    JSONObject dataobj= new JSONObject();
                                                    dataobj.put("Uid",currentuser);

                                                    jsonObject.put("notification",notiificationobj);
                                                    jsonObject.put("data",dataobj);
                                                    jsonObject.put("to",token);


                                                    // Additional settings for the notification
                                                    JSONObject androidObj = new JSONObject();
                                                    androidObj.put("visibility", "public"); // Make the notification visible on the lock screen

                                                    androidObj.put("priority", "max"); // Set notification priority
                                                    notiificationobj.put("android", androidObj);
                                                    callApi(jsonObject);


                                                }
                                                catch (Exception e){

                                                }




                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });








                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });





    }
    void callApi(JSONObject jsonobj){

        MediaType JSON = MediaType.get("application/json");

        OkHttpClient client = new OkHttpClient();
        String url="https://fcm.googleapis.com/fcm/send";
        RequestBody requestBody= RequestBody.create(jsonobj.toString(),JSON);
        Request request= new Request.Builder()
                .url(url)
                .post(requestBody)
                .header("Authorization","Bearer AAAAkj6qdn4:APA91bHzNgXsK85B6GJAwViNetgJaRCrnA5ApvQ8rVnivPmiu69u3j1yCN_MVbpEdXE5q_0cJ9oHmg4ubein2RoQAZKBK1saXf6vwc7M2V6uJqjzH-7E2FYw8GJzXERBPsiWyNyPXWc0")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

            }
        });
    }



    protected void onStart() {
        super.onStart();

        HashMap<String,Object> map= new HashMap<>();
        map.put("Status","online");
        FirebaseDatabase.getInstance().getReference("Users")
                .child(currentuid).updateChildren(map);



    }
    protected void onStop() {
        super.onStop();
        HashMap<String,Object> map= new HashMap<>();
        map.put("Status","offline");
        FirebaseDatabase.getInstance().getReference("Users")
                .child(currentuid).updateChildren(map);



    }

}