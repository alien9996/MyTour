package com.example.dell.mytour.uis.fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.BannerHomeAdapter;
import com.example.dell.mytour.adapter.PersonalPageAdapter.PersonalPostAdapter;
import com.example.dell.mytour.adapter.PostHomeAdapter;
import com.example.dell.mytour.event.RecyclerTouchListener;
import com.example.dell.mytour.model.PersonalPost;
import com.example.dell.mytour.model.PostItem;
import com.example.dell.mytour.model.model_base.Comment;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Like;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Rating;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseFragments;
import com.example.dell.mytour.uis.activity.LocationActivity;
import com.example.dell.mytour.uis.activity.google_map.MapsActivity;
import com.example.dell.mytour.uis.activity.post_activity.PostDetailsActivity;
import com.example.dell.mytour.uis.activity.post_activity.PostUpPersonalActivity;
import com.example.dell.mytour.uis.activity.register_activity.EmailAndPasswordRegisterActivity;
import com.example.dell.mytour.uis.activity.result.ResultActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentHome extends BaseFragments implements View.OnClickListener{

    // khai bao cac bien cho phan banner
    private ProgressBar progress_bar;
    // phan khai bao linear layout
    private LinearLayout linear_content_home;

    // khai bao bien handler dung cho viec tat dialog
    private Handler handler;
    // khai bao bien hinh anh
    private CircleImageView img_user_avatar_home_page;


    // recyclerView
    private RecyclerView recycler_banner_home, recycler_item_post_home;
    private RecyclerView recycler_item_personal_post_home;
    // khai bao cac button
    private Button btn_view_all_advertisement, btn_view_all_post,
                    btn_up_post_home_page, btn_camera_home_page;

    // adapter
    private PersonalPostAdapter list_posts_adapter;
    private PostHomeAdapter post_adapter;
    private BannerHomeAdapter banner_home_adapter;
    // Location
    private ImageView imgDaNang;
    private ImageView imgCatBa;
    private ImageView imgNhaTrang;
    private ImageView imgHaLong;
    private ImageView imgDaLat;
    private ImageView imgVungTau;
    private ImageView imgHue;
    private ImageView imgHaNoi;


    // arraylist
    private ArrayList<PostItem> lst_post;
    private ArrayList<PostItem> lst_advertiment_banner;
    private ArrayList<PersonalPost> personalPosts;
    // khai bao cac bien dung cho firebase
    FirebaseDatabase firebase_database;
    DatabaseReference database_reference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    // cac bien thuong dung trong bai
    private boolean user_status = false;
    private String user_id ="";
    private String user_name ="";
    private String user_avt ="";
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
        return R.layout.fragment_home_layout;
    }

    @Override
    protected void injectView() {

        //Location
        imgDaNang = view_home.findViewById(R.id.imgDaNang);
        imgCatBa = view_home.findViewById(R.id.imgCatBa);
        imgNhaTrang = view_home.findViewById(R.id.imgNhaTrang);
        imgHaLong = view_home.findViewById(R.id.imgHaLong);
        imgDaLat = view_home.findViewById(R.id.imgDaLat);
        imgVungTau = view_home.findViewById(R.id.imgVungTau);
        imgHue = view_home.findViewById(R.id.imgHue);
        imgHaNoi = view_home.findViewById(R.id.imgHaNoi);


        recycler_item_personal_post_home = view_home.findViewById(R.id.recycler_item_personal_post_home);
        // khoi tao linearlayout
        linear_content_home = view_home.findViewById(R.id.linear_content_home);
        progress_bar = view_home.findViewById(R.id.progress_bar);

        // khoi tao bien handler
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progress_bar.setVisibility(View.GONE);
                linear_content_home.setVisibility(View.VISIBLE);
            }
        },4000);

        //khoi tao cac bien dung cho firebase
        firebase_database = FirebaseDatabase.getInstance();
        lst_post = new ArrayList<>();
        personalPosts = new ArrayList<>();
        lst_advertiment_banner = new ArrayList<>();

        // khoi tao anh dai dien
        img_user_avatar_home_page = view_home.findViewById(R.id.img_user_avatar_home_page);

        // khoi tao cac button
        btn_view_all_post = view_home.findViewById(R.id.btn_view_all_post);
        btn_view_all_advertisement = view_home.findViewById(R.id.btn_view_all_advertisement);
        btn_up_post_home_page = view_home.findViewById(R.id.btn_up_post_home_page);
        btn_camera_home_page = view_home.findViewById(R.id.btn_camera_home_page);

        //set su kien onclick cho cac button
        btn_view_all_post.setOnClickListener(this);
        btn_view_all_advertisement.setOnClickListener(this);
        btn_camera_home_page.setOnClickListener(this);
        btn_up_post_home_page.setOnClickListener(this);

        // goi cac phuong thuc
        setAvatarUser();
        createBannerDataHomeFragment();
        createPostHomePage();

        clickLocation();
        setOnClickForRecyclerView();

        if (user != null) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String Uid = user.getUid();
                    loadPosts(Uid);
                }
            }, 3000);

        } else {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    String Uid = "";
                    loadPosts(Uid);
                }
            }, 3000);
        }

    }

    @Override
    protected void injectVariables() {

    }

    //----------------------------------------------------------------------------------------------
    /*phuong thuc set anh dai dien cho phan dang bai*/
    private void setAvatarUser(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user != null){
            setUser_id(user.getUid());
            user_status = true;
            database_reference = firebase_database.getReference("Users");
            database_reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot_user : dataSnapshot.getChildren()){
                        Users users = snapshot_user.getValue(Users.class);
                        if(user.getUid().equals(users.getUser_id())){
                            if(users.getUser_avatar().equals("") || users.getUser_avatar() == null){
                                img_user_avatar_home_page.setImageResource(R.drawable.ic_friend_black_24dp);
                            } else{
                                Glide.with(getContext().getApplicationContext()).load(users.getUser_avatar()).into(img_user_avatar_home_page);

                            }
                            setUser_name(users.getUser_full_name());
                            setUser_avt(users.getUser_avatar());
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
            user_status = false;
            img_user_avatar_home_page.setImageResource(R.drawable.ic_friend_black_24dp);
        }
    }
    /*phuong thuc set anh dai dien cho phan dang bai*/


    //----------------------------------------------------------------------------------------------
    /*ke thua phuong thuc onclick*/
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_view_all_advertisement:
                Intent intent_post_advertisement = new Intent(getActivity(), ResultActivity.class);
                intent_post_advertisement.putExtra("intent_type", "0");
                startActivity(intent_post_advertisement);
                break;
            case R.id.btn_view_all_post:
                Intent intent_post = new Intent(getActivity(), ResultActivity.class);
                intent_post.putExtra("intent_type", "1");
                startActivity(intent_post);
                break;
            case R.id.btn_up_post_home_page:
                if(user_status){
                    Intent intent_post_up = new Intent(getActivity(), PostUpPersonalActivity.class);
                    intent_post_up.putExtra("user_name",getUser_name());
                    intent_post_up.putExtra("user_avatar",getUser_avt());
                    intent_post_up.putExtra("user_id", user.getUid());
                    startActivity(intent_post_up);
                } else {
                    showDialogWhenClickFalse();
                }
                break;
            case R.id.btn_camera_home_page:
                if(user_status){
                    Intent intent_post_up = new Intent(getActivity(), PostUpPersonalActivity.class);
                    intent_post_up.putExtra("user_name",getUser_name());
                    intent_post_up.putExtra("user_avatar",getUser_avt());
                    intent_post_up.putExtra("user_id", getUser_id());
                    startActivity(intent_post_up);
                } else {
                    showDialogWhenClickFalse();
                }
                /*Intent intent =  new Intent(getActivity(), MapsActivity.class);
                startActivity(intent);
                break;*/
        }
    }
    /*ke thua phuong thuc onclick*/

    //-------------------------------------------------------------------------------------------------------------------------
    // cac phuong thuc show dialog
    /*phuong thuc show dialog khi moi khoi dong ung dung*/
    private void showDialogLoading(){
        final Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_loading);

        dialog.show();
    }
    /*phuong thuc show dialog khi moi khoi dong ung dung*/

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
    /*phuong thuc showdialog khi ma nguoi dung chua dang nhap ma bam vao cac nut chuc nang*/



    //----------------------------------------------------------------------------------------------
    /*phương thức chuyển trang đến trang đăng ký*/
    private void gotoRegisterScreen(){
        Intent intent = new Intent(getActivity(), EmailAndPasswordRegisterActivity.class);
        startActivity(intent);
    }
    /*phương thức chuyển trang đến trang đăng ký*/



    //----------------------------------------------------------------------------------------------
    /*// phuong thuc tạo giá trị cho mục banner trong trang FragmentHome*/
    private void createBannerDataHomeFragment() {

        recycler_banner_home = view_home.findViewById(R.id.recycler_banner_home);
        recycler_banner_home.setNestedScrollingEnabled(false);

        getReferenceBanner();

        banner_home_adapter = new BannerHomeAdapter(getActivity(), lst_advertiment_banner);
        RecyclerView.LayoutManager layout_manager_banner_home = new LinearLayoutManager(getActivity());
        recycler_banner_home.setLayoutManager(layout_manager_banner_home);
        recycler_banner_home.setAdapter(banner_home_adapter);

    }
    /*// phuong thuc tạo giá trị cho mục banner trong trang FragmentHome*/


    //----------------------------------------------------------------------------------------------
    /*// *phuong thuc set su kien onClick cho item trong recuclerView*/
    public void setOnClickForRecyclerView() {
        // su kien onclick cho phan banner
        recycler_banner_home.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recycler_banner_home, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), PostDetailsActivity.class);
                intent.putExtra("post_id", lst_advertiment_banner.get(lst_advertiment_banner.size() - position - 1).getPost_id().toString());
                intent.putExtra("user_avatar", lst_advertiment_banner.get(lst_advertiment_banner.size() - position - 1).getUser_post_avatar().toString());
                intent.putExtra("user_name", lst_advertiment_banner.get(lst_advertiment_banner.size() - position - 1).getUser_post_name().toString());
                intent.putExtra("rating_post", lst_advertiment_banner.get(lst_advertiment_banner.size() - position - 1).getPost_rating_average().toString());
                intent.putExtra("post_background", lst_advertiment_banner.get(lst_advertiment_banner.size() - position - 1).getPost_avatar());
                getContext().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));


        // su kien onclick cho phan bai dang
        recycler_item_post_home.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recycler_item_post_home, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getContext(), PostDetailsActivity.class);
                intent.putExtra("post_id", lst_post.get(lst_post.size() - position - 1).getPost_id().toString());
                intent.putExtra("user_avatar", lst_post.get(lst_post.size() - position - 1).getUser_post_avatar().toString());
                intent.putExtra("user_name", lst_post.get(lst_post.size() - position - 1).getUser_post_name().toString());
                intent.putExtra("rating_post", lst_post.get(lst_post.size() - position - 1).getPost_rating_average().toString());
                intent.putExtra("post_background", lst_post.get(lst_post.size() - position - 1).getPost_avatar());
                getContext().startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }
    /*// *phuong thuc set su kien onClick cho item trong recuclerView*/


    //------------------Location Click------------------------------------------------------------------------------------------
    public void clickLocation() {
        imgDaNang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoScreenLocation("0");
            }
        });
        imgHaNoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoScreenLocation("0");
            }
        });
        imgHue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoScreenLocation("1");
            }
        });
        imgNhaTrang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoScreenLocation("1");
            }
        });
        imgCatBa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoScreenLocation("1");
            }
        });
        imgDaLat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoScreenLocation("0");
            }
        });
        imgVungTau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoScreenLocation("0");
            }
        });
        imgHaLong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoScreenLocation("0");
            }
        });
    }

    //phuong thuc chuyen trang sang trang location
    private void gotoScreenLocation(String status){
        Intent intent = new Intent(getActivity(), LocationActivity.class);
        intent.putExtra("status", status);
        startActivity(intent);
    }


    //----------------------------------------------------------------------------------------------
    /*//phuong thuc khoi tao gia tri cho phan bai viet ben trang chu*/

    public void createPostHomePage() {
        try {
            recycler_item_post_home = view_home.findViewById(R.id.recycler_item_post_home);
            recycler_item_post_home.setNestedScrollingEnabled(false); //set trang thai cho recyclerView ko bi mat gia toc


            getReferencePost();

            post_adapter = new PostHomeAdapter(getActivity(), lst_post);
            RecyclerView.LayoutManager layout_manager = new LinearLayoutManager(getContext());
            recycler_item_post_home.setLayoutManager(layout_manager);
            recycler_item_post_home.setAdapter(post_adapter);

        } catch (Exception ex) {
            Log.d("Post:", "" + ex.getMessage());
        }
    }
    /*//phuong thuc khoi tao gia tri cho phan bai viet ben trang chu*/


    //----------------------------------------------------------------------------------------------

    /*phuong thuc getReferenceBanner*/
    private void getReferenceBanner() {
        database_reference = firebase_database.getReference("Post");

        if (database_reference != null) {

            database_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lst_advertiment_banner.clear();
                    for (DataSnapshot post_snapshot : dataSnapshot.getChildren()) {
                        PostItem post_item = new PostItem();
                        Post post = post_snapshot.getValue(Post.class);

                        if(post.getPost_type() == 0){

                            getAvatarPost(post.getPost_id(), post_item);
                            getUserInfo(post.getUser_id(), post_item);
                            getAverageRating(post.getPost_id(), post_item);
                            faceDataPost(post, post_item);
                            lst_advertiment_banner.add(post_item);

                        }

                    }
                    banner_home_adapter.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
    /*phuong thuc getReferenceBanner*/


    //----------------------------------------------------------------------------------------------

    /*phuong thuc getReferencePost*/
    private void getReferencePost() {
        database_reference = firebase_database.getReference("Post");

        if (database_reference != null) {

            database_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    lst_post.clear();
                    for (DataSnapshot post_snapshot : dataSnapshot.getChildren()) {
                        PostItem post_item = new PostItem();
                        Post post = post_snapshot.getValue(Post.class);
                        if(post.getPost_type() != 0 && post.isPost_level()){
                            getAvatarPost(post.getPost_id(), post_item);
                            getUserInfo(post.getUser_id(), post_item);
                            getAverageRating(post.getPost_id(), post_item);
                            faceDataPost(post, post_item);
                            lst_post.add(post_item);
                        }

                    }
                    post_adapter.notifyDataSetChanged();

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
        item_post.setPost_location_stop(post.getPost_location_service_end());
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
                post_adapter.notifyDataSetChanged();
                banner_home_adapter.notifyDataSetChanged();
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
                post_adapter.notifyDataSetChanged();
                banner_home_adapter.notifyDataSetChanged();
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
                post_adapter.notifyDataSetChanged();
                banner_home_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*phuong thuc tinh diem rating*/

    //========Loading PersonalPost=============
    private void loadPosts(String Uid) {

        RecyclerView.LayoutManager manager = new LinearLayoutManager(getContext());
        recycler_item_personal_post_home.setLayoutManager(manager);
        list_posts_adapter = new PersonalPostAdapter(personalPosts, getContext(), Uid);
        recycler_item_personal_post_home.setAdapter(list_posts_adapter);

        getPost();
    }

    public void seeMorePost(){

    }

    public void getPost() {
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
                    if (!post.isPost_level()) {
                        personalPost.setPost(post);
                        //getImages(post.getPost_id(), personalPost);
                        getPostUser(post.getUser_id(), personalPost);
                        getLike(post.getPost_id(), personalPost);
                        getFirstComment(post.getPost_id(), personalPost);
                        personalPosts2.add(personalPost);
                    }

                }
                for (int i = personalPosts2.size()-1; i>=0; i--){
                    personalPosts.add(personalPosts2.get(i));
                    list_posts_adapter.notifyDataSetChanged();
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getPostUser(final String Uid, final PersonalPost personalPost) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("Users");

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot dataUser : dataSnapshot.getChildren()) {
                    Users users = dataUser.getValue(Users.class);
                    if (users.getUser_id().equals(Uid)) {
                        personalPost.setUsers(users);
                        break;

                    }

                }
                list_posts_adapter.notifyDataSetChanged();


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Không thể truy cập dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void getLike(final String postId, final PersonalPost personalPost) {
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference imageReference = firebaseDatabase.getReference("Like");
            imageReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot likeSnap : dataSnapshot.getChildren()) {
                        Like like = likeSnap.getValue(Like.class);
                        if (like.getPost_id().equals(postId) && currentUser.getUid().equals(like.getFriend_id())) {
                            personalPost.setLike(like);
                            list_posts_adapter.notifyDataSetChanged();
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
                        list_posts_adapter.notifyDataSetChanged();
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
