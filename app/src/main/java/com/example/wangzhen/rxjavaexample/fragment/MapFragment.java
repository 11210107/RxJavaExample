package com.example.wangzhen.rxjavaexample.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangzhen.rxjavaexample.R;
import com.example.wangzhen.rxjavaexample.adapter.MapItemListAdapter;
import com.example.wangzhen.rxjavaexample.domain.GankBeauty;
import com.example.wangzhen.rxjavaexample.domain.MapResult;
import com.example.wangzhen.rxjavaexample.network.NetWork;
import com.example.wangzhen.rxjavaexample.util.GankBeautyToItemsMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener{
    //当前页
    private int pageNumber = 1;
    //最后一条可见条目
    private int lastVisibleItem;
    @Bind(R.id.tv_page_number)
    TextView           mTvPageNumber;
    @Bind(R.id.recycler_view)
    RecyclerView       mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.btn_next_page)
    AppCompatButton    mBtnNextPage;
    @Bind(R.id.btn_last_page)
    AppCompatButton    mBtnLastPage;

    MapItemListAdapter mAdapter;

    public MapFragment() {
        // Required empty public constructor
    }

    Observer<List<GankBeauty>> mObserver = new Observer<List<GankBeauty>>(){
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.load_fail_message, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNext(List<GankBeauty> gankBeautys) {
            if (gankBeautys != null && gankBeautys.size() > 0) {
                mAdapter.setDatas(gankBeautys);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }


    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MapItemListAdapter();
        final GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.blue,R.color.green,R.color.red,R.color.yellow);
//        mSwipeRefreshLayout.setEnabled(false);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState ==RecyclerView.SCROLL_STATE_IDLE && lastVisibleItem + 1 ==mAdapter.getItemCount()) {
                    mAdapter.changeLoadMoreStatus(MapItemListAdapter.LOADING_MORE);
                    loadMorePage(pageNumber + 1);
                    pageNumber++;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
//                Log.d("wzTest", "lastVisibleItem:" + lastVisibleItem);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        //加载数据
        loadPage(pageNumber);
    }

    private void loadMorePage(int pageNumber) {
        Log.d("wzTest", "pageNumber:" + pageNumber);
        unSubscribe();
        subscription = NetWork.getGankApi()
                .getGankImage(10,pageNumber)
                .map(new Func1<MapResult, List<GankBeauty>>() {
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
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<List<GankBeauty>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.load_fail_message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<GankBeauty> gankBeauties) {
                        if (gankBeauties != null && gankBeauties.size() > 0) {
                            mAdapter.addAllDatas(gankBeauties);
                            mAdapter.changeLoadMoreStatus(MapItemListAdapter.PULLUP_LOAD_MORE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
//                .subscribe(new Subscriber<MapResult>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        Toast.makeText(getActivity(), R.string.load_fail_message, Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(MapResult mapResult) {
//                        if (mapResult.beauties != null && mapResult.beauties.size() > 0) {
//                            mAdapter.addAllDatas(mapResult.beauties);
//                            mAdapter.changeLoadMoreStatus(MapItemListAdapter.PULLUP_LOAD_MORE);
//                            mSwipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                });
    }

    @Override
    protected int getTitleRes() {
        return R.string.flat_map_tap;
    }

    @Override
    protected int getDialogLayout() {
        return R.layout.dialog_map;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_next_page, R.id.btn_last_page,R.id.tip_query})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_page:
                loadPage(++pageNumber);
                if (pageNumber == 2) {
                    mBtnLastPage.setEnabled(true);
                }
                break;
            case R.id.btn_last_page:
                loadPage(--pageNumber);
                if (pageNumber == 1) {
                    mBtnLastPage.setEnabled(false);
                }
                break;
            case R.id.tip_query:
                new AlertDialog.Builder(getContext())
                        .setTitle(getTitleRes())
                        .setView(getActivity().getLayoutInflater().inflate(getDialogLayout(), null))
                        .show();
                break;
        }
    }

    private void loadPage(int pageNumber) {
        Log.d("wzTest", "pageNumber:" + pageNumber);
        mSwipeRefreshLayout.setRefreshing(true);
        unSubscribe();
        subscription = NetWork.getGankApi()
                .getGankImage(10,pageNumber)
                .map(GankBeautyToItemsMapper.getInstance())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);
    }


    @Override
    public void onRefresh() {
//        Log.d("wzTest",Thread.currentThread().getName());
        pageNumber = 1;
        loadPage(pageNumber);
    }
}
