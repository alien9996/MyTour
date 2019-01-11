package com.example.dell.mytour.uis.activity.result;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPost.ListFriendAdapter;
import com.example.dell.mytour.event.RecyclerTouchListener;
import com.example.dell.mytour.model.ItemFriend;
import com.example.dell.mytour.model.model_base.Friend;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.PersonalPageActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FriendListActivity extends BaseActivity {

    //khai bao toolbar
    Toolbar toolbar;
    private ActionBar action_bar;

    // khai bao cac recyclerView
    private RecyclerView recycler_invitation_friend, recycler_list_friend;

    // khai bao cac adapter
    private ListFriendAdapter friend_adapter;
    private ListFriendAdapter invitation_adapter;

    // khai bao cac ArrayList
    private ArrayList<ItemFriend> lst_invitation;
    private ArrayList<ItemFriend> lst_friend;

    // khai bao cac phuong thuc lam viec voi firebase
    private FirebaseDatabase firebase_database;
    private DatabaseReference database_reference;
    private FirebaseUser user;


    @Override
    protected int injectLayout() {
        return R.layout.activity_list_friend_and_invitation;
    }

    @Override
    protected void injectView() {

        // phan khoi tao toolbar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        action_bar = getSupportActionBar();
        action_bar.setTitle("Bạn bè");
        action_bar.setDisplayHomeAsUpEnabled(true);

        // khoi tao recycler view
        recycler_invitation_friend = findViewById(R.id.recycler_invitation_friend);
        recycler_list_friend = findViewById(R.id.recycler_list_friend);

        recycler_list_friend.setNestedScrollingEnabled(false);
        recycler_invitation_friend.setNestedScrollingEnabled(false);

        // khoi tao cac phuong  thuc lam  viec voi firebase
        firebase_database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // goi cac phuong thuc
        setOnclickForRecyclerView();

        createRecyclerInvitation();

        createRecyclerFriend();

    }

    @Override
    protected void injectVariables() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //----------------------------------------------------------------------------------------------
    /*phuong thuc set onclick cho recycler view*/
    private void setOnclickForRecyclerView(){
        recycler_invitation_friend.addOnItemTouchListener(new RecyclerTouchListener(this, recycler_invitation_friend, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                /*Intent intent = new Intent(FriendListActivity.this, PersonalPageActivity.class);
                intent.putExtra("UserId", lst_invitation.get(lst_invitation.size() - position -1).getFriend_id());
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recycler_list_friend.addOnItemTouchListener(new RecyclerTouchListener(this, recycler_list_friend, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(FriendListActivity.this, PersonalPageActivity.class);
                intent.putExtra("UserId", lst_friend.get(lst_friend.size() - position -1).getFriend_id());
                startActivity(intent);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    /*phuong thuc set onclick cho recycler view*/

    //---------------------------------------------------------------------------------------------------------------------
    // cac phuong thuc khoi tao gia tri cho recyclerView invitation
    private void createRecyclerInvitation(){
        lst_invitation = new ArrayList<>();

        getInvitation();

        invitation_adapter = new ListFriendAdapter(FriendListActivity.this, lst_invitation, true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FriendListActivity.this);

        recycler_invitation_friend.setLayoutManager(layoutManager);
        recycler_invitation_friend.setAdapter(invitation_adapter);
        invitation_adapter.notifyDataSetChanged();


    }

    // cac phuong thuc khoi tao gia tri cho recyclerView invitation
    private void createRecyclerFriend(){
        lst_friend = new ArrayList<>();

        getListFriend();

        friend_adapter = new ListFriendAdapter(FriendListActivity.this, lst_friend, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FriendListActivity.this);

        recycler_list_friend.setLayoutManager(layoutManager);
        recycler_list_friend.setAdapter(friend_adapter);
        friend_adapter.notifyDataSetChanged();

    }



    //---------------------------------------------------------------------------------------------------------------------
    // cac  phuong thuc lay du lieu tu firebase
    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay ra loi moi ket ban*/
    private void getInvitation(){
        database_reference = firebase_database.getReference("Friend");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lst_invitation.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Friend friend = snapshot.getValue(Friend.class);
                    if(user.getUid().equals(friend.getFriend_link()) && !friend.isFriend_status()){
                        ItemFriend item_friend = new ItemFriend();
                        item_friend.setFriend_key(snapshot.getKey());
                        item_friend.setFriend_id(friend.getUser_id());
                        getInfoUser(friend.getUser_id(), item_friend);

                        lst_invitation.add(item_friend);
                    }
                }
                invitation_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc lay ra loi moi ket ban*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay danh sach ban be */
    private void getListFriend(){
        database_reference = firebase_database.getReference("Friend");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lst_friend.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Friend friend = snapshot.getValue(Friend.class);
                    if(user.getUid().equals(friend.getUser_id()) && friend.isFriend_status()){
                        ItemFriend item_friend = new ItemFriend();
                        item_friend.setFriend_key(snapshot.getKey());
                        item_friend.setFriend_id(friend.getFriend_link());
                        getInfoUser(friend.getFriend_link(), item_friend);

                        lst_friend.add(item_friend);
                    }
                }
                friend_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*phuong thuc lay danh sach ban be */

    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay ten va lay anh tu Users*/
    private void getInfoUser (final String user_id,final ItemFriend item_friend){

        database_reference = firebase_database.getReference("Users");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Users user = snapshot.getValue(Users.class);
                    if(user_id.equals(user.getUser_id())){
                        item_friend.setFull_name(user.getUser_full_name());
                        item_friend.setAvatar(user.getUser_avatar());
                        item_friend.setAddress(user.getUser_address());
                    }
                }
                friend_adapter.notifyDataSetChanged();
                invitation_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc lay ten va lay anh tu Users*/
}
