package com.example.tiongradua.graduationproject.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;

import java.util.List;

/**
 * Created by 本人 on 2019/10/29.
 */

public class ViewPagerApdapter extends FragmentPagerAdapter {
    List<Fragment> fragments;
    public ViewPagerApdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
