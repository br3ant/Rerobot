package com.bayi.rerobot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bayi.rerobot.R;
import com.bayi.rerobot.greendao.AddTimer;
import com.bayi.rerobot.util.Contants;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.suke.widget.SwitchButton;

import java.util.List;

public class TimerAdapter extends BaseQuickAdapter<AddTimer, BaseViewHolder> {
private Context context;
public TimerAdapter(@NonNull List<AddTimer> data, Context context,OnState onState) {
        super(data);
        mLayoutResId = R.layout.addtimer_item;
        this.context=context;
       this.onState=onState;
        }
        private OnState onState;
     public interface OnState{
        void onState(int postion,boolean isChecked);
       }
@Override
protected void convert(BaseViewHolder helper, AddTimer item) {
    ConstraintLayout cl= helper.getView(R.id.cl);
       SwitchButton switch_button=helper.getView(R.id.switch_button);
       switch_button.setChecked(item.getState());
        switch_button.toggle();     //switch state
        switch_button.toggle(false);//switch without animation
        switch_button.setShadowEffect(false);//disable shadow effect
        switch_button.setEnabled(true);//disable button
        switch_button.setEnableEffect(false);//disable the switch animation
        switch_button.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                        //TODO do your job
                        if(onState!=null){
                          onState.onState(helper.getLayoutPosition(),isChecked);
                        }
                }
        });
         cl.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(onRecyclerViewItemClickListener!=null){
                     onRecyclerViewItemClickListener.onItemClick(helper.getPosition());
                 }
             }
         });
         helper.setText(R.id.text_time,item.getTime());
        TextView week=helper.getView(R.id.week);
        if(item.getWeek().equals("once")){
                week.setText("执行一次");
        }else if(item.getWeek().equals("everyday")){
                week.setText("每天");
        }else {
            week.setText(getsetweek(item.getWeek()));
        }
        switch (item.getModetype()){
                case Contants.PENWU:
                        helper.setText(R.id.mode,"运行模式|区域喷雾");
                        break;
                case Contants.DIANWEI:
                        helper.setText(R.id.mode,"运行模式|点位喷雾");
                        break;
                case Contants.XUNLUO:
                        helper.setText(R.id.mode,"运行模式|巡逻");
                        break;
         }
        }
        OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
      public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        }
        private String getsetweek(String s){
        StringBuffer sb=new StringBuffer();
          char[]chars=s.toCharArray();
          for(int i=0;i<chars.length;i++){
                switch (chars[i]){
                        case '1': sb.append("周一 ");
                                break;
                        case '2': sb.append("周二 ");
                                break;
                        case '3': sb.append("周三 ");
                                break;
                        case '4': sb.append("周四 ");
                                break;
                        case '5': sb.append("周五 ");
                                break;
                        case '6': sb.append("周六 ");
                                break;
                        case '7': sb.append("周日 ");
                                break;

                }
          }
          return sb.toString();
        }
        }


