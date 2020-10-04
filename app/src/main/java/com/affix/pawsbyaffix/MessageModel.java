package com.affix.pawsbyaffix;

public class MessageModel {

    String ChatNo,ProfileImage,Uid,search,username,LastMessage,Participant2,Time;

    public MessageModel() {
    }

    public MessageModel(String chatNo, String profileImage, String uid, String LastMessage, String search, String username, String Participant2, String Time) {
        ChatNo = chatNo;
        ProfileImage = profileImage;
        Uid = uid;
        this.LastMessage = LastMessage;
        this.search = search;
        this.username = username;
        this.Participant2 = Participant2;
        this.Time = Time;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getLastMessage() {
        return LastMessage;
    }

    public void setLastMessage(String lastMessage) {
        LastMessage = lastMessage;
    }

    public String getParticipant2() {
        return Participant2;
    }

    public void setParticipant2(String participant2) {
        Participant2 = participant2;
    }

    public String getChatNo() {
        return ChatNo;
    }

    public void setChatNo(String chatNo) {
        ChatNo = chatNo;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getEmail() {
        return LastMessage;
    }

    public void setEmail(String email) {
        this.LastMessage = email;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
