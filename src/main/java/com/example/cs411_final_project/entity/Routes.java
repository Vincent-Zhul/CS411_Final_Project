package com.example.cs411_final_project.entity;

public class Routes {
    private String flightNumber;
    private int AirlineId;
    private int departureAirportID;
    private int arrivalAirportID;
    private int stops;

    // 构造器
    public Routes() {
    }

    public Routes(String flightNumber, int AirlineId, int departureAirportID, int arrivalAirportID, int stops) {
        this.flightNumber = flightNumber;
        this.AirlineId = AirlineId;
        this.arrivalAirportID = arrivalAirportID;
        this.departureAirportID = departureAirportID;
        this.stops = stops;

    }

    // Getter 和 Setter 方法
    public String getflightNumber() {
        return flightNumber;
    }

    public void setflightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getAirlineId() {
        return AirlineId;
    }

    public void setAirlineId(int AirlineId) {
        this.AirlineId = AirlineId;
    }

    public int getdepartureAirportID() {
        return departureAirportID;
    }

    public void setdepartureAirportID(int departureAirportID) {
        this.departureAirportID = departureAirportID;
    }

    public int getarrivalAirportID() {
        return arrivalAirportID;
    }

    public void setarrivalAirportID(int arrivalAirportID) {
        this.arrivalAirportID = arrivalAirportID;
    }

    public int getstops() {
        return stops;
    }

    public void setstops(int stops) {
        this.stops = stops;
    }

}
