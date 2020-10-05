package com.eligosoftware.notifon;



import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;

import com.eligosoftware.notifon.database.TranslatesBase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * Created by mragl on 19.11.2016.
 */

public class SettingsActivity extends PreferenceActivity {

    private AppCompatDelegate mDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //addPreferencesFromResource(R.xml.settings);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content,new SettingsFragment()).commit();

        // my_child_toolbar is defined in the layout file
//        Toolbar myChildToolbar =
//                (Toolbar) findViewById(and);
//        setSupportActionBar(myChildToolbar);


        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getActionBar().setDisplayHomeAsUpEnabled(true);

        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private AppCompatDelegate getDelegate() {
        if (mDelegate == null) {
            mDelegate = AppCompatDelegate.create(this, null);
        }
        return mDelegate;
    }
    public static class SettingsFragment extends PreferenceFragment{

        SharedPreferences.OnSharedPreferenceChangeListener listener;
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.settings);

            listener=new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                    NotifonService.setServiceAlarm(getActivity().getApplicationContext(),NotifonPreferences.isAlarmOn(getActivity().getApplicationContext()));
                }
            };
            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(listener);

           final ListPreference listPreference=(ListPreference)findPreference("lastWord");
            setListPreferenceData(listPreference,getActivity());

        }

        protected static void setListPreferenceData(ListPreference lp,Context context) {
            TranslatesBase mTranslateBase=TranslatesBase.get(context);

            ArrayList<String> entries=new ArrayList();
            ArrayList<String> entryValues=new ArrayList();

            entries.add("-1");
            entryValues.add("From start");

            List<Word> hm=mTranslateBase.getWords();

            for (Word w:hm){
               entries.add(w.id+"");
                entryValues.add(w.Word);
            }


            String[] entriesA=entries.toArray(new String[entries.size()]);
            String[] entryValuesA=entryValues.toArray(new String[entryValues.size()]);
            lp.setEntries(entryValuesA);
            lp.setDefaultValue("-1");
            lp.setEntryValues(entriesA);

        }
        @Override
        public void onDestroy() {
            super.onDestroy();
            PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(listener);

        }
    }
}
