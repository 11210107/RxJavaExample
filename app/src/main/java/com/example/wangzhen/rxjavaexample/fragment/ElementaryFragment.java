package com.example.wangzhen.rxjavaexample.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.example.wangzhen.rxjavaexample.R;
import com.example.wangzhen.rxjavaexample.adapter.MyRecyclerAdapter;
import com.example.wangzhen.rxjavaexample.domain.CardImage;
import com.example.wangzhen.rxjavaexample.network.NetWork;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ElementaryFragment extends Fragment {


    @Bind(R.id.radio_button_lovely)
    AppCompatRadioButton mRadioButtonLovely;
    @Bind(R.id.radio_button_police)
    AppCompatRadioButton mRadioButtonPolice;
    @Bind(R.id.radio_button_pretend)
    AppCompatRadioButton mRadioButtonPretend;
    @Bind(R.id.radio_button_myself)
    AppCompatRadioButton mRadioButtonMyself;
    @Bind(R.id.tip_query)
    Button               mTipQuery;
    @Bind(R.id.recycler_view)
    RecyclerView         mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout   mSwipeRefreshLayout;

    MyRecyclerAdapter mAdapter = new MyRecyclerAdapter();

    public ElementaryFragment() {

    }

    Subscription subscription ;


    Observer<List<CardImage>> observer = new Observer<List<CardImage>>() {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(List<CardImage> cardImages) {
            if (cardImages != null && cardImages.size() > 0) {
                mSwipeRefreshLayout.setRefreshing(false);
                mAdapter.setDatas(cardImages);
            }
        }


    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_elementary, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN, Color.BLACK);
        mSwipeRefreshLayout.setEnabled(false);
        return view;
    }

    @OnCheckedChanged({R.id.radio_button_lovely,R.id.radio_button_police,R.id.radio_button_myself,R.id.radio_button_pretend})
    public void onTabChecked(RadioButton searchRb, boolean checked) {
        if (checked) {
            unSubscribe();
            mAdapter.setDatas(null);
            mSwipeRefreshLayout.setRefreshing(true);
            search(searchRb.getText().toString());
        }
    }

    private void search(String key) {
        subscription = NetWork.getPretendApi()
                .search(key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);

    }

    protected void unSubscribe() {
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        unSubscribe();
    }
}
