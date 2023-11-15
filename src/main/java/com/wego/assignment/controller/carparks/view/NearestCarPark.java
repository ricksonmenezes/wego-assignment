package com.wego.assignment.controller.carparks.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NearestCarPark {


    private String address;
    private Double latitude;
    private Double longitude;
    @JsonProperty("total_lots")
    private Integer totalLots;
    @JsonProperty("available_lots")
    private Integer availableLots;

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    @JsonIgnore
    private Double distance;

    public NearestCarPark(String address, double latitude, double longitude, int totalLots, int availableLots, Double distance) {

        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totalLots = totalLots;
        this.availableLots = availableLots;
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getTotalLots() {
        return totalLots;
    }

    public void setTotalLots(Integer totalLots) {
        this.totalLots = totalLots;
    }

    public Integer getAvailableLots() {
        return availableLots;
    }

    public void setAvailableLots(Integer availableLots) {
        this.availableLots = availableLots;
    }

}
