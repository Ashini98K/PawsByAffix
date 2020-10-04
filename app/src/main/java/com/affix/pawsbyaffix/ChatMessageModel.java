package com.affix.pawsbyaffix;

public class ChatMessageModel {
    String MessageValue,SenderId,SentTime;
    int MsgNo;
    public ChatMessageModel() {
    }

    public ChatMessageModel(String MessageValue, int MsgNo, String SenderId, String SentTime) {
        this.MessageValue = MessageValue;
        this.MsgNo = MsgNo;
        this.SenderId = SenderId;
        this.SentTime = SentTime;
    }

    public String getMessageValue() {
        return MessageValue;
    }

    public void setMessageValue(String messageValue) {
        MessageValue = messageValue;
    }

    public int getMsgNo() {
        return MsgNo;
    }

    public void setMsgNo(int msgNo) {
        MsgNo = msgNo;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }

    public String getSentTime() {
        return SentTime;
    }

    public void setSentTime(String sentTime) {
        SentTime = sentTime;
    }
}
