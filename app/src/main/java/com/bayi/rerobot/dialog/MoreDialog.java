package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.cewenAdapter;
import com.bayi.rerobot.adapter.moreAdapter;
import com.bayi.rerobot.bean.CewenBean;
import com.bayi.rerobot.bean.Cwjson;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.ItemOffsetDecoration;
import com.bayi.rerobot.util.KeyboardUtils;
import com.bayi.rerobot.util.SpHelperUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.bayi.rerobot.adapter.cewenAdapter.base64ToBitmap;
import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;

public class MoreDialog extends Dialog {

    public MoreDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder  {
        private final Context mContext;
        private final MoreDialog mSpsDialog;



        public Builder(Context mContext,OnRecyclerViewItemClickListener L) {
            this.mContext = mContext;
            this.onRecyclerViewItemClickListener=L;
            mSpsDialog = new MoreDialog(mContext, R.style.style_dialog);
        }
        private View view;
        private RecyclerView recyclerView1;
        private List<String> datas= Arrays.asList("设置","设备控制","更多");
        private moreAdapter adapter;

        public MoreDialog create(){

            view = LayoutInflater.from(mContext).inflate(R.layout.more_dialog,null);

            mSpsDialog.setCanceledOnTouchOutside(true);

            Window window = mSpsDialog.getWindow();
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = mSpsDialog.getWindow().getAttributes();

            lp.width = (int)(getScreenWidth()*0.5);
            lp.height = (int)(getScreenHeight()*0.4);

            lp.gravity = Gravity.CENTER;
            recyclerView1=view.findViewById(R.id.recycle1);
            adapter=new moreAdapter(datas,mContext);

            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(int id) {
                    if(onRecyclerViewItemClickListener!=null){
                        onRecyclerViewItemClickListener.onItemClick(id);
                        mSpsDialog.dismiss();
                    }
                }
            });
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext, R.dimen.item_offset);
            recyclerView1.addItemDecoration(itemDecoration);
            LinearLayoutManager ms= new LinearLayoutManager(mContext);
            ms.setOrientation(LinearLayoutManager.VERTICAL);

            recyclerView1.setLayoutManager(ms);
            recyclerView1.setAdapter(adapter);



            mSpsDialog.getWindow().setAttributes(lp);


            return mSpsDialog;
        }

        OnRecyclerViewItemClickListener onRecyclerViewItemClickListener;
        public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
        }

    }
}
