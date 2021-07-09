package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.TimerAdapter;
import com.bayi.rerobot.greendao.AddTimer;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.DisplayUtil;
import com.bayi.rerobot.util.RecycleViewDivider;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;


import java.util.List;

import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;
import static com.clock.AlarmUtil.setALRM;


public class TimerTaskDialog extends Dialog {

    public TimerTaskDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder implements View.OnClickListener {
        private final Context mContext;
        private final TimerTaskDialog mSpsDialog;


        public Builder(Context mContext) {
            this.mContext = mContext;
            mSpsDialog = new TimerTaskDialog(mContext, R.style.style_dialog);
        }
        private RecyclerView recyclerView;
         private TimerAdapter adapter;
        private List<AddTimer> addtimer;
        public TimerTaskDialog create(){
            addtimer= App.getDaoSession().getAddTimerDao().loadAll();
            View view = LayoutInflater.from(mContext).inflate(R.layout.timer_task_dialog,null);

            mSpsDialog.setCanceledOnTouchOutside(false);

            Window window = mSpsDialog.getWindow();
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = mSpsDialog.getWindow().getAttributes();
            lp.width = (int)(getScreenWidth()*1);
            lp.height = (int)(getScreenHeight()*1);
            lp.gravity = Gravity.TOP;
            ImageView addition=view.findViewById(R.id.addition);
            ImageView back=view.findViewById(R.id.back);
            addition.setOnClickListener(this);
            back.setOnClickListener(this);
            recyclerView=view.findViewById(R.id.recycle);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.addItemDecoration(new RecycleViewDivider(
                    mContext, LinearLayoutManager.VERTICAL, 1,Color.parseColor("#FFFFFF")));
            adapter=new TimerAdapter(addtimer, mContext, new TimerAdapter.OnState() {
                @Override
                public void onState(int postion, boolean isChecked) {
                    Log.e("week",isChecked+""+postion);
                    addtimer.get(postion).setState(isChecked);
                    App.getDaoSession().getAddTimerDao().updateInTx(addtimer);
                    setALRM(addtimer,false);
                }
            });
            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(int id) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        new TimerEditDialog.Builder(mContext)
                                .setData(addtimer.get(id))
                                .Ondismiss(new onDismiss() {
                            @Override
                            public void dismiss() {
                                //刷新时间
                                addtimer= App.getDaoSession().getAddTimerDao().loadAll();
                                adapter.setNewData(addtimer);
                            }
                        }).create().show();
                    }
                }
            });
            recyclerView.setAdapter(adapter);
            mSpsDialog.getWindow().setAttributes(lp);


            return mSpsDialog;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onClick(View v) {
          switch (v.getId()){
              case R.id.addition:
                  new TimerAddDialog.Builder(mContext).Ondismiss(new onDismiss() {
                      @Override
                      public void dismiss() {
                          //刷新时间
                          addtimer= App.getDaoSession().getAddTimerDao().loadAll();
                          adapter.setNewData(addtimer);

                      }
                  }).create().show();
                  break;
              case R.id.back:
                  mSpsDialog.dismiss();
                  break;
              default:
                  break;
          }
        }

    }
}
