package com.bayi.rerobot.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.bean.ListDataSave;
import com.bayi.rerobot.service.aiService;
import com.bayi.rerobot.tobot.BaseConstant;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.SpHelperUtil;
import com.tobot.slam.SlamManager;
import com.tobot.slam.agent.listener.OnFinishListener;
import com.tobot.slam.agent.listener.OnResultListener;
import com.tobot.slam.data.LocationBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SetActivity extends BaseActivity {

    @BindView(R.id.edit)
    EditText edit;
    @BindView(R.id.charge)
    EditText charge;
    @BindView(R.id.startname)
    EditText startname;
    @BindView(R.id.mapname)
    EditText mapname;

    String MmapName;
    @BindView(R.id.btn)
    Button btn;
    private SpHelperUtil spHelperUtil;
    private ListDataSave dataSave;

    @Override
    public void initView() {
        setContentView(R.layout.activity_set);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        spHelperUtil = new SpHelperUtil(SetActivity.this);

        dataSave = new ListDataSave(SetActivity.this, "myLocationBean");
    }


    @Override
    protected void onResume() {
        super.onResume();
        MmapName = (String) spHelperUtil.getSharedPreference(Contants.mapName, "ceshi");
        startname.setText((String) spHelperUtil.getSharedPreference(Contants.startName, "a1"));
        charge.setText((String) spHelperUtil.getSharedPreference(Contants.CHARGE, "30"));
        mapname.setText(MmapName);


    }

    @Override
    protected void onPause() {
        super.onPause();
        App.currentMap = mapname.getText().toString().trim();
        spHelperUtil.put(Contants.startName, startname.getText().toString().trim());
        spHelperUtil.put(Contants.CHARGE, charge.getText().toString().trim());
        spHelperUtil.put(Contants.mapName, mapname.getText().toString().trim());

    }

    @OnClick(R.id.btn)
    public void onViewClicked() {
        toast("重定位");
        new Thread(new Runnable() {
            @Override
            public void run() {
                SlamManager.getInstance().recoverLocationByDefault(new OnResultListener<Boolean>() {
                    @Override
                    public void onResult(Boolean data) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (data) {
                                    toast("重定位成功");
                                } else {
                                    toast("重定位失败");
                                }
                            }
                        });
                    }
                });
            }
        }).start();
    }

    @OnClick(R.id.changmap)
    public void onViewClicked1() {
        App.currentMap = mapname.getText().toString().trim();

        Log.e("setactivity", BaseConstant.getMapNumPath(SetActivity.this, App.currentMap));

        List<String> mapPath = new ArrayList<>();
        mapPath.add(BaseConstant.getMapNumPath(SetActivity.this, App.currentMap));
        SlamManager.getInstance().requestAllLocationInThread(mapPath, new OnResultListener<List<LocationBean>>() {
            @Override
            public void onResult(List<LocationBean> locationBeans) {
                dataSave.setDataList(Contants.LISTBEAN, locationBeans);
            }
        });
    }


    @OnClick({R.id.btn_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.btn_close:
                this.finish();
                break;
        }
    }


    @OnClick(R.id.loadmap)
    public void onViewClicked2() {
        App.currentMap = mapname.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                SlamManager.getInstance().loadMapInThread(BaseConstant.getMapNumPath(SetActivity.this, App.currentMap), new OnFinishListener<List<LocationBean>>() {
                    @Override
                    public void onFinish(List<LocationBean> data) {

                        // relocation();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataSave.setDataList(Contants.LISTBEAN,data);
                                toast("加载地图成功");
                            }
                        });


                    }

                    @Override
                    public void onError () {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                toast("加载地图失败");
                            }
                        });

                    }
                });
            }
        }).start();

    }

}
