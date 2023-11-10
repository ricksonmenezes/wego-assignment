package com.wego.assignment.controller.carparks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wego.assignment.common.APIError;
import com.wego.assignment.controller.carparks.view.CarParkAvailabilityResponse;
import com.wego.assignment.controller.carparks.view.CarParkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="api/carparks")
public class CarParksRestController extends RestTemplate {

    @Autowired
    @Qualifier("carparkRestClient")
    private RestTemplate restTemplate;

    @RequestMapping(value = "/v1/carparkavailability", method = RequestMethod.GET)
    public ResponseEntity<?> testController()
    {
        ResponseEntity<?> response = null;
        HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());
        try {

            ResponseEntity<CarParkAvailabilityResponse> carparkResponse = restTemplate.exchange("https://api.data.gov.sg/v1/transport/carpark-availability", HttpMethod.GET, httpEntity, CarParkAvailabilityResponse.class);

            response =  new ResponseEntity<>(carparkResponse, HttpStatus.OK);
        }
        catch (Exception e)
        {

            APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to perform client operation error" + e.getMessage());

            response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return response;

    }


}
