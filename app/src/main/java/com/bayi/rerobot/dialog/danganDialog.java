package com.bayi.rerobot.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bayi.rerobot.R;

import static com.bayi.rerobot.util.DisplayUtil.getScreenHeight;
import static com.bayi.rerobot.util.DisplayUtil.getScreenWidth;

/**
 * Created by tongzn
 * on 2021/3/8
 */
public class danganDialog extends Dialog {
    public interface  dismissListener{
        void dismiss();
    }
    private danganDialog(@NonNull Context context) {
        super(context);
    }
    private danganDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
    private danganDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

        public static class Builder  {
            private final Context mContext;
            private final danganDialog mSpsDialog;
            private dismissListener mdiss = null;
            public Builder(Context mContext) {
                this.mContext = mContext;
                mSpsDialog = new danganDialog(mContext, R.style.dialog);
            }
           public void getdismissListener(dismissListener dis){
                this.mdiss=dis;
           }
           public boolean isShow(){
                return mSpsDialog.isShowing();
           }
           public void dismiss(){
               mSpsDialog.dismiss();
           }
            public void show(){
               mSpsDialog.show();
            }
            public danganDialog create(){

                View view = LayoutInflater.from(mContext).inflate(R.layout.dangan_dialog,null);
                mSpsDialog.setCanceledOnTouchOutside(false);
                Window window = mSpsDialog.getWindow();
                window.setContentView(view);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams lp = mSpsDialog.getWindow().getAttributes();

                lp.width = (int)(getScreenWidth()*0.8);
                lp.height = (int)(getScreenHeight()*0.5);

                lp.gravity = Gravity.CENTER;
                mSpsDialog.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                       if(mdiss!=null){
                           mdiss.dismiss();
                       }
                    }
                });
                mSpsDialog.getWindow().setAttributes(lp);

                return mSpsDialog;
            }


        }

    }

