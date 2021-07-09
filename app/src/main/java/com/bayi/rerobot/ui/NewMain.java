package com.bayi.rerobot.ui;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.chatAdapter;
import com.bayi.rerobot.bean.ChatMsg;
import com.bayi.rerobot.bean.Cwjson;
import com.bayi.rerobot.bean.IsPw;
import com.bayi.rerobot.bean.MainMsg;
import com.bayi.rerobot.bean.MapName;
import com.bayi.rerobot.bean.SleepOrWork;
import com.bayi.rerobot.bean.goFragment;
import com.bayi.rerobot.bean.onlineMode;
import com.bayi.rerobot.bean.powerUi;
import com.bayi.rerobot.bean.waterAndpower;
import com.bayi.rerobot.fragment.Area1Fragment_9;
import com.bayi.rerobot.fragment.Area2Fragment_10;
import com.bayi.rerobot.fragment.MapSetFragment_15;
import com.bayi.rerobot.fragment.ambientFragment_18;
import com.bayi.rerobot.fragment.bookstoreDetailFragment_6;
import com.bayi.rerobot.fragment.bookstoreFragment_2;
import com.bayi.rerobot.fragment.bookstoreResultFragment_4;
import com.bayi.rerobot.fragment.bookstoreSearchFragment_3;
import com.bayi.rerobot.fragment.cmdFragment_19;
import com.bayi.rerobot.fragment.consultFragment_1;
import com.bayi.rerobot.fragment.daolanFragment_16;
import com.bayi.rerobot.fragment.introduceFragment_5;
import com.bayi.rerobot.fragment.newMainFragment_0;
import com.bayi.rerobot.fragment.reportFragment_17;
import com.bayi.rerobot.fragment.setFragment_8;
import com.bayi.rerobot.fragment.temFragment_11;
import com.bayi.rerobot.fragment.timer1Fragment_12;
import com.bayi.rerobot.fragment.timer2Fragment_13;
import com.bayi.rerobot.fragment.timer3Fragment_14;
import com.bayi.rerobot.fragment.xiaoduFragment_7;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.SpHelperUtil;
import com.bayi.rerobot.utilView.BatteryState;
import com.bayi.rerobot.utilView.PassWordLayout;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;


public class NewMain extends BaseActivity {

    @BindView(R.id.img_net)
    ImageView imgNet;
    @BindView(R.id.img_water)
    ImageView imgWater;
    @BindView(R.id.img_gps)
    ImageView imgGps;
    @BindView(R.id.img_cewen)
    ImageView imgCewen;
    @BindView(R.id.img_xiaodu)
    ImageView imgXiaodu;
    @BindView(R.id.BatteryState)
    BatteryState BatteryState;
    @BindView(R.id.battertxt)
    TextView battertxt;
    @BindView(R.id.tips_1)
    TextView tips1;
    @BindView(R.id.tips_2)
    TextView tips2;
    @BindView(R.id.tips_3)
    TextView tips3;
    @BindView(R.id.tips_4)
    TextView tips4;
    @BindView(R.id.tips_5)
    TextView tips5;
    @BindView(R.id.lin_sleep)
    ConstraintLayout linSleep;
    @BindView(R.id.linTip)
    LinearLayout linTip;
    @BindView(R.id.aiView)
    RecyclerView aiView;
    @BindView(R.id.gif_wave)
    GifImageView gifWave;
    @BindView(R.id.chat_layout)
    View myChat;
    @BindView(R.id.pa3)
    PassWordLayout pass;
    @BindView(R.id.mapname)
    TextView mapname;
    @BindView(R.id.lin)
    LinearLayout lin;
    @BindView(R.id.main_content)
    FrameLayout mainContent;
    @BindView(R.id.txt_sleep)
    TextView txtSleep;
    @BindView(R.id.img_start)
    ImageView imgStart;
    @BindView(R.id.img_stop)
    ImageView imgStop;
    @BindView(R.id.t1)
    TextView t1;
    @BindView(R.id.t2)
    TextView t2;
    @BindView(R.id.lighting)
    ImageView lighting;


