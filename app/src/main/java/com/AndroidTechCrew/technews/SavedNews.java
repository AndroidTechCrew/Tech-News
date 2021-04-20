package com.AndroidTechCrew.technews;


public class SavedNews{
    private String title;
    private String imgURL;
    private String description;
    private String link;

    public SavedNews(String t, String i, String d, String l){
        title = t;
        imgURL = i;
        description = d;
        link = l;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }
}
