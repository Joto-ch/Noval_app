package com.example.demo.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.request.target.ViewTarget;
import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.R.id.book_image;
import static com.example.demo.R.id.image;
import static com.example.demo.R.id.tag_accessibility_heading;
import static com.example.demo.R.id.text;

public class bookAdapter extends BaseAdapter{

    private static final String TAG = "bookAdapter";

    private ArrayList<book> mArrayList;
//    private int resourceId;

//    public bookAdapter(@NonNull Context context,int textViewResourceId,
//                       @NonNull List<book> objects) {
//        super(context, textViewResourceId, objects);
//        resourceId = textViewResourceId;
//    }
//    private Context context;



    private class ViewHolder{
        TextView name;
        TextView introduction;
        ImageView image;
    }

    public bookAdapter(ArrayList<book> arrayList){
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
                    .inflate(R.layout.book_item, null);
            holder.name = convertView.findViewById(R.id.book_name);
            holder.introduction = convertView.findViewById(R.id.book_introduction);
            holder.image = convertView.findViewById(book_image);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        book books = mArrayList.get(position);

        holder.name.setText(books.getName());
        holder.introduction.setText(books.getIntroduction());
//        holder.image.setImageResource(books.getImageId());

        Glide.with(parent.getContext()).load(books.getImageId()).into(holder.image);

        return convertView;
    }
}


