package com.AndroidTechCrew.technews.fragments;

public class Comment {
    private String profilePic;
    private String username;
    private String comment;
    public Comment(String profilePic, String username, String comment){
        this.profilePic = profilePic;
        this.username = username;
        this.comment = comment;
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
}
