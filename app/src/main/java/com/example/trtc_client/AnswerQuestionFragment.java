package com.example.trtc_client;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.trtc_client.adapter.MyAdapter;
import com.example.trtc_client.utils.MyScrollView;
import com.example.trtc_client.utils.SwZoomDragImageView;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;





public class AnswerQuestionFragment extends Fragment implements View.OnClickListener{
    final Context context = getActivity();
    private View FcontentView; //Fragment页面对应的view

    private Chronometer jishiqi;
    private TextView tx_answers_sum; //作答人数/总人数

    private TextView txType_tiwen; //互动类型-提问
    private TextView txType_suiji; //互动类型-随机
    private TextView txType_qiangda; //互动类型-抢答
    private TextView txModle_danxuan; //互动模式-单选
    private TextView txModle_duoxuan; //互动模式-多选
    private TextView txModle_panduan; //互动模式-判断
    private TextView txModle_luru; //互动模式-录入

    private ImageView imgdanxuan , imgduoxuan , imgpanduan , imgluru;

    private TextView txChoose; //选项数
    private Spinner spinner; //下拉选择框

    private Button btBegin1 , btBegin2; //开始按钮（不可点击、可点击）
    private LinearLayout linear1 , linear2; //按钮：单题分析 答题详情  结束作答 关闭提问;计时器区域
    private Button btSingle , btAnswers , btEnd , btClosed; //单题分析 答题详情  结束作答 关闭提问

    private BarChart barChart; //柱状图

    //单选UI
    private LinearLayout linear_danxuan;
    private ImageView a , b , c , d , e , f , g , h;

    //多选UI
    private LinearLayout linear_duoxuan;
    private ImageView a1 , b1 , c1 , d1 , e1 , f1 , g1 , h1;

    //判断UI
    private LinearLayout linear_panduan;
    private ImageView right , error;
    private String answer;

    //单题分析页面，左侧班级列表
    private int selectedIndex = 0;

    //单题分析 答题详情右侧页面顶部详细信息（最快答对、正确率等）
    private TextView tx_quick , tx_top1 , tx_top2 , tx_top3;
    private ImageView img_top1 , img_top2 , img_top3;

    private TextView tx_answer1 , tx_answer2 , tx_right1 , tx_right2 , tx_dati1 , tx_dati2;
    private LinearLayout linear_right , linear_quick , linear_answer;

    //答题详情页面，右侧答题情况，静态数据
    private List<String> listAnswers = Arrays.asList(new String[]{"A" , "B" , "C" , "D" , "未答"});
    private List<List<String>> listStusName = new ArrayList<List<String>>();
    private List<String> listA = Arrays.asList(new String[]{
            "孟炎仁", "王友祖"
    });

    private List<String> listB = Arrays.asList(new String[]{
            "钱宇华钱宇华", "王春云钱宇华" , "薛志钱宇华" , "苏南小钱宇华" , "郑晓钱宇华" , "钱胜利" , "吴志辉" ,
            "郑合惠子" , "王艳" , "于松石钱宇华" , "陈灵峰" , "苏志坚" , "赵祖媛" , "梦闽南" ,
            "殷勇" , "黄宏钱宇华" , "赫仑" , "市容秒" , "杨磊方" , "王丽佳钱宇华" , "钱芬宇" ,
            "王仁义" , "袁云钱宇华" , "吕宇钱宇华" , "赵晓茹" , "秦思茹" , "齐国" , "谢松大" ,
            "唐辉" , "钱蕾钱宇华" , "周松志" , "张雷华" , "郑丽媛" , "沈波博" , "王宏辉" ,
            "孙丹云" , "吴雷晓钱宇华" , "雷德元" , "严锦春"
    });

    private List<String> listC = Arrays.asList(new String[]{
            "谢军家", "王华磊" , "蒋建辉" , "吴小巧" , "张峰"
    });

    private List<String> listD = Arrays.asList(new String[]{
            "谢怀宇", "吴妙仁" , "孙国宁" , "陈思" , "马兴元" , "孟丹君" , "潘松韩" , "黄韵文"
    });

    private List<String> listNo = Arrays.asList(new String[]{
            "华彬彬彬", "华仁超" , "马德义"
    });

    //主观题学生答案
    private List<String> stusNameList = Arrays.asList(new String[]{
            "苏思坚" , "钱宇华" , "王友祖" , "孟艳仁" ,
            "华武" , "薛志" , "钱生利" , "沈泽友" ,
            "于松石" , "赵祖媛" , "梦闽南" , "王媛",
            "张宇"
    });
    private List<String> stusAnswerList = Arrays.asList(new String[]{
            "http://www.cn901.com/res/studentAnswerImg/AppImage/2021/12/09/cjzx200101_203003336.png",
            "http://www.cn901.com/res/studentAnswerImg/AppImage/2021/12/09/cjzx200106_172630439.png",
            "http://www.cn901.com/res/studentAnswerImg/AppImage/2021/12/09/cjzx200106_202531675.png",
            "http://www.cn901.com/res/studentAnswerImg/AppImage/2021/12/09/cjzx200106_202636014.png",
            "https://img0.baidu.com/it/u=3485269656,617316610&fm=253&fmt=auto&app=138&f=JPEG?w=660&h=440",
            "https://pics3.baidu.com/feed/cf1b9d16fdfaaf51055aeb850e4a2de7f01f7a73.jpeg?token=13a989ae26c8c26689240bf4a3c776a0",
            "https://pics1.baidu.com/feed/d0c8a786c9177f3eaac47e380fd182ce9f3d563f.jpeg?token=9e9fe01f9182bf35540fcc0aa68083dc",
            "两种物质以任意质量比混合，如混合物的质量一定，充分燃烧时产生的二氧化碳是定值。两种物质以任意质量比混合，如混合物的质量一定，充分燃烧时产生的二氧化言是定值。",
            "https://pics4.baidu.com/feed/b3b7d0a20cf431adeea8635f342815a62edd9801.jpeg?token=18bf863bdfcae92a41b76078d3c70bce",
            "两种有机物必须最简式相同，或者互为同分异构体。",
            "http://img2.baidu.com/it/u=4157293960,2103840381&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500",
            "https://pics6.baidu.com/feed/024f78f0f736afc3a536efa7cd0752cdb64512e6.jpeg?token=16c6baf4189e1e4a301c25ebb325d7f4",
            "两种有机物必须最简式相同，或者互为同分异构体。",
    });
    private int selectStuAnswerIndex = 0; //主观题（选中哪个学生的答案stusNameList）
    private int selectStuAnswerIndex_img = 0;//主观题（选中哪个学生的答案stusNameList_img）
    private List<String> stusNameList_img = Arrays.asList(new String[]{
            "苏思坚" , "钱宇华" , "王友祖" , "孟艳仁" ,
            "华武" , "薛志" , "钱生利" ,
            "于松石" , "梦闽南" , "王媛",
    });
    private List<String> stusAnswerList_img = Arrays.asList(new String[]{
            "http://www.cn901.com/res/studentAnswerImg/AppImage/2021/12/09/cjzx200101_203003336.png",
            "http://www.cn901.com/res/studentAnswerImg/AppImage/2021/12/09/cjzx200106_172630439.png",
            "http://www.cn901.com/res/studentAnswerImg/AppImage/2021/12/09/cjzx200106_202531675.png",
            "http://www.cn901.com/res/studentAnswerImg/AppImage/2021/12/09/cjzx200106_202636014.png",
            "https://img0.baidu.com/it/u=3485269656,617316610&fm=253&fmt=auto&app=138&f=JPEG?w=660&h=440",
            "https://pics3.baidu.com/feed/cf1b9d16fdfaaf51055aeb850e4a2de7f01f7a73.jpeg?token=13a989ae26c8c26689240bf4a3c776a0",
            "https://pics1.baidu.com/feed/d0c8a786c9177f3eaac47e380fd182ce9f3d563f.jpeg?token=9e9fe01f9182bf35540fcc0aa68083dc",
            "https://pics4.baidu.com/feed/b3b7d0a20cf431adeea8635f342815a62edd9801.jpeg?token=18bf863bdfcae92a41b76078d3c70bce",
            "http://img2.baidu.com/it/u=4157293960,2103840381&fm=253&fmt=auto&app=138&f=JPEG?w=667&h=500",
            "https://pics6.baidu.com/feed/024f78f0f736afc3a536efa7cd0752cdb64512e6.jpeg?token=16c6baf4189e1e4a301c25ebb325d7f4",
    });

    private List<String> stusAnswerList_name1 = Arrays.asList(new String[]{"钱生利"});
    private List<String> stusAnswerList_name2 = Arrays.asList(new String[]{"王花蕾" , "华仁超"});
    private List<List<String>> stusAnswerList_name = new ArrayList<List<String>>();

    private List<String> stusAnswerList_answer = Arrays.asList(new String[]{
            "两种物质以任意质量比混合，如混合物的质量一定，充分燃烧时产生的二氧化碳是定值。",
            "两种有机物必须最简式相同，或者互为同分异构体。"
    });

    private List<String> stusAnswerList_Noanswer = Arrays.asList(new String[]{
            "谢海玉" , "吴妙仁" , "孔国宁" , "陈思" , "马元兴" , "孟丹君"
    });




    public AnswerQuestionFragment() {
        // Required empty public constructor
        listStusName.add(listA);
        listStusName.add(listB);
        listStusName.add(listC);
        listStusName.add(listD);
        listStusName.add(listNo);

        stusAnswerList_name.add(stusAnswerList_name1);
        stusAnswerList_name.add(stusAnswerList_name2);
    }

    //UI组件事件绑定
    private void bindViews() {
        txType_tiwen.setOnClickListener(this);
        txType_suiji.setOnClickListener(this);
        txType_qiangda.setOnClickListener(this);
        txModle_danxuan.setOnClickListener(this);
        txModle_duoxuan.setOnClickListener(this);
        txModle_panduan.setOnClickListener(this);
        txModle_luru.setOnClickListener(this);

        imgdanxuan.setOnClickListener(this);
        imgduoxuan.setOnClickListener(this);
        imgpanduan.setOnClickListener(this);
        imgluru.setOnClickListener(this);
    }

    //重置所有文本的选中状态(互动类型)
    private void setSelected1(){
        txType_tiwen.setSelected(false);
        txType_suiji.setSelected(false);
        txType_qiangda.setSelected(false);
    }
    //重置所有文本的选中状态(互动模式)
    private void setSelected2(){
        txModle_danxuan.setSelected(false);
        txModle_duoxuan.setSelected(false);
        txModle_panduan.setSelected(false);
        txModle_luru.setSelected(false);

        imgdanxuan.setSelected(false);
        imgduoxuan.setSelected(false);
        imgpanduan.setSelected(false);
        imgluru.setSelected(false);
    }

