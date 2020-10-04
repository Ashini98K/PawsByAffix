package com.affix.pawsbyaffix;

class UserProfileModel {

    String Datetime,Username,caption,image;
    int PostNumber;
    int LikesCount;
    public UserProfileModel() {
    }

    public UserProfileModel(String datetime, int postNumber, String username, String caption, String image, int LikesCount) {
        Datetime = datetime;

        PostNumber = postNumber;
        Username = username;
        this.caption = caption;
        this.image = image;
        this.LikesCount = LikesCount;
    }

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String datetime) {
        Datetime = datetime;
    }

    public int getPostNumber() {
        return PostNumber;
    }

    public void setPostNumber(int postNumber) {
        PostNumber = postNumber;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getLikes() {
        return LikesCount;
    }

    public void setLikes(int Likes) {
        this.LikesCount = Likes;
    }
}
