package com.bayi.rerobot.fragment;

import android.view.View;

import com.bayi.rerobot.R;
import com.bayi.rerobot.dialog.MoreDialog;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;

import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class newMainFragment_0 extends BaseFragment {


    private NewMain mainActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_newmian;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {

        }
    }

    @OnClick({R.id.view_consult, R.id.view_book, R.id.view_cewen, R.id.view_xiaodu, R.id.view_jieshao, R.id.view_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.view_consult:
                mainActivity.setFragmentView(1);
                break;
            case R.id.view_book:
                mainActivity.setFragmentView(2);
                break;
            case R.id.view_cewen:
               mainActivity.setFragmentView(11);
                break;
            case R.id.view_xiaodu:
                mainActivity.goXiaodu();
                break;
            case R.id.view_jieshao:
                mainActivity.setFragmentView(5);
                break;
            case R.id.view_set:
                mainActivity.setFragmentView(16);
                break;
        }
    }
}
