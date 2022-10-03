package com.example.trtc_client.setBoardFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.trtc_client.MainActivity;
import com.example.trtc_client.R;
import com.tencent.teduboard.TEduBoardController;


public class Set_Highlighter_Fragment extends Fragment {

    private LinearLayout sethihtlightersize1,sethihtlightersize2,sethihtlightersize3,sethihtlightersize4,sethihtlightersize5,sethihtlightersize6,sethihtlightergray,sethihtlighterblack,sethihtlighterblue,sethihtlightergreen,sethihtlighteryellow,sethihtlighterred;
    private ImageButton hihtlightersize1,hihtlightersize2,hihtlightersize3,hihtlightersize4,hihtlightersize5,hihtlightersize6,hihtlightergray,hihtlighterblack,hihtlighterblue,hihtlightergreen,hihtlighteryellow,hihtlighterred;


    public Set_Highlighter_Fragment() {
    }


    public static Set_Highlighter_Fragment newInstance(String param1, String param2) {
        Set_Highlighter_Fragment fragment = new Set_Highlighter_Fragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.set__highlighter_fragment, container, false);
        MainActivity activity = (MainActivity) getActivity();
        hihtlightersize1 = view.findViewById(R.id.hihtlightersize1);
        sethihtlightersize1 = view.findViewById(R.id.sethihtlightersize1);
        hihtlightersize1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(40);
                sethihtlightersize1.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlightersize2 = view.findViewById(R.id.hihtlightersize2);
        sethihtlightersize2 = view.findViewById(R.id.sethihtlightersize2);
        hihtlightersize2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(70);
                sethihtlightersize2.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlightersize3 = view.findViewById(R.id.hihtlightersize3);
        sethihtlightersize3 = view.findViewById(R.id.sethihtlightersize3);
        hihtlightersize3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(100);
                sethihtlightersize3.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlightersize4 = view.findViewById(R.id.hihtlightersize4);
        sethihtlightersize4 = view.findViewById(R.id.sethihtlightersize4);
        hihtlightersize4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(150);
                sethihtlightersize4.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlightersize5 = view.findViewById(R.id.hihtlightersize5);
        sethihtlightersize5 = view.findViewById(R.id.sethihtlightersize5);
        hihtlightersize5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(200);
                sethihtlightersize5.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlightersize6 = view.findViewById(R.id.hihtlightersize6);
        sethihtlightersize6 = view.findViewById(R.id.sethihtlightersize6);
        hihtlightersize6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinSizestatus();
                activity.getmBoard().setTextSize(250);
                sethihtlightersize6.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });

        hihtlightergray = view.findViewById(R.id.hihtlightergray);
        sethihtlightergray = view.findViewById(R.id.sethihtlightergray);
        hihtlightergray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","gray");
                activity.getmBoard().setHighlighterColor(new TEduBoardController.TEduBoardColor(Color.GRAY));
                sethihtlightergray.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlighterblack = view.findViewById(R.id.hihtlighterblack);
        sethihtlighterblack = view.findViewById(R.id.sethihtlighterblack);
        hihtlighterblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","black");
                activity.getmBoard().setHighlighterColor(new TEduBoardController.TEduBoardColor(Color.BLACK));
                sethihtlighterblack.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlighterblue = view.findViewById(R.id.hihtlighterblue);
        sethihtlighterblue = view.findViewById(R.id.sethihtlighterblue);
        hihtlighterblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","blue");
                activity.getmBoard().setHighlighterColor(new TEduBoardController.TEduBoardColor(Color.BLUE));
                sethihtlighterblue.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlightergreen = view.findViewById(R.id.hihtlightergreen);
        sethihtlightergreen = view.findViewById(R.id.sethihtlightergreen);
        hihtlightergreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","green");
                activity.getmBoard().setHighlighterColor(new TEduBoardController.TEduBoardColor(Color.GREEN));
                sethihtlightergreen.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlighteryellow = view.findViewById(R.id.hihtlighteryellow);
        sethihtlighteryellow = view.findViewById(R.id.sethihtlighteryellow);
        hihtlighteryellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","yellow");
                activity.getmBoard().setHighlighterColor(new TEduBoardController.TEduBoardColor(Color.YELLOW));
                sethihtlighteryellow.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        hihtlighterred = view.findViewById(R.id.hihtlighterred);
        sethihtlighterred = view.findViewById(R.id.sethihtlighterred);
        hihtlighterred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("paintcolor","red");
                activity.getmBoard().setHighlighterColor(new TEduBoardController.TEduBoardColor(Color.RED));
                sethihtlighterred.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });

        if(activity.getmBoard()!=null){
            System.out.println("+++荧光笔颜色"+activity.getmBoard().getHighlighterColor().toInt()+"---"+activity.getmBoard().getBrushColor().toInt());
            if(activity.getmBoard().getBrushThin()==40){
                sethihtlightersize1.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==70){
                sethihtlightersize2.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==100){
                sethihtlightersize3.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==150){
                sethihtlightersize4.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==200){
                sethihtlightersize5.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getBrushThin()==250){
                sethihtlightersize6.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
            if(activity.getmBoard().getHighlighterColor().toInt()==8947848){
                sethihtlightergray.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getHighlighterColor().toInt()==0){
                sethihtlighterblack.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getHighlighterColor().toInt()==255){
                sethihtlighterblue.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getHighlighterColor().toInt()==16777215){
                sethihtlightergreen.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getHighlighterColor().toInt()==16776960){
                sethihtlighteryellow.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getHighlighterColor().toInt()==16711680){
                sethihtlighterred.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        }

        return view;
    }

    public void setLinSizestatus(){
        sethihtlightersize1.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlightersize2.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlightersize3.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlightersize4.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlightersize5.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlightersize6.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
    }
    public void setLinColorstatus(){
        sethihtlightergray.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlighterblack.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlighterblue.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlightergreen.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlighteryellow.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        sethihtlighterred.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
    }
}