package com.example.wangzhen.rxjavaexample.network;

import com.example.wangzhen.rxjavaexample.network.Api.PretendApi;
import com.example.wangzhen.rxjavaexample.common.Constants;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wangzhen on 17/1/17.
 */
public class NetWork {

    public static PretendApi pretendApi;

    public static OkHttpClient okHttpClient = new OkHttpClient();
    public static Converter.Factory gsonConvertFactory = GsonConverterFactory.create();
    public static CallAdapter.Factory rxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

    public static PretendApi getPretendApi(){
        if (pretendApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .baseUrl(Constants.pretendApiBaseUrl)
                    .addConverterFactory(gsonConvertFactory)
                    .addCallAdapterFactory(rxJavaCallAdapterFactory)
                    .build();
            pretendApi = retrofit.create(PretendApi.class);
        }
        return pretendApi;
    }

}
