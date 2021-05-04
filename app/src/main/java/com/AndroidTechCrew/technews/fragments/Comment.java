package com.AndroidTechCrew.technews.fragments;

import android.net.Uri;

public class Comment {
    private String profilePic;
    private String username;
    private String comment;
    private String articleLink;



    public Comment(String profilePic, String username, String comment){
        this.profilePic = profilePic;
        this.username = username;
        this.comment = comment;
    }
    public String getCommentArticleLink() {
        return articleLink;
    }

    public void setCommentArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }


}