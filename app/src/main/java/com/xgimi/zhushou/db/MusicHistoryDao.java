package com.xgimi.zhushou.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XGIMI on 2017/7/5.
 */

public class MusicHistoryDao extends SQLiteOpenHelper {

    private List<String> mMusicSearchRecordList;

    private static final String TAG = "MusicHistoryDao";
    private static final int VERSION = 1;
    private static final String CREATE_SQL = "create table if not exists MusicHistoryTab(_id integer primary key autoincrement, search_content text)";
    private static final String DB_NAME = "MusicHistory.db";
    private static final String TABLE_NAME = "MusicHistoryTab";

    public MusicHistoryDao(Context context) {
        super(context, DB_NAME, null, VERSION);

    }

    public MusicHistoryDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MusicHistoryDao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }

    public List<String> getMusicSearchRecordList() {
        if (mMusicSearchRecordList == null) {
            mMusicSearchRecordList = new ArrayList<>();
            SQLiteDatabase db = this.getReadableDatabase();
            // db.query(TABLE_NAME, new String[]{"last_position"}, "video_id=?", new String[]{video.getVideoID()}, null, null, null);
            Cursor cursor = db.query(TABLE_NAME, new String[]{"search_content"}, "_id>?", new String[]{"0"}, null, null, null);
            while (cursor.moveToNext()) {
                Log.d(TAG, "getMusicSearchRecordList: " + cursor.getString(cursor.getColumnIndex("search_content")));
                mMusicSearchRecordList.add(cursor.getString(cursor.getColumnIndex("search_content")));
            }
        }
        Log.d(TAG, "getMusicSearchRecordList: size = " + mMusicSearchRecordList.size());
        return mMusicSearchRecordList;
    }

    public void insertMusicSearchHistory(String history) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{"_id"}, "search_content=?", new String[]{history}, null, null, null);
        if (cursor.getCount() == 0) {
            Log.d(TAG, "insertMusicSearchHistory: new record");
            ContentValues cv = new ContentValues();
            cv.put("search_content", history);
            db.insert(TABLE_NAME, null, cv);
//            mMusicSearchRecordList.add(history);
        } else {
            Log.d(TAG, "insertMusicSearchHistory: already exist");
        }
    }

    public void deleteMusicSearchRecod(String content) {
        Log.d(TAG, "deleteMusicSearchRecod: ");
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, "search_content=?", new String[]{content});
    }

    public void cleanAllRecord() {
        mMusicSearchRecordList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, "_id>?", new String[]{"0"});
    }
}
