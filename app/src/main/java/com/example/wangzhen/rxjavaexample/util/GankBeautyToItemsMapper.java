package com.example.wangzhen.rxjavaexample.util;

import com.example.wangzhen.rxjavaexample.domain.GankBeauty;
import com.example.wangzhen.rxjavaexample.domain.MapResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by wangzhen on 17/2/6.
 */
public class GankBeautyToItemsMapper implements Func1<MapResult,List<GankBeauty>> {
    private static GankBeautyToItemsMapper gankBeautyToItemsMapper = new GankBeautyToItemsMapper();

    private GankBeautyToItemsMapper(){

    }

    public static GankBeautyToItemsMapper getInstance() {
        return gankBeautyToItemsMapper;
    }
    @Override
    public List<GankBeauty> call(MapResult mapResult) {
        List<GankBeauty> gankBeauties = mapResult.beauties;
        List<GankBeauty> items = new ArrayList<>(gankBeauties.size());
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS'Z'");
        SimpleDateFormat outputFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        for (GankBeauty gankBeauty : gankBeauties) {
            GankBeauty item = new GankBeauty();
            try {
                Date date = inputFormat.parse(gankBeauty.createdAt);
                item.createdAt = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
                item.createdAt = "unknown date";
            }
            item.url = gankBeauty.url;
            items.add(item);
        }
        return items;
    }
}
