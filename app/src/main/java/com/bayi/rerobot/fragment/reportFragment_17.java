package com.bayi.rerobot.fragment;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.utilView.PieView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by tongzn
 * on 2020/5/4
 */
public class reportFragment_17 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.pv1)
    PieView pv1;
    @BindView(R.id.pv2)
    PieView pv2;
    private NewMain mainActivity;
    private List<Float> a= Arrays.asList(0.1f,0.45f,0.05f,0.40f);
    private List<Integer> b= Arrays.asList(Color.RED, Color.BLUE, Color.YELLOW, Color.GRAY);

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_report;
    }

    @Override
    protected void initView() {

        mainActivity = (NewMain) getActivity();
        titleText.setText("报表");

    }

    @Override
    protected void initData() {
        super.initData();
       pv1.updateDate(a,b,true);
       pv2.updateDate(a,b,false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {

        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

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
