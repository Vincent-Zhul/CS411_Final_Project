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

    // getter and setter
    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public int getAirlineId() {
        return AirlineId;
    }

    public void setAirlineId(int AirlineId) {
        this.AirlineId = AirlineId;
    }

    public int getDepartureAirportID() {
        return departureAirportID;
    }

    public void setDepartureAirportID(int departureAirportID) {
        this.departureAirportID = departureAirportID;
    }

    public int getArrivalAirportID() {
        return arrivalAirportID;
    }

    public void setArrivalAirportID(int arrivalAirportID) {
        this.arrivalAirportID = arrivalAirportID;
    }

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

}
