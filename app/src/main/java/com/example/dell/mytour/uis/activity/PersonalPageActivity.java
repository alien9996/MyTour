package com.example.dell.mytour.uis.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.model_base.Follows;
import com.example.dell.mytour.model.model_base.Friend;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Message;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.register_activity.EmailAndPasswordRegisterActivity;
import com.example.dell.mytour.uis.fragments.frahments.personalpage.FragmentListDiary;
import com.example.dell.mytour.uis.fragments.frahments.personalpage.FragmentListFriend;
import com.example.dell.mytour.uis.fragments.frahments.personalpage.FragmentListImage;
import com.example.dell.mytour.uis.fragments.frahments.personalpage.FragmentListPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PersonalPageActivity extends BaseActivity implements View.OnClickListener {
    private FragmentTabHost fragment_tab_host;

    private boolean linearLayout;
    private ImageView img_ava_background;
    private ImageView img_avatar;

    private Button personal_page_edit_info, btn_add_friend;
    private TextView txt_full_name;
    private TextView txt_address;
    private TextView personal_count_followed;
    private TextView personal_count_pictures;
    private TextView personal_count_post;

    private DatabaseReference userReference;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();
    String Uid = null;

    @Override
    protected void onStart() {
        super.onStart();
        linearLayout = findViewById(R.id.personal_page_linearLayout).requestFocus();

    }

    @Override
    protected int injectLayout() {
        return R.layout.activity_personal_page;
    }

    @Override
    protected void injectView() {

        img_ava_background = findViewById(R.id.img_avt_background);
        img_avatar = findViewById(R.id.img_avatar);

        personal_page_edit_info = findViewById(R.id.personal_page_edit_info);
        btn_add_friend = findViewById(R.id.btn_add_friend);
        btn_add_friend.setOnClickListener(this);

        txt_full_name = findViewById(R.id.txt_full_name);
        txt_address = findViewById(R.id.txt_address);

        personal_count_followed = findViewById(R.id.personal_count_followed);
        personal_count_pictures = findViewById(R.id.personal_count_pictures);
        personal_count_post = findViewById(R.id.personal_count_post);


        Intent intent_main = getIntent();
        Uid = intent_main.getStringExtra("UserId");

        getCurrentUser(Uid);
        initFollows();
        countPost();
        countPictures();
        if (user != null) {
            if (user.getUid().equals(Uid)) {
                createFragmentTabHost();
                personal_page_edit_info.setOnClickListener(buttonClick);
            } else {
                createAnotherUserFragmentTabHost();
                personal_page_edit_info.setText("Follow");
                personal_page_edit_info.setOnClickListener(buttonClick2);
                btn_add_friend.setVisibility(View.VISIBLE);
            }
        }

        // goi cac ham
        checkFriend();


    }

    /*// khai bao tinh trang button addFriend*/
    private int btn_type = 0;
    private String friend_key;

    public String getFriend_key() {
        return friend_key;
    }

    public void setFriend_key(String friend_key) {
        this.friend_key = friend_key;
    }

    public int getBtn_type() {
        return btn_type;
    }

    public void setBtn_type(int btn_type) {
        this.btn_type = btn_type;
    }

    @Override
    protected void injectVariables() {

    }
    /*// khai bao tinh trang button addFriend*/



    // ke thua phuong thuc onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add_friend:
                if(btn_type == 0){
                    addFriend();
                } else {
                    if(btn_type == 3){
                        confirmFriend(Uid);
                        updateStatus(getFriend_key());
                        btn_add_friend.setEnabled(false);
                        btn_add_friend.setText("Bạn bè");
                    }
                }

                break;
        }
    }

    //--------------------------------------------------------------------------------------------------
    /*phuong thuc kiem tra xem da ket ban hay chua*/
    private void checkFriend(){
        if(user != null){
            FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebase_database.getReference("Friend");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Friend friend = snapshot.getValue(Friend.class);
                        if(user.getUid().equals(friend.getUser_id()) && Uid.equals(friend.getFriend_link())){
                            if(friend.isFriend_status()){
                                btn_add_friend.setText("Đang là bạn");
                                btn_add_friend.setEnabled(false);
                                setBtn_type(2);
                                return;
                            } else {
                                btn_add_friend.setText("Đã gửi lời mời");
                                btn_add_friend.setEnabled(false);
                                setBtn_type(1);
                                return;
                            }
                        }
                        if(user.getUid().equals(friend.getFriend_link()) && Uid.equals(friend.getUser_id()) && !friend.isFriend_status()){
                            btn_add_friend.setText("Xác nhận bạn bè");
                            setFriend_key(friend.getFriend_id());
                            setBtn_type(3);
                            return;
                        }
                    }
                    setBtn_type(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
    /*phuong thuc kiem tra xem da ket ban hay chua*/

    // phuong thuc get Date
    //--------------------------------------------------------------------------------------------------
    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        return "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)
                + " - " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    //----------------------------------------------------------------------------------------------
    // cac phuong thuc set ket ban
    /*khi click vao nut ket ban*/
    private void addFriend() {
        if (user != null) {
            FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebase_database.getReference();

            String key = reference.child("Friend").push().getKey();

            Friend friend = new Friend(key, Uid, false, getDateTime(), user.getUid());

            Map<String, Object> friend_values = friend.toMap();
            Map<String, Object> child_add = new HashMap<>();

            child_add.put("/Friend/" + key, friend_values);

            reference.updateChildren(child_add).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    btn_add_friend.setText("Đã gửi lời mời");
                    btn_add_friend.setEnabled(false);
                    Log.e("Add Friend", "Thanh cong");
                    upDataToMessage(2);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Add Friend", "That bai");
                }
            });


        } else {
            showDialogWhenClickFalse();
        }
    }
    /*khi click vao nut ket ban*/




    //----------------------------------------------------------------------------------------------
    // cac phuong thuc set ket ban
    /*khi click vao nut xac nhan ban*/
    private void confirmFriend(String friend_id) {
        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase_database.getReference();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String key = reference.child("Friend").push().getKey();

        Friend friend = new Friend(key, friend_id, true, getDateTime(), user.getUid());

        Map<String, Object> friend_values = friend.toMap();
        Map<String, Object> child_add = new HashMap<>();

        child_add.put("/Friend/" + key, friend_values);

        reference.updateChildren(child_add).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Add Friend", "That bai");
            }
        });

    }

    /*khi click vao nut xac nhan ban*/

    //----------------------------------------------------------------------------------------------
    /*update friend stattus true*/
    private void updateStatus(String key){
        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase_database.getReference();

        reference.child("Friend").child(key).child("friend_status").setValue(true);
    }
    /*update friend stattus true*/





    //----------------------------------------------------------------------------------------------
    /*phuong thuc up len firebase Message*/
    private void upDataToMessage(int type) {
        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase_database.getReference();

        String key = reference.child("Message").push().getKey();

        Message message = new Message(
                key,
                getDateTime(),
                type,
                false,
                "",
                Uid,
                user.getUid()
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
    /*phuong thuc up len firebase Message*/


    //----------------------------------------------------------------------------------------------
    // cac phuong thuc show dialog
    //----------------------------------------------------------------------------------------------
    /*phuong thuc showdialog khi ma nguoi dung chua dang nhap ma bam vao cac nut chuc nang*/
    private void showDialogWhenClickFalse() {
        final Dialog dialog = new Dialog(PersonalPageActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_fail);

        TextView txt_title_message_fail, txt_message_fail;
        Button btn_action, btn_again;

        txt_title_message_fail = dialog.findViewById(R.id.txt_title_message_fail);
        txt_message_fail = dialog.findViewById(R.id.txt_message_fail);
        btn_action = dialog.findViewById(R.id.btn_action);
        btn_again = dialog.findViewById(R.id.btn_again);

        txt_message_fail.setText("Đây là chức năng dàng cho người dùng là thành viên hoặc doanh nghệp." +
                " Để sử dụng chức năng này bạn hãy đăng ký một tài khoản.");
        txt_title_message_fail.setText("Bạn không thể thực hiện thao tác này");
        btn_again.setText("Hủy");
        btn_action.setText("Đăng ký ngay");

        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegisterScreen();
                dialog.cancel();
            }
        });

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    /*phương thức chuyển trang đến trang đăng ký*/
    private void gotoRegisterScreen() {
        Intent intent = new Intent(PersonalPageActivity.this, EmailAndPasswordRegisterActivity.class);
        startActivity(intent);
    }

    //----------------------------------------------------------------------------------------------
    // phuong thuc khoi tao fragment
    private void createFragmentTabHost() {

        fragment_tab_host = findViewById(android.R.id.tabhost);
        fragment_tab_host.setup(this, getSupportFragmentManager(), R.id.real_tab_content);

        Bundle bundle = new Bundle();
        bundle.putString("UserId", Uid);

        fragment_tab_host.clearAllTabs();
        fragment_tab_host.addTab(fragment_tab_host.newTabSpec("Bài viết")
                .setIndicator(getTabIndicator(this, "Bài viết", R.drawable.ic_format_list_bulleted_black_24dp)), FragmentListPost.class, bundle);

        fragment_tab_host.addTab(fragment_tab_host.newTabSpec("Ảnh")
                .setIndicator(getTabIndicator(this, "Ảnh", R.drawable.ic_list_image_black_24dp)), FragmentListImage.class, bundle);

        fragment_tab_host.addTab(fragment_tab_host.newTabSpec("Nhật ký")
                .setIndicator(getTabIndicator(this, "Nhật ký", R.drawable.ic_history_black_24dp)), FragmentListDiary.class, bundle);

        fragment_tab_host.addTab(fragment_tab_host.newTabSpec("Bạn bè")
                .setIndicator(getTabIndicator(this, "Bạn bè", R.drawable.ic_friend_black_24dp)), FragmentListFriend.class, bundle);


    }

    private void createAnotherUserFragmentTabHost() {

        fragment_tab_host = findViewById(android.R.id.tabhost);
        fragment_tab_host.setup(this, getSupportFragmentManager(), R.id.real_tab_content);

        Bundle bundle = new Bundle();
        bundle.putString("UserId", Uid);

        fragment_tab_host.clearAllTabs();
        fragment_tab_host.addTab(fragment_tab_host.newTabSpec("Bài viết")
                .setIndicator(getTabIndicator(this, "Bài viết", R.drawable.ic_format_list_bulleted_black_24dp)), FragmentListPost.class, bundle);

        fragment_tab_host.addTab(fragment_tab_host.newTabSpec("Ảnh")
                .setIndicator(getTabIndicator(this, "Ảnh", R.drawable.ic_list_image_black_24dp)), FragmentListImage.class, bundle);

        fragment_tab_host.addTab(fragment_tab_host.newTabSpec("Bạn bè")
                .setIndicator(getTabIndicator(this, "Bạn bè", R.drawable.ic_friend_black_24dp)), FragmentListFriend.class, bundle);


    }

    public View getTabIndicator(Context context, String title, int icon) {

        View v = LayoutInflater.from(context).inflate(R.layout.item_tabhost_widget, null);
        ImageView img = (ImageView) v.findViewById(R.id.item_tabhost_img);
        img.setImageResource(icon);
        TextView titleIndicator = (TextView) v.findViewById(R.id.item_tabhost_title);
        titleIndicator.setText(title);
        XmlResourceParser parser = getResources().getXml(R.xml.tabhost_change_text_color);
        try {
            ColorStateList colorStateList = ColorStateList.createFromXml(getResources(), parser);
            titleIndicator.setTextColor(colorStateList);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return v;
    }


    //-----OnClick-------------
    Button.OnClickListener buttonClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.personal_page_edit_info:
                    Intent intent = new Intent(PersonalPageActivity.this, EditInfoUserActivity.class);
                    startActivity(intent);
            }
        }
    };

    Button.OnClickListener buttonClick2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.personal_page_edit_info:
                    if (personal_page_edit_info.getText().equals("Follow")) {
                        addFollow();
                        personal_page_edit_info.setText("Followed");
                    } else {
                        unFollow();
                        personal_page_edit_info.setText("Follow");
                    }


            }
        }
    };

    public void countPictures() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference pictureReference = firebaseDatabase.getReference("Image");

        pictureReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count_pic = 0;
                for (DataSnapshot picSnap : dataSnapshot.getChildren()) {
                    Image image = picSnap.getValue(Image.class);
                    if (Uid.equals(image.getUser_id())) {
                        count_pic++;
                    }
                }
                personal_count_pictures.setText(String.valueOf(count_pic));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void countPost() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference postReference = firebaseDatabase.getReference("Post");

        postReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count_post = 0;
                for (DataSnapshot postSnap : dataSnapshot.getChildren()) {
                    Post post = postSnap.getValue(Post.class);
                    if (Uid.equals(post.getUser_id())) {
                        count_post++;
                    }
                }
                personal_count_post.setText(String.valueOf(count_post));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void initFollows() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference followReference = firebaseDatabase.getReference("Follows");

        followReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count_followed = 0;
                for (DataSnapshot followSnap : dataSnapshot.getChildren()) {
                    Follows follows = followSnap.getValue(Follows.class);
                    if (currentUserId.equals(follows.getUser_id()) && Uid.equals(follows.getFriend_id())) {
                        personal_page_edit_info.setText("Followed");
                    }
                    if (Uid.equals(follows.getFriend_id())) {
                        count_followed++;
                    }
                }
                personal_count_followed.setText(String.valueOf(count_followed));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addFollow() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference followReference = firebaseDatabase.getReference();

        String key = followReference.child("Follows").push().getKey();
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy");
        Follows follows = new Follows(key, true, ft.format(dNow).toString(), currentUserId, Uid);
        followReference.child("Follows").child(key).setValue(follows);

    }

    public void unFollow() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference followReference = firebaseDatabase.getReference("Follows");

        followReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot followSnap : dataSnapshot.getChildren()) {
                    Follows follows = followSnap.getValue(Follows.class);
                    if (currentUserId.equals(follows.getUser_id()) && Uid.equals(follows.getFriend_id())) {
                        String key = followSnap.getKey();
                        followReference.child(key).removeValue();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getCurrentUser(final String Uid) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");


        if (user != null) {

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Users users = data.getValue(Users.class);
                        if (users.getUser_id().equals(Uid)) {
                            txt_full_name.setText(users.getUser_full_name());
                            txt_address.setText(users.getUser_address());
                            if (!users.getUser_avatar().equals("") && users.getUser_avatar().startsWith("http") && getBaseContext() != null) {
                                Glide.with(context.getApplicationContext()).load(users.getUser_avatar()).into(img_avatar);
                                Glide.with(context.getApplicationContext()).load(users.getUser_avatar()).into(img_ava_background);

                            } else {
                                img_avatar.setImageResource(R.drawable.ic_friend_black_24dp);
                                img_ava_background.setImageResource(R.drawable.ic_friend_black_24dp);
                            }
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(PersonalPageActivity.this, "Không thể truy cập dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }


}