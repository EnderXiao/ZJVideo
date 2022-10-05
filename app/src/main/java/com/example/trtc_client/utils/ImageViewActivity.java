package com.example.trtc_client.utils;


import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.trtc_client.R;

public class ImageViewActivity extends AppCompatActivity {
    private static ImageView msg_head;
    public static void setHead_url(String head_url) {
        Head_url = head_url;
    }
    private static String Head_url="http://www.cn901.com/res/avatar/2022/07/21/avatar-mingming_173040431.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_response);
        msg_head=findViewById(R.id.msg_head);
        Glide.with(this).load(Head_url).into(msg_head);
    }
}