    //互动模式其中一项是否被选中
    private boolean isSelected(){
        if(txModle_danxuan.isSelected()
                || txModle_duoxuan.isSelected()
                || txModle_panduan.isSelected()
                || txModle_luru.isSelected()
        ){
            return true;
        }
        return false;
    }

    //显示答案详情
    private void showStusAnswers(View v){
        LinearLayout linearStusAnswers = v.findViewById(R.id.linearStusAnswers);
        //清空布局
        linearStusAnswers.removeAllViews();
        //选择的答案个数
        int linearSum = listAnswers.size();
        LinearLayout[] answersList = new LinearLayout[linearSum];
        for(int k = 0 ; k < answersList.length ; k++){
            answersList[k] = new LinearLayout(getActivity());
            //设置参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            answersList[k].setLayoutParams(params);
            answersList[k].setOrientation(LinearLayout.HORIZONTAL);
        }
        boolean flag = false; //用来标识当前展示的选项是否是正确答案
        for(int i = 0 ; i < listStusName.size() ; i++){
            if(listAnswers.get(i).equals(answer)){
                flag = true;
            }

            TextView txt_answerTitle = new TextView(getActivity());
            //设置参数
            LinearLayout.LayoutParams tparams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tparams.setMargins(30 , 5 , 0 , 0);
            txt_answerTitle.setLayoutParams(tparams);
            txt_answerTitle.setTextSize(15);
            txt_answerTitle.setTextColor(Color.parseColor("#828798"));

            TextView txt_answer = new TextView(getActivity());
            //设置参数
            LinearLayout.LayoutParams txt_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            txt_params.setMargins(5 , 5 , 0 , 0);
            txt_answer.setLayoutParams(txt_params);
            txt_answer.setTextSize(20);
            if(listAnswers.get(i).equals("未答")){
                txt_answerTitle.setText("未答题");
                answersList[i].addView(txt_answerTitle);
            }else{
                txt_answerTitle.setText("选择: ");
                txt_answer.setTextColor(Color.parseColor("#90d7ec"));
                txt_answer.setText(listAnswers.get(i));
                answersList[i].addView(txt_answerTitle);
                answersList[i].addView(txt_answer);
            }
            linearStusAnswers.addView(answersList[i]);

//            StusNameAdapter stusAdapter = new StusNameAdapter(listStusName.get(i) , this.getActivity());
//            MyListView lv_stuName = new MyListView(getActivity());
//            lv_stuName.setDivider(null); //不显示边框
//            lv_stuName.setAdapter(stusAdapter);

            //            MyLayout linear = new MyLayout(getActivity());
//            linear.setPadding(5 , 0 , 5 , 0);
//            linear.setHorizontalSpace(10);//不设置默认为0
//            linear.setVerticalSpace(10);//不设置默认为0


//            linearStusAnswers.addView(lv_stuName);

            //包裹选择当前选项作为答案的学生姓名
            LinearLayout linearAll = new LinearLayout(getActivity());
            //设置参数
            LinearLayout.LayoutParams linear_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linear_params.setMargins(20 , 0 , 20 , 0 );
            linearAll.setLayoutParams(linear_params);
            linearAll.setOrientation(LinearLayout.VERTICAL);

            int linearCount = (int)Math.ceil(listStusName.get(i).size() / 8.0);
            Log.e("一共有多少学生:   " , listStusName.get(i).size() + "");
            Log.e("一共需要多少个linear:   " , linearCount + "");
            LinearLayout[] linearList = new LinearLayout[linearCount];
            for(int k = 0 ; k < linearList.length ; k++){
                linearList[k] = new LinearLayout(getActivity());
                //设置参数
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                linearList[k].setLayoutParams(params);
                linearList[k].setWeightSum(8);
                linearList[k].setOrientation(LinearLayout.HORIZONTAL);
            }
            for(int j = 0 ; j < listStusName.get(i).size() ; j++){
                TextView txt_name = new TextView(getActivity());
                //设置参数
//                LinearLayout.LayoutParams txtname_params = new LinearLayout.LayoutParams(70, 30);
                LinearLayout.LayoutParams txtname_params = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1);

                //每行展示8个学生姓名
                if(j % 8 == 0){
                    txtname_params.setMargins(10 , 5 , 5 , 5);
                }else{
                    txtname_params.setMargins(5 , 5 , 5 , 5);
                }
                txt_name.setLayoutParams(txtname_params);
                txt_name.setPadding(5 , 2 , 5 , 2);
                txt_name.setTextSize(15);
                txt_name.setGravity(Gravity.CENTER);
                txt_name.setText(listStusName.get(i).get(j));
//                txt_name.setBackgroundColor(Color.parseColor("#007947"));
                if(flag){ //正确答案
                    txt_name.setBackgroundColor(Color.parseColor("#84bf96"));
                    txt_name.setTextColor(Color.parseColor("#007947"));
                }else{
                    txt_name.setBackgroundColor(Color.parseColor("#d3d7d4"));
                    txt_name.setTextColor(Color.parseColor("#828798"));
                }
                linearList[j / 8].addView(txt_name);
            }
            flag = false;
            for(int k = 0 ; k < linearList.length ; k++){
                linearAll.addView(linearList[k]);
            }
            linearStusAnswers.addView(linearAll);
        }
    }

    //判断是否显示页面右侧顶部详细信息
    private void isShowMoreInformation(int flag){
        if(answer != null && answer.length() > 0){ //已经设置了答案 flag=1标识单题分析页面
            if(flag == 1){
                linear_quick.setVisibility(View.VISIBLE);
            }else{
                linear_quick.setVisibility(View.INVISIBLE);
            }
            linear_right.setVisibility(View.VISIBLE);
            linear_answer.setVisibility(View.VISIBLE);

            tx_answer2.setText(answer);
        }else{
            if(flag != 1){
                linear_quick.setVisibility(View.GONE);
                linear_answer.setVisibility(View.GONE);
                linear_right.setVisibility(View.INVISIBLE);
            }
        }
    }

    //显示弹框(客观题：单选、多选、判断)
    private void showPopupWindow(View v , int flag){
        //将popupWindow将要展示的弹窗内容view放入popupWindow中
        PopupWindow popupWindow = new PopupWindow(v ,
                1000,
                650,
                false);
//                popupWindow.setContentView(view);
//                popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//                popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //点击view之外的空白区域或者物理返回按钮也可关闭popupWindow
//                popupWindow.setFocusable(true);

        //弹框展示在屏幕中间Gravity.CENTER,x和y是相对于Gravity.CENTER的偏移
        popupWindow.showAtLocation(v , Gravity.CENTER , 0 , 0);

        //弹框左侧显示“汇总数据”
        ListView lvClass = v.findViewById(R.id.lvClass);
        lvClass.setDivider(null); //不显示边框

        //单题分析 答题详情右侧页面顶部详细信息（最快答对、正确率等）
        tx_quick = v.findViewById(R.id.tx_quick);
        img_top1 = v.findViewById(R.id.img_top1);
        tx_top1 = v.findViewById(R.id.tx_top1);
        img_top2 = v.findViewById(R.id.img_top2);
        tx_top2 = v.findViewById(R.id.tx_top2);
        img_top3 = v.findViewById(R.id.img_top3);
        tx_top3 = v.findViewById(R.id.tx_top3);

        tx_answer1 = v.findViewById(R.id.tx_answer1);
        tx_answer2 = v.findViewById(R.id.tx_answer2);
        tx_right1 = v.findViewById(R.id.tx_right1);
        tx_right2 = v.findViewById(R.id.tx_right2);
        tx_dati1 = v.findViewById(R.id.tx_dati1);
        tx_dati2 = v.findViewById(R.id.tx_dati2);

        linear_right = v.findViewById(R.id.linear_right);
        linear_answer = v.findViewById(R.id.linear_answer);
        linear_quick = v.findViewById(R.id.linear_quick);

        //判断是否显示页面右侧顶部详细信息
        isShowMoreInformation(flag);

        //单题分析 答题详情
        View view1 , view2;   //单题分析 答题详情字体上方的横条
        TextView txt_danti , txt_daan;
        txt_danti = v.findViewById(R.id.txdanti);
        txt_daan = v.findViewById(R.id.txdaan);
        view1 = v.findViewById(R.id.view1);
        view2 = v.findViewById(R.id.view2);

        ScrollView slStusAnswers = v.findViewById(R.id.slStusAnswers);

        //单题分析-柱状图，答题详情-学生答案
        barChart = v.findViewById(R.id.bar_chart);
        barChart.setTouchEnabled(false); // 设置是否可以触摸
        barChart.setDragEnabled(true);// 是否可以拖拽
        barChart.setScaleEnabled(true);// 是否可以缩放
        final String[][] labelName = {
                {"对" , "错" , "未答"},
                {"A", "B", "未答"},
                {"A", "B", "C", "未答"},
                {"A", "B", "C", "D", "未答"},
                {"A", "B", "C", "D", "E", "未答"},
                {"A", "B", "C", "D", "E", "F", "未答"},
                {"A", "B", "C", "D", "E", "F", "G", "未答"},
                {"A", "B", "C", "D", "E", "F", "G", "H", "未答"},
                {"AB", "BCD", "ACD", "ABDE", "ADEFGH", "DFG", "AGH", "ABCDEFGH", "BDE", "AEF", "DF" , "DE" , "ABG" , "DEF" , "未答"}};
        final int[][] labelCount = {
                {39, 5, 8},
                {39, 5, 8},
                {2, 39, 5, 8},
                {2, 39, 5, 8, 3},
                {6, 39, 5, 8, 20, 7},
                {6, 20, 5, 8, 25, 7 , 2},
                {6, 30, 7, 8, 25, 7 , 10 , 2},
                {6, 30, 7, 8, 25, 7 , 10 , 5 , 2},
                {6, 30, 7, 8, 25, 7 , 10 , 5 , 2 , 7 , 11 , 12 , 13 , 14 , 23}};

        //要显示的数据(左侧班级信息)
        List<String> listitem = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String temp = "name" + i;
            listitem.add(temp);
        }

        //创建⼀个Adapter
        MyAdapter myAdapter = new MyAdapter(listitem , this.getActivity() , selectedIndex);
        lvClass.setAdapter(myAdapter);
        lvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                Object result = adapterView.getItemAtPosition(position);//获取选择项的值
                myAdapter.changeSelected(position);
                selectedIndex = position;
                Log.e("当前选中的index是123：  " , position + "");

                if(view1.getVisibility() == 0){  //单题分析，显示柱状图
//                    ((TextView) v.findViewById(R.id.txt_area)).setVisibility(View.GONE);
                    slStusAnswers.setVisibility(View.GONE);
                    barChart.setVisibility(View.GONE);
                    barChart.setVisibility(View.VISIBLE);
                    barChart.getDescription().setEnabled(false); // 不显示描述
                    barChart.setExtraOffsets(20, 20, 20, 20); // 设置饼图的偏移量，类似于内边距 ，设置视图窗口大小

                    setAxis(labelName[position]); // 设置坐标轴
                    setLegend(); // 设置图例
                    setData(labelName[position], labelCount[position]);  // 设置数据
                }

                if(view2.getVisibility() == 0){ //答题详情，显示学生姓名
                    barChart.setVisibility(View.GONE);
                    slStusAnswers.setVisibility(View.VISIBLE);
                    showStusAnswers(v); //显示“答案详情”
//                    ((TextView) v.findViewById(R.id.txt_area)).setVisibility(View.VISIBLE);
//                    ((TextView) v.findViewById(R.id.txt_area)).setText("答题详情-学生答案" + listitem.get(position));
                }
            }
        });

        //初始进入popupwindow
        if(flag == 1){  //"单题分析“
//            ((TextView) v.findViewById(R.id.txt_area)).setVisibility(View.GONE);
            slStusAnswers.setVisibility(View.GONE);
            barChart.setVisibility(View.VISIBLE);
            barChart.getDescription().setEnabled(false); // 不显示描述
            barChart.setExtraOffsets(20, 20, 20, 20); // 设置饼图的偏移量，类似于内边距 ，设置视图窗口大小

            setAxis(labelName[selectedIndex]); // 设置坐标轴
            setLegend(); // 设置图例
            setData(labelName[selectedIndex], labelCount[selectedIndex]);  // 设置数据

            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            txt_danti.setTextColor(Color.parseColor("#007947"));
            txt_daan.setTextColor(Color.parseColor("#FF000000"));
        }else{ //"答题详情"
            barChart.setVisibility(View.GONE);
            slStusAnswers.setVisibility(View.VISIBLE);
//            ((TextView) v.findViewById(R.id.txt_area)).setVisibility(View.VISIBLE);
//            ((TextView) v.findViewById(R.id.txt_area)).setText("答题详情-学生答案" + listitem.get(myAdapter.getmSelect()));
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            txt_daan.setTextColor(Color.parseColor("#007947"));
            txt_danti.setTextColor(Color.parseColor("#FF000000"));

            showStusAnswers(v); //显示“答案详情”
        }


        txt_danti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedIndex = myAdapter.getmSelect();
                slStusAnswers.setVisibility(View.GONE);
