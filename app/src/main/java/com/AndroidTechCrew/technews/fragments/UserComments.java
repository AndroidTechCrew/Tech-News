package com.AndroidTechCrew.technews.fragments;

public class UserComments {
    private String profilePic;
    private String username;
    private String comment;
    private String articleLink;
    private String image;
    private String description;
    private String articleTitle;
    private String uid;



    public UserComments(String profilePic, String username, String comment, String uid, String articleLink, String image, String description, String articleTitle){
        this.profilePic = profilePic;
        this.username = username;
        this.comment = comment;
        this.uid = uid;
        this.articleLink = articleLink;
        this.image = image;
        this.description = description;
        this.articleTitle = articleTitle;
    }
    public String getCommentArticleLink() {
        return articleLink;
    }

    public void setCommentArticleLink(String articleLink) {
        this.articleLink = articleLink;
    }

    public String getArticleLink() {
        return articleLink;
    }

    public String getImage() {
        return image;
    }

    public String getDescription() {
        return description;
    }

    public String getArticleTitle() {
        return articleTitle;
    }

    public String getUid() {
        return uid;
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

