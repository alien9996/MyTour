package com.example.dell.mytour.uis;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dell.mytour.R;

public abstract class BaseFragments extends Fragment{

    protected View view_home;
    protected int layout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = injectLayout();
        view_home = inflater.inflate(layout,container,false);
        injectView();
        injectVariables();
        return view_home;
    }

    protected abstract int injectLayout();

    protected abstract void injectView(); // dung de khoi tao cac view trong activity

    protected abstract void injectVariables(); // dung de khoi tao cac bien
}
