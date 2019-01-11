package com.example.dell.mytour.uis.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.mytour.R;
import com.example.dell.mytour.uis.BaseActivity;
import com.example.dell.mytour.uis.activity.result.ResultActivity;


public class LocationActivity extends BaseActivity implements View.OnClickListener{

    // khai bao linearlayout
    private LinearLayout ln_bien_lang_co, ln_cung_dinh_hue, ln_vuon_bach_ma, ln_chua_thien_mu, ln_background;
    private TextView txt_message;

    @Override
    protected int injectLayout() {
        return R.layout.activity_location;
    }

    @Override
    protected void injectView() {
        // khoi tao cac linearLayout
        ln_bien_lang_co = findViewById(R.id.ln_bien_lang_co);
        ln_cung_dinh_hue = findViewById(R.id.ln_cung_dinh_hue);
        ln_vuon_bach_ma = findViewById(R.id.ln_vuon_bach_ma);
        ln_chua_thien_mu = findViewById(R.id.ln_chua_thien_mu);
        ln_background = findViewById(R.id.ln_background);

        //khai bao cac text view
        txt_message = findViewById(R.id.txt_message);

        //set su kien onClick cho linearLayout
        ln_bien_lang_co.setOnClickListener(this);
        ln_cung_dinh_hue.setOnClickListener(this);
        ln_vuon_bach_ma.setOnClickListener(this);
        ln_chua_thien_mu.setOnClickListener(this);

        Intent intent = getIntent();
        if(Integer.parseInt(intent.getStringExtra("status")) == 0){
            ln_background.setVisibility(View.GONE);
            txt_message.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void injectVariables() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ln_vuon_bach_ma:
                gotoScreenSearch("Vườn Bạch Mã, Huế");
                break;
            case R.id.ln_bien_lang_co:
                gotoScreenSearch("Biển Lăng Cô, Huế");
                break;
            case R.id.ln_chua_thien_mu:
                gotoScreenSearch("Chùa Thiên Mụ, Huế");
                break;
            case R.id.ln_cung_dinh_hue:
                gotoScreenSearch("Cung Đình Huế");
                break;
        }
    }

    // phuong thuc chuyen sang trang tim kiem
    private void gotoScreenSearch(String query){
        Intent intent_search = new Intent(LocationActivity.this, ResultActivity.class);
        intent_search.putExtra("intent_type", "2");
        intent_search.putExtra("data", query);
        startActivity(intent_search);
    }
}

