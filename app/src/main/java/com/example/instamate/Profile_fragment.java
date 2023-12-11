package com.example.instamate;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.instamate.Adapters.ProfilePagerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_fragment extends Fragment {

 TextView pf_username,pf_bio;
 TabLayout tabLayout;
 ViewPager2 viewPager2;
 CircleImageView pf_pic;
 Uri uri;
 CircleImageView edit_profile_pic;
 TextView edit_pro_btn;
 EditText edit_profile_name;
 EditText edit_profile_gender;
 EditText edit_profile_dob;
 EditText edit_profile_bio;
 AppCompatButton edit_profile_upload;
 HashMap<String,Object> updatedata;
 DialogPlus dialogPlus;
    BottomNavigationView bottomNavigationView;

    TextView profile_f_post_count;

    TextView shared_profile;

 AppCompatButton edit_profile_uploader;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile_fragment, container, false);
        bottomNavigationView = getActivity().findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().findItem(R.id.navigation_home).setIcon(R.drawable.home_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_profile).setIcon(R.drawable.user_s);
        bottomNavigationView.getMenu().findItem(R.id.navigation_add).setIcon(R.drawable.add_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_search).setIcon(R.drawable.search_us);
        bottomNavigationView.getMenu().findItem(R.id.navigation_reel).setIcon(R.drawable.reel_us);
        profile_f_post_count=view.findViewById(R.id.profile_fragment_post_count);

        pf_username=view.findViewById(R.id.profile_fragment_username);
        pf_pic=view.findViewById(R.id.profile_fragment_pic);
        pf_bio=view.findViewById(R.id.profile_fragment_bio);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager2 = view.findViewById(R.id.viewPager);
        edit_profile_uploader=view.findViewById(R.id.edit_profile_uploader);
        shared_profile=view.findViewById(R.id.shared_profile);

        Shader shader= new LinearGradient(
                90,90,
                0,shared_profile.getLineHeight(),
                new int[]{
                        Color.parseColor("#02bfcc"),
                        Color.parseColor("#7d2ae6")


                }, null,Shader.TileMode.REPEAT
        );
        shared_profile.getPaint().setShader(shader);



        updatedata= new HashMap<>();


        ProfilePagerAdapter adapter = new ProfilePagerAdapter(getActivity());
        viewPager2.setAdapter(adapter);
        viewPager2.setUserInputEnabled(true);
        viewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);

        new TabLayoutMediator(tabLayout, viewPager2,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setIcon(R.drawable.add_s);
                            setTabIconColor(tab, Color.BLACK); // Set your desired color
                            break;
                        case 1:
                            tab.setIcon(R.drawable.reel_s);
                            setTabIconColor(tab, Color.BLACK); // Set your desired color
                            break;
                    }
                }
        ).attach();


        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        String uid=mAuth.getCurrentUser().getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile_DataHolder dataHolder= snapshot.getValue(Profile_DataHolder.class);
                pf_username.setText(dataHolder.getUsername().toString());
                pf_bio.setText(dataHolder.getBio().toString());
                String url=dataHolder.getProfile_pic_url().toString();
                Glide.with(getContext()).load(url).into(pf_pic);
                usersRef.child("Posts").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        int count= (int) snapshot.getChildrenCount();
                        profile_f_post_count.setText(count+"");


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        edit_profile_uploader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogPlus = DialogPlus.newDialog(getContext())
                        .setContentHolder(new ViewHolder(LayoutInflater.from(getContext()).inflate(R.layout.edit_profile_db,null)))
                        .setExpanded(false)
                        .setContentHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .create();

                dialogPlus.show();

                View edit_view=dialogPlus.getHolderView();
                edit_profile_pic=edit_view.findViewById(R.id.edit_profile_pic);
                edit_pro_btn=edit_view.findViewById(R.id.edit_profile_pic_btn);
                edit_profile_name=edit_view.findViewById(R.id.edit_profile_name);
                 edit_profile_gender=edit_view.findViewById(R.id.edit_profile_gender);
                 edit_profile_dob=edit_view.findViewById(R.id.edit_profile_dob);
                 edit_profile_bio=edit_view.findViewById(R.id.edit_profile_bio);
                edit_profile_upload=edit_view.findViewById(R.id.edit_profile_upload);
                usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Profile_DataHolder dataHolder= snapshot.getValue(Profile_DataHolder.class);
                        Glide.with(getContext()).load(dataHolder.getProfile_pic_url()).into(edit_profile_pic);
                        edit_profile_name.setText(dataHolder.getUsername().toString());
                        edit_profile_gender.setText(dataHolder.getGender().toString());
                        edit_profile_dob.setText(dataHolder.getDob().toString());
                        edit_profile_bio.setText(dataHolder.getBio().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                edit_profile_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geturidata();

                    }
                });

                edit_pro_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geturidata();
                    }
                });
                edit_profile_upload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                     if(uri!=null) {
                         StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("user_profile/" + "image1" + new Random().nextInt(100) + ".jpg");
                         storageReference.putFile(uri)
                                 .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                     @Override
                                     public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                         storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                             @Override
                                             public void onSuccess(Uri uri) {
                                                 String username = edit_profile_name.getText().toString();
                                                 String dob = edit_profile_dob.getText().toString();
                                                 String gender = edit_profile_gender.getText().toString();
                                                 String bio = edit_profile_bio.getText().toString();
                                                 updatedata.put("username", username);
                                                 updatedata.put("bio", bio);
                                                 updatedata.put("gender", gender);
                                                 updatedata.put("dob", dob);

                                                 DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
                                                 db.child(uid).updateChildren(updatedata);
                                                 Toast.makeText(getContext(), "Update data SuccessFully", Toast.LENGTH_LONG).show();
                                                 dialogPlus.dismiss();

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
                     else{
                         String username = edit_profile_name.getText().toString();
                         String dob = edit_profile_dob.getText().toString();
                         String gender = edit_profile_gender.getText().toString();
                         String bio = edit_profile_bio.getText().toString();
                         updatedata.put("username", username);
                         updatedata.put("bio", bio);
                         updatedata.put("gender", gender);
                         updatedata.put("dob", dob);

                         DatabaseReference db = FirebaseDatabase.getInstance().getReference("Users");
                         db.child(uid).updateChildren(updatedata);
                         Toast.makeText(getContext(), "Update data SuccessFully", Toast.LENGTH_LONG).show();
                         dialogPlus.dismiss();
                     }





                    }
                });

            }
        });








        return view;
    }
    private void setTabIconColor(TabLayout.Tab tab, int color) {
        Drawable icon = tab.getIcon();
        if (icon != null) {
            // Apply color filter to the icon
            icon.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1 && resultCode== Activity.RESULT_OK){
            uri=data.getData();
            try {
                updatedata.put("profile_pic_url",uri.toString());

                Glide.with(getContext()).load(uri).into(edit_profile_pic);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
   public void geturidata(){
       Intent intent= new Intent(Intent.ACTION_PICK);
       intent.setType("image/*");
       startActivityForResult(intent,1);

    }
    public void onDestroyView() {
        super.onDestroyView();
        // Dismiss the dialog if it's showing
        if (dialogPlus != null && dialogPlus.isShowing()) {
            dialogPlus.dismiss();
        }
    }

}