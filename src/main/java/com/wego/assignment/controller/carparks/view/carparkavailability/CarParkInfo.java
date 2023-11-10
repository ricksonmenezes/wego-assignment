package com.wego.assignment.controller.carparks.view.carparkavailability;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CarParkInfo {

    @JsonProperty("total_lots")
    private Integer totalLots;
    @JsonProperty("lot_type")
    private String lotType;
    @JsonProperty("lots_available")
    private Integer lotsAvailable;

    public Integer getTotalLots() {
        return totalLots;
    }

    public void setTotalLots(Integer totalLots) {
        this.totalLots = totalLots;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public Integer getLotsAvailable() {
        return lotsAvailable;
    }

    public void setLotsAvailable(Integer lotsAvailable) {
        this.lotsAvailable = lotsAvailable;
    }
}