//                ((TextView) v.findViewById(R.id.txt_area)).setVisibility(View.GONE);
                barChart.setVisibility(View.VISIBLE);
                barChart.getDescription().setEnabled(false); // 不显示描述
                barChart.setExtraOffsets(20, 20, 20, 20); // 设置饼图的偏移量，类似于内边距 ，设置视图窗口大小
                setAxis(labelName[selectedIndex]); // 设置坐标轴
                setLegend(); // 设置图例
                setData(labelName[selectedIndex], labelCount[selectedIndex]);  // 设置数据

                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                txt_danti.setTextColor(Color.parseColor("#007947"));
                txt_daan.setTextColor(Color.parseColor("#FF000000"));

                isShowMoreInformation(1);
            }
        });

        txt_daan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                barChart.setVisibility(View.GONE);
                slStusAnswers.setVisibility(View.VISIBLE);
//                ((TextView) v.findViewById(R.id.txt_area)).setVisibility(View.VISIBLE);
//                ((TextView) v.findViewById(R.id.txt_area)).setText("答题详情-学生答案" + listitem.get(myAdapter.getmSelect()));
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                txt_daan.setTextColor(Color.parseColor("#007947"));
                txt_danti.setTextColor(Color.parseColor("#FF000000"));

                showStusAnswers(v); //显示“答案详情”

                isShowMoreInformation(2);
            }
        });

        //设置答案按钮
        ImageView img_szda = v.findViewById(R.id.imgSet);
        //公布结果按钮
        ImageView img_gbjg = v.findViewById(R.id.imgPush);
        //刷新按钮
        ImageView img_flash = v.findViewById(R.id.imgFlash);
        //退出按钮
        ImageView imgexit = v.findViewById(R.id.imgExit);

        img_szda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                LinearLayout linear1 = v.findViewById(R.id.linear1);
//                LinearLayout linear2 = v.findViewById(R.id.linear2);
//                LinearLayout linear3 = v.findViewById(R.id.linear3);
//                View ve = v.findViewById(R.id.view);
//
//                linear1.getBackground().setAlpha(10);
//                linear2.getBackground().setAlpha(10);
//                linear3.getBackground().setAlpha(10);
//                ve.getBackground().setAlpha(10);


//                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
//                lp.alpha = 0.2f; // 0.0~1.0
//                getActivity().getWindow().setAttributes(lp); //getActivity() 是上下文context

                View v_self = LayoutInflater.from(getActivity()).inflate(R.layout.single_question_answers, null, false);
                setAnswers(v_self , labelName[myAdapter.getmSelect()] , v , popupWindow , flag);
            }
        });

        img_gbjg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                //将popupwindow移动到左上角
                popupWindow.showAtLocation(v , Gravity.TOP | Gravity.LEFT , 0 , 0);
                //不显示公布结果、刷新按钮，设置答案以及退出按钮向右移动
                img_szda.setPadding(200 , 0 , 0 , 0);
                img_gbjg.setVisibility(View.GONE);
                img_flash.setVisibility(View.GONE);
            }
        });

        img_flash.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                Log.e("点击了刷新按钮", view.getId() + "");
                int position = myAdapter.getmSelect();
                if(view1.getVisibility() == 0){  //单题分析，显示柱状图
                    slStusAnswers.setVisibility(View.GONE);
//                    ((TextView) v.findViewById(R.id.txt_area)).setVisibility(View.GONE);
                    barChart.setVisibility(View.GONE);
                    barChart.setVisibility(View.VISIBLE);
                    barChart.getDescription().setEnabled(false); // 不显示描述
                    barChart.setExtraOffsets(20, 20, 20, 20); // 设置饼图的偏移量，类似于内边距 ，设置视图窗口大小

                    setAxis(labelName[position]); // 设置坐标轴
                    setLegend(); // 设置图例
                    setData(labelName[position], labelCount[position]);  // 设置数据
                }

                if(view2.getVisibility() == 0){ //答题详情，显示学生姓名
                    barChart.setVisibility(View.GONE);
                    slStusAnswers.setVisibility(View.VISIBLE);
                    showStusAnswers(v); //显示“答案详情”
//                    ((TextView) v.findViewById(R.id.txt_area)).setVisibility(View.VISIBLE);
//                    ((TextView) v.findViewById(R.id.txt_area)).setText("答题详情-学生答案" + listitem.get(position));
                }
            }
        });

        imgexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                selectedIndex = 0;
                answer = "";
            }
        });
    }

    // 设置坐标轴
    private void setAxis(String[] labelName) {
        // 设置x轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setYOffset(10); // 设置标签对x轴的偏移量，垂直方向
        // 设置y轴，y轴有两条，分别为左和右
        if(txModle_duoxuan.isSelected()){
            //设置X轴文字顺时针旋转角度，逆时针取负值
            barChart.getXAxis().setLabelRotationAngle(-50);
            xAxis.setYOffset(5); // 设置标签对x轴的偏移量，垂直方向
        }
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);  // 设置x轴显示在下方，默认在上方

        xAxis.setDrawGridLines(false); // 将此设置为true，绘制该轴的网格线。
        xAxis.setLabelCount(labelName.length);  // 设置x轴上的标签个数
        xAxis.setTextSize(15f); // x轴上标签的大小
        xAxis.setAxisLineColor(Color.parseColor("#426ab3"));
        // 设置x轴显示的值的格式
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if ((int) value < labelName.length) {
                    return labelName[(int) value];
                } else {
                    return "";
                }
            }
        });

        YAxis yAxis_right = barChart.getAxisRight();
        yAxis_right.setAxisMaximum(40f);  // 设置y轴的最大值
        yAxis_right.setAxisMinimum(0f);  // 设置y轴的最小值
        yAxis_right.setEnabled(false);  // 不显示右边的y轴

        YAxis yAxis_left = barChart.getAxisLeft();
        yAxis_left.setAxisMaximum(40f);
        yAxis_left.setAxisMinimum(0f);
        yAxis_left.setTextSize(15f); // 设置y轴的标签大小
        yAxis_left.setAxisLineColor(Color.parseColor("#426ab3"));
        yAxis_left.setGridColor(Color.parseColor("#426ab3"));
