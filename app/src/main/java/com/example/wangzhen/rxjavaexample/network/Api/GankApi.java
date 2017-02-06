package com.example.wangzhen.rxjavaexample.network.Api;


import com.example.wangzhen.rxjavaexample.domain.MapResult;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
/**
 * Created by wangzhen on 17/2/3.
 */
public interface GankApi {
    @GET("data/福利/{number}/{page}")
    Observable<MapResult> getGankImage(@Path("number") int number, @Path("page") int page);
}
