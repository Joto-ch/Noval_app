package com.example.demo;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;

public class menuAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> frags;

    public menuAdapter(FragmentManager fm, List<Fragment> frags) {
        super(fm);
        this.frags = frags;
    }

    @Override
    public Fragment getItem(int position) {
        return frags.get(position);
    }

    @Override
    public int getCount() {
        return frags.size();
    }
}
