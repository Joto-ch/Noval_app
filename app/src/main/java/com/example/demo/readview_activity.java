package com.example.demo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapThumbnailImageViewTarget;
//import com.example.demo.fragment.menuFragment;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

import io.reactivex.Maybe;

public class readview_activity extends AppCompatActivity implements OnClickListener {

    private static final String TAG = "readview_activity";

    private int size[] = {18, 20, 24, 28, 30};
    private String color[] = {"#ffffff", "#ccffcc", "#ffffcc", "#9999ff", "#cc9933"};
    private int size_position = 0;
    private int color_position = 0;

    private int click = 0;  //点击判断

    private AppDatabase appDatabase;
    private LocalBookDao localBookDao;
    private String name;  //书名

    private String url_capter; //章节目录
    private String url_pre;
    private String url_next;

    private String url;
    private Handler handler;
    private String  content;

    private TextView read_capter;
    private TextView read_text;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.readview_layout);

        final LinearLayout linearLayout = findViewById(R.id.menu);

        Button read_pre = findViewById(R.id.read_pre);
        Button read_menu = findViewById(R.id.read_menu);
        Button read_next = findViewById(R.id.read_next);
        read_pre.setOnClickListener(this);
        read_menu.setOnClickListener(this);
        read_next.setOnClickListener(this);

        Button size_plus = findViewById(R.id.size_plus);
        Button size_delete = findViewById(R.id.size_delete);
        Button btn1 = findViewById(R.id.btn1_circle);
        Button btn2 = findViewById(R.id.btn2_circle);
        Button btn3 = findViewById(R.id.btn3_circle);
        Button btn4 = findViewById(R.id.btn4_circle);
        Button btn5 = findViewById(R.id.btn5_circle);
        size_plus.setOnClickListener(this);
        size_delete.setOnClickListener(this);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);

        read_capter = findViewById(R.id.read_capter);
        read_text = findViewById(R.id.read_text);
        //设置初始字体大小,设置初始背景颜色
        init_style(size_position, color_position);
        //添加TextView滑动
        read_text.setMovementMethod(ScrollingMovementMethod.getInstance());
        //点击菜单
        read_text.setClickable(true);

        read_text.setOnClickListener(new DoubleClickListener() {
            @Override
            public void onDoubleClick(View v) {
                Log.d(TAG, "onDoubleClick: sucessed");
                click++;
                if (click%2 == 1){
                    linearLayout.setVisibility(View.VISIBLE);
                }else {
                    linearLayout.setVisibility(View.GONE);
                }
            }
        });

        Intent url_read_get = getIntent();
        url = url_read_get.getStringExtra("url_read");
        Log.d(TAG, "get_url_read: "+url);

        try {
            Read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        handler = new Handler(){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
//                    read_text.getSelectionStart();
                    read_text.setText(" "+content);
                    read_text.scrollTo(0,0);
                }
            }
        };
    }

    private void init_style(int s, int c){
        LinearLayout linearLayout = findViewById(R.id.read);
        //设置初始字体大小
        read_text.setTextSize(TypedValue.COMPLEX_UNIT_DIP, size[s]);
        //设置初始背景颜色
        read_text.setBackgroundColor(Color.parseColor(color[c]));
        read_capter.setBackgroundColor(Color.parseColor(color[c]));
        linearLayout.setBackgroundColor(Color.parseColor(color[c]));
    }

    private void Read() throws IOException{

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    Document document = Jsoup.connect(url).get();

                    Elements elements = document.getElementsByClass("info");
                    name = elements.select("span").get(0).select("a").text();
                    Log.d(TAG, "boook_name: "+name);

                    url_pre = "http://www.booksky.cc"+document.select("li").get(1)
                            .select("a").attr("href");
                    Log.d(TAG, "url_pre: "+url_pre);
                    url_capter = "http://www.booksky.cc"+document.select("li").get(2)
                            .select("a").attr("href");
                    Log.d(TAG, "url_capter: "+url_capter);
                    url_next = "http://www.booksky.cc"+document.select("li").get(3)
                            .select("a").attr("href");
                    Log.d(TAG, "url_next: "+url_next);

                    String title = document.select("h1").text();
                    Log.d(TAG, "title: "+title);

                    Element element = document.getElementById("chaptercontent");
                    content = element.html();
//                    content = content.replaceAll("<br>", "\n");
                    content = content.replaceAll("<br>|&nbsp;", " ");
                    content = " "+content;
//                    read_text.setText(element.text());
                    Log.d(TAG, "element: "+content);

                    read_capter.setText(title);

                    Message msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.read_pre:
                if (url_pre.equals(url_capter)){
                    break;
                }else {
                    url = url_pre;
                    try {
                        //更新read_url
                        Read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.read_next:
                url = url_next;
                try {
                    //更新read_url
                    Read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.read_menu:
                Intent intent = new Intent(readview_activity.this, capterlist_activity.class);
                intent.putExtra("URL_CAPTER", url_capter);
                startActivityForResult(intent, 1);
                break;
            case R.id.size_plus:
                if (size_position !=4){
                    size_position++;
                    init_style(size_position, color_position);
                }
                break;
            case R.id.size_delete:
                if (size_position != 0){
                    size_position--;
                    init_style(size_position, color_position);
                }
                break;
            case R.id.btn1_circle:
                color_position = 0;
                init_style(size_position, color_position);
                break;
            case R.id.btn2_circle:
                color_position = 1;
                init_style(size_position, color_position);
                break;
            case R.id.btn3_circle:
                color_position = 2;
                init_style(size_position, color_position);
                break;
            case R.id.btn4_circle:
                color_position = 3;
                init_style(size_position, color_position);
                break;
            case R.id.btn5_circle:
                color_position = 4;
                init_style(size_position, color_position);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK){
                    url = data.getStringExtra("setcapter");
                    Log.d(TAG, "return url: "+url);
                    try {
                        Read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.d(TAG, "请求失败。");
                }
                break;
            default:
                break;
        }
    }

    //    private void replaceFrgment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.replace(R.id.menu_left, fragment);
//        fragmentTransaction.commit();
//    }

    @Override
    protected void onPause() {
        super.onPause();

        new Thread(new Runnable() {
            @Override
            public void run() {

                appDatabase = Room.databaseBuilder(getBaseContext(), AppDatabase.class, "APP_data")
//                        .allowMainThreadQueries()
                        .build();
                localBookDao = appDatabase.getLocalBookDao();

//                List<LocalBook> list = localBookDao.getAllLocalBook();
////                for (int i=0; i<list.size(); i++){
////                    if (list.get(i).getName().equals(name)){
////                        LocalBook localBook = list.get(i);
////                        localBook.setRead_url(url);
////                        localBookDao.updataLocalBook(localBook);
////                        Log.d(TAG, "updata_url: "+localBook.getRead_url());
////                    }
////                }

                try {
                    LocalBook localBook = localBookDao.getLocalBook(name);
                    localBook.setRead_url(url);
                    localBookDao.updataLocalBook(localBook);
                    Log.d(TAG, "upadata_url: "+url);
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        }).start();
    }
}
