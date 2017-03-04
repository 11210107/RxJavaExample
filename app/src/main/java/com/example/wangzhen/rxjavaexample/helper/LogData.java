package com.example.wangzhen.rxjavaexample.helper;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hexi on 15/3/9.
 */
public class LogData {

//    private String osVersion;
//    private String device;//设备型号
//    private String screen;//分辨率
//    private String network;// WIFI|3G|4G
//    private String appVersion;
//    private String appName;
//    private String deviceId;
//    private String username;
//    private Integer userType;
//    private String tradeAccount;
//
//    private String eventType;
//    private String event;

    private Map<String, String> datas;
    private String data        = "";
    /**
     * platform : pc
     * user : jason
     * activity : 0
     * app : 猎银大师
     * version : 1.0.0
     * identify : UUID_HERE
     * type : 0
     * action : download
     * describe : succeed
     * net : WiFi
     * checksum : d7e8bdb02eb7be45debf47a588c9dc70
     */
    private String channel     = "10";
    private String platform    = "android";
    private String user        = "";
    private String activity    = "";
    private String app         ="";
    private String version     = "";
    private String identify    = "";
    private String type        = "0";
    private String action      = "";
    private String describe    = "";
    private String net         = "";
    private String checksum    = "0";
    private String ip          = "";
    private String company     = "";//serverId
    private String time        = "";
    private String os          = "";
    private String os_version  = "";
    private String user_status = "";

    public LogData withChannel(String channel) {
        this.channel = channel;
        return this;
    }

    public void setData() {
        if (this.datas != null && this.datas.size() > 0) {
            data = new Gson().toJson(datas);
        }
    }

    public void clearDatas() {
        if (this.datas != null && this.datas.size() > 0) {
            this.datas.clear();
            this.datas = null;
        }
    }

    public LogData withOs(String os) {
        this.os = os;
        return this;
    }

    public LogData withOsVersion(String osVersion) {
        this.os_version = osVersion;
        return this;
    }

    public LogData withApp(String app) {
        this.app = app;
        return this;
    }

    public LogData withVersion(String version) {
        this.version = version;
        return this;
    }

    public LogData withIdentify(String identify) {
        this.identify = identify;
        return this;
    }

    public LogData withCheckSum(String checksum) {
        this.checksum = checksum;
        return this;
    }

    public LogData withPlatform(String platform) {
        this.platform = platform;
        return this;
    }



    public static enum EventType {
        PAGE("page"),
        EVENT("event");

        private EventType(String value) {
            this.value = value;
        }

        public String value;
    }

    private LogData() {
        super();
    }

    public static class Builder {
        private LogData logData;

        public Builder(Context context) {
            this.logData = new LogData();

//            this.logData.net = NetworkUtil.getNetworkType(context).value;
//            this.logData.ip = NetworkUtil.getIp();
//            this.logData.company = Util.getCompanyName(context);
//
//            if (UserHelper.getInstance(context).isLogin()) {
//                User user = UserHelper.getInstance(context).getUser();
//                if (!TextUtils.isEmpty(user.getPhone())) {
//                    this.logData.user = user.getPhone();
//                } else {
//                    this.logData.user = user.getNickname();
//                }
////                this.logData.user = user.getUsername();
//                this.logData.user_status = String.valueOf(user.getUserType());
////                this.logData.tradeAccount = user.getTradeAccount();
//            }

        }

        private Builder withEventType(EventType eventType) {
//            this.logData.eventType = eventType.value;
            return this;
        }

        private Builder withEvent(String event) {
            this.logData.action = event;
            return this;
        }

        private Builder withTime(long time) {
            this.logData.time = String.valueOf(time/1000);
            return this;
        }

        public Builder withActivity(String activity) {
            this.logData.activity = activity;
            return this;
        }


        private LogData build() {
            return this.logData;
        }

        public LogData pv(String name) {
            return this.withEventType(EventType.PAGE)
                    .withEvent(name)
                    .withTime(System.currentTimeMillis())
                    .build();

        }

        public LogData event(String name) {
            return this.withEventType(EventType.EVENT)
                    .withEvent(name)
                    .withTime(System.currentTimeMillis())
                    .build();
        }

        public LogData eventNew(String name) {
            String activity = "";
//            for (Map.Entry<String, String> entry : EventConfig.actions.entrySet()) {
//                if (TextUtils.equals(name, entry.getValue())) {
//                    activity = entry.getKey();
//                }
//            }
            return this.withEvent(name)
                    .withActivity(activity)
                    .withTime(System.currentTimeMillis())
                    .build();
        }

    }

    public LogData append(String key, String value) {
        if (TextUtils.isEmpty(key) || value == null || TextUtils.isEmpty(value.toString())) {
            return this;
        }
        if (this.datas == null) {
            this.datas = new HashMap<>();
        }
        this.datas.put(key, value);
        return this;
    }

    public LogData append(Map<String, String> values) {
        if (values == null || values.isEmpty()) {
            return this;
        }
        if (this.datas == null) {
            this.datas = values;
        } else {
            this.datas.putAll(values);
        }
        return this;
    }
}
