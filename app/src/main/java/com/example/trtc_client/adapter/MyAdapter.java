package com.example.trtc_client.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.trtc_client.R;

import java.util.List;

public class MyAdapter extends BaseAdapter {
    private Context mContext;  //上下文
    private List<String> mData;  //listview的数据
    private int mSelect = 0;  //当前选中的index

    public MyAdapter() {
    }

    public MyAdapter(List<String> mData, Context mContext , int mSelect) {
        this.mData = mData;
        this.mContext = mContext;
        this.mSelect = mSelect;
    }

    public void changeSelected(int positon){ //刷新方法
       if(positon != mSelect){
            mSelect = positon;
            notifyDataSetChanged();
       }
    }

    public int getmSelect(){
        return mSelect;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.classlist_item, parent, false);
            holder = new ViewHolder();
            holder.txt_classname = (TextView) convertView.findViewById(R.id.classname);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(mSelect == position){  //选中的item样式
            holder.txt_classname.setBackgroundColor(Color.parseColor("#007947"));
            holder.txt_classname.setTextColor(Color.parseColor("#FFFFFF"));
        }else{
            holder.txt_classname.setBackgroundColor(Color.parseColor("#FFFFFF"));
            holder.txt_classname.setTextColor(Color.parseColor("#828798"));
        }
        holder.txt_classname.setText(mData.get(position));
        return convertView;
    }

    private class ViewHolder {
        TextView txt_classname;
    }
}
