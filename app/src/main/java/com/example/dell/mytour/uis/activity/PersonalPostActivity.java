package com.example.dell.mytour.uis.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.health.UidHealthStats;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPageAdapter.PersonalPostCommentAdapter;
import com.example.dell.mytour.adapter.PersonalPost.ImagePagerAdapter;
import com.example.dell.mytour.model.PersonalPost;
import com.example.dell.mytour.model.PostComment;
import com.example.dell.mytour.model.model_base.Answer;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Like;
import com.example.dell.mytour.model.model_base.Message;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Tag;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.utils.Heart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.os.health.UidHealthStats;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPageAdapter.PersonalPostCommentAdapter;
import com.example.dell.mytour.adapter.PersonalPost.ImagePagerAdapter;
import com.example.dell.mytour.model.PersonalPost;
import com.example.dell.mytour.model.PostComment;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Like;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.utils.Heart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PersonalPostActivity extends BaseActivity implements View.OnClickListener {

    private ArrayList<PostComment> postComments;
    private ArrayList<String> urls;
    private RecyclerView rc_comment;
    private PersonalPostCommentAdapter adapter_comment;
    private CheckBox personal_post_cb_like_item;
    private boolean relativeLayout;
    private ImagePagerAdapter imgAdapter;

    private CircleImageView personal_img_user;
    private CircleImageView personal_post_img_comment_ava;
    private TextView personal_txt_name_user;
    private TextView personal_txt_status_user;
    private TextView personal_txt_note_friend;
    private TextView personal_txt_time_item;
    private TextView personal_txt_location;
    private TextView personal_txt_content;
    private TextView personal_post_txt_count_like;
    private TextView personal_post_txt_count_comment;
    private EditText personal_post_edt_comment_text;
    private TextView see_new_comment;
    private TextView personal_post_img_postision;
    private TextView personal_post_comment_status;
    private Button personal_btn_menu_item_personal_post2;
    private ViewPager viewPager;
    private LinearLayout personal_post_addcmmm;
    private RelativeLayout personal_post_img_relative;
    private Button personal_post_btn_comment_clear_text;
    private Button personal_post_btn_send_comment;

    // cac bien su dung trong bai
    private String user_post;


    public String getUser_post() {
        return user_post;
    }

    public void setUser_post(String user_post) {
        this.user_post = user_post;
    }

    String postId = null;
    int load_more = 5;
    public  boolean answerClick = false;

    //fireBase
    final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userReference;
    DatabaseReference cmmDatabaseReference;
    String currentUserId = currentUser.getUid();


    @Override
    protected void onStart() {
        super.onStart();
        relativeLayout = findViewById(R.id.personal_post_relativeLayout).requestFocus();
    }

    @Override
    protected int injectLayout() {
        return R.layout.activity_personal_post;
    }

    @Override
    protected void injectView() {
        rc_comment = findViewById(R.id.personal_post_comment_recycle);
        personal_post_cb_like_item = findViewById(R.id.personal_post_cb_like_item);
        personal_img_user = findViewById(R.id.personal_img_user2);

        personal_txt_name_user = findViewById(R.id.personal_txt_name_user2);
        personal_txt_status_user = findViewById(R.id.personal_txt_status_user2);
        personal_txt_note_friend = findViewById(R.id.personal_txt_note_friend2);
        personal_txt_time_item = findViewById(R.id.personal_txt_time_item_post_personal2);
        personal_txt_location = findViewById(R.id.personal_txt_location_personal_post2);
        personal_txt_content = findViewById(R.id.personal_txt_content_item_personal_post2);

        personal_btn_menu_item_personal_post2 = findViewById(R.id.personal_btn_menu_item_personal_post2);
        personal_post_txt_count_like = findViewById(R.id.personal_txt_count_like_item_personal_post);
        personal_post_txt_count_comment = findViewById(R.id.personal_txt_count_comment_item_personal_post);
        personal_post_addcmmm = findViewById(R.id.personal_post_addcmmm);

        personal_post_img_comment_ava = findViewById(R.id.personal_post_img_comment_ava);

        personal_post_edt_comment_text = findViewById(R.id.personal_post_edt_comment_text);

        personal_post_btn_comment_clear_text = findViewById(R.id.personal_post_btn_comment_clear_text);
        personal_post_btn_send_comment = findViewById(R.id.personal_post_btn_send_comment);
        personal_post_img_postision = findViewById(R.id.personal_post_img_postision);
        personal_post_img_relative = findViewById(R.id.personal_post_img_relative);
        see_new_comment = findViewById(R.id.see_new_comment);
        personal_post_comment_status = findViewById(R.id.personal_post_comment_status);

        viewPager = findViewById(R.id.personal_post_img_viewpager);

        //firebase
        cmmDatabaseReference = firebaseDatabase.getReference("Comments");


        urls = new ArrayList<>();
        Intent intent = getIntent();
        postId = intent.getStringExtra("PostId");


        //loadComment
        postComments = new ArrayList<>();
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        rc_comment.setLayoutManager(manager);
        adapter_comment = new PersonalPostCommentAdapter(postComments, PersonalPostActivity.this, currentUserId, new PersonalPostCommentAdapter.OnItemClickListener() {
            @Override
            public void onAnswerClick(EditText editText, View view, final int position) {
                answerClick = true;
                Button personal_post_btn_send_answer = view.findViewById(R.id.personal_post_btn_send_answer);
                personal_post_comment_status.setVisibility(View.VISIBLE);
                personal_post_comment_status.setText("Đang trả lời bình luận");
                personal_post_btn_send_comment.setVisibility(View.VISIBLE);
                personal_post_btn_send_answer.setVisibility(View.INVISIBLE);
                // Toast.makeText(PersonalPostActivity.this, "Tra loi " + postComments.get(position).getComment().getCmm_content(), Toast.LENGTH_SHORT).show();
                personal_post_edt_comment_text.requestFocus();

                final RecyclerView post_comment_list_answer = view.findViewById(R.id.post_comment_list_answer);
                personal_post_btn_send_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (answerClick) {
                            DatabaseReference dataReference = firebaseDatabase.getReference();

                            String key = dataReference.child("Answers").push().getKey();
                            if (!TextUtils.isEmpty(personal_post_edt_comment_text.getText().toString().trim())) {
                                Answer answer = new Answer(key, personal_post_edt_comment_text.getText().toString().trim()
                                        , getDate(), true, currentUserId, postComments.get(position).getComment().getCmm_id());
                                dataReference.child("Answers").child(key).setValue(answer);

//                    answerComments.add(answer);
                                post_comment_list_answer.getAdapter().notifyDataSetChanged();

                                personal_post_edt_comment_text.setText("");
                                personal_post_edt_comment_text.clearFocus();
                                personal_post_btn_send_comment.setVisibility(View.VISIBLE);
                                personal_post_comment_status.setVisibility(View.GONE);
                                answerClick = false;
                            } else {
                                Toast.makeText(PersonalPostActivity.this, "Bạn chưa nhập comment hoặc quá nhiều khoảng trắng", Toast.LENGTH_SHORT).show();

                            }
                        }
                    }
                });

            }

            @Override
            public void onSendClick(Button button, View view, ArrayList<Answer> answerComments, int position) {



            }

            @Override
            public void onMenuClick(Button button, View view, final int position) {

                final TextView personal_comment_txt_comment = view.findViewById(R.id.personal_comment_txt_comment);
                final TextView personal_comment_txt_xemthem = view.findViewById(R.id.personal_comment_txt_xemthem);
                //firebase
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference cmmReference = firebaseDatabase.getReference();
                //Creat menu

                if (currentUserId.equals(postComments.get(position).getUsers().getUser_id())) {
                    PopupMenu popupMenu = new PopupMenu(context, button);
                    //Inflating menu
                    popupMenu.inflate(R.menu.post_comment_menu);
                    //add listenner
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.post_comment_delete:
                                    cmmReference.child("Comments").child(postComments.get(position).getComment().getCmm_id()).removeValue();
                                    break;
                                case R.id.post_comment_edit:
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                    //create message, title, body(), Button
                                    builder.setTitle("Chinh sua Comment");
                                    //Tao 1 view de gan cho body
                                    LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                                    final View viewDialog = layoutInflater.inflate(R.layout.custom_dialog_edit_comment, null);
                                    builder.setView(viewDialog);

                                    final EditText edit_comment = viewDialog.findViewById(R.id.comment_edit);
                                    edit_comment.setText(personal_comment_txt_comment.getText());

                                    builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            String new_comment = edit_comment.getText().toString().trim();
                                            if (!TextUtils.isEmpty(new_comment)) {
                                                cmmReference.child("Comments").child(postComments.get(position).getComment().getCmm_id()).child("cmm_content").setValue(new_comment);
                                                personal_comment_txt_comment.setText(new_comment);
                                                personal_comment_txt_xemthem.setVisibility(View.VISIBLE);
                                                personal_comment_txt_comment.setMaxLines(1000);
                                                personal_comment_txt_comment.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (personal_comment_txt_comment.getLineCount() > 3) {
                                                            personal_comment_txt_xemthem.setVisibility(View.VISIBLE);
                                                            personal_comment_txt_xemthem.setText("rút gọn.");
                                                        } else {
                                                            personal_comment_txt_xemthem.setVisibility(View.GONE);
                                                        }
                                                    }
                                                });
                                            } else {

                                            }


                                        }
                                    });
                                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });

                                    AlertDialog alertDialog = builder.create();
                                    alertDialog.show();
                                    Button positive = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
                                    positive.setAllCaps(false);
                                    Button negative = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                                    negative.setAllCaps(false);
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                } else {
                    PopupMenu popupMenu = new PopupMenu(context, button);
                    //Inflating menu
                    popupMenu.inflate(R.menu.post_comment_menu2);

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.post_comment_report) {
                                Toast.makeText(context, "Reported", Toast.LENGTH_SHORT).show();
                                return true;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            }
        });
        rc_comment.setAdapter(adapter_comment);



        //ImagePager
        imgAdapter = new ImagePagerAdapter(urls, PersonalPostActivity.this, ImageView.ScaleType.CENTER_CROP);
        viewPager.setAdapter(imgAdapter);

        ImgData();
        LikeButton();
        getPostData();
        MenuClick();
        getComment(postId);
        getCommentString();
        getCurrentUser();
        countTag(postId);

        personal_post_edt_comment_text.setOnClickListener(this);
        personal_post_btn_comment_clear_text.setOnClickListener(this);
        see_new_comment.setOnClickListener(this);
        personal_post_edt_comment_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    personal_post_btn_comment_clear_text.setVisibility(View.VISIBLE);
                    personal_post_btn_send_comment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!answerClick) {
                                DatabaseReference DataReference = firebaseDatabase.getReference();

                                String key = DataReference.child("Comments").push().getKey();
                                if (!TextUtils.isEmpty(personal_post_edt_comment_text.getText().toString().trim())) {
                                    Comment comment1 = new Comment(key, personal_post_edt_comment_text.getText().toString().trim(),
                                            getDate(), true, postId, currentUserId);
                                    // DataReference.child("Comments").child(key).setValue(comment1);
                                    Map<String, Object> friend_values = comment1.toMap();
                                    Map<String, Object> child_add = new HashMap<>();

                                    child_add.put("/Comments/" + key, friend_values);

                                    DataReference.updateChildren(child_add).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.e("Add Cmm", "Thanh cong");
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.e("Add Cmm", "That bai");
                                        }
                                    });

                                    personal_post_edt_comment_text.setText("");
                                    personal_post_btn_comment_clear_text.setVisibility(View.GONE);
                                    personal_post_edt_comment_text.clearFocus();
                                    hideSoftKeyboard(PersonalPostActivity.this);


                                    adapter_comment.notifyDataSetChanged();
                                    personal_post_comment_status.setVisibility(View.GONE);


                                } else {
                                    Toast.makeText(context, "Bạn chưa nhập comment hoặc quá nhiều khoảng trắng", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {

                            }
                            // phan up binh luan len

                            // phan up du lieu vao object message
                            upDataToMessage(0);
                        }
                    });
                }
                else {
                    personal_post_btn_comment_clear_text.setVisibility(View.GONE);
                }
            }
        });

        viewPagePosition();


    }

    @Override
    protected void injectVariables() {

    }


    public void viewPagePosition() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                personal_post_img_postision.setText((position + 1) + "/" + urls.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    /*
     * Get All Comment from server;
     * get User of ex comment info
     *
     *
     *
     */
    public void getComment(final String postId) {
        cmmDatabaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postComments.clear();
                ArrayList<PostComment> postComments2 = new ArrayList<>();

                for (DataSnapshot cmmSnap : dataSnapshot.getChildren()) {
                    PostComment postComment = new PostComment();
                    Comment comment = cmmSnap.getValue(Comment.class);
                    if (comment.getPost_id().equals(postId)) {
                        postComment.setComment(comment);
                        getCommentUser(comment.getFriend_id(), postComment);
                        postComments2.add(postComment);
                    }
                }
                if (postComments2.size() <= load_more) {
                    load_more = postComments2.size();
                    see_new_comment.setText("Het binh luan");
                    see_new_comment.setEnabled(false);
                } else {
                    see_new_comment.setText("Xem các bình luận cũ hơn");
                    see_new_comment.setEnabled(true);
                }
                for (int i = postComments2.size() - 1; i >= (postComments2.size() - load_more); i--) {
                    postComments.add(postComments2.get(i));
                    adapter_comment.notifyDataSetChanged();

                }
                adapter_comment.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getCommentUser(final String Uid, final PostComment postComment) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference userDatabaseReference = firebaseDatabase.getReference("Users");
        userDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users users = dataSnapshot1.getValue(Users.class);
                    if (Uid.equals(users.getUser_id())) {
                        postComment.setUsers(users);
                        break;
                    }
                }
                adapter_comment.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //===============================================

    /*
     * Get info of Post
     * get user who post this post
     * get all image of the post
     * Like button
     * count comment and like of post
     * count tag
     *
     */

    public void getPostData() {
        DatabaseReference postReference = firebaseDatabase.getReference("Post");
        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                    Post post = postSnap.getValue(Post.class);
                    if (post.getPost_id().equals(postId)) {
                        setUser_post(post.getUser_id());
                        getPostUserData(post.getUser_id());
                        personal_txt_location.setText(post.getPost_location_map());
                        personal_txt_content.setText(post.getPost_content());
                        personal_txt_time_item.setText(post.getPost_date());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getPostUserData(final String Uid) {
        DatabaseReference userReference = firebaseDatabase.getReference("Users");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnap : dataSnapshot.getChildren()) {
                    Users postUsers = userSnap.getValue(Users.class);
                    if (postUsers.getUser_id().equals(Uid)) {
                        if (!postUsers.getUser_avatar().equals("") && postUsers.getUser_avatar().startsWith("http")) {
                            Glide.with(PersonalPostActivity.this.getApplicationContext()).load(postUsers.getUser_avatar()).into(personal_img_user);
                        } else {
                            personal_img_user.setImageResource(R.drawable.ic_friend_black_24dp);
                        }
                        personal_txt_name_user.setText(postUsers.getUser_full_name());
                        personal_txt_status_user.setText("");
                        personal_txt_note_friend.setText("");


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void ImgData() {
        DatabaseReference imageReference = firebaseDatabase.getReference("Image");
        imageReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot imgSnap : dataSnapshot.getChildren()) {
                    Image image = imgSnap.getValue(Image.class);
                    if (image.getPost_id().equals(postId)) {
                        urls.add(image.getImg_link());

                    }
                }
                if (urls.size() == 0) {
                    personal_post_img_relative.setVisibility(View.GONE);
                }
                imgAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void initLikeButton() {
        userReference = firebaseDatabase.getReference("Like");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot dataLike : dataSnapshot.getChildren()) {
                    Like like = dataLike.getValue(Like.class);
                    if (like.getPost_id().equals(postId) && like.getFriend_id().equals(currentUser.getUid())) {

                        personal_post_cb_like_item.setChecked(true);
                    }
                    if (like.getPost_id().equals(postId)) {
                        i = i + 1;
                    }

                }
                personal_post_txt_count_like.setText(i + " người thích và");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //---------------------
    }


    public void countTag(final String postId) {

        DatabaseReference tagReference = firebaseDatabase.getReference("Tag");
        tagReference.addListenerForSingleValueEvent(new ValueEventListener() {
            final ArrayList<Tag> listTag = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot tagSnap : dataSnapshot.getChildren()) {
                    Tag tag = tagSnap.getValue(Tag.class);
                    if (tag.getPost_id().equals(postId)) {
                        listTag.add(tag);
                    }
                }
                if (listTag.size() == 0) {
                    personal_txt_status_user.setText("");
                    personal_txt_note_friend.setText("");
                } else {
                    personal_txt_status_user.setText("cùng với ");
                    personal_txt_note_friend.setText(listTag.size() + " người khác");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //=================================

    public void getCurrentUser() {
        userReference = firebaseDatabase.getReference("Users");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Users currentUsers = dataSnapshot1.getValue(Users.class);
                    if (currentUserId.equals(currentUsers.getUser_id())) {
                        if (!currentUsers.getUser_avatar().equals("") && currentUsers.getUser_avatar().startsWith("http")) {
                            Glide.with(PersonalPostActivity.this.getApplicationContext()).load(currentUsers.getUser_avatar()).into(personal_post_img_comment_ava);

                        } else {
                            personal_post_img_comment_ava.setImageResource(R.drawable.ic_friend_black_24dp);
                        }
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getCommentString() {
        userReference = firebaseDatabase.getReference("Comments");
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for (DataSnapshot dataCmm : dataSnapshot.getChildren()) {
                    Comment comment = dataCmm.getValue(Comment.class);
                    if (comment.getPost_id().equals(postId)) {
                        i = i + 1;
                    }

                }
                personal_post_txt_count_comment.setText(i + " bình luận");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //---------------------
    }

    public void LikeButton() {
        initLikeButton();
        personal_post_cb_like_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userReference = firebaseDatabase.getReference("Like");
                if (personal_post_cb_like_item.isChecked()) {

                    Date dNow = new Date();
                    SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy");
                    String key = userReference.child("Like").push().getKey();
                    Like like = new Like(ft.format(dNow), key, true, postId, currentUser.getUid());
                    userReference.child(key).setValue(like);

                    upDataToMessage(1);
                } else {

                    userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            for (DataSnapshot dataLike : dataSnapshot.getChildren()) {
                                Like like = dataLike.getValue(Like.class);
                                if (like.getPost_id().equals(postId) && like.getFriend_id().equals(currentUser.getUid())) {
                                    String key2 = dataLike.getKey();
                                    userReference.child(key2).removeValue();
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                    deleteMessage(postId);
                }
            }
        });
    }

    //======================================================================

    public void MenuClick() {
        personal_btn_menu_item_personal_post2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creat menu
                PopupMenu popupMenu = new PopupMenu(context, personal_btn_menu_item_personal_post2);
                //Inflating menu
                popupMenu.inflate(R.menu.post_comment_menu);

                popupMenu.show();
            }
        });
    }

    public String getDate() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy");
        return ft.format(dNow);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.personal_post_edt_comment_text:
                personal_post_btn_comment_clear_text.setVisibility(View.VISIBLE);
                break;

            case R.id.personal_post_btn_comment_clear_text:
                personal_post_edt_comment_text.setText("");
                personal_post_comment_status.setVisibility(View.VISIBLE);
                break;
            case R.id.see_new_comment:
                load_more= load_more + 5;
                getComment(postId);
                break;

        }
    }

    //----------------------------------------------------------------------------------------------
    /*phuong thuc up du lieu len object Message*/
    private void upDataToMessage(int type) {
        DatabaseReference reference = firebaseDatabase.getReference();

        String key = reference.child("Message").push().getKey();

        Message message = new Message(
                key,
                getDate(),
                type,
                false,
                postId,
                getUser_post(),
                currentUserId
        );

        Map<String, Object> message_values = message.toMap();
        Map<String, Object> child_update = new HashMap<>();
        child_update.put("/Message/" + key, message_values);
        reference.updateChildren(child_update).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.e("Status", "Thanh cong");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Status", "That bai");
            }
        });
    }
    /*phuong thuc up du lieu len object Message*/

    //----------------------------------------------------------------------------------------------
    /*phuong thuc delete message khi bam vao nut dislike*/
    private void deleteMessage(final String post_id){
        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        final DatabaseReference reference = firebase_database.getReference("Message");

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Message message = snapshot.getValue(Message.class);
                    if( post_id.equals(message.getPost_id()) && currentUserId.equals(message.getUser_id())){
                        reference.child(snapshot.getKey()).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc delete message khi bam vao nut dislike*/
}
