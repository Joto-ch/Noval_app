package com.example.demo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed;

import static com.example.demo.R.id.empty_listview;

public class capterlist_activity extends AppCompatActivity {

    private static final String TAG = "capterlist_activity";

    private ListView listView;
    private ArrayList<capter> mArrayList;
    private capterAdapter madapter;
    private String url;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.capter_listview);

        Intent url_put = getIntent();
        url = url_put.getStringExtra("URL_CAPTER");
        Log.d(TAG, "URL_CAPTER:" + url);

        listView = findViewById(R.id.capter_listview);
        listView.setEmptyView(findViewById(empty_listview));
        mArrayList = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                capter item = mArrayList.get(position);
                Intent intent = new Intent();
                intent.putExtra("setcapter", item.getItemcapter_url());
                Log.d(TAG, "item.getItemcapter_url: "+item.getItemcapter_url());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        connect_capter();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
                    madapter = new capterAdapter(mArrayList);
                    listView.setAdapter(madapter);
                }
            }
        };
    }

    private void connect_capter() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Document document = Jsoup.connect(url).get();

                    Elements elements = document.getElementsByClass("hide");

                    for (int i=0; i<elements.size(); i++){
                        String capter_name = elements.get(i).select("a").text();
                        String capter_url = "http://www.booksky.cc"+elements.get(i).select("a")
                                .attr("href");

                        Log.d(TAG, "capter_name: "+capter_name);
                        Log.d(TAG, "capter_url: "+capter_url);
                        mArrayList.add(new capter(capter_name, capter_url));
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
