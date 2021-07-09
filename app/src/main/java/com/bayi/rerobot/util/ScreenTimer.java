package com.bayi.rerobot.util;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.bayi.rerobot.ui.ScreenActivity;

/**
 * Created by tongzn
 * on 2020/8/21
 */
public class ScreenTimer {
    private static final String TAG = "PlayVideoManger";
    private static long LAST_TIME = 0;
    private static final int PLAY_TIME = 60*1000;
    //private static final int PLAY_TIME = 10*1000;
    private static final  int SEND_TIME = 3*1000;
    private static Context mContext;
    private static SpHelperUtil spHelperUtil;
    public static void initScreenTimer(Context context){
        mContext = context;
        spHelperUtil=new SpHelperUtil(context);
    }

    public static  void eliminateEvent(){
        LAST_TIME = System.currentTimeMillis();
    }

    public static  void startMonitor(){
        mHandler.sendEmptyMessageDelayed(0, SEND_TIME);
    }
    public static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            boolean screen=(boolean)spHelperUtil.getSharedPreference(Contants.screen,false);
//            if(!screen){
//                mHandler.sendEmptyMessageDelayed(0, SEND_TIME);
//                return;
//            }
            long idsTime = System.currentTimeMillis()-LAST_TIME;
            if(idsTime >= PLAY_TIME){
                //调试隐藏
              // gotoScreen();
            }else {
                mHandler.sendEmptyMessageDelayed(0, SEND_TIME);
            }

        }
    };
    private static void gotoScreen(){

        Intent intent = new Intent(mContext, ScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

    }
}

