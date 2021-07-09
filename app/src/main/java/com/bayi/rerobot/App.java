package com.bayi.rerobot;

import android.app.Activity;
import android.app.Application;
import android.app.slice.SliceManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.bayi.rerobot.communication.ConsumerProduct;
import com.bayi.rerobot.communication.OrderStorage;
import com.bayi.rerobot.communication.SocketClientImpl;
import com.bayi.rerobot.gen.DaoMaster;
import com.bayi.rerobot.gen.DaoSession;
import com.bayi.rerobot.greendao.AddTimer;
import com.bayi.rerobot.service.aiService;
import com.bayi.rerobot.tobot.ConnectSlamThread;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.CrashHandler;
import com.bayi.rerobot.util.LogUtil;

import com.bayi.rerobot.util.ScreenTimer;
import com.bayi.rerobot.util.SpHelperUtil;
import com.bayi.rerobot.util.TaskType;

import com.bayi.rerobot.util.USBCardFinerUtil;
import com.clock.AlarmUtil;
import com.iflytek.cloud.SpeechUtility;
import com.tobot.slam.SlamManager;

import com.tobot.slam.agent.listener.OnSlamExceptionListener;
import com.voice.osCaeHelper.CaeCoreHelper;
import com.yist.eailibrary.bean.map.VirtualWallB;
import com.yist.eailibrary.bean.mission.MissionB;
import com.yist.eailibrary.bean.mission.task.BaseTask;
import com.yist.eailibrary.constants.HandlerCode;
import com.yist.eailibrary.utils.JniEAIBotUtil;

import org.greenrobot.greendao.database.Database;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


