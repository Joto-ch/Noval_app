package com.example.demo;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.NavigationRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

public abstract class BaseActivity extends FragmentActivity {
    protected BaseActivity act;
    protected final String TAG=getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act=this;
        setContentView(getLayoutID());
        initView();
        initData();
        initListener();
    }
    @LayoutRes
    protected abstract int getLayoutID();
    protected abstract void initListener();
    protected abstract void initView();
    protected abstract void initData();

    @SuppressWarnings("unchecked")
    protected <E> E f(int id){
        return (E)findViewById(id);
    }
}
