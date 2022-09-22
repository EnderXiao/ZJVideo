package com.example.trtc_client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trtc_client.adapter.TabBarAdapter;
import com.google.android.material.tabs.TabLayout;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMGroupMemberInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMSimpleMsgListener;
import com.tencent.imsdk.v2.V2TIMUserInfo;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.teduboard.TEduBoardController;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.tencent.trtc.debug.GenerateTestUserSig;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //Tabbar三个Fragment
    private List<Fragment> mFragmenglist = new ArrayList<>();
    public List<Fragment> getmFragmenglist() {
        return mFragmenglist;
    }
    public void setmFragmenglist(List<Fragment> mFragmenglist) {this.mFragmenglist = mFragmenglist;}
//    private final String[] tabs = {"视频列表", "聊天讨论", "互动答题"};

    public final static String TAG = "Ender_MainActivity";
    private TRTCCloud mTRTCCloud;
    private TRTCCloudDef.TRTCParams myTRTCParams;
    private TXCloudVideoView mTXCVVTeacherPreviewView;
    private RelativeLayout teacherTRTCBackground;
    private ImageView boardBtn;
    private ImageView canvasBtn;
    private ImageView contentBtn;
    private ImageView memberBtn;
    private ImageView handBtn;
    private ImageView cameraBtn;
    private ImageView audioBtn;
    private TextView teacher_name_view;

    public static String mTeacherId;

    private boolean musicOn = true;
    private boolean cameraOn = true;

    // 监听用户进入房间
    public static ArrayList<String> mUserList = new ArrayList<String>();
    public static ArrayList<String> mCameraUserList = new ArrayList<String>();
    public static ArrayList<String> mMicrophoneUserList = new ArrayList<String>();
    public static int mUserCount = 0;


    private  String UserId = "hongyi";
    private  String UserSig = "eJwtzMsOgjAURdF-6diQS180JM5wYogTH3NiW7g0YAMEi8Z-F7HDs06y3*RSnpPZDCQnNAGy2zZq009ocePm0dcLxmfUrvIeNclTDiBTpRj8HxM8DmZ1IQQFiDph9zMpWcY5ZzRWsF7DT7fQonRZw07FUjFwVzOH49jeEahxXW91c7DpLfiX2pPPF4cTMsg_";
    //private  String UserSig = "eJyrVgrxCdYrSy1SslIy0jNQ0gHzM1NS80oy0zLBwhn5eemVmYZQqeKU7MSCgswUJStDEwMDM0MLC2MDiExqRUFmUSpQ3NTU1MjAACpakpkLEjMzMzY3AQKoaHFmOtDk8AJH70LPYkN-Y*1w11y3EHdLH9fgLE-X-Arf8OCqlNBgIw-PIA9PL9OMQFulWgCKxDFd";
    //private  String UserSig = "eJwtzE8LgjAcxvH3snPINrc5hA4FHfpDmOWhY7TpfohrLbVF9N4z8-h8Hvi*0Wl3jHrtUYpohNFs3KC0baGEkc3NVi*g0-VQ9cU5UCglDGNBpIzx-9HBgdeDc84pxpO20PxMiDhhjHE6VaAayudyv*mSA*-q7dX1RaaCaaxc*-syN6tFFp69zXOqNSXFHH2*tlYy5A__";
    private  String Roomid  = "911";
    private  int SDKappID =1400618830;

    //即时通信相关
    private V2TIMManager v2TIMManager;
        //选择左侧工具相关
    private Boolean menu_l_status=true; //true 代表展开
    private ImageButton menu01,menu02,menu03,menu04,menu05,menu06,menu07,menu08,menu09,menu10,menu11,menu12;
        //底部按钮
    private Boolean menu_b_status=true; //true 代表展开
    private TextView b_size,b_cur,b_sum,b_chu,b_per;
    private ImageButton menub01,menub02,menub03,menub04,menub05,menub06,menub07,menub08,menub09,menub10,menub11;
        //聊天消息列表
    private List<Chat_Msg> data = new ArrayList<>();

    //互动白板相关
    private View boardview;
    private TEduBoardController mBoard;
    private Boolean BoardStatus=false;
    private TEduBoardController.TEduBoardCallback mBoardCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ChatRoomFragment chatRoomFragment=new ChatRoomFragment();
        VideoListFragment videoListFragment = new VideoListFragment();
        AnswerQuestionFragment answerQuestionFragment = new AnswerQuestionFragment();
        mFragmenglist.add(videoListFragment);
        mFragmenglist.add(chatRoomFragment);
        mFragmenglist.add(answerQuestionFragment);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView class_id_text_view = findViewById(R.id.class_id);
        @SuppressLint("UseCompatLoadingForDrawables") Drawable class_id_icon = getResources().getDrawable(R.drawable.copy);
        class_id_icon.setBounds(0,0,15,15);
        class_id_text_view.setCompoundDrawables(null, null, class_id_icon,null);


        // 获取CameraView
        mTXCVVTeacherPreviewView = findViewById(R.id.teacher_camera);
        teacherTRTCBackground = findViewById(R.id.teacher_background);

        // 获取底部按钮
        canvasBtn = findViewById(R.id.canvas_btn);
        boardBtn = findViewById(R.id.board_btn);
        contentBtn = findViewById(R.id.content_btn);
        memberBtn = findViewById(R.id.member_btn);
        handBtn = findViewById(R.id.hand_btn);
        audioBtn = findViewById(R.id.mic_btn);
        cameraBtn = findViewById(R.id.camera_btn);
        teacher_name_view = findViewById(R.id.teacher_name);

        boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBoard!=null&&BoardStatus){
                    FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
                    FrameLayout container = findViewById(R.id.teachingcontent);
                    container.addView(boardview, layoutParams);
                    RelativeLayout rf_leftmenu = findViewById(R.id.menu_left);
                    rf_leftmenu.setVisibility(View.VISIBLE);
                    RelativeLayout rf_bottommenu = findViewById(R.id.menu_bottom);
                    rf_bottommenu.setVisibility(View.VISIBLE);
                    RelativeLayout rf_shoukeneirong = findViewById(R.id.bg_shoukeneirong);
                    rf_shoukeneirong.setVisibility(View.GONE);
                }
            }
        });

        initTIM();

        initTabBarNavigation();
        enterLiveRoom();
    }

    public void initTabBarNavigation() {

        //使用适配器将ViewPager与Fragment绑定在一起
        ViewPager viewPager = findViewById(R.id.tar_bar_view_page);
        TabBarAdapter tabBarAdapter = new TabBarAdapter(getSupportFragmentManager());
        tabBarAdapter.setmFragment(mFragmenglist);
        viewPager.setAdapter(tabBarAdapter);

        //将TabLayout与ViewPager绑定在一起
        TabLayout mTabLayout = findViewById(R.id.tab_bar_table_layout);
        mTabLayout.setupWithViewPager(viewPager);

        //指定Tab的位置
        TabLayout.Tab videoList = mTabLayout.getTabAt(0);
        TabLayout.Tab chatRoom = mTabLayout.getTabAt(1);
        TabLayout.Tab answerQuestion = mTabLayout.getTabAt(2);
    }


    // 退出房间
    public static void exitRoom() {}

    public static class MyTRTCCloudListener extends TRTCCloudListener {
        private WeakReference<MainActivity> mContext;

        public MyTRTCCloudListener(MainActivity activity) {
            super();
            mContext = new WeakReference<>(activity);
        }

        @Override
        public void onEnterRoom(long result) {
            MainActivity activity = mContext.get();
            if(result > 0) {
                Log.e(TAG, "onEnterRoom: 进入房间成功，耗时: " + result);
                Toast.makeText(activity, "进入房间成功，耗时: " + "[" + result+ "]" , Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "onEnterRoom: 进入房间失败，错误代码：" + result);
                Toast.makeText(activity, "进入房间失败，错误代码: " + "[" + result+ "]" , Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            MainActivity activity = mContext.get();
            Log.d(TAG, "onUserVideoAvailable userId " + userId + ", mUserCount " + mUserCount + ",available " + available);
            Toast.makeText(activity, "onUserVideoAvailable userId " + userId + ", mUserCount " + mUserCount + ",available " + available , Toast.LENGTH_SHORT).show();
            System.out.println("onUserVideoAvailable userId " + userId + ", mUserCount " + mUserCount + ",available " + available);
            int index = mUserList.indexOf(userId);
            System.out.println("onUserVideoAvailable:"+userId);
//            if (userId.equals(mTeacherId+"_camera")&&!available){
//                System.out.println("mingming_camera exit room");
//                exitRoom();
//                teacher_enable=false;
//                return;
//            }
            if (available) {
                if (index != -1) {
                    return;
                }
                mUserList.add(userId);
//                refreshRemoteVideoViews();
            } else {
                if (index == -1) {
                    return;
                }
                mUserList.remove(index);
//                refreshRemoteVideoViews();
            }
            for(int i =0;i<mUserList.size();i++){
                System.out.println(mUserList.get(i)+" : "+mUserList.get(i));
            }

        }

        @Override
        public void onRemoteUserEnterRoom(String userId){
            VideoListFragment videoListFragment = new VideoListFragment();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("mUserList", mUserList);
            videoListFragment.setArguments(bundle);
            videoListFragment.RefreshingCameraView(mUserList);
            MainActivity activity = mContext.get();
            Log.e(TAG, "onRemoteUserEnterRoom: userId" + userId );
            System.out.println("onRemoteUserEnterRoom userId " + userId );
            Toast.makeText(activity, "onRemoteUserEnterRoom userId " + userId , Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onRemoteUserLeaveRoom(String userId, int reason){
            MainActivity activity = mContext.get();
            System.out.println("onRemoteUserLeaveRoom userId " + userId );
            Toast.makeText(activity, "onRemoteUserLeaveRoom userId " + userId , Toast.LENGTH_SHORT).show();
            if (userId.equals(mTeacherId+"_camera")){
                System.out.println("mingming_camera exit room");
                exitRoom();
//                teacher_enable=false;
            }
        }

        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
            MainActivity activity = mContext.get();
            if (activity != null) {
                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode+ "]" , Toast.LENGTH_SHORT).show();
                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
                    activity.exitRoom();
                }
            }
        }
    }

    public void enterLiveRoom() {

//        Log.e(TAG, "enterLiveRoom: ");

        mTRTCCloud = TRTCCloud.sharedInstance(getApplicationContext());
        mTRTCCloud.setListener(new MyTRTCCloudListener(MainActivity.this));

        // 组装TRTC进房参数
//        String userId = "mingming";
        myTRTCParams = new TRTCCloudDef.TRTCParams();
        myTRTCParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        myTRTCParams.userId = UserId;
        myTRTCParams.roomId = Integer.parseInt(Roomid);
        myTRTCParams.userSig = GenerateTestUserSig.genTestUserSig(myTRTCParams.userId);
//        myTRTCParams.userSig = "eJwtzMEKgkAUheF3mXXI9eoMKbTQiFoE4WQQ7dSZ4jY0mVoa0btn6vJ8B-4PS7d756UrFjJ0gM2GTUrbhs40cEfZHdzpqZXJypIUC10fQCAEyMdHdyVVunfOOQLAqA3d-iYE*p6HPJgqdOnD-ukooyQNjELZbeQhf7ytKAp7Xc7XBmFXJzxvoyes4nbBvj8x1DFE";


        // 设置订阅模式，SDK将默认不拉取远端的用户声音，但会拉取远端用户视频
        mTRTCCloud.setDefaultStreamRecvMode(false,true);
        // 进入房间
         mTRTCCloud.enterRoom(myTRTCParams, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);


        // 开启音量检测
        mTRTCCloud.enableAudioVolumeEvaluation(300,true);

        // 发布音视频流

        // 设置本地画面的预览模式：设置画面为填充；开启左右镜像
        TRTCCloudDef.TRTCRenderParams myTRTCRenderParams = new TRTCCloudDef.TRTCRenderParams();
        myTRTCRenderParams.fillMode = TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FILL;
        myTRTCRenderParams.mirrorType = TRTCCloudDef.TRTC_VIDEO_MIRROR_TYPE_AUTO;
        mTRTCCloud.setLocalRenderParams(myTRTCRenderParams);

        // 开启本地摄像头预览
        mTRTCCloud.startLocalPreview(true, mTXCVVTeacherPreviewView);
        cameraOn = true;


        // 开启本地麦克风
        mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_SPEECH);
        musicOn = true;

        teacherTRTCBackground.setVisibility(View.INVISIBLE);
//        teacherTRTCBackground.bringToFront();

        // 设置姓名旁的静音标记

        @SuppressLint("UseCompatLoadingForDrawables") Drawable teacher_name_mic_icon = getResources().getDrawable(R.drawable.mic_on);
        teacher_name_mic_icon.setBounds(0,0,20,20);
        teacher_name_view.setCompoundDrawables(teacher_name_mic_icon, null, null, null);


    }


    public void switchCamera(View view) {
        Log.e(TAG, "switchCamera: switchCamera" );
        if(cameraOn) {
            Log.e(TAG, "switchCamera: close");
            mTRTCCloud.stopLocalPreview();
            teacherTRTCBackground.setVisibility(View.VISIBLE);
//            mTRTCCloud.muteLocalVideo(TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, true);
            cameraOn = false;
            cameraBtn.getDrawable().setLevel(10);
        } else {
            Log.e(TAG, "switchCamera: open");
            mTRTCCloud.startLocalPreview(true, mTXCVVTeacherPreviewView);
            teacherTRTCBackground.setVisibility(View.INVISIBLE);
//            mTRTCCloud.muteLocalVideo(TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_BIG, false);
            cameraOn = true;
            cameraBtn.getDrawable().setLevel(5);
        }
    }

    public void switchMusic(View view) {
        if(musicOn) {
            mTRTCCloud.stopLocalAudio();
            musicOn = false;
            audioBtn.getDrawable().setLevel(10);

            // 设置图标
            @SuppressLint("UseCompatLoadingForDrawables") Drawable teacher_name_mic_icon = getResources().getDrawable(R.drawable.mic_off);
            teacher_name_mic_icon.setBounds(0,0,20,20);
            teacher_name_view.setCompoundDrawables(teacher_name_mic_icon, null, null, null);
        } else {
            mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_SPEECH);
            musicOn = true;
            audioBtn.getDrawable().setLevel(5);

            // 设置图标
            @SuppressLint("UseCompatLoadingForDrawables") Drawable teacher_name_mic_icon = getResources().getDrawable(R.drawable.mic_on);
            teacher_name_mic_icon.setBounds(0,0,20,20);
            teacher_name_view.setCompoundDrawables(teacher_name_mic_icon, null, null, null);
        }
    }

    public void sentToVideoList(Bundle bundle) {
        VideoListFragment videoListFragment = new VideoListFragment();
    }

    public void showMemberListBtn(View view) {
        View popupView = getLayoutInflater().inflate(R.layout.member_list_pop_window, null);
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int offsetX = Math.abs(popupWindow.getContentView().getMeasuredWidth() - view.getWidth())/2;
        int offsetY = -1000;
        popupWindow.showAsDropDown(view, offsetX, offsetY, Gravity.START);

    }

    //初始化白板
    public void initBoard(){
        // 创建并初始化白板控制器
        //（1）鉴权配置
        System.out.println("+++开始初始化白板");
        TEduBoardController.TEduBoardAuthParam authParam = new TEduBoardController.TEduBoardAuthParam(
                SDKappID , UserId, UserSig);
        //（2）白板默认配置
        TEduBoardController.TEduBoardInitParam initParam = new TEduBoardController.TEduBoardInitParam();
        initParam.timSync=false;

        mBoard = new TEduBoardController(this);
        //（3）添加白板事件回调 实现TEduBoardCallback接口
        mBoardCallback = new TEduBoardController.TEduBoardCallback(){
            @Override
            public void onTEBError(int code, String msg) {
                System.out.println("onTEBError"+"+++++++++++++code"+code+msg);
            }
            @Override
            public void onTEBWarning(int code, String msg) {
                System.out.println("onTEBWarning"+"+++++++++++++code:"+code+msg);
            }
            @Override
            public void onTEBInit() {
                System.out.println("onTEBInit"+"+++++++++++++");
                BoardStatus=true;
//                b_cur.setText(mBoard.getBoardList().indexOf(mBoard.getCurrentBoard())+1);
//                b_sum.setText(mBoard.getBoardList().size());
            }
            @Override
            public void onTEBHistroyDataSyncCompleted() {
                System.out.println("onTEBHistroyDataSyncCompleted"+"+++++++++++++");
                if(mBoard!=null&&mBoard.getBoardList().size()>0){
                    b_cur.setText((mBoard.getBoardList().indexOf(mBoard.getCurrentBoard())+1)+"");
                    b_sum.setText(mBoard.getBoardList().size()+"");
                }else {
                    b_cur.setText("1");
                    b_sum.setText("1");
                }
            }
            @Override
            public void onTEBSyncData(String data) {
                final V2TIMMessage message = V2TIMManager.getMessageManager().createCustomMessage(data.getBytes(), "", "TXWhiteBoardExt".getBytes());
                if (message.getCustomElem() != null) {
                    message.getCustomElem().setExtension("TXWhiteBoardExt".getBytes());
                }
                V2TIMManager.getInstance().getConversationManager().getConversation(Roomid, new V2TIMValueCallback<V2TIMConversation>() {
                    @Override
                    public void onError(int i, String s) {
                        // 获取回话失败
                        System.out.println("+++获取会话失败");
                    }
                    @Override
                    public void onSuccess(V2TIMConversation v2TIMConversation) {
                        V2TIMManager.getInstance().getMessageManager().sendMessage(message, null, Roomid, 1, false, null,  new V2TIMSendCallback<V2TIMMessage>() {
                            @Override
                            public void onSuccess(V2TIMMessage v2TIMMessage) {
                                // 发送 IM 消息成功
//                                System.out.println("+++发送 IM 消息成功");
                            }
                            @Override
                            public void onError(int i, String s) {
                                // 发送 IM 消息失败，建议进行重试
                                System.out.println("+++发送 IM 消息失败，建议进行重试"+s);
                                mBoard.syncAndReload();
                            }
                            @Override
                            public void onProgress(int i) {
                                System.out.println("+++发送中onProgress"+i);
                            }
                        });
                    }
                });
            }

            @Override
            public void onTEBUndoStatusChanged(boolean canUndo) {
                System.out.println("onTEBUndoStatusChanged"+"+++"+canUndo);
            }

            @Override
            public void onTEBRedoStatusChanged(boolean canRedo) {
                System.out.println("onTEBRedoStatusChanged"+"++++++"+canRedo);
            }

            @Override
            public void onTEBImageStatusChanged(String boardId, String url, int status) {
                System.out.println("onTEBImageStatusChanged"+"+++");
            }

            @Override
            public void onTEBSetBackgroundImage(String url) {
                System.out.println("onTEBSetBackgroundImage"+"+++");
            }

            @Override
            public void onTEBAddImageElement(String url) {
                System.out.println("onTEBAddImageElement"+"+++");
            }

            @Override
            public void onTEBAddElement(String id, int type, String url) {
                System.out.println("onTEBAddElement"+"+++");
            }

            @Override
            public void onTEBDeleteElement(List<String> id) {
                System.out.println("onTEBDeleteElement"+"+++");
            }

            @Override
            public void onTEBSelectElement(List<TEduBoardController.ElementItem> elementItemList) {
                System.out.println("onTEBSelectElement"+"+++");
            }

            @Override
            public void onTEBMathGraphEvent(int code, String boardId, String graphId, String message) {
                System.out.println("onTEBMathGraphEvent"+"+++");
            }

            @Override
            public void onTEBZoomDragStatus(String fid, int scale, int xOffset, int yOffset) {
                System.out.println("onTEBZoomDragStatus"+"++++");
                if( Integer.parseInt(mBoard.getBoardScale()+"")>300){
                    mBoard.setBoardScale(300);
                    b_size.setText("300");
                }else {
                    b_size.setText(mBoard.getBoardScale()+"");
                }

            }

            @Override
            public void onTEBBackgroundH5StatusChanged(String boardId, String url, int status) {
                System.out.println("onTEBBackgroundH5StatusChanged"+"+++");
            }

            @Override
            public void onTEBTextElementWarning(String code, String message) {
                System.out.println("onTEBTextElementWarning"+"++++");
            }

            @Override
            public void onTEBImageElementStatusChanged(int status, String currentBoardId, String imgUrl, String currentImgUrl) {
                System.out.println("onTEBImageElementStatusChanged"+"++");
            }

            @Override
            public void onTEBAddBoard(List<String> boardList, String fileId) {
                System.out.println("onTEBAddBoard"+"++++");
            }

            @Override
            public void onTEBDeleteBoard(List<String> boardList, String fileId) {
                System.out.println("onTEBDeleteBoard"+"+++");
            }

            @Override
            public void onTEBGotoBoard(String boardId, String fileId) {
                System.out.println("onTEBGotoBoard"+"+++++");
                b_cur.setText((mBoard.getBoardList().indexOf(mBoard.getCurrentBoard())+1)+"");

            }

            @Override
            public void onTEBGotoStep(int currentStep, int totalStep) {
                System.out.println("onTEBGotoStep"+"+++++");
            }

            @Override
            public void onTEBRectSelected() {
                System.out.println("onTEBRectSelected"+"++++");
            }

            @Override
            public void onTEBRefresh() {
                System.out.println("onTEBRefresh"+"++++");
            }

            @Override
            public void onTEBOfflineWarning(int count) {
                System.out.println("onTEBOfflineWarning"+"+++");
            }
            @Override
            public void onTEBAddTranscodeFile(String fileId) {
                System.out.println("onTEBAddTranscodeFile"+"+++");
            }
            @Override
            public void onTEBDeleteFile(String fileId) {
                System.out.println("onTEBDeleteFile"+"+++");
            }
            @Override
            public void onTEBSwitchFile(String fileId) {
                System.out.println("onTEBSwitchFile"+"+++");
            }

            @Override
            public void onTEBFileUploadProgress(String path, int currentBytes, int totalBytes, int uploadSpeed, float percent) {
                System.out.println("onTEBFileUploadProgress"+"+++++");
            }

            @Override
            public void onTEBFileUploadStatus(String path, int status, int errorCode, String errorMsg) {
                System.out.println("onTEBFileUploadStatus"+"++++");
            }

            @Override
            public void onTEBFileTranscodeProgress(String file, String errorCode, String errorMsg, TEduBoardController.TEduBoardTranscodeFileResult result) {
                System.out.println("onTEBFileTranscodeProgress"+"+++++");
            }

            @Override
            public void onTEBH5FileStatusChanged(String fileId, int status) {
                System.out.println("onTEBH5FileStatusChanged"+"+++++++");
            }

            @Override
            public void onTEBAddImagesFile(String fileId) {
                System.out.println("onTEBAddImagesFile"+"++++++");
            }

            @Override
            public void onTEBVideoStatusChanged(String fileId, int status, float progress, float duration) {
                System.out.println("onTEBVideoStatusChanged"+"++++++");
            }

            @Override
            public void onTEBAudioStatusChanged(String elementId, int status, float progress, float duration) {
                System.out.println("onTEBAudioStatusChanged"+"+++++");
            }

            @Override
            public void onTEBSnapshot(String path, int code, String msg) {
                System.out.println("onTEBSnapshot"+"++++");
            }

            @Override
            public void onTEBH5PPTStatusChanged(int statusCode, String fid, String describeMsg) {
                System.out.println("onTEBH5PPTStatusChanged"+"+++");
            }

            @Override
            public void onTEBTextElementStatusChange(String status, String id, String value, int left, int top) {
                System.out.println("onTEBTextElementStatusChange"+"+++++");
            }

            @Override
            public void onTEBScrollChanged(String boardId, int trigger, double scrollLeft, double scrollTop, double scale) {
                System.out.println("onTEBScrollChanged"+"+++");
            }

            @Override
            public void onTEBClassGroupStatusChanged(boolean enable, String classGroupId, int operationType, String message) {
                System.out.println("onTEBClassGroupStatusChanged"+"++");
            }

            @Override
            public void onTEBCursorPositionChanged(Point point) {
                System.out.println("onTEBCursorPositionChanged"+"+++");
            }

            @Override
            public void onTEBElementPositionChange(List<TEduBoardController.ElementItem> elementItemList) {
                System.out.println("onTEBElementPositionChange"+"++++");
            }
        };

        mBoard.addCallback(mBoardCallback);
        //（4）进行初始化
        mBoard.init(authParam,  Integer.parseInt(Roomid), initParam);
        //（2）获取白板 View
        boardview = mBoard.getBoardRenderView();
        // 初始化白板的按钮功能
        initBoardMenu();
    }
    public void initTIM(){
        //初始化 IMSDK
        V2TIMSDKConfig timSdkConfig = new V2TIMSDKConfig();
        boolean result = V2TIMManager.getInstance().initSDK(this, SDKappID, timSdkConfig, new V2TIMSDKListener() {
            @Override
            public void onConnecting() {
                super.onConnecting();
                System.out.println("+++onConnecting");
            }
            @Override
            public void onConnectSuccess() {
                super.onConnectSuccess();
                System.out.println("+++onConnectSuccess");
                //初始化成功 登录TIM
                LoginTIM();
            }
            @Override
            public void onConnectFailed(int code, String error) {
                super.onConnectFailed(code, error);
                System.out.println("+++onConnectFailed");
            }

            @Override
            public void onKickedOffline() {
                super.onKickedOffline();
                System.out.println("+++onKickedOffline");
            }

            @Override
            public void onUserSigExpired() {
                super.onUserSigExpired();
                System.out.println("+++onUserSigExpired");
            }
        });

    }
    public void LoginTIM(){
        V2TIMManager.getInstance().login(UserId, UserSig, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                System.out.println("++++++登陆失败"+s);
            }
            @Override
            public void onSuccess() {
                System.out.println("++++++登陆成功");
                //登陆成功  创建群组
                createGroup();
            }
        });
        V2TIMManager.getInstance().addSimpleMsgListener(new V2TIMSimpleMsgListener() {
            @Override
            public void onRecvC2CTextMessage(String msgID, V2TIMUserInfo sender, String text) {
                super.onRecvC2CTextMessage(msgID, sender, text);
                System.out.println("+++onRecvC2CTextMessage"+msgID+sender+text);
            }
            @Override
            public void onRecvC2CCustomMessage(String msgID, V2TIMUserInfo sender, byte[] customData) {
                super.onRecvC2CCustomMessage(msgID, sender, customData);
                System.out.println("+++onRecvC2CCustomMessage"+msgID+sender);
            }
            @Override
            public void onRecvGroupTextMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, String text) {
                super.onRecvGroupTextMessage(msgID, groupID, sender, text);
                System.out.println("+++onRecvGroupTextMessage"+msgID+sender+text);
                try {
                    JSONObject msg = new JSONObject(text);
                    System.out.println("+++JSON"+msg);
                    String msgcontent = msg.get("text").toString();
                    String date = msg.get("date").toString();
                    System.out.println("+++dedaode"+msgcontent+date);
                    Chat_Msg msg_rec = new Chat_Msg(sender.getUserID(),date,msgcontent,0);
                    ChatRoomFragment f = (ChatRoomFragment)getmFragmenglist().get(1);
                    f.setData(msg_rec);
                    f.getChatMsgAdapter().notifyDataSetChanged();
                    f.getChatlv().setSelection(f.getChatlv().getBottom());
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("+++接收消息转换失败");
                }

            }
            @Override
            public void onRecvGroupCustomMessage(String msgID, String groupID, V2TIMGroupMemberInfo sender, byte[] customData) {
                String msg = new String(customData);
                System.out.println("+++"+msg);
                try {
                    JSONObject jsonmsg = new JSONObject(msg);
                    String value = jsonmsg.get("value").toString();
                    JSONObject jsonoperator = new JSONObject(value);
                    String operator =jsonoperator.get("operator").toString();
                    if(operator!=UserId){
                        mBoard.addSyncData(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("+++转换出错！");
                }

//
            }
        });
    };
    public void createGroup(){
        V2TIMManager.getInstance().createGroup(V2TIMManager.GROUP_TYPE_MEETING, Roomid, Roomid, new V2TIMValueCallback<String>() {
            @Override
            public void onSuccess(String s) {
                // 创建群组成功
                System.out.println("++++创建群组成功！");
                initBoard();
            }
            @Override
            public void onError(int code, String desc) {
                // 创建群组失败
                if(10021==code){
                    V2TIMManager.getInstance().joinGroup(Roomid, Roomid, new V2TIMCallback() {
                        @Override
                        public void onSuccess() {
                            // 加群成功
                            System.out.println("++++加群成功");
                            initBoard();
                        }
                        @Override
                        public void onError(int i, String s) {
                            // 加群失败
                            if(10013==i){
                                System.out.println("+++已经是组员了");
                                initBoard();
                            }else {
                                System.out.println("+++++加群失败"+s+i);
                            }
                        }
                    });
                }else if(10025==code){
                    System.out.println("+++你创建的群组，已经是组员了");
                    initBoard();
                }else {
                    System.out.println("+++创建群组失败！"+desc+"    code："+code);
                }
            }
        });

    }

    public void initBoardMenu(){
        b_size = findViewById(R.id.board_size);
        b_cur =  findViewById(R.id.board_curpage);
        b_sum =  findViewById(R.id.board_sumpage);
        b_chu =  findViewById(R.id.b_chu);
        b_per =  findViewById(R.id.b_per);

        //select_menu按钮
        menu01 = findViewById(R.id.menu01);
        menu01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(0);
                setLeftmenustatus();
                menu01.setBackgroundResource(R.mipmap.menu_01_mouse1);
                b_cur.setText((mBoard.getBoardList().indexOf(mBoard.getCurrentBoard())+1)+"");
            }
        });
        menu02 = findViewById(R.id.menu02);
        menu02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(1);
                setLeftmenustatus();
                menu02.setBackgroundResource(R.mipmap.menu_02_paint1);
            }
        });
        menu03 = findViewById(R.id.menu03);
        menu03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(11);
                setLeftmenustatus();
                menu03.setBackgroundResource(R.mipmap.menu_03_text1);
            }
        });
        menu04 = findViewById(R.id.menu04);
        menu04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(6);
                setLeftmenustatus();
                menu04.setBackgroundResource(R.mipmap.menu_04_jihe1);
            }
        });
        menu05 = findViewById(R.id.menu05);
        menu05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(9);
                setLeftmenustatus();
                menu05.setBackgroundResource(R.mipmap.menu_05_select1);
            }
        });
        menu06 = findViewById(R.id.menu06);
        menu06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.useMathTool(1);
                setLeftmenustatus();
                menu06.setBackgroundResource(R.mipmap.menu_06_tools1);
            }
        });
        menu07 = findViewById(R.id.menu07);
        menu07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(12);
                b_size.setText(mBoard.getBoardScale()+"%");
                setLeftmenustatus();
                menu07.setBackgroundResource(R.mipmap.menu_07_move1);
            }
        });
        menu08 = findViewById(R.id.menu08);
        menu08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(2);
                setLeftmenustatus();
                menu08.setBackgroundResource(R.mipmap.menu_08_earsea1);
            }
        });
        menu09 = findViewById(R.id.menu09);
        menu09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.clear(false);
                setLeftmenustatus();
                menu09.setBackgroundResource(R.mipmap.menu_09_clean1);
