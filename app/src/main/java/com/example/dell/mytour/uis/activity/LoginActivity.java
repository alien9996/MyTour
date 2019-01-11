package com.example.dell.mytour.uis.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.common.EncodeMD5;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.register_activity.EmailAndPasswordRegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private EditText edt_user_name_login, edt_password_login;
    private Button btn_login, btn_skip_login;
    private FirebaseAuth firebase_auth;

    @Override
    protected int injectLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void injectView() {

        btn_login = findViewById(R.id.btn_login);
        btn_skip_login = findViewById(R.id.btn_skip_login);

        edt_user_name_login = findViewById(R.id.edt_user_name_login);
        edt_password_login = findViewById(R.id.edt_password_login);

        // init instance of FirebaseAuthg
        firebase_auth = FirebaseAuth.getInstance();

        btn_login.setOnClickListener(this);
        btn_skip_login.setOnClickListener(this);

    }

    @Override
    protected void injectVariables() {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
            String email_loign = edt_user_name_login.getText().toString().trim();
            String password_login = edt_password_login.getText().toString().trim();

            if (TextUtils.isEmpty(email_loign)) {
                edt_user_name_login.setError("Email không được để trống");
                return;
            }

            if (TextUtils.isEmpty(password_login)) {
                edt_password_login.setError("Password không được để trống");
                return;
            }

            signIn(email_loign, EncodeMD5.toMD5(password_login));
        }
        if(v.getId() == R.id.btn_skip_login){
            gotoHomeScreen();
        }
    }


    // phuong thuc signIn
    private void signIn(String email, String password) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Đăng nhập...");
        progressDialog.show();
        firebase_auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            gotoHomeScreen();
                            progressDialog.dismiss();
                        } else {
                            showDialog();
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    // phuong thuc chuyen trang khi thanh cong
    private void gotoHomeScreen() {
        Intent intent_home = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent_home);
        this.finish();
    }

    // chuyen den trang dang ky
    private void gotoRegisterScreen(){
        Intent intent_register = new Intent(LoginActivity.this, EmailAndPasswordRegisterActivity.class);
        startActivity(intent_register);
    }


    // phuong thuc show dialog
    public void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_fail);

        Button btn_register, btn_again;
        TextView txt_message_fail;

        btn_register = dialog.findViewById(R.id.btn_action);
        btn_again = dialog.findViewById(R.id.btn_again);
        txt_message_fail = dialog.findViewById(R.id.txt_message_fail);

        txt_message_fail.setText("Có vẻ như "+edt_user_name_login.getText().toString()+" không khớp với tài" +
                "khoản hiện tại. Nếu bạn chưa có tài khoản MyTour, bạn có thể tạo tài khoản ngay bây giờ.");

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegisterScreen();
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



}
