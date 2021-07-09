package com.bayi.rerobot.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.bayi.rerobot.R;

import com.iflytek.cloud.VoiceWakeuper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class FirstActivity extends BaseActivity {

    @BindView(R.id.nlp_text)
    EditText mNlpText;





    @Override
    public void initView() {
        setContentView(R.layout.activity_first);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        FirstActivityPermissionsDispatcher.needpermissionWithPermissionCheck(this);
        EventBus.getDefault().register(this);
        //LogUtil.saveLog("开机了");
    }

    @NeedsPermission({Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    void needpermission() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        FirstActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA})
    public void OnPermissionDenied() {
        finish();
    }

    private void showTip(final String str) {
        toast(str);
    }

    @Override
    protected void onResume() {
        super.onResume();

   startActivity(new Intent(FirstActivity.this,NewMain.class));
   finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(String message) {
        if(message.equals("")){
            mNlpText.setText("");
        }else {
            mNlpText.append(message);
        }

    }

}
