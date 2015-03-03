package net.infobosccoma.backgroundplayer.Model;

/**
 * Created by Maxi on 10/02/2015.
 */
public class ItemVideo {

    private String title;
    private String description;
    private String thumbnailURL;
    private String id;
    private String date;

    public ItemVideo(){}

    public ItemVideo(String title, String description, String thumbnailURL, String id){
        this.title=title;
        this.description=description;
        this.thumbnailURL=thumbnailURL;
        this.id=id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }


    public void setDate(String text){this.date=text;}

    public String getDate(){
        return this.date;
    }

}
