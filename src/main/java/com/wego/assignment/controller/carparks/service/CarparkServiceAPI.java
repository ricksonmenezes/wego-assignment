package com.wego.assignment.controller.carparks.service;

import com.wego.assignment.common.APIError;
import com.wego.assignment.common.view.LatLong;
import com.wego.assignment.controller.carparks.exception.CarParkAPIException;
import com.wego.assignment.controller.carparks.exception.CarParkException;
import com.wego.assignment.controller.carparks.view.CarParkAvailabilityResponse;
import com.wego.assignment.controller.carparks.view.CarParkResponse;
import com.wego.assignment.controller.carparks.view.OneMapTokenRequest;
import com.wego.assignment.controller.carparks.view.OneMapToken;
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
public class CarparkServiceAPI {

    @Autowired
    @Qualifier("carparkRestClient")
    RestTemplate restTemplate;

    public OneMapToken getOpenMapToken() throws CarParkAPIException {

        URI ApiUrl = null;
        Map<String, String> urlParams = new HashMap<>();

        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://www.onemap.gov.sg/api/auth/post/getToken");



            HttpHeaders headers = new HttpHeaders();
            OneMapTokenRequest payload = new OneMapTokenRequest("ricksonmenezes@gmail.com", "rIcks@n12345");
            HttpEntity<OneMapTokenRequest> httpEntity = new HttpEntity<>(payload, headers);

            ApiUrl = new URI(builder.buildAndExpand(urlParams).toUri().toString());

            ResponseEntity<OneMapToken> responseEntity = restTemplate.exchange(ApiUrl, HttpMethod.POST, httpEntity, OneMapToken.class);

            return responseEntity.getBody();


        } catch (RestClientException e) {
            throw new CarParkAPIException(e.getMessage(),e);

        } catch (Exception e) {

            throw new CarParkAPIException(e.getMessage(),e);

        }

    }

    public  LatLong getLatLongFromSvy21FromOneMap(double x, double y) throws CarParkAPIException {

        URI ApiUrl = null;
        Map<String, String> urlParams = new HashMap<>();

        try {

            UriComponentsBuilder builder = UriComponentsBuilder.fromUriString("https://www.onemap.gov.sg/api/common/convert/3414to4326")
                    .queryParam("X", x)
                    .queryParam("Y", y);



            HttpHeaders headers = new HttpHeaders();

            OneMapToken oneMapToken = getOpenMapToken();
            headers.add("Authorization", "Bearer " + oneMapToken.getAccessToken());
            HttpEntity<String> httpEntity = new HttpEntity<>(headers);


            ApiUrl = new URI(builder.buildAndExpand(urlParams).toUri().toString());

            ResponseEntity<LatLong> responseEntity = restTemplate.exchange(ApiUrl, HttpMethod.GET, httpEntity, LatLong.class);

            return responseEntity.getBody();


        } catch (RestClientException e) {
            throw new CarParkAPIException(e.getMessage(),e);

        } catch (Exception e) {

            throw new CarParkAPIException(e.getMessage(),e);

        }

    }

    public CarParkAvailabilityResponse getCarParkAvailability() throws CarParkAPIException {

        ResponseEntity<?> response = null;
        HttpEntity<String> httpEntity = new HttpEntity<>(new HttpHeaders());

        try {

            ResponseEntity<CarParkAvailabilityResponse> carparkResponse = restTemplate.exchange("https://api.data.gov.sg/v1/transport/carpark-availability", HttpMethod.GET, httpEntity, CarParkAvailabilityResponse.class);
            return carparkResponse.getBody();

        }
        catch (Exception e)
        {
            throw new CarParkAPIException(e.getMessage(),e);
        }

    }
}
