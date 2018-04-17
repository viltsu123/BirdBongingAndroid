package com.example.viltsu.birdbonging;

/**
 * Created by ville-pekkapalmgren on 05/03/18.
 */

public class Bird {
    private String birdType;
    private String message;
    private int imgResId;
    private int dbId;
    private String Birdmessage;

    public Bird(String birdType, String message, int imgResId, int birdId, String bmessage) {
        this.birdType = birdType;
        this.message = message;
        this.imgResId = imgResId;
        this.dbId = birdId;
        this.Birdmessage = bmessage;
    }

    public String getBirdmessage() {
        return Birdmessage;
    }

    public void setBirdmessage(String birdmessage) {
        Birdmessage = birdmessage;
    }

    public int getDbId(){return dbId;}

    public void setDbId(int birdID){
        this.dbId = birdID;
    }

    public String getBirdType() {
        return birdType;
    }

    public void setBirdType(String birdType) {
        this.birdType = birdType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImgResId() {
        return imgResId;
    }

    public void setImgResId(int imgResId) {
        this.imgResId = imgResId;
    }

    @Override
    public String toString() {
        return "Bird{" +
                "birdType='" + birdType + '\'' +
                ", message='" + message + '\'' +
                ", imgResId=" + imgResId +
                ", dbId=" + dbId +
                ", Birdmessage='" + Birdmessage + '\'' +
                '}';
    }
}
