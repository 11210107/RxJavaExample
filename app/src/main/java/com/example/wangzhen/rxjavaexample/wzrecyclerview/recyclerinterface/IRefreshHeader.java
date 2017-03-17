package com.example.wangzhen.rxjavaexample.wzrecyclerview.recyclerinterface;

/**
 * Created by wangzhen on 17/3/17.
 */
public interface IRefreshHeader {
    //正常状态
    int STATE_NORMAL             = 0;
    //松开刷新
    int STATE_RELEASE_TO_REFRESH = 1;
    //正在刷新
    int STATE_RELEASING          = 2;
    //刷新完成
    int STATE_DONE               = 3;

    void onReset();

    //可以刷新状态,已经过了指定距离
    void onPrepare();

    //下拉移动
    void onMove(float offSet, float sumOffSet);

    //松开刷新
    void onRelease();

    /**
     * 下拉刷新完成
     */
    void refreshComplete();


    /**
     * 获取HeaderView
     */
    void getHeaderView();


}
