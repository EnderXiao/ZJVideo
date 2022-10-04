package com.example.trtc_client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.trtc_client.adapter.HandsUpListViewAdapter;
import com.example.trtc_client.adapter.MemberListViewAdapter;
import com.example.trtc_client.adapter.SetBrd_TabBarAdapter;
import com.example.trtc_client.adapter.TabBarAdapter;
import com.example.trtc_client.setBoardFragment.Set_Highlighter_Fragment;
import com.example.trtc_client.setBoardFragment.Set_bg_Fragment;
import com.example.trtc_client.setBoardFragment.Set_eraser_Fragment;
import com.example.trtc_client.setBoardFragment.Set_geometry_Fragment;
import com.example.trtc_client.setBoardFragment.Set_more_Fragment;
import com.example.trtc_client.setBoardFragment.Set_paint_Fragment;
import com.example.trtc_client.setBoardFragment.Set_text_Fragment;
import com.example.trtc_client.utils.UriUtils;
import com.google.android.material.tabs.TabLayout;
import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.CosXmlServiceConfig;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlProgressListener;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.CosXmlRequest;
import com.tencent.cos.xml.model.CosXmlResult;
import com.tencent.cos.xml.transfer.COSXMLUploadTask;
import com.tencent.cos.xml.transfer.TransferConfig;
import com.tencent.cos.xml.transfer.TransferManager;
import com.tencent.cos.xml.transfer.TransferState;
import com.tencent.cos.xml.transfer.TransferStateListener;
import com.tencent.imsdk.v2.V2TIMAdvancedMsgListener;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMConversation;
import com.tencent.imsdk.v2.V2TIMGroupInfo;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMMessage;
import com.tencent.imsdk.v2.V2TIMMessageReceipt;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.imsdk.v2.V2TIMSendCallback;
import com.tencent.imsdk.v2.V2TIMValueCallback;
import com.tencent.liteav.TXLiteAVCode;
import com.tencent.qcloud.core.auth.QCloudCredentialProvider;
import com.tencent.qcloud.core.auth.ShortTimeCredentialProvider;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.teduboard.TEduBoardController;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;
import com.tencent.trtc.debug.GenerateTestUserSig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static Handler handlerCount = new Handler();
    private static Runnable runnablere_mBoardaddResouce;
    private static Timer Boardtimer = new Timer();  // 白板定时任务  用于获取转码进度

    //Tabbar三个Fragment
    private List<Fragment> mFragmenglist = new ArrayList<>();
    public List<Fragment> getmFragmenglist() {
        return mFragmenglist;
    }
    public void setmFragmenglist(List<Fragment> mFragmenglist) {this.mFragmenglist = mFragmenglist;}

    public final static String TAG = "Ender_MainActivity";
    private static TRTCCloud mTRTCCloud;
    private TRTCCloudDef.TRTCParams myTRTCParams;
    private TXCloudVideoView mTXCVVTeacherPreviewView;
    private RelativeLayout teacherTRTCBackground;
    private ImageView boardBtn;     //白板按钮
    private ImageView canvasBtn;    //文件 按钮  现在是画笔按钮

    private ImageView contentBtn;  //文件夹按钮  现在是授课内容
    private ImageView membesw;
    private ImageView handBtn;
    private ImageView cameraBtn;   //关闭摄像头按钮
    private ImageView audioBtn;

    private ImageView exit_btn;     //下课按钮
    private TextView teacher_name_view; //显示教师名称
    private ImageView overClassBtn;
    private Group group_btn;

    public static String mTeacherId;

    private boolean musicOn = true;
    private boolean cameraOn = true;

    // 监听用户进入房间
    public static ArrayList<String> mUserList = new ArrayList<String>();
    public static ArrayList<String> mCameraUserList = new ArrayList<String>();
    public static ArrayList<String> mMicrophoneUserList = new ArrayList<String>();
    public static int mUserCount = 0;

    private String MRegion="ap-guangzhou"	;                                          //存储桶配置的大区 	ap-guangzhou
    private String Mbucket = "zjkj-1258767809";                                        //存储桶名称   由bucketname-appid 组成，appid必须填入
    private String MsecretId = "AKID5ybx2rPggPr23oHUR8YhZBWZLr6xaw2r";                 //存储桶   永久密钥 secretId
    private String MsecretKey = "auxjESQCk11lEQL0O5WhbEZdRyEDwOYR";                    //存储桶    永久密钥 secretKey

//    protected static   String UserId = "";

    public static String userId = "";                                          //用户ID
    private  String UserSig ="";                                                       //腾讯服务签名
//    private  String UserSig =GenerateTestUserSig.genTestUserSig(UserId);
//    protected static  String Roomid  = "";                                              // 互动白板 房间号
    public static String roomid  = "750795";
    private static String subjectId = "subjectid";                                      //学科ID

    private  int SDKappID =1400618830;                                                  //SDKAppID



    public static String userName = "xgy";
    public static String keTangName = "keTang";
    public static String keTangId = "keTangId";



    //即时通信相关
    private V2TIMManager v2TIMManager;                                        //IM实例
    private boolean IMLoginresult;                                            //IM登录结果

    //白板左侧菜单栏
    private Boolean menu_l_status=true;                                       //左侧工具栏状态   true 代表展开
    private ImageButton menu01,menu02,menu03,menu04,menu05,menu06,menu07,menu08,menu09,menu10,menu11,menu12; //左侧工具栏图标 按钮
    //白板底部菜单栏
    private Boolean menu_b_status=true;                                        //底部工具栏状态  true 代表展开
    private TextView b_size,b_cur,b_sum,b_chu,b_per;                           //底部工具栏中显示的文字  当前白板缩放比例  当前页数  总页数
    private ImageButton menub01,menub02,menub03,menub04,menub05,menub06,menub07,menub08,menub09,menub10,menub11;
        //聊天消息列表
    private List<Chat_Msg> data = new ArrayList<>();

    //互动白板相关
    private TextView alert_text, upload_btn;                                    //提示白板当前的状态
    private View boardview;                                                     //白板的view
    private static TEduBoardController mBoard;                                  //白板实例
    public TEduBoardController getmBoard() {return this.mBoard;}                //获取到白班实例
    private Boolean BoardStatus=false;                                          //记录白板初始化 状态
    private Boolean addBoardtoFragmentstatus=false;                             //记录白板是否添加到了父容器中
    private FrameLayout.LayoutParams addBoardlayoutParams;                      //添加白板的时候用到的参数
    private FrameLayout Board_container;                                        //显示白板的Fragment
    private ConstraintLayout rf_leftmenu;                                       //白板左侧菜单栏
    private RelativeLayout rf_bottommenu,rf_shoukeneirong,select_resources;     //白板底部菜单栏   中间默认状态布局    选择授课资源布局
    private TEduBoardController.TEduBoardCallback mBoardCallback;               //白板回调
    private ImageButton geometry11,geometry12,geometry13,geometry14,geometry21,geometry22,geometry23,geometry24,geometry31,geometry32,geometry33,geometry34,geometry41,geometry42,geometry43,geometry44,geometry51,geometry52,geometry53,geometry61,geometry62,geometry63;
    private ImageButton teachingtools1,teachingtools2,teachingtools3,teachingtools4,teachingtools5;
    private PopupWindow pw_selectpaint;                                 //选择画笔 一级弹窗
    private PopupWindow pw_selecgeometry;                               //选择 几何工具  一级弹窗
    private PopupWindow pw_selectteachingtools;                         //选择教学工具   一级弹窗
    private PopupWindow pw_selecteraser;                                //选择橡皮   一级弹窗
    private List<Fragment>  mTabFragmenList = new ArrayList<>();        // 右侧 视频列表 聊天列表  互动课堂  三个tabbar
    private ImageView menu02color,menu03color,menu04color;              // 笔  文字   几何工具  显示右下角选择的颜色
    private TableLayout select_menu_top ,select_menu;
    private RelativeLayout menu01RL,menu02RL,menu03RL,menu04RL,menu05RL,menu06RL,menu07RL,menu08RL,menu09RL,menu10RL,menu11RL,menu12RL;
    private CosXmlService cosXmlService;                                //初始化 COS Service，获取实例

    // 记录当前文件ID 文件当前页ID，  白板页ID
    private String FileID=null;                     //当前文件ID
    private String CurFileID=null;                  //当前文件页面ID
    private String BoardID="#DEFAULT";              //当前文件ID
    private String CurBoardID=null;                 //当前白板页ID
    private String CurType=null;                    // 初试为空   两种类型  Board和File
    private static Boolean isquestion=false;        //用于记录是不是  题目保存调用快照

    private Button choosefile,uploadfile;                       //选择文件弹窗  选择文件  上传文件 按钮
    private TextView msgTips,filename;                          //选择文件弹窗  提示当前文件名称  提示当前文件状态
    private ProgressBar  proBar;                                //上传文件 进度条
    private ImageView close_select_resources,resupload;         //关闭选择文件弹窗  右上角本地上传
    private Boolean isincludeType  = false;                     //判断文件格式是否上传
    private String curfilepath,curfilename;                     //记录当前文件路径   当前文件名称
    private LinearLayout uploadprogress;                        //选择文件弹窗布局  用来设置Visiable

    //TabBarFragment
    private final ChatRoomFragment chatRoomFragment = new ChatRoomFragment();                       //右侧聊天的Fragment实例
    private final VideoListFragment videoListFragment =  new VideoListFragment();                   //右侧视频列表的Fragment实例
    private final AnswerQuestionFragment answerQuestionFragment = new AnswerQuestionFragment();     //右侧互动答题的Fragment实例

    // 成员列表
    private static View memberPopupView;
    private static ListView memberList;
    private static MemberListViewAdapter listViewAdapter;

    // 举手列表
    private View handsUpPopupView;
    private ListView handsUpList;
    public HandsUpListViewAdapter handsUpListViewAdapter;
    public List<HandsUpItem> handsUpItemList = new ArrayList<>();
    public Switch handsUpSwitchBtn;

    // UI消息监听器
    public Handler handler;
    @SuppressLint("HandlerLeak")

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        单条权限动态获取
//        String PM_SINGLE= Manifest.permission.CAMERA;
//        int nRet= ContextCompat.checkSelfPermission(this,PM_SINGLE);
//        if(nRet!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,new String[]{PM_SINGLE},10000);
//        }

