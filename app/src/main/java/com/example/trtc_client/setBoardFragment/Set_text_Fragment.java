package com.example.trtc_client.setBoardFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import androidx.fragment.app.Fragment;

import com.example.trtc_client.MainActivity;
import com.example.trtc_client.R;
import com.tencent.teduboard.TEduBoardController;


public class Set_text_Fragment extends Fragment {

    private LinearLayout settextgray,settextblack,settextblue,settextgreen,settextyellow,settextred;
    private ImageButton textgray,textblack,textblue,textgreen,textyellow,textred;
    private SeekBar seekBar;
    public Set_text_Fragment() {

    }


    public static Set_text_Fragment newInstance(String param1, String param2) {
        Set_text_Fragment fragment = new Set_text_Fragment();
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
        View view= inflater.inflate(R.layout.set_text_fragment, container, false);
        MainActivity activity = (MainActivity) getActivity();

        seekBar =view.findViewById(R.id.selectTextSize);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                System.out.println("+++proccess"+progress);
                if(progress<=1){
                    //1号字体
                    activity.getmBoard().setTextSize(240);
                }else if(progress<=2){
                    //2号字体
                    activity.getmBoard().setTextSize(320);
                }else if(progress<=3){
                    //3号字体
                    activity.getmBoard().setTextSize(700);
                }else if(progress<=4){
                    //4号字体
                    activity.getmBoard().setTextSize(1000);
                }else if(progress<=5){
                    //5号字体
                    activity.getmBoard().setTextSize(1300);
                }else {
                    //6号字体
                    activity.getmBoard().setTextSize(1600);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        textgray = view.findViewById(R.id.textgray);
        settextgray = view.findViewById(R.id.settextgray);
        textgray.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("textcolor","gray");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.GRAY));
                settextgray.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        textblack = view.findViewById(R.id.textblack);
        settextblack = view.findViewById(R.id.settextblack);
        textblack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("textcolor","black");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.BLACK));
                settextblack.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        textblue = view.findViewById(R.id.textblue);
        settextblue = view.findViewById(R.id.settextblue);
        textblue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("textcolor","blue");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.BLUE));
                settextblue.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        textgreen = view.findViewById(R.id.textgreen);
        settextgreen = view.findViewById(R.id.settextgreen);
        textgreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("textcolor","green");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.GREEN));
                settextgreen.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        textyellow = view.findViewById(R.id.textyellow);
        settextyellow = view.findViewById(R.id.settextyellow);
        textyellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("textcolor","yellow");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.YELLOW));
                settextyellow.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });
        textred = view.findViewById(R.id.textred);
        settextred = view.findViewById(R.id.settextred);
        textred.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLinColorstatus();
                activity.forSetFragmentSet("textcolor","red");
                activity.getmBoard().setBrushColor(new TEduBoardController.TEduBoardColor(Color.RED));
                settextred.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }
        });

        if(activity.getmBoard()!=null){
            if(activity.getmBoard().getTextColor().toInt()==8947848){
                settextgray.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getTextColor().toInt()==0){
                settextblack.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getTextColor().toInt()==255){
                settextblue.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getTextColor().toInt()==65280){
                settextgreen.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getTextColor().toInt()==16776960){
                settextyellow.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }else if(activity.getmBoard().getTextColor().toInt()==16711680){
                settextred.setBackground(getContext().getResources().getDrawable(R.color.blue_white));
            }


            if(activity.getmBoard().getTextSize()<=240){
                seekBar.setProgress(1);
            }else if(activity.getmBoard().getTextSize()<=320){
                seekBar.setProgress(2);
            }else if(activity.getmBoard().getTextSize()<=700){
                seekBar.setProgress(3);
            }else if(activity.getmBoard().getTextSize()<=1000){
                seekBar.setProgress(4);
            }else if(activity.getmBoard().getTextSize()<=1300){
                seekBar.setProgress(5);
            }else if(activity.getmBoard().getTextSize()<=1600){
                seekBar.setProgress(6);
            }
        }
        return view;
    }

    public void setLinColorstatus(){
        settextgray.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        settextblack.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        settextblue.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        settextgreen.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        settextyellow.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
        settextred.setBackground(getContext().getResources().getDrawable(R.color.bg_select_menu));
    }
}