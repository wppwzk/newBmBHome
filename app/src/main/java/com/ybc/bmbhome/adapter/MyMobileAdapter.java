package com.ybc.bmbhome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybc.bmbhome.R;
import com.ybc.bmbhome.utils.App;

import java.util.ArrayList;

/**
 * Created by ybc on 2016/7/6.
 * App使用情况适配器
 */
public class MyMobileAdapter extends BaseAdapter {
    ArrayList<App> apps = new ArrayList<>();
    Context context;

    public MyMobileAdapter() {
    }

    public MyMobileAdapter(ArrayList<App> apps, Context context) {
        this.apps = apps;
        this.context = context;
    }

    @Override
    public boolean isEmpty() {
        return apps.size() == 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_imageview);
        TextView textView1 = (TextView) convertView.findViewById(R.id.item_text1);
        TextView textView2 = (TextView) convertView.findViewById(R.id.item_text2);
        TextView textView3 = (TextView) convertView.findViewById(R.id.item_text3);
        imageView.setBackground(apps.get(position).icon);
        textView1.setText(apps.get(position).name);
        textView2.setText(apps.get(position).time_last);
        textView3.setText(apps.get(position).time_long);
        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