//        多条权限动态获取
        String[] PM_MULTIPLE={
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS
        };

        if(Build.VERSION.SDK_INT>=23) {
            ArrayList<String> pmList = new ArrayList<>();
            //获取当前未授权的权限列表
            for (String permission : PM_MULTIPLE) {
                int nRet = ContextCompat.checkSelfPermission(this, permission);
                if (nRet != PackageManager.PERMISSION_GRANTED) {
                    pmList.add(permission);
                }
            }
            if (pmList.size() > 0) {
                String[] sList = pmList.toArray(new String[0]);
                ActivityCompat.requestPermissions(this, sList, 10000);
            }
        }
       //初始化测试参数
        Intent intent = getIntent();
        System.out.println("+++参数"+intent.getExtras().get("subjectid"));
        System.out.println("+++参数"+intent.getExtras().get("roomid"));
        System.out.println("+++参数"+intent.getExtras().get("userid"));
        userId=intent.getExtras().get("userid").toString();
        roomid = intent.getExtras().get("roomid").toString();
        subjectId = intent.getExtras().get("subjectid").toString();
        UserSig =GenerateTestUserSig.genTestUserSig(intent.getExtras().get("userid").toString());



        List<MemberDataBean> testList1 = new ArrayList<>();
        mFragmenglist.add(videoListFragment);
        mFragmenglist.add(chatRoomFragment);
        mFragmenglist.add(answerQuestionFragment);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView class_id_text_view = findViewById(R.id.class_id);
        class_id_text_view.setText(roomid);
        @SuppressLint("UseCompatLoadingForDrawables") Drawable class_id_icon = getResources().getDrawable(R.drawable.copy);
        class_id_icon.setBounds(0,0,15,15);
        class_id_text_view.setCompoundDrawables(null, null, class_id_icon,null);




        // 获取CameraView
        mTXCVVTeacherPreviewView = findViewById(R.id.teacher_camera);
        teacherTRTCBackground = findViewById(R.id.teacher_background);

        alert_text = findViewById(R.id.alert_text);

        // 获取底部按钮

        handsUpPopupView = getLayoutInflater().inflate(R.layout.hands_up_pop_window, null);
        handsUpSwitchBtn = handsUpPopupView.findViewById(R.id.hands_up_controller);
        handsUpList = handsUpPopupView.findViewById(R.id.hands_up_list);
        memberPopupView = getLayoutInflater().inflate(R.layout.member_list_pop_window, null);
        memberList = memberPopupView.findViewById(R.id.member_list);
        group_btn = findViewById(R.id.group_buttons);
        canvasBtn = findViewById(R.id.canvas_btn);
        exit_btn = findViewById(R.id.exit_btn);

        boardBtn = findViewById(R.id.board_btn);
        contentBtn = findViewById(R.id.content_btn);
        membesw = findViewById(R.id.member_btn);
        handBtn = findViewById(R.id.hand_btn);
        audioBtn = findViewById(R.id.mic_btn);
        cameraBtn = findViewById(R.id.camera_btn);
        overClassBtn = findViewById(R.id.exit_btn);
        teacher_name_view = findViewById(R.id.teacher_name);

        MainActivity that = this;
        handsUpSwitchBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    HttpActivity.memberController("", "", "", "handAllNo", "", -1, that);
                } else {
                    HttpActivity.memberController("", "", "", "handAllYes", "", -1, that);
                }
            }
        });

        overClassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onExitLiveRoom();
            }
        });
        teacher_name_view.setText(userId+"老师");
        //文件上传部分按钮
        select_resources=findViewById(R.id.select_resources);
        proBar = findViewById(R.id.proBar);
        msgTips = findViewById(R.id.msgTips);
        filename = findViewById(R.id.filename);
        close_select_resources = findViewById(R.id.close_select_resources);
        resupload= findViewById(R.id.res_upload);
        choosefile = findViewById(R.id.choosefile);
        uploadfile = findViewById(R.id.uploadfile);
        uploadprogress = findViewById(R.id.uploadprogress);

        //下课按钮
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyBoard();
                addBoardtoFragmentstatus =false;
                rf_leftmenu.setVisibility(View.GONE);
                rf_bottommenu.setVisibility(View.GONE);
                RelativeLayout bg_shoukeneirong = findViewById(R.id.bg_shoukeneirong);
                bg_shoukeneirong.setVisibility(View.VISIBLE);
                alert_text.setText("已经下课，请退出教室！");
                Board_container.setVisibility(View.GONE);
                canvasBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                contentBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
                boardBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
        //选择文件按钮
        choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadprogress.setVisibility(View.GONE);
                uploadfile.setText("开始上传");
                msgTips.setText("文件正在上传：");
                filename.setText("未选择任何文件");
                curfilename="";
                curfilepath="";
                intoFileManager();
            }
        });
        //上传文件按钮
        uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击开始上传 执行上传任务
                if(isincludeType){
                    uploadprogress.setVisibility(View.VISIBLE);
                    uploadfile.setText("正在上传");
                    msgTips.setText("正在上传中：");
                    Time time = new Time("GMT+8");
                    time.setToNow();
                    String cosprefix = "class/"+time.year+"/"+(time.month+1)+"/"+time.monthDay+"/"+subjectId+"/"+roomid+"/res/";
                    UploadToBucket(cosprefix,curfilepath,curfilename,false);
                }
            }
        });
        //文件夹 按钮 现在是授课内容 未替换图片
        contentBtn = findViewById(R.id.content_btn);
        contentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addBoardtoFragmentstatus){
                    if(select_resources.getVisibility()==View.VISIBLE){
                        select_resources.setVisibility(View.GONE);
                    }else {
                        select_resources.setVisibility(View.VISIBLE);
                        filename.setText("未选择任何文件");
                        uploadfile.setText("开始上传");
                        uploadprogress.setVisibility(View.GONE);
                        curfilename="";
                        curfilepath="";
                    }
                }
            }
        });

        //文件按钮  现在显示是画笔  未替换图片
        canvasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     //判断元素类型 保存快照
                System.out.println("+++点了切换文件："+"--白板切换前类型:"+CurType+"--当前页白板ID"+FileID+"当前元素："+mBoard.getBoardElementList(null).toString());
               if(addBoardtoFragmentstatus){
                   if("Board".equals(CurType)&&FileID!=null){
                       TEduBoardController.TEduBoardSnapshotInfo path = new TEduBoardController.TEduBoardSnapshotInfo();
                       path.path = getCacheDir()+"/"+CurBoardID+".png";
                       mBoard.snapshot(path);
                       mBoard.switchFile(FileID);
                       if(CurFileID!=null){
                           mBoard.gotoBoard(CurFileID,false);
                       }
                   }else if("File".equals(CurType)&&FileID!=null) {
                       System.out.println("+++当前就是在文件页面");
                   }else {
                       System.out.println("+++先上传文件");
                   }
               }
            }
        });

        //白板需要用到的一些组件 初始化
         addBoardlayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
         Board_container = findViewById(R.id.teachingcontent);
         rf_leftmenu = findViewById(R.id.menu_left);
         rf_bottommenu = findViewById(R.id.menu_bottom);
         rf_shoukeneirong = findViewById(R.id.bg_shoukeneirong);


         //白板按钮
        boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("+++点了白板按钮：白板是否为空："+(mBoard!=null)+"--白板初始化状态"+BoardStatus+"--白板当前类型:"+CurType+"--当前页白板ID"+BoardID+"当前元素："+mBoard.getBoardElementList(null).toString());
                //判断元素类型 保存快照
                    //白板初始化完成了
                    if(BoardStatus&&addBoardtoFragmentstatus){
                        if(mBoard!=null&&"Board".equals(CurType)){
                            System.out.println("+++我就是在白板页面了");
                        }else {
                            if("File".equals(CurType)){
                                System.out.println("+++我要切换白板页面了");
                                TEduBoardController.TEduBoardSnapshotInfo path = new TEduBoardController.TEduBoardSnapshotInfo();
                                path.path = getCacheDir()+"/"+CurBoardID+".png";
                                mBoard.snapshot(path);
                                mBoard.switchFile(BoardID);
                                if(CurBoardID!=null){
                                    mBoard.gotoBoard(CurBoardID,false);
                                }
                            }
                        }
                    }
                    else {
                        addBoardtoFragmentstatus =  mBoard.addBoardViewToContainer(Board_container,boardview,addBoardlayoutParams);
                        rf_leftmenu.setVisibility(View.VISIBLE);
                        rf_bottommenu.setVisibility(View.VISIBLE);
                        rf_shoukeneirong.setVisibility(View.GONE);
                    }
            }
        });

        if(mBoard==null||CurType==null){
            initTIM();
        }


        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                int position = -1;
                switch (msg.what) {
                    case 1:
                        setHandsUpData();
                        break;
                    case 2:
                        position = msg.getData().getInt("position");
                        switchMemberListAudioIcon(position);
                        break;
                    case 3:
                        position = msg.getData().getInt("position");
                        switchMemberListChatIcon(position);
                        break;
                    case 4:
                        break;
                    case 5:
                        position = msg.getData().getInt("position");
                        switchSpeakerIcon(position);
                        break;
                    default:
                        break;
                }
            }
        };

        //初始化存储桶服务
        InitBucket(this);
        initHandsUpList();
        initMemberList();
        initTabBarNavigation();
        enterLiveRoom();
    }

    public void setHandsUpData() {
        handsUpItemList.clear();
        List<HandsUpItem> tempHandsUpItemList = new ArrayList<>();
        for (int i = 0; i < AnswerActivity.handsUpList.size(); i++) {
            tempHandsUpItemList.add(new HandsUpItem(AnswerActivity.handsUpList.get(i).getName(), AnswerActivity.handsUpList.get(i).getUserId(), false));
        }
        for (int i =0; i< tempHandsUpItemList.size(); i++) {
            Log.e(TAG, "updateHandsUpList: "  + tempHandsUpItemList.get(i).toString());
        }
        handsUpItemList.addAll(tempHandsUpItemList);
        handsUpListViewAdapter.notifyDataSetChanged();
    }

    public void switchMemberListAudioIcon(int position) {
        MemberItem item = listViewAdapter.getItem(position);
        if(item != null){
            Log.e(TAG, "switchMemberListAudioIcon: 获取用户item " + item.getName());
            item.setAudioControl(!item.getAudioControl());
            Toast.makeText(MainActivity.this, "成员 " + position + " 禁音按钮被点击", Toast.LENGTH_SHORT).show();
            listViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "成员 " + position + " 非法", Toast.LENGTH_SHORT).show();
        }
    }

    public void switchMemberListChatIcon(int position) {
        MemberItem item = listViewAdapter.getItem(position);
        if(item != null){
            item.setChatControl(!item.getChatControl());
            Toast.makeText(MainActivity.this, "成员 " + position + " 禁言按钮被点击", Toast.LENGTH_SHORT).show();
            listViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "成员 " + position + " 非法", Toast.LENGTH_SHORT).show();
        }
    }

    public void switchSpeakerIcon(int position) {
        MemberItem item = listViewAdapter.getItem(position);
        if(item != null){
            item.setSpeakControl(!item.getSpeakControl());
            Toast.makeText(MainActivity.this, "成员 " + position + " 上讲台按钮被点击", Toast.LENGTH_SHORT).show();
            listViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "成员 " + position + " 非法", Toast.LENGTH_SHORT).show();
        }
    }

    public void initHandsUpList() {
        if(AnswerActivity.handsUpList != null) {
            for (int i = 0; i < AnswerActivity.handsUpList.size(); i++) {
                handsUpItemList.add(new HandsUpItem(AnswerActivity.handsUpList.get(i).getName(), AnswerActivity.handsUpList.get(i).getUserId(), false));
                Log.e(TAG, "initHandsUpList: "  + AnswerActivity.handsUpList.get(i).toString());
            }

        }
        handsUpListViewAdapter = new HandsUpListViewAdapter(this, handsUpList, handsUpItemList);
        handsUpList.setAdapter(handsUpListViewAdapter);

        handsUpList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, "Click Item" + i , Toast.LENGTH_SHORT).show();
            }
        });

        handsUpListViewAdapter.setOnSpeakerControllerClickListener(new HandsUpListViewAdapter.onSpeakerControllerListener() {
            @Override
            public void onSpeakControllerClick(int i) {
                upToSpeaking(i);
            }
        });
    }
    // 上讲台处理
    public void upToSpeaking(int position) {
        Toast.makeText(this, "举手成员 " + position + " 上讲台被点击了", Toast.LENGTH_SHORT).show();
    }

    public void initMemberList() {
        List<MemberItem> memberDataList = new ArrayList<>();
        HttpActivity.getMemberList();
//        memberList.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        if(AnswerActivity.joinList != null) {
            for (int i = 0; i < AnswerActivity.joinList.size(); i++) {
                memberDataList.add(new MemberItem(AnswerActivity.joinList.get(i).getName(), AnswerActivity.joinList.get(i).getUserId(), 1 ,true, true, true, false, true));
                Log.e(TAG, "initMemberList: " + AnswerActivity.joinList.get(i).toString());
            }
        }
        if(AnswerActivity.ketangList != null) {
            for (int i = 0; i < AnswerActivity.ketangList.size(); i++) {
                memberDataList.add(new MemberItem(AnswerActivity.ketangList.get(i).getName(), AnswerActivity.joinList.get(i).getUserId(), 0 , true, true, true, false, true));
                Log.e(TAG, "initMemberList: " + AnswerActivity.ketangList.get(i).toString());
            }
        }

