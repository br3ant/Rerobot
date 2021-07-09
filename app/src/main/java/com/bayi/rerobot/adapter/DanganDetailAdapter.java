package com.bayi.rerobot.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.bayi.rerobot.R;
import com.bayi.rerobot.bean.Result;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class DanganDetailAdapter extends BaseQuickAdapter<Result, BaseViewHolder> {
    private Context context;
    public DanganDetailAdapter(@NonNull List<Result> data, Context context) {
        super(data);
        mLayoutResId = R.layout.dangan_detail_item;
        this.context=context;

    }

    @Override
    protected void convert(BaseViewHolder helper, Result item) {

        RelativeLayout re= helper.getView(R.id.re);
         helper.setText(R.id.title,item.getTitle());
         helper.setText(R.id.archiveno,item.getArchiveno());
         helper.setText(R.id.type_name,item.getType_name());
        Button up=helper.getView(R.id.up);
         if(item.isShow()){
             up.setVisibility(View.VISIBLE);
         }else {
             up.setVisibility(View.GONE);
         }
         up.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(onRecyclerViewItemClickListener1!=null){
                     onRecyclerViewItemClickListener1.onItemClick(helper.getPosition());
                 }
             }
         });
         re.setOnClickListener(v -> {
            if(onRecyclerViewItemClickListener!=null){
                onRecyclerViewItemClickListener.onItemClick(helper.getPosition());
            }
        });

    }
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener1;
    public void setOnItemClickListener1(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener1 = onRecyclerViewItemClickListener;
    }
}

