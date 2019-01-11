package com.example.dell.mytour.uis.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dell.mytour.R;
import com.example.dell.mytour.model.model_base.Image;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditInfoUserActivity extends BaseActivity {


    private CircleImageView edit_info_ava;
    private TextView edit_info_change_ava;
    private EditText edit_info_name;
    private TextView txt_info_birthday;
    private EditText edit_info_email;
    private EditText edit_info_address;
    private EditText edit_info_des;
    private EditText edit_info_phone;
    private ProgressBar edit_info_progressBar;
    private Button edit_btn_info_name;
    private Button edit_btn_info_email;
    private Button edit_btn_info_address;
    private Button edit_btn_info_des;
    private Button edit_btn_info_phone;

    private boolean edit_linear;

    Bitmap imgAvaBitmap = null;

    private static final int REQUEST_CAPTURE = 111;
    private static final int REQUEST_GALLERY = 222;


    //to work with firebase realtime
    private DatabaseReference userReference;
    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    Uri uri;


    @Override
    protected void onStart() {
        super.onStart();
        edit_linear = findViewById(R.id.edit_linearlayout).requestFocus();
    }

    @Override
    protected int injectLayout() {
        return R.layout.activity_edit_info_user;
    }

    @Override
    protected void injectView() {

        edit_info_ava = findViewById(R.id.edit_info_ava);
        edit_info_change_ava = findViewById(R.id.edit_info_change_ava);
        edit_info_name = findViewById(R.id.edit_info_name);
        edit_btn_info_name = findViewById(R.id.edit_btn_info_name);
        txt_info_birthday = findViewById(R.id.edit_info_birthday);
        edit_info_email = findViewById(R.id.edit_info_email);
        edit_info_address = findViewById(R.id.edit_info_address);
        edit_info_phone = findViewById(R.id.edit_info_phone);
        edit_info_des = findViewById(R.id.edit_info_des);
        edit_info_progressBar = findViewById(R.id.edit_info_progressbar);
        edit_btn_info_address = findViewById(R.id.edit_btn_info_address);
        edit_btn_info_email = findViewById(R.id.edit_btn_info_email);
        edit_btn_info_phone = findViewById(R.id.edit_btn_info_phone);
        edit_btn_info_des = findViewById(R.id.edit_btn_info_des);


        //firebase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");


        getCurrentUser();
        //Button edit
        ButtonClick();


        //Change ava
        edit_info_change_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence[] items = {"Camera", "Library"};
                AlertDialog.Builder builder = new AlertDialog.Builder(EditInfoUserActivity.this);
                builder.setTitle("Chọn ảnh");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            //take photo
                            handleGetImageFromCamera();

                        } else {
                            //pick Image from library
                            handleGetImageFromLibrary();

                        }
                    }
                });
                builder.show();

            }
        });

        edit_info_ava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upLoadImageStorage();
            }
        });


    }


    @Override
    protected void injectVariables() {

    }


    public void ChangeDateOfBirth(View view) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditInfoUserActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                txt_info_birthday.setText(i2 + "/" + (i1 + 1) + "/" + i);
            }
        }, 1990, 1, 1);
        datePickerDialog.show();
    }

    //pick an image from gallery

    private void handleGetImageFromLibrary() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_GALLERY);
        } else {
            openGallery();
        }
    }

    public void openGallery() {
        Intent intent = new Intent();
        //show only img, no video or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Chon 1 ảnh"), REQUEST_GALLERY);
    }

    //Take a photo

    private void handleGetImageFromCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA}, REQUEST_CAPTURE);
        } else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAPTURE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera isn't available", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_GALLERY) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Can't not open Library", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            uri = data.getData();
            Bundle extras = data.getExtras();
            imgAvaBitmap = (Bitmap) extras.get("data");
            edit_info_ava.setImageBitmap(imgAvaBitmap);
        }
        if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
            uri = data.getData();
            imgAvaBitmap = getBitmapFromUri(EditInfoUserActivity.this, uri);
            edit_info_ava.setImageBitmap(imgAvaBitmap);
        }

    }

    public Bitmap getBitmapFromUri(Context context, Uri uriFile) {
        Bitmap bitmap = null;
        try {
            bitmap = decodeUri(context, uriFile);
        } catch (IOException e) {
            bitmap = BitmapFactory.decodeResource(context.getResources(),
                    R.mipmap.ic_launcher);
            e.printStackTrace();

        }
        return bitmap;
    }

    private static Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // the new size we want to scale
        final int REQUIRED_SIZE = 140;

        //find the correct scale value. It should power of 2
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        //DECODE WITH inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }

    //-------------onClick()---------------
    public void ButtonClick() {
        edit_btn_info_des.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_info_des.setEnabled(true);
                edit_info_des.requestFocus();
                int pos = edit_info_des.getText().toString().trim().length();
                edit_info_des.setSelection(pos);
                edit_info_des.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                            String info = edit_info_des.getText().toString().trim();
                            String note = "user_description";
                            if (!TextUtils.isEmpty(edit_info_name.getText())) {
                                showAlertDialog(note, info);
                            }
                            edit_info_des.setEnabled(false);
                        }
                        return true;
                    }
                });

            }
        });
        edit_btn_info_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_info_name.setEnabled(true);
                edit_info_name.requestFocus();
                int pos = edit_info_name.getText().toString().trim().length();
                edit_info_name.setSelection(pos);
                edit_info_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                            String name = edit_info_name.getText().toString().trim();
                            String note = "user_full_name";
                            if (!TextUtils.isEmpty(edit_info_name.getText())) {
                                showAlertDialog(note, name);
                            }
                            edit_info_name.setEnabled(false);
                        }
                        return true;
                    }
                });
            }
        });
        edit_btn_info_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_info_phone.setEnabled(true);
                edit_info_phone.requestFocus();
                int pos = edit_info_phone.getText().toString().trim().length();
                edit_info_phone.setSelection(pos);
                edit_info_phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                            String info = edit_info_phone.getText().toString().trim();
                            String note = "user_phone";
                            if (!TextUtils.isEmpty(edit_info_name.getText())) {
                                showAlertDialog(note, info);
                            }
                            edit_info_phone.setEnabled(false);
                        }
                        return true;
                    }
                });
            }
        });
        edit_btn_info_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_info_email.setEnabled(true);
                edit_info_email.requestFocus();
                int pos = edit_info_email.getText().toString().trim().length();
                edit_info_email.setSelection(pos);
                edit_info_email.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                            String info = edit_info_email.getText().toString().trim();
                            String note = "user_email";
                            if (!TextUtils.isEmpty(edit_info_name.getText())) {
                                showAlertDialog(note, info);
                            }
                            edit_info_email.setEnabled(false);
                        }
                        return true;
                    }
                });
            }
        });
        edit_btn_info_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit_info_address.setEnabled(true);
                edit_info_address.requestFocus();
                int pos = edit_info_address.getText().toString().trim().length();
                edit_info_address.setSelection(pos);
                edit_info_address.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        if ((keyEvent != null && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (i == EditorInfo.IME_ACTION_DONE)) {
                            String info = edit_info_address.getText().toString().trim();
                            String note = "user_address";
                            if (!TextUtils.isEmpty(edit_info_name.getText())) {
                                showAlertDialog(note, info);

                            }
                            edit_info_address.setEnabled(false);
                        }
                        return true;
                    }
                });
            }
        });
    }

    //----------Firebase-------------------

    public void getCurrentUser() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        userReference = firebaseDatabase.getReference("Users");
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        if (user != null) {
            //Toast.makeText(getContext(), " "+user.getUid(), Toast.LENGTH_SHORT).show();
            final Boolean[] hadUser = {false};
            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        Users users = data.getValue(Users.class);
                        //Toast.makeText(MainActivity.this, ""+users.getUser_id(), Toast.LENGTH_SHORT).show();
                        if (users.getUser_id().equals(user.getUid())) {

                            edit_info_name.setText(users.getUser_full_name());
                            edit_info_email.setText(users.getUser_email());
                            edit_info_phone.setText(users.getUser_phone());
                            edit_info_address.setText(users.getUser_address());
                            edit_info_des.setText(users.getUser_description());
                            txt_info_birthday.setText(users.getUser_birthday());

                            if (!users.getUser_avatar().equals("") && users.getUser_avatar().startsWith("http")){

                                Glide.with(EditInfoUserActivity.this).load(users.getUser_avatar()).into(edit_info_ava);
                            }

                            edit_info_progressBar.setVisibility(View.GONE);
                            // Toast.makeText(getContext(), ""+users.getUser_full_name(), Toast.LENGTH_SHORT).show();
                            hadUser[0] = true;
                            break;
                        }


                    }
                    if (!hadUser[0]) {
                        Toast.makeText(EditInfoUserActivity.this, "Lỗi không tìm thấy user", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(EditInfoUserActivity.this, "Không thể truy cập dữ liệu", Toast.LENGTH_SHORT).show();
                }
            });


        }

    }


    public void showAlertDialog(final String note, final String info) {
        // define AlertDialog
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Create title, Button
        alertDialog.setTitle("Thong bao");
        alertDialog.setMessage("Ban co muon sua thông tin này?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        ArrayList<String> listKeys = new ArrayList<>();
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            Users users = dataSnapshot1.getValue(Users.class);
                            //Toast.makeText(MainActivity.this, ""+users.getUser_id(), Toast.LENGTH_SHORT).show();
                            if (users.getUser_id().equals(user.getUid())) {
                                String userID = dataSnapshot1.getKey();
                                //Toast.makeText(EditInfoUserActivity.this, "" + userID, Toast.LENGTH_SHORT).show();
                                userReference.child(userID).child(note).setValue(info);
                                Toast.makeText(EditInfoUserActivity.this, "Luu thanh cong", Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getCurrentUser();
            }
        });

        //show
        alertDialog.show();
    }

    //-------Stored 1 Image ----------


    private String getDateTime(){
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("hh:mm:ss - dd/MM/yyyy");
        return ft.format(dNow);
    }

    private void upLoadImageStorage() {
        StorageReference storage_reference = FirebaseStorage.getInstance().getReference();
        if (uri != null) {

            // tham chieu den file can luu tren cloud
            final StorageReference file_image = storage_reference.child("image_post").child(UUID.randomUUID().toString());

            // thuc hien upload file
            file_image.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    getDownloadIamgeUri(file_image);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    showDialogPostUpFail();
                    return;
                }
            });
        }

        //closeProgressDialog();