//        for (int i = 0; i < 10; i ++){
//            memberDataList.add(new MemberItem("测试", Integer.toString(i), "2", "3", "4", "5"));
//        }
        MainActivity that = this;
        listViewAdapter = new MemberListViewAdapter(this, memberList, memberDataList);
        memberList.setAdapter(listViewAdapter);

        listViewAdapter.setOnItemButtonListener(new MemberListViewAdapter.onItemButtonListener() {
            @Override
            public void onMoveOutClick(int i) {
                Toast.makeText(MainActivity.this, "成员 " + i + " 移除按钮被点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChatControlClick(int i) {
                MemberItem item = listViewAdapter.getItem(i);
                if(item != null){
                    if(item.getChatControl()){
                        HttpActivity.memberController("", "", "closeWords", "", item.getUserId(), i, that);
                    } else {
                        HttpActivity.memberController("", "", "openWords", "", item.getUserId(), i, that);
                    }
                    Toast.makeText(MainActivity.this, "成员 " + i + " 禁言按钮被点击", Toast.LENGTH_SHORT).show();
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "成员 " + i + " 非法", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSpeakControlClick(int i) {
                MemberItem item = listViewAdapter.getItem(i);
                if(item != null){
                    if(item.getSpeakControl()){
                        HttpActivity.speakerController(item.getUserId(), item.getName(), "down", i, that);
                    } else {
                        HttpActivity.speakerController(item.getUserId(), item.getName(), "up", i, that);
                    }
                    Toast.makeText(MainActivity.this, "成员 " + i + " 上讲台按钮被点击", Toast.LENGTH_SHORT).show();
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "成员 " + i + " 非法", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAudioControlClick(int i) {
                MemberItem item = listViewAdapter.getItem(i);
                if(item != null){
                    if(item.getAudioControl()){
                        HttpActivity.memberController("closeMic", "", "", "", item.getUserId(), i, that);
                    } else {
                        HttpActivity.memberController("openMic", "", "", "", item.getUserId(), i, that);
                    }
                    Toast.makeText(MainActivity.this, "成员 " + i + " 禁音按钮被点击", Toast.LENGTH_SHORT).show();
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "成员 " + i + " 非法", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onVideoControlClick(int i) {
                Toast.makeText(MainActivity.this, "成员 " + i + " 禁视频按钮被点击", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBoardControlClick(int i) {
                MemberItem item = listViewAdapter.getItem(i);
                if(item != null){
                    if(item.getBoardControl()){
                        drawAuthority( "drawAuthority", "no", item.getUserId());
                        item.setBoardControl(false);
                    } else {
                        drawAuthority("drawAuthority" , "yes", item.getUserId());
                        item.setBoardControl(true);
                    }
                    Toast.makeText(MainActivity.this, "成员 " + i + " 禁绘画按钮被点击", Toast.LENGTH_SHORT).show();
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "成员 " + i + " 非法", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initTabBarNavigation() {

        //使用适配器将ViewPager与Fragment绑定在一起
        ViewPager viewPager = findViewById(R.id.tar_bar_view_page);
        TabBarAdapter tabBarAdapter = new TabBarAdapter(getSupportFragmentManager());
        tabBarAdapter.setmFragment(mFragmenglist);
        viewPager.setAdapter(tabBarAdapter);

        //设置ViewPager的最大缓存数
        viewPager.setOffscreenPageLimit(3);
        //将TabLayout与ViewPager绑定在一起
        TabLayout mTabLayout = findViewById(R.id.tab_bar_table_layout);
        mTabLayout.setupWithViewPager(viewPager,true);

        //指定Tab的位置
        TabLayout.Tab videoList = mTabLayout.getTabAt(0);
        TabLayout.Tab chatRoom = mTabLayout.getTabAt(1);
        TabLayout.Tab answerQuestion = mTabLayout.getTabAt(2);
    }


    // 退出房间
    public static void exitRoom() {
        mTRTCCloud.exitRoom();
        HttpActivity.stopHandsUpTimer();
    }

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
                activity.initMemberList();
                Log.e(TAG, "onEnterRoom: 进入房间成功，耗时: " + result);
                Toast.makeText(activity, "进入房间成功，耗时: " + "[" + result+ "]" , Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "onEnterRoom: 进入房间失败，错误代码：" + result);
                Toast.makeText(activity, "进入房间失败，错误代码: " + "[" + result+ "]" , Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean available) {
            MainActivity activity = mContext.get();
            Log.d(TAG, "onUserAudioAvailable userId " + userId + ", mUserCount " + userId + ",available " + available);
            System.out.println("onUserAudioAvailable userId " + userId + ", mUserCount " + userId + ",available " + available);
            System.out.println("onUserVideoAvailable:"+userId);
            activity.videoListFragment.setAudio(userId, available, activity, activity.mTRTCCloud);
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            MainActivity activity = mContext.get();
            activity.initMemberList();
            Log.d(TAG, "onUserVideoAvailable userId " + userId + ", mUserCount " + mUserCount + ",available " + available);
            Toast.makeText(activity, "onUserVideoAvailable userId " + userId + ", mUserCount " + mUserCount + ",available " + available , Toast.LENGTH_SHORT).show();
            System.out.println("onUserVideoAvailable userId " + userId + ", mUserCount " + mUserCount + ",available " + available);
            System.out.println("onUserVideoAvailable:"+userId);
//            if (userId.equals(mTeacherId+"_camera")&&!available){
//                System.out.println("mingming_camera exit room");
//                exitRoom();
//                teacher_enable=false;
//                return;
//            }
            if(available) {
                if(AnswerActivity.findMemberInKetangList(userId) != null) {

                    mUserList.add(userId);
                }
            }
            else
                mUserList.remove(userId);
            activity.videoListFragment.setVideo(userId, available, activity, activity.mTRTCCloud);

        }

        @Override
        public void onRemoteUserEnterRoom(String userId){
            MainActivity activity = mContext.get();
            activity.initMemberList();
            Log.e(TAG, "onRemoteUserEnterRoom: userId" + userId );
            System.out.println("onRemoteUserEnterRoom userId " + userId );
            Toast.makeText(activity, "onRemoteUserEnterRoom userId " + userId , Toast.LENGTH_SHORT).show();
            activity.videoListFragment.addCameraView(userId, activity.mTRTCCloud);
        }

        @Override
        public void onRemoteUserLeaveRoom(String userId, int reason){
            MainActivity activity = mContext.get();
            activity.initMemberList();
            activity.videoListFragment.leaveRoom(userId, reason, activity,
                    activity.mTRTCCloud);
//            Toast.makeText(activity, "onRemoteUserLeaveRoom userId " + userId , Toast.LENGTH_SHORT).show();
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
        myTRTCParams.userId = userId;
        myTRTCParams.roomId = Integer.parseInt(roomid);
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

        // 初始化房间信息
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        HttpActivity.initClass(screenWidth, screenHeight, "skydt", this);

        // 开启举手监听事件
        HttpActivity.startHandsUpTimer(this);
    }


    public void switchCamera(View view) {
        Log.e(TAG, "switchCamera: switchCamera" );
        if(mTRTCCloud!=null){
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
    }

    public void showMemberListBtn(View view) {
        PopupWindow popupWindow = new PopupWindow(memberPopupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        memberPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int offsetX = - memberPopupView.getMeasuredWidth() / 4;
        int offsetY = - memberPopupView.getMeasuredHeight() - (view.getHeight()) - 10;
        popupWindow.showAsDropDown(view, offsetX, offsetY, Gravity.START);
    }

    public void showHandsUpBtn(View view) {
        PopupWindow popupWindow = new PopupWindow(handsUpPopupView, 300, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        handsUpPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int offsetX = - handsUpPopupView.getMeasuredWidth() /4;
        int offsetY = - handsUpPopupView.getMeasuredHeight() - (view.getHeight()) - 10;
        popupWindow.showAsDropDown(view, offsetX, offsetY, Gravity.START);
    }

    //初始化白板
    public void initBoard(){
        // 创建并初始化白板控制器
        //（1）鉴权配置
        System.out.println("+++开始初始化白板");
        mBoard=null;
        mBoardCallback=null;
        TEduBoardController.TEduBoardAuthParam authParam = new TEduBoardController.TEduBoardAuthParam(
                SDKappID , userId, UserSig);
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
                System.out.println("onTEBWarning"+"+++++++++++++code:"+code);
                if(code==7){  //VIDEO_ALREADY_EXISTS
                    System.out.println("onTEBWarning"+"+++++++++++++VIDEO已经存在了:");
                    mBoard.gotoBoard(msg);
                }else if(code==3){  //H5PPT_ALREADY_EXISTS
                    System.out.println("onTEBWarning"+"+++++++++++++H5PPT已经存在了:");
                    mBoard.gotoBoard(msg);
                }else if(code==6){  //H5PPT_ALREADY_EXISTS
                    System.out.println("onTEBWarning"+"+++++++++++++H5FILE已经存在了:");
                    mBoard.gotoBoard(msg);
                }
//                TEduBoardController.TEduBoardWarningCode.TEDU_BOARD_WARNING_IMAGE_MEDIA_BITRATE_TOO_LARGE
            }
            @Override
            public void onTEBInit() {
                System.out.println("onTEBInit"+"++++白板初始化完成了");
                BoardStatus=true;
                boardview = mBoard.getBoardRenderView();
                initBoardMenu();
                alert_text.setText("白板加载完成！");
                if(!addBoardtoFragmentstatus){
                    addBoardtoFragmentstatus =  mBoard.addBoardViewToContainer(Board_container,boardview,addBoardlayoutParams);
                    rf_leftmenu.setVisibility(View.VISIBLE);
                    rf_bottommenu.setVisibility(View.VISIBLE);
                    rf_shoukeneirong.setVisibility(View.GONE);//默认图片那个消失
                }
            }
            @Override
            public void onTEBHistroyDataSyncCompleted() {
                System.out.println("onTEBHistroyDataSyncCompleted"+"+++++++++++++");
                //白板加载完成 请切换
                if(mBoard!=null&&mBoard.getBoardList().size()>0){
                    b_sum.setText(mBoard.getFileBoardList(mBoard.getCurrentFile()).size()+"");
                    b_cur.setText((mBoard.getFileBoardList(mBoard.getCurrentFile()).indexOf(mBoard.getCurrentBoard())+1)+"");
                }
            }
            @Override
            public void onTEBSyncData(String data) {
                findViewById(R.id.setBoardWindow).setVisibility(View.GONE);
                final V2TIMMessage message = V2TIMManager.getMessageManager().createCustomMessage(data.getBytes(), "", "TXWhiteBoardExt".getBytes());
                if (message.getCustomElem() != null) {
                    message.getCustomElem().setExtension("TXWhiteBoardExt".getBytes());
                    //设置头像  名称  +++
                    message.getCustomElem().setDescription("");
                    message.getCustomElem().setData(data.getBytes());
                }
                V2TIMManager.getInstance().getConversationManager().getConversation(roomid, new V2TIMValueCallback<V2TIMConversation>() {
                    @Override
                    public void onError(int i, String s) {
                        // 获取回话失败
                        System.out.println("+++获取会话失败");
                    }
                    @Override
                    public void onSuccess(V2TIMConversation v2TIMConversation) {
                        V2TIMManager.getInstance().getMessageManager().sendMessage(message, null, roomid, 1, false, null,  new V2TIMSendCallback<V2TIMMessage>() {
                            @Override
                            public void onSuccess(V2TIMMessage v2TIMMessage) {
                                // 发送 IM 消息成功
                                if(findViewById(R.id.setBoardWindow).getVisibility()==View.VISIBLE){
                                    findViewById(R.id.setBoardWindow).setVisibility(View.GONE);
                                }
                            }
                            @Override
                            public void onError(int i, String s) {
                                // 发送 IM 消息失败，建议进行重试
                                System.out.println("+++发送 IM 消息失败，建议进行重试"+s);
                                mBoard.syncAndReload();
                            }
                            @Override
                            public void onProgress(int i) {
//                                System.out.println("+++发送中onProgress"+i);
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
                select_resources.setVisibility(View.GONE);
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
                if(!"#DEFAULT".equals(fileId)){
                    FileID=fileId;
                    CurFileID=null;
                }
                //更改页码
                b_sum.setText(mBoard.getFileBoardList(mBoard.getCurrentFile()).size()+"");
                b_cur.setText((mBoard.getFileBoardList(mBoard.getCurrentFile()).indexOf(mBoard.getCurrentBoard())+1)+"");
            }

            @Override
            public void onTEBDeleteBoard(List<String> boardList, String fileId) {
                b_sum.setText(mBoard.getFileBoardList(mBoard.getCurrentFile()).size()+"");
                b_cur.setText((mBoard.getFileBoardList(mBoard.getCurrentFile()).indexOf(mBoard.getCurrentBoard())+1)+"");
            }

            @Override
            public void onTEBGotoBoard(String boardId, String fileId) {
                if(BoardID.equals(fileId)){
                    CurType="Board";
                    CurBoardID = boardId;
                }else {
                    CurType="File";
                    CurFileID = boardId;
                }
                b_cur.setText((mBoard.getFileBoardList(mBoard.getCurrentFile()).indexOf(mBoard.getCurrentBoard())+1)+"");
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
                System.out.println("onTEBAddTranscodeFile"+fileId);
                select_resources.setVisibility(View.GONE);
            }
            @Override
            public void onTEBDeleteFile(String fileId) {
                System.out.println("onTEBDeleteFile"+"+++:删除了文件的ID："+fileId);
            }
            @Override
            public void onTEBSwitchFile(String fileId) {
                handlerCount.removeCallbacks(runnablere_mBoardaddResouce);
                if(fileId.equals("#DEFAULT")){
 //                 当是白板的时候就要 跳转到之前相应页码数
                    CurType="Board";
                    CurBoardID = mBoard.getCurrentBoard();
                }else {
 //                 当不是白板的时候 记录一下  打开的文件ID
                    CurType="File";
                    if(!fileId.equals(FileID)){
                        //打开的文件不是上一次打开的文件就需要存起来文件ID了
                        FileID=fileId;
                    }
                    CurFileID = mBoard.getCurrentBoard();
                }
                b_sum.setText(mBoard.getFileBoardList(mBoard.getCurrentFile()).size()+"");
                b_cur.setText((mBoard.getFileBoardList(mBoard.getCurrentFile()).indexOf(mBoard.getCurrentBoard())+1)+"");
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
                System.out.println("onTEBFileTranscodeProgress"+"+++++FileTranscodeProgress");
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
            }

            @Override
            public void onTEBAudioStatusChanged(String elementId, int status, float progress, float duration) {
                System.out.println("onTEBAudioStatusChanged"+"+++++");
            }

            @Override
            public void onTEBSnapshot(String path, int code, String msg) {
                System.out.println("onTEBSnapshot"+"++++白板快照"+path+msg+code);

                if(code==0){
                    File ff = new File(path);
                    String name = ff.getName();
                    Time time = new Time("GMT+8");
                    time.setToNow();
                    // isquestion  用来区分本次快照是题目的快照还是 切换的时候保存的快照
                    String cosprefix = isquestion?"class/"+time.year+"/"+(time.month+1)+"/"+time.monthDay+"/"+subjectId+"/"+roomid+"/question/" : "class/"+time.year+"/"+(time.month+1)+"/"+time.monthDay+"/"+subjectId+"/"+roomid+"/capture/";
                    UploadToBucket(cosprefix,path,name,true);

                }else {
                    System.out.println("++++白板快照出错"+msg+"   code:"+code);
                }
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
        mBoard.init(authParam,  Integer.parseInt(roomid), initParam);
        //（2）获取白板 View
        // 初始化白板的按钮功能
        initBoardMenu();
    }

    public void initTIM(){
        //初始化 IMSDK
        V2TIMSDKConfig timSdkConfig = new V2TIMSDKConfig();
        IMLoginresult = V2TIMManager.getInstance().initSDK(this, SDKappID, timSdkConfig, new V2TIMSDKListener() {
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
        V2TIMManager.getInstance().login(userId, UserSig, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                System.out.println("++++++登陆失败"+s);
            }
            @Override
            public void onSuccess() {
                //高级消息监听器
                V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
                    @Override
                    public void onRecvNewMessage(V2TIMMessage msg) {
                        String Msg_Extension = new String(msg.getCustomElem().getExtension());
                        super.onRecvNewMessage(msg);
                        if("TXWhiteBoardExt".equals(Msg_Extension)){
                            //白板消息
                            mBoard.addSyncData(new String(msg.getCustomElem().getData()));
                        }else if("TBKTExt".equals(Msg_Extension)){
                            //文本消息
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            Chat_Msg msg_rec = new Chat_Msg(msg.getUserID(),format.format(new Date(msg.getTimestamp()*1000)),new String(msg.getCustomElem().getData()),0);// type  0 别人 1 自己
                            ChatRoomFragment f = (ChatRoomFragment)getmFragmenglist().get(1);
                            f.setData(msg_rec);
                            f.getChatMsgAdapter().notifyDataSetChanged();
                            f.getChatlv().setSelection(f.getChatlv().getBottom());
                        }
                    }
                    @Override
                    public void onRecvC2CReadReceipt(List<V2TIMMessageReceipt> receiptList) {
                        super.onRecvC2CReadReceipt(receiptList);
                    }
                    @Override
                    public void onRecvMessageRevoked(String msgID) {
                        super.onRecvMessageRevoked(msgID);
                    }
                });
                //登陆成功  创建群组
                createGroup();
            }
        });

        //简单消息监听器  弃用  因为要根据 Extension 判断是白板消息 还是 聊天消息
//        V2TIMManager.getInstance().addSimpleMsgListener(new V2TIMSimpleMsgListener() {

    };

                public void createGroup(){
        V2TIMManager.getInstance().createGroup(V2TIMManager.GROUP_TYPE_MEETING, roomid, roomid, new V2TIMValueCallback<String>() {
            @Override
            public void onSuccess(String s) {
                // 创建群组成功
                initBoard();
            }
            @Override
            public void onError(int code, String desc) {
                // 创建群组失败
                if(10021==code){
                    V2TIMManager.getInstance().joinGroup(roomid, roomid, new V2TIMCallback() {
                        @Override
                        public void onSuccess() {
                            // 加群成功
                            initBoard();
                        }
                        @Override
                        public void onError(int i, String s) {
                            // 加群失败
                            if(10013==i){
                                //已经是组员了
                                initBoard();
                            }else {
                                System.out.println("+++++加群失败"+s+i);
                            }
                        }
                    });
                }else if(10025==code){
                    //你创建的群组，已经是组员了
                    initBoard();
                }else {
                    System.out.println("+++创建群组失败！"+desc+"    code："+code);
                }
            }
        });

    }

    //初始化白板的 左侧  底部按钮
    public void initBoardMenu(){
        b_size = findViewById(R.id.board_size);
        b_cur =  findViewById(R.id.board_curpage);
        b_sum =  findViewById(R.id.board_sumpage);
        b_chu =  findViewById(R.id.b_chu);
        b_per =  findViewById(R.id.b_per);
        menu01RL = findViewById(R.id.menu01RL);
        menu02RL = findViewById(R.id.menu02RL);
        menu03RL = findViewById(R.id.menu03RL);
        menu04RL = findViewById(R.id.menu04RL);
        menu05RL = findViewById(R.id.menu05RL);
        menu06RL = findViewById(R.id.menu06RL);
        menu07RL = findViewById(R.id.menu07RL);
        menu08RL = findViewById(R.id.menu08RL);
        menu09RL = findViewById(R.id.menu09RL);
        menu10RL = findViewById(R.id.menu10RL);
        menu11RL = findViewById(R.id.menu11RL);
        menu12RL = findViewById(R.id.menu12RL);
        menu02color= findViewById(R.id.menu02color);
        menu03color= findViewById(R.id.menu03color);
        menu04color= findViewById(R.id.menu04color);
        select_menu = findViewById(R.id.select_menu);
        select_menu_top = findViewById(R.id.select_menu_top);

//        文件上传按钮
        resupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //文件上传  打开文件管理器
                uploadprogress.setVisibility(View.GONE);
                uploadfile.setText("开始上传");
                msgTips.setText("文件正在上传：");
                filename.setText("未选择任何文件");
                curfilename="";
                curfilepath="";
                intoFileManager();
            }
        });
        //关闭文件弹窗按钮
        close_select_resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭文件上传弹窗
                select_resources.setVisibility(View.GONE);
            }
        });

        //左侧功能栏  第一个按钮  鼠标按钮
        menu01 = findViewById(R.id.menu01);
        menu01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(0);
                setLeftmenustatus(true);
                menu01.setBackgroundResource(R.mipmap.menu_01_mouse1);
                b_cur.setText((mBoard.getFileBoardList(mBoard.getCurrentFile()).indexOf(mBoard.getCurrentBoard())+1)+"");
            }
        });
        //左侧功能栏  第2个按钮  画笔按钮
        menu02 = findViewById(R.id.menu02);
        menu02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置点击效果
                mBoard.setPenAutoFittingMode(TEduBoardController.TEduBoardPenFittingMode.NONE);
                mBoard.setToolType(1);
                setLeftmenustatus(true);
                menu02.setBackgroundResource(R.mipmap.menu_02_paint1);
                menu02color.setBackground(getResources().getDrawable(R.color.bg_selected_menu));

                //开启画笔弹窗
                View v_selectpaint = getLayoutInflater().inflate(R.layout.pw_selectpaint,null);
                pw_selectpaint  = new PopupWindow(v_selectpaint,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                pw_selectpaint.showAsDropDown(findViewById(R.id.menu02),(select_menu.getWidth()+menu02.getWidth())/2+5,-menu02.getHeight());
                ImageButton paint1 = v_selectpaint.findViewById(R.id.paint1);
                ImageButton paint2 = v_selectpaint.findViewById(R.id.paint2);
                ImageButton paint3 = v_selectpaint.findViewById(R.id.paint3);
                paint1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBoard.setToolType(1);
                        menu02color.setBackground(getResources().getDrawable(R.color.bg_selected_menu));
                        pw_selectpaint.dismiss();
                    }
                });
                paint2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBoard.setToolType(19);
                        mBoard.setHighlighterColor(new TEduBoardController.TEduBoardColor(Color.GREEN));
                        mBoard.setBrushThin(250);
                        menu02color.setBackground(getResources().getDrawable(R.color.bg_selected_menu));
                        pw_selectpaint.dismiss();
                    }
                });
                paint3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        松手拟合几何图形
                        mBoard.setPenAutoFittingMode(TEduBoardController.TEduBoardPenFittingMode.AUTO);
                        menu02color.setBackground(getResources().getDrawable(R.color.bg_selected_menu));
                        pw_selectpaint.dismiss();
                    }
                });
            }
        });
        //左侧功能栏  第3个按钮  文本 按钮
        menu03 = findViewById(R.id.menu03);
        menu03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(11);
                setLeftmenustatus(true);
                menu03.setBackgroundResource(R.mipmap.menu_03_text1);
                menu03color.setBackground(getResources().getDrawable(R.color.bg_selected_menu));
            }
        });
        //左侧功能栏  第4个按钮  选择几何图形按钮
        menu04 = findViewById(R.id.menu04);
        menu04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //开启 几何图形弹窗
                mBoard.setToolType(6);
                setLeftmenustatus(true);
                menu04.setBackgroundResource(R.mipmap.menu_04_jihe1);
                menu04color.setBackground(getResources().getDrawable(R.color.bg_selected_menu));
                //开启 几何图形弹窗
                View v_selectgeometry = getLayoutInflater().inflate(R.layout.pw_selectgeometry,null);
                if(pw_selecgeometry==null){
                    pw_selecgeometry  = new PopupWindow(v_selectgeometry,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                }
                pw_selecgeometry.showAsDropDown(findViewById(R.id.menu04),(select_menu.getWidth()+menu04.getWidth())/2+5,-menu04.getHeight());
                 geometry11 = v_selectgeometry.findViewById(R.id.geometry11);
                 geometry12 = v_selectgeometry.findViewById(R.id.geometry12);
                 geometry13 = v_selectgeometry.findViewById(R.id.geometry13);
                 geometry14 = v_selectgeometry.findViewById(R.id.geometry14);
                 geometry21 = v_selectgeometry.findViewById(R.id.geometry21);
                 geometry22 = v_selectgeometry.findViewById(R.id.geometry22);
                 geometry23 = v_selectgeometry.findViewById(R.id.geometry23);
                 geometry24 = v_selectgeometry.findViewById(R.id.geometry24);
                 geometry31 = v_selectgeometry.findViewById(R.id.geometry31);
                 geometry32 = v_selectgeometry.findViewById(R.id.geometry32);
                 geometry33 = v_selectgeometry.findViewById(R.id.geometry33);
                 geometry34 = v_selectgeometry.findViewById(R.id.geometry34);
                 geometry41 = v_selectgeometry.findViewById(R.id.geometry41);
                 geometry42 = v_selectgeometry.findViewById(R.id.geometry42);
                 geometry43 = v_selectgeometry.findViewById(R.id.geometry43);
                 geometry44 = v_selectgeometry.findViewById(R.id.geometry44);
                 geometry51 = v_selectgeometry.findViewById(R.id.geometry51);
                 geometry52 = v_selectgeometry.findViewById(R.id.geometry52);
                 geometry53 = v_selectgeometry.findViewById(R.id.geometry53);
                 geometry61 = v_selectgeometry.findViewById(R.id.geometry61);
                 geometry62 = v_selectgeometry.findViewById(R.id.geometry62);
                 geometry63 = v_selectgeometry.findViewById(R.id.geometry63);

                geometry11.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry11.setImageResource(R.mipmap.selectgeometry111);
                        setgeometrystatus();
                        mBoard.setToolType(4);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry12.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry12.setImageResource(R.mipmap.selectgeometry121);
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_DOTTED; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(4);
                        setgeometrystatus();
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry13.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry13.setImageResource(R.mipmap.selectgeometry131);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.endArrowType = TEduBoardController.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_NORMAL;//开始箭头 实心箭头TEDU_BOARD_ARROW_TYPE_SOLID 普通箭头 TEDU_BOARD_ARROW_TYPE_NORMAL 无箭头 TEDU_BOARD_ARROW_TYPE_NONE
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(4);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry14.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry14.setImageResource(R.mipmap.selectgeometry141);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.startArrowType = TEduBoardController.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_NORMAL;//开始箭头 实心箭头TEDU_BOARD_ARROW_TYPE_SOLID 普通箭头 TEDU_BOARD_ARROW_TYPE_NORMAL 无箭头 TEDU_BOARD_ARROW_TYPE_NONE
                        style.endArrowType = TEduBoardController.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_NORMAL;//开始箭头 实心箭头TEDU_BOARD_ARROW_TYPE_SOLID 普通箭头 TEDU_BOARD_ARROW_TYPE_NORMAL 无箭头 TEDU_BOARD_ARROW_TYPE_NONE
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(4);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry21.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry21.setImageResource(R.mipmap.selectgeometry211);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(6);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry22.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry22.setImageResource(R.mipmap.selectgeometry221);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(13);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry23.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry23.setImageResource(R.mipmap.selectgeometry231);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(5);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry24.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry24.setImageResource(R.mipmap.selectgeometry241);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(15);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry31.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry31.setImageResource(R.mipmap.selectgeometry311);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(6);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry32.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry32.setImageResource(R.mipmap.selectgeometry321);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(13);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry33.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry33.setImageResource(R.mipmap.selectgeometry331);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(5);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry34.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry34.setImageResource(R.mipmap.selectgeometry341);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(15);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry41.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry41.setImageResource(R.mipmap.selectgeometry411);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(20);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry42.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry42.setImageResource(R.mipmap.selectgeometry421);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(21);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry43.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry43.setImageResource(R.mipmap.selectgeometry431);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(22);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry44.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry44.setImageResource(R.mipmap.selectgeometry441);
                        setgeometrystatus();
                        mBoard.setToolType(TEduBoardController.TEduBoardToolType.TEDU_BOARD_TOOL_TYPE_COORDINATE);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry51.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry51.setImageResource(R.mipmap.selectgeometry511);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(20);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry52.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry52.setImageResource(R.mipmap.selectgeometry521);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(21);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry53.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(22);
                        geometry53.setImageResource(R.mipmap.selectgeometry531);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry61.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry61.setImageResource(R.mipmap.selectgeometry611);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(23);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry62.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry62.setImageResource(R.mipmap.selectgeometry621);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(24);
                        pw_selecgeometry.dismiss();
                    }
                });
                geometry63.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geometry63.setImageResource(R.mipmap.selectgeometry631);
                        setgeometrystatus();
                        TEduBoardController.TEduBoardGraphStyle style = new TEduBoardController.TEduBoardGraphStyle();
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //线类型，即虚实线  实线TEDU_BOARD_LINE_TYPE_SOLID = 1  虚线TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//填充类型, 即实心空心，只对平面几何图形有效   不填充TEduBoardFillTypeNONE = 1 填充TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(25);
                        pw_selecgeometry.dismiss();
                    }
                });

            }
        });
        //左侧功能栏  第5个按钮  框选工具按钮
        menu05 = findViewById(R.id.menu05);
        menu05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(9);
                setLeftmenustatus(true);
                menu05.setBackgroundResource(R.mipmap.menu_05_select1);
            }
        });
        //左侧功能栏  第6个按钮  选择教学工具按钮
        menu06 = findViewById(R.id.menu06);
        menu06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu06.setBackgroundResource(R.mipmap.menu_06_tools1);
                //开启教学工具弹窗
                View v_selectteachingtools = getLayoutInflater().inflate(R.layout.pw_select_teachingtools,null);
                if(pw_selectteachingtools==null){
                    pw_selectteachingtools  = new PopupWindow(v_selectteachingtools,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                }
                pw_selectteachingtools.showAsDropDown(findViewById(R.id.menu06),(select_menu.getWidth()+menu06.getWidth())/2+5,-menu06.getHeight());
                 teachingtools1 = v_selectteachingtools.findViewById(R.id.teachingtools1);
                 teachingtools2 = v_selectteachingtools.findViewById(R.id.teachingtools2);
                 teachingtools3 = v_selectteachingtools.findViewById(R.id.teachingtools3);
                 teachingtools4 = v_selectteachingtools.findViewById(R.id.teachingtools4);
                 teachingtools5 = v_selectteachingtools.findViewById(R.id.teachingtools5);
                teachingtools1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBoard.useMathTool(1);
                        teachingtools1.setImageResource(R.mipmap.selectteachingtools11);
                        pw_selectteachingtools.dismiss();
                    }
                });
                teachingtools2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBoard.useMathTool(2);
                        teachingtools2.setImageResource(R.mipmap.selectteachingtools21);
                        pw_selectteachingtools.dismiss();
                    }
                });
                teachingtools3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBoard.useMathTool(3);
                        teachingtools3.setImageResource(R.mipmap.selectteachingtools31);
                        pw_selectteachingtools.dismiss();
                    }
                });
                teachingtools4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBoard.useMathTool(4);
                        teachingtools4.setImageResource(R.mipmap.selectteachingtools41);
                        pw_selectteachingtools.dismiss();
                    }
                });
                teachingtools5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBoard.useMathTool(5);
                        teachingtools5.setImageResource(R.mipmap.selectteachingtools51);
                        pw_selectteachingtools.dismiss();
                    }
                });
            }
        });
        //左侧功能栏  第7个按钮  移动缩放按钮
        menu07 = findViewById(R.id.menu07);
        menu07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(12);
                b_size.setText(mBoard.getBoardScale()+"%");
                setLeftmenustatus(true);
                menu07.setBackgroundResource(R.mipmap.menu_07_move1);
            }
        });
        //左侧功能栏  第8个按钮  橡皮擦按钮
        menu08 = findViewById(R.id.menu08);
        menu08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(2);
                setLeftmenustatus(true);
                menu08.setBackgroundResource(R.mipmap.menu_08_earsea1);
                //开启橡皮擦弹窗
                View v_selecteraser = getLayoutInflater().inflate(R.layout.pw_selecteraser,null);
                if(pw_selecteraser==null){
                    pw_selecteraser  = new PopupWindow(v_selecteraser,ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
                }
                pw_selecteraser.showAsDropDown(findViewById(R.id.menu08),(select_menu.getWidth()+menu08.getWidth())/2+5,-menu08.getHeight());
                ImageButton eraser1 = v_selecteraser.findViewById(R.id.eraser1);
                ImageButton eraser2 = v_selecteraser.findViewById(R.id.eraser2);
                eraser1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         if(mBoard.isPiecewiseErasureEnable()){
                             mBoard.setPiecewiseErasureEnable(false);
                         }
                        eraser1.setImageResource(R.mipmap.selecteraser11);
                        pw_selecteraser.dismiss();
                    }
                });
                eraser2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mBoard.isPiecewiseErasureEnable()){
                            mBoard.setEraserSize(4);
                            mBoard.setPiecewiseErasureEnable(true);
                        }

                        eraser2.setImageResource(R.mipmap.selecteraser21);
                        pw_selecteraser.dismiss();
                    }
                });
            }
        });
        //左侧功能栏  第9个按钮  清空按钮
        menu09 = findViewById(R.id.menu09);
        menu09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.clear(false);
                mBoard.setToolType(0);
            }
        });
        //左侧功能栏  第10个按钮  激光笔 按钮
        menu10 = findViewById(R.id.menu10);
        menu10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(3);
                setLeftmenustatus(true);
                menu10.setBackgroundResource(R.mipmap.menu_101);
            }
        });

        //左侧功能栏  第11个按钮  设置 按钮
        menu11 = findViewById(R.id.menu11);
        menu11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout setBoardWindow = findViewById(R.id.setBoardWindow);
                if(setBoardWindow.getVisibility()==View.VISIBLE){
                    setBoardWindow.setVisibility(View.GONE);
                }else {
                    setBoardWindow.setVisibility(View.VISIBLE);
                    //使用适配器将ViewPager与Fragment绑定在一起
                    ViewPager setboardviewPager = findViewById(R.id.set_board_tar_bar_viewpage);
                    setboardviewPager.setOffscreenPageLimit(3);
                    //构造参数
                    Set_eraser_Fragment set_eraser_fragment = new Set_eraser_Fragment("4");
                    Set_geometry_Fragment set_geometry_fragment = new Set_geometry_Fragment();
                    Set_text_Fragment set_text_fragment = new Set_text_Fragment();
                    Set_paint_Fragment set_paint_fragment = new Set_paint_Fragment();
                    Set_bg_Fragment set_bg_fragment = new Set_bg_Fragment();
                    Set_more_Fragment set_more_fragment = new Set_more_Fragment();
                    Set_Highlighter_Fragment set_highlighter_fragment = new Set_Highlighter_Fragment();
//                设置弹窗  四种状态   画笔设置|文本设置|几何图形设置|板擦设置 +  背景设置  +  更多设置
                    String[] sl=null;
                    mTabFragmenList.clear();
                    if (mBoard.getToolType() == 1) {
                        sl = new String[]{"画笔设置", "背景设置", "更多设置"};
                        mTabFragmenList.add(set_paint_fragment);
                    } else if (mBoard.getToolType() == 11) {
                        sl = new String[]{"文本设置", "背景设置", "更多设置"};
                        mTabFragmenList.add(set_text_fragment);
                    } else if (mBoard.getToolType() == 6) {
                        sl = new String[]{"几何图形设置", "背景设置", "更多设置"};
                        mTabFragmenList.add(set_geometry_fragment);
                    } else if (mBoard.getToolType() == 2) {
                        sl = new String[]{"板擦设置", "背景设置", "更多设置"};
                        mTabFragmenList.add(set_eraser_fragment);
                    } else if (mBoard.getToolType() == 19) {   //这里是荧光笔设置
                        sl = new String[]{"画笔设置", "背景设置", "更多设置"};
                        mTabFragmenList.add(set_highlighter_fragment);
                    }else {
                        sl = new String[]{"背景设置", "更多设置"};
                    }

                    mTabFragmenList.add(set_bg_fragment);
                    mTabFragmenList.add(set_more_fragment);

                    SetBrd_TabBarAdapter setBrd_tabBarAdapter = new SetBrd_TabBarAdapter(getSupportFragmentManager());

                    setBrd_tabBarAdapter.setmTitles(sl);
                    setBrd_tabBarAdapter.setmFragment(mTabFragmenList);
                    //改变Fragment管理器里面的Tag值，让他每次都重新创建新的Fragment  达到动态切换的效果
                    setBrd_tabBarAdapter.changeId();
                    setBrd_tabBarAdapter.notifyDataSetChanged();
                    setboardviewPager.setAdapter(setBrd_tabBarAdapter);
                    TabLayout mTabLayout = findViewById(R.id.setboard_tar_bar);
                    mTabLayout.setupWithViewPager(setboardviewPager);

                }

            }
        });

        //左侧功能栏  第12个按钮  状态栏 收起 展开按钮
        menu12 = findViewById(R.id.menu12);
        menu12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.setBoardWindow).setVisibility(View.GONE);
