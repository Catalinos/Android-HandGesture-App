package com.example.handgestureapp;

import android.graphics.Bitmap;

public class GestureData {
    String gestureName;
    Bitmap gestureImage;


    public GestureData(String gestureName, Bitmap gestureImage) {
        this.gestureName = gestureName;
        this.gestureImage = gestureImage;
    }



    public Bitmap getGestureImage() {
        return gestureImage;
    }
    public void setGestureImage(Bitmap gestureImage) {
        this.gestureImage = gestureImage;
    }


    public String getGestureName() {
        return gestureName;
    }
    public void setGestureName(String gestureName) {
        this.gestureName = gestureName;
    }


}
