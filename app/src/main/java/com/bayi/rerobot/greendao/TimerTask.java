package com.bayi.rerobot.greendao;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;

public class TimerTask {
    @Id(autoincrement = true)
    private Long id;
    private int timerType;//定时任务类型，分执行一次，每天和选择星期
    @Convert(columnType = String.class, converter = StringConverter.class)
    private List<String> targetnameList;
     private int roundNum;//执行次数

}