    private boolean chatShow = true;
    private Handler mHandler = new Handler();
    private chatAdapter adapter;
    private List<ChatMsg> chatMsg = new ArrayList<>();
    private String password = "7656";
    private SpHelperUtil spHelperUtil;
    private PopupWindow popupWindow;
    private TextView textView;
    public static String presentMap = "";
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            pass.removeAllPwd();
            pass.setVisibility(View.GONE);
            hideKeyboard();

        }
    };
    private Runnable mRunable = new Runnable() {
        @Override
        public void run() {
            chatMsg.clear();
            adapter.notifyDataSetChanged();
            linSleep.setVisibility(View.VISIBLE);
            linTip.setVisibility(View.GONE);
            aiView.setVisibility(View.GONE);
        }
    };

    @Override
    public void initView() {
        setContentView(R.layout.activity_new_main);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setFragmentView(0);
        EventBus.getDefault().register(this);
        setObjectAnimator(tips1);
        setObjectAnimator(tips4);
        spHelperUtil = new SpHelperUtil(this);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setObjectAnimator(tips2);
            }
        }, 1500);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setObjectAnimator(tips3);
            }
        }, 2500);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                setObjectAnimator(tips5);
            }
        }, 1500);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        aiView.setLayoutManager(layoutManager);
        adapter = new chatAdapter(chatMsg);
        aiView.setAdapter(adapter);
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
                    String p = (String) spHelperUtil.getSharedPreference(Contants.MIMA, "7656");
                    if (p.equals(pwd)) {
                        pass.removeAllPwd();
                        pass.setVisibility(View.GONE);
                        hideKeyboard();
                        //new NewTaskDialog.Builder(NewMain.this).create().show();
                        setFragmentView(7);
                    } else {
                        pass.removeAllPwd();
                        App.toast("密码错误");
                    }
                }
            }
        });
        textView = new TextView(this);
        textView.setGravity(Gravity.CENTER);

    }

    private void setObjectAnimator(TextView t) {
        if (t.getVisibility() == View.GONE) {
            t.setVisibility(View.VISIBLE);

        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(t, "alpha", 0f, 1f, 0f);
        animator.setDuration(4000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.start();
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(t, "translationY", 0, -200);
        animator1.setDuration(4000);
        animator1.setRepeatCount(ValueAnimator.INFINITE);
        animator1.start();
    }

    @OnClick({R.id.img_start, R.id.img_stop})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_start:
                EventBus.getDefault().post(new SleepOrWork().setI(1));
                break;
            case R.id.img_stop:
                EventBus.getDefault().post(new SleepOrWork().setI(0));
                break;
        }
    }

    //网络在线
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLine(onlineMode mode) {
        if (mode.isIsonLine()) {
            Glide.with(this).load(R.mipmap.net0).into(imgNet);
        } else {
            Glide.with(this).load(R.mipmap.net1).into(imgNet);
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void powerUi(powerUi pw) {

        if (pw.isCharge()) {
            if (lighting.getVisibility() == View.GONE) {
                lighting.setVisibility(View.VISIBLE);
            }
        } else {
            if (lighting.getVisibility() == View.VISIBLE) {
                lighting.setVisibility(View.GONE);

            }
        }
    }
    //喷雾状态
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pw(IsPw pw) {
        if (pw.isPw()) {
            Glide.with(this).load(R.mipmap.xiaodu0).into(imgXiaodu);
        } else {
            Glide.with(this).load(R.mipmap.xiaodu1).into(imgXiaodu);
        }

    }

    //水位和电量
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(waterAndpower msg) {
        if (msg.getPower() != -1) {
            BatteryState.setPower(msg.getPower());
            battertxt.setText(msg.getPower() + "");
        } else {
            if (msg.getWater() == 0x02) {//0 和1表示水太少了

                Glide.with(this).load(R.mipmap.water2).into(imgWater);
            }
            if (msg.getWater() == 0x03) {
                Glide.with(this).load(R.mipmap.water1).into(imgWater);
            }
            if (msg.getWater() == 0x07) {
                Glide.with(this).load(R.mipmap.water0).into(imgWater);
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(MainMsg msg) {
        Log.e("ssss", msg.getMsg());
        if (msg.getMsg().equals("clear")) {
            mHandler.removeCallbacks(mRunable);
            mHandler.postDelayed(mRunable, 15000);
            gifWave.setVisibility(View.GONE);
            return;
        } else {
            mHandler.removeCallbacks(mRunable);
            if (gifWave.getVisibility() == View.GONE) {
                gifWave.setVisibility(View.VISIBLE);
            }
            linSleep.setVisibility(View.GONE);
            linTip.setVisibility(View.VISIBLE);
            aiView.setVisibility(View.VISIBLE);
            ChatMsg c = new ChatMsg();
            c.setLeft(msg.isAsk());
            c.setMsg(msg.getMsg());
            chatMsg.add(c);
        }
        adapter.notifyItemInserted(chatMsg.size() - 1);
        aiView.scrollToPosition(chatMsg.size() - 1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cwjson(Cwjson cwjson) {
        Glide.with(this).load(R.mipmap.wd0).into(imgCewen);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void mapName(MapName mapName) {
        presentMap = mapName.getMapName();
        mapname.setText("当前地图: ".concat(mapName.getMapName()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void goFragment(goFragment msg) {
        setFragmentView(msg.getI());
    }

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private newMainFragment_0 newMainFragment;
    private consultFragment_1 consultFragment;
    private bookstoreFragment_2 bookstoreFragment;
    private bookstoreSearchFragment_3 bookstoreSearchFragment;
    private bookstoreResultFragment_4 bookstoreResultFragment;
    private introduceFragment_5 introduceFragment;
    private bookstoreDetailFragment_6 bookstoreDetailFragment;
    private xiaoduFragment_7 xiaoduFragment;
    private setFragment_8 setFragment;
    private Area1Fragment_9 Area1Fragment;
    private Area2Fragment_10 Area2Fragment;
    private temFragment_11 temFragment;
    private timer1Fragment_12 timer1Fragment;
    private timer2Fragment_13 timer2Fragment;
    public timer3Fragment_14 timer3Fragment;
    private MapSetFragment_15 MapSetFragment;
    private daolanFragment_16 daolanFragment;
    private reportFragment_17 reportFragment;
    private ambientFragment_18 ambientFragment;
    private cmdFragment_19 cmdFragment;

    public void setFragmentView(int i) {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        if (newMainFragment != null) fragmentTransaction.hide(newMainFragment);
        if (consultFragment != null) fragmentTransaction.hide(consultFragment);
        if (bookstoreFragment != null) fragmentTransaction.hide(bookstoreFragment);
        if (bookstoreSearchFragment != null) fragmentTransaction.hide(bookstoreSearchFragment);
        if (bookstoreResultFragment != null) fragmentTransaction.hide(bookstoreResultFragment);
        if (introduceFragment != null) fragmentTransaction.hide(introduceFragment);
        if (bookstoreDetailFragment != null) fragmentTransaction.hide(bookstoreDetailFragment);
        if (xiaoduFragment != null) fragmentTransaction.hide(xiaoduFragment);
        if (setFragment != null) fragmentTransaction.hide(setFragment);
        if (Area1Fragment != null) fragmentTransaction.hide(Area1Fragment);
        if (Area2Fragment != null) fragmentTransaction.hide(Area2Fragment);
        if (temFragment != null) fragmentTransaction.hide(temFragment);
        if (timer1Fragment != null) fragmentTransaction.hide(timer1Fragment);
        if (timer2Fragment != null) fragmentTransaction.hide(timer2Fragment);
        if (timer3Fragment != null) fragmentTransaction.hide(timer3Fragment);
        if (MapSetFragment != null) fragmentTransaction.hide(MapSetFragment);
        if (daolanFragment != null) fragmentTransaction.hide(daolanFragment);
        if (reportFragment != null) fragmentTransaction.hide(reportFragment);
        if (ambientFragment != null) fragmentTransaction.hide(ambientFragment);
        if (cmdFragment != null) fragmentTransaction.hide(cmdFragment);
        if (i != 2 && i != 4 && i != 5 && i != 7 && i != 8
                && i != 9 && i != 10 && i != 11 && i != 12
                && i != 13 && i != 14 && i != 15 && i != 16
                && i != 17 && i != 18 && i != 19) {
            if (!chatShow) {
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(myChat, "translationY", 750, 0);
                animator1.setDuration(500);
                animator1.start();
                chatShow = true;
                linTip.setVisibility(View.GONE);
                linSleep.setVisibility(View.VISIBLE);
                aiView.setVisibility(View.GONE);
            }
        } else {
            if (chatShow) {
                ObjectAnimator animator1 = ObjectAnimator.ofFloat(myChat, "translationY", 0, 750);
                animator1.setDuration(500);
                animator1.start();
                chatShow = false;
                linTip.setVisibility(View.VISIBLE);
                aiView.setVisibility(View.VISIBLE);
                linSleep.setVisibility(View.GONE);
            }
        }
        switch (i) {
            case 0://
                if (newMainFragment == null) {
                    newMainFragment = new newMainFragment_0();
                    fragmentTransaction.add(R.id.main_content, newMainFragment);
                }
                fragmentTransaction.show(newMainFragment);
                break;
            case 1://
                if (consultFragment == null) {
                    consultFragment = new consultFragment_1();
                    fragmentTransaction.add(R.id.main_content, consultFragment);
                }
                fragmentTransaction.show(consultFragment);
                break;
            case 2://
                if (bookstoreFragment == null) {
                    bookstoreFragment = new bookstoreFragment_2();
                    fragmentTransaction.add(R.id.main_content, bookstoreFragment);
                }
                fragmentTransaction.show(bookstoreFragment);
                break;
            case 3://图书搜索
                if (bookstoreSearchFragment == null) {
                    bookstoreSearchFragment = new bookstoreSearchFragment_3();
                    fragmentTransaction.add(R.id.main_content, bookstoreSearchFragment);
                }
                fragmentTransaction.show(bookstoreSearchFragment);
                break;
            case 4://图书搜索结果
                if (bookstoreResultFragment == null) {
                    bookstoreResultFragment = new bookstoreResultFragment_4();
                    fragmentTransaction.add(R.id.main_content, bookstoreResultFragment);
                }
                fragmentTransaction.show(bookstoreResultFragment);
                break;
            case 5://介绍
                if (introduceFragment == null) {
                    introduceFragment = new introduceFragment_5();
                    fragmentTransaction.add(R.id.main_content, introduceFragment);
                }
                fragmentTransaction.show(introduceFragment);
                break;
            case 6://图书查询详情
                if (bookstoreDetailFragment == null) {
                    bookstoreDetailFragment = new bookstoreDetailFragment_6();
                    fragmentTransaction.add(R.id.main_content, bookstoreDetailFragment);
                }
                fragmentTransaction.show(bookstoreDetailFragment);
                break;
            case 7://消毒
                if (xiaoduFragment == null) {
                    xiaoduFragment = new xiaoduFragment_7();
                    fragmentTransaction.add(R.id.main_content, xiaoduFragment);
                }
                fragmentTransaction.show(xiaoduFragment);
                break;
            case 8://设置
                if (setFragment == null) {
                    setFragment = new setFragment_8();
                    fragmentTransaction.add(R.id.main_content, setFragment);
                }
                fragmentTransaction.show(setFragment);
                break;
            case 9://
                if (Area1Fragment == null) {
                    Area1Fragment = new Area1Fragment_9();
                    fragmentTransaction.add(R.id.main_content, Area1Fragment);
                }
                fragmentTransaction.show(Area1Fragment);
                break;
            case 10://
                if (Area2Fragment == null) {
                    Area2Fragment = new Area2Fragment_10();
                    fragmentTransaction.add(R.id.main_content, Area2Fragment);
                }
                fragmentTransaction.show(Area2Fragment);
                break;
            case 11://测温
                if (temFragment == null) {
                    temFragment = new temFragment_11();
                    fragmentTransaction.add(R.id.main_content, temFragment);
                }
                fragmentTransaction.show(temFragment);
                break;
            case 12://定时任务
                if (timer1Fragment == null) {
                    timer1Fragment = new timer1Fragment_12();
                    fragmentTransaction.add(R.id.main_content, timer1Fragment);
                }
                fragmentTransaction.show(timer1Fragment);
                break;
            case 13://新建定时任务
                if (timer2Fragment == null) {
                    timer2Fragment = new timer2Fragment_13();
                    fragmentTransaction.add(R.id.main_content, timer2Fragment);
                }
                fragmentTransaction.show(timer2Fragment);
                break;
            case 14://编辑定时任务
                if (timer3Fragment == null) {
                    timer3Fragment = new timer3Fragment_14();
                    fragmentTransaction.add(R.id.main_content, timer3Fragment);
                }
                fragmentTransaction.show(timer3Fragment);
                break;
            case 15:
                if (MapSetFragment == null) {
                    MapSetFragment = new MapSetFragment_15();
                    fragmentTransaction.add(R.id.main_content, MapSetFragment);
                }
                fragmentTransaction.show(MapSetFragment);
                break;
            case 16:
                if (daolanFragment == null) {
                    daolanFragment = new daolanFragment_16();
                    fragmentTransaction.add(R.id.main_content, daolanFragment);
                }
                fragmentTransaction.show(daolanFragment);
                break;
            case 17:
                if (reportFragment == null) {
                    reportFragment = new reportFragment_17();
                    fragmentTransaction.add(R.id.main_content, reportFragment);
                }
                fragmentTransaction.show(reportFragment);
                break;
            case 18:
                if (ambientFragment == null) {
                    ambientFragment = new ambientFragment_18();
                    fragmentTransaction.add(R.id.main_content, ambientFragment);
                }
                fragmentTransaction.show(ambientFragment);
                break;
            case 19:
                if (cmdFragment == null) {
                    cmdFragment = new cmdFragment_19();
                    fragmentTransaction.add(R.id.main_content, cmdFragment);
                }
                fragmentTransaction.show(cmdFragment);
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    final static int COUNTS = 8;// 点击次数
    final static long DURATION = 2000;// 规定有效时间
    long[] mHits = new long[COUNTS];

    private void continuousClick() {
        //每次点击时，数组向前移动一位
        System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
        //为数组最后一位赋值
        mHits[mHits.length - 1] = SystemClock.uptimeMillis();
        if (mHits[0] >= (SystemClock.uptimeMillis() - DURATION)) {
            mHits = new long[COUNTS];//重新初始化数组
            startActivity(new Intent(NewMain.this, SetActivity.class));
        }
    }

    @OnClick(R.id.BatteryState)
    public void onClick() {
        // continuousClick();
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

    public void goXiaodu() {
        pass.setVisibility(View.VISIBLE);
        pass.setFocusable(true);
        pass.setFocusableInTouchMode(true);
        pass.requestFocus();
        showKeyboard();
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, 1000 * 10);
    }

    private Runnable popupRuanable = new Runnable() {
        @Override
        public void run() {
            if (popupWindow != null && popupWindow.isShowing()) {
                popupWindow.dismiss();
            }
        }
    };

    @OnClick({R.id.img_net, R.id.img_water, R.id.img_gps, R.id.img_cewen, R.id.img_xiaodu})
    public void onClick1(View view) {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }

        switch (view.getId()) {
            case R.id.img_net:
                textView.setText("网络状态");
                popupWindow = new PopupWindow(textView, 80, 30);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.aiview_shape_left));
                popupWindow.showAsDropDown(imgNet);
                break;
            case R.id.img_water:
                textView.setText("水位数据");
                popupWindow = new PopupWindow(textView, 80, 30);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.aiview_shape_left));
                popupWindow.showAsDropDown(imgWater);
                break;
            case R.id.img_gps:
                textView.setText("底盘信号");
                popupWindow = new PopupWindow(textView, 80, 30);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.aiview_shape_left));
                popupWindow.showAsDropDown(imgGps);
                break;
            case R.id.img_cewen:
                textView.setText("测温通讯");
                popupWindow = new PopupWindow(textView, 80, 30);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.aiview_shape_left));
                popupWindow.showAsDropDown(imgCewen);
                break;
            case R.id.img_xiaodu:
                textView.setText("消毒通讯");
                popupWindow = new PopupWindow(textView, 80, 30);
                popupWindow.setOutsideTouchable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.aiview_shape_left));
                popupWindow.showAsDropDown(imgXiaodu);
                break;
        }
        mHandler.removeCallbacks(popupRuanable);
        mHandler.postDelayed(popupRuanable, 2000);
    }
}