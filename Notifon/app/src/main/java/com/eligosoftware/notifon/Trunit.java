package com.eligosoftware.notifon;

import java.io.Serializable;

/**
 * Created by mragl on 09.11.2016.
 */

public class Trunit implements Serializable{
    private static final long serialVersionUID = 1L;

    private int _id;
    private String word;

    public int getWordid() {
        return wordid;
    }

    public void setWordid(int wordid) {
        this.wordid = wordid;
    }

    private int wordid;
    private int lang_id;
    private String lang;
    private String meaning;
    private int descr_lang_id;
    private String descr_lang;
    private int know;
    private String partsp;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    private String level;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getLang_id() {
        return lang_id;
    }

    public void setLang_id(int lang_id) {
        this.lang_id = lang_id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDescr() {
        return meaning;
    }

    public void setDescr(String descr) {
        this.meaning = descr;
    }

    public int getDescr_lang_id() {
        return descr_lang_id;
    }

    public void setDescr_lang_id(int descr_lang_id) {
        this.descr_lang_id = descr_lang_id;
    }

    public String getDescr_lang() {
        return descr_lang;
    }

    public void setDescr_lang(String descr_lang) {
        this.descr_lang = descr_lang;
    }

    public int getKnow() {
        return know;
    }

    public void setKnow(int know) {
        this.know = know;
    }

    public String getPartsp() {
        return partsp;
    }

    public void setPartsp(String partsp) {
        this.partsp = partsp;
    }



}
