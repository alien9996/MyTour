package com.example.dell.mytour.uis.activity;

import android.widget.Button;
import android.widget.EditText;

import com.example.dell.mytour.R;
import com.example.dell.mytour.uis.BaseActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LoginGmailActivity extends BaseActivity {

    private EditText edt_gmail,edt_password_gmail;
    private Button btn_login_gmail;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener  authStateListener;

    @Override
    protected int injectLayout() {
        return R.layout.activity_gmail_login;
    }

    @Override
    protected void injectView() {

        // init instance of FirebaseAuth

    }

    @Override
    protected void injectVariables() {

    }




    //-------------------------------------------------



    //----------------------------------------------------------------------------
    // validate form
    private boolean validateForm(String email, String pass){


        if(email.isEmpty() || pass.isEmpty()){
            return false;
        } else {
            return true;
        }
    }
}
