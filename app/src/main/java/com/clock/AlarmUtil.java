package com.clock;

import android.util.Log;

import com.bayi.rerobot.App;
import com.bayi.rerobot.greendao.AddTimer;
import com.loonggg.lib.alarmmanager.clock.AlarmManagerUtil;


import java.util.List;

public class AlarmUtil {
    public static void setALRM(List<AddTimer> addtimer,boolean isfirst){
        for(int i=0;i<addtimer.size();i++){
            AlarmManagerUtil.cancelAlarm(App.context,addtimer.get(i).getId());
        }
        for(int i=0;i<addtimer.size();i++){
            if(!addtimer.get(i).getState()){
                continue;
            }
            String times[]=addtimer.get(i).getTime().split(":");

            switch (addtimer.get(i).getWeek()){
                case "once"://一次的就不执行了
                    if(isfirst){
                        break;
                    }
                    AlarmManagerUtil.setAlarm(App.context, 0, Integer.parseInt(times[0]), Integer.parseInt
                            (times[1]), 0, addtimer.get(i).getId());
                    break;
                case "everyday":
                    AlarmManagerUtil.setAlarm(App.context, 1, Integer.parseInt(times[0]), Integer.parseInt
                            (times[1]), 0, addtimer.get(i).getId());
                    break;

                default:
                    AlarmManagerUtil.setAlarm(App.context, 2, Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(addtimer.get(i).getWeek()), addtimer.get(i).getId());
                    break;
            }
        }
    }

}
