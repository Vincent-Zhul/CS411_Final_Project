package com.example.cs411_final_project.entity;

public class Airlines {
    private int airlineID;  // Primary key
    private String airlineName;
    private String alias;
    private String IATA;
    private String ICAO;
    private String callsign;
    private String country;
    private char active;

    public Airlines() {
    }

    public Airlines(int airlineID, String airlineName, String alias, String IATA, String ICAO, String callsign, String country, char active) {
        this.airlineID = airlineID;
        this.airlineName = airlineName;
        this.alias = alias;
        this.IATA = IATA;
        this.ICAO = ICAO;
        this.callsign = callsign;
        this.country = country;
        this.active = active;
    }

    public int getAirlineID() {
        return airlineID;
    }

    public void setAirlineID(int airlineID) {
        this.airlineID = airlineID;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getIATA() {
        return IATA;
    }

    public void setIATA(String IATA) {
        this.IATA = IATA;
    }

    public String getICAO() {
        return ICAO;
    }

    public void setICAO(String ICAO) {
        this.ICAO = ICAO;
    }

    public String getCallsign() {
        return callsign;
    }

    public void setCallsign(String callsign) {
        this.callsign = callsign;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public char isActive() {
        return active;
    }

    public void setActive(char active) {
        this.active = active;
    }
}
