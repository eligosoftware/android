package com.eligosoftware.notifon;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Process;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.eligosoftware.notifon.database.TranslatesBase;

/**
 * Created by mragl on 19.11.2016.
 */

public class NotifonService extends IntentService {
    private static final String TAG = "PollService";

    public static Intent newIntent(Context context) {
        return new Intent(context, NotifonService.class);
    }

    public NotifonService() {
        super(TAG);
        //  mTranslatesBase=TranslatesBase.get(this);
    }

    public static void setServiceAlarm(Context context, boolean isOn) {

        Intent i = NotifonService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);


        if (isOn) {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + NotifonPreferences.getNotifPeriod(context) * 60 * 1000, NotifonPreferences.getNotifPeriod(context) * 60 * 1000, pi);
            //  alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(),1000,pi);

        } else {

            alarmManager.cancel(pi);
            pi.cancel();
        }
    }

    public static boolean isServiceAlarmOn(Context context) {
        Intent i = NotifonService.newIntent(context);
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Resources resources = getResources();
        TranslatesBase mTranslatesBase = TranslatesBase.get(this);
        int max = mTranslatesBase.getTrunitCount(null);

        int last_shown_word = NotifonPreferences.getLastShownWord(this);

        if (last_shown_word >= max) {
            last_shown_word = 0;
        }
        Trunit trunit = mTranslatesBase.getNextTrunit(last_shown_word);
        //NotifonPreferences.setLastShownWord(this,last_shown_word);
        last_shown_word = trunit.get_id();
        NotifonPreferences.setLastShownWord(this, last_shown_word);

        if (trunit != null) {
            Intent i = MainActivity.newIntent(this, trunit);
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);


            Uri alarm_sount = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent notif = new Intent(this, NotifonNotificationActivity.class);

            notif.putExtra("method", "setknown");
            notif.putExtra("trunit", trunit);
            PendingIntent pi1 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), notif, 0);
            NotificationCompat.Action setknown = new NotificationCompat.Action.Builder(android.R.drawable.checkbox_on_background, "Set known", pi1).build();

            Intent notif_stop = new Intent(this, NotifonNotificationActivity.class);
            notif_stop.putExtra("method", "stopNotifications");

            PendingIntent pi2 = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), notif_stop, 0);
            NotificationCompat.Action stop_notif = new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_close_clear_cancel, "Stop", pi2).build();


            Notification notification = new NotificationCompat.Builder(this)
                    .setTicker(resources.getString(R.string.next_word))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(trunit.getWord())
                    .setContentText(trunit.getDescr())
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setSound(alarm_sount)
                    .setLights(Color.GREEN, 500, 500)
                    .addAction(setknown)
                    .addAction(stop_notif)
                    .build();

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
            notificationManagerCompat.notify(0, notification);

        }
    }
}
