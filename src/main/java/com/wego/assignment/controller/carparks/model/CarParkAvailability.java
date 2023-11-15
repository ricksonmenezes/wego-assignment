package com.wego.assignment.controller.carparks.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "CarParkAvailability")
@IdClass(CarParkAvailabilityID.class)
public class CarParkAvailability {

    /*@Id
    @Column(name = "CarParkID")
    private Integer carParkID;*/

    @Id
    @NotNull
    @Column(name = "CarParkNo")
    private String carParkNo;

    @Id
    @NotNull
    @Column(name = "LotType")
    private String lotType;;

    @NotNull
    @Column(name = "TotalLots")
    private Integer totalLots;

    @NotNull
    @Column(name = "AvailableLots")
    private Integer availableLots;

    /*@Column(name = "Latitude")
    private Double latitude;

    @Column(name = "Longitude")
    private Double longititude;*/

    @Column(name = "createdDate")
    private Date createdDate;

    @Column(name = "modifiedDate")
    private Date modifiedDate;

    @Version
    private Integer version;

    public String getCarParkNo() {
        return carParkNo;
    }

    public void setCarParkNo(String carParkNo) {
        this.carParkNo = carParkNo;
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

   /* public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongititude() {
        return longititude;
    }

    public void setLongititude(Double longititude) {
        this.longititude = longititude;
    }*/

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }


    public String getLotType() {
        return lotType;
    }

    public void setLotType(String lotType) {
        this.lotType = lotType;
    }
}
