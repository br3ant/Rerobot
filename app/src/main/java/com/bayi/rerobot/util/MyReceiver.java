package com.bayi.rerobot.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.bayi.rerobot.ui.FirstActivity;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Intent i = new Intent(context, FirstActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }
}