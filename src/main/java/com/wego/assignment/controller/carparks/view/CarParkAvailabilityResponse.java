package com.wego.assignment.controller.carparks.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wego.assignment.controller.carparks.view.carparkavailability.CarParkAvailability;
import com.wego.assignment.controller.carparks.view.carparkavailability.CarParkData;

import java.util.List;

public class CarParkAvailabilityResponse {

    @JsonProperty("items")
    List<CarParkAvailability> items;
}
