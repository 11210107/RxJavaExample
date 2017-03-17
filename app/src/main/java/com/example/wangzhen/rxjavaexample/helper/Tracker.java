package com.example.wangzhen.rxjavaexample.helper;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hexi on 15/3/9.
 */
public class Tracker {
    private static final int THRESHOLD_FOR_PRODUCT = 20;
    private static final int THRESHOLD_FOR_DEBUG   = 3;

    private static final String TAG = "Tracker";
    private Context context;

    private int threshold = THRESHOLD_FOR_PRODUCT;
    private List<LogData> data;

    private String screen;
    private String appVersion;
    private String deviceId;
    private String appName;
    private String appApplicationId;
    private String os;
    private String osVersion;
    private String checksum;

    private String  channel;
    private boolean debug;
    private boolean canAdd = true;

    private static volatile Tracker instance;

    public static Tracker getInstance(Context context) {
        if (instance == null) {
            instance = new Tracker(context);
        }
        return instance;
    }

    public Tracker init(String channel) {
        this.channel = channel;
        return this;
    }

    public Tracker withDebug(boolean debug) {
        Log.d(TAG, "=====withDebug, debug:" + debug);
        this.debug = debug;
        if (this.debug) {
            this.threshold = THRESHOLD_FOR_DEBUG;
        } else {
            this.threshold = THRESHOLD_FOR_PRODUCT;
        }
        return this;
    }

    public Tracker setAddEnable(boolean enable) {
        this.canAdd = enable;
        return this;
    }

    private Tracker(Context context) {
        this.context = context;
        this.data = new ArrayList<>();

//        this.screen = TelephoneUtil.getScreen(context);
//        this.appVersion = AppUtil.getAppVersion(context);
//        this.appName = AppUtil.getAppName(context);
//        this.deviceId = TelephoneUtil.getEncodedDeviceId(context);
//        this.appApplicationId = AppUtil.getAppApplicationId(context);
//        this.os = TelephoneUtil.getSystemModel();
//        this.osVersion = TelephoneUtil.getOsVersion();
//        String temp = deviceId + "yskj" + appApplicationId + "app";
//        Log.d(TAG, "checksum: " + MD5Util.getMD5Str(temp));
//        this.checksum = MD5Util.getMD5Str(temp);
    }

    public void addLog(LogData logData) {
        if (!canAdd) {
            return;
        }
        logData.withChannel(channel)
                .withIdentify(deviceId)
                .withOs(os)
                .withOsVersion(osVersion)
                .withVersion(appVersion)
                .withApp(appApplicationId)
                .withCheckSum(checksum);
        this.data.add(logData);
        Log.d("wzTest", "dataSize"+data.size());
        if (this.data != null && this.data.size() >= this.threshold) {
            Sender.getInstance(this.context).addToQueue(new Gson().toJson(this.data));
            clear();
        }
    }

    public void save() {
        Log.d(TAG, "=====save, debug:" + debug);
        if (this.debug) {
            return;
        }
        if (this.context != null && this.data.size() > 0) {
            Sender.getInstance(this.context).saveListToDB(new Gson().toJson(data));
            clear();

            Sender.getInstance(this.context).save();
        }
    }

    private void clear() {
        this.data = new ArrayList<>();
    }
}
