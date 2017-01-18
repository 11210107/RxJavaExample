package com.example.wangzhen.rxjavaexample.network.Api;


import com.example.wangzhen.rxjavaexample.domain.CardImage;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by wangzhen on 17/1/17.
 */
public interface PretendApi {
    @GET("search")
    Observable<List<CardImage>> search(@Query("q") String query);
}
