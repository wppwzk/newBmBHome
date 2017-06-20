package com.ybc.bmbhome.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ybc.bmbhome.R;
import com.ybc.bmbhome.utils.UserItem;

import java.util.List;

/**
 * Created by YBC on 2017/4/23.
 * 用户recyclerview适配器
 */

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private List<UserItem> mUserItem;
    private MyItemClickListener mItemClickListener;


    public UserAdapter(List<UserItem> userItemList) {
        mUserItem = userItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);

        //将全局的监听传递给holder
        ViewHolder holder = new ViewHolder(view, mItemClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserItem userItem = mUserItem.get(position);
        holder.useritemimg.setImageResource(userItem.getImageId());
        holder.useritemtxt.setText(userItem.getUserItemtxt());
    }

    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mUserItem.size();
    }

    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View useritemView;
        ImageView useritemimg;
        TextView useritemtxt;
        private MyItemClickListener mListener;


        public ViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            useritemView = itemView;
            useritemimg = (ImageView) itemView.findViewById(R.id.user_image_item);
            useritemtxt = (TextView) itemView.findViewById(R.id.user_item);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
        }

        /**
         * 实现OnClickListener接口重写的方法
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }
}
