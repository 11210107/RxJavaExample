package com.example.wangzhen.rxjavaexample.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by wangzhen on 17/2/3.
 */
public class MapResult extends Result{
    public @SerializedName("results")
    List<GankBeauty> beauties;
}
