package com.bayi.rerobot.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.newAreaTaskAdapter;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class Area1Fragment_9 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.area_recycle)
    RecyclerView recycle;
    @BindView(R.id.txt)
    TextView txt;
    @BindView(R.id.add)
    Button add;
    private NewMain mainActivity;
    private newAreaTaskAdapter adapter;
    private List<SetAreaBean> setAreaBeans;
    private List<SetAreaBean> datas = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_area1;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        setAreaBeans = App.getDaoSession().getSetAreaBeanDao().loadAll();
        if (setFragment_8.flag == 0) {
            titleText.setText("导览站点设置");
            txt.setText("已建导览(长按删除)");
            add.setText("添加导览");
            for (int i = 0; i < setAreaBeans.size(); i++) {
                if (setAreaBeans.get(i).getIsDaolan()) {
                    datas.add(setAreaBeans.get(i));
                }
            }
        } else {
            titleText.setText("区域站点设置");
            txt.setText("已建区域(长按删除)");
            add.setText("添加区域");
            for (int i = 0; i < setAreaBeans.size(); i++) {
                if (!setAreaBeans.get(i).getIsDaolan()) {
                    datas.add(setAreaBeans.get(i));
                }
            }
        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        recycle.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recycle.addItemDecoration(itemDecoration);
        adapter = new newAreaTaskAdapter(datas, getContext());
        recycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                App.getDaoSession().getSetAreaBeanDao().delete(datas.get(id));
                datas.remove(id);
                adapter.setNewData(datas);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            datas.clear();
            setAreaBeans = App.getDaoSession().getSetAreaBeanDao().loadAll();
            if (setFragment_8.flag == 0) {
                titleText.setText("导览站点设置");
                txt.setText("已建导览(长按删除)");
                add.setText("添加导览");
                for (int i = 0; i < setAreaBeans.size(); i++) {
                    if (setAreaBeans.get(i).getIsDaolan()) {
                        datas.add(setAreaBeans.get(i));

                    }
                }
            } else {
                titleText.setText("区域站点设置");
                txt.setText("已建区域(长按删除)");
                add.setText("添加区域");
                for (int i = 0; i < setAreaBeans.size(); i++) {
                    if (!setAreaBeans.get(i).getIsDaolan()) {
                        datas.add(setAreaBeans.get(i));

                    }
                }
            }
            adapter.setNewData(datas);
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
                mainActivity.setFragmentView(8);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
            case R.id.add:
                mainActivity.setFragmentView(10);
                break;
        }
    }


}
