package com.eligosoftware.notifon.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eligosoftware.notifon.Trunit;
import com.eligosoftware.notifon.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by mragl on 09.11.2016.
 */

public class TranslatesBase {
    private static TranslatesBase sTranslatesBase;
    private Context mAppContext;

    private SQLiteDatabase mDatabase;

    public TrunitCursorWrapper queryTrunits(String condition,int limit){

        String query="SELECT word._id as wordid, word.word, word.langid as langid, lang1.name as lang, descr.meaning, lang2.name as descr_lang, descr.langid "
        +"as descr_lang_id, word.know, descr.partsp, descr._id as _id , word.level "+
                "from word as word "+
                "join descr as descr "+
                "on descr.wordid=word._id "+
                "join lang as lang1 "+
                "on word.langid=lang1._id "+
                "join lang as lang2 "+
                "on descr.langid=lang2._id ";
        String order=        " order by word._id , descr._id";
        if (condition!=null){
            query=query+" where "+condition;
        }

            query=query+order;
        if (limit!=0){
            query=query+" limit "+limit;
        }
       /* SELECT lang1.name as tr1,lang2.name as tr2,l1,l2,k,l, count(*)
        from (
                select descr.langid as l2 ,word.langid as l1,know as k,level as l
                from descr as descr
                join word as word
                on descr.wordid = word.id
        ) as distinctified

        join lang as lang1
        on l1=lang1.id
        join lang as lang2
        on l2=lang2.id
        group by tr1,tr2,k,l*/
/*try {*/
        Cursor cursor=null;
        try{
    cursor = mDatabase.rawQuery(query, null);}
        catch (Exception e){
            e.printStackTrace();
        }
    return new TrunitCursorWrapper(cursor);
/*}
catch (Exception e){
    e.printStackTrace();
}
return null;*/

    }

    public List<Word> getWords(){
        List<Word> hm=new ArrayList();
        Word w=null;
       Cursor cursor= mDatabase.query(NotifonDBScheme.Word.NAME,new String[]{"_id",NotifonDBScheme.Word.Cols.WORD},null,null,null,null,null);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                w=new Word();
                w.id=cursor.getInt(cursor.getColumnIndex("_id"));
                w.Word=cursor.getString(cursor.getColumnIndex(NotifonDBScheme.Word.Cols.WORD));
                hm.add(w);
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return hm;
    }

    public int getTrunitCount(String condition){
        String query="SELECT max(word._id) as max "+
                "from word as word "+
                "join descr as descr "+
                "on descr.wordid=word._id";

        if (condition!=null){
            query=query+" where "+condition;
        }
        Cursor c=mDatabase.rawQuery(query,null);
        int max=0;
        while (c.moveToNext()){
            max=c.getInt(c.getColumnIndex("max"));
        }
        return max;
    }

    public long createSTrunit(String word,String partsp,String definition){
        ContentValues cv=new ContentValues();
        String searchvalue=word+" "+
                            partsp+" "+
                            definition;
        cv.put(NotifonDBHelper.KEY_WORD,word);
        cv.put(NotifonDBHelper.KEY_PARTSP,partsp);
        cv.put(NotifonDBHelper.KEY_DESCR,definition);
        cv.put(NotifonDBHelper.KEY_SEARCH,searchvalue);

        return mDatabase.insert(NotifonDBHelper.FTS_VIRTUAL_TABLE,null,cv);

    }
    public void  createSTrunits(){
        String query="insert into "+NotifonDBHelper.FTS_VIRTUAL_TABLE+" ("+NotifonDBHelper.KEY_WORD+", "+NotifonDBHelper.KEY_PARTSP
                +", "+NotifonDBHelper.KEY_DESCR+", "+NotifonDBHelper.KEY_SEARCH+") "+
                "SELECT word.word as "+NotifonDBHelper.KEY_WORD+",descr.partsp as "+NotifonDBHelper.KEY_PARTSP+", descr.meaning as "+NotifonDBHelper.KEY_DESCR+", word.word ||' '|| descr.partsp ||' '|| descr.meaning as "+NotifonDBHelper.KEY_SEARCH+

                " from word as word "+
                "join descr as descr "+
                "on descr.wordid=word._id ";
        mDatabase.execSQL(query);
    }
    public Cursor searchSTrunit(String inputText){

        String query="SELECT docid as _id, "+
                NotifonDBHelper.KEY_WORD+", "+
        NotifonDBHelper.KEY_DESCR+", "+
                NotifonDBHelper.KEY_PARTSP+
                " from "+NotifonDBHelper.FTS_VIRTUAL_TABLE+
                " where "+NotifonDBHelper.KEY_SEARCH+" MATCH '"+inputText+"';";
        Cursor cursor=mDatabase.rawQuery(query,null);

        if (cursor!=null)
        {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean deleteAllSTrunits(){
        int donedelete=0;
        donedelete=mDatabase.delete(NotifonDBHelper.FTS_VIRTUAL_TABLE,null,null);
        return donedelete>0;
    }





    private TranslatesBase(Context context){
        mAppContext=context.getApplicationContext();
        mDatabase=new NotifonDBHelper(mAppContext).getWritableDatabase();
    }

    public List<Trunit> getTrunits(String condition){
        List<Trunit> trunits=new ArrayList<>();
        TrunitCursorWrapper cursor=queryTrunits(condition,0);
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                trunits.add(cursor.getTrunit());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }

        return trunits;
    }
    public Trunit getTrunit(String condition,int limit){
        TrunitCursorWrapper cursor=queryTrunits(condition,limit);
        Trunit trunit=null;
        try{
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                trunit =(cursor.getTrunit());
                break;
            }
        }
        finally {
            cursor.close();
        }
        return trunit;
    }
    public static TranslatesBase get(Context context)
    {
        if (sTranslatesBase == null){
            sTranslatesBase=new TranslatesBase(context);
        }
        return sTranslatesBase;
    }

