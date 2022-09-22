package com.example.trtc_client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.trtc_client.adapter.ListViewAdapter;
import com.tencent.trtc.TRTCCloud;

import java.util.ArrayList;
import java.util.List;

public class MemberListFragment extends Fragment {

    private ListView container;
    private List<MemberItem> memberList;

    private MyHorizontalScrollView titleScroll;
    private MyHorizontalScrollView contentScroll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_list, container, false);
        findView(view);
        initData();
        initView();
        return view;
    }

    private void findView(View view) {
        titleScroll = view.findViewById(R.id.title_horsv);
        contentScroll = view.findViewById(R.id.content_horsv);
        container = view.findViewById(R.id.right_container_listview);
    }

    private void initView() {
        // 设置两个水平控件的联动
        titleScroll.setScrollView(contentScroll);
        contentScroll.setScrollView(titleScroll);
//        //添加左侧数据
//        ALeftAdapter adapter = new ALeftAdapter(this, leftlList);
//        left_container_listview.setAdapter(adapter);
//        UtilTools.setListViewHeightBasedOnChildren(left_container_listview);


        // 添加右边内容数据
        ListViewAdapter myRightAdapter = new ListViewAdapter(getContext(), memberList);
        container.setAdapter(myRightAdapter);
        UtilTools.setListViewHeightBasedOnChildren(container);
        container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MemberItem stock = (MemberItem) parent.getAdapter().getItem(position);
//                Toast.makeText(MainActivity.this, stock.getName() + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    //初始化数据源
    private void initData() {

        memberList = new ArrayList<>();
        for (int i = 0; i < 40; i++) {
            memberList.add(new MemberItem("MingMing", "111", "222", "333", "444", "555"));
        }
    }
}
