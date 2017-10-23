package com.xgimi.zhushou.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xgimi.zhushou.bean.MusicInfor;
import com.xgimi.zhushou.bean.PlayHostory;
import com.xgimi.zhushou.bean.Record;
import com.xgimi.zhushou.bean.SearchLiveRecord;
import com.xgimi.zhushou.bean.YiJianBeen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/6/20.
 */
public class RecordDao {
    public DbHelper dbHelper;
    private SQLiteDatabase db;

    public RecordDao(Context context) {
        dbHelper = new DbHelper(context);
    }

    //查询直播历史记录
    public List<SearchLiveRecord> getRecord() {
        ArrayList<SearchLiveRecord> records = new ArrayList<SearchLiveRecord>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from searchlivehistory", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            SearchLiveRecord record = new SearchLiveRecord(name);
            records.add(record);
        }
        cursor.close();
        db.close();
        return records;
    }

    //加入历史记录
    public void addRecord(String record) {
        db = dbHelper.getReadableDatabase();
        db.execSQL(
                "insert into searchlivehistory(name) values (?)",
                new Object[]{record});
        db.close();
    }

    //删除原来已经存在的
    public void deleteAlread(String name) {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from searchlivehistory where name ='"
                + name + "'");
        db.close();
    }

    //删除原来的
    public void autoDeleteRecord(int a) {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from searchlivehistory where name ="
                + a);
        db.close();
    }

    //删除所有记录
    public void deleteRecord() {
        db = dbHelper.getWritableDatabase();
        db.execSQL("delete from searchlivehistory");
        db.close();
    }


    //影视的搜索记录
    public List<SearchLiveRecord> getRecord1() {
        ArrayList<SearchLiveRecord> records = new ArrayList<SearchLiveRecord>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from searchmoviehistory", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            SearchLiveRecord record = new SearchLiveRecord(name);
            records.add(record);
        }
        cursor.close();
        db.close();
        return records;
    }

    //加入历史记录
    public void addRecord1(String record) {
        db = dbHelper.getReadableDatabase();
        db.execSQL(
                "insert into searchmoviehistory(name) values (?)",
                new Object[]{record});
        db.close();
    }


    //删除原来已经存在的
    public void deleteAlread1(String name) {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from searchmoviehistory where name ='"
                + name + "'");
        db.close();
    }

    //删除原来的
    public void autoDeleteRecord1(int a) {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from searchmoviehistory where name ="
                + a);
        db.close();
    }

    //删除所有记录
    public void deleteRecord1() {
        db = dbHelper.getWritableDatabase();
        db.execSQL("delete from searchmoviehistory");
        db.close();
    }

    //应用相关的
    //应用相关的app  查询app相关的
    public List<Record> getApplyRecord() {
        ArrayList<Record> records = new ArrayList<Record>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from applyrecord", null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            Record record = new Record(name);
            records.add(record);
        }
        cursor.close();
        db.close();
        return records;
    }

    //加入搜藏记录
    public void addApplyRecord(String record) {
        db = dbHelper.getReadableDatabase();
        db.execSQL(
                "insert into applyrecord(name) values (?)",
                new Object[]{record});
        db.close();
    }

    //删除原来已经存在的
    public void deleteapplyAlread(String name) {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from applyrecord where name ='"
                + name + "'");
        db.close();
    }

    //删除所有记录
    public void deleteApplyRecord() {
        db = dbHelper.getWritableDatabase();
        db.execSQL("delete from applyrecord");
        db.close();
    }

    //删除原来名字已经存在的
    public void deletName(String name) {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from applyrecord where name ='"
                + name + "'");
        db.close();
    }

    //影视播放记录
    public void addPlay(PlayHostory play) {
        db = dbHelper.getReadableDatabase();
        db.execSQL(
                "insert into playhository(icon,time,playid,title,kind) values (?,?,?,?,?)",
                new Object[]{play.icon, play.time, play.id, play.title, play.kind});
        db.close();
    }

    //查询播放记录
    public List<PlayHostory> ChaXunPlay() {
        ArrayList<PlayHostory> records = new ArrayList<PlayHostory>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from playhository", null);
        while (cursor.moveToNext()) {
            String icon = cursor.getString(cursor.getColumnIndex("icon"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String id = cursor.getString(cursor.getColumnIndex("playid"));
            String title = cursor.getString(cursor.getColumnIndex("title"));
            String kind = cursor.getString(cursor.getColumnIndex("kind"));
            PlayHostory record = new PlayHostory(id, icon, title, time, kind);
            records.add(record);
        }
        cursor.close();
        db.close();
        return records;
    }

    //删除原来id已经存在的
    public void deletMuscicHostory(MusicInfor.currentInfor currentinfo) {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from musichository where id ='"
                + currentinfo.id + "'");
        db.close();
    }

    //删除原来id已经存在的
    public void deletMusci() {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from musichository");
        db.close();
    }

    //删除原来名字已经存在的
    public void deletPlayHostory(PlayHostory play) {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from playhository where playid ='"
                + play.id + "'");
        db.close();
    }

    public void deletPlayHostory() {
        db = dbHelper.getReadableDatabase();
        db.execSQL("delete from playhository");
        db.close();
    }

    /**
     * 保存意见反馈的所有数据
     */
    //查询历史记录
    public List<YiJianBeen> getYiJianRecord() {
        ArrayList<YiJianBeen> records = new ArrayList<YiJianBeen>();
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from yijian", null);
        while (cursor.moveToNext()) {
            String data = cursor.getString(cursor.getColumnIndex("data"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            YiJianBeen record = new YiJianBeen(data, type);
            records.add(record);
        }
        cursor.close();
        db.close();
        return records;
    }

    //加入历史记录
    public void addYiJianRecord(String record, String type) {
        db = dbHelper.getReadableDatabase();
        db.execSQL(
                "insert into yijian(data,type) values (?,?)",
                new Object[]{record, type});
        db.close();
    }
}