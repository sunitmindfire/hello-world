package com.nikhil.spycam.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import com.nikhil.spycam.database.SpyCamDBContract.*;

/**
 * DB Helper Class for Database operations in App
 * <p>
 * Created by sunit deswal on 7/10/17.
 */

public class SpyCamDBHelper extends SQLiteOpenHelper {
    //database info and version
    private static final String DATABASE_NAME = "SPYCAM_DB";
    private static final int DB_VERSION = 1;
    //insert table query
    private static final String CREATE_TABLE_QUERY = "CREATE TABLE " + AlarmEntry.TABLE_NAME + "("
            + AlarmEntry._ID + " INTEGER PRIMARY KEY,"
            + AlarmEntry.COLUMN_NAME_DATE + " TEXT,"
            + AlarmEntry.COLUMN_NAME_TIME + " TEXT,"
            + AlarmEntry.COLUMN_NAME_DURATION + " INTEGER,"
            + AlarmEntry.COLUMN_NAME_UNIQUE_ID + " INTEGER UNIQUE)";

    public SpyCamDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public static long addAlarm(Context context,String date,String time,int duration,long uniqueID){
        long rowId =-1;
        ContentValues contentValues = new ContentValues();
        contentValues.put(AlarmEntry.COLUMN_NAME_DATE,date);
        contentValues.put(AlarmEntry.COLUMN_NAME_TIME,time);
        contentValues.put(AlarmEntry.COLUMN_NAME_DURATION,duration);
        contentValues.put(AlarmEntry.COLUMN_NAME_UNIQUE_ID,uniqueID);
        try {
            SQLiteDatabase db = new SpyCamDBHelper(context).getWritableDatabase();
            rowId = db.insertOrThrow(AlarmEntry.TABLE_NAME, null, contentValues);
        }catch (SQLiteException e){
            e.printStackTrace();
        }
        return rowId;
    }

    public static int deleteAlarm(Context context,long id) {

        SQLiteDatabase db = new SpyCamDBHelper(context).getWritableDatabase();
        int outRowDeleted = 0;
        try {
            outRowDeleted = db.delete(AlarmEntry.TABLE_NAME, AlarmEntry.COLUMN_NAME_UNIQUE_ID + "=?",
                    new String[]{String.valueOf(id)});
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        db.close();
        return outRowDeleted;

    }

    public static Cursor getAlarmList(Context context){
        Cursor cursor = null;
        String [] projection = {
                AlarmEntry._ID,
                AlarmEntry.COLUMN_NAME_DATE,
                AlarmEntry.COLUMN_NAME_TIME,
                AlarmEntry.COLUMN_NAME_DURATION,
                AlarmEntry.COLUMN_NAME_UNIQUE_ID};

        SQLiteDatabase db = new SpyCamDBHelper(context).getReadableDatabase();
        cursor = db.query(AlarmEntry.TABLE_NAME,projection,null,null,null,null,null);
        return cursor;
    }
}
