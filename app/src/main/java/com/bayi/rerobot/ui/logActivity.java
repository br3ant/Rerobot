package com.bayi.rerobot.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.MyAdapter;
import com.bayi.rerobot.adapter.logFragmentAdapter;
import com.bayi.rerobot.gen.LogSDao;
import com.bayi.rerobot.greendao.LogS;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bayi.rerobot.util.LogUtil.saveLog;
import static com.bayi.rerobot.util.LogUtil.ymdFormat;

public class logActivity extends BaseActivity  {
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.sr_layout)
    SwipeRefreshLayout sr_layout;
//    private MainActivity mainActivity;
    private MyAdapter adapter;
    private List<LogS> logs;
    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 20;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    @Override
    public void initView() {
        setContentView(R.layout.fragment_log);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        logs = App.getDaoSession().getLogSDao().queryBuilder().where(LogSDao.Properties.Id.notEq(-1)).orderDesc(LogSDao.Properties.Id).list();
//        long nowymd = new Date(System.currentTimeMillis()).getTime();
//        for (int i = 0; i < logs.size(); i++) {
//            try {
//                long ymd = ymdFormat.parse(logs.get(i).getYmd()).getTime();
//                if (nowymd - ymd > 7 * 24 * 60 * 60) {
//                    App.getDaoSession().getLogSDao().delete(logs.get(i));
//                    logs.remove(i);
//
//                }
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }

        initRefreshLayout();
        initRecyclerView();

    }

    private void initRefreshLayout() {

        sr_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e("ddddd","1234");
                sr_layout.setRefreshing(false);
            }
        });

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        this.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<LogS> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            adapter.updateList(newDatas, true);
        } else {
            adapter.updateList(null, false);
        }
    }
    private void initRecyclerView() {

        adapter = new MyAdapter(getDatas(0, PAGE_COUNT), this, getDatas(0, PAGE_COUNT).size() > 0 ? true : false);
        LinearLayoutManager  mLayoutManager = new LinearLayoutManager(this);
        recycle.setLayoutManager(mLayoutManager);
        recycle.setAdapter(adapter);
        recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (adapter.isFadeTips() == false && lastVisibleItem + 1 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }

                    if (adapter.isFadeTips() == true && lastVisibleItem + 2 == adapter.getItemCount()) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                updateRecyclerView(adapter.getRealLastPosition(), adapter.getRealLastPosition() + PAGE_COUNT);
                            }
                        }, 500);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });
    }
    private List<LogS> getDatas(final int firstIndex, final int lastIndex) {
        logs = App.getDaoSession().getLogSDao().queryBuilder().where(LogSDao.Properties.Id.notEq(-1)).orderDesc(LogSDao.Properties.Id).offset(lastVisibleItem * 20).limit(20).list();
        lastVisibleItem++;

        return logs;
    }

}
