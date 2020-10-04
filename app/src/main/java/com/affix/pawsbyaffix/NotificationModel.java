package com.affix.pawsbyaffix;

class NotificationModel {

    private String notificationTime, NotificationWitnessId, NotificationWitnessName, NotificationTargetImage,NotificationWitnessProfileImage, NewNotificationMessage;
    private int NotificationId;

    public NotificationModel() {
    }

    public NotificationModel(String notificationTime, String notificationWitnessId, String notificationWitnessName, String notificationTargetImage, String notificationWitnessProfileImage, String newNotificationMessage, int NotificationId) {
        this.notificationTime = notificationTime;
        NotificationWitnessId = notificationWitnessId;
        NotificationWitnessName = notificationWitnessName;
        NotificationTargetImage = notificationTargetImage;
        NotificationWitnessProfileImage = notificationWitnessProfileImage;
        NewNotificationMessage = newNotificationMessage;
        this.NotificationId = NotificationId;
    }

    public int getNotificationId() {
        return NotificationId;
    }

    public void setNotificationId(int notificationId) {
        NotificationId = notificationId;
    }

    public String getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(String notificationTime) {
        this.notificationTime = notificationTime;
    }

    public String getNotificationWitnessId() {
        return NotificationWitnessId;
    }

    public void setNotificationWitnessId(String notificationWitnessId) {
        NotificationWitnessId = notificationWitnessId;
    }

    public String getNotificationWitnessName() {
        return NotificationWitnessName;
    }

    public void setNotificationWitnessName(String notificationWitnessName) {
        NotificationWitnessName = notificationWitnessName;
    }

    public String getNotificationTargetImage() {
        return NotificationTargetImage;
    }

    public void setNotificationTargetImage(String notificationTargetImage) {
        NotificationTargetImage = notificationTargetImage;
    }

    public String getNotificationWitnessProfileImage() {
        return NotificationWitnessProfileImage;
    }

    public void setNotificationWitnessProfileImage(String notificationWitnessProfileImage) {
        NotificationWitnessProfileImage = notificationWitnessProfileImage;
    }

    public String getNewNotificationMessage() {
        return NewNotificationMessage;
    }

    public void setNewNotificationMessage(String newNotificationMessage) {
        NewNotificationMessage = newNotificationMessage;
    }
}
