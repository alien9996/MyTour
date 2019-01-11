package com.example.dell.mytour.uis.activity.post_activity;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPageAdapter.PersonalPostCommentAdapter;
import com.example.dell.mytour.adapter.PostItemDetailsAdapter;
import com.example.dell.mytour.model.PostComment;
import com.example.dell.mytour.model.PostDetailsItem;
import com.example.dell.mytour.model.PostItem;
import com.example.dell.mytour.model.model_base.Answer;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Rating;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.PersonalPostActivity;
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
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailsActivity extends BaseActivity implements View.OnClickListener {

    // khai bao cac bien ben view
    private Toolbar tool_bar_post_detail;
    private CollapsingToolbarLayout collapsing_tool_bar_layout;


    // linear layout
    private LinearLayout linear_time_service, linear_time_start, linear_transport;

    // image view
    private ImageView img_backdrop;

    // text view
    private TextView txt_location_start, txt_location_stop,
            txt_time_service, txt_time_start, txt_transport, txt_post_title;

    // button
    private Button btn_call;
    private RatingBar ratingBar;

    // khai bao cac bien cuc bo gan du lieu tu get intent
    private String post_id, user_avatar, user_name, rating_post, post_background;

    // khai bao cac bien su dung cho filebase
    private FirebaseDatabase firebase_database;
    private DatabaseReference database_reference;

    // khai bao ArrayList luu noi dung va hinh anh bai viet
    private RecyclerView recycler_post_content_details;
    private RecyclerView recycler_post_comment_details;
    private ArrayList<PostDetailsItem> list_post_tem;
    private PostItemDetailsAdapter post_item_adapter;
    private ArrayList<PostComment> postComments;
    private PersonalPostCommentAdapter adapter_comment;
    private Button recycler_post_btn_load_comment;

    //add comment
    CircleImageView post_details_img_comment_ava;

    EditText post_details_edt_comment_text;
    Button post_details_btn_comment_clear_text, post_details_btn_send_comment;

    final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    int load_more_cmm = 5;


    @Override
    protected int injectLayout() {
        return R.layout.activity_post_detail_layout;
    }

    @Override
    protected void injectView() {
        tool_bar_post_detail = findViewById(R.id.tool_bar_post_detail);
        setSupportActionBar(tool_bar_post_detail);

        //add comment
        post_details_img_comment_ava = findViewById(R.id.post_detais_img_comment_ava);
        post_details_edt_comment_text = findViewById(R.id.post_details_edt_comment_text);
        post_details_btn_send_comment = findViewById(R.id.post_details_btn_send_comment);
        post_details_btn_comment_clear_text = findViewById(R.id.post_details_btn_comment_clear_text);


        recycler_post_comment_details = findViewById(R.id.recycler_post_comment_details);
        postComments = new ArrayList<>();
        recycler_post_btn_load_comment = findViewById(R.id.recycler_post_btn_load_comment);
        recycler_post_btn_load_comment.setOnClickListener(this);

        ActionBar action_bar = getSupportActionBar();
        action_bar.setDisplayHomeAsUpEnabled(true);

        collapsing_tool_bar_layout = findViewById(R.id.collapsing_tool_bar_layout);


        // get du lieu tu bai dang truyen sang
        Intent intent = getIntent();
        post_id = intent.getStringExtra("post_id");
        user_avatar = intent.getStringExtra("user_avatar");
        user_name = intent.getStringExtra("user_name");
        rating_post = intent.getStringExtra("rating_post");
        post_background = intent.getStringExtra("post_background");

        // khoi tao cac bien dung cho firebase
        firebase_database = FirebaseDatabase.getInstance();

        // khoi tao cac bien linear layout
        linear_time_service = findViewById(R.id.linear_time_service);
        linear_time_start = findViewById(R.id.linear_time_start);
        linear_transport = findViewById(R.id.linear_transport);

        // khoi tao cac bien text view
        txt_location_start = findViewById(R.id.txt_location_start);
        txt_location_stop = findViewById(R.id.txt_location_stop);
        txt_time_service = findViewById(R.id.txt_time_service);
        txt_time_start = findViewById(R.id.txt_time_start);
        txt_transport = findViewById(R.id.txt_transport);
        txt_post_title = findViewById(R.id.txt_post_title);

        // khoi tao cac button
        btn_call = findViewById(R.id.btn_call);


        // khoi tao cac image view
        img_backdrop = findViewById(R.id.img_backdrop);

        btn_call.setOnClickListener(this);

        // goi cac phuong thuc
        createPostItem();
        setBackgroundPost();

        //khoi tao rating bar
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat(rating_post));

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                setRatingPost();
            }
        });

        //comment
        initCommentAdapter();
        getComment(post_id);
        if (currentUser == null) {
            post_details_img_comment_ava.setEnabled(false);
            post_details_btn_send_comment.setEnabled(false);
            post_details_edt_comment_text.setEnabled(false);
            post_details_btn_comment_clear_text.setEnabled(false);
        } else {
            getCurrentUser();
            addComment(post_id);
        }

    }

    @Override
    protected void injectVariables() {

    }

    // ke thua phuong thuc onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.recycler_post_btn_load_comment:
                load_more_cmm = load_more_cmm + 5;
                getComment(post_id);
                break;
            case R.id.btn_call:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mytour.vn/") );

                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                startActivity(intent);
                break;
        }
    }

    //----------------------------------------------------------------------------------------------
    /*phuong thuc set background cho bai dang*/
    private void setBackgroundPost() {
        if (post_background == null) {
            img_backdrop.setImageResource(R.drawable.hinh_b);
        } else {
            Glide.with(this).load(post_background).into(img_backdrop);
        }
    }
    /*phuong thuc set background cho bai dang*/

    //----------------------------------------------------------------------------------------------
    /*ke thua lai phuong thuc click vao item tren thanh toolBar*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //----------------------------------------------------------------------------------------------
    /*phuong thuc set du lieu cho phan noi dung cua bai dang*/
    private void createPostItem() {
        try {
            recycler_post_content_details = findViewById(R.id.recycler_post_content_details);
            recycler_post_content_details.setNestedScrollingEnabled(false);

            getPostData();
            list_post_tem = new ArrayList<>();
            post_item_adapter = new PostItemDetailsAdapter(PostDetailsActivity.this, list_post_tem);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PostDetailsActivity.this);
            recycler_post_content_details.setLayoutManager(layoutManager);
            recycler_post_content_details.setAdapter(post_item_adapter);

        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
    }
    /*phuong thuc set du lieu cho phan noi dung cua bai dang*/

    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay du lieu bai dang ve*/
    private void getPostData() {
        database_reference = firebase_database.getReference("Post");

        if (database_reference != null) {
            database_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                    for (DataSnapshot post_snapshot : dataSnapshot.getChildren()) {
                        Post post = post_snapshot.getValue(Post.class);
                        if (post_id.equals(post.getPost_id())) {
                            setImageAndTextForAlistPost(post.getPost_content());
                            if (post.getPost_type() == 0) {
                                txt_time_service.setText(post.getPost_time_service().toString());
                                txt_time_start.setText(post.getPost_time_service_start());
                                txt_transport.setText(post.getPost_transport_service());
                                collapsing_tool_bar_layout.setTitle(post.getPost_title_advertisement());

                            } else {
                                linear_time_service.setVisibility(View.GONE);
                                linear_time_start.setVisibility(View.GONE);
                                linear_transport.setVisibility(View.GONE);
                                btn_call.setVisibility(View.GONE);
                                collapsing_tool_bar_layout.setTitle(post.getPost_title());
                            }
                            txt_location_start.setText(post.getPost_location_service_start().toString());
                            txt_location_stop.setText(post.getPost_location_service_end().toString());
                            txt_post_title.setText(post.getPost_title());
                            break;
                        }
                    }
                    post_item_adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    /*phuong thuc lay du lieu bai dang ve*/

    //----------------------------------------------------------------------------------------------
    /*phuong thuc set hinh anh va text cho list_item_post*/
    private void setImageAndTextForAlistPost(String post_content) {

        final String[] lst_content = post_content.split("<img/>");

        database_reference = firebase_database.getReference("Image");
        database_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int i = 0;
                for (DataSnapshot snapshot_image : dataSnapshot.getChildren()) {
                    Image image = snapshot_image.getValue(Image.class);
                    if (post_id.equals(image.getPost_id())) {
                        if (i < lst_content.length) {
                            list_post_tem.add(new PostDetailsItem(lst_content[i].replace("\n", ""), image.getImg_link().toString(), image.getImg_description()));
                        } else {
                            list_post_tem.add(new PostDetailsItem(null, image.getImg_link().toString(), image.getImg_description()));
                        }
                        i++;
                    }
                }
                if (i < lst_content.length) {
                    list_post_tem.add(new PostDetailsItem(lst_content[i].replace("\n", ""), null, null));
                }
                post_item_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Loi", "Khong  the lay ve hinh anh bai dang");
            }
        });
    }
    /*phuong thuc set hinh anh va text cho list_item_post*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc set Rating cho bai dang*/
    private void setRatingPost() {
        float get_rating = ratingBar.getRating();
        float rating_average = (get_rating + Float.parseFloat(rating_post)) / 2;

        database_reference = firebase_database.getReference();

        String key = database_reference.child("Rating").push().getKey();

        Rating rating = new Rating(key, String.valueOf(rating_average), post_id);

        Map<String, Object> rating_value = rating.toMap();

        Map<String, Object> child_add = new HashMap<>();
        child_add.put("/Rating/" + post_id, rating_value);

        database_reference.updateChildren(child_add).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showToast("Đánh giá của bạn đã được tiếp nhận. Cám ơn vì đã quan tâm.");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast("Không thể hoàn thành đánh giá. Bạn hãy thử lại sau.");
            }
        });

    }
    /*phuong thuc set Rating cho bai dang*/

    //============Show listComment ===============
    public void initCommentAdapter() {
        String currentUserId = null;
        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
            currentUserId = "";
        }
        postComments = new ArrayList<>();
        final String finalCurrentUserId = currentUserId;
        final String finalCurrentUserId1 = currentUserId;
        adapter_comment = new PersonalPostCommentAdapter(postComments, PostDetailsActivity.this, currentUserId, new PersonalPostCommentAdapter.OnItemClickListener() {
            @Override
            public void onAnswerClick(EditText editText, View view, int position) {
                Button personal_post_btn_send_answer = view.findViewById(R.id.personal_post_btn_send_answer);

                post_details_edt_comment_text.setFocusableInTouchMode(false);
                editText.setFocusableInTouchMode(true);
                editText.requestFocus();
                personal_post_btn_send_answer.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSendClick(Button button, View view, ArrayList<Answer> answerList, int position) {
                RecyclerView post_comment_list_answer = view.findViewById(R.id.post_comment_list_answer);
                EditText personal_post_edt_answer_text = view.findViewById(R.id.personal_post_edt_answer_text);

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference dataReference = firebaseDatabase.getReference();

                String key = dataReference.child("Answers").push().getKey();
                if (!TextUtils.isEmpty(personal_post_edt_answer_text.getText().toString().trim())) {
                    Answer answer = new Answer(key, personal_post_edt_answer_text.getText().toString().trim()
                            , getDate(), true, finalCurrentUserId, postComments.get(position).getComment().getCmm_id());
                    dataReference.child("Answers").child(key).setValue(answer);

//                    answerComments.add(answer);
                    post_comment_list_answer.getAdapter().notifyDataSetChanged();

                    personal_post_edt_answer_text.setText("");
                    personal_post_edt_answer_text.clearFocus();
                } else {
                    Toast.makeText(PostDetailsActivity.this, "Bạn chưa nhập comment hoặc quá nhiều khoảng trắng", Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onMenuClick(Button button, View view, final int position) {
                final TextView personal_comment_txt_comment = view.findViewById(R.id.personal_comment_txt_comment);
                final TextView personal_comment_txt_xemthem = view.findViewById(R.id.personal_comment_txt_xemthem);
                //firebase
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                final DatabaseReference cmmReference = firebaseDatabase.getReference();
                //Creat menu

                if (finalCurrentUserId1.equals(postComments.get(position).getUsers().getUser_id())) {
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
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recycler_post_comment_details.setLayoutManager(manager);
        recycler_post_comment_details.setAdapter(adapter_comment);
    }

    public void getComment(final String postId) {
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference cmmDatabaseReference = firebaseDatabase.getReference("Comments");
        cmmDatabaseReference.addValueEventListener(new ValueEventListener() {


            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postComments.clear();
                ArrayList<PostComment> postComments2 = new ArrayList<>();
                for (DataSnapshot cmmSnap : dataSnapshot.getChildren()) {
                    Comment comment = cmmSnap.getValue(Comment.class);
                    PostComment postComment = new PostComment();
                    if (comment.getPost_id().equals(postId)) {
                        postComment.setComment(comment);
                        getCommentUser(comment.getFriend_id(), postComment);
                        postComments2.add(postComment);
                    }
                }
                if (postComments2.size() <= load_more_cmm) {
                    load_more_cmm = postComments2.size();
                    recycler_post_btn_load_comment.setText("Het");
                    recycler_post_btn_load_comment.setEnabled(false);
                } else {
                    recycler_post_btn_load_comment.setText("Xem thêm");
                    recycler_post_btn_load_comment.setEnabled(true);
                }
                for (int i = postComments2.size() - 1; i >= (postComments2.size() - load_more_cmm); i--) {
                    postComments.add(postComments2.get(i));
                    adapter_comment.notifyDataSetChanged();

                }


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

    //Add comment to post
    public void addComment(final String postId) {
        post_details_edt_comment_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    post_details_btn_comment_clear_text.setVisibility(View.VISIBLE);
                    post_details_edt_comment_text.requestFocus();
                } else {
                    post_details_btn_comment_clear_text.setVisibility(View.GONE);
                }

            }
        });
        post_details_edt_comment_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_details_edt_comment_text.setFocusableInTouchMode(true);
                post_details_edt_comment_text.requestFocus();
            }
        });

        post_details_btn_comment_clear_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                post_details_edt_comment_text.setText("");
            }
        });
        post_details_btn_send_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference DataReference = firebaseDatabase.getReference();
                String key = DataReference.child("Comments").push().getKey();
                if (!TextUtils.isEmpty(post_details_edt_comment_text.getText().toString().trim())) {
                    Comment comment1 = new Comment(key, post_details_edt_comment_text.getText().toString().trim(),
                            getDate(), true, postId, currentUser.getUid());
                    adapter_comment.notifyDataSetChanged();
                    post_details_edt_comment_text.setText("");
                    post_details_btn_comment_clear_text.setVisibility(View.GONE);
                    post_details_edt_comment_text.clearFocus();

                    DataReference.child("Comments").child(key).setValue(comment1);

                } else {
                    Toast.makeText(context, "Bạn chưa nhập comment hoặc quá nhiều khoảng trắng", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getDate() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy");
        return ft.format(dNow);
    }

    public void getCurrentUser(){
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("Users");
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    Users currentUsers = dataSnapshot1.getValue(Users.class);
                    if (currentUser.getUid().equals(currentUsers.getUser_id())){
                        if (!currentUsers.getUser_avatar().equals("") && currentUsers.getUser_avatar().startsWith("http")){
                            Glide.with(PostDetailsActivity.this.getApplicationContext()).load(currentUsers.getUser_avatar()).into(post_details_img_comment_ava);

                        }
                        else {
                            post_details_img_comment_ava.setImageResource(R.drawable.ic_friend_black_24dp);
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
}
