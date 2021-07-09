package com.bayi.rerobot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bayi.rerobot.R;
import com.bayi.rerobot.greendao.LogS;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class logFragmentAdapter extends BaseQuickAdapter<LogS, BaseViewHolder> {
    private Context context;
    private String currentYMD;
    public logFragmentAdapter(@NonNull List<LogS> data, Context context) {
        super(data);
        mLayoutResId = R.layout.log_fragmetn_item;
        this.context=context;

    }

    @Override
    protected void convert(BaseViewHolder helper, LogS item) {
         TextView ymd=helper.getView(R.id.ymd);
         View view_top=helper.getView(R.id.view_top);

         if(item.getYmd().equals(currentYMD)){
             view_top.setVisibility(View.VISIBLE);
             ymd.setVisibility(View.GONE);
         }else {
             view_top.setVisibility(View.GONE);
             ymd.setVisibility(View.VISIBLE);
             ymd.setText(item.getYmd());
             currentYMD=item.getYmd();
         }
         helper.setText(R.id.hm,item.getHm());
         helper.setText(R.id.msg,item.getMsg());

    }

}

