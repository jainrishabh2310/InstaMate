package com.example.instamate;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registration_users extends AppCompatActivity {
    CircleImageView profile_pic;
    TextInputEditText username,gender,dob,bio;
    AppCompatButton complete;
    Uri uri;
    FirebaseStorage firebaseStorage;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_users);
        profile_pic=findViewById(R.id.profile_pic);
        username=findViewById(R.id.username);
        gender=findViewById(R.id.gender);
        dob=findViewById(R.id.dob);
        bio=findViewById(R.id.bio);
        complete=findViewById(R.id.complete);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Select Image"),1);
//
//                Dexter.withContext(getApplicationContext()).withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
//                        .withListener(new PermissionListener() {
//                            @Override
//                            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
//
//
//                            }
//
//                            @Override
//                            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
//
//                            }
//
//                            @Override
//                            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                                permissionToken.continuePermissionRequest();
//                            }
//                        }).check();
            }
        });


        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StorageReference storageReference=FirebaseStorage.getInstance().getReference().child("user_profile/"+"image1"+new Random().nextInt(100)+".jpg");
                storageReference.putFile(uri)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        DatabaseReference db= FirebaseDatabase.getInstance().getReference("Users");
                                        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        String Username=username.getText().toString().trim();
                                        String Gender=gender.getText().toString().trim();
                                        String Dob=dob.getText().toString().trim();
                                        String Bio=bio.getText().toString().trim();
                                        Profile_DataHolder dataHolder= new Profile_DataHolder(Username,Gender,Dob,Bio,uri.toString());
                                        if(Username.isEmpty() || Gender.isEmpty() || Dob.isEmpty() || Bio.isEmpty()){
                                            Toast.makeText(getApplicationContext(),"Please fill all Details",Toast.LENGTH_LONG).show();
                                        }
                                        else {

                                            db.child(uid).setValue(dataHolder);
                                            startActivity(new Intent(Registration_users.this, DashBoard.class));
                                            finish();
                                        }

                                    }
                                });



                                Toast.makeText(getApplicationContext(),"File uploaded",Toast.LENGTH_LONG).show();

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode==RESULT_OK){
            uri=data.getData();
            try {
                Glide.with(getApplicationContext()).load(uri).into(profile_pic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}