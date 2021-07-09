package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.bayi.rerobot.App;
import com.bayi.rerobot.R;
import com.bayi.rerobot.util.Contants;
import com.bayi.rerobot.util.KeyboardUtils;
import com.bayi.rerobot.util.SpHelperUtil;

import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;

public class ITsetDialog extends Dialog {

    public ITsetDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    public static class Builder implements View.OnClickListener {
        private final Context mContext;
        private final ITsetDialog mSpsDialog;

        private SpHelperUtil spHelperUtil;
        private EditText ip,port;
        public Builder(Context mContext) {
            this.mContext = mContext;
            mSpsDialog = new ITsetDialog(mContext, R.style.style_dialog);
        }
        private View view;
        public ITsetDialog create(){
             spHelperUtil=new SpHelperUtil(App.context);
             view = LayoutInflater.from(mContext).inflate(R.layout.it_set_dialog,null);

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
            ip=view.findViewById(R.id.edit_ip);
            port=view.findViewById(R.id.edit_port);
            ip.setText((String)spHelperUtil.getSharedPreference(Contants.IT_IP,"http://115.231.60.194"));
            port.setText((String)spHelperUtil.getSharedPreference(Contants.IT_PORT,"9090"));

            mSpsDialog.getWindow().setAttributes(lp);


            return mSpsDialog;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.save:
                    if(!ip.getText().toString().equals("")&&!port.getText().toString().equals("")){
                   spHelperUtil.put(Contants.IT_IP,ip.getText().toString().trim());
                   spHelperUtil.put(Contants.IT_PORT,port.getText().toString().trim());
                   KeyboardUtils.hideKeyboard(view);
                   mSpsDialog.dismiss();
                    }else {
                       App.toast("请输入完成地址");
                    }
                    break;
                case R.id.back:
                    KeyboardUtils.hideKeyboard(view);
                    mSpsDialog.dismiss();
                    break;
                default:
                    break;
            }
        }
    }
}
