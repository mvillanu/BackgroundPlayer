package net.infobosccoma.backgroundplayer.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maxi on 20/02/2015.
 */
public class ResultsSqLiteHelper extends SQLiteOpenHelper {

    private final String SQL_CREATE_TABLE_VIDEO = "CREATE TABLE video_info(" +
            "	video_id TEXT PRIMARY KEY, " +
            "	title TEXT, " +
            "	description TEXT," +
            "thumbnail_url TEXT," +
            "current_date TEXT)";



    public ResultsSqLiteHelper(Context context, String nom, SQLiteDatabase.CursorFactory factory, int versio){
        super(context, nom, factory, versio);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_VIDEO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS video_info");
        db.execSQL(SQL_CREATE_TABLE_VIDEO);
    }




}
