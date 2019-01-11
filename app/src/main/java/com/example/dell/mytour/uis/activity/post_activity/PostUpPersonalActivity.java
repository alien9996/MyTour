package com.example.dell.mytour.uis.activity.post_activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.ImagePostUpAdapter;
import com.example.dell.mytour.adapter.PersonalPost.ListFriendAdapter;
import com.example.dell.mytour.event.RecyclerTouchListener;
import com.example.dell.mytour.model.ItemFriend;
import com.example.dell.mytour.model.model_base.Friend;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Tag;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.MainActivity;
import com.example.dell.mytour.uis.activity.register_activity.AddressAndBirthdayRegisterActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostUpPersonalActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;

    // khai bao bien cho googlePlaceAutoComplice
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    // khai bao hinh anh
    private CircleImageView img_avatar_user_personal_post;

    // khai bao text view
    private TextView txt_name_user_personal_post, txt_tag_friend, txt_title_tag_friend,
            txt_location_title, txt_location_post;

    // phuong thuc khai bao edit tex
    private EditText edt_content_post_personal_up;

    // khai bao button
    private Button btn_personal_add_image, btn_tag_friend, btn_set_post_type, btn_add_location_personal_post_up,
            btn_use_camera_personal_post, btn_up_personal_post, bnt_add_personal_image_top;

    // khai bao linear layout
    private LinearLayout linear_layout_img_personal_post_up;

    //khai bao recycler view
    private RecyclerView recycler_image_personal_post;

    // khai bao bien dung cho firebase
    private FirebaseDatabase firebase_database;
    private DatabaseReference database_reference;

    // khai bao bien dung cho load danh sach ban be
    private ListFriendAdapter friend_adapter;
    ArrayList<ItemFriend> lst_friend;


    // khai bao cac bien dung trong bai
    private String user_id = "";
    private String user_name = "";
    private String user_avt = "";
    private ArrayList<String> list_id_tag_friend = null;

    //cac bien dung cho viec su dung camera va doc the nho
    ArrayList<Uri> lst_image_uri = null;
    private Bitmap image_bitmap = null;
    private ArrayList<Bitmap> lst_bitmaps;
    private ImagePostUpAdapter image_post_up_adapter;
    private int position_lst_image = -1;
    private ProgressDialog progress_dialog;

    private String txt_tag = "";
    @Override
    protected int injectLayout() {
        return R.layout.activity_personal_post_up;
    }

    @Override
    protected void injectView() {

        // phan khoi tao toolBar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ActionBar action_bar = getSupportActionBar();
        action_bar.setDisplayHomeAsUpEnabled(true);
        action_bar.setTitle("Bài đăng cá nhân");

        // phan khoi tao image view
        img_avatar_user_personal_post = findViewById(R.id.img_avatar_user_personal_post);

        // phan khoi tao text view
        txt_name_user_personal_post = findViewById(R.id.txt_name_user_personal_post);
        txt_tag_friend = findViewById(R.id.txt_tag_friend);
        txt_title_tag_friend = findViewById(R.id.txt_title_tag_friend);
        txt_location_title = findViewById(R.id.txt_location_title);
        txt_location_post = findViewById(R.id.txt_location_post);

        txt_location_post.setText("");

        //khoi tao edit  text
        edt_content_post_personal_up = findViewById(R.id.edt_content_post_personal_up);

        // phan khoi tao button
        btn_personal_add_image = findViewById(R.id.btn_personal_add_image);
        btn_tag_friend = findViewById(R.id.btn_tag_friend);
        btn_set_post_type = findViewById(R.id.btn_set_post_type);
        btn_use_camera_personal_post = findViewById(R.id.btn_use_camera_personal_post);
        btn_up_personal_post = findViewById(R.id.btn_up_personal_post);
        bnt_add_personal_image_top = findViewById(R.id.bnt_add_personal_image_top);
        btn_add_location_personal_post_up = findViewById(R.id.btn_add_location_personal_post_up);

        //set  su kien onclick cho cac button
        btn_personal_add_image.setOnClickListener(this);
        btn_tag_friend.setOnClickListener(this);
        btn_set_post_type.setOnClickListener(this);
        btn_use_camera_personal_post.setOnClickListener(this);
        btn_up_personal_post.setOnClickListener(this);
        bnt_add_personal_image_top.setOnClickListener(this);
        btn_add_location_personal_post_up.setOnClickListener(this);

        // phan khoi tao linear layout
        linear_layout_img_personal_post_up = findViewById(R.id.linear_layout_img_personal_post_up);

        // phan khoi tao recycler view
        recycler_image_personal_post = findViewById(R.id.recycler_image_personal_post);
        recycler_image_personal_post.setNestedScrollingEnabled(false);

        // khoi tao cac bien dung cho camera va hinh anh
        lst_bitmaps = new ArrayList<>();

        // phan khoi tao recycler view theo chieu ngang

        setImageRecyclerView(); // cau lenh nay phai duoxc goi truoc khi set receler view theo chieu ngang thi moi co hieu qua
        recycler_image_personal_post.setLayoutManager(new LinearLayoutManager(recycler_image_personal_post.getContext(),
                LinearLayoutManager.HORIZONTAL, false));


        // khoi tao ArrayList
        list_id_tag_friend = new ArrayList<>();

        // phan khoi tao phuong thuc dung firebase
        firebase_database = FirebaseDatabase.getInstance();

        // khoi tao cac bien dung cho camera va hinh anh
        lst_image_uri = new ArrayList<>();

        // phan goi cac phuong thuc
        setInfoUser();
        setOnClickForRecyclerView();


    }

    @Override
    protected void injectVariables() {

    }

    //----------------------------------------------------------------------------------------------
    /*ke thua lai phuong thuc click vao item tren thanh toolBar*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showDialogWhenClickButtonBack();
        }
        return super.onOptionsItemSelected(item);
    }
    /*ke thua lai phuong thuc click vao item tren thanh toolBar*/

    //----------------------------------------------------------------------------------------------
    /*ke thua phuong thuc onclick*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_personal_add_image:
                handleGetImageFromGallery();
                linear_layout_img_personal_post_up.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_tag_friend:
                showDialogListFriend();
                break;
            case R.id.btn_set_post_type:
                showDialogSetPostType();
                break;
            case R.id.btn_use_camera_personal_post:
                linear_layout_img_personal_post_up.setVisibility(View.VISIBLE);
                hadleGetImageFromCamera();
                break;
            case R.id.bnt_add_personal_image_top:
                handleGetImageFromGallery();
                linear_layout_img_personal_post_up.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_up_personal_post:
                writePostData();
                break;
            case R.id.btn_add_location_personal_post_up:
                gotoGooglePlaceAPI();
                break;
        }
    }
    /*ke thua phuong thuc onclick*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc show place google API*/
    private void gotoGooglePlaceAPI(){
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(PostUpPersonalActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }
    }
    /*phuong thuc show place google API*/




    //----------------------------------------------------------------------------------------------
    /*phuong thuc set thong tin cho user*/
    private void setInfoUser() {
        Intent intent = getIntent();
        user_id = intent.getStringExtra("user_id");
        user_name = intent.getStringExtra("user_name");
        user_avt = intent.getStringExtra("user_avatar");

        if (user_avt != null || ("").equals(user_avt)) {
            Glide.with(this).load(user_avt).into(img_avatar_user_personal_post);
        } else {
            img_avatar_user_personal_post.setImageResource(R.drawable.ic_friend_black_24dp);
        }

        txt_name_user_personal_post.setText(user_name);
    }

    /*phuong thuc set thong tin cho user*/


    //--------------------------------------------------------------------------------------------------
    /*phuong thuc lay du lieu friend tu firebase*/
    private void getFrendReferent(){

        database_reference = firebase_database.getReference("Friend");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                lst_friend.clear();
                for(DataSnapshot snapshot_friend : dataSnapshot.getChildren()){
                    Friend friend = snapshot_friend.getValue(Friend.class);
                    if(user_id.equals(friend.getUser_id()) && friend.isFriend_status()){
                        setDataListFrind(friend.getFriend_link());

                    }
                }
                friend_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc lay du lieu friend tu firebase*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc set lay ra thong tin friend va set vao lst_friend*/
    private void setDataListFrind(final String friend_link){
        database_reference = firebase_database.getReference("Users");
        database_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users user = snapshot.getValue(Users.class);
                    if(friend_link.equals(user.getUser_id())){
                        lst_friend.add(new ItemFriend(user.getUser_full_name(), user.getUser_avatar(), user.getUser_address(),user.getUser_id(), dataSnapshot.getKey()));
                        break;
                    }
                }
                friend_adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    /*phuong thuc set lay ra thong tin friend va set vao lst_friend*/


    //==------------------------------------------------------------------------------------------------------------------------------
    // show  dialog
    //--------------------------------------------------------------------------------------------------
    /*show danh sach ban be khi bam vao nut tag ban be*/
    private void showDialogListFriend(){
        final Dialog dialog =  new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_tag_friend);


        Button  btn_cancel;
        RecyclerView recycler_list_friend;

        recycler_list_friend = dialog.findViewById(R.id.recycler_list_friend);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);
        recycler_list_friend.setNestedScrollingEnabled(false);

        lst_friend = new ArrayList<>();
        getFrendReferent();

        friend_adapter = new ListFriendAdapter(PostUpPersonalActivity.this, lst_friend, false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PostUpPersonalActivity.this);
        recycler_list_friend.setLayoutManager(layoutManager);
        recycler_list_friend.setAdapter(friend_adapter);
        friend_adapter.notifyDataSetChanged();

        // set su kien khi bam vao reclerviwe

        recycler_list_friend.addOnItemTouchListener(new RecyclerTouchListener(PostUpPersonalActivity.this, recycler_list_friend, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                txt_tag += lst_friend.get(lst_friend.size() - position -1).getFull_name() + ", ";
                list_id_tag_friend.add(lst_friend.get(lst_friend.size() - position -1).getFriend_id());
                txt_tag_friend.setText(txt_tag);
                txt_title_tag_friend.setVisibility(View.VISIBLE);
                txt_tag_friend.setVisibility(View.VISIBLE);
                dialog.cancel();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();
    }
    /*show danh sach ban be khi bam vao nut tag ban be*/




    //--------------------------------------------------------------------------------------------------
    /*show dialog khi bam vao nut set che do xem bai*/
    private void showDialogSetPostType(){
        final Dialog dialog =  new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_set_type_personal_post_up);

        final RadioButton radio_btn_post_type_public, radio_btn_post_type_protected, radio_btn_post_type_private;
        radio_btn_post_type_public = dialog.findViewById(R.id.radio_btn_post_type_public);
        radio_btn_post_type_protected = dialog.findViewById(R.id.radio_btn_post_type_protected);
        radio_btn_post_type_private = dialog.findViewById(R.id.radio_btn_post_type_private);

        Button btn_action , btn_cancel;
        btn_action = dialog.findViewById(R.id.btn_action);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);

        radio_btn_post_type_protected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_btn_post_type_public.setChecked(false);
            }
        });
        radio_btn_post_type_private.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radio_btn_post_type_public.setChecked(false);
            }
        });

        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

    }
    /*show dialog khi bam vao nut set che do xem bai*/

    //--------------------------------------------------------------------------------------------------

    /*show dialog khi dang bai that bai*/
    private void showDialogPostUpFail() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_fail);

        TextView txt_title_message_fail, txt_message_fail;
        Button btn_action, btn_again;

        txt_title_message_fail = dialog.findViewById(R.id.txt_title_message_fail);
        txt_message_fail = dialog.findViewById(R.id.txt_message_fail);
        btn_action = dialog.findViewById(R.id.btn_action);
        btn_again = dialog.findViewById(R.id.btn_again);

        txt_message_fail.setText("Có vẻ như đã có lỗi xảy ra trong quá trình đăng bài. " +
                "Bạn hãy chắc chắn răng bài đăng của mình đã đầy đủ theo yêu cầu.");
        txt_title_message_fail.setText("Không thể đăng bài");
        btn_action.setText("OK");
        btn_again.setText("Hủy bài");


        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btn_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPostScreen();
            }
        });
        dialog.show();

    }
    /*show dialog khi dang bai that bai*/


    //--------------------------------------------------------------------------------------------------
    /*show dialog khi bam vao nut back*/
    private void showDialogWhenClickButtonBack() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_fail);

        TextView txt_title_message_fail, txt_message_fail;
        Button btn_action, btn_again;

        txt_title_message_fail = dialog.findViewById(R.id.txt_title_message_fail);
        txt_message_fail = dialog.findViewById(R.id.txt_message_fail);
        btn_action = dialog.findViewById(R.id.btn_action);
        btn_again = dialog.findViewById(R.id.btn_again);

        txt_message_fail.setText("Bạn đã gần xong bài đăng. Những dữ liệu bạn vừa nhập sẽ mất, bạn có chắc chắn muốn thoát không.");
        txt_title_message_fail.setText("Ồ! Có vấn đề gì vậy.");
        btn_action.setText("Vẫn thoát");
        btn_again.setText("Hủy");

        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                finish();
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
    /*show dialog khi bam vao nut back*/

    //--------------------------------------------------------------------------------------------------
    /*ke thua phuong thuc onBackPressed*/
    @Override
    public void onBackPressed() {
        showDialogWhenClickButtonBack();
    }
    /*ke thua phuong thuc onBackPressed*/

    //--------------------------------------------------------------------------------------------------
    /*Show dialog khi dang bai thanh cong*/
    private void showDialogPostUpSuccess() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_success);

        Button btn_success;
        TextView txt_title_message_success, txt_content_message_success;

        btn_success = dialog.findViewById(R.id.btn_success);
        txt_content_message_success = dialog.findViewById(R.id.txt_content_message_success);

        txt_content_message_success.setText("Thành công! Bạn có thể xem bài viết ngay bây giờ.");
        btn_success.setText("OK");

        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                gotoPostScreen();
                finish();
            }
        });
        dialog.show();
    }
    /*Show dialog khi dang bai thanh cong*/


    //----------------------------------------------------------------------------------------------------------------------------
    // cac phuong thuc chuyen trang
    //--------------------------------------------------------------------------------------------------
    /*chuyen trang den trang post*/
    private void gotoPostScreen() {
        Intent intent = new Intent(PostUpPersonalActivity.this, MainActivity.class);
        startActivity(intent);
    }
    /*chuyen trang den trang post*/


    //------------------------------------------------------------------------------------------------------------------------------
    // code su ly phan su dung anh va camera

    //--------------------------------------------------------------------------------------------------
    /*phuong thuc set hinh anh vao recycler*/

    private void setImageRecyclerView() {
        image_post_up_adapter = new ImagePostUpAdapter(lst_bitmaps, PostUpPersonalActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PostUpPersonalActivity.this);
        recycler_image_personal_post.setLayoutManager(layoutManager);
        recycler_image_personal_post.setAdapter(image_post_up_adapter);
        image_post_up_adapter.notifyDataSetChanged();
    }
    /*phuong thuc set hinh anh vao recycler*/


    //--------------------------------------------------------------------------------------------------
    /*phuong thuc su dung camera*/
    public void hadleGetImageFromCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 9999);
        } else {
            openCamera();
        }
    }


    /*phuong thuc mo camera*/
    private void openCamera() {
        Intent take_picture_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (take_picture_intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(take_picture_intent, 9999);
        }

    }


    //--------------------------------------------------------------------------------------------------

    /*phuong thuc doc du lieu tu the nho*/
    private void handleGetImageFromGallery() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 9696);
        } else {
            openGallery();
        }
    }

    /*mo thu muc doc the tu bo nho*/
    private void openGallery() {
        Intent intent = new Intent();
        // show only image, no video or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // always  show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Pick an image"), 9696);
    }

    public Bitmap getBitmapFromUri(Context context_adapter, Uri uri_file) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeUri(context_adapter, uri_file);
        } catch (IOException ex) {
            bitmap = BitmapFactory.decodeResource(context_adapter.getResources(), R.mipmap.ic_launcher);
            ex.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeUri(Context context, Uri selected_image) throws FileNotFoundException {
        // Decode image size
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selected_image), null, options);

        // the new size we want to scale to
        final int REQUTRED_SIZE = 140;

        // Find the correct scale values. It should be the power of 2
        int width_tmp = options.outWidth, height_tmp = options.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUTRED_SIZE || height_tmp / 2 < REQUTRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options options_a = new BitmapFactory.Options();
        options_a.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selected_image), null, options_a);

    }


    //--------------------------------------------------------------------------------------------------
    /*phuong thuc kiem tra cap quyen su dung phan cung cua may*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 9999) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                showToast("Camera không thể khởi động!");
            }
        } else if (requestCode == 9696) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // now user should  be able to open gallery
                openGallery();
            } else {
                showToast("Không thể đọc dữ liệu từ thẻ nhớ!");
            }
        }
    }


    //--------------------------------------------------------------------------------------------------

    /*phuong thuc set hinh anh tu ben ngoai vao bai dang*/
    /*ke thua lai phuong thuc onActivityResult*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // lay hinh anh tu activity bat len khi mo camera hoac doc the nho
        if (requestCode == 9999 && resultCode == RESULT_OK) {

            // phuong thuc add uri cua anh vao lst_image_uri
            lst_image_uri.add(data.getData());


            Bundle extras = data.getExtras();
            image_bitmap = (Bitmap) extras.get("data");

            // phuong thuc add anh vao lst_bitmap
            lst_bitmaps.add(image_bitmap);
            //recycler_image_personal_post.setAdapter(image_post_up_adapter);
            image_post_up_adapter.notifyDataSetChanged();
        }
        if (requestCode == 9696 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            image_bitmap = getBitmapFromUri(PostUpPersonalActivity.this, uri);

            if (position_lst_image >= 0) {
                // phuong thuc add uri cua anh vao lst_image_uri
                lst_image_uri.add(position_lst_image, data.getData());
                lst_image_uri.remove(position_lst_image + 1);

                // phuong thuc add anh vao lst_bitmap
                lst_bitmaps.add(position_lst_image, image_bitmap);
                lst_bitmaps.remove(position_lst_image + 1);
                //recycler_image_personal_post.setAdapter(image_post_up_adapter);
                image_post_up_adapter.notifyDataSetChanged();
                position_lst_image = -1;
            } else {
                // phuong thuc add uri cua anh vao lst_image_uri
                lst_image_uri.add(data.getData());
                // phuong thuc add anh vao lst_bitmap
                lst_bitmaps.add(image_bitmap);
                //recycler_image_personal_post.setAdapter(image_post_up_adapter);
                image_post_up_adapter.notifyDataSetChanged();
            }

        }

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                txt_location_post.setText(place.getAddress().toString());
                txt_location_title.setVisibility(View.VISIBLE);
                txt_location_post.setVisibility(View.VISIBLE);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("error", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }


    }


    //--------------------------------------------------------------------------------------------------
    /*Phuong thuc set su kien click cho recycler*/
    public void setOnClickForRecyclerView() {

        recycler_image_personal_post.addOnItemTouchListener(new RecyclerTouchListener(PostUpPersonalActivity.this, recycler_image_personal_post, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                showContextMenu(position);
            }
        }));
    }


    //--------------------------------------------------------------------------------------------------
    /*phuong thuc show context menu*/
    private void showContextMenu(final int position) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_context_menu);

        Button btn_change_image_post_up, btn_delete_image_post_up, btn_cancel_dialog, btn_add_decription_image;

        btn_change_image_post_up = dialog.findViewById(R.id.btn_change_image_post_up);
        btn_delete_image_post_up = dialog.findViewById(R.id.btn_delete_image_post_up);
        btn_cancel_dialog = dialog.findViewById(R.id.btn_cancel_dialog);
        btn_add_decription_image = dialog.findViewById(R.id.btn_add_decription_image);
        btn_add_decription_image.setVisibility(View.GONE);


        btn_cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        btn_change_image_post_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_lst_image = position;
                handleGetImageFromGallery();
                dialog.cancel();
            }
        });

        btn_delete_image_post_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lst_bitmaps.remove(position);
                recycler_image_personal_post.setAdapter(image_post_up_adapter);
                image_post_up_adapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /*phuong thuc show context menu*/


    //--------------------------------------------------------------------------------------------------------------------------
    // cac phuong thuc up du lieu len firebase

    //--------------------------------------------------------------------------------------------------
    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        return "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)
                + " - " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }


    String key_post;

    //----------------------------------------------------------------------------------------------
    /*Phuong thuc ghi du lieu vao bang post*/
    private void writePostData() {

        String post_content = edt_content_post_personal_up.getText().toString().trim();

        if (TextUtils.isEmpty(post_content)) {
            edt_content_post_personal_up.setError("Hãy viết gì đó cho bài đăng này!");
            return;
        }

        progress_dialog = new ProgressDialog(this);
        progress_dialog.setTitle("Đang tải bài đăng lên...");
        progress_dialog.show();

        // khai bao thu vien thao tac voi fire base
        database_reference = FirebaseDatabase.getInstance().getReference();

        // lay key ngau nhien va lay user_id
        key_post = database_reference.child("Post").push().getKey();

        // day du lieu vao object Post


        Post post = new Post(key_post,
                "",
                post_content,
                getDateTime(),
                "",
                txt_location_post.getText().toString().trim(),
                "",
                "",
                "",
                "",
                "",
                "",
                3,
                "",
                "",
                false,
                true,
                user_id
        );


        // chuyen du lieu sang dang Map
        Map<String, Object> post_values = post.toMap();
        Map<String, Object> child_add_post = new HashMap<>();

        // tao thu muc luu du lieu
        child_add_post.put("/Post/" + key_post, post_values);

        // day du lieu len
        database_reference.updateChildren(child_add_post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //thanh cong
                uploadDataToTag();
                upLoadImageStorage();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //that bai
                showToast("Up bai that bai");
            }
        });

    }
    /*Phuong thuc ghi du lieu vao bang post*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc ghi du lieu vao bang tag*/
    private void uploadDataToTag(){
        // khai bao firebase database
        database_reference = FirebaseDatabase.getInstance().getReference();

        for(String friend_id : list_id_tag_friend){
            // lay key  cho upload
            String key = database_reference.child("Tag").push().getKey();

            // khai bao thuc the tag
            Tag tag = new Tag(key, true, key_post, friend_id);

            //
            Map<String, Object> tag_values = tag.toMap();
            Map<String, Object> child_update = new HashMap<>();

            //tao thu muc luu
            child_update.put("/Tag/"+key, tag_values);

            // up gia tri len
            database_reference.updateChildren(child_update).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.e("Tag", "Thanh cong");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
        }

    }
    /*phuong thuc ghi du lieu vao bang tag*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc day anh len firebaseStorage*/

    private void upLoadImageStorage() {
        StorageReference storage_reference = FirebaseStorage.getInstance().getReference();
        if (lst_image_uri.size() > 0) {
            for (int i = 0; i < lst_image_uri.size(); i++) {
                if (lst_image_uri.get(i) != null) {

                    final int position = i;
                    // tham chieu den file can luu tren cloud
                    final StorageReference file_image = storage_reference.child("image_post").child(UUID.randomUUID().toString());

                    // thuc hien upload file
                    file_image.putFile(lst_image_uri.get(i)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            getDownloadIamgeUri(file_image, position);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showDialogPostUpFail();
                            return;
                        }
                    });
                }
            }
        }
        //closeProgressDialog();
        progress_dialog.dismiss();
        showDialogPostUpSuccess();
    }
    /*phuong thuc day anh len firebaseStorage*/


    //----------------------------------------------------------------------------------------------
    /*get uri cho image tu file base storage*/
    private void getDownloadIamgeUri(StorageReference file_url, final int position) {

        file_url.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                writeImageData(uri.toString(), position);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showDialogPostUpFail();
                return;
            }
        });


    }
    /*get uri cho image tu file base storage*/


    //----------------------------------------------------------------------------------------------
    /*phuong thuc ghi du lieu vao object image*/
    private void writeImageData(String image_link, int position) {

        database_reference = FirebaseDatabase.getInstance().getReference();

        // lay key_image
        final String key_image = database_reference.child("Image").push().getKey();

        // day du lieu vao object Image
        Image image = new Image(key_image,
                image_link,
                "khong co",
                getDateTime(),
                key_post,
                user_id);

        //chuyen du lieu sang kieu map
        Map<String, Object> image_values = image.toMap();
        Map<String, Object> child_add_image = new HashMap<>();

        // tao thu muc luu du lieu
        child_add_image.put("/Image/" + key_image, image_values);
        database_reference.updateChildren(child_add_image).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showDialogPostUpFail();
                Log.e("Loi ", "Khong the dua du lieu len firebase Image");
                return;
            }
        });
    }
    /*phuong thuc ghi du lieu vao object image*/


}
