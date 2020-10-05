package com.eligosoftware.notifon.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.eligosoftware.notifon.Trunit;

/**
 * Created by mragl on 09.11.2016.
 */

public class TrunitCursorWrapper extends CursorWrapper {
    public TrunitCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Trunit getTrunit(){
        int id=getInt(getColumnIndex("_id"));
        String word=getString(getColumnIndex(NotifonDBScheme.Word.Cols.WORD));
        int wordid=getInt(getColumnIndex("wordid"));
        String level=getString(getColumnIndex(NotifonDBScheme.Word.Cols.LEVEL));
        int know=getInt(getColumnIndex(NotifonDBScheme.Word.Cols.KNOW));
        int lang_id=getInt(getColumnIndex(NotifonDBScheme.Word.Cols.LANGID));
        String lang=getString(getColumnIndex("lang"));
        String meaning=getString(getColumnIndex(NotifonDBScheme.Descr.Cols.MEANING));
        int descr_lang_id=getInt(getColumnIndex("descr_lang_id"));
        String descr_lang=getString(getColumnIndex("descr_lang"));
        String partsp=getString(getColumnIndex(NotifonDBScheme.Descr.Cols.PARTSP));;

        Trunit trunit=new Trunit();
        trunit.set_id(id);
        trunit.setWord(word);
        trunit.setWordid(wordid);
        trunit.setDescr(meaning);
        trunit.setLevel(level);
        trunit.setKnow(know);
        trunit.setLang_id(lang_id);
        trunit.setLang(lang);
        trunit.setDescr_lang(descr_lang);
        trunit.setDescr_lang_id(descr_lang_id);
        trunit.setPartsp(partsp);

        return trunit;
    }
}
