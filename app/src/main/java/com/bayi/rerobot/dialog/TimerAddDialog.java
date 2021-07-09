package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.AreaTaskAdapter;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.greendao.AddTimer;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.DisplayUtil;
import com.bayi.rerobot.util.ItemOffsetDecoration;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;

public class TimerAddDialog extends Dialog {

    public TimerAddDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        private final Context mContext;
        private final TimerAddDialog mSpsDialog;
        private TimePicker mTimepicker;
        public Builder(Context mContext) {
            this.mContext = mContext;
            mSpsDialog = new TimerAddDialog(mContext, R.style.style_dialog);
        }
        private onDismiss onDismiss;
        public Builder Ondismiss(onDismiss dismiss){
            this.onDismiss=dismiss;
            return this;
        }
        SimpleDateFormat st=new SimpleDateFormat("HH:mm");
        private RadioButton once,everyday;
        private RadioGroup group_main;
        private CheckBox week1,week2,week3,week4,week5,week6,week7;
        private TextView text_time;
        private int roundNum=1;//循环次数
        private int ModeType=Contants.ZHIWAIDENG;//MOSHI
        private AreaTaskAdapter adapter;
        private List<SetAreaBean> setAreaBeans;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public TimerAddDialog create(){
            setAreaBeans=App.getDaoSession().getSetAreaBeanDao().loadAll();
            for(int i=0;i<setAreaBeans.size();i++){
                setAreaBeans.get(i).setIsSelected(false);
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.timer_add_dialog,null);

            mSpsDialog.setCanceledOnTouchOutside(false);

            Window window = mSpsDialog.getWindow();
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = mSpsDialog.getWindow().getAttributes();

            lp.width = (int)(getScreenWidth()*1);
            lp.height = (int)(getScreenHeight()*1);

            lp.gravity = Gravity.TOP;
            initLayout(view);
            view.findViewById(R.id.back).setOnClickListener(this);
            view.findViewById(R.id.save).setOnClickListener(this);

            RadioGroup radioGroup1=view.findViewById(R.id.radiogroup1);
            RadioGroup radioGroup_num=view.findViewById(R.id.main_radiogroup_num);
             text_time=view.findViewById(R.id.text_time);
            text_time.setOnClickListener(this);
             mTimepicker=view.findViewById(R.id.timepicker);
            mTimepicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);  //设置点击事件不弹键盘
            mTimepicker.setIs24HourView(true);   //设置时间显示为24小时
            mTimepicker.setHour(8);  //设置当前小时
            mTimepicker.setMinute(0); //设置当前分（0-59）
            mTimepicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {  //获取当前选择的时间
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    try {
                        text_time.setText(st.format(st.parse(String.format("%s:%s",hourOfDay,minute))));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            RecyclerView recyclerView=view.findViewById(R.id.recycle);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,5);
            recyclerView.setLayoutManager(gridLayoutManager);
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,R.dimen.item_offset);
            recyclerView.addItemDecoration(itemDecoration);
            adapter=new AreaTaskAdapter(setAreaBeans,mContext);
            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(int id) {
                    setAreaBeans.get(id).setIsSelected(!setAreaBeans.get(id).getIsSelected());
                    adapter.setNewData(setAreaBeans);
                }
            });
            recyclerView.setAdapter(adapter);

            radioGroup_num.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.main_radiobutton_num1:
                            roundNum=1;
                            break;
                        case R.id.main_radiobutton_num3:
                            roundNum=3;
                            break;
                        case R.id.main_radiobutton_num5:
                            roundNum=5;
                            break;
                        case R.id.main_radiobutton_num10:
                            roundNum=10;
                            break;
                        default:
                            break;
                    }
                }
            });
            radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId){
                        case R.id.main_radiobutton_penwu:
                            ModeType= Contants.PENWU;
                            break;
                        case R.id.main_radiobutton_ziwaideng:
                            ModeType=Contants.ZHIWAIDENG;
                            break;
                    }
                }
            });
            mSpsDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(onDismiss!=null){
                        onDismiss.dismiss();
                    }
                }
            });
            mSpsDialog.getWindow().setAttributes(lp);

            return mSpsDialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    mSpsDialog.dismiss();
                    break;
                case R.id.save:
                    if(group_main.getVisibility()==View.VISIBLE){
                        if(once.isChecked()){
                            week="once";
                        }else {
                            week="everyday";
                        }
                    }else {
                        week=week();
                    }
                   AddTimer addTimer=new AddTimer();
                    addTimer.setModetype(ModeType);
                    addTimer.setRoundnum(roundNum);
                    addTimer.setTime(text_time.getText().toString().trim());
                    addTimer.setWeek(week);
                    List<String>arenameList=new ArrayList<>();
                    for(int i=0;i<setAreaBeans.size();i++){
                        if(setAreaBeans.get(i).getIsSelected()){
                            arenameList.add(setAreaBeans.get(i).getAreaname());
                        }
                    }
                    if(arenameList.size()==0){
                        App.toast("请添加消毒区域");
                        return;
                    }
                    addTimer.setAreaNameList(arenameList);
                    App.getDaoSession().getAddTimerDao().insertOrReplace(addTimer);
                    mSpsDialog.dismiss();
                    break;
                case R.id.text_time:
                    if(mTimepicker.getVisibility()!=View.VISIBLE){
                        mTimepicker.setVisibility(View.VISIBLE);
                    }else {
                        mTimepicker.setVisibility(View.GONE);
                    }
                    break;
                default:
                    break;
            }
        }
        private void initLayout(View view){
            once=view.findViewById(R.id.btn_once);
            everyday=view.findViewById(R.id.btn_everyday);
            group_main=view.findViewById(R.id.radiogroup_main);

            week1=view.findViewById(R.id.btn_week1);
            week2=view.findViewById(R.id.btn_week2);
            week3=view.findViewById(R.id.btn_week3);
            week4=view.findViewById(R.id.btn_week4);
            week5=view.findViewById(R.id.btn_week5);
            week6=view.findViewById(R.id.btn_week6);
            week7=view.findViewById(R.id.btn_week7);
            week1.setOnCheckedChangeListener(this);
            week2.setOnCheckedChangeListener(this);
            week3.setOnCheckedChangeListener(this);
            week4.setOnCheckedChangeListener(this);
            week5.setOnCheckedChangeListener(this);
            week6.setOnCheckedChangeListener(this);
            week7.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
             week=week();
        }
        private void noWeek(){
            week1.setChecked(false);
            week2.setChecked(false);
            week3.setChecked(false);
            week4.setChecked(false);
            week5.setChecked(false);
            week6.setChecked(false);
            week7.setChecked(false);
        }
        private String week="";
        private String week(){
            StringBuffer sp=new StringBuffer();
            if(week1.isChecked())
               sp.append("1");
            if(week2.isChecked())
                sp.append("2");
            if(week3.isChecked())
                sp.append("3");
            if(week4.isChecked())
                sp.append("4");
            if(week5.isChecked())
                sp.append("5");
            if(week6.isChecked())
                sp.append("6");
            if(week7.isChecked())
                sp.append("7");
            if(sp.toString().length()!=0){
                group_main.setVisibility(View.GONE);
            }else {
                group_main.setVisibility(View.VISIBLE);
            }
            return  sp.toString();
        }
    }

}
