package com.example.trtc_client;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.trtc_client.adapter.HandsUpListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HttpActivity extends AnswerActivity {

    private static Timer getHandsUpTimer;
    private static String baseUrl = "http://www.cn901.com";

    public static void startHandsUpTimer(MainActivity mainActivity) {

        getHandsUpTimer = new Timer();
        getHandsUpTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                getHandsUp(mainActivity);
            }
        },500,100);
    }

    public static void stopHandsUpTimer() {
        getHandsUpTimer.cancel();
    }


    public static JSONObject stringToJson(String str) {
        JSONObject jsonObject = null;
        str = str.substring(str.indexOf("{"), str.lastIndexOf("}") + 1);
        str = str.replace("\\\"","'");
        try {
            jsonObject = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    public static void getHandsUp(MainActivity mainActivity) {
        try{
            String roomId =MainActivity.Roomid;
            String userId = MainActivity.UserId;
            URL url = new URL(baseUrl + "/ShopGoods/ajax/livePlay_getChatRoomMessage.do?"
                    + "roomId=" + roomId
                    + "&userId=" + userId
                    + "&startTime=" + 0);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();
            InputStream inputStream = httpURLConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream, "GBK");
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer buffer = new StringBuffer();
            String temp = null;
            while((temp = bufferedReader.readLine()) != null){
                buffer.append(temp);
            }
            // 关闭
            bufferedReader.close();
            reader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            Log.e(TAG, "getHandsUpList: " + buffer.toString());
            try{
                String backLogJsonStr = buffer.toString();
//                String backLogJsonStr = "{\"joinlist\":[{\"value\":\"李龙龙\",\"key\":\"ming6001\"}],\"ketanglist\":[{\"num\":\"60\",\"value\":\"我校2022级葛舸班\",\"key\":\"4195ketang\"}]}";
                JSONObject jsonObject = stringToJson(backLogJsonStr);
                String userListStr = jsonObject.getString("raiseHandUserId");
                String splitter = ",";
                String[] handsUpIdList = userListStr.split(splitter);
                List<MemberDataBean> tempHandsUpList = new ArrayList<>();
                for (int i = 0; i < handsUpIdList.length; i++) {
                    Log.e(TAG, "getHandsUpList: " + handsUpIdList[i]);
                    StudentDataBean studentMemberInJoinList = findMemberInJoinList(handsUpIdList[i]);
                    if(studentMemberInJoinList != null) {
                        tempHandsUpList.add(studentMemberInJoinList);
                        continue;
                    } else {
                        ClassDataBean classMemberInKetangList = findMemberInKetangList(handsUpIdList[i]);
                        if(classMemberInKetangList != null){
                            tempHandsUpList.add((MemberDataBean) classMemberInKetangList);
                            continue;
                        } else {
                            Log.e(TAG, "getHandsUp: 未找到该用户!");
                        }
                    }
                }
                AnswerActivity.handsUpList = tempHandsUpList;
                Message updateHandsUpListMessage = Message.obtain();
                updateHandsUpListMessage.what = 1;
                mainActivity.handler.sendMessage(updateHandsUpListMessage);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void getMemberList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String roomId =MainActivity.Roomid;
                    URL url = new URL(baseUrl + "/ShopGoods/ajax/livePlay_TestGetStuJoinOrLeaveRoomList.do?"
                            + "roomId=" + roomId);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(8000);
                    httpURLConnection.setReadTimeout(8000);
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    InputStreamReader reader = new InputStreamReader(inputStream, "GBK");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuffer buffer = new StringBuffer();
                    String temp = null;

                    while((temp = bufferedReader.readLine()) != null){
                        buffer.append(temp);
                    }

                    // 关闭
                    bufferedReader.close();
                    reader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    Log.e(TAG, "getMemberList: " + buffer.toString());
                    try{
//                        String backLogJsonStr = buffer.toString();
                        String backLogJsonStr = "{\"joinlist\":[{\"value\":\"李龙龙\",\"key\":\"ming6001\"}],\"ketanglist\":[{\"num\":\"60\",\"value\":\"我校2022级葛舸班\",\"key\":\"4195ketang\"}]}";
                        JSONObject jsonObject = stringToJson(backLogJsonStr);
                        JSONArray joinListJsonArray = jsonObject.getJSONArray("joinlist");
                        JSONArray ketangListJsonArray = jsonObject.getJSONArray("ketanglist");
                        List<StudentDataBean> tempJoinList = new ArrayList<>();
                        List<ClassDataBean> tempKetangList = new ArrayList<>();
                        for (int i=0; i < joinListJsonArray.length(); i ++) {
                            JSONObject jsonObj = joinListJsonArray.getJSONObject(i);
                            StudentDataBean studentDataBean = new StudentDataBean(jsonObj.getString("value"), jsonObj.getString("key"));
                            tempJoinList.add(studentDataBean);
                        }
                        for (int i=0; i < ketangListJsonArray.length(); i ++) {
                            JSONObject jsonObj = ketangListJsonArray.getJSONObject(i);
                            ClassDataBean classDataBean = new ClassDataBean(jsonObj.getString("value"), jsonObj.getString("key"), jsonObj.getInt("num"));
                            tempKetangList.add(classDataBean);
                        }
                        joinList = tempJoinList;
                        ketangList = tempKetangList;
                        Log.e(TAG, "getMemberList: " + jsonObject);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void audioController(String action) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String roomId = MainActivity.Roomid;
                    URL url = new URL(baseUrl + "/ShopGoods/ajax/livePlay_saveControl.do?"
                            + "roomId=" + roomId
                            + "deviceMicAction=" + action
                            + "deviceCameraAction="
                            + "deviceWordsAction="
                            + "handAction="
                    );
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}

