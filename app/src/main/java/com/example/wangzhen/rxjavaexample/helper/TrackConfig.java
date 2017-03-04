package com.example.wangzhen.rxjavaexample.helper;

/**
 * Created by hexi on 15/3/9.
 */
public class TrackConfig {
    private boolean logEnabled = true;
    private static TrackConfig instance = new TrackConfig();

    private TrackConfig() {
        super();
    }

    public static TrackConfig getInstance() {
        return instance;
    }

    public boolean getLoggingFlag() {
        return this.logEnabled;
    }

}
