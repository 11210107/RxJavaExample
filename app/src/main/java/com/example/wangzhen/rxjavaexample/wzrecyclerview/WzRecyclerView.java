package com.example.wangzhen.rxjavaexample.wzrecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewConfiguration;

/**
 * Created by wangzhen on 17/3/17.
 */
public class WzRecyclerView extends RecyclerView {

    //最小敏感域
    private int mTouchSlop;
    //是否可以下拉刷新
    private boolean mPullRefreshEnabled = true;


    public WzRecyclerView(Context context) {
        this(context,null);
    }

    public WzRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WzRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mTouchSlop = ViewConfiguration.get(getContext().getApplicationContext()).getScaledTouchSlop();
        if (mPullRefreshEnabled) {

        }
    }

}
