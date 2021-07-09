package com.bayi.rerobot.fragment;

import com.bayi.rerobot.R;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class newTaskFragment extends BaseFragment {


    private NewMain mainActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.new_task_dialog;
    }

    @Override
    protected void initView() {
     mainActivity=(NewMain)getActivity();

    }

    @Override
    protected void initData() {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){

        }
    }
}
