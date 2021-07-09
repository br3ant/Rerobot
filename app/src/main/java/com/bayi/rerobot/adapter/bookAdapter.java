package com.bayi.rerobot.adapter;

import android.content.Context;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bayi.rerobot.R;
import com.bayi.rerobot.bean.book;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class bookAdapter extends BaseQuickAdapter<book, BaseViewHolder> {
    private Context context;
    public bookAdapter(@NonNull List<book> data, Context context) {
        super(data);
        mLayoutResId = R.layout.book_item;
        this.context=context;

    }

    @Override
    protected void convert(BaseViewHolder helper,book item) {
        ConstraintLayout father= helper.getView(R.id.father);
        helper.setText(R.id.name,item.getBook_name());
        helper.setText(R.id.author,item.getBook_author());
        helper.setText(R.id.type,item.getBook_type());
        helper.setText(R.id.publisher,item.getBook_publisher());
        helper.setText(R.id.date,item.getBook_publish_date());
        father.setOnClickListener(v -> {
            if(onRecyclerViewItemClickListener!=null){
                onRecyclerViewItemClickListener.onItemClick(helper.getLayoutPosition());
            }
        });

    }
    OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }
}

