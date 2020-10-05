package com.eligosoftware.notifon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by mragl on 19.11.2016.
 */

public class StartUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       boolean isOn=NotifonPreferences.isAlarmOn(context);
        Context context1=context;
                NotifonService.setServiceAlarm(context1,isOn);

    }
}
