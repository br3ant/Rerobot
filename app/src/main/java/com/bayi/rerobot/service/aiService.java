package com.bayi.rerobot.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Handler;

import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.bean.ListDataSave;
import com.bayi.rerobot.bean.MainMsg;
import com.bayi.rerobot.bean.MapName;
import com.bayi.rerobot.bean.SleepOrWork;
import com.bayi.rerobot.bean.Speaktxt;
import com.bayi.rerobot.bean.bookSearch;
import com.bayi.rerobot.bean.danganEventbus;
import com.bayi.rerobot.bean.goFragment;
import com.bayi.rerobot.bean.onlineMode;
import com.bayi.rerobot.bean.powerUi;
import com.bayi.rerobot.bean.socketEventbus;
import com.bayi.rerobot.bean.waterAndpower;
import com.bayi.rerobot.communication.Order;
import com.bayi.rerobot.communication.OrderManage;
import com.bayi.rerobot.communication.ProductCallback;
import com.bayi.rerobot.data.response.ApiRepossitory;
import com.bayi.rerobot.gen.SetAreaBeanDao;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.greendao.Task;
import com.bayi.rerobot.greendao.anySaveLog;
import com.bayi.rerobot.serial.SerialData;
import com.bayi.rerobot.serial.SerialManager;
import com.bayi.rerobot.tobot.BaseConstant;
import com.bayi.rerobot.tobot.ConnectSuccessEvent;
import com.bayi.rerobot.tobot.MapHelper;
import com.bayi.rerobot.tobot.Status;
import com.bayi.rerobot.tobot.locationEvent;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.Command;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.HttpUtil;

import com.bayi.rerobot.util.LogUtil;
import com.bayi.rerobot.util.ScreenTimer;
import com.bayi.rerobot.util.SocketClient;
import com.bayi.rerobot.util.SocketHead;
import com.bayi.rerobot.util.SocketType;
import com.bayi.rerobot.util.SpHelperUtil;
import com.bayi.rerobot.util.TaskType;
import com.bayi.rerobot.util.ToastUtil;
import com.eaibot.konyun.eaibotdemo.JniEAIBot;
import com.genius.audio.track.MyPcmPlayer;


import com.google.gson.Gson;
import com.iflytek.aiui.AIUIAgent;
import com.iflytek.aiui.AIUIConstant;
import com.iflytek.aiui.AIUIEvent;
import com.iflytek.aiui.AIUIListener;
import com.iflytek.aiui.AIUIMessage;
import com.iflytek.iflyos.cae.CAE;
import com.socketservice.ServerStartMain;
import com.tobot.slam.SlamManager;
import com.tobot.slam.agent.listener.OnFinishListener;
import com.tobot.slam.agent.listener.OnNavigateListener;
import com.tobot.slam.agent.listener.OnResultListener;
import com.tobot.slam.data.LocationBean;

import com.voice.caePk.CaeOperator;
import com.voice.caePk.OnCaeOperatorlistener;
import com.yist.eailibrary.constants.HandlerCode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;


import cn.kuwo.autosdk.api.KWAPI;
import cn.kuwo.autosdk.api.OnSearchListener;
import cn.kuwo.autosdk.api.PlayState;
import cn.kuwo.autosdk.api.SearchStatus;
import cn.kuwo.base.bean.Music;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



import static androidx.core.app.NotificationCompat.PRIORITY_MAX;
import static com.bayi.rerobot.App.toast;


public class aiService extends Service  {
    //交互状态
    //交互状态
    private int mAIUIState = AIUIConstant.STATE_IDLE;
    private boolean isAsring = true;
    private MyPcmPlayer myPcmPlayer = new MyPcmPlayer();
    private boolean isrc4=false;
    private ApiRepossitory repossitory = new ApiRepossitory();
    private SpHelperUtil spHelperUtil;
    private int batteryPercentage=0;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static MapHelper mapHelper;
    private boolean isCharge;//瞳步充电状态
    public aiService() {
    }
    public static int sleepTime=0;
    private String[]NoKnows={"对不起,小益还不知道这个问题呢","对不起,小益不知道你在说什么呢","抱歉,小益听不懂你说的话呢","抱歉,小益不明白你说的呢",
            "抱歉,你说的问题小益还在学习中呢","对不起,你提的问题我没听懂呢","不好意思,小益没听清你说的话呢","不好意思,小益没找到你要的呢",
            "不好意思,小益没听懂你说的话呢"};
    String[][]D400=new String[][]{Command.D400_0,Command.D401_0,Command.D402_0,Command.D400_1};
    String[][]D401=new String[][]{Command.D400_0,Command.D401_0,Command.D402_0,Command.D401_1};
    String[][]D402=new String[][]{Command.D400_0,Command.D401_0,Command.D402_0,Command.D402_1};
    private boolean isLowPower=false;//判断是否是低电量
    private static final String TAG = aiService.class.getSimpleName();
    private StringBuffer params;
    public static String sn;
    private List<anySaveLog> anySaveLogs;
    private SocketClient client = new SocketClient();
    Handler waterHandler = new Handler();
    Handler wakeupHandler = new Handler();//唤醒任务暂停
    private static final  boolean isNingbo=true;//判断是否是宁波图书馆版本
    public static final int relocationSuccess =2000;
    public static final int relocationFail =2001;
    public static final int locationdata =2002;
    public static final int changemapSuccess =2003;
    public static final int changemapFail =2004;
    private ListDataSave dataSave;
   public static boolean isPW=false;
    private HttpUtil httpUtil=new HttpUtil();
    public  List<LocationBean>myLocationBean;
    private AIUIAgent mAIUIAgent = null;
    //
    // 多麦克算法库
    private CaeOperator caeOperator;

    KWAPI mKwapi;

