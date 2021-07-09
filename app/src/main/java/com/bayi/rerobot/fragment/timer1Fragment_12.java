package com.bayi.rerobot.fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.TimerAdapter;
import com.bayi.rerobot.dialog.TimerEditDialog;
import com.bayi.rerobot.dialog.onDismiss;
import com.bayi.rerobot.greendao.AddTimer;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.RecycleViewDivider;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.clock.AlarmUtil.setALRM;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class timer1Fragment_12 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.area_recycle)
    RecyclerView recyclerView;
    private NewMain mainActivity;

    private TimerAdapter adapter;
    private List<AddTimer> addtimer;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timer1;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("定时任务");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        addtimer= App.getDaoSession().getAddTimerDao().loadAll();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecycleViewDivider(
                getContext(), LinearLayoutManager.VERTICAL, 1, Color.parseColor("#FFFFFF")));
        adapter=new TimerAdapter(addtimer, getContext(), new TimerAdapter.OnState() {
            @Override
            public void onState(int postion, boolean isChecked) {
                Log.e("week",isChecked+""+postion);
                addtimer.get(postion).setState(isChecked);
                App.getDaoSession().getAddTimerDao().updateInTx(addtimer);
                setALRM(addtimer,false);
            }
        });
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mainActivity.setFragmentView(14);
                    mainActivity.timer3Fragment.setData(addtimer.get(id));
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            addtimer= App.getDaoSession().getAddTimerDao().loadAll();
            adapter.setNewData(addtimer);
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.title_back, R.id.title_main, R.id.add})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(7);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
            case R.id.add:
                mainActivity.setFragmentView(13);
                break;
        }
    }


}
