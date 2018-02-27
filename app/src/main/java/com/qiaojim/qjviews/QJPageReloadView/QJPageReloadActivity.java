package com.qiaojim.qjviews.QJPageReloadView;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.qiaojim.qjviews.R;

import java.lang.ref.WeakReference;
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

    private QJReloadHandler handler = new QJReloadHandler(QJPageReloadActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qj_page_reload);

        initListener();
        initView();
    }

    private void initListener() {
        listener = new QJPageReloadView.QJPageReloadViewListener() {

            /*
            * 异步加载任务开始前回调，可准备自定义的提示UI
            * main线程中，可直接操作更新UI*/
            @Override
            public void onStart() {
//                Log.e(TAG, "======== onStart()回调 ==========" + Thread.currentThread().getName());

            }

            /*
            * 下拉刷新动作后回调
            * 非UI线程，可执行耗时的加载任务*/
            @Override
            public boolean onRefresh(int totalCount) {
//                Log.e(TAG, "======== onRefresh()回调 ==========" + Thread.currentThread().getName());

                List<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add("" + start--);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Message message = handler.obtainMessage();
                message.what = QJReloadHandler.REFRESH_OK;
                message.obj = list;
                handler.sendMessage(message);

                return true;
            }

            /*
            * 加载更多动作后回调
            * 非UI线程，可执行耗时的加载任务*/
            @Override
            public boolean onLoadMore(int totalCount) {
//                Log.e(TAG, "======== onLoadMore()回调 ==========" + Thread.currentThread().getName());

                List<String> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    list.add("" + end++);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Message message = handler.obtainMessage();
                message.what = QJReloadHandler.LOAD_MORE_OK;
                message.obj = list;
                handler.sendMessage(message);

                return true;
            }

            /*
            * 异步加载任务完成后回调，可控制自定义的提醒UI
            * 即 onRefresh()、onLoadMore() return true后回调
            * main线程中，可直接操作更新UI*/
            @Override
            public void onFinished() {
//                Log.e(TAG, "======== onFinished()回调 ==========" + Thread.currentThread().getName());
            }

            /*
            * 异步加载任务完成后错误，可控制自定义的提醒UI
            * 即 onRefresh()、onLoadMore() return false后回调
            * main线程中，可直接操作更新UI*/
            @Override
            public void onError() {
//                Log.e(TAG, "======== onError()回调 ==========" + Thread.currentThread().getName());
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
        qjPageReloadView.setAdapter(adapter);

        // 下一行demo会屏蔽xml中设置的自定义属性
        qjPageReloadView.setAutoLoadMore(false);

        qjPageReloadView.setQJPageReloadViewListener(listener);

    }

    private static class QJReloadHandler extends Handler {

        private static final int REFRESH_OK = 512;
        private static final int LOAD_MORE_OK = 513;

        private WeakReference<QJPageReloadActivity> reference = null;

        private QJReloadHandler(QJPageReloadActivity reference) {
            this.reference = new WeakReference<QJPageReloadActivity>(reference);
        }

        @Override
        public void handleMessage(Message msg) {

            QJPageReloadActivity activity = reference.get();
            switch (msg.what) {
                case REFRESH_OK:
                    activity.refreshFinished((List<String>) msg.obj);
                    break;
                case LOAD_MORE_OK:
                    activity.loadMoreFinished((List<String>) msg.obj);
                    break;
            }
        }
    }

    private void refreshFinished(List<String> list) {
        for (String s : list) {
            dataList.addFirst(s);
        }
        qjPageReloadView.update();

    }

    private void loadMoreFinished(List<String> list) {
        dataList.addAll(list);
        qjPageReloadView.update();
    }
}
