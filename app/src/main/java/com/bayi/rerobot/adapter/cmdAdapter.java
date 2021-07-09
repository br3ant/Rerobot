package com.bayi.rerobot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.bayi.rerobot.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by tongzn
 * on 2020/5/8
 */
public class cmdAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
private Context context;
private int position;
public cmdAdapter(@NonNull List<String> data, Context context) {
        super(data);
        mLayoutResId = R.layout.cmd_item;
        this.context=context;

        }

@Override
protected void convert(BaseViewHolder helper, String item) {
    helper.setText(R.id.bt,item);
    Button bt=helper.getView(R.id.bt);
   // bt.setBackgroundResource(R.drawable.area_normal);
    bt.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(onRecyclerViewItemClickListener!=null){
              //  bt.setBackgroundResource(R.drawable.area_click1);
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
