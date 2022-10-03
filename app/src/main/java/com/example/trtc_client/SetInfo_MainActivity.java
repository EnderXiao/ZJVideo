package com.example.trtc_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SetInfo_MainActivity extends AppCompatActivity {

    private EditText userid,roomid,subjectid;
    private Button login;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_info_main);
        userid= findViewById(R.id.userid);
        roomid= findViewById(R.id.roomid);
        roomid.setHint("房间号必须为数字");
        subjectid= findViewById(R.id.subjectid);
        login= findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SetInfo_MainActivity.this,MainActivity.class);
                intent.putExtra("userid",userid.getText().toString());
                intent.putExtra("roomid",roomid.getText().toString());
                intent.putExtra("subjectid",subjectid.getText().toString());
                if(userid.getText().length()>0&&roomid.getText().length()>0&&subjectid.getText().length()>0){
                    startActivity(intent);
                }

            }
        });
    }

}