//        yAxis_left.setGridLineWidth(1f);
    }

    // 设置图例
    private void setLegend() {
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);  //不显示图例
//        legend.setFormSize(12f); // 图例的图形大小
//        legend.setTextSize(15f); // 图例的文字大小
//        legend.setDrawInside(true); // 设置图例在图中
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL); // 图例的方向为垂直
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT); //显示位置，水平右对齐
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP); // 显示位置，垂直上对齐
//        // 设置水平与垂直方向的偏移量
//        legend.setYOffset(55f);
//        legend.setXOffset(30f);
    }

    //设置数据
    private void setData(String[] labelName , int[] labelCount) {
        List<IBarDataSet> sets = new ArrayList<>();
        // 此处有两个DataSet，所以有两条柱子，BarEntry（）中的x和y分别表示显示的位置和高度
        // x是横坐标，表示位置，y是纵坐标，表示高度
        List<BarEntry> barEntries1 = new ArrayList<>();
        for(int i = 0 ; i < labelCount.length ; i++){
            barEntries1.add(new BarEntry(i, labelCount[i]));
        }
//        barEntries1.add(new BarEntry(0, 2f));
//        barEntries1.add(new BarEntry(1, 39f));
//        barEntries1.add(new BarEntry(2, 5f));
//        barEntries1.add(new BarEntry(3, 8f));
//        barEntries1.add(new BarEntry(4, 3f));
        BarDataSet barDataSet1 = new BarDataSet(barEntries1, "");
        barDataSet1.setValueTextColor(Color.BLACK); // 值的颜色
        barDataSet1.setValueTextSize(15f); // 值的大小
//        barDataSet1.setColor(Color.parseColor("#1AE61A")); // 柱子的颜色
        List<Integer> colors = new ArrayList<>();
        for(int i = 0 ; i < labelName.length ; i++){
            if(labelName[i].equals(answer)){
                colors.add(Color.parseColor("#FF6100"));
            }else if(labelName[i] == "未答"){
                colors.add(Color.parseColor("#828798"));
            }else{
                colors.add(Color.parseColor("#4e72b8"));
            }
        }
        barDataSet1.setColors(colors);
//        barDataSet1.setColors(Color.parseColor("#FF6100"),
//                Color.parseColor("#485060"),
//                Color.parseColor("#FFC800"),
//                Color.parseColor("#694E42"),
//                Color.parseColor("#485060"));
//        barDataSet1.setLabel("蔬菜"); // 设置标签之后，图例的内容默认会以设置的标签显示
        // 设置柱子上数据显示的格式
        barDataSet1.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                // 此处的value默认保存一位小数
                return (int)value + "人";
            }
        });
        sets.add(barDataSet1);

        BarData barData = new BarData(sets);
        barData.setBarWidth(0.6f); // 设置柱子的宽度
        barChart.setData(barData);
    }

    //设置答案popupwindow
    public void setAnswers(View v , String[] labelName , View v_parent , PopupWindow p_parent , int flag){
        //将popupWindow将要展示的弹窗内容view放入popupWindow中
        PopupWindow popupWindow_szda = new PopupWindow(v ,
                450,
                300,
                false);

        //弹框展示在屏幕中间Gravity.CENTER,x和y是相对于Gravity.CENTER的偏移
        popupWindow_szda.showAtLocation(v , Gravity.CENTER , 0 , 0);

        //关闭popupwindow
        ImageView closeImg = v.findViewById(R.id.imgClose);
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer = "";
                popupWindow_szda.dismiss();
            }
        });

        //单选UI
        linear_danxuan = v.findViewById(R.id.linearImg1);
        a = v.findViewById(R.id.a);
        b = v.findViewById(R.id.b);
        c = v.findViewById(R.id.c);
        d = v.findViewById(R.id.d);
        e = v.findViewById(R.id.e);
        f = v.findViewById(R.id.f);
        g = v.findViewById(R.id.g);
        h = v.findViewById(R.id.h);
        //多选UI
        linear_duoxuan = v.findViewById(R.id.linearImg2);
        a1 = v.findViewById(R.id.a1);
        b1 = v.findViewById(R.id.b1);
        c1 = v.findViewById(R.id.c1);
        d1 = v.findViewById(R.id.d1);
        e1 = v.findViewById(R.id.e1);
        f1 = v.findViewById(R.id.f1);
        g1 = v.findViewById(R.id.g1);
        h1 = v.findViewById(R.id.h1);
        //判断UI
        linear_panduan = v.findViewById(R.id.linearImg3);
        right = v.findViewById(R.id.right);
        error = v.findViewById(R.id.error);

        int count = 0; //选项个数
        for(int i = 0 ; i < labelName.length ; i++){
            if(labelName[i] != "未答"){
                count++;
            }
        }
        //2-4个选项
        LinearLayout.LayoutParams lps1 = new LinearLayout.LayoutParams(90, 90);
        lps1.setMargins(3 , 3 , 3 , 3);
        //5个选项
        LinearLayout.LayoutParams lps2 = new LinearLayout.LayoutParams(70, 70);
        lps2.setMargins(3 , 3 , 3 , 3);
        //6个选项
        LinearLayout.LayoutParams lps3 = new LinearLayout.LayoutParams(65, 65);
        lps3.setMargins(3 , 3 , 3 , 3);
        //7个选项
        LinearLayout.LayoutParams lps4 = new LinearLayout.LayoutParams(55, 55);
        lps4.setMargins(3 , 3 , 3 , 3);
        //8个选项
        LinearLayout.LayoutParams lps5 = new LinearLayout.LayoutParams(49, 49);
        lps5.setMargins(3 , 3 , 3 , 3);
        //单选(图标）
        if(txModle_danxuan.isSelected()){
            linear_danxuan.setVisibility(View.VISIBLE);
            linear_duoxuan.setVisibility(View.GONE);
            linear_panduan.setVisibility(View.GONE);

            if(count == 2){
                a.setLayoutParams(lps1);
                b.setLayoutParams(lps1);
                c.setVisibility(View.GONE);
                d.setVisibility(View.GONE);
                e.setVisibility(View.GONE);
                f.setVisibility(View.GONE);
                g.setVisibility(View.GONE);
                h.setVisibility(View.GONE);
            }else if(count == 3){
                a.setLayoutParams(lps1);
                b.setLayoutParams(lps1);
                c.setLayoutParams(lps1);
                d.setVisibility(View.GONE);
                e.setVisibility(View.GONE);
                f.setVisibility(View.GONE);
                g.setVisibility(View.GONE);
                h.setVisibility(View.GONE);
            }else if(count == 4){
                a.setLayoutParams(lps1);
                b.setLayoutParams(lps1);
                c.setLayoutParams(lps1);
                d.setLayoutParams(lps1);
                e.setVisibility(View.GONE);
                f.setVisibility(View.GONE);
                g.setVisibility(View.GONE);
                h.setVisibility(View.GONE);
            }else if(count == 5){
                a.setLayoutParams(lps2);
                b.setLayoutParams(lps2);
                c.setLayoutParams(lps2);
                d.setLayoutParams(lps2);
                e.setLayoutParams(lps2);
                f.setVisibility(View.GONE);
                g.setVisibility(View.GONE);
                h.setVisibility(View.GONE);
            }else if(count == 6){
                a.setLayoutParams(lps3);
                b.setLayoutParams(lps3);
                c.setLayoutParams(lps3);
                d.setLayoutParams(lps3);
                e.setLayoutParams(lps3);
                f.setLayoutParams(lps3);
                g.setVisibility(View.GONE);
                h.setVisibility(View.GONE);
            }else if(count == 7){
                a.setLayoutParams(lps4);
                b.setLayoutParams(lps4);
                c.setLayoutParams(lps4);
                d.setLayoutParams(lps4);
                e.setLayoutParams(lps4);
                f.setLayoutParams(lps4);
                g.setLayoutParams(lps4);
                h.setVisibility(View.GONE);
            }else if(count == 8){
                a.setLayoutParams(lps5);
                b.setLayoutParams(lps5);
                c.setLayoutParams(lps5);
                d.setLayoutParams(lps5);
                e.setLayoutParams(lps5);
                f.setLayoutParams(lps5);
                g.setLayoutParams(lps5);
                h.setLayoutParams(lps5);
            }
        }
        //多选(图标）
        if(txModle_duoxuan.isSelected()){
            linear_danxuan.setVisibility(View.GONE);
            linear_panduan.setVisibility(View.GONE);
            linear_duoxuan.setVisibility(View.VISIBLE);
            if(count == 2){
                a1.setLayoutParams(lps1);
                b1.setLayoutParams(lps1);
                c1.setVisibility(View.GONE);
                d1.setVisibility(View.GONE);
                e1.setVisibility(View.GONE);
                f1.setVisibility(View.GONE);
                g1.setVisibility(View.GONE);
                h1.setVisibility(View.GONE);
            }else if(count == 3){
                a1.setLayoutParams(lps1);
                b1.setLayoutParams(lps1);
                c1.setLayoutParams(lps1);
                d1.setVisibility(View.GONE);
                e1.setVisibility(View.GONE);
                f1.setVisibility(View.GONE);
                g1.setVisibility(View.GONE);
                h1.setVisibility(View.GONE);
            }else if(count == 4){
                a1.setLayoutParams(lps1);
                b1.setLayoutParams(lps1);
                c1.setLayoutParams(lps1);
                d1.setLayoutParams(lps1);
                e1.setVisibility(View.GONE);
                f1.setVisibility(View.GONE);
                g1.setVisibility(View.GONE);
                h1.setVisibility(View.GONE);
            }else if(count == 5){
                a1.setLayoutParams(lps2);
                b1.setLayoutParams(lps2);
                c1.setLayoutParams(lps2);
                d1.setLayoutParams(lps2);
                e1.setLayoutParams(lps2);
                f1.setVisibility(View.GONE);
                g1.setVisibility(View.GONE);
                h1.setVisibility(View.GONE);
            }else if(count == 6){
                a1.setLayoutParams(lps3);
                b1.setLayoutParams(lps3);
                c1.setLayoutParams(lps3);
                d1.setLayoutParams(lps3);
                e1.setLayoutParams(lps3);
                f1.setLayoutParams(lps3);
                g1.setVisibility(View.GONE);
                h1.setVisibility(View.GONE);
            }else if(count == 7){
                a1.setLayoutParams(lps4);
                b1.setLayoutParams(lps4);
                c1.setLayoutParams(lps4);
                d1.setLayoutParams(lps4);
                e1.setLayoutParams(lps4);
                f1.setLayoutParams(lps4);
                g1.setLayoutParams(lps4);
                h1.setVisibility(View.GONE);
            }else if(count == 8){
                a1.setLayoutParams(lps5);
                b1.setLayoutParams(lps5);
                c1.setLayoutParams(lps5);
                d1.setLayoutParams(lps5);
                e1.setLayoutParams(lps5);
                f1.setLayoutParams(lps5);
                g1.setLayoutParams(lps5);
                h1.setLayoutParams(lps5);
            }
        }
        //判断(图标）
        if(txModle_panduan.isSelected()){
            linear_danxuan.setVisibility(View.GONE);
            linear_duoxuan.setVisibility(View.GONE);
            linear_panduan.setVisibility(View.VISIBLE);
        }

        //点击事件（单选）
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDanxuanImgStatus();
                a.setSelected(true);
                a.setImageDrawable(getResources().getDrawable((R.mipmap.a_select)));
            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDanxuanImgStatus();
                b.setSelected(true);
                b.setImageDrawable(getResources().getDrawable((R.mipmap.b_select)));
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDanxuanImgStatus();
                c.setSelected(true);
                c.setImageDrawable(getResources().getDrawable((R.mipmap.c_select)));
            }
        });
        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDanxuanImgStatus();
                d.setSelected(true);
                d.setImageDrawable(getResources().getDrawable((R.mipmap.d_select)));
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDanxuanImgStatus();
                e.setSelected(true);
                e.setImageDrawable(getResources().getDrawable((R.mipmap.e_select)));
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDanxuanImgStatus();
                f.setSelected(true);
                f.setImageDrawable(getResources().getDrawable((R.mipmap.f_select)));
            }
        });
        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDanxuanImgStatus();
                g.setSelected(true);
                g.setImageDrawable(getResources().getDrawable((R.mipmap.g_select)));
            }
        });
        h.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDanxuanImgStatus();
                h.setSelected(true);
                h.setImageDrawable(getResources().getDrawable((R.mipmap.h_select)));
            }
        });
        //点击事件（多选）
        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判断当前imageView是否为某一图片（可用来判断imageview是否被选中）
                if((a1.getDrawable().getCurrent().getConstantState()).equals(
                        ContextCompat.getDrawable(getActivity(),R.mipmap.ad_select).getConstantState())
                ) {
                    a1.setSelected(false);
                    a1.setImageDrawable(getResources().getDrawable((R.mipmap.ad)));
                }else {
                    a1.setSelected(true);
                    a1.setImageDrawable(getResources().getDrawable((R.mipmap.ad_select)));
                }

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((b1.getDrawable().getCurrent().getConstantState()).equals(ContextCompat.getDrawable(getActivity(),R.mipmap.bd_select).getConstantState())){
                    b1.setSelected(false);
                    b1.setImageDrawable(getResources().getDrawable((R.mipmap.bd)));
                }else{
                    b1.setSelected(true);
                    b1.setImageDrawable(getResources().getDrawable((R.mipmap.bd_select)));
                }
            }
        });
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((c1.getDrawable().getCurrent().getConstantState()).equals(ContextCompat.getDrawable(getActivity(),R.mipmap.cd_select).getConstantState())){
                    c1.setSelected(false);
                    c1.setImageDrawable(getResources().getDrawable((R.mipmap.cd)));
                }else{
                    c1.setSelected(true);
                    c1.setImageDrawable(getResources().getDrawable((R.mipmap.cd_select)));
                }
            }
        });
        d1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((d1.getDrawable().getCurrent().getConstantState()).equals(ContextCompat.getDrawable(getActivity(),R.mipmap.dd_select).getConstantState())){
                    a1.setSelected(false);
                    d1.setImageDrawable(getResources().getDrawable((R.mipmap.dd)));
                }else{
                    d1.setSelected(true);
                    d1.setImageDrawable(getResources().getDrawable((R.mipmap.dd_select)));
                }
            }
        });
        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((e1.getDrawable().getCurrent().getConstantState()).equals(ContextCompat.getDrawable(getActivity(),R.mipmap.ed_select).getConstantState())){
                    a1.setSelected(false);
                    e1.setImageDrawable(getResources().getDrawable((R.mipmap.ed)));
                }else{
                    e1.setSelected(true);
                    e1.setImageDrawable(getResources().getDrawable((R.mipmap.ed_select)));
                }
            }
        });
        f1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((f1.getDrawable().getCurrent().getConstantState()).equals(ContextCompat.getDrawable(getActivity(),R.mipmap.fd_select).getConstantState())){
                    a1.setSelected(false);
                    f1.setImageDrawable(getResources().getDrawable((R.mipmap.fd)));
                }else{
                    f1.setSelected(true);
                    f1.setImageDrawable(getResources().getDrawable((R.mipmap.fd_select)));
                }
            }
        });
        g1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((g1.getDrawable().getCurrent().getConstantState()).equals(ContextCompat.getDrawable(getActivity(),R.mipmap.gd_select).getConstantState())){
                    a1.setSelected(false);
                    g1.setImageDrawable(getResources().getDrawable((R.mipmap.gd)));
                }else{
                    g1.setSelected(true);
                    g1.setImageDrawable(getResources().getDrawable((R.mipmap.gd_select)));
                }
            }
        });
        h1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((h1.getDrawable().getCurrent().getConstantState()).equals(ContextCompat.getDrawable(getActivity(),R.mipmap.hd_select).getConstantState())){
                    h1.setSelected(false);
                    h1.setImageDrawable(getResources().getDrawable((R.mipmap.hd)));
                }else{
                    h1.setSelected(true);
                    h1.setImageDrawable(getResources().getDrawable((R.mipmap.hd_select)));
                }
            }
        });
        //点击事件（判断）
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right.setSelected(true);
                error.setSelected(false);
                right.setImageDrawable(getResources().getDrawable((R.mipmap.right_select)));
                error.setImageDrawable(getResources().getDrawable((R.mipmap.error)));
            }
        });
        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                right.setSelected(false);
                error.setSelected(true);
                right.setImageDrawable(getResources().getDrawable((R.mipmap.right)));
                error.setImageDrawable(getResources().getDrawable((R.mipmap.error_select)));
            }
        });

        Button bt_save = v.findViewById(R.id.btSave);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txModle_danxuan.isSelected()){
                    answer = "";
                    if(a.isSelected()){
                        answer = "A";
                    }else if(b.isSelected()){
                        answer = "B";
                    }else if(c.isSelected()){
                        answer = "C";
                    }else if(d.isSelected()){
                        answer = "D";
                    }else if(e.isSelected()){
                        answer = "E";
                    }else if(f.isSelected()){
                        answer = "F";
                    }else if(g.isSelected()){
                        answer = "G";
                    }else if(h.isSelected()){
                        answer = "H";
                    }else{
                        answer = "";
                    }
                    Toast.makeText(getActivity(),"单选答案是：" + answer,Toast.LENGTH_SHORT).show();
                }else if(txModle_duoxuan.isSelected()){
                    answer = "";
                    if(a1.isSelected()){ answer = answer + "A"; }
                    if(b1.isSelected()){ answer = answer + "B"; }
                    if(c1.isSelected()){ answer = answer + "C"; }
                    if(d1.isSelected()){ answer = answer + "D"; }
                    if(e1.isSelected()){ answer = answer + "E"; }
                    if(f1.isSelected()){ answer = answer + "F"; }
                    if(g1.isSelected()){ answer = answer + "G"; }
                    if(h1.isSelected()){ answer = answer + "H"; }
                    Toast.makeText(getActivity(),"多选答案是：" + answer,Toast.LENGTH_SHORT).show();
                }else if(txModle_panduan.isSelected()){
                    answer = "";
                    if(right.isSelected()){
                        answer = "对";
                    }else if(error.isSelected()){
                        answer = "错";
                    }else{
                        answer = "";
                    }
                    Toast.makeText(getActivity(),"判断答案是：" + answer,Toast.LENGTH_SHORT).show();
                }
                popupWindow_szda.dismiss(); //关掉“设置答案”页面的popupwindow
                p_parent.dismiss(); //关掉“单题分析”页面的popupwindow
                //再次打开“单题分析”页面的popupwindow
                showPopupWindow(v_parent , flag);
            }
        });
    }

    //单选图标状态
    public void setDanxuanImgStatus(){
        a.setImageDrawable(getResources().getDrawable((R.mipmap.a)));
        b.setImageDrawable(getResources().getDrawable((R.mipmap.b)));
        c.setImageDrawable(getResources().getDrawable((R.mipmap.c)));
        d.setImageDrawable(getResources().getDrawable((R.mipmap.d)));
        e.setImageDrawable(getResources().getDrawable((R.mipmap.e)));
        f.setImageDrawable(getResources().getDrawable((R.mipmap.f)));
        g.setImageDrawable(getResources().getDrawable((R.mipmap.g)));
        h.setImageDrawable(getResources().getDrawable((R.mipmap.h)));
        a.setSelected(false);
        b.setSelected(false);
        c.setSelected(false);
        d.setSelected(false);
        e.setSelected(false);
        f.setSelected(false);
        g.setSelected(false);
        h.setSelected(false);
    }


    //显示弹框(客观题：单选、多选、判断)
    private void showPopupWindow_luru(View v , int flag){
        //将popupWindow将要展示的弹窗内容view放入popupWindow中
        PopupWindow popupWindow = new PopupWindow(v ,
                1000,
                650,
                false);

        //弹框展示在屏幕中间Gravity.CENTER,x和y是相对于Gravity.CENTER的偏移
        popupWindow.showAtLocation(v , Gravity.CENTER , 0 , 0);

        //答案内容 答题详情
        View view1 , view2;   //答案内容 答题详情字体上方的横条
        TextView txt_daan , txt_dati;
        txt_daan = v.findViewById(R.id.txdaan);
        txt_dati = v.findViewById(R.id.txdati);
        view1 = v.findViewById(R.id.view1);
        view2 = v.findViewById(R.id.view2);


        //弹框左侧显示“汇总数据”
        ListView lvClass = v.findViewById(R.id.lvClass);
        lvClass.setDivider(null); //不显示边框

        //要显示的数据(左侧班级信息)
        List<String> listitem = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            String temp = "name" + i;
            listitem.add(temp);
        }

        //创建⼀个Adapter
        MyAdapter myAdapter = new MyAdapter(listitem , this.getActivity() , selectedIndex);
        lvClass.setAdapter(myAdapter);
        lvClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                myAdapter.changeSelected(position);
                selectedIndex = position;
                Log.e("当前选中的index是123：  " , position + "");

                if(view1.getVisibility() == 0){  //答案内容，显示柱图片、文字

                }

                if(view2.getVisibility() == 0){ //答题详情，显示学生姓名

                }
            }
        });

        //初始进入popupwindow
        if(flag == 1){  //"答案内容“
            view1.setVisibility(View.VISIBLE);
            view2.setVisibility(View.GONE);
            txt_daan.setTextColor(Color.parseColor("#007947"));
            txt_dati.setTextColor(Color.parseColor("#FF000000"));
            showStudentsAnswer_img(v);
        }else{ //"答题详情"
            view1.setVisibility(View.GONE);
            view2.setVisibility(View.VISIBLE);
            txt_daan.setTextColor(Color.parseColor("#FF000000"));
            txt_dati.setTextColor(Color.parseColor("#007947"));
            showStudentsAnswer(v);
        }

        txt_daan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                txt_daan.setTextColor(Color.parseColor("#007947"));
                txt_dati.setTextColor(Color.parseColor("#FF000000"));
                showStudentsAnswer_img(v);
            }
        });

        txt_dati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.VISIBLE);
                txt_daan.setTextColor(Color.parseColor("#FF000000"));
                txt_dati.setTextColor(Color.parseColor("#007947"));
                showStudentsAnswer(v);
            }
        });

        //公布结果按钮
        ImageView img_gbjg = v.findViewById(R.id.imgPush);
        //刷新按钮
        ImageView img_flash = v.findViewById(R.id.imgFlash);
        //退出按钮
        ImageView img_exit = v.findViewById(R.id.imgExit);


        img_gbjg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                //将popupwindow移动到左上角
                popupWindow.showAtLocation(v , Gravity.TOP | Gravity.LEFT , 0 , 0);
                //不显示公布结果、刷新按钮，设置答案,退出按钮向右移动
                img_gbjg.setVisibility(View.INVISIBLE);
                img_flash.setVisibility(View.INVISIBLE);
            }
        });

        img_flash.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View view) {
                Log.e("点击了刷新按钮", view.getId() + "");
                int position = myAdapter.getmSelect();
                if(view1.getVisibility() == 0){  //答案内容，显示图片、文字

                }

                if(view2.getVisibility() == 0){ //答题详情，显示学生姓名

                }
            }
        });

        img_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                selectedIndex = 0;
            }
        });
    }

    //主观题-显示学生答案内容
    private void showStudentsAnswer_img(View v){
        ScrollView slStusAnswersImg = v.findViewById(R.id.slStusAnswersImg);
        ScrollView slStusAnswers = v.findViewById(R.id.slStusAnswers);
        slStusAnswersImg.setVisibility(View.VISIBLE);
        slStusAnswers.setVisibility(View.GONE);

        //动态在这个布局中添加控件
        LinearLayout linearStusAnswersImg = v.findViewById(R.id.linearStusAnswersImg);
        linearStusAnswersImg.removeAllViews();

        int studentsNum = stusNameList.size(); //答题的学生人数
        //每行展示3个学生答案，linearsNum表示需要几个线性布局来包裹学生答案
        int linearsNum = (int)Math.ceil(studentsNum / 3.0);
        LinearLayout[] answersList = new LinearLayout[linearsNum];
        //初始化所有LinearLayout，每个都3等份
        for(int i = 0 ; i < answersList.length ; i++){
            answersList[i] = new LinearLayout(getActivity());
            //设置参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            answersList[i].setWeightSum(3);  //3等份
            answersList[i].setLayoutParams(params);
            answersList[i].setOrientation(LinearLayout.HORIZONTAL);
        }

        for(int i = 0 ; i < studentsNum ; i++){
            LinearLayout linearStu = new LinearLayout(getActivity());
            LinearLayout.LayoutParams linear_params = new LinearLayout.LayoutParams(0, 180, 1);
            //每行展示3个学生答案
            if(i % 3 == 0){
                linear_params.setMargins(10 , 5 , 5 , 5);
            }else{
                linear_params.setMargins(5 , 5 , 5 , 5);
            }
            linearStu.setOrientation(LinearLayout.VERTICAL);
            linearStu.setLayoutParams(linear_params);
            linearStu.setBackground(getResources().getDrawable(R.drawable.linear_shape));

            TextView tx_name = new TextView(getActivity());
            LinearLayout.LayoutParams tx_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            tx_name.setLayoutParams(tx_params);
            tx_name.setPadding(0 , 2 , 0 , 2);
            tx_name.setGravity(Gravity.CENTER);
            tx_name.setText(stusNameList.get(i));
            tx_name.setTextColor(Color.parseColor("#FFFFFF"));

            linearStu.addView(tx_name);

            if(stusAnswerList.get(i).substring(0 , 4).equals("http")){ //学生答案是图片
                Log.e("当前学生的答案图片地址是" , stusAnswerList.get(i));
                Log.e("当前学生的答案是" , stusAnswerList.get(i).substring(0 , 4));
                ImageView imgStu = new ImageView(getActivity());
//                SwZoomDragImageView imgStu = new SwZoomDragImageView(getActivity());
                imgStu.setId(i);
                LinearLayout.LayoutParams img_params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1);
                img_params.setMargins(2 , 0 , 2 , 2);
                imgStu.setLayoutParams(img_params);
//                imgStu.setBackgroundColor(Color.parseColor("#45b97c"));
                imgStu.setPadding(2 , 2 , 2 , 2);
                imgStu.setBackground(getResources().getDrawable(R.drawable.txt_shape));

                String imgUrl = stusAnswerList.get(i).toString();
//                if(imgUrl.startsWith("http://")){
//                    imgUrl = imgUrl.replace("http://" , "https://");
//                }


                Drawable error_img = getResources().getDrawable(R.mipmap.error_img);
//                try {
//                    Bitmap bitmap = null;
//                    bitmap = BitmapFactory.decodeStream(new URL(imgUrl).openStream());
//                    imgStu.setImageBitmap(bitmap);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

                try{
                    Glide.with(getActivity())
                            .load(imgUrl)
                            .error(error_img)
                            .fitCenter()
                            .into(imgStu);
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Log.e("图片加载抛出异常:  ", exception.getMessage());
                }


                imgStu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        slStusAnswersImg.setVisibility(View.INVISIBLE);
                        selectStuAnswerIndex = view.getId();
                        showImageAnswer(v);
                    }
                });
                linearStu.addView(imgStu);
            }else{ //学生答案是文字
//                LinearLayout linearAnswer = new LinearLayout(getActivity());
//                LinearLayout.LayoutParams answer_params = new LinearLayout.LayoutParams(
//                        LinearLayout.LayoutParams.MATCH_PARENT,
//                        0,
//                        1);
//                answer_params.setMargins(2 , 0 , 2 , 2);
//                linearAnswer.setLayoutParams(answer_params);
//                linearAnswer.setOrientation(LinearLayout.VERTICAL);
//                linearAnswer.setBackground(getResources().getDrawable(R.drawable.txt_shape));

//                ScrollView scAnswer = new ScrollView(getActivity());
                //自定义ScrollView，解决了ScrollView嵌套导致的滑动冲突问题
                MyScrollView scAnswer = new MyScrollView(getActivity());
                LinearLayout.LayoutParams sc_params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        0,
                        1);
                sc_params.setMargins(2 , 0 , 2 , 2);
                scAnswer.setLayoutParams(sc_params);
                scAnswer.setBackground(getResources().getDrawable(R.drawable.txt_shape));

                TextView tx_answer = new TextView(getActivity());
                LinearLayout.LayoutParams txt_params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
