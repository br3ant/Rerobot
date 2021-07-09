package com.bayi.rerobot.fragment;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.DanganDetailAdapter;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.danganAdapter;
import com.bayi.rerobot.bean.ChatMsg;
import com.bayi.rerobot.bean.DanganSearch;
import com.bayi.rerobot.bean.MainMsg;
import com.bayi.rerobot.bean.Result;
import com.bayi.rerobot.bean.danganEventbus;
import com.bayi.rerobot.data.response.ApiRepossitory;
import com.bayi.rerobot.dialog.danganDialog;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.util.HttpUtil;
import com.bayi.rerobot.util.ItemOffsetDecoration;
import com.bayi.rerobot.util.TaskType;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class filesFragment extends BaseFragment {

    @BindView(R.id.chat_view)
    View chatView;
    @BindView(R.id.aiView)
    RecyclerView aiView;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.cl1)
    ConstraintLayout cl1;
    @BindView(R.id.cl2)
    ConstraintLayout cl2;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.noData)
    TextView noData;
    @BindView(R.id.title)
    TextView title;


    private float pressY;

    int Top;
    private danganAdapter adapter;
    private DanganDetailAdapter danganDetailAdapter;
    private List<ChatMsg> chatMsg = new ArrayList<>();
    private ApiRepossitory repossitory = new ApiRepossitory();
    private List<Result> results;

    private danganDialog.Builder builder;
    private HttpUtil httpUtil=new HttpUtil();
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_files;
    }

    @Override
    protected void initView() {
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        builder = new danganDialog.Builder(getActivity());
        builder.create();
        builder.getdismissListener(new danganDialog.dismissListener() {
            @Override
            public void dismiss() {
                results.clear();
                cl2.setVisibility(View.GONE);
                cl1.setVisibility(View.VISIBLE);
                chatView.offsetTopAndBottom((int) (630 - chatView.getTop()));
                title.setText("档案牵引");
                ConstraintLayout.LayoutParams parms=(ConstraintLayout.LayoutParams) chatView.getLayoutParams();
                parms.bottomMargin=-0;
                chatView.setLayoutParams(parms);
            }
        });


        cl1.setVisibility(View.VISIBLE);
        cl2.setVisibility(View.GONE);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        aiView.setLayoutManager(layoutManager);
        adapter = new danganAdapter(chatMsg);
        aiView.setAdapter(adapter);

        danganDetailAdapter = new DanganDetailAdapter(results, getActivity());
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getActivity(), R.dimen.item_offset);
        recycle.addItemDecoration(itemDecoration);
        recycle.setAdapter(danganDetailAdapter);
        danganDetailAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                for (int i = 0; i < results.size(); i++) {
                    if (results.get(i).isShow()) {
                        results.get(i).setShow(false);
                        break;
                    }
                }
                results.get(id).setShow(true);
                danganDetailAdapter.setNewData(results);
            }
        });
        danganDetailAdapter.setOnItemClickListener1(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                String area = results.get(id).getRemark();
                if (!area.equals("") && area != null) {
                      builder.show();
                    App.targetName=area;
                    App.taskType= TaskType.Other;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            httpUtil.get(App.targetName);
                        }
                    }).start();
                } else {
                    toast("此档案没有位置信息...");
                }
            }
        });
        chatView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = (int) event.getX();
                float y = (int) event.getY();
                Log.e("sssss", y + "-----");
                Top = chatView.getTop();
                switch (event.getAction()) {
                    //按下
                    case MotionEvent.ACTION_DOWN:

                        pressY = event.getY();
                        break;
                    //移动
                    case MotionEvent.ACTION_MOVE:

                        float offsetY = y - pressY;
                        if (Math.abs(offsetY) > 80) break;
                        if (Top >= 630 && Top <= 1170) {
                            // chatView.layout(0,Top,0,0);
                            chatView.offsetTopAndBottom((int) (offsetY));
                        } else if (Top < 630 && offsetY > 0) {
                            chatView.offsetTopAndBottom((int) (offsetY));
                        } else if (Top > 1170 && offsetY < 0) {

                            chatView.offsetTopAndBottom((int) (offsetY));
                        }
                        break;
                    //松开
                    case MotionEvent.ACTION_UP:

                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void initData() {

    }

    private void Search() {
        String s = editSearch.getText().toString().trim();
        if (s.length() <= 0) {
            toast("不能为空");
            return;
        }
        repossitory.search(s).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().toString());
                    String result = jsonObject.getString("result");
                    Gson gson = new Gson();
                    Type userListType = new TypeToken<ArrayList<Result>>() {
                    }.getType();
                    results = gson.fromJson(result, userListType);
                    cl1.setVisibility(View.GONE);
                    cl2.setVisibility(View.VISIBLE);
                    ConstraintLayout.LayoutParams parms=(ConstraintLayout.LayoutParams) chatView.getLayoutParams();
                    parms.bottomMargin=-600;
                    chatView.setLayoutParams(parms);
                    if (results.size() > 0) {
                        noData.setVisibility(View.GONE);
                        recycle.setVisibility(View.VISIBLE);
                        danganDetailAdapter.setNewData(results);
                    } else {
                        recycle.setVisibility(View.GONE);
                        noData.setVisibility(View.VISIBLE);
                    }
                    title.setText("搜索结果");
                    Log.e("vvvvv", results.size() + "");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("vvvvv", e.toString());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ssssst", t.toString());
                toast("数据错误!");
            }
        });
    }

    @OnClick(R.id.back)
    public void onViewClicked() {
        //mainActivity.setFragmentView(0);
    }

    @OnClick(R.id.btn_search)
    public void onViewClicked1() {
        Search();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
//            logs= App.getDaoSession().getLogSDao().queryBuilder().where(LogSDao.Properties.Id.notEq(-1)).orderDesc(LogSDao.Properties.Id).list();
//            adapter.setNewData(logs);
            cl2.setVisibility(View.GONE);
            cl1.setVisibility(View.VISIBLE);
            chatView.offsetTopAndBottom((int) (630 - chatView.getTop()));
            title.setText("档案牵引");
            ConstraintLayout.LayoutParams parms=(ConstraintLayout.LayoutParams) chatView.getLayoutParams();
            parms.bottomMargin=-0;
            chatView.setLayoutParams(parms);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(MainMsg msg) {

        if (msg.getMsg().equals("clear")) {
            chatMsg.clear();
            adapter.notifyDataSetChanged();
            return;
        } else {
            ChatMsg c = new ChatMsg();
            c.setLeft(msg.isAsk());
            c.setMsg(msg.getMsg());
            chatMsg.add(c);
        }
        adapter.notifyItemInserted(chatMsg.size() - 1);
        aiView.scrollToPosition(chatMsg.size() - 1);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(danganEventbus msg) {
          builder.dismiss();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void XXX(DanganSearch msg) {
        editSearch.setText(msg.getSearchTxt());
        Search();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @OnClick({R.id.back, R.id.btn_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back:
                break;
            case R.id.btn_search:
                break;
        }
    }

    @OnClick({R.id.t1, R.id.t2, R.id.t3, R.id.t4, R.id.t5, R.id.t6})
    public void onViewClicked2(View view) {
        switch (view.getId()) {
            case R.id.t1:
                editSearch.setText("毛泽东思想");
                break;
            case R.id.t2:
                editSearch.setText("邓小平理论");
                break;
            case R.id.t3:
                editSearch.setText("人事档案");
                break;
            case R.id.t4:
                editSearch.setText("市场营销档案");
                break;
            case R.id.t5:
                editSearch.setText("文书档案");
                break;
            case R.id.t6:
                editSearch.setText("科技档案");
                break;
        }
    }


}
