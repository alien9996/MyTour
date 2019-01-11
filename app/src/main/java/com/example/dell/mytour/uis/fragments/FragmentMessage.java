package com.example.dell.mytour.uis.fragments;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.MessageAdapter;
import com.example.dell.mytour.event.RecyclerTouchListener;
import com.example.dell.mytour.model.MessageItem;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Message;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseFragments;
import com.example.dell.mytour.uis.activity.PersonalPostActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class FragmentMessage extends BaseFragments {

    private ArrayList<MessageItem> lst_message;
    private RecyclerView recycler_message;
    private MessageAdapter message_adapter;

    // khai bao text view
    private TextView txt_message;

    // khai bao cac bien dung cho firebase
    private FirebaseDatabase firebase_database;
    private DatabaseReference database_reference;
    private FirebaseUser user;


    @Override
    protected int injectLayout() {
        return R.layout.fragment_message_layout;
    }

    @Override
    protected void injectView() {
        // khoi tao cac text view
        txt_message = view_home.findViewById(R.id.txt_message);

        // khoi tao cac bien dung cho firebase
        firebase_database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        setScreenMessage();

    }

    @Override
    protected void injectVariables() {

    }


    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay ra giao dien khi chua dang nhap va khi dang nhap*/
    private void setScreenMessage() {
        if (user != null) {
            createMessages();
            setClickForRecyclerView();
            txt_message.setVisibility(View.GONE);

        } else {
            txt_message.setVisibility(View.VISIBLE);
        }
    }
    /*phuong thuc lay ra giao dien khi chua dang nhap va khi dang nhap*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc set onClick cho recyclerView*/
    private void setClickForRecyclerView(){
        recycler_message.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recycler_message, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), PersonalPostActivity.class);
                intent.putExtra("PostId", lst_message.get(lst_message.size() - position -1).getPost_id());
                startActivity(intent);

                setStatusMessage(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    /*phuong thuc set onClick cho recyclerView*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc set trang thai cua bai dang*/
    private void setStatusMessage(int position){
        database_reference = firebase_database.getReference();

        database_reference.child("Message").child(lst_message.get(lst_message.size() - position -1).getMessage_id()).child("message_status").setValue(true);

    }
    /*phuong thuc set trang thai cua bai dang*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc tao recycler view*/
    private void createMessages() {

        try {
            recycler_message = view_home.findViewById(R.id.recycler_message);
            recycler_message.setNestedScrollingEnabled(false);
            lst_message = new ArrayList<>();

            fakeDataFragmentMessage();

            message_adapter = new MessageAdapter(getActivity(),lst_message);
            RecyclerView.LayoutManager layout_manager = new LinearLayoutManager(getContext());
            recycler_message.setLayoutManager(layout_manager);
            recycler_message.setAdapter(message_adapter);
            message_adapter.notifyDataSetChanged();
        } catch (Exception ex) {
            Log.d("Post:", "" + ex.getMessage());
        }

    }
    /*phuong thuc tao recycler view*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay du lieu Message tu tren firebase*/
    public void fakeDataFragmentMessage() {

        database_reference = firebase_database.getReference("Message");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lst_message.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);

                    if(user.getUid().equals(message.getFriend_id())){
                        MessageItem message_item = new MessageItem();
                        if (message.getMessage_type() == 0) {
                            message_item.setMessage_type(0);
                            message_item.setMessage_content("Đã bình luận về bài viết của bạn");
                        }
                        if (message.getMessage_type() == 1) {
                            message_item.setMessage_type(1);
                            message_item.setMessage_content("Đã thích bài viết của bạn");
                        }
                        if (message.getMessage_type() == 2) {
                            message_item.setMessage_type(2);
                            message_item.setMessage_content("Đã gửi cho bạn lời mời kết bạn");
                        }

                        message_item.setMessage_id(message.getMessage_id());
                        message_item.setPost_id(message.getPost_id());
                        message_item.setMessage_date(message.getMessage_date());
                        message_item.setMessage_status(message.isMessage_status());
                        getInfoUser(message.getUser_id(), message_item);

                        lst_message.add(message_item);
                    }

                }
                message_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc lay du lieu Message tu tren firebase*/

    //----------------------------------------------------------------------------------------------

    /*phuong thuc get Name va hinh anh cua user*/
    private void getInfoUser(final String user_id, final MessageItem item){
        database_reference = firebase_database.getReference("Users");
        database_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users user = snapshot.getValue(Users.class);
                    if(user_id.equals(user.getUser_id())){
                        item.setUser_name(user.getUser_full_name());
                        item.setUser_img_link(user.getUser_avatar());
                        break;
                    }
                }
                message_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc get Name va hinh anh cua user*/
}
