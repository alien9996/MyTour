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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.adapter.ImagePostUpAdapter;
import com.example.dell.mytour.event.RecyclerTouchListener;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Post;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.MainActivity;
import com.example.dell.mytour.uis.activity.PersonalPageActivity;
import com.example.dell.mytour.uis.activity.register_activity.AddressAndBirthdayRegisterActivity;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
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

public class PostUpContentActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ActionBar action_bar;

    private CircleImageView img_avatar_user_post;

    // khai bao cac bien dung cho google Place API
    private GoogleMap mMap;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private int location_style = 1;

    // khai bao cac bien button
    private Button btn_up_post, btn_add_description_post_up,
            btn_add_info_for_advertisement_post, btn_use_camera, btn_add_image_post_up,
            bnt_add_image, btn_add_location_start_post_up, btn_add_location_stop_post_up;

    //khai bao cac bien text view
    private TextView txt_description_post_up, txt_title_advertisement, txt_time_service,
            txt_time_service_start, txt_transport_service, txt_old_price, txt_new_price,
            txt_location_stop_post_up, txt_location_start_post_up;

    //khai bao cac edit text
    private EditText edt_title_post_up, edt_content_post_up;


    // khai bao cac bien danh sach
    private LinearLayout linear_layout_img_post_up;
    private RecyclerView recycler_image;
    private Bitmap image_bitmap = null;
    private ArrayList<Bitmap> lst_bitmaps;
    private ImagePostUpAdapter image_post_up_adapter;

    private int post_type = 2;
    private int position_lst_image = -1;
    public static final int REQUEST_CODE = 1;
    public static final int RESULT_CODE = 2;
    String[] list_str;
    String description, location_start, location_stop;
    String[] lst_image_description = new String[]{"Hình ảnh","Hình ảnh", "Hình ảnh", "Hình ảnh",
            "Hình ảnh", "Hình ảnh", "Hình ảnh", "Hình ảnh","Hình ảnh", "Hình ảnh"};

    // thu vien thao tac voi firebase
    private DatabaseReference database_reference = null;
    private ProgressDialog progress_dialog;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();


    // thu vien thao tac voi firebase Storage
    private StorageReference storage_reference;
    ArrayList<Uri> lst_image_uri = null;


    @Override
    protected int injectLayout() {
        return R.layout.activity_post_up_content;
    }

    @Override
    protected void injectView() {


        // phan khai bao view

        img_avatar_user_post = findViewById(R.id.img_avatar_user_post);
        // khai bao cac button view
        btn_up_post = findViewById(R.id.btn_up_post);
        //btn_add_location_post_up = findViewById(R.id.btn_add_location_post_up);
        btn_add_description_post_up = findViewById(R.id.btn_add_description_post_up);
        btn_add_info_for_advertisement_post = findViewById(R.id.btn_add_info_for_advertisement_post);
        btn_use_camera = findViewById(R.id.btn_use_camera);
        btn_add_image_post_up = findViewById(R.id.btn_add_image_post_up);
        bnt_add_image = findViewById(R.id.bnt_add_image);
        btn_add_location_start_post_up = findViewById(R.id.btn_add_location_start_post_up);
        btn_add_location_stop_post_up = findViewById(R.id.btn_add_location_stop_post_up);

        // khai bao cac text view
        txt_description_post_up = findViewById(R.id.txt_description_post_up);
        txt_title_advertisement = findViewById(R.id.txt_title_advertisement);
        txt_time_service = findViewById(R.id.txt_time_service);
        txt_time_service_start = findViewById(R.id.txt_time_service_start);
        txt_transport_service = findViewById(R.id.txt_transport_service);
        txt_old_price = findViewById(R.id.txt_old_price);
        txt_new_price = findViewById(R.id.txt_new_price);
        txt_location_stop_post_up = findViewById(R.id.txt_location_stop_post_up);
        txt_location_start_post_up = findViewById(R.id.txt_location_start_post_up);

        location_start = "Không có";
        location_stop = "Không có";
        description = "Không có";
        //getUserAva
        getCurrentUser(currentUser.getUid());

        // lay style cua bai dang
        Intent intent_post_type = getIntent();
        post_type = Integer.parseInt(intent_post_type.getStringExtra("post_style"));
        showOrHideButtonAccordingPostType();
        showDialogSupport();

        // phan khoi tao thanh toolbar
        toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        action_bar = getSupportActionBar();
        action_bar.setDisplayHomeAsUpEnabled(true);
        if (post_type == 0) {
            action_bar.setTitle("Bài quảng cáo");
        }
        if (post_type == 1) {
            action_bar.setTitle("Bài chia sẻ kinh nghiệm");
        }
        if (post_type == 2) {
            action_bar.setTitle("Bài giới thiệu đánh giá");
        }


        // khai bao cac edit text
        edt_title_post_up = findViewById(R.id.edt_title_post_up);
        edt_content_post_up = findViewById(R.id.edt_content_post_up);

        // khai bao linear_layout
        linear_layout_img_post_up = findViewById(R.id.linear_layout_img_post_up);

        // khoi tao recycler view va array bitmap
        recycler_image = findViewById(R.id.recycler_image);
        lst_bitmaps = new ArrayList<>();
        recycler_image.setNestedScrollingEnabled(false);
        setImageRecyclerView();

        // khoi tao ArrayList luu cac uri cua anh dang len
        lst_image_uri = new ArrayList<>();


        // set recycler theo chieu ngang

        recycler_image.setLayoutManager(new LinearLayoutManager(recycler_image.getContext(),
                LinearLayoutManager.HORIZONTAL, false));


        // phan set onclick cho button
        btn_up_post.setOnClickListener(this);
        //btn_add_location_post_up.setOnClickListener(this);
        btn_add_description_post_up.setOnClickListener(this);
        btn_add_info_for_advertisement_post.setOnClickListener(this);
        btn_use_camera.setOnClickListener(this);
        btn_add_image_post_up.setOnClickListener(this);
        bnt_add_image.setOnClickListener(this);
        btn_add_location_stop_post_up.setOnClickListener(this);
        btn_add_location_start_post_up.setOnClickListener(this);

        setOnClickForRecyclerView();

    }

    @Override
    protected void injectVariables() {

    }

    //===========Get User ava============
    public void getCurrentUser(final String Uid) {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("Users");


        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Users users = data.getValue(Users.class);
                    if (users.getUser_id().equals(Uid)) {
                        if (!users.getUser_avatar().equals("") && users.getUser_avatar().startsWith("http")){
                            Glide.with(PostUpContentActivity.this.getApplicationContext()).load(users.getUser_avatar()).into(img_avatar_user_post);
                        }
                        else {
                            img_avatar_user_post.setImageResource(R.drawable.ic_friend_black_24dp);
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


    //----------------------------------------------------------------------------------------------
    /*ke thua lai phuong thuc click vao item tren thanh toolBar*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            showDialogWhenClickButtonBack();
        }
        return super.onOptionsItemSelected(item);
    }


    /*phuong thuc onclick*/
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_up_post:
                writePostData();
                break;
            case R.id.btn_add_location_start_post_up:
                location_style = 1;
                gotoGooglePlaceAPI();
                break;
            case R.id.btn_add_location_stop_post_up:
                location_style = 2;
                gotoGooglePlaceAPI();
                break;
            case R.id.btn_add_description_post_up:
                showDialogAddDescription();
                break;
            case R.id.btn_add_info_for_advertisement_post:
                Intent intent = new Intent(PostUpContentActivity.this, PostUpAdvertisementDetailsActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.btn_use_camera:
                linear_layout_img_post_up.setVisibility(View.VISIBLE);
                hadleGetImageFromCamera();
                break;
            case R.id.btn_add_image_post_up:
                handleGetImageFromGallery();
                linear_layout_img_post_up.setVisibility(View.VISIBLE);
                break;
            case R.id.bnt_add_image:
                handleGetImageFromGallery();
                linear_layout_img_post_up.setVisibility(View.VISIBLE);
                break;
        }
    }
    /*phuong thuc onclick*/

    //--------------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    /*phuong thuc show place google API*/
    private void gotoGooglePlaceAPI(){
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .build(PostUpContentActivity.this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
        } catch (GooglePlayServicesNotAvailableException e) {
        }
    }
    /*phuong thuc show place google API*/


    //--------------------------------------------------------------------------------------------------
    /*phuong thuc set hinh anh vao recycler*/

    private void setImageRecyclerView() {
        image_post_up_adapter = new ImagePostUpAdapter(lst_bitmaps, PostUpContentActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(PostUpContentActivity.this);
        recycler_image.setLayoutManager(layoutManager);
        recycler_image.setAdapter(image_post_up_adapter);
        image_post_up_adapter.notifyDataSetChanged();
    }
    /*phuong thuc set hinh anh vao recycler*/


    //--------------------------------------------------------------------------------------------------
    /*phuong thuc an hien button tuy theo loai bai dang*/
    private void showOrHideButtonAccordingPostType() {
        if (post_type == 0) {
            btn_add_info_for_advertisement_post.setVisibility(View.VISIBLE);
        }
    }

    /*phuong thuc an hien button tuy theo loai bai dang*/


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
                //gotoPostScreen();
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
    /*show dialog khi dang bai that bai*/


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


    //--------------------------------------------------------------------------------------------------
    /*show dialog huong dan khi moi bat dau vao trang*/
    private void showDialogSupport() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_success);

        Button btn_success;
        TextView txt_title_message_success, txt_content_message_success;

        btn_success = dialog.findViewById(R.id.btn_success);
        txt_content_message_success = dialog.findViewById(R.id.txt_content_message_success);
        txt_title_message_success = dialog.findViewById(R.id.txt_title_message_success);

        txt_title_message_success.setText("Gợi ý");
        txt_content_message_success.setText("Để bài đăng của bạn có định dạng như ý hãy chèn thẻ <img/> mỗi khi muốn " +
                "thêm hình ảnh vào bài viết và khi thêm ảnh hãy thêm theo đúng thứ tự các thẻ ảnh như trên phần nội dung.");
        btn_success.setText("Đã hiểu");

        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }
    /*show dialog goi y khi moi bat dau vao trang*/



    //--------------------------------------------------------------------------------------------------
    /*show dialog khi bam vao nut them mo ta*/
    private void showDialogAddDescription() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_add_description_post_up);

        final EditText edt_description_post_up;
        Button btn_action, btn_cancel;

        edt_description_post_up = dialog.findViewById(R.id.edt_description_post_up);
        btn_action = dialog.findViewById(R.id.btn_action);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = edt_description_post_up.getText().toString().trim();

                if (TextUtils.isEmpty(description)) {
                    edt_description_post_up.setError("Bạn chưa điền nội dung.");
                } else {
                    txt_description_post_up.setText("Mô tả: " + description);
                    txt_description_post_up.setVisibility(View.VISIBLE);
                    dialog.cancel();
                }

            }
        });
        dialog.show();
    }
    /*show dialog khi bam vao nut them mo ta*/

    //--------------------------------------------------------------------------------------------------
    /*chuyen trang den trang post*/
    private void gotoPostScreen() {
        Intent intent = new Intent(PostUpContentActivity.this, MainActivity.class);
        startActivity(intent);
    }
    /*chuyen trang den trang post*/


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
            recycler_image.setAdapter(image_post_up_adapter);
            image_post_up_adapter.notifyDataSetChanged();
        }
        if (requestCode == 9696 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            image_bitmap = getBitmapFromUri(PostUpContentActivity.this, uri);

            if (position_lst_image >= 0) {
                // phuong thuc add uri cua anh vao lst_image_uri
                lst_image_uri.add(position_lst_image, data.getData());
                lst_image_uri.remove(position_lst_image + 1);

                // phuong thuc add anh vao lst_bitmap
                lst_bitmaps.add(position_lst_image, image_bitmap);
                lst_bitmaps.remove(position_lst_image + 1);
                recycler_image.setAdapter(image_post_up_adapter);
                image_post_up_adapter.notifyDataSetChanged();
                position_lst_image = -1;
            } else {
                // phuong thuc add uri cua anh vao lst_image_uri
                lst_image_uri.add(data.getData());
                // phuong thuc add anh vao lst_bitmap
                lst_bitmaps.add(image_bitmap);
                recycler_image.setAdapter(image_post_up_adapter);
                image_post_up_adapter.notifyDataSetChanged();
            }

        }

        // lay du lieu tu activity PostUpAdvertisementDetails

        if (requestCode == REQUEST_CODE) {
            switch (resultCode) {
                case RESULT_CODE:
                    list_str = data.getStringArrayExtra("data_result");

                    //set du lieu cho các ô text
                    txt_time_service.setText("Thời gian: " + list_str[0]);
                    txt_time_service_start.setText("Thời gian xuất phát: " + list_str[1]);
                    txt_transport_service.setText("Phương tiện: " + list_str[2]);
                    txt_old_price.setText("Giá cũ: " + list_str[3]);
                    txt_new_price.setText("Giá mới: " + list_str[4]);
                    txt_title_advertisement.setText("Tiêu đề quảng cáo: " + list_str[5]);

                    //set thuoc tinh hien cac o text
                    txt_time_service.setVisibility(View.VISIBLE);
                    txt_time_service_start.setVisibility(View.VISIBLE);
                    txt_transport_service.setVisibility(View.VISIBLE);
                    txt_old_price.setVisibility(View.VISIBLE);
                    txt_new_price.setVisibility(View.VISIBLE);
                    txt_title_advertisement.setVisibility(View.VISIBLE);

                    break;
            }
        }

        //lay du lieu tu google place API
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                if(location_style == 1) {
                    location_start = place.getAddress().toString();
                    txt_location_start_post_up.setText("Vị trí xuất phát: " + location_start);
                    txt_location_start_post_up.setVisibility(View.VISIBLE);
                } else {
                    location_stop = place.getAddress().toString();
                    txt_location_stop_post_up.setText("Vị trí du lịch: " + location_stop);
                    txt_location_stop_post_up.setVisibility(View.VISIBLE);
                }

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.


            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    //--------------------------------------------------------------------------------------------------
    /*Phuong thuc set su kien click cho recycler*/
    public void setOnClickForRecyclerView() {

        recycler_image.addOnItemTouchListener(new RecyclerTouchListener(PostUpContentActivity.this, recycler_image, new RecyclerTouchListener.ClickListener() {
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


        btn_add_decription_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position_lst_image = position;
                showDialogAddImageDescription();
                dialog.cancel();
            }
        });
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
                recycler_image.setAdapter(image_post_up_adapter);
                image_post_up_adapter.notifyDataSetChanged();
                dialog.cancel();
            }
        });

        dialog.show();
    }

    /*phuong thuc show context menu*/


    //--------------------------------------------------------------------------------------------------
    /*show dialog them mo ta cho anh*/
    private void showDialogAddImageDescription() {


        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_add_description_post_up);

        final EditText edt_description_image_post_up;
        Button btn_action, btn_cancel;

        edt_description_image_post_up = dialog.findViewById(R.id.edt_description_post_up);
        btn_action = dialog.findViewById(R.id.btn_action);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        btn_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                description = edt_description_image_post_up.getText().toString().trim();

                if (TextUtils.isEmpty(description)) {
                    edt_description_image_post_up.setError("Bạn chưa điền nội dung.");
                } else {
                    if (position_lst_image >= 0) {
                        lst_image_description[position_lst_image] = edt_description_image_post_up.getText().toString().trim();
                        position_lst_image = -1;
                    }
                    dialog.cancel();
                }
            }
        });
        dialog.show();
    }
    /*show dialog them mo ta cho anh*/


    //--------------------------------------------------------------------------------------------------
    private String getDateTime() {
        Calendar calendar = Calendar.getInstance();
        return "" + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND)
                + " - " + calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR);
    }

    //-----------------------------------------------------------------------------------------------------------------------------
    // Phần code đẩy dữ liệu lên firebase

    String key_post, user_id;

    //----------------------------------------------------------------------------------------------
    /*Phuong thuc ghi du lieu vao bang post*/
    private void writePostData() {
        String post_tile = edt_title_post_up.getText().toString().trim();
        String post_content = edt_content_post_up.getText().toString().trim();

        if (TextUtils.isEmpty(post_tile)) {
            edt_title_post_up.setError("Không thể bỏ trống tiêu đề!");
            return;
        }
        if (TextUtils.isEmpty(post_content)) {
            edt_content_post_up.setError("Không thể bỏ trống nội dung!");
            return;
        }

        progress_dialog = new ProgressDialog(this);
        progress_dialog.setTitle("Đang tải bài đăng lên...");
        progress_dialog.show();
        //showProgressDialog("Đang tải bài đăng lên...");
        // khai bao thu vien thao tac voi fire base
        database_reference = FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // lay key ngau nhien va lay user_id
        key_post = database_reference.child("Post").push().getKey();
        user_id = user.getUid().toString();

        // day du lieu vao object Post
        Post post = null;

        if (post_type == 0) {
            if (list_str != null || location_start.equals("Không có") || location_stop.equals("Không có")) {
                post = new Post(key_post,
                        post_tile,
                        post_content,
                        getDateTime(),
                        description,
                        "",
                        location_stop,
                        location_start,
                        list_str[1],
                        list_str[0],
                        list_str[2],
                        list_str[5],
                        post_type,
                        list_str[3],
                        list_str[4],
                        true,
                        true,
                        user_id);
            } else {
                showToast("Đây là bài quảng cáo yêu cầu bạn nhập đầy đủ thông tin. Xin cám ơn! ");
                progress_dialog.dismiss();
                return;
            }


        } else {
            post = new Post(key_post,
                    post_tile,
                    post_content,
                    getDateTime(),
                    description,
                    "",
                    location_stop,
                    location_start,
                    "",
                    "",
                    "",
                    "",
                    post_type,
                    "",
                    "",
                    true,
                    true,
                    user_id
            );
        }


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
                lst_image_description[position],
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
















