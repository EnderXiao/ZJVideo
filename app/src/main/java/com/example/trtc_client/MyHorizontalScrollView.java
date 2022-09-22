package com.example.trtc_client;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

public class MyHorizontalScrollView extends HorizontalScrollView {
    private View mView;

    public MyHorizontalScrollView(Context context) {
        super(context);
    }

    public MyHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        //这句的意思就是我滚到哪里，设置进来的空间就滚到哪里
        if (mView != null) {
            mView.scrollTo(l, t);
        }
    }
    /**
     * 设置联动的view
     * @param view
     */
    public void setScrollView(View view) {
        mView = view;
    }
}
