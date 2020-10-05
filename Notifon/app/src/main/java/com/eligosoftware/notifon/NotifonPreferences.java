package com.eligosoftware.notifon;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by mragl on 19.11.2016.
 */

public class NotifonPreferences {
    private static final String PREF_LAST_SHOWN_WORD="lastWord";
    private static final String PREF_NOTIF_PERIOD="period";
    private static final String PREF_IS_ALARM_ON="isAlarmOn";

    public static int getLastShownWord(Context context){
        return Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_SHOWN_WORD,"-1"));
    }
    public static void setLastShownWord(Context context,int lw_id){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_SHOWN_WORD,lw_id+"")
                .apply();
    }
    public static int getNotifPeriod(Context context){
        return Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_NOTIF_PERIOD,"15"));
    }
    public static void setNotifPeriod(Context context,int period){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_NOTIF_PERIOD,period+"")
                .apply();
    }

    public static boolean isAlarmOn(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_IS_ALARM_ON,false);
    }

    public static void setAlarmOn(Context context, boolean isOn){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_IS_ALARM_ON,isOn)
                .apply();
    }

}
