package com.example.cs411_final_project.entity;

public class Planes {
    private String planeName; // Primary key
    private String IATA;
    private String ICAO;

    public Planes() {
    }

    public Planes(String planeName, String IATA, String ICAO) {
        this.planeName = planeName;
        this.IATA = IATA;
        this.ICAO = ICAO;
    }

    public String getPlaneName() {
        return planeName;
    }

    public void setPlaneName(String planeName) {
        this.planeName = planeName;
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
}
