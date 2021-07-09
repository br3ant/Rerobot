package com.bayi.rerobot.fragment;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class bookstoreSearchFragment_3 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.t1)
    TextView t1;
    @BindView(R.id.t2)
    TextView t2;
    @BindView(R.id.t3)
    TextView t3;
    @BindView(R.id.t4)
    TextView t4;
    @BindView(R.id.t5)
    TextView t5;
    @BindView(R.id.t6)
    TextView t6;
    private NewMain mainActivity;
    private int type;
    public static String NeedSearch;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_bookstoresearch;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        setData();

    }

    @Override
    protected void initData() {

    }
    private void setData(){
        editSearch.setText("");
        type = bookstoreFragment_2.BookType;
        if (type == 0) {
            titleText.setText("书名查询");
            t1.setText("时间简史");
            t2.setText("邓小平理论");
            t3.setText("安徒生童话");
            t4.setText("市场营销");
            t5.setText("高等数学");
            t6.setText("历史文书");
        } else if (type == 1) {
            titleText.setText("作者名查询");
            t1.setText("老舍");
            t2.setText("周树人");
            t3.setText("莫言");
            t4.setText("茅盾");
            t5.setText("谢婉莹");
            t6.setText("巴金");
        } else {
            titleText.setText("关键字查询");
            t1.setText("战争与和平");
            t2.setText("秘闻");
            t3.setText("科幻小说");
            t4.setText("成语大全");
            t5.setText("儿童读物");
            t6.setText("散文随笔");
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
         setData();
        }
    }


    @OnClick({R.id.title_back, R.id.title_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(2);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;
        }
    }

    @OnClick(R.id.btn_search)
    public void onClick() {
        NeedSearch = editSearch.getText().toString().trim();
        mainActivity.setFragmentView(4);
    }

    @OnClick({R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6})
    public void onClick1(View view) {
        switch (view.getId()) {
            case R.id.t1:
                editSearch.setText(t1.getText().toString());
                break;
            case R.id.t2:
                editSearch.setText(t2.getText().toString());
                break;
            case R.id.t3:
                editSearch.setText(t3.getText().toString());
                break;
            case R.id.t4:
                editSearch.setText(t4.getText().toString());
                break;
            case R.id.t5:
                editSearch.setText(t5.getText().toString());
                break;
            case R.id.t6:
                editSearch.setText(t6.getText().toString());
                break;
        }
    }
}
