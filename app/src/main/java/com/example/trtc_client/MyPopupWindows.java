package com.example.trtc_client;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.PopupWindow;

public class MyPopupWindows extends PopupWindow {
    private int popupWidth;
    private int popupHeight;
    private View parentView;

    public MyPopupWindows(Context context, int popupWidth, int popupHeight, View parentView) {
        super(context);
        this.popupWidth = popupWidth;
        this.popupHeight = popupHeight;
        this.parentView = parentView;
    }

    /**
     * 初始化控件
     */

    private void initView(Context context) {
        parentView = View.inflate(context, R.layout.member_list_pop_window, null);
        setContentView(parentView);
    }

    /**
     * 设置弹出属性
     */

    private void setPopConfig() {
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0xcc000000);
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);// 设置外部触摸会关闭窗口

        //获取自身的长宽高
        parentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        popupHeight = parentView.getMeasuredHeight();
        popupWidth = parentView.getMeasuredWidth();
    }

    /**
     * 设置显示在v上方（以v的左边距为开始位置）
     * @param v
     */
    public void showUp(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        showAtLocation(v, Gravity.NO_GRAVITY, (location[0]) - popupWidth /2, location[1] - popupHeight);
    }

    /**
     * 设置显示在v上方（以v的中心位置为开始位置）
     * @param v
     */
    public void showUp2(View v) {
        //获取需要在其上方显示的控件的位置信息
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //在控件上方显示
        showAtLocation(v, Gravity.NO_GRAVITY, (location[0] + v.getWidth() / 2) - popupWidth / 2, location[1] - popupHeight);
    }
}
