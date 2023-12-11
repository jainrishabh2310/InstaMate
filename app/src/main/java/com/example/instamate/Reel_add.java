package com.example.instamate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.inputmethodservice.Keyboard;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.instamate.Models.Post_Model;
import com.example.instamate.Models.Reel_Model;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.Date;
import java.util.HashMap;


public class Reel_add extends Fragment {

    Button add_reel_next;
//    ImageView add_reel_btn;
    VideoView add_reel_pic;
    Button add_post_uploader;
    EditText add_post_caption;
    private static final int REQUEST_VIDEO_PICK = 1;
    private static final int MAX_VIDEO_DURATION = 60000;
    Uri uri;
    LinearLayout videoholder;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_reel_add, container, false);
//        add_reel_btn=view.findViewById(R.id.add_reel_btn);
        add_reel_next=view.findViewById(R.id.add_reel_Next);
        add_reel_pic=view.findViewById(R.id.add_reel_pic);
        videoholder=view.findViewById(R.id.videoholder);

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_VIDEO_PICK);


//        add_reel_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        add_reel_next.setOnClickListener(new View.OnClickListener() {
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

                        ProgressDialog progressDialog=new ProgressDialog(getContext());
                        progressDialog.setCancelable(false);
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        FirebaseAuth mauth=FirebaseAuth.getInstance();
                        String uid=mauth.getCurrentUser().getUid();
                        FirebaseStorage storage=FirebaseStorage.getInstance();
                        StorageReference pic_upload=storage.getReference().child("user_reel/"+"reel"+System.currentTimeMillis());
                        pic_upload.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        pic_upload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                progressDialog.dismiss();
                                                String caption= add_post_caption.getText().toString().trim();
                                                DatabaseReference db= FirebaseDatabase.getInstance().getReference("Users").child(uid).child("Reels");
                                                DatabaseReference reel_db=FirebaseDatabase.getInstance().getReference("Reels");
                                                String reelid=db.push().getKey();

                                                Reel_Model ReelModel= new Reel_Model(uid,caption,uri.toString(),0, new Date(),new HashMap<>());
                                                Reel_Model model= new Reel_Model(uri.toString());

                                                DatabaseReference rf=FirebaseDatabase.getInstance().getReference("ReelsUrl");
                                                rf.child(reelid).setValue(model);

                                                db.child(reelid).setValue(ReelModel);
                                                reel_db.child(reelid).setValue(ReelModel);
                                                Toast.makeText(getContext(), "Post Successfully", Toast.LENGTH_LONG).show();
                                                add_post_caption.setText("");
                                                dialogPlus.dismiss();
                                                add_reel_next.setVisibility(View.INVISIBLE);
//                                                add_reel_btn.setVisibility(View.VISIBLE);
                                                add_reel_pic.setVisibility(View.INVISIBLE);
                                                startActivity(new Intent(getContext(), DashBoard.class));
                                                getActivity().finish();

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








            }
        });





        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_PICK && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();

            if (uri != null && isVideoDurationValid(uri)) {
//                add_reel_btn.setVisibility(View.INVISIBLE);
                add_reel_pic.setVisibility(View.VISIBLE);
                add_reel_next.setVisibility(View.VISIBLE);
                videoholder.setVisibility(View.VISIBLE);

                add_reel_pic.setVideoURI(uri);
                add_reel_pic.start();

                add_reel_pic.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        // Restart the video when it completes
                        add_reel_pic.seekTo(1); // Seek to the beginning (or use 0 for older Android versions)
                        add_reel_pic.start();
                    }
                });







                // Process the selected video
                // Your code here
            } else {
                // Video duration exceeds the allowed limit
                // Show an error message or take appropriate action
            }
        } else {
            // Handle other cases
        }
    }

    private boolean isVideoDurationValid(Uri videoUri) {
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(getContext(), videoUri);

            // Extract duration in milliseconds
            String durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long duration = Long.parseLong(durationString);

            // Check if the duration is within the allowed limit
            return duration <= MAX_VIDEO_DURATION;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

}