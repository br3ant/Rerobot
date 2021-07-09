package com.bayi.rerobot.tobot;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tobot.slam.SlamManager;
import com.tobot.slam.agent.SlamCode;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

import me.jessyan.autosize.utils.LogUtils;

/**
 * @author houdeming
 * @date 2020/5/13
 */
public class ConnectSlamThread extends Thread {
    /**
     * 底盘端口号
     */
    private static final int PORT = 1445;
    private static final long TIME_CONNECT = 2000;
    private Context mContext;
    private String mIp;
    private boolean isRun;

    public ConnectSlamThread(WeakReference<Context> reference, String ip) {
        mContext = reference.get();
        mIp = ip;
        isRun = true;
    }

    public void close() {
        isRun = false;
        interrupt();
    }

    @Override
    public void run() {
        super.run();
        while (isRun) {
            try {
                Thread.sleep(TIME_CONNECT);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }

            int connectResult;
            if (TextUtils.isEmpty(mIp)) {
                connectResult = SlamManager.getInstance().connect();
            } else {
                connectResult = SlamManager.getInstance().connect(mIp, PORT);
            }

           Log.i("ln","slam connectResult=" + connectResult);
            if (connectResult == SlamCode.SUCCESS) {
                // 添加ip
                EventBus.getDefault().post(new ConnectSuccessEvent());
                return;
            }
        }
    }


}
