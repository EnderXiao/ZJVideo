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
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HttpActivity extends AnswerActivity {

    private static final String TAG = "xiao";
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
            String roomId =MainActivity.roomid;
            String userId = MainActivity.userId;
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
            // ??????
            bufferedReader.close();
            reader.close();
            inputStream.close();
            httpURLConnection.disconnect();
//            Log.e(TAG, "getHandsUpList: " + buffer.toString());
            try{
                String backLogJsonStr = buffer.toString();
//                String backLogJsonStr = "{\"joinlist\":[{\"value\":\"?????????\",\"key\":\"ming6001\"}],\"ketanglist\":[{\"num\":\"60\",\"value\":\"??????2022????????????\",\"key\":\"4195ketang\"}]}";
                JSONObject jsonObject = stringToJson(backLogJsonStr);
                String userListStr = jsonObject.getString("raiseHandUserId");
                String splitter = ",";

                String[] handsUpIdList = userListStr.split(splitter);
                List<MemberDataBean> tempHandsUpList = new ArrayList<>();
                for (int i = 0; i < handsUpIdList.length; i++) {
                    if(handsUpIdList[i].equals("")) continue;
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
                            Log.e(TAG, "getHandsUp: ??????????????????!");
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



    public static void getMemberList(MainActivity mainActivity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String roomId =MainActivity.roomid;
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

                    // ??????
                    bufferedReader.close();
                    reader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    Log.e(TAG, "getMemberList: " + buffer.toString());
                    try{
                        String backLogJsonStr = buffer.toString();
//                        String backLogJsonStr = "{\"joinlist\":[{\"value\":\"?????????\",\"key\":\"ming6001\"}],\"ketanglist\":[{\"num\":\"60\",\"value\":\"??????2022????????????\",\"key\":\"4195ketang\"}]}";
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
                        Message msg = new Message();
                        msg.what = 7;
                        mainActivity.handler.sendMessage(msg);
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

    /**
     *
     *
     * @param micAction    ???????????????
     * @param cameraAction ???????????????
     * @param wordAction   ???????????????
     * @param handAction   ????????????
     * @param userId       ????????????ID
     * @param position     ????????????
     * @param mainActivity ?????????
     */

    public static void memberController(String micAction, String cameraAction, String wordAction, String handAction, String userId, int position, MainActivity mainActivity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String roomId = roomid;
                    if(!userId.equals("")) {
                        roomId += "@_@" + userId;
                    }
                    URL url = new URL(baseUrl + "/ShopGoods/ajax/livePlay_saveControl.do?"
                            + "roomId=" + roomId
                            + "&deviceMicAction=" + micAction
                            + "&deviceCameraAction=" + cameraAction
                            + "&deviceWordsAction=" + wordAction
                            + "&handAction=" + handAction
                    );
                    Log.e(TAG, "memberController: getUrl  " + url );
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

                    while((temp = bufferedReader.readLine()) != null) {
                        buffer.append(temp);
                    }

                    // ??????
                    bufferedReader.close();
                    reader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    try{
                        Log.e(TAG, "memberController: getRespond  " + buffer.toString() );
                        String backLogJsonStr = buffer.toString();
//                        String backLogJsonStr = "{\"joinlist\":[{\"value\":\"?????????\",\"key\":\"ming6001\"}],\"ketanglist\":[{\"num\":\"60\",\"value\":\"??????2022????????????\",\"key\":\"4195ketang\"}]}";
                        JSONObject jsonObject = stringToJson(backLogJsonStr);
                        String success = "";
                        success = jsonObject.getString("success");
                        if(!success.equals("")) {
                            Message msg = Message.obtain();
                            if(!micAction.equals(""))
                                msg.what = 2;
                            else if (!wordAction.equals(""))
                                msg.what = 3;
                            else if (!cameraAction.equals(""))
                                msg.what = 4;
                            Bundle bundle = new Bundle();
                            if(position != -1)
                                bundle.putInt("position", position);
                            msg.setData(bundle);
                            mainActivity.handler.sendMessage(msg);
                        } else {
                            Toast.makeText(mainActivity, "memberController: ????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "memberController: " + e.toString() );
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "memberController: " + e.toString() );
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * ??????????????????
     *
     * @param userId       ????????????ID
     * @param stuName      ?????????????????????
     * @param type         ????????????
     * @param position     ????????????
     * @param mainActivity ?????????
     */

    public static void speakerController(String userId, String stuName, String type, int position, MainActivity mainActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String roomId = roomid;
                    URL url = new URL(baseUrl + "/ShopGoods/ajax/livePlay_savePlatform.do?"
                            + "roomId=" + roomId
                            + "&stuId=" + userId
                            + "&name=" + URLEncoder.encode(stuName, "utf-8")
                            + "&type=" + type
                    );
                    Log.e(TAG, "speakerController: " +  url);

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

                    while((temp = bufferedReader.readLine()) != null) {
                        buffer.append(temp);
                    }

                    // ??????
                    bufferedReader.close();
                    reader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    Log.e(TAG, "speakerController: " +  buffer.toString());
                    try{
                        String backLogJsonStr = buffer.toString();
//                        String backLogJsonStr = "{\"joinlist\":[{\"value\":\"?????????\",\"key\":\"ming6001\"}],\"ketanglist\":[{\"num\":\"60\",\"value\":\"??????2022????????????\",\"key\":\"4195ketang\"}]}";
                        JSONObject jsonObject = stringToJson(backLogJsonStr);
                        String success = "";
                        success = jsonObject.getString("success");
                        if(!success.equals("")) {
                            Message msg = Message.obtain();
                            msg.what = 5;
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", position);
                            msg.setData(bundle);
                            mainActivity.handler.sendMessage(msg);
                        } else {
                            Toast.makeText(mainActivity, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void initClass(int screenWidth, int screenHeight, String source, MainActivity mainActivity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String roomId = MainActivity.roomid;
                    String userId = MainActivity.userId;
                    String userName = MainActivity.userName;
                    String keTangId = MainActivity.keTangId;
                    String keTangName = MainActivity.keTangName;
                    URL url = new URL(baseUrl + "/ShopGoods/ajax/livePlay_deleteMemcached.do?"
                            + "roomId=" + roomId
                            + "&keTangId=" + keTangId
                            + "&keTangName=" + URLEncoder.encode(keTangName, "utf-8")
                            + "&userId=" + userId
                            + "&name=" + URLEncoder.encode(userName, "utf-8")
                            + "&source=" + source
                            + "&width=" + screenWidth
                            + "&height=" + screenHeight
                    );

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

                    while((temp = bufferedReader.readLine()) != null) {
                        buffer.append(temp);
                    }

                    // ??????
                    bufferedReader.close();
                    reader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    Log.e(TAG, "audioController: " +  buffer.toString());
                    try{
                        String backLogJsonStr = buffer.toString();
//                        String backLogJsonStr = "{\"joinlist\":[{\"value\":\"?????????\",\"key\":\"ming6001\"}],\"ketanglist\":[{\"num\":\"60\",\"value\":\"??????2022????????????\",\"key\":\"4195ketang\"}]}";
                        JSONObject jsonObject = stringToJson(backLogJsonStr);
                        String success = "";
                        success = jsonObject.getString("success");
                        if(!success.equals("")) {
                            Toast.makeText(mainActivity, "????????????????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mainActivity, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void overClass(String type, String source, MainActivity mainActivity){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String roomId = MainActivity.roomid;
                    String userId = MainActivity.userId;
                    String userName = MainActivity.userName;
                    String keTangId = MainActivity.keTangId;
                    String keTangName = MainActivity.keTangName;
                    URL url = new URL(baseUrl + "/ShopGoods/ajax/livePlay_hostJoinOrLeaveRoom.do?"
                            + "roomId=" + roomId
                            + "&keTangId=" + keTangId
                            + "&keTangName=" + URLEncoder.encode(keTangName, "utf-8")
                            + "&userId=" + userId
                            + "&name=" + URLEncoder.encode(userName, "utf-8")
                            + "&type=" + type
                            + "&source=" + source
                    );

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

                    while((temp = bufferedReader.readLine()) != null) {
                        buffer.append(temp);
                    }

                    // ??????
                    bufferedReader.close();
                    reader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    Log.e(TAG, "audioController: " +  buffer.toString());
                    try{
                        String backLogJsonStr = buffer.toString();
//                        String backLogJsonStr = "{\"joinlist\":[{\"value\":\"?????????\",\"key\":\"ming6001\"}],\"ketanglist\":[{\"num\":\"60\",\"value\":\"??????2022????????????\",\"key\":\"4195ketang\"}]}";
                        JSONObject jsonObject = stringToJson(backLogJsonStr);
                        String success = "";
                        success = jsonObject.getString("success");
                        if(!success.equals("")) {
                            Toast.makeText(mainActivity, "?????????????????????", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mainActivity, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}

