package com.bayi.rerobot.fragment;

import android.view.View;
import android.widget.TextView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.gen.SetBeanDao;
import com.bayi.rerobot.greendao.SetBean;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.SpHelperUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bayi.rerobot.util.LogUtil.ymdFormat;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class ambientFragment extends BaseFragment {
    @BindView(R.id.text_tem)
    TextView textTem;
    @BindView(R.id.text_hum)
    TextView textHum;
    @BindView(R.id.text_co2)
    TextView textCo2;
    @BindView(R.id.text_pm25)
    TextView textPm25;
    @BindView(R.id.text_vvoc)
    TextView textVvoc;
    @BindView(R.id.text_pm10)
    TextView textPm10;
    @BindView(R.id.text_temp)
    TextView textTemp;
    @BindView(R.id.text_num)
    TextView textNum;
    @BindView(R.id.text_high)
    TextView textHigh;
    @BindView(R.id.text_low)
    TextView textLow;

    private NewMain mainActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ambient;
    }

    @Override
    protected void initView() {

        mainActivity = (NewMain) getActivity();

    }

    @Override
    protected void initData() {

            Map map = SpHelperUtil.getHashMapData(getActivity(), Contants.SYSYTEMDATA);

            textTem.setText(map.get("tem") + "");
            textHum.setText(map.get("hum") + "");
            textCo2.setText(map.get("co2") + "");
            textPm10.setText(map.get("pm10") + "");
            textPm25.setText(map.get("pm25") + "");
            textVvoc.setText(map.get("vvoc") + "");
            refreahUi(map);

    }

    private void refreahUi(Map map) {
        try {

        textTemp.setText(map.get("tem") + "℃");
        float vvoc = (float) map.get("vvoc");
        float pm25 = (float) map.get("pm25");
        int num = 0;
        if (vvoc < 0.08) {
            num += 3;
        } else if (num < 0.4) {
            num += 2;
        } else {
            num += 1;
        }
        if (pm25 < 3) {
            num += 3;
        } else if (pm25 < 5) {
            num += 2;
        } else {
            num += 1;
        }
        if (num > 4) {
            textNum.setText("环境质量优");
        } else if (num > 3) {
            textNum.setText("环境质量良好");
        } else
            textNum.setText("环境质量一般");

        Date date = new Date(System.currentTimeMillis());
        String ymd=ymdFormat.format(date);
        List<SetBean>  setBeans= App.getDaoSession().getSetBeanDao().queryBuilder().where(SetBeanDao.Properties.Ymd.eq(ymd)).orderAsc(SetBeanDao.Properties.Tem).list();
            textLow.setText(setBeans.get(0).getTem()+"℃");
            textHigh.setText(setBeans.get(setBeans.size()-1).getTem()+"℃");
        }catch (Exception e){}

    }

    @OnClick({R.id.img_back, R.id.img_main, R.id.view_main, R.id.view_tem, R.id.view_co2, R.id.view_vvoc, R.id.view_hum, R.id.view_pm25, R.id.view_pm10})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                mainActivity.setFragmentView(0);
                break;
            case R.id.img_main:
                mainActivity.setFragmentView(0);
                break;
            case R.id.view_main:
                break;
            case R.id.view_tem:
                mainActivity.setFragmentView(3);
                break;
            case R.id.view_co2:
                mainActivity.setFragmentView(3);
                break;
            case R.id.view_vvoc:
                mainActivity.setFragmentView(3);
                break;
            case R.id.view_hum:
                mainActivity.setFragmentView(3);
                break;
            case R.id.view_pm25:
                mainActivity.setFragmentView(3);
                break;
            case R.id.view_pm10:
                mainActivity.setFragmentView(3);
                break;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Map map = SpHelperUtil.getHashMapData(getActivity(), Contants.SYSYTEMDATA);
            textTem.setText(map.get("tem") + "");
            textHum.setText(map.get("hum") + "");
            textCo2.setText(map.get("co2") + "");
            textPm10.setText(map.get("pm10") + "");
            textPm25.setText(map.get("pm25") + "");
            textVvoc.setText(map.get("vvoc") + "");
            refreahUi(map);
        }
    }
}
