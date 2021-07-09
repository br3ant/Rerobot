package com.voice.osCaeHelper;

import android.content.Context;
import android.util.Log;

import com.bayi.rerobot.App;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.SpHelperUtil;
import com.iflytek.iflyos.cae.CAE;
import com.iflytek.iflyos.cae.ICAEListener;
import com.voice.caePk.OnCaeOperatorlistener;
import com.voice.caePk.util.FileUtil;

import java.io.File;


public  class CaeCoreHelper {
    final static String TAG      = "CaeCoreHelper";
    //添加资源文件自动copy 到对应目录下
    private  static  String modeFileName ="res.bin";
    private static  String pramFileName = "hlw.param";
    private  static  String iniPramFielName = "hlw.ini";
    private  static  String caeModelName = "res_cae_model.bin";
    private  static  String ivwModelName = "res_ivw_model.bin";

    private  static  String mWorkDir =  "/sdcard/cae/";
    private  static String mResPath = "/sdcard/cae/"+modeFileName;  //资源文件路径
    private  static  String mPramPath =  "/sdcard/cae/"+pramFileName;



    private   static    String mResdir = "/sdcard/cae/";  //资源文件路径


    private String mHlwIni="/sdcard/cae/hlw.ini";

    // 授权文件
    String hlw="/sdcard/cae/hlw.param";


    private String osAutn = "f3917435-d52a-4bd2-9a38-51f437f9cfbf";//boer zhineng

    private boolean isUseOsV2 = false;

    private OnCaeOperatorlistener caeOperatorlistener;
    public CaeCoreHelper(OnCaeOperatorlistener listener, boolean is2Mic){
        this.caeOperatorlistener = listener;
        EngineInit(is2Mic);
    }


    public static  void portingFile(Context context){
        FileUtil.CopyAssets2Sdcard(context, CaeCoreHelper.getModeFIleName(), CaeCoreHelper.getModeFilePath());
        FileUtil.CopyAssets2Sdcard(context, CaeCoreHelper.getPramFIleName(), CaeCoreHelper.getHlwPramFilePath());
        FileUtil.CopyAssets2Sdcard(context,iniPramFielName, mWorkDir+iniPramFielName);
        FileUtil.CopyAssets2Sdcard(context,caeModelName, mWorkDir+caeModelName);
        FileUtil.CopyAssets2Sdcard(context,ivwModelName, mWorkDir+ivwModelName);
    }


    public static boolean hasModeFile(){
        File file = new File(mResPath);
        if (file !=null) {
            return file.exists();
        }
        return false;
    }
    public static  String getModeFilePath(){
        File dir = new File(mResdir);
        if (dir!=null && !dir.exists()){
            dir.mkdirs();

        }
        return mResPath;
    }
    public static  String getHlwPramFilePath(){
        File dir = new File(mResdir);
        if (dir!=null && !dir.exists()){
            dir.mkdirs();

        }
        return mPramPath;
    }

    public static String getModeFIleName(){
        return modeFileName;
    }

    public static String getPramFIleName(){
        return pramFileName;
    }


    private SpHelperUtil spHelperUtil;
    public boolean EngineInit(boolean is2MIc){
        boolean rst = false;
        Log.d(TAG,"EngineInit in");
        CAE.loadLib();
        spHelperUtil=new SpHelperUtil(App.context);
        int isInit = CAE.CAENew(mHlwIni,hlw, mCAEListener);

        //iflyos 正式版本
        //int isInit = CAE.CAENew(mResPath,hlw,mCAEListener);
        String ver = CAE.CAEGetVersion();
        Log.d(TAG,"EngineInit  result:  " +isInit +"version:"+ver);
        rst = (isInit == 0)? true:false;
        osAutn=(String) spHelperUtil.getSharedPreference(Contants.IOT_osautn,"60da7bdd-78e4-4b39-9f0f-11b20543cc2c");
        Log.e(TAG+"osAutn",osAutn);
        int auth =  CAE.CAEAuth(osAutn);
        if (auth ==0 ){
            Log.d(TAG, "鉴权成功");
        }else{
            Log.d(TAG, "鉴权失败："+auth);
            return  false;
        }
        CAE.CAESetShowLog(0);
        CAE.CAESetRealBeam(-1);
        return rst;
    }

    public  void reLoadResource(String modeFilePath){
        CAE.CAEReloadResource(modeFilePath);
    }

    //送入原始音频到算法中
    public void writeAudio(byte[] audio){
        CAE.CAEAudioWrite(audio, audio.length);
    }
    //重置引擎，需要初始化引擎
    public void ResetEngine(){

    }

    public void DestoryEngine(){
        CAE.CAEDestory();
    }

    //os yue kuo banben
    private ICAEListener mCAEListener = new ICAEListener() {
        @Override
        public void onWakeup(float power, int angle, int beam) {
            CAE.CAESetRealBeam(beam);
            if (caeOperatorlistener!=null){

                caeOperatorlistener.onWakeup(angle,beam);
            }
        }

        @Override
        public void onAudioCallback(byte[] audioData, int dataLen) {
            //Log.d(TAG,"onAudioCallback  "+dataLen);
            if (caeOperatorlistener!=null) {
                caeOperatorlistener.onAudio(audioData, dataLen);
            }
        }

        @Override
        public void onIvwAudioCallback(byte[] bytes, int i) {

        }
    };


  /*iflyos banben
    private ICAEListener mCAEListener = new ICAEListener() {
        @Override
        public void onWakeup(float power, int angle, int beam) {
            CAE.CAESetRealBeam(beam);
            if (caeOperatorlistener!=null){

                caeOperatorlistener.onWakeup(angle,beam);
            }
        }

        @Override
        public void onAudioCallback(byte[] audioBuffer, int length) {
            if (caeOperatorlistener!=null) {
                caeOperatorlistener.onAudio(audioBuffer, length);
            }
        }

        @Override
        public void onIvwAudioCallback(byte[] audioBuffer, int length) {

        }
    };
    */

}
