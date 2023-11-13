package com.wego.assignment.controller.carparks.view;

import com.wego.assignment.common.view.LatLong;

import java.util.List;

public class CarParkDataCache {

    private String carparkNo;
    //private LatLong latLong;
    private List<LotData> lotData;

    public String getCarparkNo() {
        return carparkNo;
    }

    public void setCarparkNo(String carparkNo) {
        this.carparkNo = carparkNo;
    }

    /*public LatLong getLatLong() {
        return latLong;
    }

    public void setLatLong(LatLong latLong) {
        this.latLong = latLong;
    }*/

    public List<LotData> getLotData() {
        return lotData;
    }

    public void setLotData(List<LotData> lotData) {
        this.lotData = lotData;
    }
}
