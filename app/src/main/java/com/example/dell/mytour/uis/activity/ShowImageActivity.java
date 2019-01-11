package com.example.dell.mytour.uis.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPost.ImagePagerAdapter;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.uis.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShowImageActivity extends BaseActivity {

    private Toolbar show_img_toolbar;
    private ViewPager show_img;
    private ImagePagerAdapter adapter;
    private ArrayList<String> urls;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userReference;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();

    String Uid = null;
    int position = 0;
    String type = null;

    @Override
    protected int injectLayout() {
        return R.layout.activity_show_image;
    }

    @Override
    protected void injectView() {
        show_img = findViewById(R.id.show_img);
        show_img_toolbar = findViewById(R.id.show_img_toolbar);
        urls = new ArrayList<>();

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        Uid = intent.getStringExtra("UserId");
        position = intent.getIntExtra("position", 0);

        adapter = new ImagePagerAdapter(urls, ShowImageActivity.this, ImageView.ScaleType.FIT_CENTER);
        show_img.setAdapter(adapter);
        getData();




    }

    @Override
    protected void injectVariables() {

    }


    public void getData() {
        userReference = firebaseDatabase.getReference("Image");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot image : dataSnapshot.getChildren()){
                    Image image1 = image.getValue(Image.class);
                    if (image1.getUser_id().equals(Uid)){
                        urls.add(image1.getImg_link());
                    }

                }

                adapter.notifyDataSetChanged();
                show_img.setCurrentItem(position);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
