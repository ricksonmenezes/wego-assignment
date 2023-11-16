package com.wego.assignment.common.resttemplate;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

public class CarParkRestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (
                httpResponse.getStatusCode().series() == CLIENT_ERROR
                        || httpResponse.getStatusCode().series() == SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {

        if (httpResponse.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            throw new CarParkAPIException("Internal Server Error");
        }

        else if (httpResponse.getStatusCode().value() == HttpStatus.SERVICE_UNAVAILABLE.value())
        {
            throw new CarParkAPIException("To Many Concurrent Request");

        }
        else if (httpResponse.getStatusCode().value() == HttpStatus.TOO_MANY_REQUESTS.value())
        {
            throw new CarParkAPIException("To Many Daily Request");

        }else{
            throw new CarParkAPIException("Error while processing request. error code " + httpResponse.getStatusText());
        }

    }
}
