package com.example.wangzhen.rxjavaexample;

import android.app.Application;

/**
 * Created by wangzhen on 17/1/17.
 */
public class App extends Application {

    private static App INSTANCE;

    public static App getInstance() {
        return INSTANCE;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }
}
