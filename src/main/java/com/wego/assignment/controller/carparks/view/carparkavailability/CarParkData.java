package com.wego.assignment.controller.carparks.view.carparkavailability;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

public class CarParkData {


    @JsonProperty("carpark_number")
    private String carparkNumber;

    @JsonProperty("update_datetime")
    private String updateDateTime;

    @JsonProperty("carpark_info")
    List<CarParkInfo> carparkInfo;

    public String getCarparkNumber() {
        return carparkNumber;
    }

    public void setCarparkNumber(String carparkNumber) {
        this.carparkNumber = carparkNumber;
    }

    public String getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(String updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public List<CarParkInfo> getCarparkInfo() {
        return carparkInfo;
    }

    public void setCarparkInfo(List<CarParkInfo> carparkInfo) {
        this.carparkInfo = carparkInfo;
    }


}
