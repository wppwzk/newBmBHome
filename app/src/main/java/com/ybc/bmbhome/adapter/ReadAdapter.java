package com.ybc.bmbhome.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybc.bmbhome.R;
import com.ybc.bmbhome.utils.ReadList;

import java.util.List;


/**
 * Created by YBC on 2017/7/2 20:42 20:25 20:25.
 */

public class ReadAdapter extends ArrayAdapter<ReadList> {
    private int resourseId;
    public ReadAdapter(Context context, int textViewResourceId, List<ReadList> objects) {
        super(context, textViewResourceId, objects);
        resourseId=textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReadList readList=getItem(position);
        View view;
        ViewHolder viewholder;
        if(convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourseId,parent,false);
            viewholder=new ViewHolder();
            viewholder.readimage=(ImageView) view.findViewById(R.id.read_image);
            viewholder.title= (TextView) view.findViewById(R.id.readtitle);
            viewholder.time= (TextView) view.findViewById(R.id.readtime);
            viewholder.id= (TextView) view.findViewById(R.id.readid);
            viewholder.imgurl= (TextView) view.findViewById(R.id.imgurl);
            view.setTag(viewholder);
        }else {
            view=convertView;
            viewholder= (ViewHolder) view.getTag();
        }

        viewholder.imgurl.setText(readList.getImgUrl());
        viewholder.readimage.setImageBitmap(readList.getBitmap());
        viewholder.title.setText(readList.getTitle());
        viewholder.time.setText(readList.getCreateTime());
        viewholder.id.setText(readList.getId()+"");
        return view;
    }

    class ViewHolder{
        ImageView readimage;
        TextView title,time,id,imgurl;
    }
}
