package com.wego.assignment.controller.carparks.controller;

import com.wego.assignment.common.APIError;
import com.wego.assignment.controller.carparks.view.CarParkResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="api/v1/test")
public class CarParksRestController {

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public ResponseEntity<?> testController()
    {
        ResponseEntity<?> response = null;

        try {

            CarParkResponse carParkResponseresponse = new CarParkResponse();
            carParkResponseresponse.setTest("reaches");

            response =  new ResponseEntity<>(carParkResponseresponse, HttpStatus.OK);
        }
        catch (Exception e)
        {

            APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to perform client operation");

            response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return response;

    }


}
