package com.bayi.rerobot.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bayi.rerobot.R;
import com.bayi.rerobot.greendao.LogS;
import com.bayi.rerobot.greendao.SetBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by tongzn
 * on 2020/5/6
 */
public class tabAdapter  extends BaseQuickAdapter<SetBean, BaseViewHolder> {
    private Context context;
    private int mPage;
    public tabAdapter(@NonNull List<SetBean> data, Context context,int mPage) {
        super(data);
        mLayoutResId = R.layout.tab_fragment_item;
        this.context=context;
        this.mPage=mPage;
    }

    @Override
    protected void convert(BaseViewHolder helper, SetBean item) {
        helper.setText(R.id.ymd,item.getYmd());
        helper.setText(R.id.hms,item.getHms());
        String data="";

        switch (mPage){
            case 0:
                data=String.format("%s℃",item.getTem());
                break;
            case 1:
                data=String.format("%sRH",item.getHum());
                break;
            case 2:
                data=String.format("%smg/l",item.getCo2());
                break;
            case 3:
                data=String.format("%sug/m3",item.getPm25());
                break;
            case 4:
                data=String.format("%smg/m3",item.getVvoc());
                break;
            case 5:
                data=String.format("%sug/m3",item.getPm10());
                break;
        }
       helper.setText(R.id.data,data);

    }

}


