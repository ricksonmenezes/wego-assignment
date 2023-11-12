package com.wego.assignment.controller.carparks.repo;

import com.wego.assignment.controller.carparks.model.CarParkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CarParkInfoRepository extends JpaRepository<CarParkInfo, String> {

}
