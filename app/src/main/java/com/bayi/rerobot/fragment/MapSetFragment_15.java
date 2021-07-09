package com.bayi.rerobot.fragment;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.mapsetAdapter;
import com.bayi.rerobot.bean.ListDataSave;
import com.bayi.rerobot.bean.MapName;
import com.bayi.rerobot.tobot.BaseConstant;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.ItemOffsetDecoration;
import com.bayi.rerobot.util.SpHelperUtil;
import com.tobot.slam.SlamManager;
import com.tobot.slam.agent.listener.OnFinishListener;
import com.tobot.slam.data.LocationBean;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.bayi.rerobot.tobot.BaseConstant.getMapDirectory;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class MapSetFragment_15 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.jiantou)
    ImageView jiantou;
    @BindView(R.id.re)
    RelativeLayout re;
    @BindView(R.id.txt)
    TextView txt;

    private NewMain mainActivity;
    private mapsetAdapter adapter;
    private ListDataSave dataSave;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mapset;
    }

    private List<String> datas = new ArrayList<>();
    private SpHelperUtil spHelperUtil;
    private boolean isopen = false;

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("地图加载");
    }

    private PopupWindow popupWindow;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spHelperUtil = new SpHelperUtil(getContext());
        dataSave = new ListDataSave(getContext(), "myLocationBean");
        datas = getFileName(getMapDirectory(getContext()), ".stcm");
        txt.setText("地图加载:".concat(NewMain.presentMap));
        adapter = new mapsetAdapter(datas, getContext());
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                toast("地图正在加载...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String data1 = datas.get(id).replace(".stcm", "");
                        SlamManager.getInstance().loadMapInThread(BaseConstant.getMapNumPath(getContext(), data1), new OnFinishListener<List<LocationBean>>() {
                            @Override
                            public void onFinish(List<LocationBean> data) {

                                // relocation();
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dataSave.setDataList(Contants.LISTBEAN, data);
                                        toast("加载地图成功");
                                        MapName name = new MapName();
                                        name.setMapName(data1);
                                        spHelperUtil.put("mapname", data1);
                                        EventBus.getDefault().post(name);
                                        txt.setText("地图加载:".concat(NewMain.presentMap));
                                    }
                                });


                            }

                            @Override
                            public void onError() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast("加载地图失败");
                                    }
                                });

                            }
                        });
                    }
                }).start();
            }
        });
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recycle.addItemDecoration(itemDecoration);
        LinearLayoutManager ms = new LinearLayoutManager(getContext());
        ms.setOrientation(LinearLayoutManager.VERTICAL);
        recycle.setLayoutManager(ms);
        recycle.setAdapter(adapter);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            datas = getFileName(getMapDirectory(getContext()), ".stcm");
            adapter.setNewData(datas);
            txt.setText("地图加载:".concat(NewMain.presentMap));
        } else {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @OnClick({R.id.title_back, R.id.title_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(8);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;


        }
    }

    public ArrayList<String> getFileName(String fileAbsolutePaht, String type) {
        ArrayList<String> result = new ArrayList<String>();
        File file = new File(fileAbsolutePaht);
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; ++i) {
            if (!files[i].isDirectory()) {
                String fileName = files[i].getName();
                if (fileName.trim().toLowerCase().endsWith(type)) {
                    result.add(fileName);
                }
            }
        }
        return result;
    }

    @OnClick(R.id.jiantou)
    public void onClick() {
        rotateAnim(isopen);
        if (isopen) {
            close();
        } else {
            open();
        }
        isopen = !isopen;
    }

    private void open() {

        ObjectAnimator animator = ObjectAnimator.ofFloat(re, "alpha", 0f, 1f);
        animator.setDuration(200);
        animator.setRepeatCount(0);
        animator.start();

    }

    private void close() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(re, "alpha", 1f, 0f);
        animator.setDuration(200);
        animator.setRepeatCount(0);
        animator.start();
    }

    public void rotateAnim(boolean isopen) {
        Animation anim = null;
        if (!isopen) {
            anim = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        } else {
            anim = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        }
        anim.setFillAfter(true); // 设置保持动画最后的状态
        anim.setDuration(200); // 设置动画时间
        anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        anim.setFillAfter(true);// 设置旋转后停止
        jiantou.startAnimation(anim);
    }
}
