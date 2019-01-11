package com.example.dell.mytour.adapter.PersonalPageAdapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dell.mytour.uis.fragments.frahments.personalpage.FragmentListDiary;
import com.example.dell.mytour.uis.fragments.frahments.personalpage.FragmentListFriend;
import com.example.dell.mytour.uis.fragments.frahments.personalpage.FragmentListImage;
import com.example.dell.mytour.uis.fragments.frahments.personalpage.FragmentListPost;

public class PersonalPageViewPagerAdapter extends FragmentPagerAdapter {
    public PersonalPageViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentListPost();
            case 1:
                return new FragmentListImage();
            case 2:
                return new FragmentListDiary();
            case 3:
                return new FragmentListFriend();
            default:
                return new FragmentListPost();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Bài đăng";
            case 1:
                return "Ảnh";
            case 2:
                return "Nhật ký";
            case 3:
                return "Bạn bè";
            default:
                return "Bài đăng";
        }
    }
}
