package com.bayi.rerobot.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.AreaTaskAdapter;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.dialog.VarDialog;
import com.bayi.rerobot.greendao.AddTimer;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.ItemOffsetDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class timer3Fragment_14 extends BaseFragment implements CompoundButton.OnCheckedChangeListener {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.btn_week1)
    CheckBox week1;
    @BindView(R.id.btn_week2)
    CheckBox week2;
    @BindView(R.id.btn_week3)
    CheckBox week3;
    @BindView(R.id.btn_week4)
    CheckBox week4;
    @BindView(R.id.btn_week5)
    CheckBox week5;
    @BindView(R.id.btn_week6)
    CheckBox week6;
    @BindView(R.id.btn_week7)
    CheckBox week7;
    @BindView(R.id.radiogroup1)
    RadioGroup radioGroup1;
    @BindView(R.id.main_radiogroup_num)
    RadioGroup radioGroup_num;
    @BindView(R.id.radiobutton1_penwu)
    RadioButton radiobutton1_penwu;
    @BindView(R.id.radiobutton1_xunluo)
    RadioButton radiobutton1_xunluo;
    @BindView(R.id.radiobutton1_dianwei)
    RadioButton radiobutton1_dianwei;
    @BindView(R.id.main_radiobutton_num1)
    RadioButton main_radiobutton_num1;
    @BindView(R.id.main_radiobutton_num0)
    RadioButton main_radiobutton_num0;
    @BindView(R.id.main_radiobutton_num5)
    RadioButton main_radiobutton_num5;
    @BindView(R.id.main_radiobutton_num10)
    RadioButton main_radiobutton_num10;
    @BindView(R.id.text_time)
    TextView text_time;
    @BindView(R.id.timepicker)
    TimePicker mTimepicker;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;
    @BindView(R.id.radiogroup_main)
    RadioGroup group_main;
    @BindView(R.id.btn_once)
    RadioButton once;
    @BindView(R.id.delete)
    Button delete;
    @BindView(R.id.btn_everyday)
    RadioButton everyday;
    @BindView(R.id.txt_num)
    TextView txtNum;
    @BindView(R.id.txt_area)
    TextView txtArea;
    private NewMain mainActivity;

    public AddTimer addTimer;
    private AreaTaskAdapter adapter;
    private List<SetAreaBean> setAreaBeans;
    SimpleDateFormat st = new SimpleDateFormat("HH:mm");
    private int roundNum = 1;//循环次数
    private int ModeType = Contants.PENWU;//MOSHI
    private int zdy1 = 0;
    private int zdy2 = 0;
    private List<SetAreaBean> areadatas = new ArrayList<>();
    private List<SetAreaBean> pointdatas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timer2;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("编辑定时任务");
    }

    public void setData(AddTimer addTimer1) {
        this.addTimer = addTimer1;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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
        week1.setOnCheckedChangeListener(this);
        week2.setOnCheckedChangeListener(this);
        week3.setOnCheckedChangeListener(this);
        week4.setOnCheckedChangeListener(this);
        week5.setOnCheckedChangeListener(this);
        week6.setOnCheckedChangeListener(this);
        week7.setOnCheckedChangeListener(this);
        for (int i = 0; i < setAreaBeans.size(); i++) {
            setAreaBeans.get(i).setIsSelected(false);
        }
        for (int i = 0; i < setAreaBeans.size(); i++) {
            for (int j = 0; j < addTimer.getAreaNameList().size(); j++) {
                if (setAreaBeans.get(i).getAreaname().equals(addTimer.getAreaNameList().get(j))) {
                    setAreaBeans.get(i).setIsSelected(true);
                }
            }
        }
        mTimepicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);  //设置点击事件不弹键盘
        mTimepicker.setIs24HourView(true);   //设置时间显示为24小时
        mTimepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                try {
                    text_time.setText(st.format(st.parse(String.format("%s:%s", hourOfDay, minute))));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        delete.setVisibility(View.VISIBLE);
        Log.e("getTime", addTimer.getTime());
        String times[] = addTimer.getTime().split(":");
        mTimepicker.setHour(Integer.valueOf(times[0]));  //设置当前小时
        mTimepicker.setMinute(Integer.valueOf(times[1])); //设置当前分（0-59）
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        recyclerView.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        if(addTimer.getModetype()==Contants.DIANWEI){
            adapter = new AreaTaskAdapter(pointdatas, getContext());
        }else {
        adapter = new AreaTaskAdapter(areadatas, getContext());
        }
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                if(radiobutton1_dianwei.isChecked()){
                    pointdatas.get(id).setIsSelected(!pointdatas.get(id).getIsSelected());
                    adapter.setNewData(pointdatas);
                }else {
                    areadatas.get(id).setIsSelected(!areadatas.get(id).getIsSelected());
                    adapter.setNewData(areadatas);

                }
            }
        });
        recyclerView.setAdapter(adapter);
        radioGroup_num.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.main_radiobutton_num1:
                        roundNum = Integer.valueOf(main_radiobutton_num1.getText().toString());
                        break;

                    case R.id.main_radiobutton_num5:
                        roundNum = Integer.valueOf(main_radiobutton_num5.getText().toString());
                        break;
                    case R.id.main_radiobutton_num10:
                        roundNum = Integer.valueOf(main_radiobutton_num10.getText().toString());
                        break;
                    case R.id.main_radiobutton_num0:
                        if(radiobutton1_dianwei.isChecked()){
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

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobutton1_penwu:
                        ModeType = Contants.PENWU;
                        adapter.setNewData(areadatas);
                        txtNum.setText("循环次数");
                        main_radiobutton_num1.setText("1");
                        main_radiobutton_num5.setText("5");
                        main_radiobutton_num10.setText("10");
                        if (zdy1 < 1) {
                            main_radiobutton_num0.setText("自定义");
                        } else {
                            main_radiobutton_num0.setText(String.valueOf(zdy1));
                        }
                        txtArea.setText("选择区域");
                        if (main_radiobutton_num0.isChecked()) {
                            roundNum = zdy1;
                        }
                        break;
                    case R.id.radiobutton1_dianwei:
                        ModeType = Contants.DIANWEI;
                        adapter.setNewData(pointdatas);
                        txtNum.setText("消毒时长(单位:秒)");
                        main_radiobutton_num1.setText("60");
                        main_radiobutton_num5.setText("300");
                        main_radiobutton_num10.setText("600");
                        if (zdy2 < 1) {
                            main_radiobutton_num0.setText("自定义");
                        } else {
                            main_radiobutton_num0.setText(String.valueOf(zdy2));
                        }
                        txtArea.setText("选择点位");
                        if (main_radiobutton_num0.isChecked()) {
                            roundNum = zdy2;
                        }
                        break;
                    case R.id.radiobutton1_xunluo:
                        ModeType = Contants.XUNLUO;
                        adapter.setNewData(areadatas);
                        txtNum.setText("循环次数");
                        main_radiobutton_num1.setText("1");
                        main_radiobutton_num5.setText("5");
                        main_radiobutton_num10.setText("10");
                        txtArea.setText("选择区域");
                        if (zdy1 < 2) {
                            main_radiobutton_num0.setText("自定义");
                        } else {
                            main_radiobutton_num0.setText(String.valueOf(zdy1));
                        }
                        if (main_radiobutton_num0.isChecked()) {
                            roundNum = zdy1;
                        }
                        break;
                }
                if (main_radiobutton_num1.isChecked()) {
                    roundNum = Integer.valueOf(main_radiobutton_num1.getText().toString());
                } else if (main_radiobutton_num5.isChecked()) {
                    roundNum = Integer.valueOf(main_radiobutton_num5.getText().toString());
                } else if (main_radiobutton_num10.isChecked()) {
                    roundNum = Integer.valueOf(main_radiobutton_num10.getText().toString());
                }
            }
        });
        setData();

    }
     private void setData(){
         String week = addTimer.getWeek();
         switch (week) {
             case "once":
                 once.setChecked(true);
                 everyday.setChecked(false);
                 break;
             case "everyday":
                 once.setChecked(false);
                 everyday.setChecked(true);
                 break;
             default:
                 group_main.setVisibility(View.GONE);
                 char[] chars = week.toCharArray();
                 for (int i = 0; i < chars.length; i++) {
                     switch (chars[i]) {
                         case '1':
                             week1.setChecked(true);
                             break;
                         case '2':
                             week2.setChecked(true);
                             break;
                         case '3':
                             week3.setChecked(true);
                             break;
                         case '4':
                             week4.setChecked(true);
                             break;
                         case '5':
                             week5.setChecked(true);
                             break;
                         case '6':
                             week6.setChecked(true);
                             break;
                         case '7':
                             week7.setChecked(true);
                             break;

                     }
                 }
                 break;
         }
         if (addTimer.getModetype() == Contants.PENWU) {
             radiobutton1_penwu.setChecked(true);
             switch (addTimer.getRoundnum()) {
                 case 1:
                     main_radiobutton_num1.setChecked(true);
                     break;
                 case 5:
                     main_radiobutton_num5.setChecked(true);
                     break;
                 case 10:
                     main_radiobutton_num10.setChecked(true);
                     break;
                 default:
                     main_radiobutton_num0.setChecked(true);
                     main_radiobutton_num0.setText(String.valueOf(addTimer.getRoundnum()));
                     zdy1=addTimer.getRoundnum();
                     break;
             }
         } else if(addTimer.getModetype() == Contants.DIANWEI){
             radiobutton1_dianwei.setChecked(true);
             switch (addTimer.getRoundnum()) {
                 case 60:
                     main_radiobutton_num1.setChecked(true);
                     break;
                 case 300:
                     main_radiobutton_num5.setChecked(true);
                     break;
                 case 600:
                     main_radiobutton_num10.setChecked(true);
                     break;
                 default:
                     main_radiobutton_num0.setChecked(true);
                     main_radiobutton_num0.setText(String.valueOf(addTimer.getRoundnum()));
                     zdy2=addTimer.getRoundnum();
                     break;
             }
         }else if((addTimer.getModetype() == Contants.XUNLUO)){
             radiobutton1_xunluo.setChecked(true);
             switch (addTimer.getRoundnum()) {
                 case 1:
                     main_radiobutton_num1.setChecked(true);
                     break;
                 case 5:
                     main_radiobutton_num5.setChecked(true);
                     break;
                 case 10:
                     main_radiobutton_num10.setChecked(true);
                     break;
                 default:
                     main_radiobutton_num0.setChecked(true);
                     main_radiobutton_num0.setChecked(true);
                     main_radiobutton_num0.setText(String.valueOf(addTimer.getRoundnum()));
                     zdy1=addTimer.getRoundnum();
                     break;
             }
         }

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
            if((addTimer.getModetype() == Contants.DIANWEI)){
            adapter.setNewData(pointdatas);
            }else{
                adapter.setNewData(areadatas);
            }
            setData();
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.title_back, R.id.title_main, R.id.text_time, R.id.delete, R.id.txt_cf,R.id.rdset})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(12);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
            case R.id.text_time:
                if (mTimepicker.getVisibility() != View.VISIBLE) {
                    mTimepicker.setVisibility(View.VISIBLE);
                } else {
                    mTimepicker.setVisibility(View.GONE);
                }
                break;
            case R.id.rdset:
                skipToSetting(!radiobutton1_dianwei.isChecked());
                break;
            case R.id.delete:
                App.getDaoSession().getAddTimerDao().delete(addTimer);
                toast("删除成功");
                mainActivity.setFragmentView(12);
                break;
            case R.id.txt_cf:
                if(roundNum==0){
                    toast("自定义参数设置错误");
                    return;
                }
                if (group_main.getVisibility() == View.VISIBLE) {
                    if (once.isChecked()) {
                        week = "once";
                    } else {
                        week = "everyday";
                    }
                } else {
                    week = week();
                }

                AddTimer addtimer = new AddTimer();
                addtimer.setModetype(ModeType);
                addtimer.setRoundnum(roundNum);
                addtimer.setId(addTimer.getId());
                addtimer.setTime(text_time.getText().toString().trim());
                addtimer.setWeek(week);
                List<String> arenameList = new ArrayList<>();
                if(radiobutton1_dianwei.isChecked()){
                    for (int i = 0; i < pointdatas.size(); i++) {
                        if (pointdatas.get(i).getIsSelected()) {
                            arenameList.add(pointdatas.get(i).getAreaname());
                        }
                    }
                }else {
                    for (int i = 0; i < areadatas.size(); i++) {
                        if (areadatas.get(i).getIsSelected()) {
                            arenameList.add(areadatas.get(i).getAreaname());
                        }
                    }
                }
                if (arenameList.size() == 0) {
                    App.toast("请添加消毒区域");
                    return;
                }
                addtimer.setAreaNameList(arenameList);
                App.getDaoSession().getAddTimerDao().update(addtimer);
                toast("保存成功");
                mainActivity.setFragmentView(12);
                break;
        }
    }

    private String week = "";

    private String week() {
        StringBuffer sp = new StringBuffer();
        if (week1.isChecked())
            sp.append("1");
        if (week2.isChecked())
            sp.append("2");
        if (week3.isChecked())
            sp.append("3");
        if (week4.isChecked())
            sp.append("4");
        if (week5.isChecked())
            sp.append("5");
        if (week6.isChecked())
            sp.append("6");
        if (week7.isChecked())
            sp.append("7");
        if (sp.toString().length() != 0) {
            group_main.setVisibility(View.GONE);
        } else {
            group_main.setVisibility(View.VISIBLE);
        }
        return sp.toString();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        week();
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

                                main_radiobutton_num0.setText("自定义");
                            } else {
                                main_radiobutton_num0.setText(p1);
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
}