//                mBoard.setToolType();
            }
        });
        menu10 = findViewById(R.id.menu10);
        menu10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(3);
                setLeftmenustatus();
                menu10.setBackgroundResource(R.mipmap.menu_101);
            }
        });
        menu11 = findViewById(R.id.menu11);
        menu11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus();
                menu11.setBackgroundResource(R.mipmap.menu_11_bg1);
//                设置背景图片  才可以
//                mBoard.setBackgroundImage();
//                mBoard.setToolType(0);
            }
        });
        menu12 = findViewById(R.id.menu12);
        menu12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                收起来
                if(menu_l_status){
                    menu03.setVisibility(View.GONE);
                    menu04.setVisibility(View.GONE);
                    menu05.setVisibility(View.GONE);
                    menu06.setVisibility(View.GONE);
                    menu07.setVisibility(View.GONE);
                    menu09.setVisibility(View.GONE);
                    menu10.setVisibility(View.GONE);
                    menu11.setVisibility(View.GONE);
                    menu12.setBackgroundResource(R.mipmap.menu_12_up);
                    menu_l_status=false;
                }else {
                    menu03.setVisibility(View.VISIBLE);
                    menu04.setVisibility(View.VISIBLE);
                    menu05.setVisibility(View.VISIBLE);
                    menu06.setVisibility(View.VISIBLE);
                    menu07.setVisibility(View.VISIBLE);
                    menu09.setVisibility(View.VISIBLE);
                    menu10.setVisibility(View.VISIBLE);
                    menu11.setVisibility(View.GONE);
                    menu12.setBackgroundResource(R.mipmap.menu_12_down);
                    menu_l_status=true;
                }
            }
        });

