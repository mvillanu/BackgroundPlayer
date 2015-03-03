package net.infobosccoma.backgroundplayer.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.infobosccoma.backgroundplayer.Model.ItemVideo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Maxi on 20/02/2015.
 */
public class VideoConversor {

    private ResultsSqLiteHelper helper;



    public VideoConversor() {

    }

    /**
     * Constructor amb paràmetres
     * @param helper l'ajudant de la BD de Titulars
     */
    public VideoConversor(ResultsSqLiteHelper helper) {
        this.helper = helper;
    }



    /**
     * Desa un nou titular a la taula
     * @param video l'objecte a desar
     * @return l'id del nou titular desat
     */
    public long save(ItemVideo video) {
        long index = -1;
        // s'agafa l'objecte base de dades en mode escriptura
        SQLiteDatabase db = helper.getWritableDatabase();
        // es crea un objecte de diccionari (clau,valor) per indicar els valors a afegir
        ContentValues dades = new ContentValues();

        dades.put("video_id", video.getId());
        dades.put("title", video.getTitle());
        dades.put("description", video.getDescription());
        dades.put("thumbnail_url",video.getThumbnailURL());
        dades.put("current_date",getDate());


        try {
            index = db.insertOrThrow("video_info", null, dades);
            // volem veure en el log el que passa
        }
        catch(Exception e) {
            // volem reflectir en ellog que hi ha hagut un error
            Log.e("Video ", e.getMessage());
        }
        return index;
    }


    public void save(List<ItemVideo> videos) {
        for(ItemVideo video : videos){
            // s'agafa l'objecte base de dades en mode escriptura
            SQLiteDatabase db = helper.getWritableDatabase();
            // es crea un objecte de diccionari (clau,valor) per indicar els valors a afegir
            ContentValues dades = new ContentValues();

            dades.put("video_id", video.getId());
            dades.put("title", video.getTitle());
            dades.put("description", video.getDescription());
            dades.put("thumbnail_url",video.getThumbnailURL());
            dades.put("current_date",getDate());


            try {
                db.insertOrThrow("video_info", null, dades);
                // volem veure en el log el que passa
                //Log.i("Video ", dades.toString() + " afegit");
            }
            catch(Exception e) {
                // volem reflectir en ellog que hi ha hagut un error
                Log.e("Video ", e.getMessage());
            }

            //Log.i("debug_maxi", "registre afegit a base de dades");
        }


    }



    public Cursor getAll(){
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.query(true, "video_info",
                new String[]{"video_id","title","description","thumbnail_url","current_date"},
                null, null, null, null, null, null);

    }


    public Boolean removeAll(){

        SQLiteDatabase db = helper.getReadableDatabase();
        return db.delete("video_info",null,null)>0;
    }

    public void removeAllByDay(){

        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("DELETE FROM video_info " +
                "where current_date='"+getDate()+"'");
    }

    private String getDate(){
        Date date = new Date();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date).toString();
    }

}
