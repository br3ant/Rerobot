package com.bayi.rerobot.fragment;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.bean.Speaktxt;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class introduceFragment_5 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.speakTxt)
    TextView speakTxt;
    private NewMain mainActivity;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_introduce;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("本馆介绍");
        speakTxt.setText("    浙江理工大学图书馆是一个以纺织、服装专业馆藏为特色，纸质资源与电子资源均衡发展的综合性文献信息资源中心，图书馆坚持服务立馆、特色兴馆、科技强馆的发展战略，以加强文献资源建设、深化读者服务、提升信息技术为重点，积极推进图书馆数字化、信息化，主动服务教学科研与人才培养。我馆被浙江省教育厅确定为浙江高校数字图书馆五个分中心之一，2010年以来连续三次获得浙江省高校图书馆先进集体。\n" +
                "图书馆馆舍面积3.6万平方米，共设有12个阅览室和1个特藏阅览室，3000多个阅览座位。周开放时间达98小时，网络数据库24小时不间断服务。日平均接待读者5000人次，高峰时达10000人次/日。\n" +
                "    截至2020年底，图书馆拥有纸质藏书257万册，电子图书总量超过180万种，纸质报刊1036种，中文纸质期刊331种、外文纸质期刊170种、报纸77种、电子数据库45个，ScienceDirect、IEL、Ebsco 、Web of Science、Springer等主要数据库均已配备。其中，纺织、服装特色学科的收藏品种和数量在全国已有相当影响，理学、经济、管理、机械、电子、人文、建筑、生命科学等多学科的馆藏也已形成了一定的规模，为学校教学科研与人才培养提供了充分的文献资源保障。\n" +
                "    图书馆设有自助借还书机5台，3M门禁系统1套，建成了设备先进、省内一流的信息共享空间，开启了读者自助学习的新模式，硬件配置位居国内同行前列。除提供传统的文献借阅服务之外，还积极开展特色品牌活动，促进校园文化建设，每年举办“读书节”系列活动。此外图书馆还提供馆际互借、文献传递、科技查新、查收查引、读者培训等科技信息服务。\n" +
                "    图书馆加强人才队伍建设，努力提升服务保障能力。截止目前，图书馆在编人员51人。其中，硕士及以上学位22人，正高职称5人，副高职称12人。人员结构正趋于年轻化、专业化和知识化，为深层次的文献资源服务和知识服务打下了坚实的基础。\n" +
                "    作为21世纪的浙江理工大学人才培养和科学研究的核心要素，未来的浙江理工大学图书馆将继续立足于“以人为本，服务至上”的服务理念，支持教学和科研，推动知识创新、共享、保存及文化传承，努力建成国内先进、省内一流的大学图书馆");
        EventBus.getDefault().post(new Speaktxt().setTxt(speakTxt.getText().toString()));
    }

    @Override
    protected void initData() {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            EventBus.getDefault().post(new Speaktxt().setTxt(speakTxt.getText().toString()));
        }else {
            EventBus.getDefault().post(new Speaktxt());
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


}
