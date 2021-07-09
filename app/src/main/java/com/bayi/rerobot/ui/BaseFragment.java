package com.bayi.rerobot.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class BaseFragment extends Fragment {
    private Toast mToast;
    private Unbinder unbinder;
    protected View mView;
    public void toast(String msg){
        if (mToast != null) {
            mToast.cancel();
        }else {

        }
        mToast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        mToast.show();

    }
    protected int getLayoutId() {
        return -1;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getLayoutId() != -1) {
            mView = inflater.inflate(getLayoutId(), null);
            unbinder = ButterKnife.bind(this, mView);
        }
        return mView;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
    }
    protected void initView() {
    }

    protected void initData() {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

}
