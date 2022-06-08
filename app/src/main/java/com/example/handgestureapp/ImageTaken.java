package com.example.handgestureapp;

import android.graphics.Bitmap;

import java.io.Serializable;

public class ImageTaken implements Serializable {

    private UserCredentials myCredentials;
    private Bitmap currentImage;


    public ImageTaken(UserCredentials myCredentials, Bitmap currentImage) {
        this.myCredentials = myCredentials;
        this.currentImage = currentImage;
    }

    public Bitmap getCurrentImage() {
        return currentImage;
    }

    public void setCurrentImage(Bitmap currentImage) {
        this.currentImage = currentImage;
    }

    public UserCredentials getMyCredentials() {
        return myCredentials;
    }

    public String getUsername(){
        return myCredentials.getUsername();
    }

    public void setMyCredentials(UserCredentials myCredentials) {
        this.myCredentials = myCredentials;
    }
}
