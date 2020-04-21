package com.example.demo.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.viewpager.widget.ViewPager;

import com.example.demo.AppDatabase;
import com.example.demo.LocalBook;
import com.example.demo.LocalBookDao;
import com.example.demo.R;

import java.util.ArrayList;
import java.util.List;

public class bookshelfFragment extends Fragment {

    private boolean flag = true;

    private Handler handler;

    private AppDatabase appDatabase;
    private LocalBookDao localBookDao;

    private ListView listView;
    private ArrayList<book> mArrayList;
    private bookAdapter madapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookshelf_fragment, container, false);
        listView = view.findViewById(R.id.bookshelf_listview);
        mArrayList = new ArrayList<>();
//        madapter = new bookAdapter(mArrayList);
////        listView.setAdapter(madapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //跳转至readview_acitivity,并传递url
                book item = mArrayList.get(position);
                Intent intent = new Intent(getActivity(), com.example.demo.readview_activity.class);
                intent.putExtra("url_read", item.getUrl());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final book item = mArrayList.get(position);
//                final AlertDialog.Builder sure = new AlertDialog.Builder(getActivity());
//                sure.setTitle("提醒");
//                sure.setMessage("您确定要删除此书?");
//                sure.setPositiveButton("确定",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
                                //调用数据库删除
                                LocalBook localBook = new LocalBook(item.getName()
                                ,item.getIntroduction(), item.getImageId(), item.getUrl());
                                mArrayList.remove(position);
                                delete(localBook);
//                                Toast.makeText(getActivity(), "您取消了操作", Toast.LENGTH_SHORT)
//                                        .show();
//                            }
//                        });
//                sure.setNegativeButton("取消",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getActivity(), "您取消了操作", Toast.LENGTH_SHORT)
//                                        .show();
//                            }
//                        });
//                sure.show();
                return false;
            }
        });

//        for (int i=0; i<4; i++){
//            mArrayList.add(new book("第"+i+"本书", "简介简介简介简介简介简介",
//                    "https://bookcover.yuewen.com/qdbimg/349573/1004132133/150","djfkls"));
//        }

        view_print();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
//                    mArrayList.clear();
//                    view_print();
                    madapter = new bookAdapter(mArrayList);
                    listView.setAdapter(madapter);
                }
            }
        };

        return  view;
    }

    private void view_print() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "APP_data")
//                        .allowMainThreadQueries()
                        .build();
                localBookDao = appDatabase.getLocalBookDao();

//                LocalBook book1 = new LocalBook("大主宰2", "天蚕土豆", "cover_url"
//                        ,"read_url");
//                LocalBook book2 = new LocalBook("斗破苍穹2", "天蚕土豆", "cover_url"
//                        ,"read_url");
//                localBookDao.insertLocalBook(book1, book2);

                mArrayList.clear();
                List<LocalBook> list = localBookDao.getAllLocalBook();
                for (int i=0; i<list.size(); i++){
                    LocalBook localBook = list.get(i);
                    mArrayList.add(new book(localBook.getName(), localBook.getAuthor()
                            ,localBook.getCover_url(), localBook.getRead_url()));
                }

                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void delete(final LocalBook localBook){

        new Thread(new Runnable() {
            @Override
            public void run() {

                appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "APP_data")
//                        .allowMainThreadQueries()
                        .build();
                localBookDao = appDatabase.getLocalBookDao();

                localBookDao.deleteLocalBook(localBook);

                Message msg = new Message();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!flag){
            view_print();
        }

        flag = false;
    }
}
