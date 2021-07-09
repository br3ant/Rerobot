package com.bayi.rerobot.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bayi.rerobot.App;
import com.bayi.rerobot.service.aiService;
import com.bayi.rerobot.util.AppManager;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.ScreenTimer;
import com.bayi.rerobot.util.SpHelperUtil;

import org.greenrobot.eventbus.EventBus;



public abstract class BaseActivity extends AppCompatActivity {
    private Toast mToast;

    public void toast(String msg){
        if (mToast != null) {
            mToast.cancel();
        }else {

        }
        mToast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
/*        LinearLayout layout = (LinearLayout) mToast.getView();
        TextView tv = (TextView) layout.getChildAt(0);
        tv.setTextSize(40);
        mToast.setGravity(Gravity.CENTER, 0, 0);*/
        mToast.show();

    }
    public abstract void initView();
    /**
     * 开启服务
     */



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // hideBottomUIMenu(this);
        AppManager.getAppManager().addActivity(this);
        initView();

    }
    protected void hideBottomUIMenu(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(0);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
    @Override
    protected void onResume() {
        super.onResume();
        //startService(new Intent(this,aiService.class));

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }

    }

    public void getMsgFail(){
        toast("获取数据失败");
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(ev.getAction() == MotionEvent.ACTION_UP){
            ScreenTimer.eliminateEvent();
        }
        return super.dispatchTouchEvent(ev);
    }
}
