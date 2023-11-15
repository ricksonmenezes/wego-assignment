package com.wego.assignment.controller.carparks.repo;

import com.wego.assignment.controller.carparks.model.CarParkAvailability;
import com.wego.assignment.controller.carparks.model.CarParkAvailabilityID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarParkAvailabilityRepo extends JpaRepository<CarParkAvailability, CarParkAvailabilityID> {


}
