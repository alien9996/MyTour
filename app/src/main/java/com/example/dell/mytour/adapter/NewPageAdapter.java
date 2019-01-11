package com.example.dell.mytour.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.dell.mytour.uis.fragments.FragmentAdd;
import com.example.dell.mytour.uis.fragments.FragmentHome;
import com.example.dell.mytour.uis.fragments.FragmentLibrary;
import com.example.dell.mytour.uis.fragments.FragmentMessage;
import com.example.dell.mytour.uis.fragments.FragmentPost;

public class NewPageAdapter extends FragmentStatePagerAdapter {
    public NewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        switch (position){
            case 0:
                fragment  = new FragmentHome();
                return  fragment;
            case 1:
                fragment  = new FragmentPost();
                return  fragment;
            case 2:
                fragment  = new FragmentAdd();
                return  fragment;
            case 3:
                fragment  = new FragmentMessage();
                return  fragment;
            case 4:
                fragment  = new FragmentLibrary();
                return  fragment;
            default: return null;
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}
