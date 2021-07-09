package com.bayi.rerobot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bayi.rerobot.R;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class newAreaTaskAdapter extends BaseQuickAdapter<SetAreaBean, BaseViewHolder> {
    private Context context;
    private boolean isDelete=false;
    public newAreaTaskAdapter(@NonNull List<SetAreaBean> data, Context context) {
        super(data);
        mLayoutResId = R.layout.new_area_task_item;
        this.context=context;

    }
    public void setDelete(boolean delete){
        this.isDelete=delete;
    }

    @Override
    protected void convert(BaseViewHolder helper, SetAreaBean item) {

        LinearLayout line= helper.getView(R.id.line);
        ImageView bt= helper.getView(R.id.bt_delete);
        TextView text=helper.getView(R.id.text);
        if(isDelete){
            bt.setVisibility(View.VISIBLE);
        }else {
            bt.setVisibility(View.GONE);
        }
        if(item.getIsSelected()){
            text.setTextColor(Color.parseColor("#ffffff"));
            line.setBackgroundResource(R.drawable.area_normal);
        }else {
            text.setTextColor(Color.parseColor("#ffffff"));
            line.setBackgroundResource(R.drawable.area_normal);
        }
        text.setText(item.getAreaname());
//        bt.setOnClickListener(v -> {
//            if(onRecyclerViewItemClickListener!=null){
//                onRecyclerViewItemClickListener.onItemClick(helper.getPosition());
//            }
//        });
         line.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 if(onRecyclerViewItemClickListener!=null){
                     onRecyclerViewItemClickListener.onItemClick(helper.getPosition());
                 }
                 isDelete=true;

                return false;
             }
         });

    }
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
    OnItemLongClickListener OnItemLongClickListener;
    public void setOnItemLongClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

}