//                txt_params.setMargins(2 , 0 , 2 , 2);
                tx_answer.setLayoutParams(txt_params);
                tx_answer.setPadding(5 , 5 , 5 , 0);
//                tx_answer.setBackground(getResources().getDrawable(R.drawable.txt_shape));
                tx_answer.setText(stusAnswerList.get(i));
//                tx_answer.setTextColor(Color.parseColor("#007947"));
//                linearAnswer.addView(tx_answer);
//                linearStu.addView(linearAnswer);
//                linearStu.addView(tx_answer);
                scAnswer.addView(tx_answer);
                linearStu.addView(scAnswer);
            }
//            imgStu.setScaleType(ImageView.ScaleType.CENTER);
//            imgStu.setAdjustViewBounds(true);
//            imgStu.setMaxHeight(200);
//            imgStu.setMaxWidth(200);
            answersList[i / 3].addView(linearStu);
        }
        for(int i = 0 ; i < answersList.length ; i++){
            linearStusAnswersImg.addView(answersList[i]);
        }
    }


    //显示主观题答案-图片
    private void showImageAnswer(View v){
        LinearLayout linear1 = v.findViewById(R.id.linear1);
        LinearLayout linear_imgAndtxt = v.findViewById(R.id.linear_imgAndtxt);
        LinearLayout linear_img = v.findViewById(R.id.linear_img);
        //清空布局
        linear_img.removeAllViews();

        ImageView img_back = v.findViewById(R.id.img_back);
        ImageView img_next = v.findViewById(R.id.img_next);
        TextView tx_who = v.findViewById(R.id.tx_who);
        TextView tx_num = v.findViewById(R.id.tx_num);
        TextView tx_close = v.findViewById(R.id.tx_close);

        linear1.setVisibility(View.GONE);
        linear_imgAndtxt.setVisibility(View.VISIBLE);

        Log.e("当前选中的学生id是: ", selectStuAnswerIndex + "");
        //在stusAnswerList_img中查找stusAnswerList.get(selectStuAnswerIndex)对应的index
        int index = isHave(stusNameList.get(selectStuAnswerIndex));
        if(index >= 0){
            Log.e("当前选中的学生indxe是: ", index + "");
            Log.e("当前选中的学生姓名是: ", stusNameList_img.get(index));
            Log.e("当前选中的学生答案url是: ", stusAnswerList_img.get(index));
            int allNum = stusNameList_img.size(); //图片答案-总人数
            String tx = (index + 1) + "/" + allNum;
            tx_num.setText(tx);
            tx_who.setText(stusNameList_img.get(index)); //学生姓名

            Drawable error_img = getResources().getDrawable(R.mipmap.error_img);
            SwZoomDragImageView imgAnswer = new SwZoomDragImageView(getActivity());
//        ImageView imgAnswer = new ImageView(getActivity());
            LinearLayout.LayoutParams imgAnswer_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            imgAnswer.setLayoutParams(imgAnswer_params);

            try{
                Glide.with(this)
                        .load(stusAnswerList_img.get(index).toString())
                        .error(error_img)
                        .fitCenter()
                        .into(imgAnswer);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            linear_img.addView(imgAnswer);
        }

        //上翻页
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectStuAnswerIndex_img >= 1){
                    selectStuAnswerIndex_img--;
                    int allNum = stusNameList_img.size(); //图片答案-总人数
                    String tx = (selectStuAnswerIndex_img + 1) + "/" + allNum;
                    tx_num.setText(tx);
                    tx_who.setText(stusNameList_img.get(selectStuAnswerIndex_img));
                    linear_img.removeAllViews();  //清空布局
                    Drawable error_img = getResources().getDrawable(R.mipmap.error_img);
                    SwZoomDragImageView imgAnswer = new SwZoomDragImageView(getActivity());
                    LinearLayout.LayoutParams imgAnswer_params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    imgAnswer.setLayoutParams(imgAnswer_params);
                    try{
                        Glide.with(getActivity())
                                .load(stusAnswerList_img.get(selectStuAnswerIndex_img).toString())
                                .error(error_img)
                                .fitCenter()
                                .into(imgAnswer);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    linear_img.addView(imgAnswer);
                }
            }
        });
        //下翻页
        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectStuAnswerIndex_img < stusNameList_img.size() - 1){
                    selectStuAnswerIndex_img++;
                    int allNum = stusNameList_img.size(); //图片答案-总人数
                    String tx = (selectStuAnswerIndex_img + 1) + "/" + allNum;
                    tx_num.setText(tx);
                    tx_who.setText(stusNameList_img.get(selectStuAnswerIndex_img));
                    linear_img.removeAllViews();
                    Drawable error_img = getResources().getDrawable(R.mipmap.error_img);
                    SwZoomDragImageView imgAnswer = new SwZoomDragImageView(getActivity());
                    LinearLayout.LayoutParams imgAnswer_params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    imgAnswer.setLayoutParams(imgAnswer_params);
                    try{
                        Glide.with(getActivity())
                                .load(stusAnswerList_img.get(selectStuAnswerIndex_img).toString())
                                .error(error_img)
                                .fitCenter()
                                .into(imgAnswer);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    linear_img.addView(imgAnswer);
                }
            }
        });


        tx_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear1.setVisibility(View.VISIBLE);
                linear_imgAndtxt.setVisibility(View.GONE);
                selectStuAnswerIndex = 0;
                selectStuAnswerIndex_img = 0;
            }
        });