    public Trunit getRandomTrunit(){

        Random random=new Random();
        List<Trunit> trunits=getTrunits("word.know = 0");
        int max=trunits.size()-1;
        int pos=random.nextInt(max+1);

        return trunits.get(pos);
    }
    public Trunit getNextTrunit(int wordid){


        Trunit trunit=getTrunit("word.know = 0 and word._id>"+wordid,1);

        return trunit;
    }


    public void updateTrunit(int word_id,boolean knowit){
        ContentValues cv=new ContentValues();
        cv.put(NotifonDBScheme.Word.Cols.KNOW,knowit);

        mDatabase.update(NotifonDBScheme.Word.NAME,cv,"_id=?",new String[]{word_id+""});
    }
    public void updateTrunit(Trunit mTrunit){
        ContentValues cv=new ContentValues();
        cv.put(NotifonDBScheme.Word.Cols.KNOW,mTrunit.getKnow());
        cv.put(NotifonDBScheme.Word.Cols.LEVEL,mTrunit.getLevel());
        cv.put(NotifonDBScheme.Word.Cols.WORD,mTrunit.getWord());

        mDatabase.update(NotifonDBScheme.Word.NAME,cv,"_id=?",new String[]{mTrunit.getWordid()+""});

        cv.clear();
        cv.put(NotifonDBScheme.Descr.Cols.MEANING,mTrunit.getDescr());
        cv.put(NotifonDBScheme.Descr.Cols.PARTSP,mTrunit.getPartsp());

        mDatabase.update(NotifonDBScheme.Descr.NAME,cv,"_id=?",new String[]{mTrunit.get_id()+""});
    }
    public void addTrunit(Trunit mTrunit){
        ContentValues cv=new ContentValues();
        cv.put(NotifonDBScheme.Word.Cols.WORD,mTrunit.getWord());
        cv.put(NotifonDBScheme.Word.Cols.LEVEL,mTrunit.getLevel());
        cv.put(NotifonDBScheme.Word.Cols.KNOW,mTrunit.getKnow());
        cv.put(NotifonDBScheme.Word.Cols.LANGID,mTrunit.getLang_id());

        long word_id=mDatabase.insert(NotifonDBScheme.Word.NAME,null,cv);


        cv.clear();
        cv.put(NotifonDBScheme.Descr.Cols.WORDID,word_id);
        cv.put(NotifonDBScheme.Descr.Cols.PARTSP,mTrunit.getPartsp());
        cv.put(NotifonDBScheme.Descr.Cols.MEANING,mTrunit.getDescr());
        cv.put(NotifonDBScheme.Descr.Cols.LANGID,mTrunit.getDescr_lang_id());

        mDatabase.insert(NotifonDBScheme.Descr.NAME,null,cv);
    }

    public void deleteTrunit(Trunit mTrunit){
       List<Trunit> list=getTrunits("word._id="+mTrunit.getWordid());
        mDatabase.delete(NotifonDBScheme.Descr.NAME,"_id=?",new String[]{mTrunit.get_id()+""});
        if (list.size()==1){   // if we have 2 and more languages check to not leave "words without translations"
            mDatabase.delete(NotifonDBScheme.Word.NAME,"_id=?",new String[]{mTrunit.getWordid()+""});
        }
    }

    /*public void close(){
        if (mDatabase!=null){
            mDatabase.close();
        }
    }*/
}
