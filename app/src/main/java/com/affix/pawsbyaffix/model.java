package com.affix.pawsbyaffix;

class model {

    String Datetime,Username,caption,image,ProfileImage,userid,postiddate, fullname;
    int PostNumber,LikeCount,comment;


    public model() {

    }

    public model(String datetime, String fullname, String username, String caption, String image, String profileimage, String postiddate, String userid, int postNumber, int likes, int comment) {
        Datetime = datetime;
        Username = username;
        this.caption = caption;
        this.image = image;
        this.ProfileImage = profileimage;
        PostNumber = postNumber;
        LikeCount = likes;
        this.comment = comment;
        this.userid = userid;
        this.postiddate = postiddate;
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public int getLikeCount() {
        return LikeCount;
    }

    public void setLikeCount(int likeCount) {
        LikeCount = likeCount;
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

    public String getProfileimage() {
        return ProfileImage;
    }

    public void setProfileimage(String profileimage) {
        this.ProfileImage = profileimage;
    }

    public int getPostNumber() {
        return PostNumber;
    }

    public void setPostNumber(int postNumber) {
        PostNumber = postNumber;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public int getComment() {
        return comment;
    }

    public void setComment(int comment) {
        this.comment = comment;
    }
}


