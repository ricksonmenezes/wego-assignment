package com.wego.assignment.controller.carparks.model;

import java.io.Serializable;

public class CarParkAvailabilityID implements Serializable {

    private String carParkNo;
    private String lotType;;

    public String getCarParkNo() {
        return carParkNo;
    }

    public void setCarParkNo(String carParkNo) {
        this.carParkNo = carParkNo;
    }

    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }
}
