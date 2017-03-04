package com.example.wangzhen.rxjavaexample.helper;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.baidao.tools.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ytx.library.provider.ApiFactory;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import retrofit2.Call;
import retrofit2.Response;


/**
 * Created by hexi on 15/3/9.
 */
public class Sender implements Runnable {

    private static final String TAG = "Sender";
    private Context               context;
    private BlockingQueue<String> queue;
    private Object sendMutex = new Object();


    //singleton
    private static volatile Sender instance;

    public static Sender getInstance(Context context) {
        if (instance == null) {
            instance = new Sender(context);
        }
        return instance;
    }

    //constructor
    private Sender(Context context) {
        this.context = context;
        queue = new LinkedBlockingQueue<>(512);
        startThread();
    }

    private void startThread() {
        new Thread(this).start();
    }


    @Override
    public void run() {
        boolean hasMoreData;
        while (TrackConfig.getInstance().getLoggingFlag()) {
            String list = queue.poll();
            if (list != null) {
                boolean success = sendList(list);
                if (!success) {
                    saveListToDB(list);
                }
            } else {
                Integer recordId = getLastRecordIdFromDb();
                if (recordId != null) {
                    String singleRecordList = DBHelper.getInstance(context).getLogDataById(recordId);
                    Log.d("Sender", "DB Data: " + singleRecordList);
                    if (!TextUtils.isEmpty(singleRecordList) && sendList(singleRecordList)) {
                        DBHelper.getInstance(context).deleteLogDataById(recordId);
                    }
                }
            }
            try {
                Thread.sleep(1_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            hasMoreData = hasDataToSend();

            while (!isSendingReady() || !hasMoreData) {
                try {
                    synchronized (sendMutex) {
                        sendMutex.wait(120_000);//time out 2 minutes
                    }
                    hasMoreData = hasDataToSend();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean sendList(String json) {
        if (TextUtils.isEmpty(json)) {
            return true;
        }
        boolean success = Sender.executeSyncPostTask(format(json), context);
        Log.d("tag", "====log sent, result: " + success);
        return success;
    }

    private String format(String logDatasJson) {
        List<LogData> logDatas = new Gson().fromJson(logDatasJson, new TypeToken<ArrayList<LogData>>() {
        }.getType());
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0, length = logDatas.size(); i < length; i++) {
            LogData item = logDatas.get(i);
            item.setData();
            item.clearDatas();
            sb.append(new Gson().toJson(item));
            if (i != length - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private static boolean executeSyncPostTask(String json, Context context) {
        try {

            //Call call = ApiFactory.getStatisticsApi().sendLog(json);
            Call call = ApiFactory.getNewUploadApi().sendListTrack(json);

            Log.d(TAG, "executeSyncPostTask json: " + json);
            Response response = call.execute();
            Log.d("Sender", "code: " + response.code());
            if (response.code() == HttpURLConnection.HTTP_OK) {
                return true;
            } else {
                Log.e("Sender", "send tracker log to server error");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveListToDB(String json) {
        if (context != null) {
            try {
                return DBHelper.getInstance(context).save(json);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private Integer getLastRecordIdFromDb() {
        if (context != null) {
            return DBHelper.getInstance(context).getLastLogDataId();
        }
        return null;
    }

    private boolean hasDataToSend() {
        int size = this.queue.size();
        return (size > 0) || (getLastRecordIdFromDb() != null);
    }

    private boolean isSendingReady() {
        return NetworkUtil.isNetworkConnected(context);
    }

    public void addToQueue(String dataJson) {
        try {
            queue.add(dataJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        notifySendMutex();
    }

    private void notifySendMutex() {
        synchronized (sendMutex) {
            sendMutex.notifyAll();
        }
    }

    public void save() {
        List<String> newQueue = new ArrayList<>();
        queue.drainTo(newQueue);
        for (String logData : newQueue) {
            this.saveListToDB(logData);
        }
        notifySendMutex();
        newQueue.clear();
    }
}
