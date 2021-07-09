package com.bayi.rerobot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bayi.rerobot.R;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class AreaTaskAdapter extends BaseQuickAdapter<SetAreaBean, BaseViewHolder> {
    private Context context;
    public AreaTaskAdapter(@NonNull List<SetAreaBean> data, Context context) {
        super(data);
        mLayoutResId = R.layout.area_task_item;
        this.context=context;

    }

    @Override
    protected void convert(BaseViewHolder helper, SetAreaBean item) {

        LinearLayout line= helper.getView(R.id.line);
        TextView text=helper.getView(R.id.text);

        if(item.getIsSelected()){
            text.setTextColor(Color.parseColor("#ffffff"));
            line.setBackgroundResource(R.drawable.area_click1);
        }else {
            text.setTextColor(Color.parseColor("#ffffff"));
            line.setBackgroundResource(R.drawable.area_normal);
        }
         text.setText(item.getAreaname());
        line.setOnClickListener(v -> {
            if(onRecyclerViewItemClickListener!=null){
                onRecyclerViewItemClickListener.onItemClick(helper.getPosition());
            }
        });

    }
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}

