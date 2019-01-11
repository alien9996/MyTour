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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPost.ListFriendAdapter;
import com.example.dell.mytour.adapter.PostAdapter;
import com.example.dell.mytour.adapter.PostHomeAdapter;
import com.example.dell.mytour.event.RecyclerTouchListener;
import com.example.dell.mytour.model.ItemFriend;
import com.example.dell.mytour.model.PostItem;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Rating;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.PersonalPageActivity;
import com.example.dell.mytour.uis.activity.post_activity.PostDetailsActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ResultActivity extends BaseActivity {

    private Toolbar toolbar;
    private ActionBar action_bar;
    private ProgressBar progress_bar_resul;


    // khai bao recycler view
    private RecyclerView recycler_post_search, recycler_friend_search;

    // khai bao cac adapter
    private PostAdapter post_home_adapter;
    private ListFriendAdapter friend_adapter;

    // khai bao linear layout
    private LinearLayout linear_list_friend, linear_list_post, linear_str_key, linear_result;

    // khai bao text view
    private TextView txt_title_search, txt_key_search;

    // khai bao cac bien nhan intent
    private String key_search = "";
    private int intent_type = 0;
    String[] lst_key;

    // khai bao cac array list
    private ArrayList<PostItem> lst_post;
    private ArrayList<ItemFriend> lst_friend;

    // khai bao cac bien lam viec voi firebase
    private FirebaseDatabase firebase_database;
    private DatabaseReference database_reference;

    @Override
    protected int injectLayout() {
        return R.layout.activity_result_layout;
    }

    @Override
    protected void injectView() {

        // khoi tao progress bar
        progress_bar_resul = findViewById(R.id.progress_bar_resul);

        // phan khoi tao toolbar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        action_bar = getSupportActionBar();
        action_bar.setDisplayHomeAsUpEnabled(true);


        //khoi tao linear_layou
        linear_list_friend = findViewById(R.id.linear_list_friend);
        linear_list_post = findViewById(R.id.linear_list_post);
        linear_str_key = findViewById(R.id.linear_str_key);
        linear_result = findViewById(R.id.linear_result);

        // khoi tao text view
        txt_title_search = findViewById(R.id.txt_title_search);
        txt_key_search = findViewById(R.id.txt_key_search);

        // khoi tao cac gia tri lam viec voi firebase
        firebase_database = FirebaseDatabase.getInstance();

        // khoi tao các recycler view
        recycler_post_search = findViewById(R.id.recycler_post_search);
        recycler_post_search.setNestedScrollingEnabled(false);

        recycler_friend_search = findViewById(R.id.recycler_friend_search);
        recycler_post_search.setNestedScrollingEnabled(false);

        // goi cac phuong thuc
        setOnClickForRecyclerView();
        getIntentInfo();


    }

    @Override
    protected void injectVariables() {

    }

    //----------------------------------------------------------------------------------------------
    /*ke thua lai phuong thuc click vao item tren thanh toolBar*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //---------------------------------------------------------------------------------------------
    /*phuong thuc get cac intent*/
    private void getIntentInfo() {
        Intent intent = getIntent();
        intent_type = Integer.parseInt(intent.getStringExtra("intent_type"));
        if (intent_type == 0) {
            linear_list_friend.setVisibility(View.GONE);
            linear_str_key.setVisibility(View.GONE);
            action_bar.setTitle("Bài quảng cáo");
            txt_title_search.setText("Bài đăng quảng cáo");
            setDataPost(0);
        } else {
            if (intent_type == 1) {
                linear_list_friend.setVisibility(View.GONE);
                linear_str_key.setVisibility(View.GONE);
                action_bar.setTitle("Bài giới thiệu - chia sẻ kinh nghiệm");
                txt_title_search.setText("Bài giới thiệu - chia sẻ kinh nghiệm");
                setDataPost(1);
            } else {
                linear_list_friend.setVisibility(View.VISIBLE);
                linear_str_key.setVisibility(View.VISIBLE);
                progress_bar_resul.setVisibility(View.VISIBLE);
                linear_result.setVisibility(View.GONE);
                action_bar.setTitle("Tìm kiếm");

                key_search = intent.getStringExtra("data");
                lst_key = key_search.split(" ");
                if (key_search.length() >= 9) {
                    txt_key_search.setText(key_search.substring(0,7)+"...");
                } else {
                    txt_key_search.setText(key_search);
                }

                setDataPost(2);
                getReferenceUser();
            }

        }
    }
    /*phuong thuc get cac intent*/


    //---------------------------------------------------------------------------------------------
    /*phuong thuc set su kien onClick cho cac item recycler*/
    public void setOnClickForRecyclerView() {
        recycler_post_search.addOnItemTouchListener(new RecyclerTouchListener(this, recycler_post_search, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ResultActivity.this, PostDetailsActivity.class);
                intent.putExtra("post_id", lst_post.get(lst_post.size() - position - 1).getPost_id().toString());
                intent.putExtra("user_avatar", lst_post.get(lst_post.size() - position - 1).getUser_post_avatar().toString());
                intent.putExtra("user_name", lst_post.get(lst_post.size() - position - 1).getUser_post_name().toString());
                intent.putExtra("rating_post", lst_post.get(lst_post.size() - position - 1).getPost_rating_average().toString());
                intent.putExtra("post_background", lst_post.get(lst_post.size() - position - 1).getPost_avatar());
                ResultActivity.this.startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        recycler_friend_search.addOnItemTouchListener(new RecyclerTouchListener(this, recycler_friend_search, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(ResultActivity.this, PersonalPageActivity.class);
                intent.putExtra("UserId", lst_friend.get(lst_friend.size() - position - 1).getFriend_id());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }
    /*phuong thuc set su kien onClick cho cac item recycler*/


    //---------------------------------------------------------------------------------------------
    /*phuong thuc do du lieu ra neu intent type = 0*/
    private void setDataPost(int type) {

        lst_post = new ArrayList<>();

        getReferencePost(type);

        post_home_adapter = new PostAdapter(this, lst_post);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_post_search.setLayoutManager(layoutManager);
        recycler_post_search.setAdapter(post_home_adapter);

    }
    /*phuong thuc do du lieu ra neu intent type = 0*/


    //----------------------------------------------------------------------------------------------

    /*phuong thuc getReferencePost*/
    private void getReferencePost(final int post_type) {
        database_reference = firebase_database.getReference("Post");

        if (database_reference != null) {

            database_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lst_post.clear();

                    if (post_type == 0) {
                        for (DataSnapshot post_snapshot : dataSnapshot.getChildren()) {
                            PostItem post_item = new PostItem();
                            Post post = post_snapshot.getValue(Post.class);
                            if (post.getPost_type() == 0) {
                                getAvatarPost(post.getPost_id(), post_item);
                                getUserInfo(post.getUser_id(), post_item);
                                getAverageRating(post.getPost_id(), post_item);
                                faceDataPost(post, post_item);
                                lst_post.add(post_item);
                            }
                        }
                    } else {
                        if (post_type == 1) {
                            for (DataSnapshot post_snapshot : dataSnapshot.getChildren()) {
                                PostItem post_item = new PostItem();
                                Post post = post_snapshot.getValue(Post.class);
                                if (post.getPost_type() != 0 && post.isPost_level()) {
                                    getAvatarPost(post.getPost_id(), post_item);
                                    getUserInfo(post.getUser_id(), post_item);
                                    getAverageRating(post.getPost_id(), post_item);
                                    faceDataPost(post, post_item);
                                    lst_post.add(post_item);
                                }
                            }
                        } else {

                            for (DataSnapshot post_snapshot : dataSnapshot.getChildren()) {
                                PostItem post_item = new PostItem();
                                Post post = post_snapshot.getValue(Post.class);

                                for (int i = 0; i < lst_key.length; i++) {
                                    if (post.getPost_title().toLowerCase().indexOf(lst_key[i].toLowerCase()) >= 0) {
                                        getAvatarPost(post.getPost_id(), post_item);
                                        getUserInfo(post.getUser_id(), post_item);
                                        getAverageRating(post.getPost_id(), post_item);
                                        faceDataPost(post, post_item);
                                        lst_post.add(post_item);
                                    }
                                }
                            }
                        }
                    }
                    post_home_adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
    /*phuong thuc getReferencePost*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc set du lieu vap post*/
    private void faceDataPost(Post post, PostItem item_post) {

        String mini_content = "";
        if (post.getPost_content().length() > 80) {
            mini_content = post.getPost_content().substring(0, 80) + "...";
        } else {
            mini_content = post.getPost_content();
        }
        item_post.setPost_id(post.getPost_id());
        item_post.setPost_title(post.getPost_title());
        item_post.setPost_content(mini_content.replace("\n", " "));
        item_post.setPost_date(post.getPost_date());
        item_post.setPost_price_new(post.getPost_tour_price_new());
        item_post.setPost_price_old(post.getPost_tour_price_old());
        item_post.setPost_advertisement_title(post.getPost_title_advertisement());
    }
    /*phuong thuc set du lieu vap post*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay hinh anh cho bai dang*/
    private void getAvatarPost(final String post_id, final PostItem item_post) {

        database_reference = firebase_database.getReference("Image");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot_image : dataSnapshot.getChildren()) {
                    Image image = snapshot_image.getValue(Image.class);
                    if (post_id.equals(image.getPost_id())) {
                        item_post.setPost_avatar(image.getImg_link().toString());
                        break;
                    }
                }
                post_home_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Loi", "Khong  the lay ve hinh anh bai dang");
            }
        });
    }
    /*phuong thuc lay hinh anh cho bai dang*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay anh dai dien cho user*/
    private void getUserInfo(final String user_id, final PostItem item_post) {

        database_reference = firebase_database.getReference("Users");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot_user : dataSnapshot.getChildren()) {
                    Users users = snapshot_user.getValue(Users.class);
                    if (user_id.equals(users.getUser_id())) {
                        item_post.setUser_post_avatar(users.getUser_avatar().toString());
                        item_post.setUser_post_name(users.getUser_full_name());
                        break;
                    }
                }
                post_home_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Loi", "Khong  the lay ve thong tin user");
            }
        });

    }
    /*phuong thuc lay anh dai dien cho user*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc tinh diem rating*/
    private void getAverageRating(final String post_id, final PostItem item_post) {

        database_reference = firebase_database.getReference("Rating");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float average = 0;
                for (DataSnapshot snapshot_rating : dataSnapshot.getChildren()) {
                    Rating rating = snapshot_rating.getValue(Rating.class);
                    if (post_id.equals(rating.getPost_id())) {
                        average = Float.parseFloat(rating.getRating_level());
                        item_post.setPost_rating_average(String.valueOf(average));
                        break;
                    }
                }
                if (average == 0) {
                    average = (float) 3.5;
                    item_post.setPost_rating_average(String.valueOf(average));
                }
                post_home_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*phuong thuc tinh diem rating*/


    //---------------------------------------------------------------------------------------------------
    // phan danh sach ban be
    //----------------------------------------------------------------------------------------------
    /*phuong thuc khoi tao danh sach ban be*/
    private void getReferenceUser() {

        lst_friend = new ArrayList<>();
        getListUser();

        friend_adapter = new ListFriendAdapter(ResultActivity.this, lst_friend, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ResultActivity.this);
        recycler_friend_search.setLayoutManager(layoutManager);
        recycler_friend_search.setAdapter(friend_adapter);
        friend_adapter.notifyDataSetChanged();

    }

    /*phuong thuc khoi tao danh sach ban be*/


    //----------------------------------------------------------------------------------------------
    /*lay ve danh sach user*/
    private void getListUser() {

        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        database_reference = firebase_database.getReference("Users");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot_user : dataSnapshot.getChildren()) {
                    Users users = snapshot_user.getValue(Users.class);
                    for (int i = 0; i < lst_key.length; i++) {
                        if (users.getUser_full_name().toLowerCase().indexOf(lst_key[i].toLowerCase()) >= 0 || users.getUser_email().indexOf(lst_key[i].toLowerCase()) >= 0) {
                            lst_friend.add(new ItemFriend(users.getUser_full_name(), users.getUser_avatar(), users.getUser_address(), users.getUser_id(), snapshot_user.getKey()));
                        }
                    }
                }
                friend_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Loi", "Khong  the lay ve thong tin user");
            }
        });
        linear_result.setVisibility(View.VISIBLE);
        progress_bar_resul.setVisibility(View.GONE);
    }
    /*lay ve danh sach user*/
}
