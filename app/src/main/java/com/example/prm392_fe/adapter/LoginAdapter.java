package com.example.prm392_fe.adapter;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.prm392_fe.fragment.LoginTabFragment;
import com.example.prm392_fe.fragment.SignUpTabFragment;

public class LoginAdapter extends FragmentPagerAdapter {
    private Context context;
    int totalTabs;

    public LoginAdapter(@NonNull FragmentManager fm, Context context, int totalTabs){
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount(){
        return totalTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginTabFragment();
            case 1:
                return new SignUpTabFragment();
            default:
                return null;
        }
    }
}
