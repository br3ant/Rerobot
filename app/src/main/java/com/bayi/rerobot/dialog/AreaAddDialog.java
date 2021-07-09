package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;

import com.bayi.rerobot.adapter.SourceListAdapter;
import com.bayi.rerobot.bean.ListDataSave;
import com.bayi.rerobot.greendao.SetAreaBean;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.ItemOffsetDecoration;
import com.bayi.rerobot.util.KeyboardUtils;
import com.bayi.rerobot.util.MissionActionB;
import com.bayi.rerobot.utilView.tagView.TagContainerLayout;
import com.bayi.rerobot.utilView.tagView.TagView;
import com.tobot.slam.data.LocationBean;

import java.util.ArrayList;
import java.util.List;

import static com.bayi.rerobot.App.currentMap;
import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;


public class AreaAddDialog extends Dialog {



    public AreaAddDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder implements View.OnClickListener {
        private final Context mContext;
        private final AreaAddDialog mSpsDialog;
        private ListDataSave dataSave;
        public Builder(Context mContext) {
            this.mContext = mContext;
            mSpsDialog = new AreaAddDialog(mContext, R.style.style_dialog);
            dataSave=new ListDataSave(mContext,"myLocationBean");
        }

        public Builder Ondismiss( onDismiss dismiss){
            this.onDismiss=dismiss;
            return this;
        }
        private onDismiss onDismiss;

        private RecyclerView sourceList;
        private List<MissionActionB> actionsToSave = new ArrayList<>();
        private TagContainerLayout tagContainerLayout;
        private  SourceListAdapter sourceListAdapter;
        private List<LocationBean>targetNameList=new ArrayList<>();
        private EditText areaName;
        private View view;
        public AreaAddDialog create(){

             view = LayoutInflater.from(mContext).inflate(R.layout.area_add_dialog,null);

            mSpsDialog.setCanceledOnTouchOutside(false);

            Window window = mSpsDialog.getWindow();
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = mSpsDialog.getWindow().getAttributes();

            lp.width = (int)(getScreenWidth()*1);
            lp.height = (int)(getScreenHeight()*1);
            lp.gravity = Gravity.TOP;
            view.findViewById(R.id.back).setOnClickListener(this);
            view.findViewById(R.id.save).setOnClickListener(this);
             sourceList = view.findViewById(R.id.recycle);
             areaName=view.findViewById(R.id.AreaName);
            //sourceList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,5);
            sourceList.setLayoutManager(gridLayoutManager);
            ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mContext,R.dimen.item_offset);
            sourceList.addItemDecoration(itemDecoration);
            sourceListAdapter = new SourceListAdapter(mContext);
            tagContainerLayout = view.findViewById(R.id.tagContainerLayout);;
            sourceListAdapter.setOnSelectedListener(new SourceListAdapter.OnSelectedListener() {
                @Override
                public void onSelected(int position, LocationBean bean) {
                    actionsToSave.add(new MissionActionB(0, currentMap, bean.getLocationNumber(),bean));
                    tagContainerLayout.setEnableCross(false);
                    tagContainerLayout.setMissionActionList(actionsToSave);
                }
            });
            sourceList.setAdapter(sourceListAdapter);
            List<LocationBean>  targets = new ArrayList<>();
            try {
                List<LocationBean>beans= dataSave.getDataList(Contants.LISTBEAN,LocationBean.class);
                for(int i=0;i< beans.size();i++){
                    targets.add(beans.get(i));
                }
            }catch (Exception e){
                Log.e("sssss",e.toString());
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
                    ((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(new long[]{0L, 20}, -1);
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
            mSpsDialog.setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if(onDismiss!=null){
                        onDismiss.dismiss();
                    }
                }
            });
            mSpsDialog.getWindow().setAttributes(lp);
            return mSpsDialog;
        }
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    KeyboardUtils.hideKeyboard(view);
                    mSpsDialog.dismiss();
                    break;
                case R.id.save:
                    startMission();
                    break;

                default:
                    break;
            }
        }
//        private  Handler handler = new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                Bundle bundle;
//                int result;
//                switch (msg.what) {
//                    case HandlerCode.GET_TARGETS_B://获取站点  0初始点，1充电点，2导航点。
//                        bundle = msg.getData();
//                        result = bundle.getInt("result");
//                        if (result == 0) {
//                            List<String>  targets = new ArrayList<>();
//                            List<TargetB> targetList = bundle.getParcelableArrayList("targetList");
//                            if (targetList != null && targetList.size() > 0) {
//                                for (TargetB target : targetList) {
//                                    //0初始点，1充电点，2导航点
//                                    if (target.getType() == 2) {
//                                        targets.add(target.getName());
//                                    }
//                                }
//                                sourceListAdapter.setData(targets);
//                            } else {
//                                App.jniEAIBotUtil.getTargetsB(0, currentMap, handler, HandlerCode.GET_TARGETS_B);
//                            }
//                        } else {
//                            App.jniEAIBotUtil.getTargetsB(0, currentMap, handler, HandlerCode.GET_TARGETS_B);
//                        }
//                        break;
//                    default:
//                        break;
//                }
//                return false;
//            }
//        });
        private void startMission() {

            targetNameList.clear();
            for (MissionActionB missionAction : actionsToSave) {
                //missionData += missionAction.getStringToSave();
                //String str=missionAction.getStringToSave().replace("NavigationTask#","");
                //String result[] = str.substring(str.indexOf("#")+1, str.indexOf("],")).split(":");
                targetNameList.add(missionAction.getBean());
            }
                    String name = areaName.getText().toString().trim();
                    if (!"".equals(name)) {
                        SetAreaBean setAreaBean=new SetAreaBean();
                        setAreaBean.setAreaname(name);
                        setAreaBean.setData(targetNameList);
                        App.getDaoSession().getSetAreaBeanDao().insertOrReplace(setAreaBean);
                        App.toast("设置成功");
                        KeyboardUtils.hideKeyboard(view);
                       mSpsDialog.dismiss();
                    }else {
                        App.toast("区域名称不能为空");
                    }

        }
    }



}
