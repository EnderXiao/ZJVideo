package com.example.trtc_client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trtc_client.adapter.ChatMsgAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatRoomFragment extends Fragment {
    private List<Chat_Msg> data= new ArrayList<Chat_Msg>();
    private ChatMsgAdapter chatMsgAdapter;

    public ChatMsgAdapter getChatMsgAdapter() {
        return chatMsgAdapter;
    }

    public void setChatMsgAdapter(ChatMsgAdapter chatMsgAdapter) {
        this.chatMsgAdapter = chatMsgAdapter;
    }

    private ListView chatlv;

    public ListView getChatlv() {
        return chatlv;
    }

    public void setChatlv(ListView chatlv) {
        this.chatlv = chatlv;
    }

    public void setData(Chat_Msg msg){
        System.out.println("+++你好，我是ChatRoomFragment里面的setData函数");
        data.add(msg);
    }

    @Override
    public void onResume() {
        super.onResume();
        getChatlv().setSelection(getChatlv().getBottom());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("+++你好，我是ChatRoomFragment里面的onCreateView函数");
        View view = inflater.inflate(R.layout.chat_room, container, false);
        chatlv =  view.findViewById(R.id.chatlv);
        chatMsgAdapter=new ChatMsgAdapter(view.getContext(), R.layout.item_response,data);
        chatlv.setAdapter(chatMsgAdapter);
        TextView cleanall = view.findViewById(R.id.cleanall);
        cleanall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                chatMsgAdapter.notifyDataSetChanged();
            }
        });
        TextView stopchat = view.findViewById(R.id.stopchat);

        RadioButton radioButton =view.findViewById(R.id.allnochat);
        stopchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) getActivity();
                activity.stopAllchat(!radioButton.isChecked());
            }
        });

        EditText edtext = view.findViewById(R.id.inputtext);
        Button subtext = view.findViewById(R.id.subtext);
        subtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtext.getText().length()==0){
                    //提示请先输入消息
                    edtext.setHint("请先输入要讨论的内容");
                }else {
                    //创建消息
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Chat_Msg msg = new Chat_Msg("hongyi2",sdf.format(new Date()),edtext.getText().toString(),1);
                    //发送给别人
                    MainActivity activity = (MainActivity) getActivity();
                    activity.sendMsg(msg);//activity中的方法

                    //自己这里显示
                    data.add(msg);
                    chatlv.setSelection(chatlv.getBottom());

                    //清空输入框
                    edtext.setText("");
                }

            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("+++你好，我是ChatRoomFragment里面的onCreat函数");

    }


}
