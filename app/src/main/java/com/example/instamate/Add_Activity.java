package com.example.instamate;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;

public class Add_Activity extends AppCompatActivity {

    boolean post,reel,story;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        post=false;
        reel=false;
        story=false;

        int screenHeight = getScreenHeight(this);
        int dialogHeight = screenHeight / 2;

        final DialogPlus dialogPlus = DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(LayoutInflater.from(this).inflate(R.layout.add_create_dialog,null)))
                .setExpanded(false)
                .setContentHeight(dialogHeight)
                .setOnBackPressListener(new OnBackPressListener() {
                    @Override
                    public void onBackPressed(DialogPlus dialogPlus) {
                        startActivity(new Intent(Add_Activity.this, DashBoard.class));
                        finish();
                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel(DialogPlus dialog) {
                        startActivity(new Intent(Add_Activity.this, DashBoard.class));
                        finish();
                    }
                })
               // Set a maximum height

                .create();

        dialogPlus.show();

//        dialogPlus.onBackPressed(dialogPlus);

        View add =dialogPlus.getHolderView();
        LinearLayoutCompat post_select=add.findViewById(R.id.post_select);
        LinearLayoutCompat reel_select=add.findViewById(R.id.reel_select);
        LinearLayoutCompat story_select=add.findViewById(R.id.story_select);



        post_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                post=true;

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.add_activity, new Post_add()).addToBackStack(null)
                        .commit();
                dialogPlus.dismiss();
//                Intent intent= new Intent(Intent.ACTION_PICK);
//                intent.setType("image/*");
//                startActivityForResult(intent,1);
//                dialogPlus.dismiss();






            }
        });

        reel_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.add_activity,new Reel_add()).addToBackStack(null)
                        .commit();
                dialogPlus.dismiss();




            }
        });

        story_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.add_activity,new Strory_add()).addToBackStack(null)
                        .commit();
                dialogPlus.dismiss();






            }
        });












    }
    private int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }




}
