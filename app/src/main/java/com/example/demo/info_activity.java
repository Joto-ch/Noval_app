package com.example.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.BatchUpdateException;

import static androidx.room.Room.databaseBuilder;

public class info_activity extends AppCompatActivity {

    private static final String TAG = "info_activity";

    private AppDatabase appDatabase;
    private LocalBookDao localBookDao;

    private Handler handler;
    private String url;
    private String bookname;  //书名
    private String bookauthor;  // 作者
    private String url_read;  //阅读链接
    private String img_cov;   //封面
    private String introduction;

    private ImageView info_bookcover;
    private TextView info_bookname;
    private TextView info_bookclick;
    private TextView info_bookauthor;
    private TextView info_bookintr;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_layout);

        Button info_bookadd = findViewById(R.id.info_bookadd);
        Button info_bookread = findViewById(R.id.info_bookread);

        info_bookcover = findViewById(R.id.info_bookcover);
        info_bookname = findViewById(R.id.info_bookname);
        info_bookclick = findViewById(R.id.info_bookclick);
        info_bookauthor = findViewById(R.id.info_bookathour);
        info_bookintr = findViewById(R.id.info_bookintr);
        info_bookintr.setMovementMethod(ScrollingMovementMethod.getInstance());

        Intent url_get = getIntent();
        url = url_get.getStringExtra("URL");
        Log.d(TAG, "info_activity: "+url);

        info_bookadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bookname, bookauthor, img_cov, url_read; //存储
                add_book();
            }
        });

        info_bookread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(info_activity.this, readview_activity.class);
                intent.putExtra("url_read", url_read);
                Log.d(TAG, "onClick: "+url_read);
                startActivity(intent);
            }
        });

//        reading.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(info_activity.this, readview_activity.class);
//                intent.putExtra("URL", url);
//                startActivity(intent);
//            }
//        });

        try {
            getdata();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler = new Handler(){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
                    Glide.with(getBaseContext()).load(img_cov).into(info_bookcover);
                    info_bookintr.setText(introduction);
                }
            }
        };

    }

    private void getdata() throws IOException {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Document document = Jsoup.connect(url).get();

                    Elements info = document.getElementsByClass("novelinfo-l");
                    Elements img = document.select("img");
                    Elements elements = document.getElementsByClass("operate");
                    Elements intro = document.getElementsByClass("body novelintro");

                    introduction = intro.get(0).text();
                    Log.d(TAG, "introduction: "+introduction);

                    url_read = "http://www.booksky.cc"
                            +elements.select("a").get(0).attr("href");
                    Log.d(TAG, "url_read: "+url_read);

                    bookname = document.select("h1").text();
                    bookauthor = info.get(0).select("li").get(0).text();

                    info_bookname.setText(bookname);
                    info_bookauthor.setText(bookauthor);
                    info_bookclick.setText(info.get(0).select("li").get(1).text());
//                    info_bookcapter.setText(info.get(0).select("li").get(4).text());

                    img_cov = img.get(1).attr("src");
                    Log.d(TAG, "run: "+img_cov);
//                    info_bookcover.setImageResource(R.drawable.oo);
//                    Glide.with(info_activity.this).load(img_cov).into(info_bookcover);

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void add_book(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase = databaseBuilder(getBaseContext(), AppDatabase.class, "APP_data")
//                        .allowMainThreadQueries()
                        .build();
                localBookDao = appDatabase.getLocalBookDao();

                Log.d(TAG, "insert_pre");

                LocalBook localBook = new LocalBook(bookname, bookauthor, img_cov, url_read);
                localBookDao.insertLocalBook(localBook);

                Log.d(TAG, "insert_next");

            }
        }).start();
    }
}
