package com.ybc.bmbhome.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ybc.bmbhome.R;
import com.ybc.bmbhome.utils.DiaryItem;

import java.util.List;


/**
 * 自定义ListView适配器
 *
 * @author king
 */
public class MyDiaryItemAdapter extends BaseAdapter {
    List<DiaryItem> items = null;
    private Context context;

    public MyDiaryItemAdapter(List<DiaryItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public int getCount() {
        return this.items.size();
    }

    public Object getItem(int paramInt) {
        return null;
    }

    public long getItemId(int paramInt) {
        return paramInt;
    }

    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
        if (paramView == null) {
            //LayoutInflater localLayoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
            LayoutInflater localLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            paramView = localLayoutInflater.inflate(R.layout.diary_list_item, paramViewGroup, false);
            localLayoutInflater.inflate(R.layout.diary_list_item, paramViewGroup, false);
        }

        TextView paramTitle = (TextView) paramView.findViewById(R.id.list_diary_title);
        TextView paramDate = (TextView) paramView.findViewById(R.id.list_diary_date);
        TextView paramMood = (TextView) paramView.findViewById(R.id.list_diary_mood);
        DiaryItem localDiaryItems = (DiaryItem) this.items.get(paramInt);
        paramTitle.setText(localDiaryItems.getTitle());
        paramDate.setText(localDiaryItems.getDate());
        paramMood.setText(localDiaryItems.getMood());

        return paramView;
    }
}
