package com.example.dell.mytour.uis.activity.register_activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.model.model_base.Users;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class DescriptionRegisterActivity extends BaseActivity implements View.OnClickListener{
    private RadioButton radio_btn_user_normal, radio_btn_user_member, radio_btn_user_enterprise;
    private EditText edt_description;
    private Button btn_register;

    private FirebaseAuth firebase_auth;

    public ProgressDialog progressDialog;

    @Override
    protected int injectLayout() {
        return R.layout.activity_register_description;
    }

    @Override
    protected void injectView() {
        radio_btn_user_normal = findViewById(R.id.radio_btn_user_normal);
        radio_btn_user_member = findViewById(R.id.radio_btn_user_member);
        radio_btn_user_enterprise = findViewById(R.id.radio_btn_user_enterprise);

        btn_register = findViewById(R.id.btn_register);
        edt_description = findViewById(R.id.edt_description);

        // init instance of FirebaseAuth
        firebase_auth = FirebaseAuth.getInstance();

        btn_register.setOnClickListener(this);
        radio_btn_user_normal.setOnClickListener(this);
        radio_btn_user_member.setOnClickListener(this);
        radio_btn_user_enterprise.setOnClickListener(this);
    }

    @Override
    protected void injectVariables() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.radio_btn_user_member:
                radio_btn_user_normal.setChecked(false);
                break;
            case R.id.radio_btn_user_enterprise:
                radio_btn_user_enterprise.setChecked(false);
                radio_btn_user_normal.setChecked(true);
                showDialogFail(
                        "Ồ! Không được.",
                        "Đây là cấp độ đặc biệt, bạn vui lòng liên hệ trực tiếp với chúng tôi để được hỗ trợ.",
                        "Liên hệ",
                        "Hủy");
                break;
            case R.id.btn_register:
                signUP();
                break;
        }
    }

    // phuong thuc tao tai khoan tren firebaseAuthencation
    private void signUP() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đăng ký...");
        progressDialog.show();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email_register");
        String password = intent.getStringExtra("password_register");
        firebase_auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            writeUser();
                        } else {
                            showDialogFail(
                                    "Ồ! Có lỗi xảy ra.",
                                    "Có vẻ như việc đăng kí tài khoản của bạn có trục trặc! Bạn có muốn tiếp tục hành dộng này.",
                                    "Thử lại sau",
                                    "Thử lại"
                            );
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    // ghi du lieu vao firebase
    private void writeUser(){
        // lay gia tri tu inten
        Intent intent = getIntent();
        String email = intent.getStringExtra("email_register");
        String password = intent.getStringExtra("password_register");
        String full_name = intent.getStringExtra("full_name_register");
        String phone = intent.getStringExtra("phone_register");
        String address = intent.getStringExtra("address_register");
        String birthday = intent.getStringExtra("birthday_register");

        FirebaseUser curren_user = firebase_auth.getCurrentUser();
        // lay gia tri nhap tu activty
        String description = edt_description.getText().toString().trim();
        int level = 2;
        if(radio_btn_user_normal.isChecked()){
            level = 2;
        } else {
            if (radio_btn_user_member.isChecked()){
                level = 1;
            }
        }

        // khoi tao doi tuong user
        Users users = new Users(
                curren_user.getUid(),
                "",
                password,
                full_name,
                email,
                birthday,
                address,
                phone,
                description,
                "",
                level,
                true);

        // day du lieu len firebase
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        String key = reference.child("Users").push().getKey();

        Map<String, Object> user_values = users.toMap();

        Map<String, Object> child_add = new HashMap<>();
        child_add.put("/Users/"+key, user_values);

        Task<Void> task = reference.updateChildren(child_add);

        if(task.isSuccessful() == false){
            progressDialog.dismiss();
            showDialogSuccess();

        } else {
            showDialogFail(
                    "Ồ! Có lỗi xảy ra.",
                    "Có vẻ như việc đăng kí tài khoản của bạn có trục trặc! Bạn có muốn tiếp tục hành dộng này.",
                    "Thử lại sau",
                    "Thử lại"
                    );
            progressDialog.dismiss();
        }
    }
    // ghi du lieu vao firebase

    /*phuong thuc show dialog khi dang ki thanh cong */
    private void showDialogSuccess(){
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_success);

        Button btn_success;
        TextView txt_title_message_success, txt_content_message_success;

        txt_title_message_success = dialog.findViewById(R.id.txt_title_message_success);
        txt_content_message_success = dialog.findViewById(R.id.txt_content_message_success);
        btn_success = dialog.findViewById(R.id.btn_success);

        txt_title_message_success.setText("Thành công");
        txt_content_message_success.setText("Việc đăng ký tài khoản của bạn đã thành công. Bạn có muốn sử dụng hệ thống ngay.");

        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                gotoHomeScreen();
            }
        });

        dialog.show();
    }
    /*phuong thuc show dialog khi dang ki thanh cong */

    /*phuong thuc show dialog khi dang ki that bai*/
    private void showDialogFail(String title, String message, String btn_name_left, String btn_name_right){
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_fail);

        Button btn_exit, btn_again;
        TextView txt_message_fail, txt_title_message_fail;

        btn_exit = dialog.findViewById(R.id.btn_action);
        btn_again = dialog.findViewById(R.id.btn_again);
        txt_message_fail = dialog.findViewById(R.id.txt_message_fail);
        txt_title_message_fail = dialog.findViewById(R.id.txt_title_message_fail);

        txt_message_fail.setText(message);
        txt_title_message_fail.setText(title);
        btn_exit.setText(btn_name_left);
        btn_again.setText(btn_name_right);

        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoHomeScreen();
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
    /*phuong thuc show dialog khi dang ki that bai*/

    /*phuong thuc chuyen trang sang trang Home*/
    private void gotoHomeScreen(){
        Intent intent_home = new Intent(DescriptionRegisterActivity.this, MainActivity.class);
        startActivity(intent_home);
    }
    /*phuong thuc chuyen trang sang trang Home*/

}
