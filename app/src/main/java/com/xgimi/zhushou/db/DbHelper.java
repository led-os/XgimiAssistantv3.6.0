package com.xgimi.zhushou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/6/20.
 */
public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "zhushousan", null, 10);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table searchlivehistory(_id integer primary key autoincrement ,name)");
        db.execSQL("create table searchmoviehistory(_id integer primary key autoincrement ,name)");
        db.execSQL("create table applyrecord(_id integer primary key autoincrement ,name)");
        db.execSQL("create table musichository(_id integer primary key autoincrement ,id,title,singer)");
        db.execSQL("create table playhository(_id integer primary key autoincrement ,icon,time,playid,title,kind)");
        db.execSQL("create table yijian(_id integer primary key autoincrement ,data,type)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("drop table IF EXISTS " + "record");
            db.execSQL("drop table IF EXISTS " + "applyrecord");
            db.execSQL("drop table IF EXISTS " + "musichository");
            db.execSQL("drop table IF EXISTS " + "playhository");
            db.execSQL("drop table IF EXISTS " + "searchlivehistory");
            db.execSQL("drop table IF EXISTS " + "searchmoviehistory");
            db.execSQL("drop table IF EXISTS " + "musichository");

            db.execSQL("create table searchlivehistory(_id integer primary key autoincrement ,name)");
            db.execSQL("create table searchmoviehistory(_id integer primary key autoincrement ,name)");
            db.execSQL("create table applyrecord(_id integer primary key autoincrement ,name)");
            db.execSQL("create table musichository(_id integer primary key autoincrement ,id,title,singer)");
            db.execSQL("create table playhository(_id integer primary key autoincrement ,icon,time,playid,title,kind)");
        }

    }
}
