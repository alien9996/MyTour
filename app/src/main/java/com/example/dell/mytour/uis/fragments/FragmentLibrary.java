package com.example.dell.mytour.uis.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.model_base.Follows;
import com.example.dell.mytour.model.model_base.Friend;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseFragments;
import com.example.dell.mytour.uis.activity.HistoryAndDiaryActivity;
import com.example.dell.mytour.uis.activity.LoginActivity;
import com.example.dell.mytour.uis.activity.LoginGmailActivity;
import com.example.dell.mytour.uis.activity.MainActivity;
import com.example.dell.mytour.uis.activity.PersonalPageActivity;
import com.example.dell.mytour.uis.activity.register_activity.EmailAndPasswordRegisterActivity;
import com.example.dell.mytour.uis.activity.result.FriendListActivity;
import com.example.dell.mytour.uis.fragments.frahments.personalpage.FragmentListDiary;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentLibrary extends BaseFragments implements View.OnClickListener {

    // phan khai bao cac button
    Button btn_info_detail_user_libary, btn_logout, btn_login_gmail, btn_register, btn_login;
    Button btn_frg_library_not_login_login, btn_list_friend_and_invitation, btn_history;

    // phan khai bao cac text view
    TextView txt_name_user;
    TextView user_count_follower;
    TextView user_count_pictures;
    TextView user_count_posts;

    //
    CircleImageView img_avt_user_library;
    LinearLayout frg_library_not_login, frg_library_login;
    ProgressBar account_progressbar;

    private DatabaseReference userReference;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected int injectLayout() {
        return R.layout.fragment_library_layout;
    }

    @Override
    protected void injectView() {

        // phan khoi tao cac button
        btn_info_detail_user_libary = view_home.findViewById(R.id.btn_info_detail_user_libary);
        btn_logout = view_home.findViewById(R.id.btn_logout);
        btn_login_gmail = view_home.findViewById(R.id.btn_login_gmail);
        btn_register = view_home.findViewById(R.id.btn_register);
        btn_login = view_home.findViewById(R.id.btn_login);
        btn_list_friend_and_invitation = view_home.findViewById(R.id.btn_list_friend_and_invitation);
        btn_history = view_home.findViewById(R.id.btn_history);

        //
        frg_library_login = view_home.findViewById(R.id.frg_library_login);
        frg_library_not_login = view_home.findViewById(R.id.frg_library_not_login);

        //
        btn_frg_library_not_login_login = view_home.findViewById(R.id.btn_frg_library_not_login_login);
        txt_name_user = view_home.findViewById(R.id.txt_name_user);
        img_avt_user_library = view_home.findViewById(R.id.img_avt_user_library);
        account_progressbar = view_home.findViewById(R.id.account_progressbar);
        user_count_follower = view_home.findViewById(R.id.user_count_follower);
        user_count_pictures = view_home.findViewById(R.id.user_count_pictures);
        user_count_posts = view_home.findViewById(R.id.user_count_posts);


        // phan set onclick cho btn
        btn_info_detail_user_libary.setOnClickListener(this);
        btn_logout.setOnClickListener(this);
        btn_login_gmail.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        btn_frg_library_not_login_login.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        btn_list_friend_and_invitation.setOnClickListener(this);
        btn_history.setOnClickListener(this);


        if (user != null) {

            frg_library_not_login.setVisibility(View.GONE);
            account_progressbar.setVisibility(View.VISIBLE);
            getCurrentUser();
            getCountFormFriend();
        } else {
            btn_logout.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
            btn_list_friend_and_invitation.setVisibility(View.GONE);
        }

        // goi cac phuong thuc

    }

    @Override
    protected void injectVariables() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_info_detail_user_libary:
                Intent intent_personal_page = new Intent(getActivity(), PersonalPageActivity.class);
                String Uid = user.getUid();
                intent_personal_page.putExtra("UserId", Uid);
                startActivity(intent_personal_page);
                break;
            case R.id.btn_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent_login = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent_login);
                getActivity().finish();
                break;
            case R.id.btn_login_gmail:
                gotoLoginScreen();
                break;
            case R.id.btn_register:
                Intent intent_register = new Intent(getActivity(), EmailAndPasswordRegisterActivity.class);
                startActivity(intent_register);
                break;
            case R.id.btn_frg_library_not_login_login:
                FirebaseAuth.getInstance().signOut();
                Intent intent_login2 = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent_login2);
                getActivity().finish();
                break;
            case R.id.btn_login:
                gotoLoginScreen();
                break;
            case R.id.btn_list_friend_and_invitation:
                Intent intent = new Intent(getActivity(), FriendListActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_history:
                Intent intent_history = new Intent(getActivity(), HistoryAndDiaryActivity.class);
                startActivity(intent_history);
                break;


        }
    }

    private void gotoLoginScreen() {
        FirebaseAuth.getInstance().signOut();
        Intent intent_login = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent_login);
        getActivity().finish();
    }


    //----------------------------------------------------------------------------------------------
    /*kiem tra trong bang friend de dua ra thong bao*/
    private void getCountFormFriend() {

        FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebase_database.getReference("Friend");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total_friend = 0;
                int total_invitation = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Friend friend = snapshot.getValue(Friend.class);
                    if (user.getUid().equals(friend.getUser_id()) && friend.isFriend_status()) {
                        total_friend++;
                    }
                    if (user.getUid().equals(friend.getFriend_link()) && !friend.isFriend_status()) {
                        total_invitation++;
                    }
                }
                btn_list_friend_and_invitation.setText("   Bạn bè("+total_friend+" bạn và "+total_invitation+" lời mời)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*kiem tra trong bang friend de dua ra thong bao*/

    public void getCurrentUser() {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");


        if (user != null) {
            countFollows(user.getUid());
            countPictures(user.getUid());
            countPost(user.getUid());
            //Toast.makeText(getContext(), " "+user.getUid(), Toast.LENGTH_SHORT).show();

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Users users = data.getValue(Users.class);
                        //Toast.makeText(MainActivity.this, ""+users.getUser_id(), Toast.LENGTH_SHORT).show();
                        if (users.getUser_id().equals(user.getUid())) {
                            // Toast.makeText(getContext(), ""+users.getUser_full_name(), Toast.LENGTH_SHORT).show();
                            txt_name_user.setText(users.getUser_full_name());

                            //image ava
                            if (users.getUser_avatar() != null && users.getUser_avatar().startsWith("http") && getContext() != null) {
                                Glide.with(getContext().getApplicationContext()).load(users.getUser_avatar()).into(img_avt_user_library);
                            }

                            frg_library_login.setVisibility(View.VISIBLE);
                            break;
                        }
                    }

                    account_progressbar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Không thể truy cập dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    public void countPictures(final String Uid) {
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
                user_count_pictures.setText(String.valueOf(count_pic));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void countPost(final String Uid) {
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
                user_count_posts.setText(String.valueOf(count_post));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void countFollows(final String Uid) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference followReference = firebaseDatabase.getReference("Follows");

        followReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int count_followed = 0;
                for (DataSnapshot followSnap : dataSnapshot.getChildren()) {
                    Follows follows = followSnap.getValue(Follows.class);
                    if (Uid.equals(follows.getFriend_id())) {
                        count_followed++;
                    }
                }
                user_count_follower.setText(String.valueOf(count_followed));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

//    public Bitmap decodeFromFirebaseBase64(String image) throws IOException{
//        byte[] decodeByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(decodeByteArray, 0, decodeByteArray.length);
//
//    }
}
