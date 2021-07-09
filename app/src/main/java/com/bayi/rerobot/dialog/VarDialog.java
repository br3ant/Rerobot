package com.bayi.rerobot.dialog;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bayi.rerobot.R;

import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;


/**
 * Created by bayi01 on 2016/1/12.
 */
public class VarDialog implements View.OnClickListener {
    Context context;
    private Dialog loadingDialog;
    private varDialogListener listener;

    private EditText editpassword;
    private String content;
   public VarDialog(Context context,String content, varDialogListener listener) {
        this.listener = listener;
        this.context = context;
        this.content=content;
    }

    @SuppressLint("InflateParams")
    public Dialog createcheckDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v= inflater.inflate(R.layout.var, null);
        v.findViewById(R.id.checkmm_qr).setOnClickListener(this);
        LinearLayout layout = v.findViewById(R.id.lcheck_mm);
        editpassword=  v.findViewById(R.id.checkmm);
        if(content!=null){
            editpassword.setText(content);
        }
        editpassword.setSelection(editpassword.getText().length());
        loadingDialog = new Dialog(context, R.style.timer_Dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(false); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); //点击加载框以外的区域

        Window window = loadingDialog.getWindow();
        window.setContentView(v);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = loadingDialog.getWindow().getAttributes();

        lp.width = (int)(getScreenWidth()*0.8);
        lp.height = (int)(getScreenHeight()*0.15);
        loadingDialog.getWindow().setAttributes(lp);
        loadingDialog.show();
        return loadingDialog;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.checkmm_qr:
                String str =editpassword.getText().toString();
                listener.getValue(str, v);
                loadingDialog.dismiss();
                break;
        }

    }



    /**
     * 回调函数
     */
    public interface varDialogListener{
        /**
         *
         * @param p1 输入密码
         */
        void getValue(String p1, View view);
    }

}
