package com.qiaojim.qjviews.QJPageReloadView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.qiaojim.qjviews.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class QJPageReloadActivity extends AppCompatActivity {

    private final String TAG = "QJPageReloadView";

    private int start = -1;
    private int end = 30;

    private QJPageReloadView qjPageReloadView;
    private QJPageReloadView.QJPageReloadViewListener listener;
    private LinkedList<String> dataList = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qj_page_reload);

        initListener();
        initView();
    }

    private void initListener() {
        listener = new QJPageReloadView.QJPageReloadViewListener() {
            @Override
            public void onRefresh(int totalCount) {
                Log.e(TAG, "======== onRefresh()回调 ==========");

                for (int i = 0; i < 10; i++) {
                    dataList.addFirst("" + start--);
                }
                qjPageReloadView.update();
            }

            @Override
            public void onLoadMore(int totalCount) {
                Log.e(TAG, "======== onLoadMore()回调 ==========");

                for (int i = 0; i < 10; i++) {
                    dataList.add("" + end++);
                }
                qjPageReloadView.update();
            }

            @Override
            public void onAutoLoadMore(int totalCount) {
                Log.e(TAG, "======== onAutoLoadMore()回调 ==========");

                for (int i = 0; i < 10; i++) {
                    dataList.add("" + end++);
                }
                qjPageReloadView.update();
            }
        };
    }

    private void initView() {

        for (int i = 0; i < 30; i++) {
            dataList.add("" + i);
        }

        qjPageReloadView = findViewById(R.id.qj_page_reload_view);
        QJReloadViewAdapter adapter = new QJReloadViewAdapter(this, qjPageReloadView);
        adapter.setData(dataList);
        qjPageReloadView.setAutoLoadMore(false);
        qjPageReloadView.setAdapter(adapter);
        qjPageReloadView.setQJPageReloadViewListener(listener);

    }
}
