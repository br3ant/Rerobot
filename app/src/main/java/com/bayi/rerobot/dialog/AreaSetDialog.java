package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.AreaTaskAdapter;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.newAreaTaskAdapter;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.DisplayUtil;
import com.bayi.rerobot.util.ItemOffsetDecoration;

import java.util.List;

import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;

public class AreaSetDialog extends Dialog {

    public AreaSetDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder implements View.OnClickListener {
        private final Context mContext;
        private final AreaSetDialog mSpsDialog;
        private newAreaTaskAdapter adapter;
        private List<SetAreaBean> setAreaBeans;
        private boolean isdelete=false;
        public Builder(Context mContext) {
            this.mContext = mContext;
            mSpsDialog = new AreaSetDialog(mContext, R.style.style_dialog);
        }

       private RecyclerView recycle;
        public AreaSetDialog create(){
            setAreaBeans= App.getDaoSession().getSetAreaBeanDao().loadAll();
            View view = LayoutInflater.from(mContext).inflate(R.layout.area_set_dialog,null);

            mSpsDialog.setCanceledOnTouchOutside(false);

            Window window = mSpsDialog.getWindow();
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = mSpsDialog.getWindow().getAttributes();

            lp.width = (int)(getScreenWidth()*1);
            lp.height = (int)(getScreenHeight()*1);
            lp.gravity = Gravity.TOP;
            Button bt_delete= view.findViewById(R.id.bt_isDelete);
            isdelete=false;
            bt_delete.setText("删除");
            bt_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isdelete){
                        bt_delete.setText("删除");
                        adapter.setDelete(false);
                    }else {
                        bt_delete.setText("完成");
                        adapter.setDelete(true);

                    }
                    isdelete=!isdelete;
                    adapter.notifyDataSetChanged();
                }
            });
            view.findViewById(R.id.back).setOnClickListener(this);
            view.findViewById(R.id.addition).setOnClickListener(this);
            recycle=view.findViewById(R.id.area_recycle);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,5);
            recycle.setLayoutManager(gridLayoutManager);
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,R.dimen.item_offset);
            recycle.addItemDecoration(itemDecoration);
            adapter=new newAreaTaskAdapter(setAreaBeans,mContext);
            recycle.setAdapter(adapter);
            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(int id) {
                    if(isdelete){
                        App.getDaoSession().getSetAreaBeanDao().delete(setAreaBeans.get(id));
                        setAreaBeans.remove(id);
                        adapter.setNewData(setAreaBeans);
                    }
                }
            });
            mSpsDialog.getWindow().setAttributes(lp);
            return mSpsDialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    mSpsDialog.dismiss();
                    break;
                case R.id.addition:
                    new AreaAddDialog.Builder(mContext).Ondismiss(new onDismiss() {
                        @Override
                        public void dismiss() {
                            setAreaBeans= App.getDaoSession().getSetAreaBeanDao().loadAll();
                         adapter.setNewData(setAreaBeans);
                        }
                    }).create().show();
                    break;

                default:
                    break;
            }
        }
    }

}
