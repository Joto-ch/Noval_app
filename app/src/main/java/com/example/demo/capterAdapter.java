package com.example.demo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.demo.fragment.book;
import com.example.demo.fragment.bookAdapter;

import java.util.ArrayList;
import java.util.logging.Handler;

import static com.example.demo.R.id.book_image;

public class capterAdapter extends BaseAdapter {

    private static final String TAG = "capterAdapter";

    private ArrayList<capter> mArrayList;

    private class ViewHolder{
        TextView capter_name;
    }

    public capterAdapter(ArrayList<capter> arrayList){
        mArrayList = arrayList;
    }

    @Override
    public int getCount() {
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();

            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.capter_item, null);
            holder.capter_name = convertView.findViewById(R.id.capter_name);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        capter capters = mArrayList.get(position);

        holder.capter_name.setText(capters.getCapter());

        return convertView;
    }
}
