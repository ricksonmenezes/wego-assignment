package com.wego.assignment.controller.carparks.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wego.assignment.controller.carparks.view.carparkavailability.CarParkAvailabilityView;

import java.util.List;

public class CarParkAvailabilityResponse {

    @JsonProperty("items")
    List<CarParkAvailabilityView> items;

    public List<CarParkAvailabilityView> getItems() {
        return items;
    }
}
