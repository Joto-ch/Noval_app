package com.example.demo;

import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.v4.media.session.IMediaControllerCallback;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioGroup;

import com.example.demo.fragment.bookshelfFragment;
import com.example.demo.fragment.categoryFragment;
import com.example.demo.fragment.leaderboardFragment;

import java.util.ArrayList;
import java.util.List;

public class menu_activity extends BaseActivity{
    private ViewPager vp;
    private RadioGroup rg;
    private int[] rbs = {R.id.bookshelf, R.id.search_book, R.id.category};
    private List<Fragment> mFragments;

    //简化后的方法
    @Override
    protected int getLayoutID() {
        return R.layout.menu_layout;
    }

    @Override
    protected void initView() {
        vp = f(R.id.vp);
        rg = f(R.id.rg);
    }

    @Override
    protected void initData() {

        mFragments=new ArrayList<>();
        bookshelfFragment one=new bookshelfFragment();
        leaderboardFragment two=new leaderboardFragment();
        categoryFragment three=new categoryFragment();
        mFragments.add(one);
        mFragments.add(two);
        mFragments.add(three);

        // 设置填充器
        vp.setAdapter(new menuAdapter(getSupportFragmentManager(),mFragments));
        // 设置缓存页面数
        vp.setOffscreenPageLimit(2);

    }

    @Override
    protected void initListener() {
        //radioGroup的点击事件
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                for (int i = 0; i < rbs.length; i++) {
                    if (rbs[i] != checkedId) continue;
                    //加载滑动
                    vp.setCurrentItem(i);
                }
            }
        });
        //ViewPager的点击事件 vp-rg互相监听：vp
        vp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                rg.check(rbs[position]);
            }
        });
        //设置一个默认页
        rg.check(rbs[0]);
    }
}
