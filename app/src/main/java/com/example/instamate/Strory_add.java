package com.example.instamate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.instamate.Models.Story_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;
import java.util.HashMap;


public class Strory_add extends Fragment {

    ImageView add_btn;
    Button add_story_uploader;
    ImageView add_story_pic;
    Uri storyuri;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_strory_add, container, false);

        add_story_pic=view.findViewById(R.id.add_story_pic);

        add_story_uploader=view.findViewById(R.id.add_story_uploader);
        Intent intent= new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,1);


        add_story_uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog=new ProgressDialog(getContext());
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Stories");
                String currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        String storyid=reference.child(currentuser).push().getKey();
                        FirebaseStorage storage=FirebaseStorage.getInstance();
                        StorageReference pic_upload=storage.getReference().child("user_story/"+"story"+System.currentTimeMillis());

                        pic_upload.putFile(storyuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                pic_upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        progressDialog.dismiss();


                                        DatabaseReference reference1= FirebaseDatabase.getInstance().getReference("Users");


                                        Story_Model storyModel= new Story_Model(currentuser,storyid,new Date(),uri.toString());
                                        reference1.child(currentuser).child("Story").child(storyid).setValue(storyModel);
                                        reference.child(currentuser).child(storyid).setValue(storyModel);
                                        startActivity(new Intent(getContext(), DashBoard.class));
                                        getActivity().finish();

//                                        DatabaseReference reference2 =FirebaseDatabase.getInstance().getReference("AllStorySeen");
//                                        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                for(DataSnapshot uniqueid : snapshot.getChildren()) {
//                                                    String unique = uniqueid.getKey();
//                                                    Log.d("09876543qrtyuiop;lkjhgfdzxcvbn", "onDataChange: "+unique);
//                                                    if (!unique.equals(currentuser)) {
//                                                        reference2.child(unique).addValueEventListener(new ValueEventListener() {
//                                                            @Override
//                                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                                if (snapshot.exists()) {
//                                                                    HashMap<String, Object> mapp = new HashMap<>();
//                                                                    mapp.put(currentuser, false);
//                                                                    reference2.child(unique).updateChildren(mapp);
//
//
//                                                                }
//
//                                                            }
//
//                                                            @Override
//                                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                                            }
//                                                        });
//
//
//                                                    }
//                                                }
//
//
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });









                                    }
                                });

                            }
                        })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                        float per=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                                        progressDialog.setMessage("Uploaded"+(int)per+"%");

                                    }
                                });


                    }


        });




        return view;
    }

        public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode== Activity.RESULT_OK){
         storyuri =data.getData();
            try {
                add_story_uploader.setVisibility(View.VISIBLE);
                add_story_pic.setVisibility(View.VISIBLE);
//                add_btn.setVisibility(View.INVISIBLE);
                add_story_pic.setImageURI(storyuri);

//                Glide.with(getContext()).load(storyuri).into(add_story_pic);


            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }


    }
}