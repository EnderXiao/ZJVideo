package com.example.trtc_client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.VolumeShaper;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.Time;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private Timer timer;

    private static Handler handlerCount = new Handler();
    private static Runnable runnablere_mBoardaddResouce;
    private static Timer Boardtimer = new Timer();  // ??????????????????  ????????????????????????

    //Tabbar??????Fragment
    private List<Fragment> mFragmenglist = new ArrayList<>();
    public List<Fragment> getmFragmenglist() {
        return mFragmenglist;
    }
    public void setmFragmenglist(List<Fragment> mFragmenglist) {this.mFragmenglist = mFragmenglist;}

    public final static String TAG = "Ender_MainActivity";
    public static TRTCCloud mTRTCCloud;
    private TRTCCloudDef.TRTCParams myTRTCParams;
    private TXCloudVideoView mTXCVVTeacherPreviewView;
    private RelativeLayout teacherTRTCBackground;
    private ImageView boardBtn;     //????????????
    private ImageView canvasBtn;    //?????? ??????  ?????????????????????

    private ImageView contentBtn;  //???????????????  ?????????????????????
    private ImageView memberBtn;
    private ImageView handBtn;
    private ImageView cameraBtn;   //?????????????????????
    private ImageView audioBtn;

    private ImageView exit_btn;     //????????????
    private TextView teacher_name_view; //??????????????????
    private ImageView overClassBtn;
    private Group group_btn;

    public static String mTeacherId;

    private boolean musicOn = true;
    private boolean cameraOn = true;

    // ????????????????????????
    public static ArrayList<String> mUserList = new ArrayList<String>();
    public static ArrayList<String> mCameraUserList = new ArrayList<String>();
    public static ArrayList<String> mMicrophoneUserList = new ArrayList<String>();
    public static int mUserCount = 0;

    private String MRegion="ap-guangzhou"	;                                          //???????????????????????? 	ap-guangzhou
    private String Mbucket = "zjkj-1258767809";                                        //???????????????   ???bucketname-appid ?????????appid????????????
    private String MsecretId = "AKID5ybx2rPggPr23oHUR8YhZBWZLr6xaw2r";                 //?????????   ???????????? secretId
    private String MsecretKey = "auxjESQCk11lEQL0O5WhbEZdRyEDwOYR";                    //?????????    ???????????? secretKey

    private  String UserSig ="";                                                        //??????????????????
//  private  String UserSig =GenerateTestUserSig.genTestUserSig(UserId);


    public static String roomid  = "";                                                                              //?????????     ????????????
    public static String roomName ="";                                                                              //????????????    ????????????
    public static String userId = "";                                                                               //??????ID     mingming
    public static String userCn="";                                                                                 //?????????      ??????
    public static String keTangId = "";                                                                             //??????id      4193
    public static String keTangName="";                                                                             //????????????     ??????????????????60??????
    public static String userHead = "http://www.cn901.com/res/avatar/2022/07/21/avatar-mingming_173040431.png";     //????????????
    public static String subjectId = "";                                                                   //??????ID     10007




    private  int SDKappID =1400618830;                                                  //SDKAppID

    public static String teaName = "";
    public static String teaHead = "";
    public static String userName = "xgy";

    //??????????????????
    private V2TIMManager v2TIMManager;                                        //IM??????
    private boolean IMLoginresult;                                            //IM????????????

    //?????????????????????
    private Boolean menu_l_status=true;                                       //?????????????????????   true ????????????
    private ImageButton menu01,menu02,menu03,menu04,menu05,menu06,menu07,menu08,menu09,menu10,menu11,menu12; //????????????????????? ??????
    //?????????????????????
    private Boolean menu_b_status=true;                                        //?????????????????????  true ????????????
    private TextView b_size,b_cur,b_sum,b_chu,b_per;                           //?????????????????????????????????  ????????????????????????  ????????????  ?????????
    private ImageButton menub01,menub02,menub03,menub04,menub05,menub06,menub07,menub08,menub09,menub10,menub11,menub02_1,menub05_1;
        //??????????????????
    private List<Chat_Msg> data = new ArrayList<>();

    //??????????????????
    private TextView alert_text, upload_btn;                                    //???????????????????????????
    private View boardview;                                                     //?????????view
    private static TEduBoardController mBoard;                                  //????????????
    public TEduBoardController getmBoard() {return this.mBoard;}                //?????????????????????
    private Boolean BoardStatus=false;                                          //????????????????????? ??????
    private Boolean addBoardtoFragmentstatus=false;                             //??????????????????????????????????????????
    private FrameLayout.LayoutParams addBoardlayoutParams;                      //????????????????????????????????????
    private FrameLayout Board_container;                                        //???????????????Fragment
    private ConstraintLayout rf_leftmenu;                                       //?????????????????????
    private RelativeLayout rf_bottommenu,rf_shoukeneirong,select_resources;     //?????????????????????   ????????????????????????    ????????????????????????
    private TEduBoardController.TEduBoardCallback mBoardCallback;               //????????????
    private ImageButton geometry11,geometry12,geometry13,geometry14,geometry21,geometry22,geometry23,geometry24,geometry31,geometry32,geometry33,geometry34,geometry41,geometry42,geometry43,geometry44,geometry51,geometry52,geometry53,geometry61,geometry62,geometry63;
    private ImageButton teachingtools1,teachingtools2,teachingtools3,teachingtools4,teachingtools5;
    public static Integer cur_paintsize=100,cur_Highlighterpaintsize=450;  //???????????? ?????? ?????????????????????
    private PopupWindow pw_selectpaint;                                 //???????????? ????????????
    private PopupWindow pw_selecgeometry;                               //?????? ????????????  ????????????
    private PopupWindow pw_selectteachingtools;                         //??????????????????   ????????????
    private PopupWindow pw_selecteraser;                                //????????????   ????????????
    private List<Fragment>  mTabFragmenList = new ArrayList<>();        // ?????? ???????????? ????????????  ????????????  ??????tabbar
    private ImageView menu02color,menu03color,menu04color;              // ???  ??????   ????????????  ??????????????????????????????
    private TableLayout select_menu_top ,select_menu;
    private RelativeLayout menu01RL,menu02RL,menu03RL,menu04RL,menu05RL,menu06RL,menu07RL,menu08RL,menu09RL,menu10RL,menu11RL,menu12RL;
    private static TextView headerCountString;

    private TextView classTitle;
    private TextView classTime;
    private long baseTimer;
    private CosXmlService cosXmlService;                                //????????? COS Service???????????????

    // ??????????????????ID ???????????????ID???  ?????????ID
    private String FileID=null;                     //????????????ID
    private String CurFileID=null;                  //??????????????????ID
    private String BoardID="#DEFAULT";              //????????????ID
    private String CurBoardID=null;                 //???????????????ID
    private String CurType=null;                    // ????????????   ????????????  Board???File
    private static Boolean isquestion=false;        //?????????????????????  ????????????????????????

    private Button choosefile,uploadfile;                       //??????????????????  ????????????  ???????????? ??????
    private TextView msgTips,filename;                          //??????????????????  ????????????????????????  ????????????????????????
    private ProgressBar  proBar;                                //???????????? ?????????
    private ImageView close_select_resources,resupload;         //????????????????????????  ?????????????????????
    private Boolean isincludeType  = false;                     //??????????????????????????????
    private String curfilepath,curfilename;                     //????????????????????????   ??????????????????
    private LinearLayout uploadprogress;                        //????????????????????????  ????????????Visiable

    //TabBarFragment
    private final ChatRoomFragment chatRoomFragment = new ChatRoomFragment();                       //???????????????Fragment??????
    private final VideoListFragment videoListFragment =  new VideoListFragment();                   //?????????????????????Fragment??????
    private final AnswerQuestionFragment answerQuestionFragment = new AnswerQuestionFragment();     //?????????????????????Fragment??????

    // ????????????
    private static View memberPopupView;
    private static ImageView memberPopupCloseBtn;
    private static ListView memberList;
    private static MemberListViewAdapter listViewAdapter;
    private static TextView memberListCountString;
    private Vector<MemberItem> memberDataList;

    // ????????????
    private View handsUpPopupView;
    private static ImageView handsUpPopupCloseBtn;
    private ListView handsUpList;
    public HandsUpListViewAdapter handsUpListViewAdapter;
    public List<HandsUpItem> handsUpItemList = new ArrayList<>();
    private TextView handBtnBadge;
    public Switch handsUpSwitchBtn;

    // UI???????????????
    public Handler handler;
    @SuppressLint("HandlerLeak")

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        ????????????????????????
//        String PM_SINGLE= Manifest.permission.CAMERA;
//        int nRet= ContextCompat.checkSelfPermission(this,PM_SINGLE);
//        if(nRet!= PackageManager.PERMISSION_GRANTED){
//            ActivityCompat.requestPermissions(this,new String[]{PM_SINGLE},10000);
//        }

