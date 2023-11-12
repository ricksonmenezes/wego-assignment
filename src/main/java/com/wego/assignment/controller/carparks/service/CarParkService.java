package com.wego.assignment.controller.carparks.service;

import com.wego.assignment.common.view.LatLong;
import com.wego.assignment.controller.carparks.exception.CarParkAPIException;
import com.wego.assignment.controller.carparks.exception.CarParkException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CarParkService {

    @Autowired
    CarParkInfoCSVService carParkInfoCSVService;

    @Autowired
    CarparkServiceAPI carparkServiceAPI;

    public LatLong convertSVY21ToLatLong(double x, double y) throws CarParkException {

        try {

            return carparkServiceAPI.getLatLongFromSvy21FromOneMap(x, y);

        } catch (CarParkAPIException e) {

            throw new CarParkException("Calling conversion API from one map failed " +  e.getMessage(),e);

        }catch (Exception e) {

            throw new CarParkException(e.getMessage(),e);
        }
    }

    public void saveCarParkInfoCSV()  throws CarParkException{

        try {
            carParkInfoCSVService.syncCarParkInfoFile();
        } catch (Exception e) {
            throw new CarParkException(e.getMessage(),e);

        }

    }
}
