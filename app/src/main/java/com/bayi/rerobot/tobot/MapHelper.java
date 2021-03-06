package com.bayi.rerobot.tobot;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.slamtec.slamware.action.ActionStatus;
import com.slamtec.slamware.robot.Pose;
import com.tobot.slam.SlamManager;

import java.lang.ref.WeakReference;

import static com.eaibot.konyun.eaibotdemo.JniEAIBot.ON_EAI_BATTERY;

/**
 * @author houdeming
 * @date 2019/10/23
 */
public class MapHelper {
    private static final int MSG_STOP = 1;
    private Context mContext;
    private Thread mapUpdate;

    private HandlerThread mHandlerThread;
    private MapThreadHandle mMapThreadHandle;
    private boolean isFirstRefresh;
    private int mRefreshCount;
    private boolean isStart;
    private SignalThread mSignalThread;
    private Handler mhandler;

    public MapHelper(WeakReference<Context> contextWeakReference, WeakReference<Handler> handlerWeakReference) {
        mhandler=handlerWeakReference.get();
        mContext=contextWeakReference.get();
        startUpdateMap();
        mHandlerThread = new HandlerThread("MAP_THREAD");
        mHandlerThread.start();
        mMapThreadHandle = new MapThreadHandle(mHandlerThread.getLooper());
    }

   public void destroy() {
        stopUpdateMap();
        cancelAction();
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
    }

   public void cancelAction() {
        if (mMapThreadHandle != null) {
            mMapThreadHandle.obtainMessage(MSG_STOP).sendToTarget();
        }
    }

   public void updateMap() {
        isFirstRefresh = true;
        mRefreshCount = 0;
    }

    private void startUpdateMap() {
        isFirstRefresh = true;
        isStart = true;
        if (mapUpdate == null) {
            mapUpdate = new Thread(updateMapRunnable);
            mapUpdate.start();
        }
        if (mSignalThread == null) {
            mSignalThread = new SignalThread();
            mSignalThread.start();
        }
    }

    private void stopUpdateMap() {
        isFirstRefresh = false;
        isStart = false;
        if (mapUpdate != null) {
            mapUpdate.interrupt();
            mapUpdate = null;
        }
        if (mSignalThread != null) {
            mSignalThread.interrupt();
            mSignalThread = null;
        }
    }

    private Runnable updateMapRunnable = new Runnable() {
        int cnt;

        @Override
        public void run() {
            cnt = 0;
            mRefreshCount = 0;

            while (isStart) {
                if (mhandler == null ) {
                    return;
                }

                try {
                    if ((cnt % 10) == 0) {
                        // ???????????????????????????
                        Pose pose = SlamManager.getInstance().getPose();
                       // mMapView.setRobotPose(pose);
                       // mhandler.obtainMessage(MainHandle.MSG_GET_ROBOT_POSE, pose).sendToTarget();
                        // ??????????????????????????????
                      /*  mMapView.setLaserScan(SlamManager.getInstance().getLaserScan());
                        // ????????????????????????
                        mMapView.setHealth(SlamManager.getInstance().getRobotHealthInfo(), pose);
                        // ?????????????????????
                        mMapView.setSensors(SlamManager.getInstance().getSensors(), SlamManager.getInstance().getSensorValues(), pose);*/
                    }

                    if ((cnt % 15) == 0) {
                        // ????????????
                        if (isFirstRefresh || SlamManager.getInstance().isMapUpdate()) {
                            mRefreshCount++;
                            if (mRefreshCount > 3) {
                                mRefreshCount = 0;
                                isFirstRefresh = false;
                            }
                            // ????????????
                           // mMapView.setMap(SlamManager.getInstance().getMap());
                        } else {
                           // mMapView.setMapUpdate(false);
                        }
                   /*     // ???????????????
                        mMapView.setLines(ArtifactUsage.ArtifactUsageVirutalWall, SlamManager.getInstance().getLines(ArtifactUsage.ArtifactUsageVirutalWall));
                        // ????????????
                        mMapView.setLines(ArtifactUsage.ArtifactUsageVirtualTrack, SlamManager.getInstance().getLines(ArtifactUsage.ArtifactUsageVirtualTrack));
                        // ??????????????????
                        mMapView.setRemainingMilestones(SlamManager.getInstance().getRemainingMilestones());
                        mMapView.setRemainingPath(SlamManager.getInstance().getRemainingPath());*/
                        // ??????????????????
                        int battery = SlamManager.getInstance().getBatteryPercentage();

                        boolean isCharge = SlamManager.getInstance().isBatteryCharging();

                        int locationQuality = SlamManager.getInstance().getLocalizationQuality();

                        // ?????????????????????
                        ActionStatus actionStatus = SlamManager.getInstance().getRemainingActionStatus();

                       // mMainHandler.obtainMessage(MainHandle.MSG_GET_STATUS, new Object[]{battery, isCharge, locationQuality, actionStatus}).sendToTarget();
                        Status status=new Status();
                        status.setBattery(battery);

                        status.setLocationQuality(locationQuality);
                        status.setChargeStatus(isCharge);


                        mhandler.obtainMessage(ON_EAI_BATTERY, status).sendToTarget();

                        // ?????????????????????
                        //mMapView.setHomePose(SlamManager.getInstance().getHomePose());
                    }

                    Thread.sleep(33);
                    cnt++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private class SignalThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (isStart) {
                if (mhandler != null) {
                    //mhandler.obtainMessage(MainHandle.MSG_GET_RSSI, NetworkUtils.getRssi(mContext)).sendToTarget();
                }

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    private class MapThreadHandle extends Handler {

        private MapThreadHandle(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == MSG_STOP) {
                SlamManager.getInstance().cancelAction();
            }
        }
    }
}
