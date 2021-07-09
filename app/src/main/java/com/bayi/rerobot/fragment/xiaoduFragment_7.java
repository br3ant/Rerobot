package com.bayi.rerobot.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.AreaTaskAdapter;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.communication.Order;
import com.bayi.rerobot.communication.OrderManage;
import com.bayi.rerobot.communication.ProductCallback;
import com.bayi.rerobot.dialog.MoreDialog;
import com.bayi.rerobot.dialog.VarDialog;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.greendao.Task;
import com.bayi.rerobot.service.aiService;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.ItemOffsetDecoration;
import com.bayi.rerobot.util.LogUtil;
import com.bayi.rerobot.util.SocketType;
import com.bayi.rerobot.util.SpHelperUtil;
import com.bayi.rerobot.util.TaskType;
import com.iflytek.cloud.Setting;
import com.tobot.slam.SlamManager;
import com.tobot.slam.data.LocationBean;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class xiaoduFragment_7 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.main_radiogroup)
    RadioGroup radioGroup;
    @BindView(R.id.main_radiogroup_num)
    RadioGroup radioGroup_num;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    @BindView(R.id.main_radiobutton_num0)
    RadioButton Num0;
    @BindView(R.id.txt_num)
    TextView txtNum;
    @BindView(R.id.main_radiobutton_num1)
    RadioButton mainRadiobuttonNum1;
    @BindView(R.id.main_radiobutton_num5)
    RadioButton mainRadiobuttonNum5;
    @BindView(R.id.main_radiobutton_num10)
    RadioButton mainRadiobuttonNum10;
    @BindView(R.id.main_radiobutton_penwu)
    RadioButton mainRadiobuttonPenwu;
    @BindView(R.id.main_radiobutton_dianwei)
    RadioButton mainRadiobuttonDianwei;
    @BindView(R.id.main_radiobutton_xunluo)
    RadioButton mainRadiobuttonXunluo;
    @BindView(R.id.txt_area)
    TextView txtArea;
    private NewMain mainActivity;

    private int roundNum = 1;//循环次数
    private int ModeType = Contants.PENWU;//MOSHI
    private SpHelperUtil spHelperUtil;
    private List<SetAreaBean> areadatas = new ArrayList<>();
    private List<SetAreaBean> pointdatas = new ArrayList<>();
    private AreaTaskAdapter adapter;
    private int zdy1 = 0;
    private int zdy2 = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_xiaodu;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("智能消毒");
        spHelperUtil = new SpHelperUtil(getContext());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<SetAreaBean> setAreaBeans = App.getDaoSession().getSetAreaBeanDao().loadAll();
        for (int i = 0; i < setAreaBeans.size(); i++) {
            if (setAreaBeans.get(i).getIsDaolan()) {
                pointdatas.add(setAreaBeans.get(i));
            } else {
                areadatas.add(setAreaBeans.get(i));
            }
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        adapter = new AreaTaskAdapter(areadatas, getContext());
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                if (mainRadiobuttonDianwei.isChecked()) {
                    pointdatas.get(id).setIsSelected(!pointdatas.get(id).getIsSelected());
                    adapter.setNewData(pointdatas);
                } else {
                    areadatas.get(id).setIsSelected(!areadatas.get(id).getIsSelected());
                    adapter.setNewData(areadatas);
                }

            }
        });
        recyclerView.setAdapter(adapter);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_radiobutton_penwu:
                        ModeType = Contants.PENWU;
                        adapter.setNewData(areadatas);
                        txtNum.setText("循环次数");
                        mainRadiobuttonNum1.setText("1");
                        mainRadiobuttonNum5.setText("5");
                        mainRadiobuttonNum10.setText("10");
                        if(zdy1<1){
                            Num0.setText("自定义");
                        }else {
                            Num0.setText(String.valueOf(zdy1));
                        }
                        txtArea.setText("选择区域");
                        if(Num0.isChecked()){
                            roundNum=zdy1;
                        }
                        break;
                    case R.id.main_radiobutton_dianwei:
                        ModeType = Contants.DIANWEI;
                        adapter.setNewData(pointdatas);
                        txtNum.setText("消毒时长(单位:秒)");
                        mainRadiobuttonNum1.setText("60");
                        mainRadiobuttonNum5.setText("300");
                        mainRadiobuttonNum10.setText("600");
                        if(zdy2<1){
                            Num0.setText("自定义");
                        }else {
                            Num0.setText(String.valueOf(zdy2));
                        }
                        txtArea.setText("选择点位");
                        if(Num0.isChecked()){
                            roundNum=zdy2;
                        }
                        break;
                    case R.id.main_radiobutton_xunluo:
                        ModeType = Contants.XUNLUO;
                        adapter.setNewData(areadatas);
                        txtNum.setText("循环次数");
                        mainRadiobuttonNum1.setText("1");
                        mainRadiobuttonNum5.setText("5");
                        mainRadiobuttonNum10.setText("10");
                        txtArea.setText("选择区域");
                        if(zdy1<2){
                            Num0.setText("自定义");
                        }else {
                            Num0.setText(String.valueOf(zdy1));
                        }
                        if(Num0.isChecked()){
                            roundNum=zdy1;
                        }
                        break;

                    default:
                        break;

                }
                if (mainRadiobuttonNum1.isChecked()) {
                    roundNum = Integer.valueOf(mainRadiobuttonNum1.getText().toString());
                } else if (mainRadiobuttonNum5.isChecked()) {
                    roundNum = Integer.valueOf(mainRadiobuttonNum5.getText().toString());
                } else if (mainRadiobuttonNum10.isChecked()) {
                    roundNum = Integer.valueOf(mainRadiobuttonNum10.getText().toString());
                }

            }
        });
        radioGroup_num.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_radiobutton_num1:
                        roundNum = Integer.valueOf(mainRadiobuttonNum1.getText().toString());
                        break;

                    case R.id.main_radiobutton_num5:
                        roundNum = Integer.valueOf(mainRadiobuttonNum5.getText().toString());
                        break;
                    case R.id.main_radiobutton_num10:
                        roundNum = Integer.valueOf(mainRadiobuttonNum10.getText().toString());
                        break;
                    case R.id.main_radiobutton_num0:
                        if(mainRadiobuttonDianwei.isChecked()){
                            roundNum = zdy2;
                        }else {
                            roundNum = zdy1;
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            pointdatas.clear();
            areadatas.clear();
            List<SetAreaBean> setAreaBeans = App.getDaoSession().getSetAreaBeanDao().loadAll();
            for (int i = 0; i < setAreaBeans.size(); i++) {
                if (setAreaBeans.get(i).getIsDaolan()) {
                    pointdatas.add(setAreaBeans.get(i));
                } else {
                    areadatas.add(setAreaBeans.get(i));
                }
            }
            mainRadiobuttonPenwu.setChecked(true);
            adapter.setNewData(areadatas);
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.title_back, R.id.title_main, R.id.rdset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(0);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
            case R.id.rdset:
                skipToSetting(!mainRadiobuttonDianwei.isChecked());
                break;
        }
    }

    public void skipToSetting(boolean b) {
        final int[] i = {0};
         if(b){
             i[0] =zdy1;
         } else {
             i[0] =zdy2;
         }
         VarDialog dialog = new VarDialog(getContext(), String.valueOf(i[0]), new
                VarDialog.varDialogListener() {
                    @Override
                    public void getValue(String p1, View view) {
                        try {
                            i[0] = Integer.valueOf(p1);
                            if (i[0] < 1) {
                                i[0] = 0;

                                Num0.setText("自定义");
                            } else {
                                Num0.setText(p1);
                            }
                            roundNum = i[0];
                            if(b){
                                zdy1= i[0];
                            }else{
                                zdy2= i[0];
                            }
                        } catch (Exception e) {
                            toast("请输入数字");
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

    @OnClick({R.id.btn_startTask, R.id.btn_stop, R.id.view_timer, R.id.view_chongdian, R.id.view_water, R.id.view_more})
    public void onClick1(View view) {
        switch (view.getId()) {
            case R.id.btn_startTask:
                aiService.isPW = false;
                switch (ModeType) {
                    case Contants.PENWU:
                        LogUtil.saveLog("人工下区域喷雾");
                        NewTask(0, areadatas);
                        break;
                    case Contants.DIANWEI:
                        LogUtil.saveLog("人工下点位喷雾");
                        NewTask(1, pointdatas);
                        break;
                    case Contants.XUNLUO://xunluo
                        LogUtil.saveLog("人工下发区域巡逻任务");
                        NewTask(2, areadatas);

                        break;

                    default:
                        break;
                }
                break;
            case R.id.btn_stop:
                aiService.isPW = false;
                tasklist.clear();
                App.getDaoSession().getTaskDao().deleteAll();
                sendsocket(SocketType.StopPenwu);
                aiService.sleepTime = 0;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SlamManager.getInstance().cancelAction();
                    }
                }).start();
                break;
            case R.id.view_timer:
                mainActivity.setFragmentView(12);
                break;
            case R.id.view_chongdian:
                aiService.isPW = false;
                App.getDaoSession().getTaskDao().deleteAll();
                aiService.sleepTime = 0;
                tasklist.clear();
                App.toast("正在去充电");
                EventBus.getDefault().post("充电");
                sendsocket(SocketType.StopPenwu);
                mainActivity.setFragmentView(0);
                break;
            case R.id.view_water:
                LogUtil.saveLog("加水");
                EventBus.getDefault().post("加水");
                mainActivity.setFragmentView(0);
                break;
            case R.id.view_more:
                //  mainActivity.setFragmentView(8);
                MoreDialog.Builder b = new MoreDialog.Builder(getContext(), new OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(int id) {
                        switch (id) {
                            case 0:
                                mainActivity.setFragmentView(8);
                                break;
                            case 1:
                                mainActivity.setFragmentView(19);
                                break;
                            case 2:
                                mainActivity.setFragmentView(17);
                                break;
                        }

                    }
                });
                b.create().show();
                break;
        }
    }

    private List<Task> tasklist = new ArrayList<>();

    private void NewTask(int isPenwu, List<SetAreaBean> datas) {

        toast("正在开始任务。。。");
        App.getDaoSession().getTaskDao().deleteAll();
        aiService.sleepTime = 0;
        tasklist.clear();
        if(roundNum==0){
            toast("无效参数设置");
            return;
        }
        if (isPenwu == 0 || isPenwu == 2) {
            for (int i = 0; i < roundNum; i++) {
                for (int j = 0; j < datas.size(); j++) {
                    if (datas.get(j).getIsSelected()) {
                        List<LocationBean> strings = datas.get(j).getData();
                        for (int z = 0; z < strings.size(); z++) {
                            Task task = new Task();
                            task.setTargetname(strings.get(z));
                            tasklist.add(task);
                        }
                    }
                }
            }
        } else {
            aiService.sleepTime =  roundNum;
            for (int j = 0; j < datas.size(); j++) {
                if (datas.get(j).getIsSelected()) {
                    List<LocationBean> strings = datas.get(j).getData();
                    for (int z = 0; z < strings.size(); z++) {
                        Task task = new Task();
                        task.setTargetname(strings.get(z));
                        tasklist.add(task);
                    }
                }
            }

        }
        Log.e("ssssss----->",roundNum+"");
        App.getDaoSession().getTaskDao().insertOrReplaceInTx(tasklist);


        sendsocket(SocketType.blue);
        String water = (String) spHelperUtil.getSharedPreference("water", "中水位");


        if (isPenwu == 0 || isPenwu == 1) {
            if (water.equals("低水位")) {
                toast("当前喷雾消毒水较少,无法执行任务");
                App.getDaoSession().getTaskDao().deleteAll();
                aiService.sleepTime = 0;
                return;
            }
            aiService.isPW = true;
            sendsocket(SocketType.PENWU);
            App.taskType = TaskType.Task;
        } else {
            aiService.isPW = false;
        }
        toast("开始执行");

        if (tasklist.size() == 0) {
            toast("请添加区域位置");//不选为全部
        } else {
            aiService.index = 0;
            EventBus.getDefault().post("任务");
        }
    }

    private static void sendsocket(int type) {
        switch (type) {
            case SocketType.PENWU:

                try {
                    OrderManage.getInstance().startPenWu(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {


                            if (state == ProductCallback.Sur) {
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.PENWU);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.StopPenwu:
                try {
                    OrderManage.getInstance().stopPenWu(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {


                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.StopPenwu);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.red:
                try {
                    OrderManage.getInstance().red_open(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {

                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.red);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.blue:
                try {
                    OrderManage.getInstance().blue_open(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {

                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.blue);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.green:
                try {
                    OrderManage.getInstance().green_open(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {

                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.green);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;


        }

    }
}