//        ????????????????????????
        String[] PM_MULTIPLE={
                Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA,Manifest.permission.WRITE_CONTACTS
        };

        if(Build.VERSION.SDK_INT>=23) {
            ArrayList<String> pmList = new ArrayList<>();
            //????????????????????????????????????
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

       //?????????????????????
        Intent intent = getIntent();
        userId=intent.getExtras().get("userid").toString();
        userCn=intent.getExtras().get("userCn").toString();
        roomid = intent.getExtras().get("roomid").toString();
        roomName = intent.getExtras().get("roomname").toString();

        subjectId = intent.getExtras().get("subjectid").toString();
        UserSig =GenerateTestUserSig.genTestUserSig(intent.getExtras().get("userid").toString());

        keTangId = intent.getExtras().get("ketangid").toString();
        keTangName = intent.getExtras().get("ketangname").toString();


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




        // ??????CameraView
        mTXCVVTeacherPreviewView = findViewById(R.id.teacher_camera);
        teacherTRTCBackground = findViewById(R.id.teacher_background);

        alert_text = findViewById(R.id.alert_text);

        headerCountString = findViewById(R.id.student_num);
        classTime = findViewById(R.id.class_time);
        classTitle = findViewById(R.id.class_title);

        // ??????????????????

        handsUpPopupView = getLayoutInflater().inflate(R.layout.hands_up_pop_window, null);
        handsUpSwitchBtn = handsUpPopupView.findViewById(R.id.hands_up_controller);
        handsUpPopupCloseBtn = handsUpPopupView.findViewById(R.id.hands_up_list_pop_close);
        handsUpList = handsUpPopupView.findViewById(R.id.hands_up_list);
        memberPopupView = getLayoutInflater().inflate(R.layout.member_list_pop_window, null);
        memberListCountString = memberPopupView.findViewById(R.id.member_list_pop_title);
        memberPopupCloseBtn = memberPopupView.findViewById(R.id.member_list_pop_close);
        memberList = memberPopupView.findViewById(R.id.member_list);
        group_btn = findViewById(R.id.group_buttons);
        canvasBtn = findViewById(R.id.canvas_btn);
        exit_btn = findViewById(R.id.exit_btn);
        handBtnBadge = findViewById(R.id.hand_btn_badge);

        boardBtn = findViewById(R.id.board_btn);
        contentBtn = findViewById(R.id.content_btn);
        memberBtn = findViewById(R.id.member_btn);
        handBtn = findViewById(R.id.hand_btn);
        audioBtn = findViewById(R.id.mic_btn);
        cameraBtn = findViewById(R.id.camera_btn);
        overClassBtn = findViewById(R.id.exit_btn);
        teacher_name_view = findViewById(R.id.teacher_name);
        // ???????????????
        RelativeLayout scroll_block = findViewById(R.id.stroll);
        ViewGroup.LayoutParams scroll_block_params = scroll_block.getLayoutParams();
        if(isTabletDevice(this)) {
            scroll_block_params.width = UtilTools.dip2px(this, 0);
        } else {
            scroll_block_params.width = UtilTools.dip2px(this, 160);
        }
        scroll_block.setLayoutParams(scroll_block_params);

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
        teacher_name_view.setText(userId+"??????");
        //????????????????????????
        select_resources=findViewById(R.id.select_resources);
        proBar = findViewById(R.id.proBar);
        msgTips = findViewById(R.id.msgTips);
        filename = findViewById(R.id.filename);
        close_select_resources = findViewById(R.id.close_select_resources);
        resupload= findViewById(R.id.res_upload);
        choosefile = findViewById(R.id.choosefile);
        uploadfile = findViewById(R.id.uploadfile);
        uploadprogress = findViewById(R.id.uploadprogress);

        //????????????
        exit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                destroyBoard();
                onExitLiveRoom();
                addBoardtoFragmentstatus =false;
                rf_leftmenu.setVisibility(View.GONE);
                rf_bottommenu.setVisibility(View.GONE);
                RelativeLayout bg_shoukeneirong = findViewById(R.id.bg_shoukeneirong);
                bg_shoukeneirong.setVisibility(View.VISIBLE);
                alert_text.setText("?????????????????????????????????");
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
        //??????????????????
        choosefile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadprogress.setVisibility(View.GONE);
                uploadfile.setText("????????????");
                msgTips.setText("?????????????????????");
                filename.setText("?????????????????????");
                curfilename="";
                curfilepath="";
                intoFileManager();
            }
        });
        //??????????????????
        uploadfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //?????????????????? ??????????????????
                if(isincludeType){
                    uploadprogress.setVisibility(View.VISIBLE);
                    uploadfile.setText("????????????");
                    msgTips.setText("??????????????????");
                    Time time = new Time("GMT+8");
                    time.setToNow();
                    String cosprefix = "class/"+time.year+"/"+(time.month+1)+"/"+time.monthDay+"/"+subjectId+"/"+roomid+"/res/";
                    UploadToBucket(cosprefix,curfilepath,curfilename,false);
                }
            }
        });
        //????????? ?????? ????????????????????? ???????????????
        contentBtn = findViewById(R.id.content_btn);
        contentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addBoardtoFragmentstatus){
                    if(select_resources.getVisibility()==View.VISIBLE){
                        select_resources.setVisibility(View.GONE);
                    }else {
                        select_resources.setVisibility(View.VISIBLE);
                        filename.setText("?????????????????????");
                        uploadfile.setText("????????????");
                        uploadprogress.setVisibility(View.GONE);
                        curfilename="";
                        curfilepath="";
                    }
                }
            }
        });

        //????????????  ?????????????????????  ???????????????
        canvasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     //?????????????????? ????????????
                System.out.println("+++?????????????????????"+"--?????????????????????:"+CurType+"--???????????????ID"+FileID+"???????????????"+mBoard.getBoardElementList(null).toString());
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
                       System.out.println("+++???????????????????????????");
                   }else {
                       System.out.println("+++???????????????");
                   }
               }
            }
        });

        //????????????????????????????????? ?????????

        addBoardlayoutParams  = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

