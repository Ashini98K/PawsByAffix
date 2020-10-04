package com.affix.pawsbyaffix;

class SearchUserModel {

    private String Uid,username,fullname,ProfileImage,email,BackgroundImage,Bio;

    public SearchUserModel() {
    }

    public SearchUserModel(String Uid, String username, String fullname, String ProfileImage, String email, String BackgroundImage, String Bio) {
        this.Uid = Uid;
        this.username = username;
        this.fullname = fullname;
        this.ProfileImage = ProfileImage;
        this.email = email;
        this.BackgroundImage = BackgroundImage;
        this.Bio = Bio;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBackgroundImage() {
        return BackgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        BackgroundImage = backgroundImage;
    }
}
