package com.bayi.rerobot.fragment;

import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.tabsAdapter;
import com.bayi.rerobot.ui.BaseFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/6
 */
public class detectionFragment extends BaseFragment {
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.viewpager)
    ViewPager viewpager;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_detection;
    }
     private tabsAdapter pagerAdapter;
    @Override
    protected void initView() {


         pagerAdapter = new tabsAdapter(getActivity().getSupportFragmentManager(), getActivity());
        viewpager.setAdapter(pagerAdapter);
        tabs.setupWithViewPager(viewpager);
        tabs.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.img_back, R.id.img_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
               // mainActivity.setFragmentView(1);
                break;
            case R.id.img_main:
               // mainActivity.setFragmentView(0);
                break;
        }
    }
    private tabsFragment tabsFragment;
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
           pagerAdapter.notifyDataSetChanged();
        }
    }

}