//         // ?????? ????????????  ??????????????????????????? ?????????






         Board_container = findViewById(R.id.teachingcontent);


        rf_leftmenu = findViewById(R.id.menu_left);
         rf_bottommenu = findViewById(R.id.menu_bottom);
         rf_shoukeneirong = findViewById(R.id.bg_shoukeneirong);

         //????????????
        boardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("+++??????????????????????????????????????????"+(mBoard!=null)+"--?????????????????????"+BoardStatus+"--??????????????????:"+CurType+"--???????????????ID"+BoardID+"???????????????"+mBoard.getBoardElementList(null).toString());
                //?????????????????? ????????????
                    //????????????????????????
                    if(BoardStatus&&addBoardtoFragmentstatus){
                        if(mBoard!=null&&"Board".equals(CurType)){
                            System.out.println("+++???????????????????????????");
                        }else {
                            if("File".equals(CurType)){
                                System.out.println("+++???????????????????????????");
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
                    case 6:
                        setClassTime((String) msg.obj);
                        break;
                    case 7:
                        updateMemberList();
                        break;
                    default:
                        break;
                }
            }
        };
        setClassTitle("????????????");
        //????????????????????????
        InitBucket(this);
        initHandsUpList();
        initMemberList();
        initTabBarNavigation();
        enterLiveRoom();
        // ???????????????
        startTime();
    }


    /**
     * ??????????????????
     *
     * @param context ?????????
     * @return true????????????????????? false?????????????????????
     */
    public boolean isTabletDevice(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)>= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public void startTime() {
        MainActivity that = this;
        baseTimer = SystemClock.elapsedRealtime();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                int time = (int)((SystemClock.elapsedRealtime() - that.baseTimer) / 1000);
                String hh = new DecimalFormat("00").format(time / 3600);
                String mm = new DecimalFormat("00").format(time % 3600 / 60);
                String ss = new DecimalFormat("00").format(time % 60);
                String timeFormat = new String(hh + ":" + mm + ":" + ss);
                Message msg = new Message();
                msg.what = 6;
                msg.obj = timeFormat;
                that.handler.sendMessage(msg);
            }
        }, 0, 1000L);
    }

    public void stopTime() {
        if(timer != null)
            timer.cancel();
    }

    public void setClassTitle(String title) {
        classTitle.setText(title);
    }

    public void setClassTime(String time) {
        classTime.setText(time);
    }

    public void setCountMember(int classNum, int studentNum) {
        String title = "?????? (????????????: " + String.valueOf(classNum) + "  ?????????: " + String.valueOf(studentNum) +")";
        headerCountString.setText(title);
        memberListCountString.setText(title);
    }

    public TRTCCloud getmTRTCCloud() {
        return mTRTCCloud;
    }

    public void setHandBtnBadge(int num) {
        if(num > 0) {
            handBtnBadge.setVisibility(View.VISIBLE);
            if(num > 99)
                handBtnBadge.setText("99+");
            else
                handBtnBadge.setText(String.valueOf(num));
        } else {
            handBtnBadge.setText("0");
            handBtnBadge.setVisibility(View.INVISIBLE);
        }
    }

    public void setHandsUpData() {
        MainActivity that = this;
        handsUpItemList.clear();
        List<HandsUpItem> tempHandsUpItemList = new ArrayList<>();
        for (int i = 0; i < AnswerActivity.handsUpList.size(); i++) {
            tempHandsUpItemList.add(new HandsUpItem( AnswerActivity.handsUpList.get(i).getUserType(), AnswerActivity.handsUpList.get(i).getName(), AnswerActivity.handsUpList.get(i).getUserId(), false));
        }
        for (int i =0; i< tempHandsUpItemList.size(); i++) {
            Log.e(TAG, "updateHandsUpList: "  + tempHandsUpItemList.get(i).toString());
        }
        handsUpItemList.addAll(tempHandsUpItemList);
        setHandBtnBadge(handsUpItemList.size());
//        UtilTools.QBadge(this, handBtn, AnswerActivity.handsUpList.size());
        handsUpListViewAdapter.notifyDataSetChanged();
    }

    public void switchMemberListAudioIcon(int position) {
        MemberItem item = listViewAdapter.getItem(position);
        if(item != null){
            Log.e(TAG, "switchMemberListAudioIcon: ????????????item " + item.getName());
            item.setAudioControl(!item.getAudioControl());
            Toast.makeText(MainActivity.this, "?????? " + position + " ?????????????????????", Toast.LENGTH_SHORT).show();
            listViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "?????? " + position + " ??????", Toast.LENGTH_SHORT).show();
        }
    }

    public void switchMemberListVideoIcon(int position) {
        MemberItem item = listViewAdapter.getItem(position);
        if(item != null){
            Log.e(TAG, "switchMemberListAudioIcon: ????????????item " + item.getName());
            item.setVideoControl(!item.getVideoControl());
            listViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "?????? " + position + " ??????", Toast.LENGTH_SHORT).show();
        }
    }

    public void switchMemberListChatIcon(int position) {
        MemberItem item = listViewAdapter.getItem(position);
        if(item != null){
            item.setChatControl(!item.getChatControl());
            Toast.makeText(MainActivity.this, "?????? " + position + " ?????????????????????", Toast.LENGTH_SHORT).show();
            listViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "?????? " + position + " ??????", Toast.LENGTH_SHORT).show();
        }
    }

    public void switchSpeakerIcon(int position) {
        MemberItem item = listViewAdapter.getItem(position);
        if(item != null){
            this.videoListFragment.setVideo(item.getUserId(),item.getAudioControl(),this, mTRTCCloud );
            item.setSpeakControl(!item.getSpeakControl());
            Toast.makeText(MainActivity.this, "?????? " + position + " ????????????????????????", Toast.LENGTH_SHORT).show();
            listViewAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(MainActivity.this, "?????? " + position + " ??????", Toast.LENGTH_SHORT).show();
        }
    }

    // ?????????????????????
    public void initHandsUpList() {
        if(AnswerActivity.handsUpList != null) {
            for (int i = 0; i < AnswerActivity.handsUpList.size(); i++) {
                handsUpItemList.add(new HandsUpItem(AnswerActivity.handsUpList.get(i).getUserType() ,AnswerActivity.handsUpList.get(i).getName(), AnswerActivity.handsUpList.get(i).getUserId(), false));
                Log.e(TAG, "initHandsUpList: "  + AnswerActivity.handsUpList.get(i).toString());
            }

        }
        handsUpListViewAdapter = new HandsUpListViewAdapter(this, handsUpList, handsUpItemList);
        handsUpList.setAdapter(handsUpListViewAdapter);

        handsUpListViewAdapter.setOnSpeakerControllerClickListener(new HandsUpListViewAdapter.onSpeakerControllerListener() {
            @Override
            public void onSpeakControllerClick(int i) {
                upToSpeaking(i);
            }
        });
    }


    // ???????????????
    public void upToSpeaking(int position) {
        HandsUpItem item = handsUpListViewAdapter.getItem(position);
        // ?????????????????????????????????
        handsUpItemList.remove(position);
        // ???????????????
        setHandBtnBadge(handsUpItemList.size());
        handsUpListViewAdapter.notifyDataSetChanged();

        // ??????MemberList????????????
        int memberPosition = listViewAdapter.getItemPositionById(item.getUserId());


        if(memberPosition != -1){
            MemberItem memberNow = listViewAdapter.getItem(memberPosition);
            HttpActivity.speakerController(memberNow.getUserId(), item.getName(), "up", position, this);
            memberNow.setUserType(0);
            listViewAdapter.notifyDataSetChanged();
        }
        this.videoListFragment.addCameraView(item.getUserId(), mTRTCCloud);

        Toast.makeText(this, "???????????? " + position + " ?????????????????????", Toast.LENGTH_SHORT).show();
    }


    // ??????????????????
    public void updateMemberList() {
        memberDataList.clear();
        if(AnswerActivity.joinList != null) {
            for (int i = 0; i < AnswerActivity.joinList.size(); i++) {
                memberDataList.addElement(new MemberItem(AnswerActivity.joinList.get(i).getName(), AnswerActivity.joinList.get(i).getUserId(), 1 ,true, false, true, false, true));
                Log.e(TAG, "initMemberList: " + AnswerActivity.joinList.get(i).toString());
            }
        }
        if(AnswerActivity.ketangList != null) {
            for (int i = 0; i < AnswerActivity.ketangList.size(); i++) {
//                Log.e(TAG, "updateMemberList: get No . " + AnswerActivity.ketangList.get(i).toString() + " from size " + AnswerActivity.ketangList.size());
                MemberItem memberItemNew = new MemberItem(AnswerActivity.ketangList.get(i).getName(), AnswerActivity.ketangList.get(i).getUserId(), 0, true, false, true, false, true);
                memberDataList.addElement(memberItemNew);
                Log.e(TAG, "initMemberList: " + AnswerActivity.ketangList.get(i).toString());
                if(!videoListFragment.findUserInUserList(AnswerActivity.ketangList.get(i).getUserId())) {
                    videoListFragment.addCameraView(AnswerActivity.ketangList.get(i).getUserId(), mTRTCCloud);
                }
            }
        }
        setCountMember(AnswerActivity.ketangList.size(), AnswerActivity.joinList.size());
        listViewAdapter.notifyDataSetChanged();
    }

    // ?????????????????????
    public void initMemberList() {
        Log.e(TAG, "initMemberList: hahahahhahaah");
        memberDataList = new Vector<>();
        HttpActivity.getMemberList(this);

        setCountMember(AnswerActivity.ketangList.size(), AnswerActivity.joinList.size());
        MainActivity that = this;
        listViewAdapter = new MemberListViewAdapter(this, memberList, memberDataList);
        memberList.setAdapter(listViewAdapter);
        HttpActivity.getMemberList(this);

        listViewAdapter.setOnItemButtonListener(new MemberListViewAdapter.onItemButtonListener() {
            @Override
            public void onMoveOutClick(int i) {
                Toast.makeText(MainActivity.this, "?????? " + i + " ?????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "?????? " + i + " ?????????????????????", Toast.LENGTH_SHORT).show();
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "?????? " + i + " ??????", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onSpeakControlClick(int i) {
                MemberItem item = listViewAdapter.getItem(i);
                if(item != null){
                    if(item.getSpeakControl()){
                        ClassDataBean memberInKetangList = AnswerActivity.findMemberInKetangList(item.getUserId());
                        StudentDataBean studentDataBean = AnswerActivity.findMemberInJoinList(item.getUserId());
                        if(memberInKetangList != null)
                            item.setUserType(0);
                        if(studentDataBean != null) {
                            that.videoListFragment.leaveRoom(item.getUserId(), 12580, that, getmTRTCCloud());
                            item.setUserType(1);
                        }
                        HttpActivity.speakerController(item.getUserId(), item.getName(), "down", i, that);
                    } else {
                        that.videoListFragment.leaveRoom(item.getUserId(), 12580, that, getmTRTCCloud());
                        that.videoListFragment.addCameraView(item.getUserId(), getmTRTCCloud());
                        item.setUserType(0);
                        HttpActivity.speakerController(item.getUserId(), item.getName(), "up", i, that);
                    }
                    Toast.makeText(MainActivity.this, "?????? " + i + " ????????????????????????", Toast.LENGTH_SHORT).show();
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "?????? " + i + " ??????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "?????? " + i + " ?????????????????????", Toast.LENGTH_SHORT).show();
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "?????? " + i + " ??????", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onVideoControlClick(int i) {
                Toast.makeText(MainActivity.this, "?????? " + i + " ????????????????????????", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(MainActivity.this, "?????? " + i + " ????????????????????????", Toast.LENGTH_SHORT).show();
                    listViewAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "?????? " + i + " ??????", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void initTabBarNavigation() {

        //??????????????????ViewPager???Fragment???????????????
        ViewPager viewPager = findViewById(R.id.tar_bar_view_page);
        TabBarAdapter tabBarAdapter = new TabBarAdapter(getSupportFragmentManager());
        tabBarAdapter.setmFragment(mFragmenglist);
        viewPager.setAdapter(tabBarAdapter);

        //??????ViewPager??????????????????
        viewPager.setOffscreenPageLimit(3);
        //???TabLayout???ViewPager???????????????
        TabLayout mTabLayout = findViewById(R.id.tab_bar_table_layout);
        mTabLayout.setupWithViewPager(viewPager,true);

        //??????Tab?????????
        TabLayout.Tab videoList = mTabLayout.getTabAt(0);
        TabLayout.Tab chatRoom = mTabLayout.getTabAt(1);
        TabLayout.Tab answerQuestion = mTabLayout.getTabAt(2);
    }


    // ????????????
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
                Log.e(TAG, "onEnterRoom: ???????????????????????????: " + result);
                Toast.makeText(activity, "???????????????????????????: " + "[" + result+ "]" , Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "onEnterRoom: ????????????????????????????????????" + result);
                Toast.makeText(activity, "?????????????????????????????????: " + "[" + result+ "]" , Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean available) {
            MainActivity activity = mContext.get();
            Log.d(TAG, "onUserAudioAvailable userId " + userId + ", mUserCount " + userId + ",available " + available);
            System.out.println("onUserAudioAvailable userId " + userId + ", mUserCount " + userId + ",available " + available);
            System.out.println("onUserVideoAvailable:"+userId);
            activity.videoListFragment.setAudio(userId, available, activity, activity.mTRTCCloud);
            int userPosition = listViewAdapter.getItemPositionById(userId);
            activity.switchMemberListAudioIcon(userPosition);
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            MainActivity activity = mContext.get();
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
            int userPosition = listViewAdapter.getItemPositionById(userId);
            activity.switchMemberListVideoIcon(userPosition);
            if(AnswerActivity.findMemberInKetangList((userId)) != null)
                activity.videoListFragment.setVideo(userId, available, activity, activity.mTRTCCloud);

        }

        @Override
        public void onRemoteUserEnterRoom(String userId){
            MainActivity activity = mContext.get();
            HttpActivity.getMemberList(activity);
            Log.e(TAG, "onRemoteUserEnterRoom: userId" + userId );
            System.out.println("onRemoteUserEnterRoom userId " + userId );
            Toast.makeText(activity, "onRemoteUserEnterRoom userId " + userId , Toast.LENGTH_SHORT).show();
            if(AnswerActivity.findMemberInKetangList(userId) != null)
                activity.videoListFragment.addCameraView(userId, activity.mTRTCCloud);
        }

        @Override
        public void onRemoteUserLeaveRoom(String userId, int reason){
            MainActivity activity = mContext.get();
            activity.videoListFragment.leaveRoom(userId, reason, activity,
                    activity.mTRTCCloud);
            HttpActivity.getMemberList(activity);
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

        // ??????TRTC????????????
//        String userId = "mingming";
        myTRTCParams = new TRTCCloudDef.TRTCParams();
        myTRTCParams.sdkAppId = GenerateTestUserSig.SDKAPPID;
        myTRTCParams.userId = userId;
        myTRTCParams.roomId = Integer.parseInt(roomid);
        myTRTCParams.userSig = GenerateTestUserSig.genTestUserSig(myTRTCParams.userId);
//        myTRTCParams.userSig = "eJwtzMEKgkAUheF3mXXI9eoMKbTQiFoE4WQQ7dSZ4jY0mVoa0btn6vJ8B-4PS7d756UrFjJ0gM2GTUrbhs40cEfZHdzpqZXJypIUC10fQCAEyMdHdyVVunfOOQLAqA3d-iYE*p6HPJgqdOnD-ukooyQNjELZbeQhf7ytKAp7Xc7XBmFXJzxvoyes4nbBvj8x1DFE";


        // ?????????????????????SDK????????????????????????????????????????????????????????????????????????
        mTRTCCloud.setDefaultStreamRecvMode(false,true);
        // ????????????
         mTRTCCloud.enterRoom(myTRTCParams, TRTCCloudDef.TRTC_APP_SCENE_VIDEOCALL);


        // ??????????????????
        mTRTCCloud.enableAudioVolumeEvaluation(300,true);

        // ??????????????????

        // ??????????????????????????????????????????????????????????????????????????????
        TRTCCloudDef.TRTCRenderParams myTRTCRenderParams = new TRTCCloudDef.TRTCRenderParams();
        myTRTCRenderParams.fillMode = TRTCCloudDef.TRTC_VIDEO_RENDER_MODE_FILL;
        myTRTCRenderParams.mirrorType = TRTCCloudDef.TRTC_VIDEO_MIRROR_TYPE_AUTO;
        mTRTCCloud.setLocalRenderParams(myTRTCRenderParams);

        // ???????????????????????????
        mTRTCCloud.startLocalPreview(true, mTXCVVTeacherPreviewView);
        cameraOn = true;


        // ?????????????????????
        mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_SPEECH);
        musicOn = true;

        teacherTRTCBackground.setVisibility(View.INVISIBLE);
//        teacherTRTCBackground.bringToFront();

        // ??????????????????????????????

        @SuppressLint("UseCompatLoadingForDrawables") Drawable teacher_name_mic_icon = getResources().getDrawable(R.drawable.mic_on);
        teacher_name_mic_icon.setBounds(0,0,20,20);
        teacher_name_view.setCompoundDrawables(teacher_name_mic_icon, null, null, null);

        // ?????????????????????
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        HttpActivity.initClass(screenWidth, screenHeight, "skydt", this);

        // ????????????????????????
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

            // ????????????
            @SuppressLint("UseCompatLoadingForDrawables") Drawable teacher_name_mic_icon = getResources().getDrawable(R.drawable.mic_off);
            teacher_name_mic_icon.setBounds(0,0,20,20);
            teacher_name_view.setCompoundDrawables(teacher_name_mic_icon, null, null, null);
        } else {
            mTRTCCloud.startLocalAudio(TRTCCloudDef.TRTC_AUDIO_QUALITY_SPEECH);
            musicOn = true;
            audioBtn.getDrawable().setLevel(5);
            // ????????????
            @SuppressLint("UseCompatLoadingForDrawables") Drawable teacher_name_mic_icon = getResources().getDrawable(R.drawable.mic_on);
            teacher_name_mic_icon.setBounds(0,0,20,20);
            teacher_name_view.setCompoundDrawables(teacher_name_mic_icon, null, null, null);
        }
    }

    public void sentToVideoList(Bundle bundle) {
    }

    public void showMemberListBtn(View view) {
        Point point = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(point);
        int popUpWindowWidth = (int) (point.x*0.5);
        int popUpWindowHeight = (int) (point.y * 0.7);
        PopupWindow popupWindow = new PopupWindow(memberPopupView, popUpWindowWidth, popUpWindowHeight, true);
        memberPopupCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        memberPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int offsetX = - popUpWindowWidth / 4;
        int offsetY = - popUpWindowHeight - (view.getHeight()) - 10;
        popupWindow.showAsDropDown(view, offsetX, offsetY, Gravity.START);
    }

    public void showHandsUpBtn(View view) {
        Point point = new Point();
        this.getWindowManager().getDefaultDisplay().getSize(point);
        int popUpWindowWidth = (int) (point.x*0.3);
        int popUpWindowHeight = (int) (point.y * 0.5);
        PopupWindow popupWindow = new PopupWindow(handsUpPopupView, popUpWindowWidth, popUpWindowHeight, true);
        handsUpPopupCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        handsUpPopupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int offsetX = - popUpWindowWidth /4;
        int offsetY = - popUpWindowHeight - (view.getHeight()) - 10;
        popupWindow.showAsDropDown(view, offsetX, offsetY, Gravity.START);
    }

    //???????????????
    public void initBoard(){
        // ?????????????????????????????????
        //???1???????????????
        System.out.println("+++?????????????????????");
        mBoard=null;
        mBoardCallback=null;
        TEduBoardController.TEduBoardAuthParam authParam = new TEduBoardController.TEduBoardAuthParam(
                SDKappID , userId, UserSig);
        //???2?????????????????????
        TEduBoardController.TEduBoardInitParam initParam = new TEduBoardController.TEduBoardInitParam();
        initParam.timSync=false;
        mBoard = new TEduBoardController(this);
        //???3??????????????????????????? ??????TEduBoardCallback??????
        mBoardCallback = new TEduBoardController.TEduBoardCallback(){
            @Override
            public void onTEBError(int code, String msg) {
                System.out.println("onTEBError"+"+++++++++++++code"+code+msg);
                alert_text.setText("?????????????????????????????????");
            }
            @Override
            public void onTEBWarning(int code, String msg) {
                System.out.println("onTEBWarning"+"+++++++++++++code:"+code);
                if(code==7){  //VIDEO_ALREADY_EXISTS
                    System.out.println("onTEBWarning"+"+++++++++++++VIDEO???????????????:");
                    mBoard.gotoBoard(msg);
                }else if(code==3){  //H5PPT_ALREADY_EXISTS
                    System.out.println("onTEBWarning"+"+++++++++++++H5PPT???????????????:");
                    mBoard.gotoBoard(msg);
                }else if(code==6){  //H5PPT_ALREADY_EXISTS
                    System.out.println("onTEBWarning"+"+++++++++++++H5FILE???????????????:");
                    mBoard.gotoBoard(msg);
                }
//                TEduBoardController.TEduBoardWarningCode.TEDU_BOARD_WARNING_IMAGE_MEDIA_BITRATE_TOO_LARGE
            }
            @Override
            public void onTEBInit() {
                System.out.println("onTEBInit"+"++++????????????????????????");
                ConstraintLayout.LayoutParams params= new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
                if(findViewById(R.id.boardcontent).getMeasuredWidth()>findViewById(R.id.boardcontent).getMeasuredHeight()){
                    params.setMargins((findViewById(R.id.boardcontent).getMeasuredWidth()-findViewById(R.id.boardcontent).getMeasuredHeight()*16/9)/2,0,(findViewById(R.id.boardcontent).getMeasuredWidth()-findViewById(R.id.boardcontent).getMeasuredHeight()*16/9)/2,0);
                }else {
                    params.setMargins(0,(findViewById(R.id.boardcontent).getMeasuredHeight()-findViewById(R.id.boardcontent).getMeasuredWidth()*9/16)/2,0,(findViewById(R.id.boardcontent).getMeasuredHeight()-findViewById(R.id.boardcontent).getMeasuredWidth()*9/16)/2);
                }
                Board_container.setLayoutParams(params);
                findViewById(R.id.bg_shoukeneirong).setVisibility(View.GONE);
                BoardStatus=true;
                boardview = mBoard.getBoardRenderView();
                initBoardMenu();
                alert_text.setText("?????????????????????");
                if(!addBoardtoFragmentstatus){
                    addBoardtoFragmentstatus =  mBoard.addBoardViewToContainer(Board_container,boardview,addBoardlayoutParams);
                    rf_leftmenu.setVisibility(View.VISIBLE);
                    rf_bottommenu.setVisibility(View.VISIBLE);
                    rf_shoukeneirong.setVisibility(View.GONE);//????????????????????????
                }
            }
            @Override
            public void onTEBHistroyDataSyncCompleted() {
                System.out.println("onTEBHistroyDataSyncCompleted"+"+++++++++++++");
                b_sum.setText(mBoard.getFileBoardList(mBoard.getCurrentFile()).size()+"");
            }
            @Override
            public void onTEBSyncData(String data) {
                final V2TIMMessage Board_message = V2TIMManager.getMessageManager().createCustomMessage(
                        data.getBytes(),                   //data
                        "",                             //description
                        "TXWhiteBoardExt".getBytes());     //extension
                V2TIMManager.getInstance().getConversationManager().getConversation(roomid, new V2TIMValueCallback<V2TIMConversation>() {
                    @Override
                    public void onError(int i, String s) {
                        // ??????????????????
                        System.out.println("+++??????????????????"+s+i);
                    }
                    @Override
                    public void onSuccess(V2TIMConversation v2TIMConversation) {
                        V2TIMManager.getInstance().getMessageManager().sendMessage(Board_message, null, roomid, 1, false, null,  new V2TIMSendCallback<V2TIMMessage>() {
                            @Override
                            public void onSuccess(V2TIMMessage v2TIMMessage) {
                                // ?????? IM ????????????
                                System.out.println("+++????????????????????????"+v2TIMMessage.getCustomElem().toString());
                                if(findViewById(R.id.setBoardWindow).getVisibility()==View.VISIBLE){findViewById(R.id.setBoardWindow).setVisibility(View.GONE);}
                            }
                            @Override
                            public void onError(int i, String s) {
                                // ?????? IM ?????????????????????????????????
                                System.out.println("+++?????? IM ?????????????????????????????????"+s);
                                mBoard.syncAndReload();
                            }
                            @Override
                            public void onProgress(int i) {
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
                //????????????????????????????????????
                if( mBoard.getBoardScale()>300){
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
            }

            @Override
            public void onTEBDeleteBoard(List<String> boardList, String fileId) {

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
                b_cur.setText((mBoard.getFileBoardList(fileId).indexOf(boardId)+1)+"");
                b_sum.setText(mBoard.getFileBoardList(fileId).size()+"");
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
                System.out.println("onTEBDeleteFile"+"+++:??????????????????ID???"+fileId);
            }
            @Override
            public void onTEBSwitchFile(String fileId) {
                handlerCount.removeCallbacks(runnablere_mBoardaddResouce);
                if(fileId.equals("#DEFAULT")){
 //                 ??????????????????????????? ??????????????????????????????
                    CurType="Board";
                    CurBoardID = mBoard.getCurrentBoard();
                }else {
 //                 ???????????????????????? ????????????  ???????????????ID
                    CurType="File";
                    if(!fileId.equals(FileID)){
                        //?????????????????????????????????????????????????????????????????????ID???
                        FileID=fileId;
                    }
                    CurFileID = mBoard.getCurrentBoard();
                }
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
                System.out.println("onTEBSnapshot"+"++++????????????"+path+msg+code);
                if(code==0){
                    File ff = new File(path);
                    String name = ff.getName();
                    Time time = new Time("GMT+8");
                    time.setToNow();
                    // isquestion  ???????????????????????????????????????????????? ??????????????????????????????
                    String cosprefix = isquestion?"class/"+time.year+"/"+(time.month+1)+"/"+time.monthDay+"/"+subjectId+"/"+roomid+"/question/" : "class/"+time.year+"/"+(time.month+1)+"/"+time.monthDay+"/"+subjectId+"/"+roomid+"/capture/";
                    UploadToBucket(cosprefix,path,name,true);
                }else {
                    System.out.println("++++??????????????????"+msg+"   code:"+code);
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
        //???4??????????????????
        mBoard.init(authParam,  Integer.parseInt(roomid), initParam);
        //???2??????????????? View
        // ??????????????????????????????
        initBoardMenu();
    }

    public void initTIM(){
        //????????? IMSDK
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
                //??????????????? ??????TIM
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
                System.out.println("++++++????????????"+s);
                alert_text.setText("??????????????????,??????????????????");
            }
            @Override
            public void onSuccess() {
                //?????????????????????
                V2TIMManager.getMessageManager().addAdvancedMsgListener(new V2TIMAdvancedMsgListener() {
                    @Override
                    public void onRecvNewMessage(V2TIMMessage msg) {
                        String Msg_Extension = new String(msg.getCustomElem().getExtension());
                        String Msg_Description = msg.getCustomElem().getDescription();
                        super.onRecvNewMessage(msg);
                        if("TXWhiteBoardExt".equals(Msg_Extension)){
                            //????????????
                            mBoard.addSyncData(new String(msg.getCustomElem().getData()));
                        }else if("TBKTExt".equals(Msg_Extension)){
                            //????????????
                            System.out.println("+++???????????????"+Msg_Description);
                            SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                            Chat_Msg msg_rec = new Chat_Msg(Msg_Description.split("@#@")[1],format.format(new Date(msg.getTimestamp()*1000)),new String(msg.getCustomElem().getData()),2);// type  2 ?????? 1 ??????

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
                //????????????  ????????????
                createGroup();
            }
        });

        //?????????????????????  ??????  ??????????????? Extension ????????????????????? ?????? ????????????
//        V2TIMManager.getInstance().addSimpleMsgListener(new V2TIMSimpleMsgListener() {

    };

    public void createGroup(){
        V2TIMManager.getInstance().createGroup(V2TIMManager.GROUP_TYPE_MEETING, roomid, roomid, new V2TIMValueCallback<String>() {
            @Override
            public void onSuccess(String s) {
                // ??????????????????
                initBoard();
            }
            @Override
            public void onError(int code, String desc) {
                // ??????????????????
                if(10021==code){
                    V2TIMManager.getInstance().joinGroup(roomid, roomid, new V2TIMCallback() {
                        @Override
                        public void onSuccess() {
                            // ????????????
                            initBoard();
                        }
                        @Override
                        public void onError(int i, String s) {
                            // ????????????
                            if(10013==i){
                                //??????????????????
                                initBoard();
                            }else {
                                System.out.println("+++++????????????"+s+i);
                            }
                        }
                    });
                }else if(10025==code){
                    //???????????????????????????????????????
                    initBoard();
                }else {
                    System.out.println("+++?????????????????????"+desc+"    code???"+code);
                }
            }
        });

    }

    //?????????????????? ??????  ????????????
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
        b_size.setText(mBoard.getBoardScale()+"");



//        ??????????????????
        resupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????  ?????????????????????
                uploadprogress.setVisibility(View.GONE);
                uploadfile.setText("????????????");
                msgTips.setText("?????????????????????");
                filename.setText("?????????????????????");
                curfilename="";
                curfilepath="";
                intoFileManager();
            }
        });
        //????????????????????????
        close_select_resources.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????????????????
                select_resources.setVisibility(View.GONE);
            }
        });

        //???????????????  ???????????????  ????????????
        menu01 = findViewById(R.id.menu01);
        menu01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(0);
                setLeftmenustatus(true);
                menu01.setBackgroundResource(R.mipmap.menu_01_mouse1);
            }
        });
        //???????????????  ???2?????????  ????????????
        menu02 = findViewById(R.id.menu02);
        menu02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //??????????????????
                mBoard.setPenAutoFittingMode(TEduBoardController.TEduBoardPenFittingMode.NONE);
                mBoard.setToolType(1);
                mBoard.setBrushThin(cur_paintsize);
                setLeftmenustatus(true);
                menu02.setBackgroundResource(R.mipmap.menu_02_paint1);
                menu02color.setBackground(getResources().getDrawable(R.color.bg_selected_menu));

                //??????????????????
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
                        mBoard.setBrushThin(cur_paintsize);
                        //?????????????????????????????????
                        if(mBoard.getBrushColor().toInt()==8947848){
                            menu02color.setImageResource(R.mipmap.text_gray);
                        }else if(mBoard.getBrushColor().toInt()==0){
                            menu02color.setImageResource(R.mipmap.text_black);
                        }else if(mBoard.getBrushColor().toInt()==255){
                            menu02color.setImageResource(R.mipmap.text_blue);
                        }else if(mBoard.getBrushColor().toInt()==65280){
                            menu02color.setImageResource(R.mipmap.text_green);
                        }else if(mBoard.getBrushColor().toInt()==16776960){
                            menu02color.setImageResource(R.mipmap.text_yellow);
                        }else if(mBoard.getBrushColor().toInt()==16711680) {
                            menu02color.setImageResource(R.mipmap.text_red);
                        }
                        pw_selectpaint.dismiss();
                    }
                });
                paint2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mBoard.setToolType(19);
                        mBoard.setHighlighterColor(new TEduBoardController.TEduBoardColor(Color.GREEN));
                        mBoard.setBrushThin(cur_Highlighterpaintsize);  //?????????????????????  ?????????????????????  ???????????????
                        menu02color.setBackground(getResources().getDrawable(R.color.bg_selected_menu));

                        //????????????????????????  ?????????????????????????????????     ??????SDK???????????????  ?????????????????????bug
                        if(mBoard.getBrushColor().toInt()==8947848){
                            menu02color.setImageResource(R.mipmap.text_gray);
                        }else if(mBoard.getHighlighterColor().toInt()==0){
                            menu02color.setImageResource(R.mipmap.text_black);
                        }else if(mBoard.getHighlighterColor().toInt()==255){
                            menu02color.setImageResource(R.mipmap.text_blue);
                        }else if(mBoard.getHighlighterColor().toInt()==16777215){
                            menu02color.setImageResource(R.mipmap.text_green);
                        }else if(mBoard.getHighlighterColor().toInt()==16776960){
                            menu02color.setImageResource(R.mipmap.text_yellow);
                        }else if(mBoard.getHighlighterColor().toInt()==16711680) {
                            menu02color.setImageResource(R.mipmap.text_red);
                        }

                        pw_selectpaint.dismiss();
                    }
                });
                paint3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        ????????????????????????
                        mBoard.setBrushThin(cur_paintsize);
                        mBoard.setPenAutoFittingMode(TEduBoardController.TEduBoardPenFittingMode.AUTO);
                        //?????????????????????????????????
                        if(mBoard.getBrushColor().toInt()==8947848){
                            menu02color.setImageResource(R.mipmap.text_gray);
                        }else if(mBoard.getBrushColor().toInt()==0){
                            menu02color.setImageResource(R.mipmap.text_black);
                        }else if(mBoard.getBrushColor().toInt()==255){
                            menu02color.setImageResource(R.mipmap.text_blue);
                        }else if(mBoard.getBrushColor().toInt()==65280){
                            menu02color.setImageResource(R.mipmap.text_green);
                        }else if(mBoard.getBrushColor().toInt()==16776960){
                            menu02color.setImageResource(R.mipmap.text_yellow);
                        }else if(mBoard.getBrushColor().toInt()==16711680) {
                            menu02color.setImageResource(R.mipmap.text_red);
                        }
                        pw_selectpaint.dismiss();
                    }
                });
            }
        });
        //???????????????  ???3?????????  ?????? ??????
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
        //???????????????  ???4?????????  ????????????????????????
        menu04 = findViewById(R.id.menu04);
        menu04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //?????? ??????????????????
                mBoard.setToolType(6);
                setLeftmenustatus(true);
                menu04.setBackgroundResource(R.mipmap.menu_04_jihe1);
                menu04color.setBackground(getResources().getDrawable(R.color.bg_selected_menu));
                //?????? ??????????????????
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_DOTTED; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.endArrowType = TEduBoardController.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_NORMAL;//???????????? ????????????TEDU_BOARD_ARROW_TYPE_SOLID ???????????? TEDU_BOARD_ARROW_TYPE_NORMAL ????????? TEDU_BOARD_ARROW_TYPE_NONE
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.startArrowType = TEduBoardController.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_NORMAL;//???????????? ????????????TEDU_BOARD_ARROW_TYPE_SOLID ???????????? TEDU_BOARD_ARROW_TYPE_NORMAL ????????? TEDU_BOARD_ARROW_TYPE_NONE
                        style.endArrowType = TEduBoardController.TEduBoardArrowType.TEDU_BOARD_ARROW_TYPE_NORMAL;//???????????? ????????????TEDU_BOARD_ARROW_TYPE_SOLID ???????????? TEDU_BOARD_ARROW_TYPE_NORMAL ????????? TEDU_BOARD_ARROW_TYPE_NONE
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeSOLID;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
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
                        style.lineType = TEduBoardController.TEduBoardLineType.TEDU_BOARD_LINE_TYPE_SOLID; //????????????????????????  ??????TEDU_BOARD_LINE_TYPE_SOLID = 1  ??????TEDU_BOARD_LINE_TYPE_DOTTED=2
                        style.fillType = TEduBoardController.TEduBoardFillType.TEduBoardFillTypeNONE;//????????????, ????????????????????????????????????????????????   ?????????TEduBoardFillTypeNONE = 1 ??????TEduBoardFillTypeSOLID=2
                        mBoard.setGraphStyle(style);
                        mBoard.setToolType(25);
                        pw_selecgeometry.dismiss();
                    }
                });

            }
        });
        //???????????????  ???5?????????  ??????????????????
        menu05 = findViewById(R.id.menu05);
        menu05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(9);
                setLeftmenustatus(true);
                menu05.setBackgroundResource(R.mipmap.menu_05_select1);
            }
        });
        //???????????????  ???6?????????  ????????????????????????
        menu06 = findViewById(R.id.menu06);
        menu06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????????????????
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
        //???????????????  ???7?????????  ??????????????????
        menu07 = findViewById(R.id.menu07);
        menu07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(12);
                b_size.setText(mBoard.getBoardScale()+"");
                setLeftmenustatus(true);
                menu07.setBackgroundResource(R.mipmap.menu_07_move1);
            }
        });
        //???????????????  ???8?????????  ???????????????
        menu08 = findViewById(R.id.menu08);
        menu08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(2);
                setLeftmenustatus(true);
                menu08.setBackgroundResource(R.mipmap.menu_08_earsea1);
                //?????????????????????
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
        //???????????????  ???9?????????  ????????????
        menu09 = findViewById(R.id.menu09);
        menu09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.clear(false);
                mBoard.setToolType(0);
            }
        });
        //???????????????  ???10?????????  ????????? ??????
        menu10 = findViewById(R.id.menu10);
        menu10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setToolType(3);
                setLeftmenustatus(true);
                menu10.setBackgroundResource(R.mipmap.menu_101);
            }
        });

        //???????????????  ???11?????????  ?????? ??????
        menu11 = findViewById(R.id.menu11);
        menu11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout setBoardWindow = findViewById(R.id.setBoardWindow);
                if(setBoardWindow.getVisibility()==View.VISIBLE){
                    setBoardWindow.setVisibility(View.GONE);
                    //???????????? ??????Text???bug
                    if (mBoard.getToolType() == 11) {
                        mBoard.setToolType(11);
                    }
                }else {
                    setBoardWindow.setVisibility(View.VISIBLE);
                    //??????????????????ViewPager???Fragment???????????????
                    ViewPager setboardviewPager = findViewById(R.id.set_board_tar_bar_viewpage);
                    setboardviewPager.setOffscreenPageLimit(3);
                    //????????????
                    Set_eraser_Fragment set_eraser_fragment = new Set_eraser_Fragment("4");
                    Set_geometry_Fragment set_geometry_fragment = new Set_geometry_Fragment();
                    Set_text_Fragment set_text_fragment = new Set_text_Fragment();
                    Set_paint_Fragment set_paint_fragment = new Set_paint_Fragment();
                    Set_bg_Fragment set_bg_fragment = new Set_bg_Fragment();
                    Set_more_Fragment set_more_fragment = new Set_more_Fragment();
                    Set_Highlighter_Fragment set_highlighter_fragment = new Set_Highlighter_Fragment();
//                ????????????  ????????????   ????????????|????????????|??????????????????|???????????? +  ????????????  +  ????????????
                    String[] sl=null;
                    mTabFragmenList.clear();
                    if (mBoard.getToolType() == 1) {
                        sl = new String[]{"????????????", "????????????", "????????????"};
                        mTabFragmenList.add(set_paint_fragment);
                    } else if (mBoard.getToolType() == 11) {
                        mBoard.setToolType(11);
                        sl = new String[]{"????????????", "????????????", "????????????"};
                        mTabFragmenList.add(set_text_fragment);
                    } else if (mBoard.getToolType() == 6) {
                        sl = new String[]{"??????????????????", "????????????", "????????????"};
                        mTabFragmenList.add(set_geometry_fragment);
                    } else if (mBoard.getToolType() == 2) {
                        sl = new String[]{"????????????", "????????????", "????????????"};
                        mTabFragmenList.add(set_eraser_fragment);
                    } else if (mBoard.getToolType() == 19) {   //????????????????????????
                        sl = new String[]{"????????????", "????????????", "????????????"};
                        mTabFragmenList.add(set_highlighter_fragment);
                    }else {
                        sl = new String[]{"????????????", "????????????"};
                    }

                    mTabFragmenList.add(set_bg_fragment);
                    mTabFragmenList.add(set_more_fragment);

                    SetBrd_TabBarAdapter setBrd_tabBarAdapter = new SetBrd_TabBarAdapter(getSupportFragmentManager());

                    setBrd_tabBarAdapter.setmTitles(sl);
                    setBrd_tabBarAdapter.setmFragment(mTabFragmenList);
                    //??????Fragment??????????????????Tag???????????????????????????????????????Fragment  ???????????????????????????
                    setBrd_tabBarAdapter.changeId();
                    setBrd_tabBarAdapter.notifyDataSetChanged();
                    setboardviewPager.setAdapter(setBrd_tabBarAdapter);
                    TabLayout mTabLayout = findViewById(R.id.setboard_tar_bar);
                    mTabLayout.setupWithViewPager(setboardviewPager);

                }

            }
        });

        //???????????????  ???12?????????  ????????? ?????? ????????????
        menu12 = findViewById(R.id.menu12);
        menu12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.setBoardWindow).setVisibility(View.GONE);