    private Runnable watertask = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            waterHandler.postDelayed(this, 1 * 1000);//设置延迟时间，此处是5秒
            sendsocket(SocketType.WATERDATA);

        }
    };
    private Runnable taskRunable=new Runnable(){

        @Override
        public void run() {
           EventBus.getDefault().post("任务");
        }
    };
    int readnum=0;
    //是否可以加水
    private Runnable CanAddWatertask = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            waterHandler.postDelayed(this, 1 * 1000);
            sendsocket(SocketType.CanAddWater);
             readnum++;
             if(readnum>60){
                 waterHandler.removeCallbacks(CanAddWatertask);
                 readnum=0;
                 App.taskType = TaskType.Other;
                 LogUtil.saveLog("对水孔失败");
                 sendsocket(SocketType.stopwater);
             }
        }
    };
    //开始伸头
    private Runnable addwater = new Runnable() {
        public void run() {
            sendsocket(SocketType.startWater);
        }
    };
    //开始出水
    private Runnable outwater = new Runnable() {
        public void run() {
            sendsocket(SocketType.OutWater);
        }
    };
    //开始出水
    private Runnable stopwater = new Runnable() {
        public void run() {
            sendsocket(SocketType.stopwater);

        }
    };
    private Runnable wakeupRunnable = new Runnable() {
        public void run() {
            if (isCharge){//充电中取消
                return;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    httpUtil.get(App.targetName);
                }
            }).start();
        }
    };
    //自动加水
    private Runnable alwaystask = new Runnable() {
        public void run() {
            // TODOAuto-generated method stub
            waterHandler.postDelayed(this, 60 * 1000 * 1);//设置延迟时间，此处是5秒
            sendsocket(SocketType.WATERDATA);
            EventBus.getDefault().post(new waterAndpower().setPower(batteryPercentage).setWater(0x02).setCharge(isCharge));
            uprobortstate();
        }
    };

    private Runnable aiuiWork = null;

    int count;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle;
            int result;
            String mapName;
            int extend;
            switch (msg.what) {


                case JniEAIBot.ON_EAI_TASK_INFO://状态信息
                    byte[] taskBytes = msg.getData().getByteArray("data");
                    if (taskBytes != null) {
                        String taskInfo = new String(taskBytes);
                        Log.e(TAG, "taskInfo: " + taskInfo);
                        //checkTaskInfo(taskInfo);
                    }
                    break;

                case JniEAIBot.ON_EAI_BATTERY://电池状态
                     int LOW=Integer.valueOf((String)spHelperUtil.getSharedPreference(Contants.lowCharge, "30"));
                     int HIGH=Integer.valueOf((String)spHelperUtil.getSharedPreference(Contants.highCharge, "90"));
                    Status status=(Status)msg.obj;
                    batteryPercentage=status.getBattery();
                    isCharge=status.isChargeStatus();
                    powerUi pw=new powerUi();
                    pw.setCharge(isCharge);
                    EventBus.getDefault().post(pw);
                    List<Task> tasks = App.getDaoSession().getTaskDao().loadAll();
                     if (batteryPercentage==0)return;
                    //电量小于30去充电
                    if (batteryPercentage <=LOW && App.taskType != TaskType.Charge && App.taskType != TaskType.Water) {
                        if (isCharge) {
                            //充电中
                        }else {
                            isLowPower = true;
                            toast("低电量回充中。。。");
                            LogUtil.saveLog("低电量回充");
                            sendsocket(SocketType.green);
                            sendsocket(SocketType.StopPenwu);


                            App.taskType = TaskType.Charge;
                            EventBus.getDefault().post("充电");

                            upanySaveLog();//k2日志 目前k3暂时无
                        }

                    }
                    if(batteryPercentage>LOW){
                        isLowPower=false;
                    }
                    //电量大于90如果有任务去执行任务
                    if (batteryPercentage >=HIGH && App.taskType != TaskType.Water&&isCharge) {
                       // LogUtil.saveLog("充电后继续执行任务");
                        isLowPower=false;
                        String water= (String) spHelperUtil.getSharedPreference("water","中水位");
                        if(water.equals("")){
                            break;
                        }
                        if (tasks.size() > 0) {

                            if(isPW){
                                App.taskType = TaskType.Task;
                               // sendsocket(SocketType.PENWU);
                            }  else {
                                App.taskType = TaskType.Navigation;
                            }
                            EventBus.getDefault().post("任务");
                        } else {

                           //goChushi();
                        }
                    }
                    break;
                case relocationSuccess:
                    toast("重定位成功");
                    break;
                case relocationFail:
                    toast("重定位失败");
                    break;
                case changemapSuccess:
                    myLocationBean=(List<LocationBean>) msg.obj;
                    dataSave.setDataList(Contants.LISTBEAN,myLocationBean);
                    toast("地图切换成功");
                    break;
                case changemapFail:
                    toast("地图切换失败");
                    break;
                case locationdata:
                    myLocationBean=(List<LocationBean>) msg.obj;

                    dataSave.setDataList(Contants.LISTBEAN,myLocationBean);
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ServerStartMain.startSocketService();
            }
        }).start();
        spHelperUtil = new SpHelperUtil(this);
        String  address= (String)spHelperUtil.getSharedPreference(Contants.IOT_IP,"115.231.60.194");
        int port= Integer.valueOf((String)spHelperUtil.getSharedPreference(Contants.IOT_PORT,"23"));
        client.clintValue(address, port);
        Log.e(TAG, "create..");
        EventBus.getDefault().register(this);
        dataSave=new ListDataSave(aiService.this,"myLocationBean");
        myLocationBean=dataSave.getDataList(Contants.LISTBEAN,LocationBean.class);
        //changmap();

        initSerialOperator();
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        Intent notificationIntent = new Intent(this, NewMain.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        recordtime = System.currentTimeMillis();
        mAIUIAgent = AIUIAgent.createAgent(this, getAIUIParams(), mAIUIListener);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAIUIAgent != null) {
                    AIUIMessage startMsg = new AIUIMessage(
                            AIUIConstant.CMD_START, 0, 0, "", null);
                    mAIUIAgent.sendMessage(startMsg);
                }

            }
        }, 100);
        init();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initCaeEngine();
            }
        }).start();

       // initCaeEngine();
        //initKwApi();
        sendsocket(SocketType.blue);
        //          sendsocket(SocketType.startWater);
       /* sendsocket(SocketType.WULIANG);
        sendsocket(SocketType.WIND);*/
        waterHandler.removeCallbacks(alwaystask);
        waterHandler.postDelayed(alwaystask,3000);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setTicker("机器人服务")
                .setContentTitle("八益")
                .setContentText("")
                .setOngoing(true)
                .setPriority(PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .build();
        startForeground(0, notification);
        sn = getSerialNumber();
        return super.onStartCommand(intent, flags, startId);

    }


    //AIUI事件监听器
    private final AIUIListener mAIUIListener = new AIUIListener() {

        @Override
        public void onEvent(AIUIEvent event) {
            Log.e("mAIUIListener", event.eventType + "");
            switch (event.eventType) {
                case AIUIConstant.EVENT_WAKEUP:
                    //唤醒事件
                    Log.i(TAG, "on event: " + event.eventType);
                    showTip("进入识别状态");
                    speak("你好!");
                    if (mKwapi != null) {
                        mKwapi.setPlayState(PlayState.STATE_PAUSE);
                    }
                    sendsocket(SocketType.StopPenwu);
                    SlamManager.getInstance().cancelAction();
                    EventBus.getDefault().post(new socketEventbus().setType(SocketHead.activity));
                    ScreenTimer.eliminateEvent();
                    EventBus.getDefault().post(new goFragment().setI(1));
                    break;

                case AIUIConstant.EVENT_RESULT: {
                    //结果事件
                    Log.i(TAG, "on event: " + event.eventType);
                    Log.i(TAG, "on event111: " + event.info);
                    try {
                        JSONObject bizParamJson = new JSONObject(event.info);
                        Log.e("aiuijson", bizParamJson.toString());
                        JSONObject data = bizParamJson.getJSONArray("data").getJSONObject(0);
                        JSONObject params = data.getJSONObject("params");
                        JSONObject content = data.getJSONArray("content").getJSONObject(0);
                        if (content.has("cnt_id")) {
                            String cnt_id = content.getString("cnt_id");
                            JSONObject cntJson = new JSONObject(new String(event.data.getByteArray(cnt_id), "utf-8"));
                            String sub = params.optString("sub");
                            if ("iat".equals(sub)) {
                                JSONObject result = cntJson.getJSONObject("text");
                                JSONArray ws = result.getJSONArray("ws");
                                Log.e(TAG, ws.get(0).toString() + ws.get(1).toString() + "--" + ws.length());
                                StringBuffer sb = new StringBuffer("");
                                for (int i = 0; i < ws.length(); i++) {
                                    result = new JSONObject(ws.get(i).toString());
                                    JSONArray cw = result.getJSONArray("cw");
                                    result = new JSONObject(cw.get(0).toString());
                                    Log.e("tznarry", result.getString("w"));
                                    sb.append(result.getString("w").toString());
                                }
                                // EventBus.getDefault().post(new MainMsg().setState(0).setMsg(sb.toString()).setAsk(true));
                                return;
                            }

                            if ("nlp".equals(sub)) {
                                JSONObject result = cntJson.getJSONObject("intent");
                                Log.e(TAG + "---->nlp", result.toString());
                                // 解析得到语义结果
                                String str = "";
                                String question = result.getString("text");
                                if(!question.equals("")) EventBus.getDefault().post(new MainMsg().setState(0).setMsg(question).setAsk(true));
                                anySaveLog saveLog = new anySaveLog();
                                saveLog.setSn(sn);
                                str = question;
                                saveLog.setQuestion(question);
                                saveLog.setTime(sdf.format(System.currentTimeMillis()));
                                //在线语义结果
                                if (result.optInt("rc") == 0) {
                                    isrc4=false;

                                    try {
                                        JSONObject answer = result.getJSONObject("answer");
                                        if (answer != null) {
                                            str = answer.getString("text");
                                        }
                                        try {
                                            JSONArray semantic = result.getJSONArray("semantic");
                                            JSONObject jsonObject = new JSONObject(semantic.get(0).toString());
                                            String intent = jsonObject.getString("intent");
                                            if(intent.equals("findfiles")||intent.equals("select")){
                                                bookSearch s=new bookSearch();
                                                s.setSearchTxt(jsonObject.getJSONArray("slots"));
                                                s.setIntent(intent);
                                                EventBus.getDefault().post(s);
                                                if(intent.equals("findfiles")){
                                                EventBus.getDefault().post(new goFragment().setI(4));
                                                }
                                            }
                                        }catch (Exception exception){

                                        }
                                    } catch (Exception e) {
                                        str = coustome(result);

                                    }
                                    saveLog.setAnswer(str);
                                } else if (result.optInt("rc") == 4) {
                                    if(isrc4){
                                        return;
                                    }
                                    if(question.contains("消毒")||question.contains("工作")){
                                        SetAreaBean setAreaBean = App.getDaoSession().getSetAreaBeanDao()
                                                .queryBuilder().list().get(0);
                                        if(setAreaBean!=null){
                                            String name=setAreaBean.getAreaname();
                                            speak1(String.format("请问你要我去消毒吗，您可以对我说去%s消毒",name));
                                            saveLog.setAnswer(String.format("请问你要我去消毒吗，您可以对我说去%s消毒",name));
                                            str=String.format("请问你要我去消毒吗，您可以对我说去%s消毒",name);
                                        }
                                    }else {
                                        str=NoKnows[new Random().nextInt(9)];
                                        speak1(str);
                                        saveLog.setAnswer("这个问题无法回答");
                                    }
                                    isrc4=true;
                                }else {
                                    saveLog.setAnswer("空");
                                }
                                if (!TextUtils.isEmpty(str)) {
                                    EventBus.getDefault().post(new MainMsg().setState(0).setMsg(str).setAsk(false));
                                } else {
                                    str = coustome(result);
                                    saveLog.setAnswer(str);
                                    EventBus.getDefault().post(new MainMsg().setState(0).setMsg(str).setAsk(false));
                                }
                                App.getDaoSession().getAnySaveLogDao().insertOrReplace(saveLog);
                            }
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                        Log.e("Throwable e", e.toString());
                    }

                }
                break;
                case AIUIConstant.EVENT_ERROR: {
                    //错误事件
                    Log.i(TAG, "on event: " + event.eventType + event.arg1 + "\n" + event.info);
                    EventBus.getDefault().post(new MainMsg().setState(0).setMsg("错误: " + event.arg1 + "\n" + event.info).setAsk(true));
                }
                break;

                case AIUIConstant.EVENT_VAD: {
                    //vad事件
                    if (AIUIConstant.VAD_BOS == event.arg1) {
                        //找到语音前端点
                        showTip("找到vad_bos");
                    } else if (AIUIConstant.VAD_EOS == event.arg1) {
                        //找到语音后端点
                        showTip("找到vad_eos");
                    } else {
                        showTip("" + event.arg2);
                    }
                }
                break;

                case AIUIConstant.EVENT_START_RECORD: {
                    //开始录音事件
                    Log.i(TAG, "on event: " + event.eventType);
                    showTip("开始录音");
                }
                break;

                case AIUIConstant.EVENT_STOP_RECORD: {
                    //停止录音事件
                    Log.i(TAG, "on event: " + event.eventType);
                    showTip("停止录音");
                }
                break;

                case AIUIConstant.EVENT_STATE: {    // 状态事件
                    mAIUIState = event.arg1;
                    Log.e("EVENT_STATE", mAIUIState + "");
                    if (AIUIConstant.STATE_IDLE == mAIUIState) {
                        // 闲置状态，AIUI未开启
                        showTip("STATE_IDLE");
                    } else if (AIUIConstant.STATE_READY == mAIUIState) {
                        // AIUI已就绪，等待唤醒
                        showTip("STATE_READY");
                    } else if (AIUIConstant.STATE_WORKING == mAIUIState) {
                        // AIUI工作中，可进行交互
                        showTip("STATE_WORKING");
                        if (aiuiWork != null) {
                            ToastUtil.showToast(aiService.this, "AIUI开始工作");
                            aiuiWork.run();
                            aiuiWork = null;
                        }
                    }
                }
                break;
                case AIUIConstant.EVENT_SLEEP:
                    Log.e(TAG, "SLEEP");
                    EventBus.getDefault().post(new socketEventbus().setType(SocketHead.sleep));
                    EventBus.getDefault().post(new MainMsg().setState(0).setMsg("clear"));
                    isAsring = false;
                    EventBus.getDefault().post("任务1");
                    if(isPW){
                        sendsocket(SocketType.PENWU);
                    }
                    break;
                case AIUIConstant.EVENT_TTS:
                    switch (event.arg1){
                        case AIUIConstant.TTS_SPEAK_COMPLETED:
                            //showTip("播放完成");
                            // EventBus.getDefault().post(new MainMsg().setState(0).setMsg("clear"));
                    }
                    break;
                default:
                    break;
            }
        }

    };

    private OnCaeOperatorlistener onCaeOperatorlistener = new OnCaeOperatorlistener() {

        @Override
        public void onAudio(byte[] audioData, int dataLen) {
            //正在识别中，aiui 在Working 状态时可以写入识别音频
            //正在识别中，aiui 在Working 状态时可以写入识别音频
            if (isAsring && mAIUIState == AIUIConstant.STATE_WORKING) {
                Log.e(TAG, mAIUIState + "送入音频事件");
                String params = "data_type=audio,sample_rate=16000";
                AIUIMessage msg = new AIUIMessage(AIUIConstant.CMD_WRITE, 0, 0, params, audioData);
                mAIUIAgent.sendMessage(msg);
            } else {
                Log.e(TAG, "未送入音频： mAIUIState =" + mAIUIState + "  isAsring： " + isAsring);
            }
        }

        @Override
        public void onWakeup(int angle, int beam) {
            Log.e("mAIUIAgent", mAIUIAgent == null ? "no" : "yes");
            isAsring = true;
            if (myPcmPlayer != null) {
                myPcmPlayer.stopPlay();
                AIUIMessage cancelTts = new AIUIMessage(AIUIConstant.CMD_TTS, 2, 0, "", null);
                mAIUIAgent.sendMessage(cancelTts);

            }
            //阵列唤醒事件回调，唤醒后需要发命令CMD_WAKEUP，通知AIUI 进入到Working 状态；
            AIUIMessage resetWakeupMsg = new AIUIMessage(
                    AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
            mAIUIAgent.sendMessage(resetWakeupMsg);
            Log.e(TAG, "角度：" + angle + "   波束：" + beam);
            //60是偏差
            angle = (angle) % 360;
            Log.e(TAG, "ANGLE" + angle);
            // CAE.CAESetRealBeam(2);
          /*  if(App.taskType==TaskType.Task||App.taskType==TaskType.Navigation){
                App.jniEAIBotUtil.navigateTargetControlB(2, App.handler, HandlerCode.NAVIGATE_TARGET_CONTROL_B);
                wakeupHandler.removeCallbacks(wakeupRunnable);
                wakeupHandler.postDelayed(wakeupRunnable,90000);
            }*/
          /*  if(App.targetName!=(String) spHelperUtil.getSharedPreference(Contants.CHARGE,"chongdian")&&App.targetName!=(String) spHelperUtil.getSharedPreference(Contants.addwater,"")) {//充电不旋转
                if (angle <= 180 && angle > 20) {
                    // CAE.CAESetRealBeam(beam);
                    App.jniEAIBotUtil.rotateBot(-angle, 0.6f, App.handler, HandlerCode.ROTATE_BOT);
                } else if (angle < 340 && angle > 180) {
                    App.jniEAIBotUtil.rotateBot(360 - angle, 0.6f, App.handler, HandlerCode.ROTATE_BOT);
                    // CAE.CAESetRealBeam(beam);
                }
            }*/

        }
    };

    private void initSerialOperator() {

        boolean result = SerialManager.INSTANCE.getOperator().openSerialPort("dev/ttyS4", 115200, new Function1<String, Unit>() {
            @Override
            public Unit invoke(String s) {
                SerialData data = new Gson().fromJson(s, SerialData.class);
                if (data != null && data.getParam1().getKeyword().equals("xiao3 fei1 xiao3 fei1")) {
                    Log.i(TAG, "监测到串口唤醒指令 angle = " + data.getParam1().getAngle());
                    ToastUtil.showToast(aiService.this, "监测到串口唤醒指令 =" + data.getParam1().getAngle());
                    serialWeak(Integer.parseInt(data.getParam1().getAngle()));
                }

                return null;
            }
        });
        Log.i(TAG, "串口打开 = " + result);
    }

    private void serialWeak(int angle){
        Log.e("mAIUIAgent", mAIUIAgent == null ? "no" : "yes");
        isAsring = true;
        if (myPcmPlayer != null) {
            myPcmPlayer.stopPlay();
            AIUIMessage cancelTts = new AIUIMessage(AIUIConstant.CMD_TTS, 2, 0, "", null);
            mAIUIAgent.sendMessage(cancelTts);

        }
        //阵列唤醒事件回调，唤醒后需要发命令CMD_WAKEUP，通知AIUI 进入到Working 状态；
        AIUIMessage resetWakeupMsg = new AIUIMessage(
                AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
        mAIUIAgent.sendMessage(resetWakeupMsg);

        aiuiWork = () -> {
            speak1("我在");

            //开始录音
            AIUIMessage msg = new AIUIMessage(AIUIConstant.CMD_START_RECORD, 0, 0, "data_type=audio,sample_rate=16000", null);
            mAIUIAgent.sendMessage(msg);
        };

        Log.e(TAG, "角度：" + angle + "   波束：" );
        //60是偏差
        angle = (angle) % 360;
        Log.e(TAG, "ANGLE" + angle);
        // CAE.CAESetRealBeam(2);
          /*  if(App.taskType==TaskType.Task||App.taskType==TaskType.Navigation){
                App.jniEAIBotUtil.navigateTargetControlB(2, App.handler, HandlerCode.NAVIGATE_TARGET_CONTROL_B);
                wakeupHandler.removeCallbacks(wakeupRunnable);
                wakeupHandler.postDelayed(wakeupRunnable,90000);
            }*/
        if (App.targetName.equals(spHelperUtil.getSharedPreference(Contants.CHARGE, "chongdian"))) {//充电不旋转
            if (angle <= 180 && angle > 20) {
                // CAE.CAESetRealBeam(beam);
                App.jniEAIBotUtil.rotateBot(-angle, 0.6f, App.handler, HandlerCode.ROTATE_BOT);
            } else if (angle < 340 && angle > 180) {
                App.jniEAIBotUtil.rotateBot(360 - angle, 0.6f, App.handler, HandlerCode.ROTATE_BOT);
                // CAE.CAESetRealBeam(beam);
            }
        }
    }

    private void speak(String str) {
        try {
            byte[] ttsData = str.getBytes("utf-8");  //转为二进制数据
            AIUIMessage startTts = new AIUIMessage(AIUIConstant.CMD_TTS, AIUIConstant.START, 0, params.toString(), ttsData);
            mAIUIAgent.sendMessage(startTts);
            EventBus.getDefault().post(new MainMsg().setState(0).setMsg(str).setAsk(false));
        } catch (UnsupportedEncodingException e) {
            Log.e("voice", e.toString());
            e.printStackTrace();
        }

    }

    private void speak1(String str) {
        try {
            byte[] ttsData = str.getBytes("utf-8");  //转为二进制数据
            AIUIMessage startTts = new AIUIMessage(AIUIConstant.CMD_TTS, AIUIConstant.START, 0, params.toString(), ttsData);
            mAIUIAgent.sendMessage(startTts);

        } catch (UnsupportedEncodingException e) {
            Log.e("voice", e.toString());
            e.printStackTrace();
        }

    }
    private void showTip(final String str) {
        Log.e(TAG + "tip", str);
    }

    private void initCaeEngine() {
        Log.d(TAG, "initCaeEngine");
        if (null == caeOperator) {
            caeOperator = new CaeOperator();
            caeOperator.initCAEInstance(onCaeOperatorlistener);
            Log.d(TAG, "initCaeEngine");
        } else {
            Log.d(TAG, "initCaeEngine is Init Done!");
        }
        caeOperator.startrecord();
    }
    public void initKwApi() {
        try {
            mKwapi = KWAPI.createKWAPI(this, "jjkedahuanan");
            if (mKwapi.bindAutoSdkService()) {
                Log.e(TAG, "绑定音乐盒后台服务成功");
            } else {
                Log.e(TAG, "绑定音乐盒后台服务失败");
            }
        } catch (Throwable T) {
        }


    }
    /**
     * 读取配置
     */
    private String getAIUIParams() {
        String params = "";

        AssetManager assetManager = getResources().getAssets();
        try {
            InputStream ins = assetManager.open("cfg/aiui_phone.cfg");
            byte[] buffer = new byte[ins.available()];

            ins.read(buffer);
            ins.close();

            params = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return params;
    }


    private String coustome(JSONObject result) {
        try {
            JSONArray semantic = result.getJSONArray("semantic");
            JSONObject jsonObject = new JSONObject(semantic.get(0).toString());
            String intent = jsonObject.getString("intent");
            JSONArray slots = jsonObject.getJSONArray("slots");
            String ttsStr1 = "";
            switch (intent) {
                case "indexSearch"://查询电量
                    for (int i = 0; i < slots.length(); i++) {
                        JSONObject jo = new JSONObject(slots.get(i).toString());
                        if (jo.getString("name").equals("waterdata")) {
                            ttsStr1 = String.format("当前机器人水量为%s", (String)spHelperUtil.getSharedPreference("water","中水量"));
                        }
                        if (jo.getString("name").equals("robotIndex")) {
                            ttsStr1 = String.format("当前机器人电量是百分之%s", batteryPercentage);
                        }
                    }
                    break;
                case "gotowork"://执行消毒
                    LogUtil.saveLog("语音执行消毒");
                    String targetname = "";
                    String jobType = "";
                    for (int i = 0; i < slots.length(); i++) {
                        JSONObject jo = new JSONObject(slots.get(i).toString());
                        if (jo.getString("name").equals("jobArea")) {
                            targetname = jo.getString("normValue");
                        }
                        if (jo.getString("name").equals("jobType")) {
                            jobType = jo.getString("normValue");
                        }
                    }
                    if (!targetname.equals("")) {
                        SetAreaBean setAreaBean = App.getDaoSession().getSetAreaBeanDao()
                                .queryBuilder().where(SetAreaBeanDao.Properties.Areaname.eq(targetname)).unique();
                        if (setAreaBean == null) {
                            ttsStr1 = String.format("目标区域无%s点", jobType);
                            break;
                        }

                       String targetnameList = setAreaBean.getAreaname();
                        List<Task> tasklist = new ArrayList<>();
                        ttsStr1 = String.format("好的,正在去%s%s", targetname, jobType);
                        sendsocket(SocketType.blue);
                        if (jobType.length() != 0) {
                            String water= (String) spHelperUtil.getSharedPreference("water","中水位");
                            if(water.equals("低水位")){
                                ttsStr1 = "当前喷雾消毒水较少,无法执行任务";
                                break;
                            }
                            App.taskType = TaskType.Task;
                            sendsocket(SocketType.PENWU);
                            isPW=true;
                        } else {
                            App.taskType = TaskType.Navigation;
                        }
                        List<SetAreaBean>setAreaBeans=App.getDaoSession().getSetAreaBeanDao().loadAll();
                        App.getDaoSession().getTaskDao().deleteAll();
                        aiService.sleepTime=0;
                        for(int i=0;i<setAreaBeans.size();i++){
                            if(targetnameList.equals(setAreaBeans.get(i).getAreaname())){
                                List<LocationBean> strings=setAreaBeans.get(i).getData();
                                for (int z=0;z<strings.size();z++){
                                    Task task=new Task();
                                    task.setTargetname(strings.get(z));
                                    tasklist.add(task);
                                }
                                break;
                            }
                        }

                        App.getDaoSession().getTaskDao().insertOrReplaceInTx(tasklist);
                        EventBus.getDefault().post("任务");;

                    } else {
                        ttsStr1 = String.format("去哪消毒", targetname);
                    }
                    break;
                case "disinfectResult"://消毒状况
                    ttsStr1 = "还么消毒呢";
                    break;
                case "EvalueSearch"://具体环境状况
                    String data = "";
                    for (int i = 0; i < slots.length(); i++) {
                        JSONObject jo = new JSONObject(slots.get(i).toString());

                        if (jo.getString("name").equals("environmentValue")) {
                            data = jo.getString("normValue");
                        }
                    }
                    Map map = SpHelperUtil.getHashMapData(aiService.this, Contants.SYSYTEMDATA);
                    if (!data.equals("")) {

                        switch (data) {
                            case "pm2.5":
                                ttsStr1 = String.format("室内%s为%s毫克每立方米", data, map.get("pm25"));
                                break;
                            case "甲醛":
                                ttsStr1 = String.format("室内%s为%s毫克每立方米", data, map.get("vvoc"));
                                break;
                            case "co2":
                                ttsStr1 = String.format("室内二氧化碳为%s ppm", data, map.get("co2"));
                                break;
                            case "pm10":
                                ttsStr1 = String.format("室内%s为%s毫克每立方米", data, map.get("pm10"));
                                break;
                            case "室内温度":
                                ttsStr1 = String.format("%s为%s摄氏度", data, map.get("tem"));
                                break;
                            case "湿度":
                                ttsStr1 = String.format("室内%s为%s RH", data, map.get("hum"));
                                break;
                            case "环境":
                                ttsStr1 = String.format("现在室内温度是%s摄氏度，湿度是%s，二氧化碳为%s ppm，甲醛为%s毫克每立方米，环境质量优", map.get("tem"),
                                        map.get("hum"), map.get("co2"), map.get("vvoc"));
                                break;
                        }

                    } else
                        ttsStr1 = String.format("现在室内温度是%s摄氏度，湿度是%s，二氧化碳为%s ppm，甲醛为%s毫克每立方米，环境质量优", map.get("tem"),
                                map.get("hum"), map.get("co2"), map.get("vvoc"));
                    break;
                case "exceptionEValue"://是否超标
                    ttsStr1 = "质量非常好,不用担心";
                    break;

                case "stopOrder"://停止任务
                    App.getDaoSession().getTaskDao().deleteAll();
                    aiService.sleepTime=0;
                    sendsocket(SocketType.StopPenwu);
                    ttsStr1 = "好的,任务已停止";
                    sendsocket(SocketType.blue);
                    LogUtil.saveLog("停止任务");
                    SlamManager.getInstance().cancelAction();
                    isPW=false;
                    break;
                case "Charge":
                    App.targetName = (String) spHelperUtil.getSharedPreference(Contants.CHARGE,"chongdian");;
                    App.taskType = TaskType.Charge;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpUtil.get(App.targetName);
                        }
                    }).start();
                    ttsStr1 = "好的,正在去充电";
                    sendsocket(SocketType.green);
                    sendsocket(SocketType.StopPenwu);
                    App.handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            upanySaveLog();
                        }
                    },2000);
                    LogUtil.saveLog("语音充电");
                    App.getDaoSession().getTaskDao().deleteAll();
                    aiService.sleepTime=0;
                    aiService.index=0;
                    EventBus.getDefault().post("充电");
                    break;
                case "automusic":
                    String music = "";
                    for (int i = 0; i < slots.length(); i++) {
                        JSONObject jo = new JSONObject(slots.get(i).toString());
                        if (jo.getString("name").equals("music")) {
                            music = jo.getString("normValue");
                        }
                    }
                    if (!music.equals("")) {
                        if(mKwapi==null){
                            break;
                        }
                        mKwapi.searchOnlineMusic(music, new OnSearchListener() {
                            @Override
                            public void searchFinshed(SearchStatus searchStatus, boolean b, List<Music> list, boolean b1) {
                                Log.e("music", searchStatus + "|" + b);

                                mKwapi.playMusic(list, 0, false, false);
                            }

                        });
                        ttsStr1 = String.format("好的,正在播放%s", music);

                    }
                    break;
                case "PowerAdjustment":
                    String adjustment = "";
                    String job = "";
                    for (int i = 0; i < slots.length(); i++) {
                        JSONObject jo = new JSONObject(slots.get(i).toString());
                        if (jo.getString("name").equals("adjustment")) {
                            adjustment = jo.getString("normValue");
                        }
                        if (jo.getString("name").equals("job")) {
                            job = jo.getString("normValue");
                        }
                    }
                    int wuliang = (int) spHelperUtil.getSharedPreference("wuliang", 150);
                    int wind = (int) spHelperUtil.getSharedPreference("wind", 150);
                    if (adjustment.equals("大")) {
                        wuliang += 30;
                        wind += 30;
                        ttsStr1 = String.format("好的,增大%s", job);
                    } else {
                        wuliang -= 30;
                        wind -= 30;
                        ttsStr1 = String.format("好的,减少%s", job);
                    }
                    if (wuliang > 220) wuliang = 220;
                    if (wuliang < 1) wuliang = 1;
                    if (wind > 250) wind = 250;
                    if (wind < 1) wind = 1;
                    if (job.equals("雾")) {
                        spHelperUtil.put("wuliang", wuliang);
                        sendsocket(SocketType.WULIANG);
                    } else {
                        spHelperUtil.put("wind", wind);
                        sendsocket(SocketType.WIND);
                    }

                    break;
                case "intlocation":
                   // ttsStr1="好的";
                 EventBus.getDefault().post(new goFragment().setI(5));
                  /*  if (mAIUIAgent != null) {
                        AIUIMessage startMsg = new AIUIMessage(
                                AIUIConstant.CMD_RESET_WAKEUP, 0, 0, "", null);
                        mAIUIAgent.sendMessage(startMsg);
                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //introduce();
                        }
                    },1000);*/
                    break;
                case "borrowBooks":
                    EventBus.getDefault().post(new goFragment().setI(2));
                    break;
                case "findfiles":
              /*      ttsStr1="好的,正在帮您找";
                    String search = "";
                    for (int i = 0; i < slots.length(); i++) {
                        JSONObject jo = new JSONObject(slots.get(i).toString());
                        if (jo.getString("name").equals("jobName")) {
                            search = jo.getString("normValue");
                        }
                    }
                    ttsStr1=String.format("好的,正在帮您找%s",search);
                    DanganSearch s=new DanganSearch();
                    s.setSearchTxt(search);
                    EventBus.getDefault().post(s);*/
                    break;
                case "cewen":
                    ttsStr1="好的";
                   goChushi();
                    break;
                default:
                    ttsStr1 = "抱歉,小益没听懂";

                    break;
            }
            if (!ttsStr1.equals("")) {
                speak1(ttsStr1);
                return ttsStr1;
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("ddddd",e.toString());
            return "json错误";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("ondestory", "ondestory");

        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        } if (mAIUIAgent != null) {
            AIUIMessage stopMsg = new AIUIMessage(AIUIConstant.CMD_STOP, 0, 0, null, null);
            mAIUIAgent.sendMessage(stopMsg);
            mAIUIAgent.destroy();
            mAIUIAgent = null;

        }
        if (caeOperator != null) {
            caeOperator.stopRecord();
        }
        if(mKwapi!=null){
        mKwapi.unbindAutoSdkService();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                SlamManager.getInstance().disconnect();
            }
        }).start();

        if (mapHelper != null) {
            mapHelper.destroy();
            mapHelper = null;
            myLocationBean.clear();
            myLocationBean=null;
        }

        SerialManager.INSTANCE.getOperator().closeSerialPort();


    }

    private void Logi(String msg) {
        Log.i(TAG, msg);
    }

    /*  private void sendMsg(String msg){
            Log.e(TAG,msg);
          EventBus.getDefault().post(msg);
      }*/
    private void init() {
        params = new StringBuffer();  //构建合成参数
        params.append("vcn=x2_xiaojuan");  //合成发音人
        params.append(",speed=40");  //合成速度
        params.append(",pitch=50");  //合成音调
        params.append(",volume=100");  //合成音量
    }
    private int lastId = 0;
    private List<Task> task;
    public static int index = 0;
    private long recordtime;




    private void sendsocket(int type) {
        switch (type) {
            case SocketType.PENWU:
                LogUtil.saveLog("service喷雾了");
                try {
                    OrderManage.getInstance().startPenWu(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.PENWU);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.StopPenwu:
                try {
                    OrderManage.getInstance().stopPenWu(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.StopPenwu);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.WIND:
                try {
                    OrderManage.getInstance().setWIND(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.WIND);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.WULIANG:
                try {
                    OrderManage.getInstance().setWULIANG(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.WULIANG);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.WATERDATA://水位数据
                try {
                    OrderManage.getInstance().getWaterData(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                                Log.e(TAG,"水位数据"+age+"--->"+batteryPercentage);
                                int water = 0;
                            try {
                               water = Integer.valueOf(age.substring(8, 10));
                            }catch (Exception e){

                            }

                                EventBus.getDefault().post(new waterAndpower().setPower(-1).setWater(water).setCharge(isCharge));
                                if (water == 0x02) {//0 和1表示水太少了
                                    if(isCharge){
                                        return;
                                    }else {//低水位时停止喷雾
                                        sendsocket(SocketType.StopPenwu);
                                        sendsocket(SocketType.red);
                                    }
                                    spHelperUtil.put("water","低水位");
                                    /*App.taskType = TaskType.Water;
                                    App.targetName = (String) spHelperUtil.getSharedPreference(Contants.CHARGE,"chongdian");
                                    App.jniEAIBotUtil.navigateTargetB(App.targetName, App.handler, HandlerCode.NAVIGATE_TARGET_B);*/
                                }
                                if(water==0x03){
                                    spHelperUtil.put("water","中水位");
                                }
                                if (water == 0x07) {
                                    sendsocket(SocketType.stopwater);
                                    spHelperUtil.put("water","高水位");
                                }
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.WATERDATA);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.startWater:
                client.sendMsg(D400);
                waterHandler.removeCallbacks(CanAddWatertask);
                waterHandler.postDelayed(CanAddWatertask,5000);

                break;
            case SocketType.stopwater:
                client.sendMsg(D402);
                waterHandler.removeCallbacks(watertask);
                App.taskType=TaskType.Other;
                break;
            case SocketType.red:
                try {
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
                }
                break;
            case SocketType.blue:
                try {
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
                }
                break;
            case SocketType.green:
                try {
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
                }
                break;
             case SocketType.CanAddWater:
                 try {
                     OrderManage.getInstance().CanAddWater(Contants.TYPE_REBORT, new ProductCallback() {
                         @Override
                         public void onDataReceived(int state, String age) {
                             if (state == ProductCallback.Sur) {
                                 int data = Integer.valueOf(age.substring(10, 12));
                                 //0x01可以加水 0x00禁止加水

                                 if(data==0x01){
                                     waterHandler.removeCallbacks(CanAddWatertask);
                                     waterHandler.removeCallbacks(watertask);
                                     waterHandler.post(watertask);
                                     String water= (String) spHelperUtil.getSharedPreference("water","中水位");
                                     if(water.equals("高水位")){
                                         waterHandler.removeCallbacks(stopwater);
                                         waterHandler.postDelayed(stopwater,10000);
                                     }else {
                                         waterHandler.removeCallbacks(outwater);
                                         waterHandler.postDelayed(outwater, 2000);
                                     }

                                 }
                             } else if (state == ProductCallback.TIMEOUT) {
                                 sendsocket(SocketType.CanAddWater);
                             }
                         }
                     }, Order.ExecutiveLevel.HIGH);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
                 break;
            case SocketType.OutWater:
                client.sendMsg(D401);
                waterHandler.removeCallbacks(stopwater);
                waterHandler.postDelayed(stopwater,10000);
                break;

        }

    }


    //设备sn号
    private String getSerialNumber() {
        String serial = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return serial;
    }

    private void upanySaveLog() {
        //语音日志
        anySaveLogs = App.getDaoSession().getAnySaveLogDao().loadAll();
        JSONArray jsonArray = new JSONArray();
        if (anySaveLogs.size() > 0) {
            for (int i = 0; i < anySaveLogs.size(); i++) {
                JSONObject mJson = new JSONObject();
                try {
                    mJson.put("deviceno", anySaveLogs.get(i).getSn());
                    mJson.put("qst", anySaveLogs.get(i).getQuestion());
                    mJson.put("ans", anySaveLogs.get(i).getAnswer());
                    mJson.put("time",anySaveLogs.get(i).getTime());
                    jsonArray.put(mJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonArray.toString());
            repossitory.anslogsave(body).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    App.getDaoSession().getAnySaveLogDao().deleteAll();
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Actions(String msg) {
       if(msg.equals("任务")){
           if(App.taskType==TaskType.Water){
               return;
           }
           App.taskType=TaskType.Task;
           task = App.getDaoSession().getTaskDao().loadAll();
           Log.e("ssssss",task.size()+"");
           if(task.size()>0){
               for(int i=0;i<task.size();i++){
                   if(task.get(i).getState()==0){
                       index=i;
                       int finalI = i;
                       new Thread(new Runnable() {
                           @Override
                           public void run() {
                               Log.e("ssssss",task.get(finalI).getTargetname().getX()+"");
                               SlamManager.getInstance().moveTo(task.get(finalI).getTargetname(),null, 3000, onNavigateListener);
                           }
                       }).start();

                   return;
                   }
               }
               isPW=false;
               aiService.sleepTime=0;
               goChushi();
           }
       }
           else if(msg.equals("任务1")){
               if(App.taskType==TaskType.Water){
                   return;
               }
               App.taskType=TaskType.Task;
               task = App.getDaoSession().getTaskDao().loadAll();
               if(task.size()>0){
                   for(int i=0;i<task.size();i++){
                       if(task.get(i).getState()==0){
                           index=i;
                           int finalI = i;
                           new Thread(new Runnable() {
                               @Override
                               public void run() {
                                   SlamManager.getInstance().moveTo(task.get(finalI).getTargetname(),null, 3000, onNavigateListener);
                               }
                           }).start();


                           return;
                       }
                   }

               }
               isPW=false;
           aiService.sleepTime=0;
           }
       else if(msg.equals("充电")){
           isPW=false;
           App.taskType=TaskType.Charge;
           if(!isCharge)
           SlamManager.getInstance().goHome();
           LogUtil.saveLog("充电执行中");
           //测试充电中加水
          /* new Thread(new Runnable() {
               @Override
               public void run() {
                   while (!isCharge){
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }

                       if(isCharge){
                           EventBus.getDefault().post("加水");
                           break;
                       }
                   }
               }
           }).start();*/
       }else if(msg.equals("加水")){
           isPW=false;
           App.taskType=TaskType.Water;
           if(!isCharge){
           SlamManager.getInstance().goHome();
           new Thread(new Runnable() {
               @Override
               public void run() {
                   while (!isCharge){
                       try {
                           Thread.sleep(1000);
                       } catch (InterruptedException e) {
                           e.printStackTrace();
                       }

                       if(isCharge){
                           waterHandler.removeCallbacks(addwater);
                           waterHandler.post(addwater);
                           break;
                       }
                   }
               }
           }).start();

           }else {
               waterHandler.removeCallbacks(addwater);
               waterHandler.post(addwater);

           }
       }else {
           toast(msg);
       }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectSuccessEvent(ConnectSuccessEvent event) {
        toast("连接成功");
        Log.e(TAG,"ConnectSuccessEvent");
        mapHelper = new MapHelper(new WeakReference<Context>(this), new WeakReference<Handler>(handler));
        mapHelper.updateMap();
        changmap();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void speaktct(Speaktxt event) {
        if(event.getTxt().equals("")||event.getTxt()==null){
            EventBus.getDefault().post(new SleepOrWork().setI(0));
        }  else {
        speak1(event.getTxt());
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void locationEvent(locationEvent event) {
        int state=event.getStatue();
        if(state==0){
            //handler.removeCallbacks(changemaprunable);
        }else {
            //handler.removeCallbacks(relocationrunable);
        }
    }
    //语音唤醒和停止
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SleepOrWork(SleepOrWork event) {
       if(event.getI()==0){
           AIUIMessage resetWakeupMsg = new AIUIMessage(
                   AIUIConstant.CMD_RESET_WAKEUP, 0, 0, "", null);
           mAIUIAgent.sendMessage(resetWakeupMsg);
           AIUIMessage cancelTts = new AIUIMessage(AIUIConstant.CMD_TTS,AIUIConstant.CANCEL, 0, "", null);
           mAIUIAgent.sendMessage(cancelTts);

       }else {
           AIUIMessage resetWakeupMsg = new AIUIMessage(
                   AIUIConstant.CMD_WAKEUP, 0, 0, "", null);
           mAIUIAgent.sendMessage(resetWakeupMsg);
           CAE.CAESetRealBeam(0);


       }
    }
    //切换地图
    private void changmap(){

        String data1=(String) spHelperUtil.getSharedPreference("mapname","");
        if(data1.equals("")){
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                SlamManager.getInstance().loadMapInThread(BaseConstant.getMapNumPath(aiService.this, data1), new OnFinishListener<List<LocationBean>>() {
                    @Override
                    public void onFinish(List<LocationBean> data) {
                        handler.obtainMessage(changemapSuccess,data).sendToTarget();
                        MapName name=new MapName();
                        name.setMapName(data1);
                        EventBus.getDefault().post(name);

                        // relocation();
                    }

                    @Override
                    public void onError () {
                        handler.obtainMessage(changemapFail).sendToTarget();

                    }
                });
            }
        }).start();





    }
    //重定位
    private void relocation(){
        SlamManager.getInstance().recoverLocationByDefault(new OnResultListener<Boolean>() {
            @Override
            public void onResult(Boolean data) {
                if(data){
                    handler.obtainMessage(relocationSuccess).sendToTarget();
                }else {
                    handler.obtainMessage(relocationFail).sendToTarget();
                    handler.postDelayed(relocationrunable,1000*10);
                }
            }
        });
    }
    private Runnable changemaprunable=new Runnable() {
        @Override
        public void run() {
            changmap();
        }
    };
    private Runnable relocationrunable=new Runnable() {
        @Override
        public void run() {
            relocation();
        }
    };
    private void uprobortstate() {
        //机器人状态更新
        JSONObject mJson = new JSONObject();
        try {
            mJson.put("deviceno",sn);
            mJson.put("online", "在线");
            mJson.put("waterlevel", (String)spHelperUtil.getSharedPreference("water","中水量"));
            mJson.put("power",batteryPercentage+"");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), mJson.toString());
        repossitory.robotstatsave(body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                onlineMode onlineMode=new onlineMode();
                onlineMode.setIsonLine(true);
                EventBus.getDefault().post(onlineMode);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                onlineMode onlineMode=new onlineMode();
                onlineMode.setIsonLine(false);
                EventBus.getDefault().post(onlineMode);
            }
        });

    }
    private OnNavigateListener onNavigateListener=new OnNavigateListener() {
        @Override
        public void onNavigateStartTry() {
        }

        @Override
        public void onNavigateRemind() {
            EventBus.getDefault().post("导航重试中");

        }

        @Override
        public void onNavigateSensorTrigger(boolean b) {

        }

        @Override
        public void onNavigateError() {
            EventBus.getDefault().post(new danganEventbus());
            EventBus.getDefault().post("导航异常");
        }

        @Override
        public void onNavigateResult(boolean b) {
            EventBus.getDefault().post(new danganEventbus());
            if(App.taskType==TaskType.Task||App.taskType==TaskType.Navigation){
                task.get(index).setState(1);
                App.getDaoSession().getTaskDao().update(task.get(index));

                if(b){
                    EventBus.getDefault().post("导航成功");
                } else {
                    EventBus.getDefault().post("导航失败,正在前往下一个点");
                }
                handler.removeCallbacks(taskRunable);

                handler.postDelayed(taskRunable,sleepTime*1000);



            }
        }
    };

    //去初始迎宾点
    private void goChushi(){
        sendsocket(SocketType.StopPenwu);
        App.taskType=TaskType.Other;
        String startname=(String)spHelperUtil.getSharedPreference(Contants.startName, "a1");
        if(startname.equals("")){
            return;
        }
        try{
            for(int i =0;i<myLocationBean.size();i++){
                if(startname.equals(myLocationBean.get(i).getLocationNumber())){
                    int finalI = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SlamManager.getInstance().moveTo(myLocationBean.get(finalI),null, 3000, onNavigateListener);
                        }
                    }).start();
                    return;
                }
            }
        }catch (Exception e){

        }

    }
}

