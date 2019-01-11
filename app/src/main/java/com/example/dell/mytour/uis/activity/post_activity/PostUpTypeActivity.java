package com.example.dell.mytour.uis.activity.post_activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.dell.mytour.R;
import com.example.dell.mytour.uis.BaseActivity;

public class PostUpTypeActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar tool_bar;

    private Button btn_to_post_up_content;
    private RadioButton radio_btn_post_introduce, radio_btn_post_type_sharing_experiences, radio_btn_post_type_advertisement;
    String post_type = "2";

    @Override
    protected int injectLayout() {
        return R.layout.activity_post_up_type;
    }

    @Override
    protected void injectView() {

        //phan khoi tao toolbar
        tool_bar = findViewById(R.id.tool_bar);
        setSupportActionBar(tool_bar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Loại bài đăng");

        btn_to_post_up_content = findViewById(R.id.btn_to_post_up_content);
        radio_btn_post_introduce = findViewById(R.id.radio_btn_post_introduce);
        radio_btn_post_type_sharing_experiences = findViewById(R.id.radio_btn_post_type_sharing_experiences);
        radio_btn_post_type_advertisement = findViewById(R.id.radio_btn_post_type_advertisement);

        btn_to_post_up_content.setOnClickListener(this);
        radio_btn_post_introduce.setOnClickListener(this);
        radio_btn_post_type_sharing_experiences.setOnClickListener(this);
        radio_btn_post_type_advertisement.setOnClickListener(this);
    }

    @Override
    protected void injectVariables() {

    }

    // ke thua phuong thuc onOptionItemSelected


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /*phuong thuc onclick*/
    @Override
    public void onClick(View v) {
        Intent intent_post_up;
        switch (v.getId()){
            case R.id.btn_to_post_up_content:
                intent_post_up = new Intent(PostUpTypeActivity.this, PostUpContentActivity.class);
                intent_post_up.putExtra("post_style",post_type);
                startActivity(intent_post_up);
                finish();
                break;
            case R.id.radio_btn_post_introduce:
                post_type = "2";
                break;
            case R.id.radio_btn_post_type_sharing_experiences:
                post_type = "1";
                radio_btn_post_introduce.setChecked(false);
                break;
            case R.id.radio_btn_post_type_advertisement:
                post_type = "0";
                radio_btn_post_introduce.setChecked(false);
                break;
        }
    }
    /*phuong thuc onclick*/
}
