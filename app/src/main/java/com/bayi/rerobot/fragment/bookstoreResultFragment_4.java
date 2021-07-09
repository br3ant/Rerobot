package com.bayi.rerobot.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.bookAdapter;
import com.bayi.rerobot.bean.Speaktxt;
import com.bayi.rerobot.bean.book;
import com.bayi.rerobot.bean.bookSearch;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.ItemOffsetDecoration;
import com.bayi.rerobot.util.WebServiceUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class bookstoreResultFragment_4 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    private NewMain mainActivity;
    private int type;
    private List<book>books;
    private bookAdapter adapter;
    private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static String macno="";

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookstoreresult;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mainActivity = (NewMain) getActivity();
    }

    @Override
    protected void initData() {
        init(bookstoreSearchFragment_3.NeedSearch);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new bookAdapter(books, getActivity());
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recycle.addItemDecoration(itemDecoration);
        recycle.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                macno=books.get(id).getMarcno();
                mainActivity.setFragmentView(6);
            }
        });
    }
    private boolean isOk=false;
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if(!isOk){
            init(bookstoreSearchFragment_3.NeedSearch);
            }else {
                isOk=false;
            }

        }
        isOk=false;
    }

    private void init(String book) {
      //  String time = mFormatter.format(System.currentTimeMillis());
        HashMap<String, String> properties = new HashMap<String, String>();
        properties.put("libraryId", "ZSTU");
        properties.put("keyword", book);
        properties.put("searchType", "");
        properties.put("page", "1");
        properties.put("pageSize", "10");
        properties.put("postData", "");
        WebServiceUtils.callWebService(WebServiceUtils.WEB_SERVER_URL, "Search", properties, new WebServiceUtils.WebServiceCallBack() {
            @Override
            public void callBack(SoapObject result) {
                if (result != null) {
                    try {
                        recycle.setVisibility(View.VISIBLE);
                        String s = result.getProperty(0).toString();
                        books = readXML(new ByteArrayInputStream(s.getBytes()));
                        adapter.setNewData(books);
                        Log.e("WebService", books.get(0).getMarcno());
                    } catch (Exception e) {
                        Log.e("WebService1", e.toString());     recycle.setVisibility(View.GONE);
                        EventBus.getDefault().post(new Speaktxt().setTxt("没有找到此书"));
                    }

                } else {
                    recycle.setVisibility(View.GONE);
                    Toast.makeText(getActivity(), "没找到数据", Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new Speaktxt().setTxt("没有找到此书"));
                }
            }
        });
    }

    public static String md5(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @OnClick({R.id.title_back, R.id.title_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(3);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
        }
    }

    public static List<book> readXML(InputStream inStream) {

        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setInput(inStream, "UTF-8");
            int eventType = parser.getEventType();

            book currentPerson = null;
            List<book> books = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT://文档开始事件,可以进行数据初始化处理
                        books = new ArrayList<book>();
                        break;

                    case XmlPullParser.START_TAG://开始元素事件
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("book")) {
                            currentPerson = new book();
                            //currentPerson.setBook_name(parser.nextText());
                        } else if (currentPerson != null) {
                            if (name.equalsIgnoreCase("book_publisher")) {
                                currentPerson.setBook_publisher(parser.nextText());// 如果后面是Text元素,即返回它的值
                            } else if (name.equalsIgnoreCase("book_author")) {
                                currentPerson.setBook_author(parser.nextText());
                            } else if (name.equalsIgnoreCase("book_type")) {
                                currentPerson.setBook_type(parser.nextText());
                            } else if (name.equalsIgnoreCase("book_name")) {
                                currentPerson.setMarcno(parser.getAttributeValue(0).split("=")[1]);
                                currentPerson.setBook_name(parser.nextText());
                            } else if (name.equalsIgnoreCase("book_publish_date")) {
                                  currentPerson.setBook_publish_date(parser.nextText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG://结束元素事件
                        if (parser.getName().equalsIgnoreCase("book") && currentPerson != null) {
                            books.add(currentPerson);
                            currentPerson = null;
                        }

                        break;
                }

                eventType = parser.next();
            }

            inStream.close();
            return books;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(bookSearch msg) {
        JSONArray slots= null;
        try {
            slots = msg.getSearchTxt();

            for (int i = 0; i < slots.length(); i++) {
                JSONObject jo = new JSONObject(slots.get(i).toString());
                if (jo.getString("name").equals("key")) {
                    bookstoreSearchFragment_3.NeedSearch=jo.getString("normValue");
                   init(jo.getString("normValue"));
                    isOk=true;
                }else if (jo.getString("name").equals("itemRank")) {

                    int p=Integer.valueOf(jo.getString("normValue"));
                    try {
                        macno=books.get(p).getMarcno();
                        mainActivity.setFragmentView(6);

                    }catch (Exception e){

                    }

                }

            }

        } catch (JSONException e) {
            Log.e("JSONException",e.toString());
            e.printStackTrace();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
