package net.infobosccoma.backgroundplayer.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Maxi on 20/02/2015.
 */
public class RecomendSQLiteHelper extends SQLiteOpenHelper {

    private final String CREATE_TABLE = "CREATE TABLE recomend(" +
            "	recomend_id INTEGER PRIMARY KEY, " +
            "	name TEXT) ";


    public RecomendSQLiteHelper(Context context, String nom, SQLiteDatabase.CursorFactory factory, int versio){
        super(context, nom, factory, versio);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS video_info");
        db.execSQL(CREATE_TABLE);
    }


}
