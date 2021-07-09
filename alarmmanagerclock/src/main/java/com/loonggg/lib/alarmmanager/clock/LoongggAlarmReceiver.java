package com.loonggg.lib.alarmmanager.clock;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil.calMethod;

/**
 * Created by loongggdroid on 2016/3/21.
 */
public class LoongggAlarmReceiver extends BroadcastReceiver {
    public static final String ALARM_ACTION = "com.bayi.alarm.clock";
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.e("LoongggAlarmReceiver","广播发送了");
        long intervalMillis = intent.getLongExtra("intervalMillis", 0);
        int flag=intent.getIntExtra("flag",0);
        int week=intent.getIntExtra("week",0);
        if (intervalMillis != 0) {
            if(flag!=2) {
                AlarmManagerUtil.setAlarmTime(context, System.currentTimeMillis() + intervalMillis,
                        intent, intent.getLongExtra("longid", -1));
            }else {
                String num=String.valueOf(week);
                int[] w=new int[num.length()];
                for(int i=0;i<num.length();i++){
                    w[i]=Integer.parseInt(String.valueOf(num.charAt(i)));
                }

                List<Long> longs=new ArrayList<>();
                long nowTime=System.currentTimeMillis();
                for(int i=0;i<num.length();i++){
                    longs.add(calMethod(w[i], nowTime));
                }
                Collections.sort(longs);
                AlarmManagerUtil.setAlarmTime(context, longs.get(0),
                        intent, intent.getLongExtra("longid", -1));

            }
        }
        Intent clockIntent = new Intent(ALARM_ACTION);
        clockIntent.putExtra("longid",intent.getLongExtra("longid", -1));
        context.sendBroadcast(clockIntent);
    }


}
