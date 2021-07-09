package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.AreaTaskAdapter;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.communication.Order;
import com.bayi.rerobot.communication.OrderManage;
import com.bayi.rerobot.communication.ProductCallback;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.greendao.Task;
import com.bayi.rerobot.service.aiService;
import com.bayi.rerobot.ui.logActivity;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.ItemOffsetDecoration;
import com.bayi.rerobot.util.LogUtil;
import com.bayi.rerobot.util.SocketType;
import com.bayi.rerobot.util.SpHelperUtil;
import com.bayi.rerobot.util.TaskType;
import com.tobot.slam.SlamManager;
import com.tobot.slam.data.LocationBean;
import com.yist.eailibrary.constants.HandlerCode;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.bayi.rerobot.App.toast;
import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;

public class NewTaskDialog extends Dialog {


    private NewTaskDialog(@NonNull Context context) {
        super(context);
    }
    private NewTaskDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    private NewTaskDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
    public static class Builder implements View.OnClickListener {
        private final Context mContext;
        private final NewTaskDialog mSpsDialog;

        public Builder(Context mContext) {
            this.mContext = mContext;
            mSpsDialog = new NewTaskDialog(mContext, R.style.style_dialog);
        }
        private int roundNum=1;//循环次数
        private int ModeType=Contants.PENWU;//MOSHI
        private List<SetAreaBean>setAreaBeans;
        private AreaTaskAdapter adapter;
        private SpHelperUtil spHelperUtil;
        private TextView textWater;
        private ImageView lighting;
        private   com.bayi.rerobot.utilView.MyBatterView MyBatterView;

