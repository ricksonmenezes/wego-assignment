package com.wego.assignment.controller.carparks.service;

import com.wego.assignment.common.view.LatLong;
import com.wego.assignment.controller.carparks.exception.CarParkAPIException;
import com.wego.assignment.controller.carparks.exception.CarParkException;
import com.wego.assignment.controller.carparks.view.CarParkAvailabilityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
public class CarParkService {

    @Autowired
    @Qualifier("carparkRestClient")
    private RestTemplate restTemplate;


    public LatLong convertSVY21ToLatLong(double x, double y) throws CarParkException {

        URI ApiUrl = null;
        Map<String, String> urlParams = new HashMap<>();

        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://www.onemap.gov.sg/api/common/convert/3414to4326")
                    .queryParam("X", x)
                    .queryParam("Y", y);



            HttpHeaders headers = new HttpHeaders();
            headers.add("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlYjZhMmJmOTRmNjA0Y2YxMDBlMzRhMDUyMGM5ODc0ZCIsImlzcyI6Imh0dHA6Ly9pbnRlcm5hbC1hbGItb20tcHJkZXppdC1pdC0xMjIzNjk4OTkyLmFwLXNvdXRoZWFzdC0xLmVsYi5hbWF6b25hd3MuY29tL2FwaS92Mi91c2VyL3Bhc3N3b3JkIiwiaWF0IjoxNjk5NjY5MDAyLCJleHAiOjE2OTk5MjgyMDIsIm5iZiI6MTY5OTY2OTAwMiwianRpIjoiZG1EREZhRHY4WUFndTQ0RiIsInVzZXJfaWQiOjE1ODEsImZvcmV2ZXIiOmZhbHNlfQ.rlb1ocKu6d8_IAOwHyU22eQ6CZr7yzuUVfGGfi4Z2wY");
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);


            ApiUrl = new URI(builder.buildAndExpand(urlParams).toUri().toString());

            ResponseEntity<LatLong> responseEntity = restTemplate.exchange(ApiUrl, HttpMethod.GET, httpEntity, LatLong.class);

            return responseEntity.getBody();


        } catch (RestClientException e) {
            throw new CarParkException(e.getMessage(),e);

        } catch (Exception e) {

            throw new CarParkException(e.getMessage(),e);

        }


    }
}
