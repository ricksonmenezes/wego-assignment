package com.wego.assignment.controller.carparks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wego.assignment.common.APIError;
import com.wego.assignment.common.view.LatLong;
import com.wego.assignment.controller.carparks.service.CarParkService;
import com.wego.assignment.controller.carparks.view.CarParkAvailabilityResponse;
import com.wego.assignment.controller.carparks.view.CarParkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value="api/carparks")
public class CarParksRestController extends RestTemplate {

    @Autowired
    @Qualifier("carparkRestClient")
    private RestTemplate restTemplate;

    @Autowired
    CarParkService carParkService;



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


    @RequestMapping(value = "/v1/convertsvy21tolatlong", method = RequestMethod.GET)
    public ResponseEntity<?> convertSVY21ToLatLong(@RequestParam(value ="X") double latitude,
                                                   @RequestParam(value ="Y") double longitude)
    {
        ResponseEntity<?> response = null;
        HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());

        try {

            LatLong latLong = carParkService.convertSVY21ToLatLong(latitude, longitude);

            response =  new ResponseEntity<>(latLong, HttpStatus.OK);
        }
        catch (Exception e)
        {

            APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to perform client operation error" + e.getMessage());

            response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return response;

    }

    //https://www.onemap.gov.sg/api/common/convert/3414to4326?X=34955.3741&Y=39587.9068


}