        public NewTaskDialog create(){
            spHelperUtil=new SpHelperUtil(mContext);
            setAreaBeans=App.getDaoSession().getSetAreaBeanDao().loadAll();
            View view = LayoutInflater.from(mContext).inflate(R.layout.new_task_dialog,null);
            mSpsDialog.setCanceledOnTouchOutside(false);
            Window window = mSpsDialog.getWindow();
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = mSpsDialog.getWindow().getAttributes();
            lp.width = (int)(getScreenWidth()*1);
            lp.height = (int)(getScreenHeight()*1);
            lp.gravity = Gravity.TOP;
            textWater=view.findViewById(R.id.text_water);
            textWater.setText(String.format("当前水位:%s","中"));
            MyBatterView=view.findViewById(R.id.MyBatterView);
            RadioGroup radioGroup=view.findViewById(R.id.main_radiogroup);
            RadioGroup radioGroup_num=view.findViewById(R.id.main_radiogroup_num);
            Group group_=view.findViewById(R.id.group);
            Button startTask=view.findViewById(R.id.btn_startTask);
            Button stop=view.findViewById(R.id.btn_stop);
            Button pause=view.findViewById(R.id.btn_pause);
            Button restart=view.findViewById(R.id.btn_restart);
            lighting=view.findViewById(R.id.lighting);
            lighting.setVisibility(View.GONE);
            textWater.setVisibility(View.GONE);
            MyBatterView.setVisibility(View.GONE);
            view.findViewById(R.id.view_timer).setOnClickListener(this);
            view.findViewById(R.id.view_other).setOnClickListener(this);
            view.findViewById(R.id.view_more).setOnClickListener(this);
            view.findViewById(R.id.view_log).setOnClickListener(this);
            view.findViewById(R.id.icon_close).setOnClickListener(this);
            stop.setOnClickListener(this);
            pause.setOnClickListener(this);
            restart.setOnClickListener(this);
            startTask.setOnClickListener(this);
            RecyclerView recyclerView=view.findViewById(R.id.recycle);
            GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,5);
            recyclerView.setLayoutManager(gridLayoutManager);
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,R.dimen.item_offset);
            recyclerView.addItemDecoration(itemDecoration);
            adapter=new AreaTaskAdapter(setAreaBeans,mContext);
            adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(int id) {
                   setAreaBeans.get(id).setIsSelected(!setAreaBeans.get(id).getIsSelected());
                   adapter.setNewData(setAreaBeans);
                }
            });
            recyclerView.setAdapter(adapter);

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                 switch (checkedId){
                     case R.id.main_radiobutton_penwu:
                         ModeType= Contants.PENWU;
                         group_.setVisibility(View.VISIBLE);
                         break;
                     case R.id.main_radiobutton_ziwaideng:
                         ModeType=Contants.ZHIWAIDENG;
                         group_.setVisibility(View.VISIBLE);
                         break;
                     case R.id.main_radiobutton_chongdian:
                         ModeType=Contants.CHONGDIAN;
                         group_.setVisibility(View.GONE);
                         break;
                     case R.id.main_radiobutton_zhushui:
                         ModeType=Contants.WATER;
                         group_.setVisibility(View.GONE);
                         break;
                     default:
                         break;

                 }
                }
            });
            radioGroup_num.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                 switch (checkedId){
                     case R.id.main_radiobutton_num1:
                         roundNum=1;
                         break;
                     case R.id.main_radiobutton_num3:
                         roundNum=3;
                         break;
                     case R.id.main_radiobutton_num5:
                         roundNum=5;
                         break;
                     case R.id.main_radiobutton_num10:
                         roundNum=10;
                         break;
                     default:
                         break;
                 }
                }
            });
            mSpsDialog.getWindow().setAttributes(lp);


            return mSpsDialog;
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_startTask:
                    switch (ModeType){
                        case Contants.PENWU:
                            LogUtil.saveLog("人工下发喷雾消毒任务");
                            NewTask(0);
                            break;
                        case Contants.ZHIWAIDENG:
                            LogUtil.saveLog("人工下发巡逻任务");
                            NewTask(1);
                            break;
                        case Contants.CHONGDIAN:
                            App.getDaoSession().getTaskDao().deleteAll();
                            tasklist.clear();
                            App.toast("正在去充电");
                            EventBus.getDefault().post("充电");
                            sendsocket(SocketType.StopPenwu);
                            break;
                        case Contants.XUNLUO://xunluo
                            LogUtil.saveLog("人工下发巡逻任务");
                            NewTask(2);

                            break;
                        case Contants.WATER:
                            LogUtil.saveLog("加水");
                            EventBus.getDefault().post("加水");

                            break;
                        default:
                            break;
                    }
                    mSpsDialog.dismiss();
                    break;
                    case R.id.btn_stop:

                        if( App.taskType== TaskType.Task){

                            sendsocket(SocketType.StopPenwu);
            }
                        //App.jniEAIBotUtil.navigateTargetControlB(2, App.handler, HandlerCode.NAVIGATE_TARGET_CONTROL_B);
                        App.getDaoSession().getTaskDao().deleteAll();
                        SlamManager.getInstance().cancelAction();
                        break;
                case R.id.btn_pause:
                    App.jniEAIBotUtil.navigateTargetControlB(0, App.handler, HandlerCode.NAVIGATE_TARGET_CONTROL_B);
                    break;
                case R.id.btn_restart:
                    App.jniEAIBotUtil.navigateTargetControlB(1, App.handler, HandlerCode.NAVIGATE_TARGET_CONTROL_B);
                    break;
                case R.id.icon_close:
                    mSpsDialog.dismiss();

                    break;
                case R.id.view_timer:
                    new TimerTaskDialog.Builder(mContext).create().show();
                    break;
                case R.id.view_more:
                    //new MoreDialog.Builder(mContext).create().show();
                    break;
                case R.id.view_log:
                    mContext.startActivity(new Intent(mContext, logActivity.class));
                    break;
                case R.id.view_other:
                    new DeviceSetDialog.Builder(mContext).create().show();
                    break;

                default:
                    break;
            }
        }
        private List<Task>tasklist=new ArrayList<>();
        private void NewTask(int isPenwu){

            toast("正在开始任务。。。");
            App.getDaoSession().getTaskDao().deleteAll();
            tasklist.clear();
            //500任务测试
            for(int i=0;i<roundNum;i++){
                for (int j=0;j<setAreaBeans.size();j++){
                    if(setAreaBeans.get(j).getIsSelected()){
                        List<LocationBean> strings=setAreaBeans.get(j).getData();
                        for (int z=0;z<strings.size();z++){
                            Task task=new Task();
                            task.setTargetname(strings.get(z));
                            tasklist.add(task);
                        }
                    }
                }
            }
            App.getDaoSession().getTaskDao().insertOrReplaceInTx(tasklist);


            sendsocket(SocketType.blue);
            String water= (String) spHelperUtil.getSharedPreference("water","中水位");


            if(isPenwu==0) {
                if(water.equals("低水位")){
                    toast("当前喷雾消毒水较少,无法执行任务");
                    App.getDaoSession().getTaskDao().deleteAll();
                    return;
                }
                aiService.isPW=true;
                //sendsocket(SocketType.PENWU);
                App.taskType= TaskType.Task;
            }else if(isPenwu==1){
               // sendsocket(SocketType.Ziwaideng);
                App.taskType= TaskType.Navigation;
                aiService.isPW=false;
            }else {
                aiService.isPW=false;}
            toast("开始执行");

            if(tasklist.size()==0){
               // toast("请添加区域位置");//不选为全部
            }else {
            aiService.index=0;
                EventBus.getDefault().post("任务");
            }
        }
    }
    private static void sendsocket(int type){
        switch (type){
            case SocketType.PENWU:

              /*  try {
                    OrderManage.getInstance().startPenWu(Contants.TYPE_REBORT,new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {


                            if (state == ProductCallback.Sur) {
                            }else if(state==ProductCallback.TIMEOUT){
                                sendsocket(SocketType.PENWU);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                break;
                case SocketType.StopPenwu:
                    try {
                        OrderManage.getInstance().stopPenWu(Contants.TYPE_REBORT,new ProductCallback() {
                            @Override
                            public void onDataReceived(int state, String age) {
                                if (state == ProductCallback.Sur) {


                                }else if(state==ProductCallback.TIMEOUT){
                                    sendsocket(SocketType.StopPenwu);
                                }
                            }
                        }, Order.ExecutiveLevel.HIGH);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

            case SocketType.red:
               /* try {
                    OrderManage.getInstance().red_open(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {

                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.red);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                break;
            case SocketType.blue:
               /* try {
                    OrderManage.getInstance().blue_open(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {

                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.blue);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                break;
            case SocketType.green:
              /*  try {
                    OrderManage.getInstance().green_open(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {

                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.green);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                break;
        }

    }
}
