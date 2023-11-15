package com.wego.assignment.controller.carparks.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wego.assignment.common.APIError;
import com.wego.assignment.common.view.LatLong;
import com.wego.assignment.controller.carparks.model.CarPark;
import com.wego.assignment.controller.carparks.service.CarParkService;
import com.wego.assignment.controller.carparks.view.CarParkAvailabilityResponse;
import com.wego.assignment.controller.carparks.view.CarParkResponse;
import com.wego.assignment.controller.carparks.view.NearestCarPark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(value="carparks")
public class CarParksRestController extends RestTemplate {

    @Autowired
    @Qualifier("carparkRestClient")
    private RestTemplate restTemplate;

    @Autowired
    CarParkService carParkService;


    @RequestMapping(value = "/nearest", method = RequestMethod.GET)
    public ResponseEntity<?> nearestCarPark(@RequestParam(value ="latitude") Double latitude,
                                                   @RequestParam(value ="longitude") Double longitude,
                                            @RequestParam(value ="page") int page,
                                            @RequestParam(value ="per_page") int per_page)
    {
        ResponseEntity<?> response = null;
        HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());
        try {


            List<NearestCarPark> nearestCarParkData =  carParkService.nearestPoint(latitude, longitude, page, per_page);

            response =  new ResponseEntity<>(nearestCarParkData, HttpStatus.OK);
        }
        catch (Exception e)
        {

            APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to perform client operation error: " + e.getMessage());

            response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return response;

    }

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
                    "Unable to perform client operation error: " + e.getMessage());

            response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return response;

    }

    @RequestMapping(value = "/v1/synccarparkavailability", method = RequestMethod.GET)
    public ResponseEntity<?> syncCarParkAvailability()
    {
        ResponseEntity<?> response = null;

        try {

            carParkService.syncCarParkAvailability();

            response =  new ResponseEntity<>(null, HttpStatus.OK);
        }
        catch (Exception e)
        {

            APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to perform client operation error: " + e.getMessage());

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
                    "Unable to perform client operation error: " + e.getMessage());

            response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return response;

    }

    @RequestMapping(value = "/v1/carparks", method = RequestMethod.POST)
    public ResponseEntity<?> saveCarParkInfoCSV()
    {
        ResponseEntity<?> response = null;
        HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());

        try {

            carParkService.saveCarParkInfoCSV();

            response =  new ResponseEntity<>(null, HttpStatus.OK);
        }
        catch (Exception e)
        {

            APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to perform client operation error: " + e.getMessage());

            response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return response;

    }

    @RequestMapping(value = "/v1/updatecarparkslatlong", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCarParkToLatLong()
    {
        ResponseEntity<?> response = null;
        HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());

        try {

            carParkService.saveCarParkInfoCSV();

            response =  new ResponseEntity<>(null, HttpStatus.OK);
        }
        catch (Exception e)
        {

            APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to perform client operation error: " + e.getMessage());

            response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);


        }

        return response;

    }

    @RequestMapping(value = "/v1/carparks", method = RequestMethod.GET)
    public ResponseEntity<?> readCarParkInfoCSV()
    {
        ResponseEntity<?> response = null;
        HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());

        try {

            List<CarPark> carParList = carParkService.getAllCarParks();

            response =  new ResponseEntity<>(carParList, HttpStatus.OK);
        }
        catch (Exception e)
        {

            APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Unable to perform client operation error: " + e.getMessage());

            response =  new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return response;

    }


}