//                收起来
                if(menu_l_status){
                    select_menu_top.setVisibility(View.VISIBLE);
                    menu03RL.setVisibility(View.GONE);
                    menu04RL.setVisibility(View.GONE);
                    menu05RL.setVisibility(View.GONE);
                    menu06RL.setVisibility(View.GONE);
                    menu07RL.setVisibility(View.GONE);
                    menu09RL.setVisibility(View.GONE);
                    menu10RL.setVisibility(View.GONE);
                    menu12.setBackgroundResource(R.mipmap.menu_12_up);
                    menu_l_status=false;
                }else {
                    select_menu_top.setVisibility(View.GONE);
                    menu03RL.setVisibility(View.VISIBLE);
                    menu04RL.setVisibility(View.VISIBLE);
                    menu05RL.setVisibility(View.VISIBLE);
                    menu06RL.setVisibility(View.VISIBLE);
                    menu07RL.setVisibility(View.VISIBLE);
                    menu09RL.setVisibility(View.VISIBLE);
                    menu10RL.setVisibility(View.VISIBLE);
                    menu12.setBackgroundResource(R.mipmap.menu_12_down);
                    menu_l_status=true;
                }
            }
        });

//        b_size.setText(mBoard.getBoardRatio());

        //底部功能栏  第1个按钮  撤销按钮
        menub01 = findViewById(R.id.menub01);
        menub01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus(true);
                menub01.setBackgroundResource(R.mipmap.menu_b011);
                mBoard.undo();
            }
        });
        //底部功能栏  第2个按钮  重做按钮
        menub02 = findViewById(R.id.menub02);
        menub02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus(true);
                menub02.setBackgroundResource(R.mipmap.menu_b021);
                mBoard.redo();}
        });
        //底部功能栏  第3个按钮  归位 按钮（缩放比例 成100%）
        menub03 = findViewById(R.id.menub03);
        menub03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus(true);
                menub03.setBackgroundResource(R.mipmap.menu_b031);
                mBoard.setBoardScale(100);
            }
        });
        //底部功能栏  第4个按钮  缩小显示比例 按钮
        menub04 = findViewById(R.id.menub04);
        menub04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus(true);
                menub04.setBackgroundResource(R.mipmap.menu_b041);
                if( Integer.parseInt(b_size.getText().toString())-10>=100){
                    mBoard.setBoardScale( Integer.parseInt(b_size.getText().toString())-10);
                    b_size.setText(mBoard.getBoardScale()+"");
                }else {
                    mBoard.setBoardScale(100);
                    b_size.setText(mBoard.getBoardScale()+"");
                }
            }
        });
        //底部功能栏  第5个按钮  放大显示比例 按钮
        menub05 = findViewById(R.id.menub05);
        menub05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus(true);
                menub05.setBackgroundResource(R.mipmap.menu_b051);
                if( Integer.parseInt(b_size.getText().toString())+10<=300){
                    mBoard.setBoardScale( Integer.parseInt(b_size.getText().toString())+10);
                    b_size.setText(mBoard.getBoardScale()+"");
                }else{
                    mBoard.setBoardScale(300);
                    b_size.setText(mBoard.getBoardScale()+"");
                }
            }
        });
        //底部功能栏  第6个按钮  向前翻页 按钮
        menub06= findViewById(R.id.menub06);
        menub06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus(true);
                menub06.setBackgroundResource(R.mipmap.menu_b061);
                TEduBoardController.TEduBoardSnapshotInfo path = new TEduBoardController.TEduBoardSnapshotInfo();
                if(CurType=="Board"){
                    path.path = getCacheDir()+"/"+CurBoardID+".png";
                }else {
                    path.path= getCacheDir()+"/"+CurFileID+".png";
                }

                mBoard.snapshot(path);
                mBoard.prevBoard();
                b_cur.setText((mBoard.getFileBoardList(mBoard.getCurrentFile()).indexOf(mBoard.getCurrentBoard())+1)+"");
            }
        });
        //底部功能栏  第7个按钮  向后翻页 按钮
        menub07 = findViewById(R.id.menub07);
        menub07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus(true);
                menub07.setBackgroundResource(R.mipmap.menu_b071);
                TEduBoardController.TEduBoardSnapshotInfo path = new TEduBoardController.TEduBoardSnapshotInfo();
                if(CurType=="Board"){
                    path.path = getCacheDir()+"/"+CurBoardID+".png";
                }else {
                    path.path= getCacheDir()+"/"+CurFileID+".png";
                }
                mBoard.snapshot(path);
                mBoard.nextBoard();
                b_cur.setText((mBoard.getFileBoardList(mBoard.getCurrentFile()).indexOf(mBoard.getCurrentBoard())+1)+"");
            }
        });
        //底部功能栏  第8个按钮  新增一页 按钮
        menub08 = findViewById(R.id.menub08);
        menub08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加一页
                setLeftmenustatus(true);
                menub08.setBackgroundResource(R.mipmap.menu_b081);
                mBoard.addBoard(null,TEduBoardController.TEduBoardImageFitMode.TEDU_BOARD_IMAGE_FIT_MODE_CENTER, TEduBoardController.TEduBoardBackgroundType.TEDU_BOARD_BACKGROUND_IMAGE,true);
            }
        });
        //底部功能栏  第9个按钮  删除当前页 按钮
        menub09 = findViewById(R.id.menub09);
        menub09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus(true);
                menub09.setBackgroundResource(R.mipmap.menu_b091);
                //删除一页
                if(mBoard.getBoardList().size()>1){
                    mBoard.deleteBoard(null);
                }
            }
        });
        //底部功能栏  第10个按钮  折叠展开底部菜单栏 按钮
        menub10 = findViewById(R.id.menub10);
        menub10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLeftmenustatus(true);//左边菜单栏  状态都改为未选中
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


    //设置菜单  一级弹窗 的  显示状态
    public void setLeftmenustatus(Boolean issetbg){
        menu01.setBackgroundResource(R.mipmap.menu_01_mouse);
        menu02.setBackgroundResource(R.mipmap.menu_02_paint);
        menu02color.setBackground(getResources().getDrawable(R.color.bg_select_menu));
        menu03.setBackgroundResource(R.mipmap.menu_03_text);
        menu03color.setBackground(getResources().getDrawable(R.color.bg_select_menu));
        menu04.setBackgroundResource(R.mipmap.menu_04_jihe);
        menu04color.setBackground(getResources().getDrawable(R.color.bg_select_menu));
        menu05.setBackgroundResource(R.mipmap.menu_05_select);
        menu06.setBackgroundResource(R.mipmap.menu_06_tools);
        menu07.setBackgroundResource(R.mipmap.menu_07_move);
        menu08.setBackgroundResource(R.mipmap.menu_08_earsea);
        menu10.setBackgroundResource(R.mipmap.menu_10);
        menub01.setBackgroundResource(R.mipmap.menu_b01);
        menub02.setBackgroundResource(R.mipmap.menu_b02);
        menub03.setBackgroundResource(R.mipmap.menu_b03);
        menub04.setBackgroundResource(R.mipmap.menu_b04);
        menub05.setBackgroundResource(R.mipmap.menu_b05);
        menub06.setBackgroundResource(R.mipmap.menu_b06);
        menub07.setBackgroundResource(R.mipmap.menu_b07);
        menub08.setBackgroundResource(R.mipmap.menu_b08);
        menub09.setBackgroundResource(R.mipmap.menu_b09);
        if(issetbg&&findViewById(R.id.setBoardWindow).getVisibility()==View.VISIBLE){
            findViewById(R.id.setBoardWindow).setVisibility(View.GONE);
        }
    }

    /**
     * 白板控制
     *
     * @param extension 消息类型
     * @param action    操作动作
     * @param id        操作对象ID
     */

    public void drawAuthority(String extension, String action, String id) {

    }

    public void sendMsg(Chat_Msg msg){
        // 创建文本消息
        //        V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createTextMessage( "{\"text\":\""+msg.getContent()+"\",\"date\":\""+msg.getDate()+"\"}");
        // 发送消息
        final V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(msg.getContent().getBytes(), "", "TBKTExt".getBytes());
        if (v2TIMMessage.getCustomElem() != null) {
            v2TIMMessage.getCustomElem().setExtension("TBKTExt".getBytes());
            //设置头像  名称  +++
            v2TIMMessage.getCustomElem().setDescription("");
            v2TIMMessage.getCustomElem().setData(msg.getContent().getBytes());
        }

        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, null,roomid, V2TIMMessage.V2TIM_PRIORITY_NORMAL, false, null, new V2TIMSendCallback<V2TIMMessage>() {
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

    //聊天调用 此方法  设置 全员是是否 禁言
    public void stopAllchat(Boolean isstop){
        // 全员禁言
        V2TIMGroupInfo info = new V2TIMGroupInfo();
        info.setGroupID(roomid);
        info.setAllMuted(isstop);
        V2TIMManager.getGroupManager().setGroupInfo(info, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // 全员禁言成功
                ChatRoomFragment f = (ChatRoomFragment)getmFragmenglist().get(1);
                EditText ed =  f.getView().findViewById(R.id.inputtext);
                Switch sw = f.getView().findViewById(R.id.stopchat);
                sw.setChecked(isstop);
                if(isstop){
                    ed.setHint("全体禁言成功！");
                    //禁言按钮设置打开状态
                    sw.setChecked(true);
                }else {
                    ed.setHint("取消全体禁言！");
                    sw.setChecked(false);
                }
            }

            @Override
            public void onError(int code, String desc) {
                // 全员禁言失败
                System.out.println("+++全员禁言失败！"+code+desc);
                ChatRoomFragment f = (ChatRoomFragment)getmFragmenglist().get(1);
                Switch sw = f.getView().findViewById(R.id.stopchat);
                sw.setChecked(false);
                if(code==10007){
                    sw.setChecked(false);
                    EditText ed =  f.getView().findViewById(R.id.inputtext);
                    ed.setHint("你没有禁言权限！");

                }

            }
        });
    }


    public void onExitLiveRoom() {

        HttpActivity.overClass("leave", "skydt", this);
        mTRTCCloud.exitRoom();
    }

    //下课 销毁白板实例
    public void destroyBoard() {
        System.out.println("+++执行了销毁函数");
        CurType=null;
        mBoard.removeCallback(mBoardCallback);
        mBoard.uninit();
        BoardStatus=false;
        addBoardtoFragmentstatus=false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mBoard!=null){
            destroyBoard();
        }
    }


    //打开手机文件管理器
    private void intoFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 12); //requestCode==1   用来记录是这里的操作
    }

    //文件管理器选择了回调  requestCode==12的时候   支持的文件类型  MP3  MP4  doc  docx ppt  pptx  pdf  png  jpg
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK&&requestCode==12) {
            Uri uri = data.getData();               // 得到uri   用工具处理Uri得到绝对路径
            String path= UriUtils.getFileAbsolutePath(this,uri);
            File ff = new File(path);
            String name = ff.getName();
            System.out.println("+++path:"+path);   //输出文件绝对路径
            System.out.println("+++name:"+name);   //输出文件大小
            Long ffsize = ff.length();
            if(name.endsWith(".mp3")){
                if(ffsize>104857600){filename.setText("mp3文件过大！"); // mp3文件过大
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
            }else if(name.endsWith(".mp4")){
                if(ffsize>104857600){ filename.setText("mp4文件过大,请重新选择!");// mp4文件过大
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
            }else if(name.endsWith(".pdf")){
                if(ffsize>20971520){filename.setText("pdf文件过大,请重新选择!");// pdf文件过大
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
                System.out.println("+++合法上传pdf格式");
            }else if(name.endsWith(".doc")||name.endsWith(".docx")){
                System.out.println("+++合法上传doc格式");
                if(ffsize>20971520){filename.setText("doc文件过大,请重新选择!");// doc文件过大
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
            }else if(name.endsWith(".ppt")||name.endsWith(".pptx")){
                System.out.println("+++合法上传ppt格式");
                if(ffsize>20971520){filename.setText("ppt文件过大,请重新选择!");// ppt文件过大
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
                System.out.println("+++合法上传图片"+name);
            }else if(name.endsWith(".png")||name.endsWith(".jpg")){
                if(ffsize>104857600){filename.setText("图片文件过大,请重新选择!"); // 图片文件过大
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
                System.out.println("+++合法上传图片"+name);
            }else {
                filename.setText("不支持此类格式文件,请重新选择!");
                System.out.println("+++该种数据不支持上传");
            }
            if(isincludeType){
                //符合上传条件了再来记录当前 选中的文件name path
                curfilename=name;
                curfilepath=path;
            }
        }
    }

    //初始化存储桶
    private void InitBucket(Context context) {
        // String secretId = "AKID5ybx2rPggPr23oHUR8YhZBWZLr6xaw2r"; //永久密钥 secretId
        // String secretKey = "auxjESQCk11lEQL0O5WhbEZdRyEDwOYR"; //永久密钥 secretKey
        // keyDuration 为请求中的密钥有效期，单位为秒（临时密匙的时候有效）
        QCloudCredentialProvider myCredentialProvider =
                new ShortTimeCredentialProvider(MsecretId, MsecretKey, 300);
        // 存储桶所在地域简称，例如广州地区是 ap-guangzhou
        String region = "ap-guangzhou";

        // 创建 CosXmlServiceConfig 对象，根据需要修改默认的配置参数
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) // 使用 HTTPS 请求, 默认为 HTTP 请求
                .builder();
        // 初始化 COS Service，获取实例
        cosXmlService = new CosXmlService(context, serviceConfig, myCredentialProvider);
    }

    //上传文件到存储桶  存储桶公共路径  、  文件本地路径  、   文件名称   、   是否删除本地缓存
    private void UploadToBucket(String cosprefix,String path,String name,boolean isdelete){
        isquestion=false;
        //cosprefix  存储桶目录    path 本地文件路径   name  名称
        // 访问 COS 服务  上传对象
        // 初始化 TransferConfig，这里使用默认配置，如果需要定制，请参考 SDK 接口文档
                TransferConfig transferConfig = new TransferConfig.Builder().build();
        // 初始化 TransferManager
                TransferManager transferManager = new TransferManager(cosXmlService,transferConfig);

        // 存储桶名称，由bucketname-appid 组成，appid必须填入，可以在COS控制台查看存储桶名称。 https://console.cloud.tencent.com/cos5/bucket
        // String bucket = Mbucket;

                //  class/年/月/日/subjectId/roomId/res/xxxx.ppt   资源上传目录
                String cosPath = cosprefix + name; //对象在存储桶中的位置标识符，即称对象键    文件夹
                String srcPath = new File(path).toString(); //本地文件的绝对路径

        //若存在初始化分块上传的 UploadId，则赋值对应的 uploadId 值用于续传；否则，赋值 null
                String uploadId = null;
        // 上传文件
                COSXMLUploadTask cosxmlUploadTask = transferManager.upload(Mbucket, cosPath,srcPath, uploadId);

        //设置上传进度回调
                cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                    @Override
                    public void onProgress(long complete, long target) {
                        proBar.setProgress((int)(complete*100/target));
                    }
                });
        //设置返回结果回调
                cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        COSXMLUploadTask.COSXMLUploadTaskResult uploadResult =
                                (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                        if(name.endsWith("doc")||name.endsWith("pdf")||name.endsWith("docx")){
         //doc  pdf  docx  三种文件调用接口转码
                            runnablere_mBoardaddResouce = () -> {
                                try {
                                        uploadfile.setText("正在转换");
                                        msgTips.setText("文件正在转换：");
                                        proBar.setProgress(0);
                                        CreateTranscode(result.accessUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            };
                        }else if(name.endsWith("png")||name.endsWith("jpg")){
         // 图片格式文件
                            runnablere_mBoardaddResouce = () -> {
                                try {
                                    uploadfile.setText("正在加载");
                                    msgTips.setText("文件正在加载：");
                                    proBar.setProgress(95);
                                    mBoard.addElement(TEduBoardController.TEduBoardElementType.TEDU_BOARD_ELEMENT_IMAGE,result.accessUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            };
                        }else if(name.endsWith("mp3")){
          //png  音频文件
                            runnablere_mBoardaddResouce = () -> {
                                try {
                                    uploadfile.setText("正在加载");
                                    msgTips.setText("文件正在加载：");
                                    proBar.setProgress(95);
                                    mBoard.addElement(TEduBoardController.TEduBoardElementType.TEDU_BOARD_ELEMENT_AUDIO,result.accessUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            };

                        } else{
                            if(name.endsWith("ppt")||name.endsWith("pptx")){
         //ppt  pptx  采用新的转码方式  直接添加
                                runnablere_mBoardaddResouce = () -> {
                                                try {
                                                    uploadfile.setText("正在加载");
                                                    msgTips.setText("文件正在加载：");
                                                    proBar.setProgress(95);
                                                    mBoardAddTranscodeFile(name,result.accessUrl+"?for_tiw=1");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                };

                            }else {
         //  mp4 格式的数据  直接添加
                                runnablere_mBoardaddResouce = () -> {
                                    try {
                                        uploadfile.setText("正在加载");
                                        msgTips.setText("文件正在加载：");
                                        proBar.setProgress(95);
                                        mBoard.addVideoFile(result.accessUrl,name,true);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                };

                            }
                        }
                    }
                    // 如果您使用 kotlin 语言来调用，请注意回调方法中的异常是可空的，否则不会回调 onFail 方法，即：
                    // clientException 的类型为 CosXmlClientException?，serviceException 的类型为 CosXmlServiceException?
                    @Override
                    public void onFail(CosXmlRequest request,
                                       @Nullable CosXmlClientException clientException,
                                       @Nullable CosXmlServiceException serviceException) {
                        System.out.println("+++上传失败"+clientException.toString());
                        System.out.println("+++上传失败"+serviceException.toString());
                        if (clientException != null) {
                            clientException.printStackTrace();
                        } else {
                            serviceException.printStackTrace();
                        }
                    }
                });
        //设置任务状态回调, 可以查看任务过程
                cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
                    @Override
                    public void onStateChanged(TransferState state) {
                        System.out.println("+++任务状态回调,"+state.toString().equals("COMPLETED"));
                        if(state.toString().equals("COMPLETED")){
                            if(isdelete){
                                File ff = new File(path);
                                ff.delete();
                            }
                            curfilename="";   // 上传完成，记录当前选择的文件名称清空
                            curfilepath="";   // 上传完成，记录当前选择的文件路径清空
                            handlerCount.postDelayed(runnablere_mBoardaddResouce, 500); // 1000ms后执行这个runnablerefreshStatus
                        }

//                                CONSTRAINED,
//                                WAITING,
//                                IN_PROGRESS,
//                                PAUSED,
//                                RESUMED_WAITING,
//                                COMPLETED,
//                                CANCELED,
//                                FAILED,
//                                UNKNOWN;
                    }
                });
    }

    //白板添加文件  添加上去的名称， 文件的网络地址
    private void mBoardAddTranscodeFile(String name,String url) {
        TEduBoardController.TEduBoardTranscodeFileResult config = new TEduBoardController.TEduBoardTranscodeFileResult(name,url);
        mBoard.addTranscodeFile(config,true);
    }

    //为了 接口获取到的数据（字符串）转成JSON格式
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

    //创建转码任务 （pdf  doc   docx）
    private void CreateTranscode(String slink) {
        //开始创建转码任务
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String SecretId  = MsecretId;
                    String SecretKey = MsecretKey;
                    String Region    = MRegion;
                    String SdkAppId  = SDKappID+"";
                    String link      = slink;
                    URL url = new URL("http://www.cn901.com/ShopGoods/ajax/livePlay_CreateTranscode.do?"
                            + "SecretId=" + SecretId + "&SecretKey=" + SecretKey + "&Region=" + Region
                            + "&SdkAppId="+ SdkAppId   + "&link="+ link);

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

                    try{
                        String backLogJsonStr = buffer.toString();
                        JSONObject json = stringToJson(backLogJsonStr);
                        String status = json.get("Status").toString();
                        if(status.equals("success")){
                            System.out.println("+++转码接口任务成功：返回了taskId:;"+json.get("TaskId"));
//                          上传任务创建成功 就要调用查看转码状态
                            proBar.setProgress(0);
                            msgTips.setText("文件正在转码：");
                            uploadfile.setText("正在转码");
                            DescribeTranscodehandler(MsecretId,MsecretKey,Region,SdkAppId,json.get("TaskId").toString(),mBoard,proBar);
                        }else {
//                            失败，页面提示“创建文件转换任务失败，请稍后重试。”
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


    //创建 转码任务 定时请求器
    public static void DescribeTranscodehandler(String secretId,String secretKey,String region,String sdkAppId,String taskId,TEduBoardController mBoard,ProgressBar progressBar) {
        Boardtimer.schedule(new TimerTask() {
            @Override
            public void run() {
                DescribeTranscode(secretId,secretKey,region,sdkAppId,taskId,mBoard,progressBar);
            }
        },500,500);

//        runnablere_mBoardaddResouce = () -> {
//            try {
//                DescribeTranscode(secretId,secretKey,region,sdkAppId,taskId);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        };
//        handlerCount.postDelayed(runnablere_mBoardaddResouce, 500); // 500ms后执行这个runnablerefreshStatus
    }

    //获取转码进度
    private static void DescribeTranscode(String secretId,String secretKey,String region,String sdkAppId,String taskId,TEduBoardController mBoard,ProgressBar progressBar) {
        new Thread(new Runnable() {
            String ResultUrl =null;
            @Override
            public void run() {
                try{
                    String SecretId  = secretId;
                    String SecretKey = secretKey;
                    String Region    = region;
                    String SdkAppId  = sdkAppId;
                    String TaskId    = taskId;
                    URL url = new URL("http://www.cn901.com/ShopGoods/ajax/livePlay_DescribeTranscode.do?"
                            + "SecretId=" + SecretId + "&SecretKey=" + SecretKey + "&Region=" + Region
                            + "&SdkAppId="+ SdkAppId  + "&TaskId="+ TaskId);
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
//        "Status": "PROCESSING",  "Progress": "10",//转码进度，区间0--100 "ResultUrl": "",//装换成功之后的预览路径  "Pages": "3", "Title": "七年级测试卷", "Resolution": "1280*800"
//        其中Status包含  - QUEUED: 正在排队等待转换  - PROCESSING: 转换中 - FINISHED: 转换完成 - fail:装换异常，需要提示“文件转换任务失败，请稍后重试”（自定义提示）
                    try{
                        String backLogJsonStr = buffer.toString();
                        System.out.println("+++转码任务接口返回数据："+backLogJsonStr);
                        JSONObject json = stringToJson(backLogJsonStr);
                        System.out.println("+++转码任务接口返回状态："+json.get("Status"));
                        String status = json.get("Status").toString();
                        if(status.equals("FINISHED")){
                            ResultUrl =  json.get("ResultUrl").toString();
                            System.out.println("+++关闭查询转码进度任务："+ResultUrl);
                            Boardtimer.cancel();
                            runnablere_mBoardaddResouce = () -> {
                                try {
                                    TEduBoardController.TEduBoardTranscodeFileResult config = new TEduBoardController.TEduBoardTranscodeFileResult(json.get("Title").toString(), json.get("ResultUrl").toString(), Integer.parseInt(json.get("Pages").toString()), json.get("Resolution").toString());
                                    mBoard.addTranscodeFile(config,true);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            };
                            handlerCount.postDelayed(runnablere_mBoardaddResouce,2000); //
                        }else if(status.equals("fail")){
                            Boardtimer.cancel();
                            System.out.println("+++查询转码进度任务失败：");
//                            装换异常，需要提示“文件转换任务失败，请稍后重试”（自定义提示）
                        }else if(status.equals("PROCESSING")){
                            progressBar.setProgress(Integer.parseInt(json.get("Progress").toString()));
                            System.out.println("+++查询转码进度任务进行中......："+json.get("Progress"));
//                            json.get("PROCESSING").toString();设置进度条
                        }else if(status.equals("QUEUED")){
                            System.out.println("+++查询转码进度任务队列中......："+json.get("Progress"));
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


<<<<<<< HEAD
//白板快照   王璐瑶调用
    public static void ScreenShotBoard(Context context,String answerQuestionId,TEduBoardController mBoard){
=======
    //白板快照   答题部分   保存当前题目  题面
    public  void ScreenShotBoard(Context context,String answerQuestionId,TEduBoardController mBoard){
>>>>>>> a778572e660935aea05ec2124a4ace92362a760e
        isquestion=true;//调用的时候把这个值设置为true  后面改变存储的路径
        //白板快照
        TEduBoardController.TEduBoardSnapshotInfo path = new TEduBoardController.TEduBoardSnapshotInfo();
        path.path=context.getCacheDir()+"/"+answerQuestionId+".png";
        mBoard.snapshot(path);
        // class/年/月/日/subjectId/roomId/question/questionId.png   王璐瑶那块
    }

    // 清空 几何工具 弹窗  左侧的选中状态
    private void setgeometrystatus() {
        geometry11.setImageResource(R.mipmap.selectgeometry11);
        geometry12.setImageResource(R.mipmap.selectgeometry12);
        geometry13.setImageResource(R.mipmap.selectgeometry13);
        geometry14.setImageResource(R.mipmap.selectgeometry14);
        geometry21.setImageResource(R.mipmap.selectgeometry21);
        geometry22.setImageResource(R.mipmap.selectgeometry22);
        geometry23.setImageResource(R.mipmap.selectgeometry23);
        geometry24.setImageResource(R.mipmap.selectgeometry24);
        geometry31.setImageResource(R.mipmap.selectgeometry31);
        geometry32.setImageResource(R.mipmap.selectgeometry32);
        geometry33.setImageResource(R.mipmap.selectgeometry33);
        geometry34.setImageResource(R.mipmap.selectgeometry34);
        geometry41.setImageResource(R.mipmap.selectgeometry41);
        geometry42.setImageResource(R.mipmap.selectgeometry42);
        geometry43.setImageResource(R.mipmap.selectgeometry43);
        geometry44.setImageResource(R.mipmap.selectgeometry44);
        geometry51.setImageResource(R.mipmap.selectgeometry51);
        geometry52.setImageResource(R.mipmap.selectgeometry52);
        geometry53.setImageResource(R.mipmap.selectgeometry53);
        geometry61.setImageResource(R.mipmap.selectgeometry61);
        geometry62.setImageResource(R.mipmap.selectgeometry62);
        geometry63.setImageResource(R.mipmap.selectgeometry63);
    }

    //用来设置颜色的时候  控制菜单一栏  画笔 文本  几何工具右下角的小颜色显示   为了给设置的fragment调用
    public void forSetFragmentSet(String item,String params){
        if(item=="geometrycolor"){
            if(params=="gray"){
                menu04color.setImageResource(R.mipmap.text_gray);
            }else if(params=="black"){
                menu04color.setImageResource(R.mipmap.text_black);
            }else if(params=="blue"){
                menu04color.setImageResource(R.mipmap.text_blue);
            }else if(params=="green"){
                menu04color.setImageResource(R.mipmap.text_green);
            }else if(params=="yellow"){
                menu04color.setImageResource(R.mipmap.text_yellow);
            }else if(params=="red"){
                menu04color.setImageResource(R.mipmap.text_red);
            }
        }else if(item=="paintcolor"){
            if(params=="gray"){
                menu02color.setImageResource(R.mipmap.text_gray);
            }else if(params=="black"){
                menu02color.setImageResource(R.mipmap.text_black);
            }else if(params=="blue"){
                menu02color.setImageResource(R.mipmap.text_blue);
            }else if(params=="green"){
                menu02color.setImageResource(R.mipmap.text_green);
            }else if(params=="yellow"){
                menu02color.setImageResource(R.mipmap.text_yellow);
            }else if(params=="red") {
                menu02color.setImageResource(R.mipmap.text_red);
            }
        }else if(item=="textcolor"){
            if(params=="gray"){
                menu03color.setImageResource(R.mipmap.text_gray);
            }else if(params=="black"){
                menu03color.setImageResource(R.mipmap.text_black);
            }else if(params=="blue"){
                menu03color.setImageResource(R.mipmap.text_blue);
            }else if(params=="green"){
                menu03color.setImageResource(R.mipmap.text_green);
            }else if(params=="yellow"){
                menu03color.setImageResource(R.mipmap.text_yellow);
            }else if(params=="red"){
                menu03color.setImageResource(R.mipmap.text_red);
            }
        }
    }

}