public class
App extends Application implements OnSlamExceptionListener {
    public static Context context;
    private static DaoSession daoSession;
    private static DaoMaster.DevOpenHelper helper;
    public static JniEAIBotUtil jniEAIBotUtil;
    public String ip = "192.168.31.200";//机器人ip
    public String tobotip = "192.168.11.1";//瞳步机器人ip
    private static Toast mToast;
    private static boolean isInited = true;
    public static String targetName= "a1";//导航目标点
    public static String currentMap = "ceshi";//地图名称
    public static TaskType taskType = TaskType.Other;//任务类型
    private static SpHelperUtil spHelperUtil;
    private ConsumerProduct consumerProduct;
    private ConnectSlamThread mConnectSlamThread;

 private CrashHandler crashHandler;

   public static boolean isOK;
    /**
     * 获取全局上下文
     */
    public static Context getContext() {
        return context;
    }

    public static void toast(String msg) {
        if (mToast != null) {
            mToast.cancel();
        } else {
        }
        mToast = Toast.makeText(context
                , msg, Toast.LENGTH_SHORT);
        mToast.show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        spHelperUtil = new SpHelperUtil(this);
        ScreenTimer.initScreenTimer(this);
        ScreenTimer.startMonitor();
        ScreenTimer.eliminateEvent();
        crashHandler = CrashHandler.getInstance();
       crashHandler.init(context);
        isOK=true;
        targetName=(String) spHelperUtil.getSharedPreference(Contants.startName,"a1");
        currentMap=(String) spHelperUtil.getSharedPreference(Contants.mapName,"ceshi");
        SpeechUtility.createUtility(this, String.format("engine_start=ivw,delay_init=0,appid=%s", getString(R.string.app_id)));
        Intent intent = new Intent(this, aiService.class);
        try {
            startService(intent);
        } catch (Exception e) {
        }

        initGreenDao(this);
          //地盘连接
            inittobot();


        if (!CaeCoreHelper.hasModeFile()) {
            Log.e("sdcard", "部署资源");
            CaeCoreHelper.portingFile(this);
        }

        startSerialPortServer();
        //设置⏰
        initSetAlarm();

    }

    /**
     * 开启线程
     */
    private void startSerialPortServer() {
        if (null == consumerProduct) {
            consumerProduct = new ConsumerProduct(new SocketClientImpl(), OrderStorage.getInstance());
        }
        if (!consumerProduct.isAlive() || consumerProduct.isInterrupted()) {
            consumerProduct.start();
        }
    }

    /**
     * 关闭线程
     */
    private void stopSerialPortServer() {
        if (null == consumerProduct) {
            return;
        }
        if (consumerProduct.isAlive() || !consumerProduct.isInterrupted()) {
            consumerProduct.interrupt();
        }
    }

    private void initGreenDao(Context context) {
        helper = new DaoMaster.DevOpenHelper(context, "bayi.db");
        Database db = helper.getWritableDb();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public static DaoSession getDaoSession() {
        return daoSession;
    }

    private void inittobot() {
        //jniEAIBotUtil = new JniEAIBotUtil(this, "Smart");
        //jniEAIBotUtil.initEAISDK(ip, 1, 500, 0, Color.GRAY, Color.WHITE, Color.BLACK, Color.YELLOW, 2, 400, 400, 1000, handler1, HandlerCode.INIT_EAISDK);

        if(!SlamManager.getInstance().isConnected()){
            if (mConnectSlamThread == null) {
                mConnectSlamThread = new ConnectSlamThread(new WeakReference<Context>(this), tobotip);
                mConnectSlamThread.start();
            }
        }
        SlamManager.getInstance().setOnSlamExceptionListener(this);

    }



    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;
            int result;
            String mapName;
            int extend;
            switch (msg.what) {
                case HandlerCode.GET_MISSION_B:
                    bundle = msg.getData();
                    result = bundle.getInt("result");
                    if (result == 0) {
                        MissionB missionB = bundle.getParcelable("missionB");
                        if (missionB != null && missionB.getTaskList() != null) {
                            List<String> graphPathListToShow = new ArrayList<>();
                            List<String> recordPathListToShow = new ArrayList<>();
                            List<String> targetListToShow = new ArrayList<>();
                            for (BaseTask baseTask : missionB.getTaskList()) {
                                String taskType = baseTask.getType();
                                String taskSourceName = baseTask.getTaskSourceName();
                                switch (taskType) {
                                    case "NavigationTask":
                                        targetListToShow.add(taskSourceName);
                                        break;
                                    case "PlayPathTask":
                                        recordPathListToShow.add(taskSourceName);
                                        break;
                                    case "PlayGraphPathTask":
                                        graphPathListToShow.add(taskSourceName);
                                        break;
                                    default:
                                        break;
                                }
                            }
                            if (recordPathListToShow.size() > 0) {
                                jniEAIBotUtil.pathGetPathInfoB(currentMap, recordPathListToShow, handler, HandlerCode.PATH_GET_PATH_INFO_B);
                            }
                            if (graphPathListToShow.size() > 0) {
                                jniEAIBotUtil.pathGetGraphPathB(currentMap, graphPathListToShow, handler, HandlerCode.PATH_GET_GRAPH_PATH_B);
                            }
                            if (targetListToShow.size() > 0) {
                                jniEAIBotUtil.getTargetsB(0, currentMap, targetListToShow, handler, HandlerCode.GET_TARGETS_B);
                            }
                        } else {
                            toast(context.getString(R.string.synchronizeFailed));
                        }
                    } else {
                        if (result == 2) {
                            toast(context.getString(R.string.noNet));
                        } else {
                            toast(context.getString(R.string.synchronizeFailed) + ": " + result);
                        }
                    }
                    break;

                case HandlerCode.GET_VIR_POINT_INFO_B://获取虚拟墙
                    bundle = msg.getData();
                    result = bundle.getInt("result");
                    if (result == 0) {
                        VirtualWallB virtualWall = bundle.getParcelable("virtualWall");
                        if (virtualWall != null) {
                            //layerGroup.setVirtualWall(virtualWall);
                        }
                    } else {
                        if (result == 2) {
                            toast(context.getString(R.string.noNet));
                            LogUtil.saveLog("GET_VIR_POINT_INFO_B"+context.getString(R.string.noNet));
                        }
                    }
                    break;
                case HandlerCode.NAVIGATE_TARGET_CONTROL_B://导航控制
                    bundle = msg.getData();
                    result = bundle.getInt("result");
                    if (result == 0) {
                        toast(context.getString(R.string.stopGoToGoal));
                    } else {
                        if (result == 2) {
                            toast(context.getString(R.string.noNet));
                            LogUtil.saveLog("NAVIGATE_TARGET_CONTROL_B"+context.getString(R.string.noNet));
                        }
                    }
                    break;
                case HandlerCode.GET_BOT_INITPOSE_STATUS_B://判断是否校准
                    bundle = msg.getData();
                    result = bundle.getInt("result");
                    Log.e("result------>",result+"");
                    //Origin
                    switch (result) {
                        case 0:
                            toast(context.getString(R.string.initStatusFinish));
                            break;
                        case 1:
                            toast(context.getString(R.string.willInitPose));

                            break;
                        case 2:
                            toast(context.getString(R.string.disconnectInternet));

                            break;
                        case 3:
                            toast(context.getString(R.string.didnotLogin));
                            break;
                        case 4:
                            toast(context.getString(R.string.withoutPermission));
                            break;
                        case 5:
                            toast(context.getString(R.string.didnotInit));

                            break;
                        default:
                            toast(context.getString(R.string.apiAnomaly));
                            break;
                    }
                    break;

                case HandlerCode.SELECT_MAP_BY_NAME://选择地图
                    bundle = msg.getData();
                    result = bundle.getInt("result");
                    mapName = bundle.getString("mapName");
                    if (result == 0) {

                        toast("地图切换成功");
                        // toast(getString(R.string.switchMapSuccess));

                        jniEAIBotUtil.getBotInitPoseStatusB(handler, HandlerCode.GET_BOT_INITPOSE_STATUS_B);
                        jniEAIBotUtil.getVirPointInfoB(currentMap, handler, HandlerCode.GET_VIR_POINT_INFO_B);

                        //获取地图点位信息


                    } else {


                        if (result == 2) {
                            toast(context.getString(R.string.noNet));
                        } else {
                            toast(context.getString(R.string.switchMapFailed) + ": " + result);
                        }
                    }
                    break;
                case HandlerCode.NAVIGATE_TARGET_B://前往目标点导航
                    bundle = msg.getData();
                    result = bundle.getInt("result");
                    extend = bundle.getInt("extend");
                    if (result == 0) {
                        if (extend == 0) {
                            toast(context.getString(R.string.goToGoal) + ": " + targetName);
                        }
                        if (extend == 1) {
                            toast(context.getString(R.string.goToChargePileToCharge));

                        }
                        if (extend == 2) {
                            toast(context.getString(R.string.lowPowerToAutoCharge));
                        }
                    } else {

                        if (result == 2) {
                            toast(context.getString(R.string.noNet));
                            LogUtil.saveLog("NAVIGATE_TARGET_B"+context.getString(R.string.noNet));

                        } else {
                            if (extend == 0) {
                                LogUtil.saveLog("NAVIGATE_TARGET_B"+context.getString(R.string.cantGoToGoal) + ": " + result);
                                toast(context.getString(R.string.cantGoToGoal) + ": " + result);
                            } else {
                                toast(context.getString(R.string.cantGoToChargePile) + ": " + result);
                                LogUtil.saveLog("NAVIGATE_TARGET_B"+context.getString(R.string.cantGoToGoal) + ": " + result);
                            }
                        }
                    }

                    break;
                case HandlerCode.SET_BOT_INITPOSE_B:
                    bundle = msg.getData();
                    result = bundle.getInt("result");
                    String initPoseName = bundle.getString("initPoseName");
                    if (result == 0) {
                        toast("校准完毕");
                        LogUtil.saveLog("SET_BOT_INITPOSE_B"+"校准完毕");
                    } else {
                        toast("校准失败,请手动重新校准");
                        LogUtil.saveLog("SET_BOT_INITPOSE_B"+"校准失败,请手动重新校准");
                    }

                default:
                    break;
            }
        }
    };
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    toast(getString(R.string.internetDisconnect));
                    break;
                case 1:
                   /* String apkName = msg.getData().getString("apkName");
                    if (apkName.equals(version.getApkName())) {
                        toastLast(getString(R.string.isNewestVersion));
                    } else {
                        showUpdateApkDialog(apkName);
                    }*/
                    break;
                case 7:
                    toast(getString(R.string.initializationComplete));
                    break;
                case 8:
                    int extend = msg.getData().getInt("extend");
                    if (extend == 1) {
                        jniEAIBotUtil.initEAISDK(ip, 1, 500, 0, Color.GRAY, Color.WHITE, Color.BLACK, Color.YELLOW, 2, 400, 400, 1000, handler1, HandlerCode.INIT_EAISDK);
                    } else {

                    }
                    break;
                default:
                    break;
            }
        }
    };

    //
    private void uploadanslog() {

        File f = new File("/sdcard/bayi/msg.txt");
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(in, "gb2312"));
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        String temp;
        try {
            JSONArray jsonArray = new JSONArray();
            List<String> str = null;
            while ((temp = br.readLine()) != null) {
                str.add(temp);
            }
            if (str != null) {
                for (int i = 0; i < str.size(); i = i + 2) {
                    JSONObject jo = new JSONObject();
                }
            }
            br.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
      /*  repossitory.anslogsave().enqueue(new baseback() {
            @Override
            public void onResponse(Response<String> response) {

            }

            @Override
            public void onFailure() {

            }
        });*/
    }

    public static void hideSoftKeyboard(List<View> viewList) {
        if (viewList == null) return;

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        for (View v : viewList) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void initSetAlarm() {
        List<AddTimer> addtimer = getDaoSession().getAddTimerDao().loadAll();
        AlarmUtil.setALRM(addtimer,true);

    }


    @Override
    public void onSlamException(Exception e) {
        Log.e("onSlamException",e.toString());
    }
}
