package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.adapter.deviceAdapter;
import com.bayi.rerobot.bean.deviceBean;
import com.bayi.rerobot.communication.Order;
import com.bayi.rerobot.communication.OrderManage;
import com.bayi.rerobot.communication.ProductCallback;
import com.bayi.rerobot.update.UpdateManager;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.DisplayUtil;
import com.bayi.rerobot.util.SocketType;

import java.util.ArrayList;
import java.util.List;

import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;


public class DeviceSetDialog extends Dialog {

    public DeviceSetDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder implements View.OnClickListener {
        private final Context mContext;
        private final DeviceSetDialog mSpsDialog;
        private List<deviceBean> deviceBeans;
        private deviceAdapter adapter;
        private String text[] = {"喷雾电机", "环境传感器", "水位传感器"};
        private int pic[] = {R.drawable.icon_press, R.drawable.icon_sensor, R.drawable.icon_upstream};

        public Builder(Context mContext) {
            this.mContext = mContext;
            mSpsDialog = new DeviceSetDialog(mContext, R.style.style_dialog);
        }

        public Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                int state=(int)msg.obj;
                switch (msg.what) {
                    case 0:
                        deviceBeans.get(0).setState(state);
                        adapter.setPostion(2);
                        adapter.setNewData(deviceBeans);
                        sendsocket(SocketType.WATERDATA);
                        break;
                    case 1:
                        deviceBeans.get(1).setState(state);
                        adapter.setPostion(3);
                        adapter.setNewData(deviceBeans);
                        sendsocket(SocketType.stopwater);
                        break;
                    case 2:
                        adapter.setPostion(4);
                        deviceBeans.get(2).setState(state);
                        adapter.setNewData(deviceBeans);
                        App.toast("检测完毕");
                        break;

                }
            }
        };
        private RecyclerView recycle;
        private UpdateManager manager;
        public DeviceSetDialog create() {
            deviceBeans = new ArrayList<>();
            for (int i = 0; i < text.length; i++) {
                deviceBean db = new deviceBean();
                db.setText(text[i]);
                db.setPic(pic[i]);
                deviceBeans.add(db);
            }
            View view = LayoutInflater.from(mContext).inflate(R.layout.device_setting_dialog, null);

            mSpsDialog.setCanceledOnTouchOutside(false);
            manager=new UpdateManager(mContext);

            Window window = mSpsDialog.getWindow();
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = mSpsDialog.getWindow().getAttributes();

            lp.width = (int)(getScreenWidth()*1);
            lp.height = (int)(getScreenHeight()*1);
            lp.gravity = Gravity.TOP;
            recycle = view.findViewById(R.id.device_recycle);

            adapter = new deviceAdapter(deviceBeans, mContext);
            recycle.setLayoutManager(new LinearLayoutManager(mContext));
            recycle.setAdapter(adapter);
            view.findViewById(R.id.icon_close).setOnClickListener(this);
            view.findViewById(R.id.start_device).setOnClickListener(this);
            view.findViewById(R.id.view_it).setOnClickListener(this);
            view.findViewById(R.id.view_iot).setOnClickListener(this);
            view.findViewById(R.id.view_area).setOnClickListener(this);
            view.findViewById(R.id.checkupdate).setOnClickListener(this);
            TextView codename=view.findViewById(R.id.vercode_name);
            codename.setText("zjlg"+getVerName(mContext));
            mSpsDialog.getWindow().setAttributes(lp);

            return mSpsDialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.icon_close:
                    mSpsDialog.dismiss();
                    break;
                case R.id.start_device:
                    adapter.setPostion(1);
                    adapter.setNewData(deviceBeans);
                    sendsocket(SocketType.StopPenwu);
                    break;
                case R.id.view_it:
                    new ITsetDialog.Builder(mContext).create().show();
                    break;
                case R.id.view_iot:
                    new IOTsetDialog.Builder(mContext).create().show();
                    break;
                case R.id.view_area:
                    new AreaSetDialog.Builder(mContext).create().show();
                    break;
                case R.id.checkupdate:
                    new UpdateAsyncTask(mContext).execute();
                    break;
                default:
                    break;
            }
        }



        private void sendsocket(int type) {
            Message message=new Message();
            switch (type) {

                case SocketType.StopPenwu:
                    try {
                        OrderManage.getInstance().stopPenWu(Contants.TYPE_REBORT, new ProductCallback() {
                            @Override
                            public void onDataReceived(int state, String age) {
                                message.what=0;
                                if (state == ProductCallback.Sur) {
                                    message.obj=1;
                                    handler.sendMessageDelayed(message,2000);
                                } else if (state == ProductCallback.TIMEOUT) {
                                    sendsocket(SocketType.StopPenwu);
                                    message.obj=2;
                                }else {
                                    message.obj=2;
                                    handler.sendMessageDelayed(message,2000);
                                }

                            }
                        }, Order.ExecutiveLevel.HIGH);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                case SocketType.WATERDATA://水位数据
                    try {
                        OrderManage.getInstance().getWaterData(Contants.TYPE_REBORT, new ProductCallback() {
                            @Override
                            public void onDataReceived(int state, String age) {
                                message.what=1;
                                if (state == ProductCallback.Sur) {
                                 message.obj=1;
                                    handler.sendMessageDelayed(message,2000);
                                } else if (state == ProductCallback.TIMEOUT) {
                                    sendsocket(SocketType.WATERDATA);
                                    message.obj=2;
                                }else {
                                    message.obj=2;
                                    handler.sendMessageDelayed(message,2000);
                                }

                            }
                        }, Order.ExecutiveLevel.HIGH);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;

                case SocketType.stopwater:
                    try {
                        OrderManage.getInstance().stopWater(Contants.TYPE_WATER, new ProductCallback() {
                            @Override
                            public void onDataReceived(int state, String age) {
                                message.what=2;
                                if (state == ProductCallback.Sur) {
                                 message.obj=1;
                                    handler.sendMessageDelayed(message,2000);
                                } else if (state == ProductCallback.TIMEOUT) {
                                    sendsocket(SocketType.stopwater);

                                }else {
                                    message.obj=2;
                                    handler.sendMessageDelayed(message,2000);
                                }

                            }
                        }, Order.ExecutiveLevel.HIGH);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        class UpdateAsyncTask extends AsyncTask<String, Integer, String> {
            Context context;
            public UpdateAsyncTask(Context context) {
                this.context = context;
            }

            @Override
            protected String doInBackground(String... arg0) {
                if (manager.isUpdate()) {
                    return "有新版本";
                } else {
                    return "无新版本";
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result.equals("有新版本")) {
                    App.toast("正在下载新版本apk");
                    manager.downloadAPK();
                }else {
                    App.toast("已是最新版apk,无需更新");
                }
            }
        }

        private String getVerName(Context context) {
            String verName = "";
            try {
                verName = context.getPackageManager().
                        getPackageInfo(context.getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return verName;
        }
    }


}
