package com.example.dell.mytour.uis.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.ToggleButton;

import com.example.dell.mytour.R;
import com.example.dell.mytour.uis.BaseFragments;
import com.example.dell.mytour.uis.activity.result.ResultActivity;

import java.util.ArrayList;

public class FragmentAdd extends BaseFragments implements View.OnClickListener, SearchView.OnQueryTextListener{
    Button btComplete;
    RatingBar rtbar;
    Button btReset;
    EditText edMin, edMax;
    ToggleButton tgb1, tgb2, tgb3, tgb4, tgb5, tgb6, tgb7, tgb8, tgb9, tgb10
            , tgb11, tgb12, tgb13, tgb14, tgb15, tgb16, tgb17, tgb18, tgb19;
    String search;

    @Override
    protected int injectLayout() {
        return R.layout.fragment_support_layout;
    }

    @Override
    protected void injectView() {
        search = "";
        btComplete = view_home.findViewById(R.id.btComplete);
        btComplete.setOnClickListener(this);
        rtbar = view_home.findViewById(R.id.rtbar);
        btReset = view_home.findViewById(R.id.btreset);
        btReset.setOnClickListener(this);

        edMin = view_home.findViewById(R.id.tvMin);
        edMax = view_home.findViewById(R.id.tvMax);
        tgb1 = view_home.findViewById(R.id.tgb1);
        tgb2 = view_home.findViewById(R.id.tgb2);
        tgb3 = view_home.findViewById(R.id.tgb3);
        tgb4 = view_home.findViewById(R.id.tgb4);
        tgb5 = view_home.findViewById(R.id.tgb5);
        tgb6 = view_home.findViewById(R.id.tgb6);
        tgb7 = view_home.findViewById(R.id.tgb7);
        tgb8 = view_home.findViewById(R.id.tgb8);
        tgb9 = view_home.findViewById(R.id.tgb9);
        tgb10 = view_home.findViewById(R.id.tgb10);
        tgb11 = view_home.findViewById(R.id.tgb11);
        tgb12 = view_home.findViewById(R.id.tgb12);
        tgb13 = view_home.findViewById(R.id.tgb13);
        tgb14 = view_home.findViewById(R.id.tgb14);
        tgb15 = view_home.findViewById(R.id.tgb15);
        tgb16 = view_home.findViewById(R.id.tgb16);
        tgb17 = view_home.findViewById(R.id.tgb17);
        tgb18 = view_home.findViewById(R.id.tgb18);
        tgb19 = view_home.findViewById(R.id.tgb19);

        tgb1.setOnClickListener(this);
        tgb2.setOnClickListener(this);
        tgb3.setOnClickListener(this);
        tgb4.setOnClickListener(this);
        tgb5.setOnClickListener(this);
        tgb6.setOnClickListener(this);
        tgb7.setOnClickListener(this);
        tgb8.setOnClickListener(this);
        tgb9.setOnClickListener(this);
        tgb10.setOnClickListener(this);
        tgb11.setOnClickListener(this);
        tgb12.setOnClickListener(this);
        tgb13.setOnClickListener(this);
        tgb14.setOnClickListener(this);
        tgb15.setOnClickListener(this);
        tgb16.setOnClickListener(this);
        tgb17.setOnClickListener(this);
        tgb18.setOnClickListener(this);
        tgb19.setOnClickListener(this);
        Reset();
    }

    @Override
    protected void injectVariables() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btComplete:
                onQueryTextSubmit(search);
                break;
            case R.id.btreset:
                Reset();
            case R.id.tgb1:
                setToggleButton(tgb1);
            case R.id.tgb2:
                setToggleButton(tgb2);
            case R.id.tgb3:
                setToggleButton(tgb3);
            case R.id.tgb4:
                setToggleButton(tgb4);
            case R.id.tgb5:
                setToggleButton(tgb5);
            case R.id.tgb6:
                setToggleButton(tgb6);
            case R.id.tgb7:
                setToggleButton(tgb7);
            case R.id.tgb8:
                setToggleButton(tgb8);
            case R.id.tgb9:
                setToggleButton(tgb9);
            case R.id.tgb10:
                setToggleButton(tgb10);
            case R.id.tgb11:
                setToggleButton(tgb11);
            case R.id.tgb12:
                setToggleButton(tgb12);
            case R.id.tgb13:
                setToggleButton(tgb13);
            case R.id.tgb14:
                setToggleButton(tgb14);
            case R.id.tgb15:
                setToggleButton(tgb15);
            case R.id.tgb16:
                setToggleButton(tgb16);
            case R.id.tgb17:
                setToggleButton(tgb17);
            case R.id.tgb18:
                setToggleButton(tgb18);
            case R.id.tgb19:
                setToggleButton(tgb19);
        }
    }

    public void Reset() {
        rtbar.setRating(5);
        edMin.setText("");
        edMax.setText("");
        search = "";
        tgb1.setChecked(false);
        tgb2.setChecked(false);
        tgb3.setChecked(false);
        tgb4.setChecked(false);
        tgb5.setChecked(false);
        tgb6.setChecked(false);
        tgb7.setChecked(false);
        tgb8.setChecked(false);
        tgb9.setChecked(false);
        tgb10.setChecked(false);
        tgb11.setChecked(false);
        tgb12.setChecked(false);
        tgb13.setChecked(false);
        tgb14.setChecked(false);
        tgb15.setChecked(false);
        tgb16.setChecked(false);
        tgb17.setChecked(false);
        tgb18.setChecked(false);
        tgb19.setChecked(false);
    }

    public void setToggleButton (ToggleButton t) {
        if (t.isChecked()) {
            t.setBackgroundColor(Color.parseColor("#80bfff"));
            search += t.getTextOn() + " ";
        }
        else{
            t.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Intent intent_search = new Intent(getActivity(), ResultActivity.class);
        intent_search.putExtra("intent_type", "2");
        intent_search.putExtra("data", query);
        startActivity(intent_search);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }
}