package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demo.fragment.book;
import com.example.demo.fragment.bookAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class booklist_activity extends AppCompatActivity {

    private static final String TAG = "booklist_activity";

    private ListView listView;
    private ArrayList<book> mArrayList;
    private bookAdapter madapter;
    private String url;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_listview);

        Intent url_put = getIntent();
        url = url_put.getStringExtra("URL");
        Log.d(TAG, "getExtra: " + url);

        listView = findViewById(R.id.book_listview);
        listView.setEmptyView(findViewById(R.id.empty_listview));
        mArrayList = new ArrayList<>();
//        madapter = new bookAdapter(mArrayList);
//        listView.setAdapter(madapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                book item = mArrayList.get(position);
                Intent intent = new Intent(booklist_activity.this, info_activity.class);
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
                    document = Jsoup.connect(url).get();

                    Elements cover = document.getElementsByClass("pt-ll-l");
                    Log.d(TAG, "cover_size: "+cover.size());
                    Elements info = document.getElementsByClass("info");

                    for (int i=0; i<cover.size(); i++) {
                        String cover_img = "http://www.booksky.cc" + cover.get(i).select("img")
                                .attr("src");
                        String book_name = info.get(i).select("span").get(0).text();
                        String book_author = info.get(i).select("span").get(1).text();
                        String url_src = "http://www.booksky.cc" + info.get(i).select("a")
                                .attr("href");
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
