package com.wego.assignment.controller.carparks.view;

public class LotData {

    private String lotType;
    private Integer availableLots;
    private Integer totalLots;

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }

    public Integer getAvailableLots() {
        return availableLots;
    }

    public void setAvailableLots(Integer availableLots) {
        this.availableLots = availableLots;
    }

    public Integer getTotalLots() {
        return totalLots;
    }

    public void setTotalLots(Integer totalLots) {
        this.totalLots = totalLots;
    }

    @Override
    public String toString() {
        return "LotData{" +
                "lotType='" + lotType + '\'' +
                ", availableLots=" + availableLots +
                ", totalLots=" + totalLots +
                '}';
    }
}
