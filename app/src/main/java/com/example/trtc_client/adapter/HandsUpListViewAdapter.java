package com.example.trtc_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.trtc_client.HandsUpItem;
import com.example.trtc_client.MemberDataBean;
import com.example.trtc_client.MemberItem;
import com.example.trtc_client.R;

import java.util.List;

public class HandsUpListViewAdapter extends BaseAdapter {

    private ListView handsUpList;
    private List<HandsUpItem> list;
    private LayoutInflater inflater;
    private onSpeakerControllerListener mOnSpeakerControllerListener;

    public HandsUpListViewAdapter(Context context, ListView handsUpList, List<HandsUpItem> list) {
        this.list = list;
        this.handsUpList = handsUpList;
        inflater = LayoutInflater.from(context);
    }

    public interface onSpeakerControllerListener {
        void onSpeakControllerClick(int i);
    }

    public void setOnSpeakerControllerClickListener(onSpeakerControllerListener mOnSpeakerControllerListener) {
        this.mOnSpeakerControllerListener = mOnSpeakerControllerListener;
    }

    @Override
    public int getCount() {
        if(list != null){
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if(list != null){
            return list.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        HandsUpItem item = (HandsUpItem) this.getItem(i);
        ViewHolder viewHolder;

        if(view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.hands_up_list_item, null);
            viewHolder.userName = (TextView) view.findViewById(R.id.hand_up_list_user_name);
            viewHolder.speakerController = (ImageView) view.findViewById(R.id.hands_up_speaker_controller);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // 点击事件
        viewHolder.speakerController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnSpeakerControllerListener.onSpeakControllerClick(i);
            }
        });

        viewHolder.userName.setText(item.getName());
        viewHolder.userName.setTextSize(13);
        return view;
    }

    public static class ViewHolder{
        public TextView userName;
        public ImageView speakerController;
    }

}
