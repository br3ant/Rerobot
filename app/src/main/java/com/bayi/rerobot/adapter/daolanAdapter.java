package com.bayi.rerobot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bayi.rerobot.R;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by tongzn
 * on 2020/5/8
 */
public class daolanAdapter extends BaseQuickAdapter<SetAreaBean, BaseViewHolder> {
private Context context;
private int position;
public daolanAdapter(@NonNull List<SetAreaBean> data, Context context) {
        super(data);
        mLayoutResId = R.layout.daolan_item;
        this.context=context;

        }

@Override
protected void convert(BaseViewHolder helper, SetAreaBean item) {
    helper.setText(R.id.name,item.getAreaname());

    ConstraintLayout p=helper.getView(R.id.parent);
    p.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(onRecyclerViewItemClickListener!=null){
                onRecyclerViewItemClickListener.onItemClick(helper.getLayoutPosition());
            }
        }
    });


 }
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}
