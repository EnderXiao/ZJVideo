package com.example.trtc_client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
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
        data.add(msg);
    }

    @Override
    public void onResume() {
        super.onResume();
        getChatlv().setSelection(getChatlv().getBottom());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        Switch sw = view.findViewById(R.id.stopchat);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity activity = (MainActivity) getActivity();
                activity.stopAllchat(isChecked);
            }
        });

        EditText edtext = view.findViewById(R.id.inputtext);
        Button subtext = view.findViewById(R.id.subtext);
        subtext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtext.getText().length()==0){
                    //????????????????????????
                    edtext.setHint("??????????????????????????????");
                }else {
                    //????????????
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Chat_Msg msg = new Chat_Msg(MainActivity.userId,sdf.format(new Date()),edtext.getText().toString(),1);  //type 1 ?????????  2  ?????????
                    //???????????????
                    MainActivity activity = (MainActivity) getActivity();
                    activity.sendMsg(msg);//activity????????????
                    //??????????????????
                    data.add(msg);
                    chatlv.setSelection(chatlv.getBottom());
                    //???????????????
                    edtext.setText("");
                    edtext.setHint("????????????????????????");
                }

            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


}