//                ?????????
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

        //???????????????  ???1?????????  ????????????
        menub01 = findViewById(R.id.menub01);
        menub01.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    menub01.setBackgroundResource(R.mipmap.menu_b011);//?????????????????????????????????
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    menub01.setBackgroundResource(R.mipmap.menu_b01); //????????????????????????
                }
                return false;
            }
        });
        menub01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.undo();
            }
        });
        //???????????????  ???2?????????  ????????????
        menub02 = findViewById(R.id.menub02);
        menub02.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    menub02.setBackgroundResource(R.mipmap.menu_b021);//?????????????????????????????????
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    menub02.setBackgroundResource(R.mipmap.menu_b02); //????????????????????????
                }
                return false;
            }
        });
        menub02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.redo();}
        });
        //???????????????  ???3?????????  ?????? ????????????????????? ???100%???
        menub03 = findViewById(R.id.menub03);
        menub03.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    menub03.setBackgroundResource(R.mipmap.menu_b031);//?????????????????????????????????
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    menub03.setBackgroundResource(R.mipmap.menu_b03); //????????????????????????
                }
                return false;
            }
        });
        menub03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBoard.setBoardScale(100);
                b_size.setText("100");
            }
        });
        //???????????????  ???4?????????  ?????????????????? ??????
        menub04 = findViewById(R.id.menub04);
        menub04.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    menub04.setBackgroundResource(R.mipmap.menu_b041);//?????????????????????????????????
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    menub04.setBackgroundResource(R.mipmap.menu_b04); //????????????????????????
                }
                return false;
            }
        });
        menub04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Integer.parseInt(b_size.getText().toString())-10>=100){
                    mBoard.setBoardScale( Integer.parseInt(b_size.getText().toString())-10);
                    b_size.setText(mBoard.getBoardScale()+"");
                }else {
                    mBoard.setBoardScale(100);
                    b_size.setText("100");
                }
            }
        });
        //???????????????  ???5?????????  ?????????????????? ??????
        menub05 = findViewById(R.id.menub05);
        menub05.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    menub05.setBackgroundResource(R.mipmap.menu_b051);//?????????????????????????????????
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    menub05.setBackgroundResource(R.mipmap.menu_b05); //????????????????????????
                }
                return false;
            }
        });
        menub05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Integer.parseInt(b_size.getText().toString())+10<=300){
                    mBoard.setBoardScale( Integer.parseInt(b_size.getText().toString())+10);
                    b_size.setText(mBoard.getBoardScale()+"");
                }else{
                    mBoard.setBoardScale(300);
                    b_size.setText("300");
                }
            }
        });
        //???????????????  ???6?????????  ???????????? ??????
        menub06= findViewById(R.id.menub06);
        menub06.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    menub06.setBackgroundResource(R.mipmap.menu_b061);//?????????????????????????????????
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    menub06.setBackgroundResource(R.mipmap.menu_b06); //????????????????????????
                }
                return false;
            }
        });
        menub06.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TEduBoardController.TEduBoardSnapshotInfo path = new TEduBoardController.TEduBoardSnapshotInfo();
                if(CurType=="Board"){
                    path.path = getCacheDir()+"/"+CurBoardID+".png";
                }else {
                    path.path= getCacheDir()+"/"+CurFileID+".png";
                }
