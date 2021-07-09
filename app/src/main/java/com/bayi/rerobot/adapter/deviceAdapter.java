package com.bayi.rerobot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bayi.rerobot.R;
import com.bayi.rerobot.bean.deviceBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by tongzn
 * on 2020/5/8
 */
public class deviceAdapter  extends BaseQuickAdapter<deviceBean, BaseViewHolder> {
private Context context;
private int position;
public deviceAdapter(@NonNull List<deviceBean> data, Context context) {
        super(data);
        mLayoutResId = R.layout.device_item;
        this.context=context;

        }
public void setPostion(int position){
        this.position=position;
    }
@Override
protected void convert(BaseViewHolder helper, deviceBean item) {
    ImageView img=helper.getView(R.id.img);

    if(helper.getPosition()==(position-1)){
        img.setVisibility(View.VISIBLE);
    }else {
        img.setVisibility(View.INVISIBLE);
    }
        helper.setText(R.id.text,item.getText());
        helper.setBackgroundRes(R.id.pic,item.getPic());
        TextView text=helper.getView(R.id.state);
           switch (item.getState()){
               case 0:
                   text.setText("");
                   break;
               case 1:
                   text.setText("正常");
                   text.setTextColor(Color.parseColor("#FF4CC3F1"));
                   break;
               case 2:
                   text.setTextColor(Color.parseColor("#FFF44336"));
                   text.setText("错误");
                   break;
           }
        }
        }
