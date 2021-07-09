package com.bayi.rerobot.fragment;

import android.view.View;
import android.widget.TextView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.ImageAdapter;
import com.bayi.rerobot.bean.DataBean;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.youth.banner.Banner;
import com.youth.banner.config.BannerConfig;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.util.BannerUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class bookstoreFragment_2 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.banner)
    Banner banner;
    private NewMain mainActivity;
    public static int BookType;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookstore;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("图书查询");
    }

    @Override
    protected void initData() {
        useBanner();
        banner.start();
    }
    private void useBanner() {
        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(new ImageAdapter(DataBean.getData1()))
                .setIndicatorMargins(new IndicatorConfig.Margins(0, 0,
                        BannerConfig.INDICATOR_MARGIN, (int) BannerUtils.dp2px(12)))

                .setIndicator(new CircleIndicator(getActivity()));
        banner.setLoopTime(10000);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            banner.start();
        }else {
            banner.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        banner.destroy();
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

    @OnClick({R.id.view_bookname, R.id.view_bookauthor, R.id.view_bookkeywords})
    public void onClick1(View view) {
        switch (view.getId()) {
            case R.id.view_bookname:
                BookType = 0;
                break;
            case R.id.view_bookauthor:
                BookType = 1;
                break;
            case R.id.view_bookkeywords:
                BookType = 2;
                break;
        }
        mainActivity.setFragmentView(3);
    }
}
