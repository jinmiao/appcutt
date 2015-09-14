package com.appcutt.demo.views.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.appcutt.demo.views.fragments.CatsGridFragment;

import java.util.ArrayList;
import java.util.List;

public class CatsGridPagerAdappter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private List<String> titles;

    public CatsGridPagerAdappter(FragmentManager fm, Context context) {
        super(fm);
        titles = new ArrayList<>();
        titles.add(context.getString(com.appcutt.demo.R.string.popular));
        titles.add(context.getString(com.appcutt.demo.R.string.news));

        fragments = new ArrayList<>();
        fragments.add(CatsGridFragment.newInstance("250x400"));
        fragments.add(CatsGridFragment.newInstance("200x300"));
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}