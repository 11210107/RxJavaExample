package com.example.wangzhen.rxjavaexample.fragment;


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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wangzhen.rxjavaexample.R;
import com.example.wangzhen.rxjavaexample.adapter.MapItemListAdapter;
import com.example.wangzhen.rxjavaexample.domain.MapResult;
import com.example.wangzhen.rxjavaexample.network.NetWork;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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
    @Bind(R.id.tip_query)
    Button             mTipQuery;
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

    Observer<MapResult> mObserver = new Observer<MapResult>() {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getActivity(), R.string.load_fail_message, Toast.LENGTH_SHORT).show();
        }
        @Override
        public void onNext(MapResult mapResult) {
            if (mapResult.beauties != null && mapResult.beauties.size() > 0) {
                mAdapter.setDatas(mapResult.beauties);
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

    private void loadMorePage(int pageNumber) {
        Log.d("wzTest", "pageNumber:" + pageNumber);
        unSubscribe();
        subscription = NetWork.getGankApi()
                .getGankImage(10,pageNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MapResult>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), R.string.load_fail_message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(MapResult mapResult) {
                        if (mapResult.beauties != null && mapResult.beauties.size() > 0) {
                            mAdapter.addAllDatas(mapResult.beauties);
                            mAdapter.changeLoadMoreStatus(MapItemListAdapter.PULLUP_LOAD_MORE);
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

    @Override
    protected int getTitleRes() {
        return R.string.flat_map_tap;
    }

    @Override
    protected int getDialogLayout() {
        return 0;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.btn_next_page, R.id.btn_last_page})
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
        }
    }

    private void loadPage(int pageNumber) {
        Log.d("wzTest", "pageNumber:" + pageNumber);
        mSwipeRefreshLayout.setRefreshing(true);
        unSubscribe();
        subscription = NetWork.getGankApi()
                .getGankImage(10,pageNumber)
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
