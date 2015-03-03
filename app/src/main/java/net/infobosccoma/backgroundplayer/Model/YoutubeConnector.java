package net.infobosccoma.backgroundplayer.Model;

import android.app.Activity;
import android.content.Context;

import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import net.infobosccoma.backgroundplayer.PopUp;
import net.infobosccoma.backgroundplayer.R;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Maxi on 10/02/2015.
 */
public class YoutubeConnector {
    private YouTube youtube;
    private YouTube.Search.List query;
    private Activity parent;

    // Clau de developer de youtube
    public static final String KEY
            = "AIzaSyApO-0K77lkSI0oHvXlMOpFpq4lwk3aQ0Y";

    /**
     * es crea la plantilla per fer les querys a youtube
     * @param context
     */
    public YoutubeConnector(Context context) {
        youtube = new YouTube.Builder(new NetHttpTransport(),
                new JacksonFactory(), new HttpRequestInitializer() {
            @Override
            public void initialize(com.google.api.client.http.HttpRequest request) throws IOException {

            }
        }).setApplicationName(context.getString(R.string.app_name)).build();

        try{
            /**
             * Query especificant els camps que ens ha de retornar el servidor
             * En aquest cas només ens interessen els videos.
             */
            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/videoId,snippet/title,snippet/description,snippet/thumbnails/default/url)");
        }catch(IOException e){
            PopUp.CustomDialogAlert("Youtube Error", e.getMessage(),parent);
        }
    }


    /**
     * fa la cerca a youtube i en retorna una llista amb els resultats
     * @param keywords
     * @return
     */
    public List<ItemVideo> search(String keywords){
        query.setQ(keywords);
        query.setMaxResults((long)50);
        try{
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();

            List<ItemVideo> items = new ArrayList<ItemVideo>();
            for(SearchResult result:results){
                ItemVideo item = new ItemVideo();
                item.setTitle(result.getSnippet().getTitle());
                item.setDescription(result.getSnippet().getDescription());
                item.setThumbnailURL(result.getSnippet().getThumbnails().getDefault().getUrl());
                item.setId(result.getId().getVideoId());

                item.setDate(getDate());
                items.add(item);
            }
            return items;
        }catch(IOException e){
            System.err.println(e.getMessage());
            return null;
        }
    }

    private String getDate(){
        Date date = new Date();

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

}
