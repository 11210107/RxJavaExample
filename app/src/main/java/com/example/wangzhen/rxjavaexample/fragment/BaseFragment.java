package com.example.wangzhen.rxjavaexample.fragment;


import android.app.AlertDialog;
import android.support.v4.app.Fragment;

import com.example.wangzhen.rxjavaexample.R;

import butterknife.OnClick;
import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseFragment extends Fragment {

    Subscription subscription ;
    private int mTitleText;

    public BaseFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.tip_query)
    public void onTipClick() {
        new AlertDialog.Builder(getContext())
                .setTitle(getTitleRes())
                .setView(getActivity().getLayoutInflater().inflate(getDialogLayout(), null))
                .show();
    }



    protected void unSubscribe() {
        if (subscription != null && subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unSubscribe();
    }

    protected abstract int getTitleRes();

    protected abstract int getDialogLayout();
}
