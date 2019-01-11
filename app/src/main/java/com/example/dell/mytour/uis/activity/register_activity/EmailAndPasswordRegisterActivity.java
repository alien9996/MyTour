package com.example.dell.mytour.uis.activity.register_activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.common.EncodeMD5;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmailAndPasswordRegisterActivity extends BaseActivity implements View.OnClickListener, SearchView.OnQueryTextListener {

    private Button btn_next_email, btn_delete_email, btn_visilibity_password;
    private EditText edt_email_register, edt_password_register;
    private TextView txt_error;

    private String email_content, password_content;

    private DatabaseReference databse_reference;
    private FirebaseDatabase firebase_database;


    @Override
    protected int injectLayout() {
        return R.layout.activity_register_email_and_password;
    }

    @Override
    protected void injectView() {
        btn_next_email = findViewById(R.id.btn_next_email);
        btn_delete_email = findViewById(R.id.btn_delete_email);
        btn_visilibity_password = findViewById(R.id.btn_visilibity_password);

        edt_email_register = findViewById(R.id.edt_email_register);
        edt_password_register = findViewById(R.id.edt_password_register);
        txt_error = findViewById(R.id.txt_error);


        btn_next_email.setOnClickListener(this);
        btn_visilibity_password.setOnClickListener(this);
        btn_delete_email.setOnClickListener(this);

        edt_email_register.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    btn_delete_email.setVisibility(View.VISIBLE);
                } else {
                    btn_delete_email.setVisibility(View.GONE);
                }

            }
        });

        edt_password_register.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    btn_visilibity_password.setVisibility(View.VISIBLE);
                } else {
                    btn_visilibity_password.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void injectVariables() {

    }

    // phuong thuc onclick
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_email:

                // kiem tra gia tri nhap vao co trong hay khong
                email_content = edt_email_register.getText().toString().trim();
                password_content = edt_password_register.getText().toString().trim();

                if (validateForm()) {
                    getListUsers();
                }
                // goi phuong thuc tao tai khoan

                break;
            case R.id.btn_delete_email:
                edt_email_register.setText("");
                break;
            case R.id.btn_visilibity_password:
                edt_password_register.setText("");
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    // phuong thuc chuyen trang sang trang dang ki ten va so dien thoai
    private void gotoNameAndPhoneRegisterScreen() {

        Intent intent_next_email = new Intent(EmailAndPasswordRegisterActivity.this, NameAndPhoneRegisterActivity.class);
        intent_next_email.putExtra("email_register", email_content);
        intent_next_email.putExtra("pass_register", EncodeMD5.toMD5(password_content));

        startActivity(intent_next_email);
    }


    // phuong thuc lay ra danh sach cac user name de so sanh
    private void getListUsers() {

        firebase_database = FirebaseDatabase.getInstance();
        databse_reference = firebase_database.getReference("Users");
        if (databse_reference != null) {
            databse_reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean result = true;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Users user = snapshot.getValue(Users.class);

                        if (email_content.equals(user.getUser_email().toString())) {
                            txt_error.setVisibility(View.VISIBLE);
                            txt_error.setText("Email của bạn đã được sử dụng hoặc địa chỉ email không chính xác vui lòng sử dụng email khác");
                            result = false;
                            break;
                        }
                    }
                    if(result){
                        txt_error.setVisibility(View.INVISIBLE);
                        txt_error.setText("");
                        gotoNameAndPhoneRegisterScreen();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    // phuong thuc validate form
    private boolean validateForm() {
        String re_email = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|" +
                "(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|" +
                "(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        String re_password = "^[a-zA-Z0-9]{6,24}";

        if (TextUtils.isEmpty(email_content)) {
            edt_email_register.setError("Bạn không thể bỏ trống email");
            return false;
        }

        if (TextUtils.isEmpty(password_content)) {
            edt_password_register.setError("Mật khẩu không thể bỏ trống hoặc chứa kí tự đặc biệt");
            return false;
        }

        if (!email_content.matches(re_email)) {
            txt_error.setVisibility(View.VISIBLE);
            txt_error.setText("Email của bạn sai định dạng vui lòng kiểm tra lại");
            return false;
        }
        if(!password_content.matches(re_password)){
            txt_error.setVisibility(View.VISIBLE);
            txt_error.setText("Password của bạn nhỏ hơn hoặc vượt quá độ dài cho phép là 6 đến 24 hoặc có chứa kí tự đặc biệt.");
            return false;
        }
        return true;
    }

    // phuong thuc show dialog khi bam vao nut back
    private void showDialogAler(){
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_fail);

        Button btn_ok, btn_again;
        TextView txt_message_content, txt_message_title;

        txt_message_content = dialog.findViewById(R.id.txt_message_fail);
        txt_message_title = dialog.findViewById(R.id.txt_title_message_fail);
        btn_again = dialog.findViewById(R.id.btn_again);
        btn_ok = dialog.findViewById(R.id.btn_action);

        btn_again.setText("Hủy");
        btn_ok.setText("Vẫn thoát");

        txt_message_title.setText("Ồ! Có vấn đề gì vậy.");
        txt_message_content.setText("Có vẻ như bạn đã gần xong việc đăng ký 1 tài khoản mới. Bạn có chắc muốn hủy việc đăng ký này.");

        btn_ok.setOnClickListener(new View.OnClickListener() {
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

    // ke thua phuong thuc bam vao nut back


    @Override
    public void onBackPressed() {
        showDialogAler();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}
