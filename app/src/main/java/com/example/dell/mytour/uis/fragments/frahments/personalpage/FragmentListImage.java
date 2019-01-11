package com.example.dell.mytour.uis.fragments.frahments.personalpage;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPageAdapter.PersonalpageGridImageAdapter;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseFragments;
import com.example.dell.mytour.uis.activity.ShowImageActivity;
import com.example.dell.mytour.utils.SquareImage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentListImage extends BaseFragments {

    private ArrayList<String> imgURLs;
    private RecyclerView recyclerView;
    private PersonalpageGridImageAdapter adapter;

    SquareImage item_personal_img;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference userReference;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();
    Users currentUsers = new Users();
    String Uid = null;

    @Override
    protected int injectLayout() {
        return R.layout.fragment_list_image_personal;
    }

    @Override
    protected void injectView() {
        imgURLs = new ArrayList<>();
        recyclerView = view_home.findViewById(R.id.personal_image_recycleview);
        recyclerView.setNestedScrollingEnabled(false);

        Uid = getArguments().getString("UserId");
        loadImage(Uid);


    }

    @Override
    protected void injectVariables() {

    }

    public void imageRecycleView() {
        RecyclerView.LayoutManager manager = new GridLayoutManager(getContext(), 3);
        adapter = new PersonalpageGridImageAdapter(imgURLs, getContext(), new PersonalpageGridImageAdapter.OnItemImageClickListener() {
            @Override
            public void onItemClick(View itemView, final int position) {

            }

            @Override
            public void onImageClick(SquareImage image, int position) {
                Intent intent = new Intent(getActivity(), ShowImageActivity.class);
                intent.putExtra("UserId", Uid);
                intent.putExtra("position", position);
                intent.putExtra("type", "ava");
                startActivity(intent);

            }
        });
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    public void loadImage(final String Uid) {
        userReference = firebaseDatabase.getReference("Image");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot imageSnap : dataSnapshot.getChildren()) {
                    Image image = imageSnap.getValue(Image.class);
                    if (image.getUser_id().equals(Uid)) {
                        imgURLs.add(image.getImg_link());
                    }
                }
                imageRecycleView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}