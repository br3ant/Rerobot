package com.bayi.rerobot.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.logFragmentAdapter;
import com.bayi.rerobot.gen.LogSDao;
import com.bayi.rerobot.greendao.LogS;
import com.bayi.rerobot.ui.BaseFragment;

import java.text.ParseException;
import java.util.Date;
import java.util.List;


import butterknife.BindView;
import butterknife.OnClick;

import static com.bayi.rerobot.util.LogUtil.ymdFormat;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class logFragment extends BaseFragment {

    @BindView(R.id.recycle)
    RecyclerView recycle;
   // private MainActivity mainActivity;
    private logFragmentAdapter adapter;
    private List<LogS>logs;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_log;
    }

    @Override
    protected void initView() {
   //  mainActivity=(MainActivity)getActivity();
        logs= App.getDaoSession().getLogSDao().queryBuilder().where(LogSDao.Properties.Id.notEq(-1)).orderDesc(LogSDao.Properties.Id).list();
        long nowymd=new Date(System.currentTimeMillis()).getTime();
        for(int i=0;i<logs.size();i++){
            try {
             long ymd=  ymdFormat.parse(logs.get(i).getYmd()).getTime();
              if(nowymd-ymd>7*24*60*60){
                  App.getDaoSession().getLogSDao().delete(logs.get(i));
                  logs.remove(i);

              }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        adapter = new logFragmentAdapter(logs,getActivity());
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    @OnClick(R.id.back)
    public void onViewClicked() {
       //mainActivity.setFragmentView(0);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            logs= App.getDaoSession().getLogSDao().queryBuilder().where(LogSDao.Properties.Id.notEq(-1)).orderDesc(LogSDao.Properties.Id).list();
            adapter.setNewData(logs);
        }
    }
}
