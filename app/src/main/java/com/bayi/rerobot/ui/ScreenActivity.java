package com.bayi.rerobot.ui;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.ImageAdapter;
import com.bayi.rerobot.bean.DataBean;
import com.bayi.rerobot.util.ScreenTimer;
import com.bayi.rerobot.utilView.PassWordLayout;
import com.youth.banner.Banner;
import com.youth.banner.config.BannerConfig;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.util.BannerUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScreenActivity extends BaseActivity {

    @BindView(R.id.banner)
    Banner banner;

    @Override
    public void initView() {

        setContentView(R.layout.activity_screen);
    }

    private PassWordLayout pass;
    private String password = "7656";
    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            pass.removeAllPwd();
            pass.setVisibility(View.GONE);
            hideKeyboard();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        pass = (PassWordLayout) findViewById(R.id.pa3);
        pass.setPwdChangeListener(new PassWordLayout.pwdChangeListener() {
            @Override
            public void onChange(String pwd) {
                Log.e("密码改变", pwd);
                if (pass.isShown()) {
                    mHandler.removeCallbacks(mRunnable);
                    mHandler.postDelayed(mRunnable, 1000 * 10);
                }
            }

            @Override
            public void onNull() {
                Log.e("密码改变", "null");
            }

            @Override
            public void onFinished(String pwd) {
                Log.e("密码改变", "结束" + pwd);
                if (pwd.length() == 4) {
                    if (password.equals(pwd)) {
                        ScreenActivity.this.finish();
                    } else {
                        pass.removeAllPwd();
                        App.toast("密码错误");
                    }
                }
            }
        });
      useBanner();

    }
    private void useBanner() {
        banner.addBannerLifecycleObserver(this)//添加生命周期观察者
                .setAdapter(new ImageAdapter(DataBean.getData()))
                .setIndicatorMargins(new IndicatorConfig.Margins(0, 0,
                        BannerConfig.INDICATOR_MARGIN, (int) BannerUtils.dp2px(12)))

                .setIndicator(new CircleIndicator(this));
        banner.setLoopTime(10000);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP ) {

            ScreenActivity.this.finish();
        }
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            //do something.
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //开始轮播
        banner.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止轮播
        banner.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ScreenTimer.startMonitor();
        ScreenTimer.eliminateEvent();
        mHandler.removeCallbacks(mRunnable);
        banner.destroy();

    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(pass, 0);
        }

    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(pass.getWindowToken(), 0);
        }

    }
}