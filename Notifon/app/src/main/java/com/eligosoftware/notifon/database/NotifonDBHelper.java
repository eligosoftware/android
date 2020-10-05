package com.eligosoftware.notifon.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by mragl on 08.11.2016.
 */

public class NotifonDBHelper extends SQLiteOpenHelper {

    private static final int DBVERSION=1;
    private static final String DBNAME="notifon.db";
    private Context mContext;
    private final String CSVPATH="my.csv";

    //cols for virtual table

    //public static final String KEY_WORD_ID="word_id";
    //public static final String KEY_DESCR_ID="descr_id";
    public static final String KEY_WORD="word";
    public static final String KEY_DESCR="descr";
    public static final String KEY_PARTSP="partsp";
    //public static final String KEY_LANG_ID="lang_id";
    //public static final String KEY_DLANG_ID="dlang_id";
    public static final String KEY_SEARCH="searchData";


    public static final String FTS_VIRTUAL_TABLE="Trunit_info";

    private static final String DATABASE_CREATE=
            "CREATE VIRTUAL TABLE "+FTS_VIRTUAL_TABLE+ " USING fts3("+
                   /* KEY_WORD_ID+","+
                    KEY_DESCR_ID+","+*/
                    KEY_WORD+","+
                    KEY_DESCR+","+
                    KEY_PARTSP+","+
                    /*KEY_LANG_ID+","+
                    KEY_DLANG_ID+","+*/
                    KEY_SEARCH+","+
                   // " UNIQUE ("+KEY_WORD_ID+","+KEY_DESCR_ID+"));";
                    " UNIQUE ("+KEY_WORD+"));";
            ;

    public NotifonDBHelper(Context context) {
        super(context, DBNAME, null, DBVERSION);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("create table "+ NotifonDBScheme.Lang.NAME+"("+
                " _id integer primary key autoincrement not null, "+
                NotifonDBScheme.Lang.Cols.NAME+" TEXT NOT NULL, " +
                NotifonDBScheme.Lang.Cols.FULL_NAME+" TEXT NOT NULL " +
                ")");

        sqLiteDatabase.execSQL("create table "+NotifonDBScheme.Word.NAME+"("+
                " _id integer primary key autoincrement not null, "+
                NotifonDBScheme.Word.Cols.WORD+" TEXT NOT NULL, "+
                NotifonDBScheme.Word.Cols.LANGID+" integer NOT NULL, "+
                NotifonDBScheme.Word.Cols.KNOW+" integer NOT NULL, "+
                NotifonDBScheme.Word.Cols.LEVEL+" TEXT NOT NULL , "+
                "FOREIGN KEY ("+NotifonDBScheme.Word.Cols.LANGID+") REFERENCES "+NotifonDBScheme.Lang.NAME+"(_id) "
                +")");
        sqLiteDatabase.execSQL("create table "+NotifonDBScheme.Descr.NAME+"("+
                " _id integer primary key autoincrement not null, "+
                NotifonDBScheme.Descr.Cols.MEANING +" TEXT NOT NULL, "+
                NotifonDBScheme.Descr.Cols.LANGID +" integer NOT NULL, "+
                NotifonDBScheme.Descr.Cols.WORDID +" integer NOT NULL, "+
                NotifonDBScheme.Descr.Cols.PARTSP +" TEXT NOT NULL, "+
                "FOREIGN KEY ("+NotifonDBScheme.Descr.Cols.LANGID+") REFERENCES "+NotifonDBScheme.Lang.NAME+"(_id) ,"+
                "FOREIGN KEY ("+NotifonDBScheme.Descr.Cols.WORDID+") REFERENCES "+NotifonDBScheme.Word.NAME+"(_id) "+
                ")");

        ContentValues cv=new ContentValues();
        cv.put(NotifonDBScheme.Lang.Cols.NAME,"aze"); //1
        cv.put(NotifonDBScheme.Lang.Cols.FULL_NAME,"Azərbaycan");
        sqLiteDatabase.insert(NotifonDBScheme.Lang.NAME,null,cv);
        cv.clear();
        cv.put(NotifonDBScheme.Lang.Cols.NAME,"eng"); //2
        cv.put(NotifonDBScheme.Lang.Cols.FULL_NAME,"English");
        sqLiteDatabase.insert(NotifonDBScheme.Lang.NAME,null,cv);
        cv.clear();
        cv.put(NotifonDBScheme.Lang.Cols.NAME,"rus"); //3
        cv.put(NotifonDBScheme.Lang.Cols.FULL_NAME,"Русский");
        sqLiteDatabase.insert(NotifonDBScheme.Lang.NAME,null,cv);

        AssetManager assetManager=mContext.getAssets();

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
                    NotifonDBScheme.Descr.Cols.WORDID+","+
                    NotifonDBScheme.Descr.Cols.PARTSP
                    +") values ";
            String level,partsp;
            int index=1;
            while((line=csvReader.readNext())!=null){
                if (line.length<4){
                    continue;
                }
                 level=line[3].replace(" ","");
                partsp=line[2].replace(" ","");
                insertWord=insertWord+"("+
                        "'"+line[1].toLowerCase()+"', "+
                        2+", "+
                        "'"+level+"', "+
                        0
                        +"),";

                insertDescr=insertDescr+"("+
                        "'"+line[0].toLowerCase()+"', "+
                        1+", "+
                        index+", "+
                        "'"+partsp
                        +"'),";
                index++;
            }
            insertWord=insertWord.substring(0,insertWord.length()-1);
            insertDescr=insertDescr.substring(0,insertDescr.length()-1);

            sqLiteDatabase.execSQL(insertWord);
            sqLiteDatabase.execSQL(insertDescr);


            String supportedLangs="create table "+ NotifonDBScheme.SupportedLanguages.NAME+" ("+
                    " _id integer primary key autoincrement not null, "+
                    NotifonDBScheme.SupportedLanguages.Cols.LANGID+" int not null, "+
                    NotifonDBScheme.SupportedLanguages.Cols.DLANGID+" int not null"+
                    ")";
            sqLiteDatabase.execSQL(supportedLangs);

            cv.clear();
            cv.put(NotifonDBScheme.SupportedLanguages.Cols.LANGID,2);
            cv.put(NotifonDBScheme.SupportedLanguages.Cols.DLANGID,1);

            sqLiteDatabase.insert(NotifonDBScheme.SupportedLanguages.NAME,null,cv);

        } catch (IOException e) {
            e.printStackTrace();
        }


        sqLiteDatabase.execSQL(DATABASE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);//destroy old data of virtual table
        sqLiteDatabase.execSQL(DATABASE_CREATE);
    }


}