//        b_size.setText(mBoard.getBoardRatio());
        if(mBoard!=null){System.out.println("+++白板的缩放比例"+mBoard.getBoardRatio());}

        menub01 = findViewById(R.id.menub01);
        menub01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.undo();
            }
        });
        menub02 = findViewById(R.id.menub02);
        menub02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {mBoard.redo();}
        });
        menub03 = findViewById(R.id.menub03);
        menub03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setBoardScale(100);
            }
        });
        menub04 = findViewById(R.id.menub04);
        menub04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Integer.parseInt(b_size.getText().toString())-10>=100){
                    mBoard.setBoardScale( Integer.parseInt(b_size.getText().toString())-10);
                    b_size.setText(mBoard.getBoardScale()+"");
                }else {
                    mBoard.setBoardScale(100);
                    b_size.setText(mBoard.getBoardScale()+"");
                }
            }
        });
        menub05 = findViewById(R.id.menub05);
        menub05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Integer.parseInt(b_size.getText().toString())+10<=300){
                    mBoard.setBoardScale( Integer.parseInt(b_size.getText().toString())+10);
                    b_size.setText(mBoard.getBoardScale()+"");
                }else{
                    mBoard.setBoardScale(300);
                    b_size.setText(mBoard.getBoardScale()+"");
                }
            }
        });
        menub06= findViewById(R.id.menub06);
        menub06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.prevStep();
                b_cur.setText((mBoard.getBoardList().indexOf(mBoard.getCurrentBoard())+1)+"");
            }
        });
        menub07 = findViewById(R.id.menub07);
        menub07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.nextStep();
                b_cur.setText((mBoard.getBoardList().indexOf(mBoard.getCurrentBoard())+1)+"");
            }
        });
        menub08 = findViewById(R.id.menub08);
        menub08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加一页
                mBoard.addBoard(null,TEduBoardController.TEduBoardImageFitMode.TEDU_BOARD_IMAGE_FIT_MODE_CENTER, TEduBoardController.TEduBoardBackgroundType.TEDU_BOARD_BACKGROUND_IMAGE,true);
                b_cur.setText((mBoard.getBoardList().indexOf(mBoard.getCurrentBoard())+1)+"");
                b_sum.setText(mBoard.getBoardList().size()+"");
            }
        });
        menub09 = findViewById(R.id.menub09);
        menub09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //删除一页
                if(mBoard.getBoardList().size()>1){
                    mBoard.deleteBoard(null);
                    b_cur.setText((mBoard.getBoardList().indexOf(mBoard.getCurrentBoard())+1)+"");
                    b_sum.setText(mBoard.getBoardList().size()+"");
                }
            }
        });
        menub10 = findViewById(R.id.menub10);
        menub10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_b_status){
                    menub03.setVisibility(View.GONE);
                    menub04.setVisibility(View.GONE);
                    menub05.setVisibility(View.GONE);
                    menub06.setVisibility(View.GONE);
                    menub07.setVisibility(View.GONE);
                    menub08.setVisibility(View.GONE);
                    menub09.setVisibility(View.GONE);
                    b_cur.setVisibility(View.GONE);
                    b_sum.setVisibility(View.GONE);
                    b_size.setVisibility(View.GONE);
                    b_per.setVisibility(View.GONE);
                    b_chu.setVisibility(View.GONE);
                    menub10.setBackgroundResource(R.mipmap.menu_b11);
                    menu_b_status=false;
                }else {
                    menub03.setVisibility(View.VISIBLE);
                    menub04.setVisibility(View.VISIBLE);
                    menub05.setVisibility(View.VISIBLE);
                    menub06.setVisibility(View.VISIBLE);
                    menub07.setVisibility(View.VISIBLE);
                    menub08.setVisibility(View.VISIBLE);
                    menub09.setVisibility(View.VISIBLE);
                    b_cur.setVisibility(View.VISIBLE);
                    b_sum.setVisibility(View.VISIBLE);
                    b_size.setVisibility(View.VISIBLE);
                    b_per.setVisibility(View.VISIBLE);
                    b_chu.setVisibility(View.VISIBLE);
                    menub10.setBackgroundResource(R.mipmap.menu_b10);
                    menu_b_status=true;
                }
            }
        });
    }

    public void setLeftmenustatus(){
        menu01.setBackgroundResource(R.mipmap.menu_01_mouse);
        menu02.setBackgroundResource(R.mipmap.menu_02_paint);
        menu03.setBackgroundResource(R.mipmap.menu_03_text);
        menu04.setBackgroundResource(R.mipmap.menu_04_jihe);
        menu05.setBackgroundResource(R.mipmap.menu_05_select);
        menu06.setBackgroundResource(R.mipmap.menu_06_tools);
        menu07.setBackgroundResource(R.mipmap.menu_07_move);
        menu08.setBackgroundResource(R.mipmap.menu_08_earsea);
        menu09.setBackgroundResource(R.mipmap.menu_09_clean);
        menu10.setBackgroundResource(R.mipmap.menu_10);
        menu11.setBackgroundResource(R.mipmap.menu_11_bg);
    }
    public void sendMsg(Chat_Msg msg){
        // 创建文本消息
        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createTextMessage( "{\"text\":\""+msg.getContent()+"\",\"date\":\""+msg.getDate()+"\"}");
        // 发送消息
        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, null,Roomid, V2TIMMessage.V2TIM_PRIORITY_NORMAL, false, null, new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int progress) {
                // 文本消息不会回调进度
            }
            @Override
            public void onSuccess(V2TIMMessage message) {
                // 发送群聊文本消息成功
                System.out.println("+++文本消息发送成功了");
            }

            @Override
            public void onError(int code, String desc) {
                System.out.println("+++文本消息发送失败");
                // 发送群聊文本消息失败
            }
        });
    }
    public void stopAllchat(Boolean isstop){
        // 全员禁言
        V2TIMGroupInfo info = new V2TIMGroupInfo();
        info.setGroupID(Roomid);
        info.setAllMuted(isstop);
        V2TIMManager.getGroupManager().setGroupInfo(info, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // 全员禁言成功
                System.out.println("+++全员禁言成功！");

                ChatRoomFragment f = (ChatRoomFragment)getmFragmenglist().get(1);
                EditText ed =  f.getView().findViewById(R.id.inputtext);
                RadioButton rb = f.getView().findViewById(R.id.allnochat);
                rb.setChecked(isstop);
                if(isstop){
                    ed.setHint("全体禁言成功！");
                }else {
                    ed.setHint("取消全体禁言！");
                }
            }

            @Override
            public void onError(int code, String desc) {
                // 全员禁言失败
                System.out.println("+++全员禁言失败！"+code+desc);
                if(code==10007){
                    ChatRoomFragment f = (ChatRoomFragment)getmFragmenglist().get(1);
                    RadioButton rb = f.getView().findViewById(R.id.allnochat);
                    rb.setChecked(false);
                    EditText ed =  f.getView().findViewById(R.id.inputtext);
                    ed.setHint("你没有禁言权限！");
                }

            }
        });
    }

    //下课 销毁白板  反初始化IM
    public void destroyBoard() {
//        mIsEnterRoom = false;
        if (mBoard != null) {
            if (mBoardCallback != null) {
                mBoard.removeCallback(mBoardCallback);
            }
            mBoard.uninit();
        }
        mBoard = null;
        mBoardCallback = null;
//        mCreateCallBack = null;

        v2TIMManager.unInitSDK();
        v2TIMManager=null;
    }

}