//        progress_dialog.dismiss();
//        showDialogPostUpSuccess();
    }
    /*phuong thuc day anh len firebaseStorage*/


    //----------------------------------------------------------------------------------------------
    /*get uri cho image tu file base storage*/
    private void getDownloadIamgeUri(StorageReference file_url) {
        final DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference();
        file_url.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                writeImageData(uri.toString());
                setUserAva(uri.toString());
                Toast.makeText(EditInfoUserActivity.this, "Da thay doi ava", Toast.LENGTH_SHORT).show();
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
    private void writeImageData(String image_link) {

        DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference();

        // lay key_image
        final String key_image = database_reference.child("Image").push().getKey();

        // day du lieu vao object Image
        Image image = new Image(key_image,
                image_link,
                "aaa",
                getDateTime(),
                "",
                user.getUid());

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
    public void setUserAva(final String imgUrl){
        final DatabaseReference database_reference = FirebaseDatabase.getInstance().getReference("Users");
        database_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnap : dataSnapshot.getChildren()){
                    Users u = userSnap.getValue(Users.class);
                    if(u.getUser_id().equals(user.getUid())){
                        database_reference.child(userSnap.getKey()).child("user_avatar").setValue(imgUrl);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
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

            }
        });
        dialog.show();

    }

}


