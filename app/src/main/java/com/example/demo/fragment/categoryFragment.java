package com.example.demo.fragment;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.media.session.IMediaControllerCallback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.demo.R;

public class categoryFragment extends Fragment implements View.OnClickListener{
    @Nullable

    private static final String TAG = "categoryFragment";

    private String url;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.category_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Intent intent = new Intent(getActivity(), com.example.demo.booklist_activity.class);

        ImageButton category_xuanhuan = getActivity().findViewById(R.id.category_xuanhuan);
        ImageButton category_wuxia = getActivity().findViewById(R.id.category_wuxia);
        ImageButton category_xianxia = getActivity().findViewById(R.id.category_xianxia);
        ImageButton category_dushi = getActivity().findViewById(R.id.category_dushi);
        ImageButton category_lingyi = getActivity().findViewById(R.id.category_lingyi);
        ImageButton catagory_nvsheng = getActivity().findViewById(R.id.category_nvsheng);

        category_xuanhuan.setOnClickListener(this);
        category_wuxia.setOnClickListener(this);
        category_xianxia.setOnClickListener(this);
        category_dushi.setOnClickListener(this);
        category_lingyi.setOnClickListener(this);;
        catagory_nvsheng.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getActivity(), com.example.demo.catelist_activity.class);

        switch (v.getId()){
            case R.id.category_xuanhuan:
                url = "http://www.booksky.cc/category/xuanhuan/";
                break;
            case R.id.category_wuxia:
                url = "http://www.booksky.cc/category/wuxia/";
                break;
            case R.id.category_xianxia:
                url = "http://www.booksky.cc/category/xianxia/";
                break;
            case R.id.category_dushi:
                url = "http://www.booksky.cc/category/dushi/";
                break;
            case R.id.category_lingyi:
                url = "http://www.booksky.cc/category/lingyi/";
                break;
            case R.id.category_nvsheng:
                url = "http://www.booksky.cc/category/nvsheng/";
                break;
            default:
                break;
        }
        intent.putExtra("URL", url);
        startActivity(intent);
    }
}