//        linear_img.addView(imgAnswer);
    }

    //判断数组中是否包含某个字符串，并返回下标
    private int isHave(String item){
        for(int i = 0 ; i < stusNameList_img.size() ; i++){
            if(item.equals(stusNameList_img.get(i))){
                selectStuAnswerIndex_img = i;
                return i;
            }
        }
        return -1;
    }


    //主观题-显示学生答题详情
    private void showStudentsAnswer(View v){
        ScrollView slStusAnswersImg = v.findViewById(R.id.slStusAnswersImg);
        ScrollView slStusAnswers = v.findViewById(R.id.slStusAnswers);
        slStusAnswers.setVisibility(View.VISIBLE);
        slStusAnswersImg.setVisibility(View.GONE);

        //在该布局内动态添加控件
        LinearLayout linearStusAnswers = v.findViewById(R.id.linearStusAnswers);
        linearStusAnswers.removeAllViews();
        //文字答案
        for(int i = 0 ; i < stusAnswerList_answer.size() ; i++){
            //包裹选择当前选项作为答案的学生姓名
            LinearLayout linearAnswer = new LinearLayout(getActivity());
            //设置参数
            LinearLayout.LayoutParams linear_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linear_params.setMargins(20 , 0 , 20 , 5 );
            linearAnswer.setLayoutParams(linear_params);
            linearAnswer.setOrientation(LinearLayout.HORIZONTAL);

            TextView txt_da = new TextView(getActivity());
            //设置参数
            LinearLayout.LayoutParams txt_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            txt_da.setLayoutParams(txt_params);
            txt_da.setTextSize(15);
            txt_da.setText("答:");
            txt_da.setTextColor(Color.parseColor("#828798"));

            TextView txt_answer = new TextView(getActivity());
            txt_answer.setPadding(10 , 0 , 0 , 0);
            txt_answer.setLayoutParams(txt_params);
            txt_answer.setTextSize(15);
            txt_answer.setText(stusAnswerList_answer.get(i));
            txt_answer.setTextColor(Color.parseColor("#33a3dc"));
            linearAnswer.addView(txt_da);
            linearAnswer.addView(txt_answer);
            linearStusAnswers.addView(linearAnswer);

            //包裹选择当前选项作为答案的学生姓名
            LinearLayout linearAll = new LinearLayout(getActivity());
            //设置参数
            LinearLayout.LayoutParams linearAll_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linear_params.setMargins(10 , 0 , 10 , 0 );
            linearAll.setLayoutParams(linearAll_params);
            linearAll.setOrientation(LinearLayout.VERTICAL);

            int linearSum =  (int)Math.ceil(stusAnswerList_name.get(i).size() / 8.0);
            LinearLayout[] answersList = new LinearLayout[linearSum];
            for(int k = 0 ; k < answersList.length ; k++){
                answersList[k] = new LinearLayout(getActivity());
                //设置参数
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                answersList[k].setLayoutParams(params);
                answersList[k].setWeightSum(8);
                answersList[k].setOrientation(LinearLayout.HORIZONTAL);
            }
            for(int j = 0 ; j < stusAnswerList_name.get(i).size() ; j++){
                TextView txt_name = new TextView(getActivity());
                LinearLayout.LayoutParams txtname_params = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1);

                //每行展示8个学生姓名
                if(j % 8 == 0){
                    txtname_params.setMargins(10 , 5 , 5 , 5);
                }else{
                    txtname_params.setMargins(5 , 5 , 5 , 5);
                }
                txt_name.setLayoutParams(txtname_params);
                txt_name.setPadding(5 , 2 , 5 , 2);
                txt_name.setTextSize(15);
                txt_name.setGravity(Gravity.CENTER);
                txt_name.setText(stusAnswerList_name.get(i).get(j));

                txt_name.setBackgroundColor(Color.parseColor("#d3d7d4"));
                txt_name.setTextColor(Color.parseColor("#828798"));
                answersList[j / 8].addView(txt_name);
            }
            for(int k = 0 ; k < answersList.length ; k++){
                linearAll.addView(answersList[k]);
            }
            linearStusAnswers.addView(linearAll);
        }

        //拍照答案
        if(stusNameList_img.size() > 0){
            TextView txt_paizhao = new TextView(getActivity());
            //设置参数
            LinearLayout.LayoutParams txt_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            txt_params.setMargins(10 , 5 , 10 , 5);
            txt_paizhao.setLayoutParams(txt_params);
            txt_paizhao.setTextSize(15);
            txt_paizhao.setText("拍照片");
            txt_paizhao.setTextColor(Color.parseColor("#828798"));
            linearStusAnswers.addView(txt_paizhao);

            //包裹选择当前选项作为答案的学生姓名
            LinearLayout linearAll = new LinearLayout(getActivity());
            //设置参数
            LinearLayout.LayoutParams linear_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearAll.setLayoutParams(linear_params);
            linearAll.setOrientation(LinearLayout.VERTICAL);

            int linearSum =  (int)Math.ceil(stusNameList_img.size() / 8.0);
            LinearLayout[] answersList = new LinearLayout[linearSum];
            for(int k = 0 ; k < answersList.length ; k++){
                answersList[k] = new LinearLayout(getActivity());
                //设置参数
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                answersList[k].setLayoutParams(params);
                answersList[k].setWeightSum(8);
                answersList[k].setOrientation(LinearLayout.HORIZONTAL);
            }
            for(int i = 0 ; i < stusNameList_img.size() ; i++){
                TextView txt_name = new TextView(getActivity());
                LinearLayout.LayoutParams txtname_params = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1);

                //每行展示8个学生姓名
                if(i % 8 == 0){
                    txtname_params.setMargins(10 , 5 , 5 , 5);
                }else{
                    txtname_params.setMargins(5 , 5 , 5 , 5);
                }
                txt_name.setLayoutParams(txtname_params);
                txt_name.setPadding(5 , 2 , 5 , 2);
                txt_name.setTextSize(15);
                txt_name.setGravity(Gravity.CENTER);
                txt_name.setText(stusNameList_img.get(i));

                txt_name.setBackgroundColor(Color.parseColor("#d3d7d4"));
                txt_name.setTextColor(Color.parseColor("#828798"));
                answersList[i / 8].addView(txt_name);
            }
            for(int k = 0 ; k < answersList.length ; k++){
                linearAll.addView(answersList[k]);
            }
            linearStusAnswers.addView(linearAll);
        }

        //未答题
        if(stusAnswerList_Noanswer.size() > 0){
            TextView txt_weida = new TextView(getActivity());
            //设置参数
            LinearLayout.LayoutParams txt_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            txt_params.setMargins(10 , 5 , 10 , 5);
            txt_weida.setLayoutParams(txt_params);
            txt_weida.setTextSize(15);
            txt_weida.setText("未答题");
            txt_weida.setTextColor(Color.parseColor("#828798"));
            linearStusAnswers.addView(txt_weida);

            //包裹选择当前选项作为答案的学生姓名
            LinearLayout linearAll = new LinearLayout(getActivity());
            //设置参数
            LinearLayout.LayoutParams linear_params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            linearAll.setLayoutParams(linear_params);
            linearAll.setOrientation(LinearLayout.VERTICAL);

            int linearSum =  (int)Math.ceil(stusAnswerList_Noanswer.size() / 8.0);
            LinearLayout[] answersList = new LinearLayout[linearSum];
            for(int k = 0 ; k < answersList.length ; k++){
                answersList[k] = new LinearLayout(getActivity());
                //设置参数
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                answersList[k].setLayoutParams(params);
                answersList[k].setWeightSum(8);
                answersList[k].setOrientation(LinearLayout.HORIZONTAL);
            }
            for(int i = 0 ; i < stusAnswerList_Noanswer.size() ; i++){
                TextView txt_name = new TextView(getActivity());
                LinearLayout.LayoutParams txtname_params = new LinearLayout.LayoutParams(
                        0,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1);

                //每行展示8个学生姓名
                if(i % 8 == 0){
                    txtname_params.setMargins(10 , 5 , 5 , 5);
                }else{
                    txtname_params.setMargins(5 , 5 , 5 , 5);
                }
                txt_name.setLayoutParams(txtname_params);
                txt_name.setPadding(5 , 2 , 5 , 2);
                txt_name.setTextSize(15);
                txt_name.setGravity(Gravity.CENTER);
                txt_name.setText(stusAnswerList_Noanswer.get(i));

                txt_name.setBackgroundColor(Color.parseColor("#d3d7d4"));
                txt_name.setTextColor(Color.parseColor("#828798"));
                answersList[i / 8].addView(txt_name);
            }
            for(int k = 0 ; k < answersList.length ; k++){
                linearAll.addView(answersList[k]);
            }
            linearStusAnswers.addView(linearAll);
        }
    }

    public static void handleSSLHandshake() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

                }

                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            }};
            SSLContext sc = SSLContext.getInstance("TLS");
            // trustAllCerts信任所有的证书
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception ignored) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        handleSSLHandshake();

        // 设置布局文件
        View contentView = inflater.inflate(R.layout.answer_question, container, false);
        FcontentView = contentView;
        txType_tiwen = (TextView) contentView.findViewById(R.id.txt_tiwen);
        txType_suiji = (TextView) contentView.findViewById(R.id.txt_suiji);
        txType_qiangda = (TextView) contentView.findViewById(R.id.txt_qiangda);
        txModle_danxuan = (TextView) contentView.findViewById(R.id.txt_danxuan);
        txModle_duoxuan = (TextView) contentView.findViewById(R.id.txt_duoxuan);
        txModle_panduan = (TextView) contentView.findViewById(R.id.txt_panduan);
        txModle_luru = (TextView) contentView.findViewById(R.id.txt_luru);

        imgdanxuan = (ImageView) contentView.findViewById(R.id.imgdanxuan);
        imgduoxuan = (ImageView) contentView.findViewById(R.id.imgduoxuan);
        imgpanduan = (ImageView) contentView.findViewById(R.id.imgpanduan);
        imgluru = (ImageView) contentView.findViewById(R.id.imgluru);

        txChoose = (TextView) contentView.findViewById(R.id.txChoose);
        spinner = (Spinner) contentView.findViewById(R.id.spinner);

        jishiqi = (Chronometer) contentView.findViewById(R.id.id_jishiqi);
        tx_answers_sum = (TextView) contentView.findViewById(R.id.tx_answers_sum);

        btBegin1 = (Button) contentView.findViewById(R.id.btBegin1);
        btBegin2 = (Button) contentView.findViewById(R.id.btBegin2);

        btBegin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btBegin2.setVisibility(View.GONE);
                linear1.setVisibility(View.VISIBLE);
                linear2.setVisibility(View.VISIBLE);
                if(txType_tiwen.isSelected()){
                    if(txModle_luru.isSelected() || imgluru.isSelected()){
                        btSingle.setText("答案内容");
                    }else{
                        btSingle.setText("单题详情");
                    }
                    btAnswers.setText("答题详情");
                    btEnd.setText("结束作答");
                    btClosed.setText("关闭提问");
                    tx_answers_sum.setVisibility(View.VISIBLE);
                    btSingle.setTextColor(Color.parseColor("#FFFFFFFF"));
                    btAnswers.setTextColor(Color.parseColor("#FFFFFFFF"));
                    btSingle.setBackground(getResources().getDrawable((R.drawable.btn_begin_enable)));
                    btAnswers.setBackground(getResources().getDrawable((R.drawable.btn_begin_enable)));
                }else if(txType_suiji.isSelected()){
                    btSingle.setText("点赞");
                    btAnswers.setText("设置答案");
                    btEnd.setText("结束随机");
                    btClosed.setText("关闭随机");
                    tx_answers_sum.setVisibility(View.INVISIBLE);
                    btSingle.setTextColor(Color.parseColor("#80000000"));
                    btAnswers.setTextColor(Color.parseColor("#80000000"));
                    btSingle.setBackground(getResources().getDrawable((R.drawable.btn_begin_unenable)));
                    btAnswers.setBackground(getResources().getDrawable((R.drawable.btn_begin_unenable)));
                }else if(txType_qiangda.isSelected()){
                    btSingle.setText("点赞");
                    btAnswers.setText("设置答案");
                    btEnd.setText("结束抢答");
                    btClosed.setText("关闭抢答");
                    tx_answers_sum.setVisibility(View.INVISIBLE);
                    btSingle.setTextColor(Color.parseColor("#80000000"));
                    btAnswers.setTextColor(Color.parseColor("#80000000"));
                    btSingle.setBackground(getResources().getDrawable((R.drawable.btn_begin_unenable)));
                    btAnswers.setBackground(getResources().getDrawable((R.drawable.btn_begin_unenable)));
                }
                jishiqi.setBase(SystemClock.elapsedRealtime());  //设置起始时间 ，这里是从0开始
                jishiqi.start();   //开始计时
            }
        });

        linear1 = (LinearLayout) contentView.findViewById(R.id.linear1);
        linear2 = (LinearLayout) contentView.findViewById(R.id.linear2);

        btSingle = (Button) contentView.findViewById(R.id.btSingle);
        btAnswers = (Button) contentView.findViewById(R.id.btAnswers);
        btEnd = (Button) contentView.findViewById(R.id.btEnd);
        btClosed = (Button) contentView.findViewById(R.id.btClosed);

        //单题分析（客观题）、答案内容（主观题）
        btSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View view = inflater.inflate(R.layout.single_question_analysis , null , false);
                if(txType_tiwen.isSelected()){
                    if(!imgluru.isSelected()){ //客观题：单选、多选、判断
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.single_question_analysis, null, false);
                        showPopupWindow(view , 1);
                    }else{ //主观题：录入
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.luru_question_analysis, null, false);
                        showPopupWindow_luru(view , 1);
                    }
                }
            }
        });
        //答题详情（主客观题）
        btAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                View view = inflater.inflate(R.layout.single_question_analysis , null , false);
                if(txType_tiwen.isSelected()){
                    if(!imgluru.isSelected()) { //客观题：单选、多选、判断
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.single_question_analysis, null, false);
                        showPopupWindow(view, 2);
                    }else{  //主观题：录入
                        View view = LayoutInflater.from(getActivity()).inflate(R.layout.luru_question_analysis, null, false);
                        showPopupWindow_luru(view , 2);
                    }
                }
            }
        });

        //结束作答/重新作答（主观题）  结束随机/重新随机   结束抢答/重新抢答
        btEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txType_tiwen.isSelected()){
                    if(btEnd.getText().equals("结束作答")){
                        btEnd.setText("重新作答");
                        linear2.setVisibility(View.GONE);
                    }else{
                        btEnd.setText("结束作答");
                        linear2.setVisibility(View.VISIBLE);
                        jishiqi.setBase(SystemClock.elapsedRealtime());  //设置起始时间 ，这里是从0开始
                        jishiqi.start();   //开始计时
                    }
                }else if(txType_suiji.isSelected()){
                    if(btEnd.getText().equals("结束随机")){
                        btEnd.setText("重新随机");
                        linear2.setVisibility(View.GONE);
                    }else{
                        btEnd.setText("结束随机");
                        linear2.setVisibility(View.VISIBLE);
                        jishiqi.setBase(SystemClock.elapsedRealtime());  //设置起始时间 ，这里是从0开始
                        jishiqi.start();   //开始计时
                    }
                }else if(txType_qiangda.isSelected()){
                    if(btEnd.getText().equals("结束抢答")){
                        btEnd.setText("重新抢答");
                        linear2.setVisibility(View.GONE);
                    }else{
                        btEnd.setText("结束抢答");
                        linear2.setVisibility(View.VISIBLE);
                        jishiqi.setBase(SystemClock.elapsedRealtime());  //设置起始时间 ，这里是从0开始
                        jishiqi.start();   //开始计时
                    }
                }
            }
        });

        //结束作答（主客观题）
        btClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSelected1();
                setSelected2();
                txType_tiwen.setSelected(true);
                txChoose.setVisibility(View.INVISIBLE);
                spinner.setVisibility(View.INVISIBLE);
                linear1.setVisibility(View.GONE);
                jishiqi.stop();
                linear2.setVisibility(View.GONE);
            }
        });

        bindViews(); //UI组件事件绑定
        txType_tiwen.performClick();   //（主动调用组件的点击事件）模拟一次点击，既进去后选择第一项

        //调整”互动答题“中”互动类型“的图片大小
        Drawable[] drawable1 = txType_tiwen.getCompoundDrawables();
        // 数组下表0~3,依次是:左上右下
        drawable1[1].setBounds(0, 0, 45, 45);
        txType_tiwen.setCompoundDrawables(null, drawable1[1], null, null);

        Drawable[] drawable2 = txType_suiji.getCompoundDrawables();
        drawable2[1].setBounds(0, 0, 45, 45);
        txType_suiji.setCompoundDrawables(null, drawable2[1], null, null);

        Drawable[] drawable3 = txType_qiangda.getCompoundDrawables();
        drawable3[1].setBounds(0, 0, 45, 45);
        txType_qiangda.setCompoundDrawables(null, drawable3[1], null, null);

