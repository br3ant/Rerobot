package com.bayi.rerobot.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.newAreaTaskAdapter;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class ambientFragment_18 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.area_recycle)
    RecyclerView recycle;
    private NewMain mainActivity;
    private newAreaTaskAdapter adapter;
    private List<SetAreaBean> setAreaBeans;
    private List<SetAreaBean> datas=new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ambient1;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("环境数据");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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

    @OnClick({R.id.title_back, R.id.title_main,R.id.add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(8);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
            case R.id.add:
                mainActivity.setFragmentView(10);
                break;
        }
    }


}
