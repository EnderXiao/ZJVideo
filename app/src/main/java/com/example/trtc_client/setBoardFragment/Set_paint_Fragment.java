package com.example.trtc_client.setBoardFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.trtc_client.MainActivity;
import com.example.trtc_client.R;
import com.tencent.teduboard.TEduBoardController;


public class Set_paint_Fragment extends Fragment {

    private LinearLayout setpaintsize1,setpaintsize2,setpaintsize3,setpaintsize4,setpaintsize5,setpaintsize6,setpaintgray,setpaintblack,setpaintblue,setpaintgreen,setpaintyellow,setpaintred;
    private ImageButton paintsize1,paintsize2,paintsize3,paintsize4,paintsize5,paintsize6,paintgray,paintblack,paintblue,paintgreen,paintyellow,paintred;


    public static Set_paint_Fragment newInstance(String param1, String param2) {
        Set_paint_Fragment fragment = new Set_paint_Fragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.set_paint_fragment, container, false);
        MainActivity activity = (MainActivity) getActivity();
        paintsize1 = view.findViewById(R.id.paintsize1);
        setpaintsize1 = view.findViewById(R.id.setpaintsize1);
        paintsize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(40);
                setpaintsize1.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintsize2 = view.findViewById(R.id.paintsize2);
        setpaintsize2 = view.findViewById(R.id.setpaintsize2);
        paintsize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(70);
                setpaintsize2.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintsize3 = view.findViewById(R.id.paintsize3);
        setpaintsize3 = view.findViewById(R.id.setpaintsize3);
        paintsize3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(100);
                setpaintsize3.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintsize4 = view.findViewById(R.id.paintsize4);
        setpaintsize4 = view.findViewById(R.id.setpaintsize4);
        paintsize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(150);
                setpaintsize4.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintsize5 = view.findViewById(R.id.paintsize5);
        setpaintsize5 = view.findViewById(R.id.setpaintsize5);
        paintsize5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(200);
                setpaintsize5.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintsize6 = view.findViewById(R.id.paintsize6);
        setpaintsize6 = view.findViewById(R.id.setpaintsize6);
        paintsize6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(250);
                setpaintsize6.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });

        paintgray = view.findViewById(R.id.paintgray);
        setpaintgray = view.findViewById(R.id.setpaintgray);
        paintgray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","gray");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.GRAY));
                setpaintgray.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintblack = view.findViewById(R.id.paintblack);
        setpaintblack = view.findViewById(R.id.setpaintblack);
        paintblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","black");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.BLACK));
                setpaintblack.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintblue = view.findViewById(R.id.paintblue);
        setpaintblue = view.findViewById(R.id.setpaintblue);
        paintblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","blue");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.BLUE));
                setpaintblue.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintgreen = view.findViewById(R.id.paintgreen);
        setpaintgreen = view.findViewById(R.id.setpaintgreen);
        paintgreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","green");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.GREEN));
                setpaintgreen.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintyellow = view.findViewById(R.id.paintyellow);
        setpaintyellow = view.findViewById(R.id.setpaintyellow);
        paintyellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","yellow");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.YELLOW));
                setpaintyellow.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        paintred = view.findViewById(R.id.paintred);
        setpaintred = view.findViewById(R.id.setpaintred);
        paintred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","red");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.RED));
                setpaintred.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });

        if(activity.getmBoard()!=null){
            if(activity.getmBoard().getBrushThin()==40){
                setpaintsize1.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==70){
                setpaintsize2.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==100){
                setpaintsize3.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==150){
                setpaintsize4.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==200){
                setpaintsize5.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==250){
                setpaintsize6.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
            if(activity.getmBoard().getBrushColor().toInt()==8947848){
                setpaintgray.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushColor().toInt()==0){
                setpaintblack.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushColor().toInt()==255){
                setpaintblue.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushColor().toInt()==65280){
                setpaintgreen.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushColor().toInt()==16776960){
                setpaintyellow.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushColor().toInt()==16711680){
                setpaintred.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        }

        return view;
    }

    public void setLinSizestatus(){
        setpaintsize1.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintsize2.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintsize3.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintsize4.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintsize5.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintsize6.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
    }
    public void setLinColorstatus(){
        setpaintgray.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintblack.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintblue.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintgreen.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintyellow.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        setpaintred.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
    }

}