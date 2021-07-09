package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.communication.Order;
import com.bayi.rerobot.communication.OrderManage;
import com.bayi.rerobot.communication.ProductCallback;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.KeyboardUtils;
import com.bayi.rerobot.util.SocketType;
import com.bayi.rerobot.util.SpHelperUtil;

import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;

public class IOTsetDialog extends Dialog {

    public IOTsetDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder implements View.OnClickListener {
        private final Context mContext;
        private final IOTsetDialog mSpsDialog;

        private SpHelperUtil spHelperUtil;
        private EditText ip, port, osautn;
        private CheckBox checkbox,checkbox1;
        private String Mtype,level;
        private Handler handler1 = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        App.toast(String.format("设置%s%s成功",Mtype,level));
                        break;
                    case 1:
                        App.toast(String.format("设置%s%s失败",Mtype,level));
                        break;

                    default:
                        break;
                }
            }
        };
        public Builder(Context mContext) {
            this.mContext = mContext;
            mSpsDialog = new IOTsetDialog(mContext, R.style.style_dialog);
        }

        private View view;

        public IOTsetDialog create() {
            spHelperUtil = new SpHelperUtil(App.context);
            view = LayoutInflater.from(mContext).inflate(R.layout.iot_set_dialog, null);

            mSpsDialog.setCanceledOnTouchOutside(false);

            Window window = mSpsDialog.getWindow();
            window.setContentView(view);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = mSpsDialog.getWindow().getAttributes();

            lp.width = (int)(getScreenWidth()*1);
            lp.height = (int)(getScreenHeight()*1);

            lp.gravity = Gravity.TOP;
            view.findViewById(R.id.save).setOnClickListener(this);
            view.findViewById(R.id.back).setOnClickListener(this);
            view.findViewById(R.id.wind_low).setOnClickListener(this);
            view.findViewById(R.id.wind_middle).setOnClickListener(this);
            view.findViewById(R.id.wind_high).setOnClickListener(this);
            view.findViewById(R.id.wuliang_low).setOnClickListener(this);
            view.findViewById(R.id.wuliang_middle).setOnClickListener(this);
            view.findViewById(R.id.wuliang_high).setOnClickListener(this);
            ip = view.findViewById(R.id.edit_ip);
            port = view.findViewById(R.id.edit_port);
            osautn = view.findViewById(R.id.edit_osautn);
            checkbox = view.findViewById(R.id.checkbox);
            checkbox1 = view.findViewById(R.id.checkbox1);


            ip.setText((String) spHelperUtil.getSharedPreference(Contants.IOT_IP, "115.231.60.194"));
            port.setText((String) spHelperUtil.getSharedPreference(Contants.IOT_PORT, "23"));
            osautn.setText((String) spHelperUtil.getSharedPreference(Contants.IOT_osautn, "60da7bdd-78e4-4b39-9f0f-11b20543cc2c"));
            mSpsDialog.getWindow().setAttributes(lp);

            return mSpsDialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.save:
                    if (!ip.getText().toString().equals("") && !port.getText().toString().equals("")) {
                        spHelperUtil.put(Contants.IOT_IP, ip.getText().toString().trim());
                        spHelperUtil.put(Contants.IOT_PORT, port.getText().toString().trim());
                        spHelperUtil.put(Contants.IOT_osautn, osautn.getText().toString().trim());
                        KeyboardUtils.hideKeyboard(view);
                        mSpsDialog.dismiss();
                    } else {
                        App.toast("请输入完成地址");
                    }
                    break;
                case R.id.back:
                    KeyboardUtils.hideKeyboard(view);
                    mSpsDialog.dismiss();
                    break;
                case R.id.wind_low:
                    level="低";
                    spHelperUtil.put("wind", 100);
                    sendsocket(SocketType.WIND);
                    break;
                case R.id.wind_middle:
                    level="中";
                    spHelperUtil.put("wind", 150);
                    sendsocket(SocketType.WIND);
                    break;
                case R.id.wind_high:
                    level="高";
                    spHelperUtil.put("wind", 200);
                    sendsocket(SocketType.WIND);
                    break;
                case R.id.wuliang_low:
                    level="低";
                    spHelperUtil.put("wuliang", 100);
                    sendsocket(SocketType.WULIANG);
                    break;
                case R.id.wuliang_middle:
                    level="中";
                    spHelperUtil.put("wuliang", 150);
                    sendsocket(SocketType.WULIANG);
                    break;
                case R.id.wuliang_high:
                    level="高";
                    spHelperUtil.put("wuliang", 200);
                    sendsocket(SocketType.WULIANG);
                    break;
                default:
                    break;
            }
        }

        private void sendsocket(int type) {
            switch (type) {

                case SocketType.WIND:
                    Mtype="风速";
                    try {
                        OrderManage.getInstance().setWIND(Contants.TYPE_REBORT, new ProductCallback() {
                            @Override
                            public void onDataReceived(int state, String age) {
                                if (state == ProductCallback.Sur) {
                                    handler1.obtainMessage(0).sendToTarget();
                                } else if (state == ProductCallback.TIMEOUT) {
                                    sendsocket(SocketType.WIND);
                                }else {
                                    handler1.obtainMessage(1).sendToTarget();
                                }
                            }
                        }, Order.ExecutiveLevel.HIGH);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    break;
                case SocketType.WULIANG:
                    Mtype="雾量";
                    try {
                        OrderManage.getInstance().setWULIANG(Contants.TYPE_REBORT, new ProductCallback() {
                            @Override
                            public void onDataReceived(int state, String age) {
                                if (state == ProductCallback.Sur) {
                                    handler1.obtainMessage(0).sendToTarget();
                                } else if (state == ProductCallback.TIMEOUT) {
                                    sendsocket(SocketType.WULIANG);
                                }else {
                                    handler1.obtainMessage(1).sendToTarget();
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
}
