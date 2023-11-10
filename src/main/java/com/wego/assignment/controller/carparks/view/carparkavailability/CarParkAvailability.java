package com.wego.assignment.controller.carparks.view.carparkavailability;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class CarParkAvailability {

    @JsonProperty("timestamp")
    String timeStamp;

    @JsonProperty("carpark_data")
    List<CarParkData> carParkData;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<CarParkData> getCarParkData() {
        return carParkData;
    }

    public void setCarParkData(List<CarParkData> carParkData) {
        this.carParkData = carParkData;
    }

}
