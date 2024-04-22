package com.example.cs411_final_project.entity;

public class Subscription{
    private int userID; // PK
    private String flightNumber; // PK

    public Subscription(){
    }

    public Subscription(int userID, String flightNumber){
        this.userID = userID;
        this.flightNumber = flightNumber;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID() {
        this.userID = userID;
    }

    public String getFlightNumber(){
        return flightNumber;
    }
    
    public void setFlightNumber(){
        this.flightNumber = flightNumber;
    }
}


