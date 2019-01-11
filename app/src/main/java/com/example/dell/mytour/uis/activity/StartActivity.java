package com.example.dell.mytour.uis.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.mytour.R;
import com.example.dell.mytour.uis.BaseActivity;

public class StartActivity extends BaseActivity {
    // khai bao bien handler dung cho viec tat dialog
    private Handler handler;
    @Override
    protected int injectLayout() {
        return R.layout.custom_dialog_loading;
    }

    @Override
    protected void injectView() {

        startApplication();

    }

    @Override
    protected void injectVariables() {

    }

    // phuong thuc kiem tra ket noi internet
    private boolean checkConnectInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) StartActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        } else {
            return false;
        }
    }


    // phuong thuc show dialog khi khong co ket noi internet
    private void showDialogWhenConnectInternetFail(){
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_success);

        TextView txt_title_message_success, txt_content_message_success;
        Button btn_success;

        txt_title_message_success = dialog.findViewById(R.id.txt_title_message_success);
        txt_content_message_success = dialog.findViewById(R.id.txt_content_message_success);
        btn_success = dialog.findViewById(R.id.btn_success);

        txt_title_message_success.setText("Lỗi kết nối!");
        txt_content_message_success.setText("Có vẻ như đường truyền Internet của bạn có vẫn đề. " +
                "Hãy xem lại kết nối Internet của bạn và chạy lại ứng dụng.");

        btn_success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                finish();
            }
        });
        dialog.show();
    }

    // phuong thuc khoi chay ung dung khi co ket noi internet
    private void startApplication(){
        // khoi tao bien handler
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(checkConnectInternet()){
                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showDialogWhenConnectInternetFail();
                }

            }
        },2000);
    }
}
