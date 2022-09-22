package com.example.trtc_client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.trtc_client.MemberItem;
import com.example.trtc_client.R;

import org.w3c.dom.Text;

import java.util.List;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    List<MemberItem> list;


    public ListViewAdapter(Context context, List<MemberItem> models) {
        super();
        this.context = context;
        this.list = models;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(R.layout.member_list_item, parent,false);
            viewHold.textView1 = (TextView) convertView.findViewById(R.id.right_item_textview0);
            viewHold.textView2 = (TextView) convertView.findViewById(R.id.right_item_textview1);
            viewHold.textView3 = (TextView) convertView.findViewById(R.id.right_item_textview2);
            viewHold.textView4 = (TextView) convertView.findViewById(R.id.right_item_textview3);
            viewHold.textView5 = (TextView) convertView.findViewById(R.id.right_item_textview4);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        MemberItem stock = (MemberItem) getItem(position);
        viewHold.textView1.setText(stock.getItem1());
        viewHold.textView2.setText(stock.getItem2());
        viewHold.textView3.setText(stock.getItem3());
        viewHold.textView4.setText(stock.getItem4());
        viewHold.textView5.setText(stock.getItem5());



        return convertView;
    }

    static class ViewHold {

        TextView  textView1, textView2, textView3, textView4, textView5;

    }
}
