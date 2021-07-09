package com.bayi.rerobot.fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bayi.rerobot.R;
import com.bayi.rerobot.bean.book;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.WebServiceUtils;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class bookstoreDetailFragment_6 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.txt_call_no)
    TextView txtCallNo;
    @BindView(R.id.txt_qr_no)
    TextView txtQrNo;
    @BindView(R.id.txt_place)
    TextView txtPlace;
    @BindView(R.id.txt_land_borrow)
    TextView txtLandBorrow;

    private NewMain mainActivity;
   private  Map<String,String> map=new HashMap<>();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookstoredetail;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();

        titleText.setText("图书详情信息");
    }

    @Override
    protected void initData() {
        init();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            init();
        }
    }

    private void init() {
        //  String time = mFormatter.format(System.currentTimeMillis());
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("libraryId", "ZSTU");
        properties.put("marcNo", bookstoreResultFragment_4.macno);

        WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "Detail", properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (result != null) {
                    try {

                        String s = result.getProperty(0).toString();
                       readXML(new ByteArrayInputStream(s.getBytes()));
                        txtCallNo.setText(String.format("图书号:%s",map.get("call_no")));
                        txtQrNo.setText(String.format("图书条形码:%s",map.get("bar_code")));
                        txtPlace.setText(String.format("馆藏地:%s",map.get("place")));
                        txtLandBorrow.setText(String.format("借还时间:%s",map.get("status")));
                    } catch (Exception e) {
                        Log.e("WebService1", e.toString());
                    }

                } else {
                    // recycle.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "没找到数据", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @OnClick({R.id.title_back, R.id.title_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(4);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
        }
    }

    public void readXML(InputStream inStream) {

        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(inStream, "UTF-8");
            int eventType = parser.getEventType();

            book currentPerson = null;


            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT://文档开始事件,可以进行数据初始化处理
                        break;

                    case XmlPullParser.START_TAG://开始元素事件
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("detail")) {
                            currentPerson = new book();
                            //currentPerson.setBook_name(parser.nextText());
                        } else if (currentPerson != null) {
                            if (name.equalsIgnoreCase("detail_call_no")) {
                               map.put("call_no",parser.nextText());
                            } else if (name.equalsIgnoreCase("detail_bar_code")) {
                                map.put("bar_code",parser.nextText());
                            } else if (name.equalsIgnoreCase("detail_collection_place")) {
                                map.put("place",parser.nextText());
                            } else if (name.equalsIgnoreCase("detail_status")) {
                               map.put("status",parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG://结束元素事件
                        if (parser.getName().equalsIgnoreCase("detail") && currentPerson != null) {

                            currentPerson = null;
                        }

                        break;
                }

                eventType = parser.next();
            }

            inStream.close();

        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
