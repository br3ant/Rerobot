package com.bayi.rerobot.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bayi.rerobot.R;
import com.bayi.rerobot.bean.CewenBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by tongzn
 * on 2021/3/2
 */
public class cewenAdapter extends BaseQuickAdapter<CewenBean, BaseViewHolder> {
    private Context context;
    public cewenAdapter(@NonNull List<CewenBean> data, Context context) {
        super(data);
        mLayoutResId = R.layout.cewen_item;
        this.context=context;

    }

    @Override
    protected void convert(BaseViewHolder helper, CewenBean item) {
        TextView tem=helper.getView(R.id.tem);
        if(item.getLevel()==0){
            tem.setBackgroundResource(R.drawable.cewen_tem);
        } else {
            tem.setBackgroundResource(R.drawable.cewen_tem1);
        }
        tem.setText(String.format("%s°C",item.getTem()));
        helper.setText(R.id.time,item.getTime());
        helper.setImageBitmap(R.id.profile_image,base64ToBitmap(item.getBase64()));

    }
    public static Bitmap base64ToBitmap(String base64String){
        byte[] bytes = Base64.decode(base64String, Base64.DEFAULT);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;

    }
}

