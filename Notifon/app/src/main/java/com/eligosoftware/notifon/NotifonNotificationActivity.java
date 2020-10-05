package com.eligosoftware.notifon;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;

import com.eligosoftware.notifon.database.TranslatesBase;

/**
 * Created by mragl on 24.11.2016.
 */

public class NotifonNotificationActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String method=getIntent().getStringExtra("method");
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.cancel(0);
        if (method.equals("setknown")){
            Trunit trunit=(Trunit)getIntent().getSerializableExtra("trunit");
            setknown(trunit,this.getApplicationContext());
        }
        else if (method.equals("stopNotifications")){
            stopNotifications(this.getApplicationContext());
        }
        finish();
    }
    private void setknown(Trunit trunit, Context context){

        TranslatesBase mTranslateBase=TranslatesBase.get(context);
        mTranslateBase.updateTrunit(trunit.getWordid(),true);
    }
    private void stopNotifications(Context context){
        NotifonPreferences.setAlarmOn(context,false);
            NotifonService.setServiceAlarm(context,false);
    }
}
