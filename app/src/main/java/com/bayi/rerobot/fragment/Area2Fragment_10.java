package com.bayi.rerobot.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.SourceListAdapter;
import com.bayi.rerobot.bean.ListDataSave;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.ItemOffsetDecoration;
import com.bayi.rerobot.util.MissionActionB;
import com.bayi.rerobot.utilView.tagView.TagContainerLayout;
import com.bayi.rerobot.utilView.tagView.TagView;
import com.tobot.slam.data.LocationBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bayi.rerobot.App.currentMap;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class Area2Fragment_10 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.recycle)
    RecyclerView sourceList;
    @BindView(R.id.AreaName)
    EditText areaName;
    @BindView(R.id.tagContainerLayout)
    TagContainerLayout tagContainerLayout;
    @BindView(R.id.txt)
    TextView txt;
    private NewMain mainActivity;
    private ListDataSave dataSave;
    private List<MissionActionB> actionsToSave = new ArrayList<>();
    private SourceListAdapter sourceListAdapter;
    private List<LocationBean> targetNameList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_area2;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();

        if (setFragment_8.flag == 0) {
            titleText.setText("导览站点新建");
            txt.setText("导览名称");
        } else {
            titleText.setText("区域站点新建");
            txt.setText("区域名称名称");

        }

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataSave = new ListDataSave(getContext(), "myLocationBean");
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 5);
        sourceList.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        sourceList.addItemDecoration(itemDecoration);
        sourceListAdapter = new SourceListAdapter(getContext());
        sourceListAdapter.setOnSelectedListener(new SourceListAdapter.OnSelectedListener() {
            @Override
            public void onSelected(int position, LocationBean bean) {
                actionsToSave.add(new MissionActionB(0, currentMap, bean.getLocationNumber(), bean));
                tagContainerLayout.setEnableCross(false);
                tagContainerLayout.setMissionActionList(actionsToSave);
            }
        });
        sourceList.setAdapter(sourceListAdapter);
        List<LocationBean> targets = new ArrayList<>();
        try {
            List<LocationBean> beans = dataSave.getDataList(Contants.LISTBEAN, LocationBean.class);
            for (int i = 0; i < beans.size(); i++) {
                targets.add(beans.get(i));
            }
        } catch (Exception e) {
            Log.e("sssss", e.toString());
            App.toast("当前地图无点位");
        }
        sourceListAdapter.setData(targets);
        //App.jniEAIBotUtil.getTargetsB(0, currentMap, handler, HandlerCode.GET_TARGETS_B);
        tagContainerLayout.setLanguage(0);
        tagContainerLayout.setMissionActionList(actionsToSave);
        tagContainerLayout.setIsTagViewSelectable(true);

        tagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                tagContainerLayout.setEnableCross(false);
                tagContainerLayout.setMissionActionList(actionsToSave);
            }

            @Override
            public void onTagLongClick(int position, String text) {
                ((Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[]{0L, 20}, -1);
                tagContainerLayout.setEnableCross(true);
                tagContainerLayout.setMissionActionList(actionsToSave);
            }

            @Override
            public void onSelectedTagDrag(int position, String text) {

            }

            @Override
            public void onTagCrossClick(int position) {
                actionsToSave.remove(position);
                tagContainerLayout.setMissionActionList(actionsToSave);
            }

            @Override
            public void onTouchUp() {
                actionsToSave = tagContainerLayout.getMissionActionList();
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            actionsToSave.clear();
            tagContainerLayout.setMissionActionList(actionsToSave);
            areaName.setText("");
            if (setFragment_8.flag == 0) {
                titleText.setText("导览站点新建");
                txt.setText("导览名称");
            } else {
                titleText.setText("区域站点新建");
                txt.setText("区域名称");
            }
            dataSave = new ListDataSave(getContext(), "myLocationBean");
            List<LocationBean> targets = new ArrayList<>();
            try {
                List<LocationBean> beans = dataSave.getDataList(Contants.LISTBEAN, LocationBean.class);
                for (int i = 0; i < beans.size(); i++) {
                    targets.add(beans.get(i));
                }
            } catch (Exception e) {
                Log.e("sssss", e.toString());
                App.toast("当前地图无点位");
            }
            sourceListAdapter.setData(targets);
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.title_back, R.id.title_main, R.id.save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(9);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
            case R.id.save:
                startMission();
                break;
        }
    }

    private void startMission() {
        targetNameList.clear();
        for (MissionActionB missionAction : actionsToSave) {
            targetNameList.add(missionAction.getBean());
        }
        if (setFragment_8.flag == 0) {
            if (targetNameList.size() != 1) {
                App.toast("导航点只能设置一个位置");
                return;
            }

        } else {
            if (targetNameList.size() <= 1) {
                App.toast("区域需要设置多点,请补偿多点");
                return;
            }

        }
        String name = areaName.getText().toString().trim();
        if (!"".equals(name)) {
            SetAreaBean setAreaBean = new SetAreaBean();
            setAreaBean.setAreaname(name);
            if (setFragment_8.flag == 0) {
                setAreaBean.setIsDaolan(true);
            } else {
                setAreaBean.setIsDaolan(false);
            }
            setAreaBean.setData(targetNameList);
            App.getDaoSession().getSetAreaBeanDao().insertOrReplace(setAreaBean);
            App.toast("设置成功");
            mainActivity.setFragmentView(9);
        } else {
            App.toast("区域名称不能为空");
        }

    }

}
