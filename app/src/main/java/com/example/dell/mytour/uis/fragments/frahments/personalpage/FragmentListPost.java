package com.example.dell.mytour.uis.fragments.frahments.personalpage;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PersonalPageAdapter.PersonalPostAdapter;
import com.example.dell.mytour.model.PersonalPost;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Like;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseFragments;
import com.example.dell.mytour.uis.activity.post_activity.PostUpPersonalActivity;
import com.example.dell.mytour.uis.activity.register_activity.EmailAndPasswordRegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentListPost extends BaseFragments implements View.OnClickListener {

    private RecyclerView listPost;
    private PersonalPostAdapter adapterPost;
    private ArrayList<PersonalPost> personalPosts;

    private CircleImageView frg_listpost_ava;
    private Button frg_listpost_btn_addpost;
    private Button frg_listpost_btn_add_image;
    private LinearLayout frg_listpost_addpost;
    private CircleImageView personal_img_user;

    private ProgressBar frg_listpost_progress;


    //firebase
    private DatabaseReference userReference;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserId = user.getUid();
    Users pageUser = new Users();

    // cac bien thuong dung trong bai
    private boolean user_status = false;
    private String user_id = "";
    private String user_name = "";
    private String user_avt = "";
//----------------------------------------------------------------------------------------------------------------------------
    /*phuong thuc get set cho ba bien tren*/

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_avt() {
        return user_avt;
    }

    public void setUser_avt(String user_avt) {
        this.user_avt = user_avt;
    }
    /*phuong thuc get set cho ba bien tren*/
//----------------------------------------------------------------------------------------------------------------------------


    @Override
    protected int injectLayout() {
        return R.layout.fragment_list_post;
    }

    @Override
    protected void injectView() {
        listPost = view_home.findViewById(R.id.personal_list_post);
        listPost.setNestedScrollingEnabled(false);

        frg_listpost_addpost = view_home.findViewById(R.id.frg_listpost_addpost);
        frg_listpost_ava = view_home.findViewById(R.id.frg_listpost_ava);
        frg_listpost_btn_add_image = view_home.findViewById(R.id.frg_listpost_btn_add_image);
        frg_listpost_btn_addpost = view_home.findViewById(R.id.frg_listpost_btn_addpost);
        frg_listpost_progress = view_home.findViewById(R.id.frg_listpost_progress);

        frg_listpost_progress.setVisibility(View.VISIBLE);

        frg_listpost_btn_addpost.setOnClickListener(this);
        frg_listpost_btn_add_image.setOnClickListener(this);


        personalPosts = new ArrayList<>();
        String Uid = getArguments().getString("UserId");


        if (user.getUid().equals(Uid)) {
            frg_listpost_addpost.setVisibility(View.VISIBLE);
        } else {
            frg_listpost_addpost.setVisibility(View.GONE);
        }
        setListPost();
        DataUser(Uid);
        getPost(Uid);
        //refreshPost(Uid);


    }


    @Override
    protected void injectVariables() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frg_listpost_btn_addpost:
                gotoPostPersolnalScreen();
                break;
            case R.id.frg_listpost_btn_add_image:
                gotoPostPersolnalScreen();
                break;
        }
    }


    private void gotoPostPersolnalScreen() {
        if (user.getUid() != null) {
            Intent intent_post_up = new Intent(getActivity(), PostUpPersonalActivity.class);
            intent_post_up.putExtra("user_name", getUser_name());
            intent_post_up.putExtra("user_avatar", getUser_avt());
            intent_post_up.putExtra("user_id", getUser_id());
            startActivity(intent_post_up);
        } else {
            showDialogWhenClickFalse();
        }
    }


    //----------------------------------------------------------------------------------------------
    /*phuong thuc showdialog khi ma nguoi dung chua dang nhap ma bam vao cac nut chuc nang*/
    private void showDialogWhenClickFalse() {
        final Dialog dialog = new Dialog(getActivity());
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
    /*phuong thuc showdialog khi ma nguoi dung chua dang nhap ma bam vao cac nut chuc nang*/

    //----------------------------------------------------------------------------------------------
    /*phương thức chuyển trang đến trang đăng ký*/
    private void gotoRegisterScreen() {
        Intent intent = new Intent(getActivity(), EmailAndPasswordRegisterActivity.class);
        startActivity(intent);
    }
    /*phương thức chuyển trang đến trang đăng ký*/


    public void setListPost() {
        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        listPost.setLayoutManager(manager);
        adapterPost = new PersonalPostAdapter(personalPosts, getContext(), currentUserId, new PersonalPostAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
        });
        listPost.setAdapter(adapterPost);
    }

    public void DataUser(final String Uid) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataUser : dataSnapshot.getChildren()) {
                    final Users users = dataUser.getValue(Users.class);
                    if (users.getUser_id().equals(Uid)) {
                        setUser_id(users.getUser_id());
                        setUser_avt(users.getUser_avatar());
                        setUser_name(users.getUser_full_name());
                        pageUser = users;
                        if (!users.getUser_avatar().equals("") && users.getUser_avatar().startsWith("http") && getContext() != null) {
                            Glide.with(getContext().getApplicationContext()).load(users.getUser_avatar()).into(frg_listpost_ava);
                        }
                        break;

                    }

                }

                frg_listpost_progress.setVisibility(View.GONE);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Không thể truy cập dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });


    }

    //=========listPost================
    public void getPostUser(final String Uid, final PersonalPost personalPost) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataUser : dataSnapshot.getChildren()) {
                    final Users users = dataUser.getValue(Users.class);
                    if (users.getUser_id().equals(Uid)) {
                        pageUser = users;
                        personalPost.setUsers(pageUser);
                        break;

                    }

                }
                adapterPost.notifyDataSetChanged();

                frg_listpost_progress.setVisibility(View.GONE);


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Không thể truy cập dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getPost(final String Uid) {
        FirebaseDatabase firebaseDatabase2 = FirebaseDatabase.getInstance();
        DatabaseReference postReference = firebaseDatabase2.getReference("Post");
        postReference.addListenerForSingleValueEvent(new ValueEventListener() {
            PersonalPost personalPost;
            ArrayList<PersonalPost> personalPosts2 = new ArrayList<>();

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postData : dataSnapshot.getChildren()) {

                    personalPost = new PersonalPost();
                    Post post = postData.getValue(Post.class);
                    if (post.getUser_id().equals(Uid) && !post.isPost_level()) {
                        personalPost.setPost(post);
                        //getImages(post.getPost_id(), personalPost);
                        getPostUser(Uid, personalPost);
                        getLike(post.getPost_id(), personalPost);
                        getFirstComment(post.getPost_id(), personalPost);
                        personalPosts2.add(personalPost);


                    }

                }
                for (int i = personalPosts2.size() - 1; i >= 0; i--) {
                    personalPosts.add(personalPosts2.get(i));
                    adapterPost.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

//    public void getImages(final String postId, final PersonalPost personalPost) {
//
//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference imageReference = firebaseDatabase.getReference("Image");
//        imageReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                final ArrayList<String> urls = new ArrayList<>();
//                for (DataSnapshot imgSnap : dataSnapshot.getChildren()) {
//                    Image image = imgSnap.getValue(Image.class);
//                    if (image.getPost_id().equals(postId)) {
//                        urls.add(image.getImg_link());
//                    }
//
//                }
//                personalPost.setImages(urls);
//                adapterPost.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

    public void getLike(final String postId, final PersonalPost personalPost) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference imageReference = firebaseDatabase.getReference("Like");
        imageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot likeSnap : dataSnapshot.getChildren()) {
                    Like like = likeSnap.getValue(Like.class);
                    if (like.getPost_id().equals(postId) && currentUserId.equals(like.getFriend_id())) {
                        personalPost.setLike(like);
                        adapterPost.notifyDataSetChanged();
                        break;
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getFirstComment(final String postId, final PersonalPost personalPost) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference commentReference = firebaseDatabase.getReference("Comments");
        commentReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot commentSnap : dataSnapshot.getChildren()) {
                    Comment comment = commentSnap.getValue(Comment.class);
                    if (postId.equals(comment.getPost_id())) {
                        personalPost.setComment(comment);
                        adapterPost.notifyDataSetChanged();
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

}