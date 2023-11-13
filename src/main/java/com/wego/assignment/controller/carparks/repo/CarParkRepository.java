package com.wego.assignment.controller.carparks.repo;

import com.wego.assignment.controller.carparks.model.CarPark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarParkRepository extends JpaRepository<CarPark, String> {

}
