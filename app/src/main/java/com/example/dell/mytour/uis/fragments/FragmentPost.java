package com.example.dell.mytour.uis.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.PostAdapter;
import com.example.dell.mytour.event.RecyclerTouchListener;
import com.example.dell.mytour.model.PostItem;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Rating;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseFragments;
import com.example.dell.mytour.uis.activity.post_activity.PostDetailsActivity;
import com.example.dell.mytour.uis.activity.post_activity.PostUpContentActivity;
import com.example.dell.mytour.uis.activity.post_activity.PostUpTypeActivity;
import com.example.dell.mytour.uis.activity.register_activity.EmailAndPasswordRegisterActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentPost extends BaseFragments implements View.OnClickListener{
    // phan dung recyclerView
    private RecyclerView recycler_post;
    private PostAdapter post_pdapter;

    private TextView txt_post_content_share, txt_name_user_post;
    private Button btn_post_type_introduce, btn_post_type_sharing_experiences,btn_post_type_advertisement;
    private CircleImageView img_avatar_user_post;

    private LinearLayout linear_layout_user;
    private boolean user_status = true;

    // khai bao bien dung cho firebase
    private FirebaseDatabase firebase_database;
    private DatabaseReference databse_reference;
    private ArrayList<PostItem> lst_post;




    @Override
    protected int injectLayout() {
        return R.layout.fragment_post_layout;
    }

    @Override
    protected void injectView() {
        // khai bao cac text view
        txt_post_content_share = view_home.findViewById(R.id.txt_post_content_share);
        txt_name_user_post = view_home.findViewById(R.id.txt_name_user_post);

        //khoi tao circleImageView cho anh dai dien
        img_avatar_user_post = view_home.findViewById(R.id.img_avatar_user_post);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(getActivity()));

        // khai bao cac button
        btn_post_type_introduce = view_home.findViewById(R.id.btn_post_type_introduce);
        btn_post_type_sharing_experiences = view_home.findViewById(R.id.btn_post_type_sharing_experiences);
        btn_post_type_advertisement = view_home.findViewById(R.id.btn_post_type_advertisement);

        // khai bao lineae layout
        linear_layout_user = view_home.findViewById(R.id.linear_layout_user);

        // khoi tao firebase database
        firebase_database = FirebaseDatabase.getInstance();

        // set su kien onclick cho button
        btn_post_type_introduce.setOnClickListener(this);
        btn_post_type_advertisement.setOnClickListener(this);
        btn_post_type_sharing_experiences.setOnClickListener(this);
        txt_post_content_share.setOnClickListener(this);

        // goi cac phuong thuc
        checkUserStatus();
        createPost();
        setOnClickForRecyclerView();

    }

    @Override
    protected void injectVariables() {

    }

    //----------------------------------------------------------------------------------------------
    /*phuong thuc kiem tra xem nguoi dung da dang nhap chua de tuy chinh giao dien*/
    private void checkUserStatus(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            linear_layout_user.setVisibility(View.VISIBLE);
            user_status = true;

            FirebaseDatabase firebase_database = FirebaseDatabase.getInstance();
            databse_reference = firebase_database.getReference("Users");
            databse_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot_user : dataSnapshot.getChildren()){
                        Users users = snapshot_user.getValue(Users.class);
                        if(user.getUid().equals(users.getUser_id())){
                            if(users.getUser_avatar().equals("") || users.getUser_avatar() == null){
                                img_avatar_user_post.setImageResource(R.drawable.ic_friend_black_24dp);
                            } else{
                                ImageLoader image_loader = ImageLoader.getInstance();
                                image_loader.displayImage(users.getUser_avatar().toString(),img_avatar_user_post);
                            }
                            txt_name_user_post.setText(users.getUser_full_name());
                            break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Loi", "Khong  the lay ve thong tin user");
                }
            });

        } else {
            linear_layout_user.setVisibility(View.GONE);
            user_status = false;
        }
    }

    // khởi tạo recycler view
    private void createPost(){

        try {
            recycler_post = view_home.findViewById(R.id.recycler_post);
            recycler_post.setNestedScrollingEnabled(false);

            lst_post = new ArrayList<>();
            getReference();

            post_pdapter = new PostAdapter(getActivity(), lst_post);
            RecyclerView.LayoutManager layout_manager = new LinearLayoutManager(getContext());
            recycler_post.setLayoutManager(layout_manager);
            recycler_post.setAdapter(post_pdapter);

        }catch (Exception ex){
            Log.d("Post:", "" + ex.getMessage());
        }

    }

    // phương thức set onclich cho recycler view
    public void setOnClickForRecyclerView(){
        recycler_post.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recycler_post, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), PostDetailsActivity.class);
                intent.putExtra("post_id", lst_post.get(lst_post.size()-position-1).getPost_id().toString());
                intent.putExtra("user_avatar", lst_post.get(lst_post.size()-position-1).getUser_post_avatar().toString());
                intent.putExtra("user_name",lst_post.get(lst_post.size()-position-1).getUser_post_name().toString());
                intent.putExtra("rating_post", lst_post.get(lst_post.size()-position-1).getPost_rating_average().toString());
                intent.putExtra("post_background", lst_post.get(lst_post.size()-position-1).getPost_avatar());
                getContext().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    // phuong thuc onclick
    @Override
    public void onClick(View v) {
        Intent intent_post_up;
        switch (v.getId()){
            case R.id.txt_post_content_share:
                if(user_status){
                    intent_post_up = new Intent(getActivity(), PostUpTypeActivity.class);
                    startActivity(intent_post_up);
                } else {
                    showDialogWhenClickFalse();
                }

                break;
            case R.id.btn_post_type_introduce:
                if(user_status){
                    intent_post_up = new Intent(getActivity(), PostUpContentActivity.class);
                    intent_post_up.putExtra("post_style","2");
                    startActivity(intent_post_up);
                } else {
                    showDialogWhenClickFalse();
                }

                break;
            case R.id.btn_post_type_sharing_experiences:
                if(user_status){
                    intent_post_up = new Intent(getActivity(), PostUpContentActivity.class);
                    intent_post_up.putExtra("post_style","1");
                    startActivity(intent_post_up);
                } else {
                    showDialogWhenClickFalse();
                }

                break;
            case R.id.btn_post_type_advertisement:
                if(user_status){
                    intent_post_up = new Intent(getActivity(), PostUpContentActivity.class);
                    intent_post_up.putExtra("post_style","0");
                    startActivity(intent_post_up);
                } else {
                    showDialogWhenClickFalse();
                }

                break;

        }
    }

    //----------------------------------------------------------------------------------------------
    /*phuong thuc showdialog khi ma nguoi dung chua dang nhap ma bam vao cac nut chuc nang*/
    private void showDialogWhenClickFalse(){
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

    /*phương thức chuyển trang đến trang đăng ký*/
    private void gotoRegisterScreen(){
        Intent intent = new Intent(getActivity(), EmailAndPasswordRegisterActivity.class);
        startActivity(intent);
    }




    //----------------------------------------------------------------------------------------------

    /*phuong thuc getReference*/
    private void getReference(){

        databse_reference = firebase_database.getReference("Post");

        if(databse_reference != null){
            databse_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lst_post.clear();
                    for(DataSnapshot post_snapshot: dataSnapshot.getChildren()) {
                        PostItem post_item = new PostItem();
                        Post post = post_snapshot.getValue(Post.class);

                        if(post.isPost_level()){
                            getAvatarPost(post.getPost_id(), post_item);
                            getUserInfo(post.getUser_id(), post_item);
                            getAverageRating(post.getPost_id(), post_item);
                            faceDataPost(post, post_item);
                            lst_post.add(post_item);
                        }

                    }
                    post_pdapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
    /*phuong thuc getReference*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc set du lieu vap post*/
    private void faceDataPost(Post post, PostItem item_post) {

        String mini_content = "";
        if(post.getPost_content().length() > 200){
            mini_content = post.getPost_content().substring(0,200)+"...";
        } else {
            mini_content = post.getPost_content();
        }
        item_post.setPost_id(post.getPost_id());
        item_post.setPost_title(post.getPost_title());
        item_post.setPost_content(mini_content);
        item_post.setPost_date(post.getPost_date());
    }
    /*phuong thuc set du lieu vap post*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc lay hinh anh cho bai dang*/
    private void getAvatarPost(final String post_id, final PostItem item_post){

        databse_reference = firebase_database.getReference("Image");
        databse_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot_image: dataSnapshot.getChildren()){
                    Image image = snapshot_image.getValue(Image.class);
                    if(post_id.equals(image.getPost_id())){
                        item_post.setPost_avatar(image.getImg_link().toString());
                        break;
                    }
                }
                post_pdapter.notifyDataSetChanged();
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
    private void getUserInfo(final String user_id, final PostItem item_post){

        databse_reference = firebase_database.getReference("Users");
        databse_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot_user : dataSnapshot.getChildren()){
                    Users users = snapshot_user.getValue(Users.class);
                    if(user_id.equals(users.getUser_id())){
                        item_post.setUser_post_avatar(users.getUser_avatar().toString());
                        item_post.setUser_post_name(users.getUser_full_name());
                        break;
                    }
                }
                post_pdapter.notifyDataSetChanged();
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
    private void getAverageRating(final String post_id, final PostItem item_post){

        databse_reference = firebase_database.getReference("Rating");
        databse_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                float average = 0;
                for(DataSnapshot snapshot_rating: dataSnapshot.getChildren()){
                    Rating rating = snapshot_rating.getValue(Rating.class);
                    if(post_id.equals(rating.getPost_id())){
                        average = Float.parseFloat(rating.getRating_level());
                        item_post.setPost_rating_average(String.valueOf(average));
                        break;
                    }
                }
                if(average == 0){
                    average = (float) 3.5;
                    item_post.setPost_rating_average(String.valueOf(average));
                }
                post_pdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc tinh diem rating*/


}























