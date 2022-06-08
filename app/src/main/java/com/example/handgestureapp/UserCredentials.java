package com.example.handgestureapp;

import android.graphics.Region;

import java.io.Serializable;

public class UserCredentials implements Serializable {

    private String username;
    private String password;
    private String birthDate;
    private String gender;
    private String name;
    private String surname;
    private int number_ofGestures;

    private int UP_GESTURE;
    private int DOWN_GESTURE;
    private int RIGHT_GESTURE;
    private int LEFT_GESTURE;

    private String date_ofRegister;

    public UserCredentials(String username, String password, String birthDate, String gender, String name, String surname,
                           int number_ofGestures, String date_ofRegister, int UP_GESTURE, int DOWN_GESTURE, int LEFT_GESTURE, int RIGHT_GESTURE) {
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.gender = gender;
        this.name = name;
        this.surname = surname;
        this.number_ofGestures = number_ofGestures;
        this.date_ofRegister = date_ofRegister;

        this.DOWN_GESTURE = DOWN_GESTURE;
        this.UP_GESTURE = UP_GESTURE;
        this.LEFT_GESTURE = LEFT_GESTURE;
        this.RIGHT_GESTURE = RIGHT_GESTURE;


    }


    //region Getters and setters

    public String getDate_ofRegister() {
        return date_ofRegister;
    }

    public void setDate_ofRegister(String date_ofRegister) {
        this.date_ofRegister = date_ofRegister;
    }

    public int getNumber_ofGestures() {
        return number_ofGestures;
    }

    public void setNumber_ofGestures(int number_ofGestures) {
        this.number_ofGestures = number_ofGestures;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getUP_GESTURE() {
        return UP_GESTURE;
    }

    public void setUP_GESTURE(int UP_GESTURE) {
        this.UP_GESTURE = UP_GESTURE;
    }

    public int getDOWN_GESTURE() {
        return DOWN_GESTURE;
    }

    public void setDOWN_GESTURE(int DOWN_GESTURE) {
        this.DOWN_GESTURE = DOWN_GESTURE;
    }

    public int getRIGHT_GESTURE() {
        return RIGHT_GESTURE;
    }

    public void setRIGHT_GESTURE(int RIGHT_GESTURE) {
        this.RIGHT_GESTURE = RIGHT_GESTURE;
    }

    public int getLEFT_GESTURE() {
        return LEFT_GESTURE;
    }

    public void setLEFT_GESTURE(int LEFT_GESTURE) {
        this.LEFT_GESTURE = LEFT_GESTURE;
    }
    //endregion
}
