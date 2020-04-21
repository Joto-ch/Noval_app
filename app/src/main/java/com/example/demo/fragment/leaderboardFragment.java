package com.example.demo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.demo.R;
import com.example.demo.catelist_activity;
import com.example.demo.info_activity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class leaderboardFragment extends Fragment {
    @Nullable

    private static final String TAG = "leaderboardFragment";

    private EditText editText;

    private ListView listView;
    private ArrayList<book> mArrayList;
    private bookAdapter madapter;
    private String url;
    private Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.leaderboard_fragment, container, false);
        return view;
//        return inflater.inflate(R.layout.leaderboard_fragment, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        url = editText.getText().toString();
//        Log.d(TAG, "getText: "+url);
        ImageButton imageButton = getActivity().findViewById(R.id.search_image);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "ejkj", Toast.LENGTH_SHORT).show();
                editText = getActivity().findViewById(R.id.input_book);
                String s = editText.getText().toString();
                editText.setText("");
                url = "http://www.booksky.cc/modules/article/search.php?searchkey="+s;
                Log.d(TAG, "putExtra: "+url);
                if (s.length() != 0){
                    Intent intent = new Intent(getActivity(), com.example.demo.booklist_activity.class);
                    intent.putExtra("URL", url);
                    startActivity(intent);
                }
            }
        });

        listView = getActivity().findViewById(R.id.book_hot);
        mArrayList = new ArrayList<>();
//        madapter = new bookAdapter(mArrayList);
//        listView.setAdapter(madapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book item = mArrayList.get(position);
                Intent intent = new Intent(getActivity(), info_activity.class);
                intent.putExtra("URL", item.getUrl());
                startActivity(intent);
            }
        });

//        for (int i=0; i<100;i++){
//            mArrayList.add(new book("第"+i+"本书", "简介简介",
//                    "https://bookcover.yuewen.com/qdbimg/349573/1004132133/150", "lksjfl"));
//        }

        connect();

        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1) {
                    madapter = new bookAdapter(mArrayList);
                    listView.setAdapter(madapter);
                }
            }
        };
    }

    private void connect(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Document document = null;
                try {
                    document = Jsoup.connect("http://www.booksky.cc/top/votenum/").get();

                    Elements cover = document.getElementsByClass("pt-ll-l");
                    Log.d(TAG, "cover_size: "+cover.size());
                    Elements info = document.getElementsByClass("info");

                    for (int i=0; i<cover.size(); i++) {
//                        String cover_img = "http://www.booksky.cc" + cover.get(i).select("img").attr("src");
                        String cover_img = cover.get(i).select("img").attr("src");
                        String book_name = info.get(i).select("span").get(0).text();
                        book_name = book_name.replace("《", "");
                        book_name = book_name.replace("》", "");
                        String book_author = info.get(i).select("span").get(1).text();
                        String url_src = "http://www.booksky.cc" + info.get(i).select("a").attr("href");
//                            String url_src = "http://www.booksky.cc"+s;
                        Log.d(TAG, "cover_img: " + cover_img);
                        Log.d(TAG, "book_name: " + book_name);
                        Log.d(TAG, "book_author: " + book_author);
                        Log.d(TAG, "url_src: " + url_src);
                        mArrayList.add(new book(book_name, book_author, cover_img, url_src));
                    }
                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
