package com.bayi.rerobot.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.daolanAdapter;
import com.bayi.rerobot.adapter.mapsetAdapter;
import com.bayi.rerobot.bean.Speaktxt;
import com.bayi.rerobot.bean.danganEventbus;
import com.bayi.rerobot.dialog.danganDialog;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.greendao.Task;
import com.bayi.rerobot.service.aiService;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.ItemOffsetDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class daolanFragment_16 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    private NewMain mainActivity;

    private daolanAdapter adapter;
    private List<SetAreaBean> pointdatas = new ArrayList<>();
    private danganDialog.Builder builder;
    private String pointName="";
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_daolan;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("位置导航");
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void initData() {

    }
    private List<Task> tasklist = new ArrayList<>();
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        List<SetAreaBean> setAreaBeans = App.getDaoSession().getSetAreaBeanDao().loadAll();
        for (int i = 0; i < setAreaBeans.size(); i++) {
            if (setAreaBeans.get(i).getIsDaolan()) {
                pointdatas.add(setAreaBeans.get(i));

            }
        }
        adapter = new daolanAdapter(pointdatas, getContext());
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                App.getDaoSession().getTaskDao().deleteAll();
                aiService.isPW=false;
                aiService.sleepTime=0;
                tasklist.clear();
                Task task = new Task();
                task.setTargetname(pointdatas.get(id).getData().get(0));

                pointName=pointdatas.get(id).getAreaname();
                tasklist.add(task);
                App.getDaoSession().getTaskDao().insertOrReplaceInTx(tasklist);
                aiService.index = 0;
                EventBus.getDefault().post("任务");
                toast("正在导航中...");
                builder.show();
            }
        });
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recycle.addItemDecoration(itemDecoration);
        LinearLayoutManager ms = new LinearLayoutManager(getContext());
        ms.setOrientation(LinearLayoutManager.VERTICAL);
        recycle.setLayoutManager(ms);
        recycle.setAdapter(adapter);

        builder = new danganDialog.Builder(getActivity());
        builder.create();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            pointdatas.clear();
            List<SetAreaBean> setAreaBeans = App.getDaoSession().getSetAreaBeanDao().loadAll();
            for (int i = 0; i < setAreaBeans.size(); i++) {
                if (setAreaBeans.get(i).getIsDaolan()) {
                    pointdatas.add(setAreaBeans.get(i));
                }
            }
            adapter.setNewData(pointdatas);
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick({R.id.title_back, R.id.title_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(0);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
        }
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(danganEventbus msg) {
        if(builder.isShow()){
        builder.dismiss();
        EventBus.getDefault().post(new Speaktxt().setTxt("已到达".concat(pointName).concat("位置")));
        }
    }
}
