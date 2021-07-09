package com.bayi.rerobot.fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.dialog.VarDialog;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.update.UpdateManager;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.SpHelperUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class setFragment_8 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.txt_net)
    TextView txtNet;
    @BindView(R.id.txt_iot)
    TextView txtIot;
    @BindView(R.id.txt_ouautn)
    TextView txtOuautn;
    @BindView(R.id.txt_cewenip)
    TextView txtCewenip;
    @BindView(R.id.txt_mima)
    TextView txtMima;
    @BindView(R.id.txt_update)
    TextView txtUpdate;
    @BindView(R.id.txt_high)
    TextView txtHigh;
    @BindView(R.id.txt_low)
    TextView txtLow;
    @BindView(R.id.txt_chushi)
    TextView txtChushi;
    private NewMain mainActivity;
    public static int flag = 0;// 0是导览 其他是区域点位
    private SpHelperUtil spHelperUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_set;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("设置");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spHelperUtil = new SpHelperUtil(getContext());
        manager = new UpdateManager(getContext());
        txtNet.setText(String.format("%s:%s",
                (String) spHelperUtil.getSharedPreference(Contants.IT_IP, "http://115.231.60.194"), (String) spHelperUtil.getSharedPreference(Contants.IT_PORT, "9090")));
        txtIot.setText(String.format("%s:%s",
                (String) spHelperUtil.getSharedPreference(Contants.IOT_IP, "115.231.60.194"), (String) spHelperUtil.getSharedPreference(Contants.IOT_PORT, "23")));
        txtCewenip.setText(String.format("%s:%s",
                (String) spHelperUtil.getSharedPreference(Contants.CEWEN_IP, "http://223.94.36.172"), (String) spHelperUtil.getSharedPreference(Contants.CEWEN_PORT, "8105")));
        txtOuautn.setText((String) spHelperUtil.getSharedPreference(Contants.IOT_osautn, "60da7bdd-78e4-4b39-9f0f-11b20543cc2c"));
        txtMima.setText((String) spHelperUtil.getSharedPreference(Contants.MIMA, "7656"));
        txtUpdate.setText(getVerName(getContext()));
        txtHigh.setText((String)spHelperUtil.getSharedPreference(Contants.highCharge,"90"));
        txtLow.setText((String)spHelperUtil.getSharedPreference(Contants.lowCharge,"30"));
        txtChushi.setText((String)spHelperUtil.getSharedPreference(Contants.startName,"a1"));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            txtUpdate.setText(getVerName(getContext()));
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
                mainActivity.setFragmentView(7);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
        }
    }


    @OnClick({R.id.lin_net, R.id.lin_iot, R.id.lin_cewenip, R.id.lin_mima, R.id.lin_navigation,
            R.id.lin_xiaodu, R.id.lin_osautn, R.id.lin_map, R.id.lin_update, R.id.lin_high,
            R.id.lin_low, R.id.lin_chushi})
    public void onClick1(View view) {
        switch (view.getId()) {
            case R.id.lin_net:
                skipToSetting(1);
                break;
            case R.id.lin_iot:
                skipToSetting(2);
                break;
            case R.id.lin_osautn:
                skipToSetting(3);
                break;
            case R.id.lin_cewenip:
                skipToSetting(4);
                break;
            case R.id.lin_mima:
                skipToSetting(5);
                break;
            case R.id.lin_high:
                skipToSetting(6);
                break;
            case R.id.lin_low:
                skipToSetting(7);
                break;
            case R.id.lin_chushi:
                skipToSetting(8);
                break;
            case R.id.lin_navigation:
                flag = 0;
                mainActivity.setFragmentView(9);
                break;
            case R.id.lin_xiaodu:
                flag = 1;
                mainActivity.setFragmentView(9);
                break;
            case R.id.lin_map:
                mainActivity.setFragmentView(15);
                break;
            case R.id.lin_update:
                new UpdateAsyncTask(getContext()).execute();
                break;
        }
    }

    public void skipToSetting(int i) {
        StringBuilder sb = new StringBuilder();
        if (i == 1) {
            sb.append((String) spHelperUtil.getSharedPreference(Contants.IT_IP, "http://115.231.60.194"));
            sb.append(":");
            sb.append((String) spHelperUtil.getSharedPreference(Contants.IT_PORT, "9090"));
        } else if (i == 2) {
            sb.append((String) spHelperUtil.getSharedPreference(Contants.IOT_IP, "115.231.60.194"));
            sb.append(":");
            sb.append((String) spHelperUtil.getSharedPreference(Contants.IOT_PORT, "23"));
        } else if(i==3){
            sb.append((String) spHelperUtil.getSharedPreference(Contants.IOT_osautn, "60da7bdd-78e4-4b39-9f0f-11b20543cc2c"));
        }else if (i == 4) {
            sb.append((String) spHelperUtil.getSharedPreference(Contants.CEWEN_IP, "http://223.94.36.172"));
            sb.append(":");
            sb.append((String) spHelperUtil.getSharedPreference(Contants.CEWEN_PORT, "8105"));
        } else if (i == 5) {
            sb.append((String) spHelperUtil.getSharedPreference(Contants.MIMA, "7656"));
        }else if(i==6){
            sb.append((String) spHelperUtil.getSharedPreference(Contants.highCharge, "90"));
        }else if(i==7){
            sb.append((String) spHelperUtil.getSharedPreference(Contants.lowCharge, "30"));
        }else if(i==8){
            sb.append((String) spHelperUtil.getSharedPreference(Contants.startName, "a1"));
        }
        VarDialog dialog = new VarDialog(getContext(), sb.toString(), new
                VarDialog.varDialogListener() {
                    @Override
                    public void getValue(String p1, View view) {
                        try {
                            String[] ss = p1.split(":");
                            if (i == 1) {
                                spHelperUtil.put(Contants.IT_IP, ss[0].replace("http", "http:") + ss[1]);
                                spHelperUtil.put(Contants.IT_PORT, ss[2]);
                                txtNet.setText(p1);
                            } else if (i == 2) {
                                spHelperUtil.put(Contants.IOT_IP, ss[0]);
                                spHelperUtil.put(Contants.IOT_PORT, ss[1]);
                                txtIot.setText(p1);
                            } else if(i==3){
                                spHelperUtil.put(Contants.IOT_osautn, p1);
                                txtOuautn.setText(p1);
                            }else if (i == 4) {
                                spHelperUtil.put(Contants.CEWEN_IP, ss[0]);
                                spHelperUtil.put(Contants.CEWEN_PORT, ss[1]);
                                txtCewenip.setText(p1);
                            } else if (i == 5) {
                                try {
                                    int s = Integer.valueOf(p1);
                                } catch (Exception e) {
                                    toast("请设置四位数字密码");
                                    return;
                                }
                                if (p1.length() == 4) {
                                    spHelperUtil.put(Contants.MIMA, p1);
                                    txtMima.setText(p1);
                                } else {
                                    toast("请设置四位数字密码");
                                }
                            }else if(i==6){
                                try {
                                    int s = Integer.valueOf(p1);
                                } catch (Exception e) {
                                    toast("请设置数字");
                                    return;
                                }
                                spHelperUtil.put(Contants.highCharge, p1);
                                txtHigh.setText(p1);

                            }else if(i==7){
                                try {
                                    int s = Integer.valueOf(p1);
                                } catch (Exception e) {
                                    toast("请设置数字");
                                    return;
                                }
                                spHelperUtil.put(Contants.lowCharge ,p1);
                                txtLow.setText(p1);

                            }else if(i==8){
                                spHelperUtil.put(Contants.startName ,p1);
                                txtChushi.setText(p1);
                            }
                        } catch (Exception e) {
                            toast("请输入网址");
                        }

                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context
                                .INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        }

                    }
                });
        dialog.createcheckDialog();
    }

    private UpdateManager manager;

    class UpdateAsyncTask extends AsyncTask<String, Integer, String> {
        Context context;

        public UpdateAsyncTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... arg0) {
            if (manager.isUpdate()) {
                return "有新版本";
            } else {
                return "无新版本";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("有新版本")) {
                App.toast("正在下载新版本apk");
                manager.downloadAPK();
            } else {
                App.toast("已是最新版apk,无需更新");
            }
        }
    }

    private String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }
}
