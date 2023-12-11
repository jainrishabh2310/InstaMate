package com.example.instamate;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instamate.Models.Comment_Model;
import com.example.instamate.Models.Post_Model;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Post_add extends Fragment {
//    ImageView add_btn;
    Button add_post_uploader;
    Button add_post_next;
    ImageView add_post_pic;
    EditText add_post_caption;
    Uri pic_uri;
     public  Post_add(){

     }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_post_add, container, false);
//        add_btn=view.findViewById(R.id.add_post_btn);
        add_post_pic=view.findViewById(R.id.add_post_pic);

        add_post_next=view.findViewById(R.id.add_post_Next);


                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,1);

//        add_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent= new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent,1);
//
//            }
//        });
        add_post_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int screenHeight = getScreenHeight(getContext());
                int dialogHeight = screenHeight / 2;

                final DialogPlus dialogPlus = DialogPlus.newDialog(getContext())
                        .setContentHolder(new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.post_add_to_dailogbox,null)))
                        .setExpanded(false)
                        .setContentHeight(dialogHeight)  // Set a maximum height

                        .create();

                dialogPlus.show();

                View post_up =dialogPlus.getHolderView();

                add_post_caption=post_up.findViewById(R.id.add_post_caption);
                add_post_uploader=post_up.findViewById(R.id.add_post_uploader);
                add_post_uploader.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth mauth=FirebaseAuth.getInstance();
                        String uid=mauth.getCurrentUser().getUid();
                        FirebaseStorage storage=FirebaseStorage.getInstance();
                        StorageReference pic_upload=storage.getReference().child("user_post/"+"post"+System.currentTimeMillis());
                        pic_upload.putFile(pic_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        pic_upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String caption= add_post_caption.getText().toString().trim();
                                                DatabaseReference db= FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Posts");
                                                DatabaseReference post_db=FirebaseDatabase.getInstance().getReference("Posts");
                                                String postid=db.push().getKey();


                                                Post_Model postModel= new Post_Model(uid,uri.toString(),caption,0, new Date(),new HashMap<>());

                                                db.child(postid).setValue(postModel);
                                                post_db.child(postid).setValue(postModel);
                                                Toast.makeText(getContext(), "Post Successfully", Toast.LENGTH_LONG).show();
                                                add_post_caption.setText("");
                                                dialogPlus.dismiss();
                                                add_post_next.setVisibility(View.INVISIBLE);
//                                                add_btn.setVisibility(View.VISIBLE);
                                                add_post_pic.setVisibility(View.INVISIBLE);
                                                startActivity(new Intent(getContext(), DashBoard.class));
                                                getActivity().finish();


                                            }
                                        });



                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {





                                    }
                                });







                    }
                });







            }
        });







        return view;
    }

    public void updateimage(Uri uri){
//        Glide.with(getContext()).load(uri).into(add_post_pic);

        add_post_pic.setImageURI(uri);

    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        if(requestCode==1 && resultCode== Activity.RESULT_OK){
//            pic_uri=data.getData();
//            try {
//                add_post_next.setVisibility(View.VISIBLE);
////                add_btn.setVisibility(View.INVISIBLE);
//                add_post_pic.setVisibility(View.VISIBLE);
//
//
//                Glide.with(getContext()).load(pic_uri).into(add_post_pic);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    private int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

//    public interface UpdateImageListener{
//         void updateImage();
//    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK) {


                pic_uri = data.getData();
                Log.d("Mera h bhyi", "onActivityResult: "+pic_uri);
                updateimage(pic_uri);
//                try {
//                    Post_add firstFragment = new Post_add();
//                    getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.add_activity, firstFragment)
//                            .commit();
//
//                    // Update the image in the FirstFragment
//                    firstFragment.updateimage(uri);
//
//
//
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }


        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

