package com.affix.pawsbyaffix;

class VerticelUprofileModel {

    String Datetime,Username,caption,image,userid,postiddate,fullname;
    int PostNumber,Likes;

    public VerticelUprofileModel() {
    }

    public VerticelUprofileModel(String datetime, String userid, String fullname, String postiddate, int postNumber, String username, String caption, String image, int likeCount) {
        Datetime = datetime;

        PostNumber = postNumber;
        Username = username;
        this.caption = caption;
        this.image = image;
        this.Likes = likeCount;
        this.postiddate = postiddate;
        this.userid = userid;
        this.fullname = fullname;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPostiddate() {
        return postiddate;
    }

    public void setPostiddate(String postiddate) {
        this.postiddate = postiddate;
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
        return Likes;
    }

    public void setLikes(int likeCount) {
        this.Likes = likeCount;
    }
}
