package com.example.dell.mytour.uis.activity.register_activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.dell.mytour.R;
import com.example.dell.mytour.uis.BaseActivity;

public class NameAndPhoneRegisterActivity extends BaseActivity implements View.OnClickListener {

    private Button btn_next_name, btn_delete_full_name, btn_delete_phone;
    private EditText edt_full_name_register, edt_phone_register;

    @Override
    protected int injectLayout() {
        return R.layout.activity_register_name_and_phone;
    }

    @Override
    protected void injectView() {
        btn_next_name = findViewById(R.id.btn_next_name);
        btn_delete_full_name = findViewById(R.id.btn_delete_full_name);
        btn_delete_phone = findViewById(R.id.btn_delete_phone);

        edt_full_name_register = findViewById(R.id.edt_full_name_register);
        edt_phone_register = findViewById(R.id.edt_phone_register);

        btn_next_name.setOnClickListener(this);
        btn_delete_full_name.setOnClickListener(this);
        btn_delete_phone.setOnClickListener(this);

        edt_full_name_register.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    btn_delete_full_name.setVisibility(View.VISIBLE);
                } else {
                    btn_delete_full_name.setVisibility(View.GONE);
                }
            }
        });
        edt_phone_register.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    btn_delete_phone.setVisibility(View.VISIBLE);
                } else {
                    btn_delete_phone.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void injectVariables() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next_name:
                // kiem tra gia tri nhap vao co trong hay khong
                String full_name = edt_full_name_register.getText().toString().trim();
                String phone = edt_phone_register.getText().toString().trim();

                if (TextUtils.isEmpty(full_name)) {
                    edt_full_name_register.setError("Bạn không thể bỏ trống tên");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    edt_phone_register.setError("Bạn không thể bỏ trống số điện thoại");
                    return;
                }
                gotoAddressAndBirthdayScreen(full_name, phone);
                break;
            case R.id.btn_delete_phone:

                edt_phone_register.setText("");
                break;
            case R.id.btn_delete_full_name:
                edt_full_name_register.setText("");
                break;
        }
    }

    private void gotoAddressAndBirthdayScreen(String full_name, String phone){
        Intent intent = getIntent();
        String email = intent.getStringExtra("email_register");
        String password= intent.getStringExtra("pass_register");

        Intent intent_next_name = new Intent(NameAndPhoneRegisterActivity.this, AddressAndBirthdayRegisterActivity.class);
        intent_next_name.putExtra("full_name_register", full_name);
        intent_next_name.putExtra("phone_register", phone);
        intent_next_name.putExtra("email_register", email);
        intent_next_name.putExtra("password_register", password);

        startActivity(intent_next_name);
    }
}
