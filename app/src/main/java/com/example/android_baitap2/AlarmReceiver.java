package com.example.android_baitap2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    public static final String ChannelID="ChannelTuh";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("Loi","OKokok");


    }
}
