package com.bayi.rerobot.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.cewenAdapter;
import com.bayi.rerobot.bean.CewenBean;
import com.bayi.rerobot.bean.Cwjson;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.bayi.rerobot.adapter.cewenAdapter.base64ToBitmap;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class temFragment_11 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.main_name)
    TextView mainName;
    @BindView(R.id.main_tem)
    TextView main_tem;
    @BindView(R.id.main_time)
    TextView main_time;
    @BindView(R.id.tid)
    TextView tid;
    @BindView(R.id.recycle1)
    RecyclerView recyclerView1;
    @BindView(R.id.recycle2)
    RecyclerView recyclerView2;
    private NewMain mainActivity;

    private cewenAdapter adapter1,adapter2;
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private DateFormat mFormatter1 = new SimpleDateFormat("HH:mm");
    private List<CewenBean> cw1=new ArrayList<>();
    private List<CewenBean>cw2=new ArrayList<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tem;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mainActivity = (NewMain) getActivity();
        titleText.setText("测温");
        LinearLayoutManager ms= new LinearLayoutManager(getContext());
        ms.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager ms1= new LinearLayoutManager(getContext());
        ms1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView1.setLayoutManager(ms);
        recyclerView2.setLayoutManager(ms1);
        adapter1=new cewenAdapter(cw1,getContext());
        adapter2=new cewenAdapter(cw2,getContext());
        recyclerView1.setAdapter(adapter1);
        recyclerView2.setAdapter(adapter2);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {

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
    public void cwjson(Cwjson cwjson) {
        try {

            JSONObject param = new JSONObject(cwjson.getJSON());
            String picture=param.getString("picture");
            String tem=param.getString("temp_red");
            tem=tem.replace("[","").replace("]","");
            profile_image.setImageBitmap(base64ToBitmap(picture));
            main_tem.setText(String.format("%s°C",tem));
            main_time.setText(mFormatter.format(new Date()));
            Log.e("cw1",cw1.size()+"");

            adapter1.setNewData(cw1);
            adapter2.setNewData(cw2);
            CewenBean cewenBean=new CewenBean();
            cewenBean.setLevel(0);
            cewenBean.setBase64(picture);
            cewenBean.setTem(tem);
            cewenBean.setTime(mFormatter1.format(new Date()));
            if(Float.parseFloat(tem)>37.2f){
                cewenBean.setLevel(1);
                cw2.add(0,cewenBean);
            }
            cw1.add(0,cewenBean);
            if(cw1.size()>=5){
                cw1.remove(cw1.size()-1);
            }
            if(cw2.size()>=5){
                cw2.remove(cw2.size()-1);
            }
        }catch (Exception e){

            try {
                JSONObject param = new JSONObject(cwjson.getJSON());
                tid.setText(param.getString("id"));
            }catch (Exception t){

            }

        }
    }

}
