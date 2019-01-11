package com.example.dell.mytour.uis;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity{

    private ProgressDialog progress_dialog;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(injectLayout());

        context =  BaseActivity.this;

        injectVariables();
        injectView();
    }

    protected abstract int injectLayout(); // xac dinh giao dien hien thi cho activity

    protected abstract void injectView(); // dung de khoi tao cac view trong activity

    protected abstract void injectVariables(); // dung de khoi tao cac bien

    public void showProgressDialog(String title){
        try{
            if(progress_dialog ==  null){
                progress_dialog.setTitle(title);
                progress_dialog = new ProgressDialog(context);
                progress_dialog.show();
                progress_dialog.setCancelable(false); // khi bam ra ngoai progress ko tu huy
            }
        } catch (Exception ex){
            progress_dialog = new ProgressDialog(this.getParent());
            progress_dialog.show();
            progress_dialog.setCancelable(false); // khi bam ra ngoai progress ko tu huy
            ex.printStackTrace();
        }
    }

    public void closeProgressDialog(){
        try{
            if(progress_dialog != null){
                progress_dialog.cancel();
                progress_dialog = null;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void showToast(String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
}
