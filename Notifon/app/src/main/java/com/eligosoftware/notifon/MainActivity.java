package com.eligosoftware.notifon;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.eligosoftware.notifon.database.NotifonDBScheme;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends SingleFragmentActivity {


    public static final String NOTIFICATION_TRUNIT = "notification_trunit";

    @Override
    protected Fragment createFragment() {
        return MainFragment.newInstance();
    }

    public static Intent newIntent(Context context, Trunit trunit) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(NOTIFICATION_TRUNIT, trunit);

        return intent;
    }
    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);*/

        /*String s1="create table "+ NotifonDBScheme.Lang.NAME+"("+
                " _id integer primary key autoincrement not null, "+
                NotifonDBScheme.Lang.Cols.NAME+" TEXT NOT NULL, " +
                NotifonDBScheme.Lang.Cols.FULL_NAME+" TEXT NOT NULL " +
                ")";
        String s2="create table "+NotifonDBScheme.Word.NAME+"("+
                " _id integer primary key autoincrement not null, "+
                NotifonDBScheme.Word.Cols.WORD+" TEXT NOT NULL, "+
                NotifonDBScheme.Word.Cols.LANGID+" integer NOT NULL, "+
                NotifonDBScheme.Word.Cols.KNOW+" integer NOT NULL, "+
                NotifonDBScheme.Word.Cols.LEVEL+" TEXT NOT NULL , "+
                "FOREIGN KEY ("+NotifonDBScheme.Word.Cols.LANGID+") REFERENCES "+NotifonDBScheme.Lang.NAME+"(_id) "
                +")";
        String s3="create table "+NotifonDBScheme.Descr.NAME+"("+
                " _id integer primary key autoincrement not null, "+
                NotifonDBScheme.Descr.Cols.MEANING +" TEXT NOT NULL, "+
                NotifonDBScheme.Descr.Cols.LANGID +" integer NOT NULL, "+
                NotifonDBScheme.Descr.Cols.WORDID +" integer NOT NULL, "+
                "FOREIGN KEY ("+NotifonDBScheme.Descr.Cols.LANGID+") REFERENCES "+NotifonDBScheme.Lang.NAME+"(_id) ,"+
                "FOREIGN KEY ("+NotifonDBScheme.Descr.Cols.WORDID+") REFERENCES "+NotifonDBScheme.Word.NAME+"(_id) "+
                ")";

        AssetManager assetManager=getAssets();
        final String CSVPATH="my.csv";
        int index=0;
        try {
            InputStream csvStream = assetManager.open(CSVPATH);
            InputStreamReader csvStreamReader=new InputStreamReader(csvStream);
            CSVReader csvReader=new CSVReader(csvStreamReader);
            String[] line;

            String insertWord="insert into "+NotifonDBScheme.Word.NAME+" ("+
                    NotifonDBScheme.Word.Cols.WORD+","+
                    NotifonDBScheme.Word.Cols.LANGID+","+
                    NotifonDBScheme.Word.Cols.LEVEL+","+
                    NotifonDBScheme.Word.Cols.KNOW
                    +") values ";
            String insertDescr="insert into "+NotifonDBScheme.Descr.NAME+" ("+
                    NotifonDBScheme.Descr.Cols.MEANING+","+
                    NotifonDBScheme.Descr.Cols.LANGID+","+
                    NotifonDBScheme.Descr.Cols.WORDID
                    +") values ";

            String level;

            while((line=csvReader.readNext())!=null){
                index++;
                if (line.length<4){
                    continue;
                }
                level=line[3].replace(" ","");
                insertWord=insertWord+"("+
                        "'"+line[1]+"', "+
                        2+", "+
                        "'"+level+"', "+
                        0
                        +"),";

                insertDescr=insertDescr+"("+
                        "'"+line[0]+"', "+
                        1+", "+
                        index
                        +"),";

            }
            insertWord=insertWord.substring(0,insertWord.length()-1);
            insertDescr=insertDescr.substring(0,insertDescr.length()-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
    // }



}