//                mBoard.snapshot(path);
                mBoard.prevBoard();
            }
        });
        //???????????????  ???7?????????  ???????????? ??????
        menub07 = findViewById(R.id.menub07);
        menub07.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    menub07.setBackgroundResource(R.mipmap.menu_b071);//?????????????????????????????????
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    menub07.setBackgroundResource(R.mipmap.menu_b07); //????????????????????????
                }
                return false;
            }
        });
        menub07.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TEduBoardController.TEduBoardSnapshotInfo path = new TEduBoardController.TEduBoardSnapshotInfo();
                if(CurType=="Board"){
                    path.path = getCacheDir()+"/"+CurBoardID+".png";
                }else {
                    path.path= getCacheDir()+"/"+CurFileID+".png";
                }
//                mBoard.snapshot(path);
                mBoard.nextBoard();
            }
        });
        //???????????????  ???8?????????  ???????????? ??????
        menub08 = findViewById(R.id.menub08);
        menub08.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    menub08.setBackgroundResource(R.mipmap.menu_b081);//?????????????????????????????????
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    menub08.setBackgroundResource(R.mipmap.menu_b08); //????????????????????????
                }
                return false;
            }
        });
        menub08.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????
                mBoard.addBoard(null,TEduBoardController.TEduBoardImageFitMode.TEDU_BOARD_IMAGE_FIT_MODE_CENTER, TEduBoardController.TEduBoardBackgroundType.TEDU_BOARD_BACKGROUND_IMAGE,true);
            }
        });
        //???????????????  ???9?????????  ??????????????? ??????
        menub09 = findViewById(R.id.menub09);
        menub09.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    menub09.setBackgroundResource(R.mipmap.menu_b091);//?????????????????????????????????
                }else if(event.getAction() == MotionEvent.ACTION_UP){
                    menub09.setBackgroundResource(R.mipmap.menu_b09); //????????????????????????
                }
                return false;
            }
        });
        menub09.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //????????????
                if(mBoard.getBoardList().size()>1){
                    mBoard.deleteBoard(null);
                }
            }
        });
        //???????????????  ???10?????????  ??????????????????????????? ??????
        menub10 = findViewById(R.id.menub10);
        menub02_1 = findViewById(R.id.menub02_1);
        menub05_1 = findViewById(R.id.menub05_1);
        menub10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(menu_b_status){
                    menub02_1.setVisibility(View.GONE);
                    menub05_1.setVisibility(View.GONE);
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
                    menub02_1.setVisibility(View.VISIBLE);
                    menub05_1.setVisibility(View.VISIBLE);
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

    //????????????  ???????????? ???  ??????????????????
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
        if(issetbg&&findViewById(R.id.setBoardWindow).getVisibility()==View.VISIBLE){
            findViewById(R.id.setBoardWindow).setVisibility(View.GONE);
        }
    }
    /**
     * ????????????
     * @param extension ????????????  @param action    ????????????  @param id        ????????????ID
     */
    public void drawAuthority(String extension, String action, String id) {
        // ?????????????????????????????? ??????
        final V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(
                action.getBytes(),       //data
                id+"_WhiteBoard",     //descripition
                extension.getBytes());   //extension
        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, null,roomid, V2TIMMessage.V2TIM_PRIORITY_HIGH, false, null, new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int progress) {
            }
            @Override
            public void onSuccess(V2TIMMessage message) {
                System.out.println("+++???????????????????????????????????????????????????");
            }
            @Override
            public void onError(int code, String desc) {
                System.out.println("+++????????????????????????????????????????????????"+desc);
            }
        });
    }

    public void sendMsg(Chat_Msg msg){
        // ??????????????????
        final V2TIMMessage v2TIMMessage_chat = V2TIMManager.getMessageManager().createCustomMessage(
                msg.getContent().getBytes(),       //data
                ("2@#@"+userName+"@#@"+userHead),  //descripition
                "TBKTExt".getBytes());             //extension
        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage_chat, null,roomid, V2TIMMessage.V2TIM_PRIORITY_NORMAL, false, null, new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int progress) {
            }
            @Override
            public void onSuccess(V2TIMMessage message) {
                System.out.println("+++???????????????????????????");
            }
            @Override
            public void onError(int code, String desc) {
                System.out.println("+++????????????????????????");
            }
        });
    }

    //???????????? ?????????  ?????? ??????????????? ??????
    public void stopAllchat(Boolean isstop){
        // ????????????
        V2TIMGroupInfo info = new V2TIMGroupInfo();
        info.setGroupID(roomid);
        info.setAllMuted(isstop);
        V2TIMManager.getGroupManager().setGroupInfo(info, new V2TIMCallback() {
            @Override
            public void onSuccess() {
                // ??????????????????
                ChatRoomFragment f = (ChatRoomFragment)getmFragmenglist().get(1);
                EditText ed =  f.getView().findViewById(R.id.inputtext);
                Switch sw = f.getView().findViewById(R.id.stopchat);
                sw.setChecked(isstop);
                if(isstop){
                    ed.setHint("?????????????????????");
                    //??????????????????????????????
                    sw.setChecked(true);
                }else {
                    ed.setHint("?????????????????????");
                    sw.setChecked(false);
                }
            }

            @Override
            public void onError(int code, String desc) {
                // ??????????????????
                System.out.println("+++?????????????????????"+code+desc);
                ChatRoomFragment f = (ChatRoomFragment)getmFragmenglist().get(1);
                Switch sw = f.getView().findViewById(R.id.stopchat);
                sw.setChecked(false);
                if(code==10007){
                    sw.setChecked(false);
                    EditText ed =  f.getView().findViewById(R.id.inputtext);
                    ed.setHint("????????????????????????");

                }

            }
        });
    }


    public void onExitLiveRoom() {
        HttpActivity.overClass("leave", "skydt", this);
        final V2TIMMessage v2TIMMessage = V2TIMManager.getMessageManager().createCustomMessage(
                "finish".getBytes(),       //data
                "all"+"_WhiteBoard",     //descripition
                "exitRoomNotice".getBytes());   //extension
        V2TIMManager.getMessageManager().sendMessage(v2TIMMessage, null,roomid, V2TIMMessage.V2TIM_PRIORITY_HIGH, false, null, new V2TIMSendCallback<V2TIMMessage>() {
            @Override
            public void onProgress(int progress) {
            }
            @Override
            public void onSuccess(V2TIMMessage message) {
                System.out.println("+++?????????????????????????????????");
            }
            @Override
            public void onError(int code, String desc) {
                System.out.println("+++????????????????????????"+desc);
            }
        });
        stopTime();
        mTRTCCloud.exitRoom();
    }

    //?????? ??????????????????
    public void destroyBoard() {
        System.out.println("+++?????????????????????");
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


    //???????????????????????????
    private void intoFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");//???????????????
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 12); //requestCode==1   ??????????????????????????????
    }

    //??????????????????????????????  requestCode==12?????????   ?????????????????????  MP3  MP4  doc  docx ppt  pptx  pdf  png  jpg
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK&&requestCode==12) {
            Uri uri = data.getData();               // ??????uri   ???????????????Uri??????????????????
            String path= UriUtils.getFileAbsolutePath(this,uri);
            File ff = new File(path);
            String name = ff.getName();
            System.out.println("+++path:"+path);   //????????????????????????
            System.out.println("+++name:"+name);   //??????????????????
            Long ffsize = ff.length();
            if(name.endsWith(".mp3")){
                if(ffsize>104857600){filename.setText("mp3???????????????"); // mp3????????????
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
            }else if(name.endsWith(".mp4")){
                if(ffsize>104857600){ filename.setText("mp4????????????,???????????????!");// mp4????????????
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
            }else if(name.endsWith(".pdf")){
                if(ffsize>20971520){filename.setText("pdf????????????,???????????????!");// pdf????????????
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
                System.out.println("+++????????????pdf??????");
            }else if(name.endsWith(".doc")||name.endsWith(".docx")){
                System.out.println("+++????????????doc??????");
                if(ffsize>20971520){filename.setText("doc????????????,???????????????!");// doc????????????
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
            }else if(name.endsWith(".ppt")||name.endsWith(".pptx")){
                System.out.println("+++????????????ppt??????");
                if(ffsize>20971520){filename.setText("ppt????????????,???????????????!");// ppt????????????
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
                System.out.println("+++??????????????????"+name);
            }else if(name.endsWith(".png")||name.endsWith(".jpg")){
                if(ffsize>104857600){filename.setText("??????????????????,???????????????!"); // ??????????????????
                }else {
                    isincludeType=true;
                    filename.setText(name);
                }
                System.out.println("+++??????????????????"+name);
            }else {
                filename.setText("???????????????????????????,???????????????!");
                System.out.println("+++???????????????????????????");
            }
            if(isincludeType){
                //??????????????????????????????????????? ???????????????name path
                curfilename=name;
                curfilepath=path;
            }
        }
    }

    //??????????????????
    private void InitBucket(Context context) {
        // String secretId = "AKID5ybx2rPggPr23oHUR8YhZBWZLr6xaw2r"; //???????????? secretId
        // String secretKey = "auxjESQCk11lEQL0O5WhbEZdRyEDwOYR"; //???????????? secretKey
        // keyDuration ??????????????????????????????????????????????????????????????????????????????
        QCloudCredentialProvider myCredentialProvider =
                new ShortTimeCredentialProvider(MsecretId, MsecretKey, 300);
        // ??????????????????????????????????????????????????? ap-guangzhou
        String region = "ap-guangzhou";

        // ?????? CosXmlServiceConfig ????????????????????????????????????????????????
        CosXmlServiceConfig serviceConfig = new CosXmlServiceConfig.Builder()
                .setRegion(region)
                .isHttps(true) // ?????? HTTPS ??????, ????????? HTTP ??????
                .builder();
        // ????????? COS Service???????????????
        cosXmlService = new CosXmlService(context, serviceConfig, myCredentialProvider);
    }

    //????????????????????????  ?????????????????????  ???  ??????????????????  ???   ????????????   ???   ????????????????????????
    private void UploadToBucket(String cosprefix,String path,String name,boolean isdelete){
        isquestion=false;
        //cosprefix  ???????????????    path ??????????????????   name  ??????
        // ?????? COS ??????  ????????????
        // ????????? TransferConfig???????????????????????????????????????????????????????????? SDK ????????????
                TransferConfig transferConfig = new TransferConfig.Builder().build();
        // ????????? TransferManager
                TransferManager transferManager = new TransferManager(cosXmlService,transferConfig);

        // ?????????????????????bucketname-appid ?????????appid????????????????????????COS????????????????????????????????? https://console.cloud.tencent.com/cos5/bucket
        // String bucket = Mbucket;

                //  class/???/???/???/subjectId/roomId/res/xxxx.ppt   ??????????????????
                String cosPath = cosprefix + name; //?????????????????????????????????????????????????????????    ?????????
                String srcPath = new File(path).toString(); //???????????????????????????

        //????????????????????????????????? UploadId????????????????????? uploadId ????????????????????????????????? null
                String uploadId = null;
        // ????????????
                COSXMLUploadTask cosxmlUploadTask = transferManager.upload(Mbucket, cosPath,srcPath, uploadId);

        //????????????????????????
                cosxmlUploadTask.setCosXmlProgressListener(new CosXmlProgressListener() {
                    @Override
                    public void onProgress(long complete, long target) {
                        proBar.setProgress((int)(complete*100/target));
                    }
                });
        //????????????????????????
                cosxmlUploadTask.setCosXmlResultListener(new CosXmlResultListener() {
                    @Override
                    public void onSuccess(CosXmlRequest request, CosXmlResult result) {
                        COSXMLUploadTask.COSXMLUploadTaskResult uploadResult =
                                (COSXMLUploadTask.COSXMLUploadTaskResult) result;
                        if(name.endsWith("doc")||name.endsWith("pdf")||name.endsWith("docx")){
         //doc  pdf  docx  ??????????????????????????????
                            runnablere_mBoardaddResouce = () -> {
                                try {
                                        uploadfile.setText("????????????");
                                        msgTips.setText("?????????????????????");
                                        proBar.setProgress(0);
                                        CreateTranscode(result.accessUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            };
                        }else if(name.endsWith("png")||name.endsWith("jpg")){
         // ??????????????????
                            runnablere_mBoardaddResouce = () -> {
                                try {
                                    uploadfile.setText("????????????");
                                    msgTips.setText("?????????????????????");
                                    proBar.setProgress(95);
                                    mBoard.addElement(TEduBoardController.TEduBoardElementType.TEDU_BOARD_ELEMENT_IMAGE,result.accessUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            };
                        }else if(name.endsWith("mp3")){
          //png  ????????????
                            runnablere_mBoardaddResouce = () -> {
                                try {
                                    uploadfile.setText("????????????");
                                    msgTips.setText("?????????????????????");
                                    proBar.setProgress(95);
                                    mBoard.addElement(TEduBoardController.TEduBoardElementType.TEDU_BOARD_ELEMENT_AUDIO,result.accessUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            };

                        } else{
                            if(name.endsWith("ppt")||name.endsWith("pptx")){
         //ppt  pptx  ????????????????????????  ????????????
                                runnablere_mBoardaddResouce = () -> {
                                                try {
                                                    uploadfile.setText("????????????");
                                                    msgTips.setText("?????????????????????");
                                                    proBar.setProgress(95);
                                                    mBoardAddTranscodeFile(name,result.accessUrl+"?for_tiw=1");
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                };

                            }else {
         //  mp4 ???????????????  ????????????
                                runnablere_mBoardaddResouce = () -> {
                                    try {
                                        uploadfile.setText("????????????");
                                        msgTips.setText("?????????????????????");
                                        proBar.setProgress(95);
                                        mBoard.addVideoFile(result.accessUrl,name,true);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                };

                            }
                        }
                    }
                    // ??????????????? kotlin ???????????????????????????????????????????????????????????????????????????????????? onFail ???????????????
                    // clientException ???????????? CosXmlClientException????serviceException ???????????? CosXmlServiceException?
                    @Override
                    public void onFail(CosXmlRequest request,
                                       @Nullable CosXmlClientException clientException,
                                       @Nullable CosXmlServiceException serviceException) {
                        System.out.println("+++????????????"+clientException.toString());
                        System.out.println("+++????????????"+serviceException.toString());
                        if (clientException != null) {
                            clientException.printStackTrace();
                        } else {
                            serviceException.printStackTrace();
                        }
                    }
                });
        //????????????????????????, ????????????????????????
                cosxmlUploadTask.setTransferStateListener(new TransferStateListener() {
                    @Override
                    public void onStateChanged(TransferState state) {
                        System.out.println("+++??????????????????,"+state.toString().equals("COMPLETED"));
                        if(state.toString().equals("COMPLETED")){
                            if(isdelete){
                                File ff = new File(path);
                                ff.delete();
                            }
                            curfilename="";   // ??????????????????????????????????????????????????????
                            curfilepath="";   // ??????????????????????????????????????????????????????
                            handlerCount.postDelayed(runnablere_mBoardaddResouce, 500); // 1000ms???????????????runnablerefreshStatus
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

    //??????????????????  ???????????????????????? ?????????????????????
    private void mBoardAddTranscodeFile(String name,String url) {
        TEduBoardController.TEduBoardTranscodeFileResult config = new TEduBoardController.TEduBoardTranscodeFileResult(name,url);
        mBoard.addTranscodeFile(config,true);
    }

    //?????? ?????????????????????????????????????????????JSON??????
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

    //?????????????????? ???pdf  doc   docx???
    private void CreateTranscode(String slink) {
        //????????????????????????
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

                    // ??????
                    bufferedReader.close();
                    reader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();

                    try{
                        String backLogJsonStr = buffer.toString();
                        JSONObject json = stringToJson(backLogJsonStr);
                        String status = json.get("Status").toString();
                        if(status.equals("success")){
                            System.out.println("+++????????????????????????????????????taskId:;"+json.get("TaskId"));
//                          ???????????????????????? ??????????????????????????????
                            proBar.setProgress(0);
                            msgTips.setText("?????????????????????");
                            uploadfile.setText("????????????");
                            DescribeTranscodehandler(MsecretId,MsecretKey,Region,SdkAppId,json.get("TaskId").toString(),mBoard,proBar);
                        }else {
//                            ??????????????????????????????????????????????????????????????????????????????
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


    //?????? ???????????? ???????????????
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
//        handlerCount.postDelayed(runnablere_mBoardaddResouce, 500); // 500ms???????????????runnablerefreshStatus
    }

    //??????????????????
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
                    // ??????
                    bufferedReader.close();
                    reader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
//        "Status": "PROCESSING",  "Progress": "10",//?????????????????????0--100 "ResultUrl": "",//?????????????????????????????????  "Pages": "3", "Title": "??????????????????", "Resolution": "1280*800"
//        ??????Status??????  - QUEUED: ????????????????????????  - PROCESSING: ????????? - FINISHED: ???????????? - fail:????????????????????????????????????????????????????????????????????????????????????????????????
                    try{
                        String backLogJsonStr = buffer.toString();
                        System.out.println("+++?????????????????????????????????"+backLogJsonStr);
                        JSONObject json = stringToJson(backLogJsonStr);
                        System.out.println("+++?????????????????????????????????"+json.get("Status"));
                        String status = json.get("Status").toString();
                        if(status.equals("FINISHED")){
                            ResultUrl =  json.get("ResultUrl").toString();
                            System.out.println("+++?????????????????????????????????"+ResultUrl);
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
                            System.out.println("+++?????????????????????????????????");
//                            ????????????????????????????????????????????????????????????????????????????????????????????????
                        }else if(status.equals("PROCESSING")){
                            progressBar.setProgress(Integer.parseInt(json.get("Progress").toString()));
                            System.out.println("+++?????????????????????????????????......???"+json.get("Progress"));
//                            json.get("PROCESSING").toString();???????????????
                        }else if(status.equals("QUEUED")){
                            System.out.println("+++?????????????????????????????????......???"+json.get("Progress"));
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


//????????????   ???????????????
    public static void ScreenShotBoard(Context context,String answerQuestionId,TEduBoardController mBoard){
        isquestion=true;//????????????????????????????????????true  ???????????????????????????
        //????????????
        TEduBoardController.TEduBoardSnapshotInfo path = new TEduBoardController.TEduBoardSnapshotInfo();
        path.path=context.getCacheDir()+"/"+answerQuestionId+".png";
        mBoard.snapshot(path);
        // class/???/???/???/subjectId/roomId/question/questionId.png   ???????????????
    }

    // ?????? ???????????? ??????  ?????????????????????
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

    //???????????????????????????  ??????????????????  ?????? ??????  ???????????????????????????????????????   ??????????????????fragment??????
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

