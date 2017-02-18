package com.example.wangzhen.rxjavaexample.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wangzhen.rxjavaexample.R;
import com.example.wangzhen.rxjavaexample.adapter.RecyclerViewAdapter;
import com.example.wangzhen.rxjavaexample.adapter.WZRecyclerViewAdapter;
import com.example.wangzhen.rxjavaexample.domain.GankBeauty;
import com.example.wangzhen.rxjavaexample.domain.MapResult;
import com.example.wangzhen.rxjavaexample.network.NetWork;
import com.example.wangzhen.rxjavaexample.widget.CustomLinearLayoutManager;
import com.example.wangzhen.rxjavaexample.widget.LRecyclerView;
import com.example.wangzhen.rxjavaexample.widget.SampleFooter;
import com.example.wangzhen.rxjavaexample.widget.recycleradapter.HeaderFooterRecyclerViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecyclerViewFragment extends BaseFragment {

    private int pageNumber = 1;
    @Bind(R.id.recycler_view)
    LRecyclerView mRecyclerView;

    private LinearLayoutManager             mLinearLayoutManager;
    private RecyclerViewAdapter             mRecyclerViewAdapter;
    private StaggeredGridLayoutManager      mStaggeredGridLayoutManager;
    private WZRecyclerViewAdapter           mWZRecyclerViewAdapter;
    private HeaderFooterRecyclerViewAdapter mHeaderFooterRecyclerViewAdapter;

    public RecyclerViewFragment() {
    }

    @Override
    protected int getTitleRes() {
        return 0;
    }

    @Override
    protected int getDialogLayout() {
        return 0;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //recyclerView设置固定大小
        mRecyclerView.setHasFixedSize(true);
        //创建线性布局
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        //线性布局垂直方向
        mLinearLayoutManager.setOrientation(OrientationHelper.VERTICAL);
        //创建布局管理器
        LinearLayoutManager linearLayoutManager=new CustomLinearLayoutManager(getActivity());
        //设置横向
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        //设置布局管理器
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        //创建瀑布流布局
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, OrientationHelper.VERTICAL);
        //给recyclerView设置布局管理器
        //mRecyclerView.setLayoutManager(mCustomLinearLayoutManager);
        //添加分割线
//        DividerDecoration divider = new DividerDecoration.Builder(getActivity())
//                .setHeight(R.dimen.default_divider_height)
//                .setPadding(R.dimen.default_divider_padding)
//                .setColorResource(R.color.split)
//                .build();
//        //mRecyclerView.addItemDecoration(new MyDecoration(getActivity(),OrientationHelper.VERTICAL));
//        mRecyclerView.addItemDecoration(divider);
        //创建适配器
        mRecyclerViewAdapter = new RecyclerViewAdapter(getActivity());
        mWZRecyclerViewAdapter = new WZRecyclerViewAdapter(mRecyclerViewAdapter);
        mHeaderFooterRecyclerViewAdapter = new HeaderFooterRecyclerViewAdapter(mRecyclerViewAdapter);
        //recyclerView设置适配器
        mRecyclerView.setAdapter(mHeaderFooterRecyclerViewAdapter);
        View header1 = LayoutInflater.from(getActivity()).inflate(R.layout.sample_header,(ViewGroup)getActivity().findViewById(android.R.id.content), false);
        mHeaderFooterRecyclerViewAdapter.addHeaderView(header1);
        mHeaderFooterRecyclerViewAdapter.addFooterView(header1);
        mHeaderFooterRecyclerViewAdapter.removeFooterView();
        //add a HeaderView
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.sample_header,(ViewGroup)getActivity().findViewById(android.R.id.content), false);
        mWZRecyclerViewAdapter.addHeaderView(header);
        SampleFooter sampleFooter = new SampleFooter(getActivity());
        mWZRecyclerViewAdapter.addFooterView(sampleFooter);
        mRecyclerViewAdapter.setOnRecyclerItemClickListener(new RecyclerViewAdapter.OnRecyclerItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "点击了第"+position+"项", Toast.LENGTH_SHORT).show();
            }
        });
        loadData(pageNumber);
    }

    public void loadData(int pageNumber) {
        unSubscribe();
        subscription = NetWork.getGankApi().getGankImage(10, pageNumber)
                .subscribeOn(Schedulers.io())
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
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<GankBeauty>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getActivity(), R.string.load_fail_message, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(List<GankBeauty> gankBeauties) {
                        if (gankBeauties != null && gankBeauties.size() > 0) {
                            mRecyclerViewAdapter.setDatas(gankBeauties);
                            mWZRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