//        ImageView imgdanxuan = (ImageView) contentView.findViewById(R.id.imgdanxuan);
//        imgdanxuan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                imgdanxuan.setImageResource(R.mipmap.danxuan2);
//            }
//        });

        //调整”互动答题“中”互动模式“的图片大小
//        Drawable[] drawable4 = txModle_danxuan.getCompoundDrawables();
//        drawable4[1].setBounds(0, 0, 52, 42);
//        txModle_danxuan.setCompoundDrawables(null, drawable4[1], null, null);
//
//        Drawable[] drawable5 = txModle_duoxuan.getCompoundDrawables();
//        drawable5[1].setBounds(0, 0, 52, 42);
//        txModle_duoxuan.setCompoundDrawables(null, drawable5[1], null, null);
//
//        Drawable[] drawable6 = txModle_panduan.getCompoundDrawables();
//        drawable6[1].setBounds(0, 0, 52, 42);
//        txModle_panduan.setCompoundDrawables(null, drawable6[1], null, null);
//
//        Drawable[] drawable7 = txModle_luru.getCompoundDrawables();
//        drawable7[1].setBounds(0, 0, 52, 42);
//        txModle_luru.setCompoundDrawables(null, drawable7[1], null, null);

        // Inflate the layout for this fragment
        return contentView;
    }

    @Override
    public void onClick(View v) {
//        jishiqi.stop();
        if(v.getId() == txType_tiwen.getId()) {
            setSelected1();
            txType_tiwen.setSelected(true);
        }else if(v.getId() == txType_suiji.getId()){
            setSelected1();
            txType_suiji.setSelected(true);
        }else if(v.getId() == txType_qiangda.getId()){
            setSelected1();
            txType_qiangda.setSelected(true);
        }else if(v.getId() == txModle_danxuan.getId() || v.getId() == imgdanxuan.getId()){
            txChoose.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            setSelected2();
            txModle_danxuan.setSelected(true);
            imgdanxuan.setSelected(true);
            btSingle.setText("单题分析");
        }else if(v.getId() == txModle_duoxuan.getId() || v.getId() == imgduoxuan.getId()){
            txChoose.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);
            setSelected2();
            txModle_duoxuan.setSelected(true);
            imgduoxuan.setSelected(true);
            btSingle.setText("单题分析");
        }else if(v.getId() == txModle_panduan.getId() || v.getId() == imgpanduan.getId()){
            txChoose.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            setSelected2();
            txModle_panduan.setSelected(true);
            imgpanduan.setSelected(true);
            btSingle.setText("单题分析");
        }else if(v.getId() == txModle_luru.getId() || v.getId() == imgluru.getId()){
            txChoose.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            setSelected2();
            txModle_luru.setSelected(true);
            imgluru.setSelected(true);
            btSingle.setText("答案内容");
        }

        //互动类型和互动模式都已选
        if(isSelected()){
            btBegin1.setVisibility(View.GONE);
            btBegin2.setVisibility(View.VISIBLE);
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.GONE);
        }else{
            txChoose.setVisibility(View.GONE);
            spinner.setVisibility(View.GONE);
            btBegin1.setVisibility(View.VISIBLE);
            btBegin2.setVisibility(View.GONE);
            linear1.setVisibility(View.GONE);
            linear2.setVisibility(View.GONE);
        }
    }
}