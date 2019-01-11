package com.example.dell.mytour.uis.fragments.frahments.personalpage;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPageAdapter.PersonalListFriendAdapter;
import com.example.dell.mytour.model.FollowUser;
import com.example.dell.mytour.model.model_base.Follows;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseFragments;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FragmentListFriend extends BaseFragments {

    private ArrayList<FollowUser> listUsers;
    private PersonalListFriendAdapter adapter;
    private RecyclerView rcUsers;


    @Override
    protected int injectLayout() {
        return R.layout.fragment_list_friend;
    }

    @Override
    protected void injectView() {

        rcUsers = view_home.findViewById(R.id.list_friend_recycle_view);
        rcUsers.setNestedScrollingEnabled(false);

        listUsers = new ArrayList<>();
        adapter = new PersonalListFriendAdapter(listUsers, getContext());

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        rcUsers.setAdapter(adapter);
        rcUsers.setLayoutManager(manager);

        String Uid = getArguments().getString("UserId");

        getFollow(Uid);
    }

    @Override
    protected void injectVariables() {

    }

    public void getFollow(final String Uid){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Follows");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    FollowUser followUser = new FollowUser();
                    Follows follows = dataSnapshot1.getValue(Follows.class);
                    if (Uid.equals(follows.getUser_id())){
                        followUser.setFollows(follows);
                        getUser(follows.getFriend_id(), followUser);
                        listUsers.add(followUser);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getUser(final String Uid, final FollowUser followUser){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference = firebaseDatabase.getReference("Users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Users users = dataSnapshot1.getValue(Users.class);
                    if (Uid.equals(users.getUser_id())){
                        followUser.setUsers(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



}
