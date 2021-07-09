package com.bayi.rerobot.fragment;

import android.view.View;
import android.widget.TextView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class consultFragment_1 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    private NewMain mainActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_consult;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("咨询问答");
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {

        }
    }


    @OnClick({R.id.title_back, R.id.title_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(0);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
        }
    }
}
