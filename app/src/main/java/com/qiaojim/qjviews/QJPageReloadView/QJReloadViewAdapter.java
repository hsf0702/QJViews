package com.qiaojim.qjviews.QJPageReloadView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.qiaojim.qjviews.R;

import java.util.List;

/**
 * Author: QiaoJim
 * Date:  2018/1/2
 * Email: qiaojim@qq.com
 * Desc:
 */
public class QJReloadViewAdapter extends BaseAdapter {

    private final String TAG = "QJPageReloadView";

    private QJPageReloadView view;
    private Context context;
    private List<String> data;

    public QJReloadViewAdapter(Context context, QJPageReloadView view) {
        this.context = context;
        this.view = view;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = LayoutInflater.from(context).inflate(R.layout.layout_qj_reload_item, null);
            viewHolder.tv = convertView.findViewById(R.id.text);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tv.setText(data.get(position));
        return convertView;
    }

    private static class ViewHolder {
        TextView tv;
    }

    public void setData(List<String> list) {
        this.data = list;
    }
}
