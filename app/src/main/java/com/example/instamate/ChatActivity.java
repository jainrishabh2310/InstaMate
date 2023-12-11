package com.example.instamate;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instamate.Adapters.Chat_Notes_Adapter;
import com.example.instamate.Adapters.Chat_Profile_Adapter;
import com.example.instamate.Models.Notes_Model;
import com.example.instamate.Models.Post_Model;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    private GestureDetector gestureDetector;

    ImageView current_user;
    TextView current_username;

    Chat_Profile_Adapter chatProfileAdapter;
    RecyclerView chat_alluser_rv;


    ArrayList<String> list;

    EditText search_user_chats;

    RecyclerView chat_user_notes_rv;

    TextView chat_current_user_note;

    Chat_Notes_Adapter chatNotesAdapter;

    ArrayList<Notes_Model> noteholder;
    String currentuserid;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
//        gestureDetector=new GestureDetect/or(this,new SwipeGesture);

        current_user=findViewById(R.id.chat_current_userpic);
        current_username=findViewById(R.id.chat_current_username);
        chat_alluser_rv=findViewById(R.id.chat_allusers_rv);
        search_user_chats=findViewById(R.id.search_users_chats);
        chat_user_notes_rv=findViewById(R.id.chat_notes_recview);
        chat_current_user_note=findViewById(R.id.chat_current_user_note);
        list=new ArrayList<>();

         currentuserid= FirebaseAuth.getInstance().getCurrentUser().getUid();


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Users")
                .child(currentuserid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile_DataHolder profileDataHolder=snapshot.getValue(Profile_DataHolder.class);
                if(profileDataHolder!=null){
                current_username.setText(profileDataHolder.getUsername().toString());
                Glide.with(getApplicationContext())
                        .load(profileDataHolder.profile_pic_url)
                        .centerCrop()
                        .into(current_user);



                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        chatProfileAdapter= new Chat_Profile_Adapter(list,getApplicationContext());

        chat_alluser_rv.setLayoutManager(new LinearLayoutManager(this));
        chat_alluser_rv.setAdapter(chatProfileAdapter);

        chat_user_notes_rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        noteholder= new ArrayList<>();


        chatNotesAdapter= new Chat_Notes_Adapter(noteholder,getApplicationContext());
        chat_user_notes_rv.setAdapter(chatNotesAdapter);
        FirebaseDatabase.getInstance().getReference("Notes")
                .orderByKey().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        noteholder.clear();
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            Log.d("notesbole tho", "onDataChange: ");
                            Notes_Model model = snapshot1.getValue(Notes_Model.class);
                            Log.d("notesbole tho", "onDataChange: "+model);

                            if (snapshot1.getKey().equals(currentuserid)) continue;

                            noteholder.add(model);
                            Log.d("notesbole tho", "onDataChange: "+noteholder.size());



                        }
                        chatNotesAdapter.notifyDataSetChanged();






                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });









        DatabaseReference fetchall= FirebaseDatabase.getInstance().getReference("Users");
        fetchall.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot value:snapshot.getChildren()){
                    if(!currentuserid.equals(value.getKey())){
                    Log.d(TAG, "onDataChange: "+value.getKey());


                        list.add(value.getKey());
                    }



                }
                chatProfileAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





//        FirebaseRecyclerOptions<Profile_DataHolder> options
//                = new FirebaseRecyclerOptions.Builder<Profile_DataHolder>()
//                .setQuery(FirebaseDatabase.getInstance().getReference("Users"), Profile_DataHolder.class)
//                .build();







        search_user_chats.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                list.clear();
                String str=s.toString().trim();
                if(!str.isEmpty()){


                    FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(str).endAt(str+"\uf8ff")
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot id : snapshot.getChildren()){
                                        list.add(id.getKey());
                                        chatProfileAdapter.notifyDataSetChanged();

                                    }






                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });




                }
                else{

                    fetchall.orderByKey().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            list.clear();
                            for(DataSnapshot value:snapshot.getChildren()){
                                if(!currentuserid.equals(value.getKey())){
                                    Log.d(TAG, "onDataChange: "+value.getKey());


                                    list.add(value.getKey());
                                }



                            }
                            chatProfileAdapter.notifyDataSetChanged();



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
FirebaseDatabase.getInstance().getReference("Users").child(currentuserid).child("notes").addValueEventListener(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {

        if(snapshot.exists()){
            String note=snapshot.getValue(String.class);
            chat_current_user_note.setText(note);
        }

    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});






        chat_current_user_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesupload(currentuserid);


            }
        });
        current_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notesupload(currentuserid);

            }
        });



    }

    private void notesupload(String userid) {
        final DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(LayoutInflater.from(this).inflate(R.layout.note_upload_layout,null)))
                .setExpanded(true)
                .create();

        dialogPlus.show();

        View notes =dialogPlus.getHolderView();
        ImageView imageView=notes.findViewById(R.id.notes_current_pic);
        EditText text=notes.findViewById(R.id.notes_text);
        ImageView send=notes.findViewById(R.id.note_upload_to_db);

        FirebaseDatabase.getInstance().getReference("Users").child(userid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profile_DataHolder profileDataHolder=snapshot.getValue(Profile_DataHolder.class);
                        Glide.with(getApplicationContext())
                                .load(profileDataHolder.getProfile_pic_url())
                                .into(imageView);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String str = text.getText().toString();
                if (str.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Enter text",Toast.LENGTH_LONG).show();


                } else {
                    FirebaseDatabase.getInstance().getReference("Users").child(userid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Profile_DataHolder profileDataHolder = snapshot.getValue(Profile_DataHolder.class);
                            String pic=profileDataHolder.getProfile_pic_url();
                            String username=profileDataHolder.getUsername();
                            Notes_Model notesModel= new Notes_Model(username,pic,str);
                            FirebaseDatabase.getInstance().getReference("Notes")
                                    .child(userid).setValue(notesModel);
                            HashMap<String,Object> map= new HashMap<>();
                            map.put("notes",str);

                            FirebaseDatabase.getInstance().getReference("Users")
                                            .child(userid).updateChildren(map);



                            text.setText("");
                            dialogPlus.dismiss();





                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                }
            }
        });






    }

    @Override
    protected void onStart() {
        super.onStart();
        HashMap<String,Object> map= new HashMap<>();
        map.put("Status","online");
        FirebaseDatabase.getInstance().getReference("Users")
                .child(currentuserid).updateChildren(map);



    }

    @Override
    protected void onStop() {
        super.onStop();
        HashMap<String,Object> map= new HashMap<>();
        map.put("Status","offline");
        FirebaseDatabase.getInstance().getReference("Users")
                .child(currentuserid).updateChildren(map);



    }
}