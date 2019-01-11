package com.example.dell.mytour.uis.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPageAdapter.PersonalListDiaryAdapter;
import com.example.dell.mytour.event.RecyclerTouchListener;
import com.example.dell.mytour.model.MessageItem;
import com.example.dell.mytour.model.model_base.Message;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HistoryAndDiaryActivity extends BaseActivity {

    //phan khai bao toolbar
    private Toolbar toolbar;
    private ActionBar action_bar;
    private ProgressBar progress_bar_resul;

    // phan khai bao recycler view recycler_history
    private RecyclerView recycler_history;
    public PersonalListDiaryAdapter adapter;
    private ArrayList<MessageItem> lst_diary;

    // khai bao cac bien dung cho firebase
    private FirebaseDatabase firebase_database;
    private DatabaseReference database_reference;
    private FirebaseUser firebase_user;

    @Override
    protected int injectLayout() {
        return R.layout.activity_history_and_diary;

    }

    @Override
    protected void injectView() {

        // khoi tao progress bar
        progress_bar_resul = findViewById(R.id.progress_bar_resul);

        // phan khoi tao toolbar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        action_bar = getSupportActionBar();
        action_bar.setTitle("Lịch sử/Sự kiện");
        action_bar.setDisplayHomeAsUpEnabled(true);

        // khoi tao cac bien dung cho firebase
        firebase_database = FirebaseDatabase.getInstance();
        firebase_user = FirebaseAuth.getInstance().getCurrentUser();

        if (firebase_user != null) {
            createRecyclerAndAdapter();
            setOnclickForRecyclerView();
        }

    }

    @Override
    protected void injectVariables() {

    }


    // ke thua phuong thuc click vao thanh toolbar

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    //----------------------------------------------------------------------------------------------
    /*phuong thuc set onclick cho recycler View*/
    private void setOnclickForRecyclerView() {
        recycler_history.addOnItemTouchListener(new RecyclerTouchListener(HistoryAndDiaryActivity.this, recycler_history, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                gotoScreen(position);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    /*phuong thuc set onclick cho recycler View*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc chuyen trang khi click vao recycler view*/
    private void gotoScreen(int position) {
        if (lst_diary.get(lst_diary.size() - position - 1).getMessage_type() == 2) {
            Intent intent = new Intent(HistoryAndDiaryActivity.this, PersonalPageActivity.class);
            intent.putExtra("UserId", lst_diary.get(lst_diary.size() - position - 1).getFriend_id());
            startActivity(intent);
        } else {
            Intent intent = new Intent(HistoryAndDiaryActivity.this, PersonalPostActivity.class);
            intent.putExtra("PostId", lst_diary.get(lst_diary.size() - position - 1).getPost_id());
            startActivity(intent);
        }
    }
    /*phuong thuc chuyen trang khi click vao recycler view*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc khoi tao recycler view va adapter*/
    private void createRecyclerAndAdapter() {
        recycler_history = findViewById(R.id.recycler_history);
        recycler_history.setNestedScrollingEnabled(false);
        lst_diary = new ArrayList<>();

        fakeData();
        adapter = new PersonalListDiaryAdapter(lst_diary, HistoryAndDiaryActivity.this);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(HistoryAndDiaryActivity.this);
        recycler_history.setLayoutManager(manager);
        recycler_history.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    /*phuong thuc khoi tao recycler view va adapter*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay du lieu tu Object Message tren firebase*/
    public void fakeData() {

        database_reference = firebase_database.getReference("Message");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lst_diary.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);

                    if (firebase_user.getUid().equals(message.getUser_id())) {
                        MessageItem message_item = new MessageItem();
                        if (message.getMessage_type() == 0) {
                            message_item.setMessage_type(0);

                            message_item.setMessage_content("bình luận về bài viết của ");
                        }
                        if (message.getMessage_type() == 1) {
                            message_item.setMessage_type(1);
                            message_item.setMessage_content("thích bài viết của ");
                        }

                        if (message.getMessage_type() == 2) {
                            message_item.setMessage_type(2);
                            message_item.setMessage_content("gửi lời mời kết bạn đến ");
                        }
                        getInfoUser(message.getFriend_id(), message_item);
                        message_item.setMessage_id(message.getMessage_id());
                        message_item.setPost_id(message.getPost_id());
                        message_item.setMessage_date(message.getMessage_date());
                        message_item.setFriend_id(message.getFriend_id());

                        lst_diary.add(message_item);
                    }

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc lay du lieu tu Object Message tren firebase*/


    //----------------------------------------------------------------------------------------------

    /*phuong thuc get Name va hinh anh cua user*/
    private void getInfoUser(final String user_id, final MessageItem item) {
        database_reference = firebase_database.getReference("Users");
        database_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Users user = snapshot.getValue(Users.class);
                    if (user_id.equals(user.getUser_id())) {
                        item.setUser_name(user.getUser_full_name());
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc get Name va hinh anh cua user*/
}
