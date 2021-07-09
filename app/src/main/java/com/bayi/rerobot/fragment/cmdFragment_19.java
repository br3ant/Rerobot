package com.bayi.rerobot.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.OnRecyclerViewItemClickListener;
import com.bayi.rerobot.adapter.cmdAdapter;
import com.bayi.rerobot.communication.Order;
import com.bayi.rerobot.communication.OrderManage;
import com.bayi.rerobot.communication.ProductCallback;
import com.bayi.rerobot.ui.BaseFragment;
import com.bayi.rerobot.ui.NewMain;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.ItemOffsetDecoration;
import com.bayi.rerobot.util.LogUtil;
import com.bayi.rerobot.util.SocketType;
import com.bayi.rerobot.util.SpHelperUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by tongzn
 * on 2020/5/4
 */
public class cmdFragment_19 extends BaseFragment {


    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.recycle)
    RecyclerView recyclerView;

    private NewMain mainActivity;

    private List<String> datas = Arrays.asList("开启喷雾", "停止喷雾", "开启绿灯", "开启蓝灯",
            "开启红灯", "风速小", "风速中", "风速大", "雾量小", "雾量中", "雾量大");
   private cmdAdapter adapter;
   private SpHelperUtil spHelperUtil;
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cmd;
    }

    @Override
    protected void initView() {
        mainActivity = (NewMain) getActivity();
        titleText.setText("单发命令");

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        spHelperUtil=new SpHelperUtil(getContext());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 4);
        recyclerView.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        recyclerView.addItemDecoration(itemDecoration);
        adapter=new cmdAdapter(datas,getContext());

        adapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int id) {
                toast(datas.get(id));
                switch (id){
                    case 0:
                        sendsocket(SocketType.PENWU);
                        break;
                    case 1:
                        sendsocket(SocketType.StopPenwu);
                        break;
                    case 2:
                        sendsocket(SocketType.green);
                        break;
                    case 3:
                        sendsocket(SocketType.blue);
                        break;
                    case 4:
                        sendsocket(SocketType.red);
                        break;
                    case 5:
                        spHelperUtil.put("wind", 100);
                        sendsocket(SocketType.WIND);
                        break;
                    case 6:
                        spHelperUtil.put("wind", 150);
                        sendsocket(SocketType.WIND);
                        break;
                    case 7:
                        spHelperUtil.put("wind", 200);
                        sendsocket(SocketType.WIND);
                        break;
                    case 8:
                        spHelperUtil.put("wuliang", 100);
                        sendsocket(SocketType.WULIANG);
                        break;
                    case 9:
                        spHelperUtil.put("wuliang", 150);
                        sendsocket(SocketType.WULIANG);
                        break;
                    case 10:
                        spHelperUtil.put("wuliang", 200);
                        sendsocket(SocketType.WULIANG);
                        break;

                 }
            }
        });
        recyclerView.setAdapter(adapter);
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

    }

    @OnClick({R.id.title_back, R.id.title_main})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                mainActivity.setFragmentView(7);
                break;
            case R.id.title_main:
                mainActivity.setFragmentView(0);
                break;

        }
    }

    private void sendsocket(int type) {
        switch (type) {
            case SocketType.PENWU:
                LogUtil.saveLog("service喷雾了");
                try {
                    OrderManage.getInstance().startPenWu(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.PENWU);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.StopPenwu:
                try {
                    OrderManage.getInstance().stopPenWu(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.StopPenwu);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.WIND:
                try {
                    OrderManage.getInstance().setWIND(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.WIND);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.WULIANG:
                try {
                    OrderManage.getInstance().setWULIANG(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {
                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.WULIANG);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;

            case SocketType.red:
                try {
                    OrderManage.getInstance().red_open(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {

                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.red);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.blue:
                try {
                    OrderManage.getInstance().blue_open(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {

                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.blue);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case SocketType.green:
                try {
                    OrderManage.getInstance().green_open(Contants.TYPE_REBORT, new ProductCallback() {
                        @Override
                        public void onDataReceived(int state, String age) {
                            if (state == ProductCallback.Sur) {

                            } else if (state == ProductCallback.TIMEOUT) {
                                sendsocket(SocketType.green);
                            }
                        }
                    }, Order.ExecutiveLevel.HIGH);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;


        }

    }
}
