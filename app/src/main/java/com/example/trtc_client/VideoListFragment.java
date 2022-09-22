package com.example.trtc_client;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tencent.liteav.TXLiteAVCode;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tencent.trtc.TRTCCloud;
import com.tencent.trtc.TRTCCloudDef;
import com.tencent.trtc.TRTCCloudListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Stack;
import java.util.TreeMap;

public class VideoListFragment extends Fragment {

    public static String TAG = "Ender_VideoListFragment";

    public static ArrayList<String> mUserList = new ArrayList<>();
    public static ArrayList<String> mCameraUserList = new ArrayList<>();
    public static TreeMap<String, CameraFragment> mCameraFragmentMap = new TreeMap<String, CameraFragment>();
    public static Stack<Integer> availableFragment = new Stack<>();
    public static TreeMap<String, Integer> occupiedFragment = new TreeMap<>();

    // TRTC监听器
    public static TRTCCloud mTRTCCloud;
    public static TRTCCloudListener myListener;
    public static ArrayList<CameraFragment> videoFragmentList = new ArrayList<>();

    public static LinearLayout ScrollContainer;

    // Fragment管理

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.video_list, container, false);
        Bundle arguments = getArguments();

        mTRTCCloud = TRTCCloud.sharedInstance(getActivity().getApplicationContext());
        mTRTCCloud.setListener(new MyTRTCCloudListener(VideoListFragment.this));

        if(arguments != null) {
            mUserList = arguments.getStringArrayList("mUserList");
        }
        initView(view);
        return view;
    }

    public void initView(View view) {

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        videoFragmentList = new ArrayList<>();
        videoFragmentList.add((CameraFragment) fragmentManager.findFragmentById(R.id.camera_1));
        this.hideFragment(fragmentTransaction,videoFragmentList.get(0));
        availableFragment.push(0);
        videoFragmentList.add((CameraFragment) fragmentManager.findFragmentById(R.id.camera_2));
        this.hideFragment(fragmentTransaction,videoFragmentList.get(1));
        availableFragment.push(1);
        videoFragmentList.add((CameraFragment) fragmentManager.findFragmentById(R.id.camera_3));
        this.hideFragment(fragmentTransaction,videoFragmentList.get(2));
        availableFragment.push(2);
        videoFragmentList.add((CameraFragment) fragmentManager.findFragmentById(R.id.camera_4));
        this.hideFragment(fragmentTransaction,videoFragmentList.get(3));
        availableFragment.push(3);
        videoFragmentList.add((CameraFragment) fragmentManager.findFragmentById(R.id.camera_5));
        this.hideFragment(fragmentTransaction,videoFragmentList.get(4));
        availableFragment.push(4);
        videoFragmentList.add((CameraFragment) fragmentManager.findFragmentById(R.id.camera_6));
        this.hideFragment(fragmentTransaction,videoFragmentList.get(5));
        availableFragment.push(5);
        ScrollContainer = view.findViewById(R.id.ll_content);
        fragmentTransaction.commit();
    }

    public void hideFragment(FragmentTransaction mTransaction, CameraFragment fragment) {
        if(fragment != null) {
            mTransaction.hide(fragment);
        }
    }

    public void hideFragment(CameraFragment fragment) {
        if(fragment != null) {
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.hide(fragment);
            fragmentTransaction.commit();
        }
   }

    public void addCameraView(String userId) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Integer mUserCount = availableFragment.pop();
        CameraFragment cameraFragmentNow = videoFragmentList.get(mUserCount);
        mCameraFragmentMap.put(userId, cameraFragmentNow);
        occupiedFragment.put(userId, mUserCount);
        cameraFragmentNow.setUserName(userId);
        fragmentTransaction.show(cameraFragmentNow);
        cameraFragmentNow.showVideo(mTRTCCloud, userId);
        fragmentTransaction.commit();
    }

    public void RefreshingCameraView(ArrayList<String> mUserList) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < mUserList.size(); i++) {
            CameraFragment cameraFragment = new CameraFragment();
            fragmentTransaction.add(R.id.ll_content, cameraFragment);
        }
        fragmentTransaction.commit();
    }

    public static class MyTRTCCloudListener extends TRTCCloudListener {
        private WeakReference<VideoListFragment> mContext;

        public MyTRTCCloudListener(VideoListFragment fragment) {
            super();
            mContext = new WeakReference<>(fragment);
        }

        @Override
        public void onEnterRoom(long result) {
            VideoListFragment fragment = mContext.get();
            if(result > 0) {
                Log.e(TAG, "onEnterRoom: 进入房间成功，耗时: " + result);
//                Toast.makeText(fragment, "进入房间成功，耗时: " + "[" + result+ "]" , Toast.LENGTH_SHORT).show();
            } else {
                Log.e(TAG, "onEnterRoom: 进入房间失败，错误代码：" + result);
//                Toast.makeText(activity, "进入房间失败，错误代码: " + "[" + result+ "]" , Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUserAudioAvailable(String userId, boolean available) {
            VideoListFragment fragment = mContext.get();
            Log.d(TAG, "onUserAudioAvailable userId " + userId + ", mUserCount " + userId + ",available " + available);
            System.out.println("onUserAudioAvailable userId " + userId + ", mUserCount " + userId + ",available " + available);
            System.out.println("onUserVideoAvailable:"+userId);
            CameraFragment cameraFragment = mCameraFragmentMap.get(userId);
            if(cameraFragment != null) {
                if (available) {
                    cameraFragment.startAudio(mTRTCCloud, userId);
                } else {
                    cameraFragment.stopAudio(mTRTCCloud, userId);
                }
            } else {
                Toast.makeText(fragment.getActivity(), "未找到用户", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onUserVideoAvailable(String userId, boolean available) {
            VideoListFragment fragment = mContext.get();
            Log.d(TAG, "onUserVideoAvailable userId " + userId + ", mUserCount " + userId + ",available " + available);
//            Toast.makeText(activity, "onUserVideoAvailable userId " + userId + ", mUserCount " + mUserCount + ",available " + available , Toast.LENGTH_SHORT).show();
            System.out.println("onUserVideoAvailable userId " + userId + ", mUserCount " + userId + ",available " + available);
            System.out.println("onUserVideoAvailable:"+userId);
//            if (userId.equals(mTeacherId+"_camera")&&!available){
//                System.out.println("mingming_camera exit room");
//                exitRoom();
//                teacher_enable=false;
//                return;
//            }
            CameraFragment cameraFragmentNow = mCameraFragmentMap.get(userId);
            if(cameraFragmentNow != null) {
                if (available) {
                        cameraFragmentNow.showVideo(mTRTCCloud, userId);
                } else {
                        cameraFragmentNow.hideVideo(mTRTCCloud, userId);
//                refreshRemoteVideoViews();
                }
            } else {
                Toast.makeText(fragment.getActivity(), "未找到用户", Toast.LENGTH_SHORT).show();
            }
            for(int i =0;i<mUserList.size();i++){
                System.out.println(mUserList.get(i)+" : "+mUserList.get(i));
            }

        }

        @Override
        public void onRemoteUserEnterRoom(String userId){
            Log.e(TAG, "onRemoteUserEnterRoom: userId" + userId );
            System.out.println("onRemoteUserEnterRoom userId " + userId );
            VideoListFragment fragment = mContext.get();
            Log.e(TAG, "onRemoteUserEnterRoom: added");
            fragment.addCameraView(userId);
            // Toast.makeText(activity, "onRemoteUserEnterRoom userId " + userId , Toast.LENGTH_SHORT).show();

        }

//        private void refreshRemoteVideoViews() {
//            for (int i = 0; i < mRemoteViewList.size(); i++) {
//                if (i < mRemoteUidList.size()) {
//                    String remoteUid = mRemoteUidList.get(i);
//                    mRemoteViewList.get(i).setVisibility(View.VISIBLE);
//                    mTRTCCloud.startRemoteView(remoteUid, TRTCCloudDef.TRTC_VIDEO_STREAM_TYPE_SMALL,mRemoteViewList.get(i));
//                } else {
//                    mRemoteViewList.get(i).setVisibility(View.GONE);
//                }
//            }
//        }

        @Override
        public void onRemoteUserLeaveRoom(String userId, int reason){
            VideoListFragment fragment = mContext.get();
            System.out.println("onRemoteUserLeaveRoom userId " + userId );
            CameraFragment cameraFragmentNow = mCameraFragmentMap.get(userId);
            if(cameraFragmentNow != null) {
                cameraFragmentNow.hideVideo(mTRTCCloud, userId);
                availableFragment.push(occupiedFragment.get(userId));

            }
            fragment.hideFragment(cameraFragmentNow);
//            Toast.makeText(activity, "onRemoteUserLeaveRoom userId " + userId , Toast.LENGTH_SHORT).show();
            
        }

        @Override
        public void onError(int errCode, String errMsg, Bundle extraInfo) {
            Log.d(TAG, "sdk callback onError");
            VideoListFragment fragment = mContext.get();
//            if (activity != null) {
//                Toast.makeText(activity, "onError: " + errMsg + "[" + errCode+ "]" , Toast.LENGTH_SHORT).show();
//                if (errCode == TXLiteAVCode.ERR_ROOM_ENTER_FAIL) {
//                    activity.exitRoom();
//                }
//            }
        }
    }
}
