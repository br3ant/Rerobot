package com.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.bayi.rerobot.App;
import com.bayi.rerobot.communication.Order;
import com.bayi.rerobot.communication.OrderManage;
import com.bayi.rerobot.communication.ProductCallback;
import com.bayi.rerobot.gen.SetAreaBeanDao;
import com.bayi.rerobot.greendao.AddTimer;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.greendao.Task;
import com.bayi.rerobot.service.aiService;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.LogUtil;
import com.bayi.rerobot.util.SocketType;
import com.bayi.rerobot.util.SpHelperUtil;
import com.bayi.rerobot.util.TaskType;
import com.tobot.slam.data.LocationBean;
import com.yist.eailibrary.constants.HandlerCode;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static com.bayi.rerobot.App.toast;


public class LoongggAlarmReceiver extends BroadcastReceiver {
    private List<Task>tasklist=new ArrayList<>();
    private static  final String TAG="LoongggAlarmReceiver";
    private SpHelperUtil spHelperUtil;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        spHelperUtil=new SpHelperUtil(context);
        String water= (String) spHelperUtil.getSharedPreference("water","中水位");
        if(water.equals("低水位")){
            toast("当前喷雾消毒水较少,无法执行任务");
            return;
        }
        sendsocket(SocketType.blue);
        long id = intent.getLongExtra("longid",-1);
        Log.e(TAG,id+"");
        AddTimer addTimer;
        addTimer= App.getDaoSession().getAddTimerDao().load(id);
        aiService.sleepTime = 0;

        List<String> areanameList=addTimer.getAreaNameList();
        Log.e(TAG,addTimer.getRoundnum()+""+addTimer.getModetype());
        if(addTimer.getModetype()!=Contants.DIANWEI){
        for(int z=0;z<addTimer.getRoundnum();z++){
            for(int i=0;i<areanameList.size();i++){
                List<SetAreaBean> list= App.getDaoSession().getSetAreaBeanDao().queryBuilder().
                        where(SetAreaBeanDao.Properties.Areaname.eq(areanameList.get(i))).build().list();
                List<LocationBean>areaList = null;
                try {
                    areaList=list.get(0).getData();
                }catch (IndexOutOfBoundsException e){
                    LogUtil.saveLog("定时任务点位数据为空");
                    return;
                }
                for(int j=0;j<areaList.size();j++){
                    Task task=new Task();
                    task.setTargetname(areaList.get(j));
                    tasklist.add(task);
                }
            }
         }
        }else {
            aiService.sleepTime = addTimer.getRoundnum();
            for(int i=0;i<areanameList.size();i++){
                List<SetAreaBean> list= App.getDaoSession().getSetAreaBeanDao().queryBuilder().
                        where(SetAreaBeanDao.Properties.Areaname.eq(areanameList.get(i))).build().list();
                List<LocationBean>areaList = null;
                try {
                    areaList=list.get(0).getData();
                }catch (IndexOutOfBoundsException e){
                    LogUtil.saveLog("定时任务点位数据为空");
                    return;
                }
                for(int j=0;j<areaList.size();j++){
                    Task task=new Task();
                    task.setTargetname(areaList.get(j));
                    tasklist.add(task);
                }
            }
        }
        Log.e(TAG,"--->>"+tasklist.size()+"");
        App.getDaoSession().getTaskDao().insertOrReplaceInTx(tasklist);
        if(App.isOK) {
            if (addTimer.getModetype() == Contants.PENWU||addTimer.getModetype() == Contants.DIANWEI) {
                sendsocket(SocketType.PENWU);
                aiService.isPW=true;
            } else {
                sendsocket(SocketType.Ziwaideng);
                aiService.isPW=false;
            }
            aiService.index = 0;
            App.targetName = tasklist.get(aiService.index).getTargetname().getLocationNumber();
            App.taskType = TaskType.Task;
            EventBus.getDefault().post("任务");
        }else {
            App.handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (addTimer.getModetype() == Contants.PENWU||addTimer.getModetype() == Contants.DIANWEI) {
                        sendsocket(SocketType.PENWU);
                    } else {
                        sendsocket(SocketType.Ziwaideng);

                    }
                    aiService.index = 0;
                    App.targetName = tasklist.get(aiService.index).getTargetname().getLocationNumber();
                    App.taskType = TaskType.Task;
                    EventBus.getDefault().post("任务");
                }
            },20000);

        }    }

    private static void sendsocket(int type){
        switch (type){
            case SocketType.PENWU:
                try {
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
                }
                break;
            case SocketType.Ziwaideng:
             /*   try {
                    OrderManage.getInstance().startZhiwaideng(Contants.TYPE_REBORT,new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                            }else if(state==ProductCallback.TIMEOUT){
                                sendsocket(SocketType.Ziwaideng);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;*/
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
             /*   try {
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
                /*try {
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
